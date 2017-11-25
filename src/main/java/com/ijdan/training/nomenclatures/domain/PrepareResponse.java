package com.ijdan.training.nomenclatures.domain;

import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.InternalErrorException;
import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.ResourceNotFoundException;
import com.ijdan.training.nomenclatures.repository.DatabasesProperties;
import com.ijdan.training.nomenclatures.repository.H2Connection;
import com.ijdan.training.nomenclatures.repository.H2Repository;
import com.ijdan.training.nomenclatures.repository.IRepository;
import com.ijdan.training.nomenclatures.response.CsvResponse;
import com.ijdan.training.nomenclatures.response.IFormatter;
import com.ijdan.training.nomenclatures.response.JsonResponse;
import com.ijdan.training.nomenclatures.response.XmlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class PrepareResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrepareResponse.class);
    private static List<String> clausesPart = new ArrayList<>();

    @Autowired
    private H2Connection h2connection;

    @Autowired
    DatabasesProperties databasesProperties;

    private Nomenclature nomenclatureConfig;

    public PrepareResponse(DatabasesProperties databasesProperties) {
        this.databasesProperties = databasesProperties;
    }

    public List<Map> getCollection(Nomenclature nomenclatureConfig, List<String> selectedFields, String sortField, String sortSens, String paginPacket, String offset){
        this.nomenclatureConfig = nomenclatureConfig;
        /**
         * Validation des RequestParam
         * Evite les éventuelles injections SQL
         * */
        //Validation des attributs souhaités en sorite
        selectedFields = nomenclatureConfig.getOutputKeys(selectedFields);
        HashMap selectedColumn = new HashMap<String, String>();
        for(String sf : selectedFields){
            selectedColumn.put(this.nomenclatureConfig.getOutput().get(sf), sf);
        }

        //Validation l'attribut sur lequel le tri est permis
        sortField = nomenclatureConfig.getSort().getSortField (sortField);
        //Validation du sens du tri
        sortSens = nomenclatureConfig.getSort().getSortSens (sortSens);
        //Validation du nombre d'éléments par appels souhaité par l'appelant
        paginPacket = nomenclatureConfig.getPaging().getPagingPacket (paginPacket);
        //Validation de l'offset : le positionnement du paquet à retourner
        offset = nomenclatureConfig.getOffset (offset);


        try {
            h2connection.createStatement(nomenclatureConfig.getCache());
            /*
             if (nomenclatureConfig.getSummary().isEnabled()){
                response.put(nomenclatureConfig.getSummary().getNbElementsAttributeName(), String.valueOf(values.size()));

                rs = h2connection.executeQuery( this.getQueryTotal() );
                if (rs.next()) {
                    response.put(nomenclatureConfig.getSummary().getTotalAttributeName(), rs.getString("TOTAL"));
                }
                */
        }catch (SQLException e){
            LOGGER.error("Erreur d'exécution de la requête : ");
            LOGGER.error("e >>" + e.getMessage());
            throw new ResourceNotFoundException("Err.00003", "!! Nomenclature inexistante !!");
        }

        IRepository repository;
        repository = new H2Repository(databasesProperties); //Selon les cas. A voir plus tard.

        return repository.findAllItems(nomenclatureConfig, selectedColumn, sortField, sortSens, paginPacket, offset);
    }

    public HashMap<String, List<Map>> getItem (Nomenclature nomenclatureConfig, String id, List<String> selectedFields) {
        /**
         * Validation des RequestParam
         * */
        //Validation des attributs souhaités en sorite
        selectedFields = nomenclatureConfig.getOutputKeys(selectedFields);

        this.nomenclatureConfig = nomenclatureConfig;
        HashMap response = new HashMap<String, Object>();
        String query = this.getQueryItem(id, selectedFields);

        try {
            h2connection.createStatement(nomenclatureConfig.getCache());
            ResultSet rs = h2connection.executeQuery(query);
            List<Map> values = this.getListMap(rs, selectedFields);
            response.put(nomenclatureConfig.getResourceName(), values);
        }catch (SQLException e){
            LOGGER.error("Erreur d'exécution de la requête : " + query);
            LOGGER.error("e >>" + e.getMessage());
            throw new InternalErrorException("Err.00003", "!! Erreur d'exécution de la requête !!");
        }

        return response;
    }

    public ResponseEntity<String> getAdaptedContentType(HashMap<String, Object> response,  HttpServletRequest httpRequest){
        HttpHeaders httpHeaders= new HttpHeaders();
        IFormatter output;

        switch (httpRequest.getHeader("accept")){
            case MediaType.APPLICATION_XML_VALUE:
                httpHeaders.setContentType(MediaType.APPLICATION_XML);
                output = new XmlResponse();
                break;

            case MediaType.TEXT_PLAIN_VALUE:
            case "text/csv":
                httpHeaders.setContentType(MediaType.TEXT_PLAIN);
                output = new CsvResponse();
                break;

            default:
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                output = new JsonResponse();
        }

        return new ResponseEntity<>(output.transform(response), httpHeaders, HttpStatus.OK);
    }

    public String getQueryTotal() {
        List<String> request = new ArrayList<>();

        request.add("SELECT COUNT(*) as TOTAL");

        request.add("FROM "+ this.nomenclatureConfig.getDbTable());

        request.add(String.join(" ", this.getClausesPart()));

        return String.join(" ", request);
    }

    public List<String> getClausesPart() {
        if (clausesPart.size() == 0){
            List<Clause> clauses = nomenclatureConfig.getClause();
            clausesPart.add("WHERE 1=1");
            for (Clause c : clauses) {
                clausesPart.add("AND " + c.getName() + " IN ('"+ String.join("', '", c.getValues()) +"')");
            }
        }

        return this.clausesPart;
    }

    public String getQueryCollection (List<String> selectedFields, String sortField, String sortSens, String paginPacket, String offset){
        List<String> request = new ArrayList<>();

        List<String> selectedColumn = new ArrayList<>();
        for(String sf : selectedFields){
            selectedColumn.add(this.nomenclatureConfig.getOutput().get(sf) + " AS " + sf);
        }
        request.add("SELECT " + String.join(", ", selectedColumn));

        request.add("FROM "+ this.nomenclatureConfig.getDbTable());

        request.add(String.join(" ", this.getClausesPart()));

        request.add("ORDER BY "+ this.nomenclatureConfig.getOutput().get(sortField) + " " + sortSens);

        if(this.nomenclatureConfig.getPaging().isEnabled()){
            request.add("LIMIT " + offset + ", " + paginPacket);
        }

        return String.join(" ", request);
    }

    public String getQueryItem(String id, List<String> selectedFields){
        List<String> request = new ArrayList<>();

        List<String> selectedColumn = new ArrayList<>();
        for(String sf : selectedFields){
            selectedColumn.add(this.nomenclatureConfig.getOutput().get(sf) + " AS " + sf);
        }
        request.add("SELECT " + String.join(", ", selectedColumn));

        request.add("FROM "+ this.nomenclatureConfig.getDbTable());

        request.add(String.join(" ", this.getClausesPart()));
        request.add("AND "+ nomenclatureConfig.getPk() +"='"+ id +"'");

        return String.join(" ", request);
    }

    public List<Map> getListMap (ResultSet rs, List<String> selectedFields) throws SQLException {
        List<Map> items = new ArrayList<>();
        while (rs.next()) {
            Map item = new HashMap<String, String>();
            for (String selectedField : selectedFields) {
                item.put(selectedField, rs.getString(selectedField));
            }
            items.add(item);
        }

        return items;
    }


}