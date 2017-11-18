package com.ijdan.training.nomenclatures.domain;

import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.InternalErrorException;
import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.ResourceNotFoundException;
import com.ijdan.training.nomenclatures.repository.H2Connection;
import org.json.simple.JSONObject;
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

import static org.springframework.http.MediaType.TEXT_PLAIN;

@Component
public class PrepareResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrepareResponse.class);
    private static List<String> clausesPart = new ArrayList<>();

    @Autowired
    private H2Connection h2connection;

    private Nomenclature nomenclatureConfig;

    public HashMap<String, Object> getCollection(Nomenclature nomenclatureConfig, List<String> selectedFields, String sortField, String sortSens, String paginPacket, String offset){
        this.nomenclatureConfig = nomenclatureConfig;
        /**
         * Validation des RequestParam
         * */
        //Validation des attributs souhaités en sorite
        selectedFields = nomenclatureConfig.getOutputKeys(selectedFields);
        //Validation l'attribut sur lequel le tri est permis
        sortField = nomenclatureConfig.getSortField (sortField);
        //Validation du sens du tri
        sortSens = nomenclatureConfig.getSortSens (sortSens);
        //Validation du nombre d'éléments par appels souhaité par l'appelant
        paginPacket = nomenclatureConfig.getPagingPacket (paginPacket);
        //Validation de l'offset : le positionnement du paquet à retourner
        offset = nomenclatureConfig.getOffset (offset);

        HashMap response = new HashMap<String, Object>();
        String query = this.getQueryCollection(selectedFields, sortField, sortSens, paginPacket, offset);
        LOGGER.warn("quert = "+ query);
        try {
            h2connection.createStatement(nomenclatureConfig.getCache());
            ResultSet rs = h2connection.executeQuery(query);
            List<Map> values = nomenclatureConfig.getListMap(rs, selectedFields);
            response.put(nomenclatureConfig.getResourceName(), values);

            if (nomenclatureConfig.getSummary().isEnabled()){
                response.put(nomenclatureConfig.getSummary().getNbElementsAttributeName(), String.valueOf(values.size()));

                rs = h2connection.executeQuery( this.getQueryTotal() );
                if (rs.next()) {
                    response.put(nomenclatureConfig.getSummary().getTotalAttributeName(), rs.getString("TOTAL"));
                }
            }
        }catch (SQLException e){
            LOGGER.error("Erreur d'exécution de la requête : " + query.toString());
            throw new ResourceNotFoundException("Err.00003", "!! Nomenclature inexistante !!");
        }

        return response;
    }

    public HashMap<String, Object> getItem (Nomenclature nomenclatureConfig, String id, List<String> selectedFields) {
        /**
         * Validation des RequestParam
         * */
        //Validation des attributs souhaités en sorite
        selectedFields = nomenclatureConfig.getOutputKeys(selectedFields);

        this.nomenclatureConfig = nomenclatureConfig;
        HashMap response = new HashMap<String, Object>();
        String query = this.getQueryItem(id, selectedFields);
        LOGGER.warn("query = "+ query);

        try {
            h2connection.createStatement(nomenclatureConfig.getCache());
            ResultSet rs = h2connection.executeQuery(query);
            List<Map> values = nomenclatureConfig.getListMap(rs, selectedFields);
            response.put(nomenclatureConfig.getResourceName(), values);
        }catch (SQLException e){
            LOGGER.error("Erreur d'exécution de la requête : " + query.toString());
            throw new InternalErrorException("Err.00003", "!! Erreur d'exécution de la requête !!");
        }

        return response;
    }

    public ResponseEntity<String> adaptContentType(HashMap<String, Object> response,  HttpServletRequest httpRequest){
        HttpHeaders httpHeaders= new HttpHeaders();

        switch (httpRequest.getHeader("accept")){
            case MediaType.APPLICATION_XML_VALUE:
                httpHeaders.setContentType(MediaType.APPLICATION_XML);
                return new ResponseEntity<String>((new Response(response)).getXMLResponse(), httpHeaders, HttpStatus.OK);

            case MediaType.TEXT_PLAIN_VALUE:
                httpHeaders.setContentType(TEXT_PLAIN);
                return new ResponseEntity<String>(new Response(response).getCSVResponse(), httpHeaders, HttpStatus.OK);

            default:
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<String>(JSONObject.toJSONString(response), httpHeaders, HttpStatus.OK);
        }

    }


    public String getQueryTotal() {
        List<String> request = new ArrayList<String>();

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
        List<String> request = new ArrayList<String>();

        List<String> selectedColumn = new ArrayList<String>();
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
        List<String> request = new ArrayList<String>();

        List<String> selectedColumn = new ArrayList<String>();
        for(String sf : selectedFields){
            selectedColumn.add(this.nomenclatureConfig.getOutput().get(sf) + " AS " + sf);
        }
        request.add("SELECT " + String.join(", ", selectedColumn));

        request.add("FROM "+ this.nomenclatureConfig.getDbTable());

        request.add(String.join(" ", this.getClausesPart()));
        request.add("AND "+ nomenclatureConfig.getPk() +"='"+ id +"'");

        return String.join(" ", request);
    }

}