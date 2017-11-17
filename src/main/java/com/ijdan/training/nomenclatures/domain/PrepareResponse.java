package com.ijdan.training.nomenclatures.domain;

import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.ResourceNotFoundException;
import com.ijdan.training.nomenclatures.repository.H2Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class PrepareResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrepareResponse.class);

    @Autowired
    private H2Connection h2connection;

    private Nomenclature nomenclature;

    public HashMap<String, Object> get(Nomenclature nomenclature, HttpServletRequest httpRequest){
        this.nomenclature = nomenclature;
        /**
         * Validation des RequestParam
         * */
        //Validation des attributs souhaités en sorite
        List<String> selectedFields = new ArrayList<String>(Arrays.asList(httpRequest.getParameter("selectedFields").split(",")));
        selectedFields = nomenclature.getOutputKeys(selectedFields);

        //Validation l'attribut sur lequel le tri est permis
        String sortField = nomenclature.getSortField (httpRequest.getParameter("sortField"));

        //Validation du sens du tri
        String sortSens = nomenclature.getSortSens (httpRequest.getParameter("sortField"));

        //Validation du nombre d'éléments par appels souhaité par l'appelant
        String paginPacket = nomenclature.getPagingPacket (httpRequest.getParameter("paginPacket"));

        //Validation de l'offset : le positionnement du paquet à retourner
        String offset = nomenclature.getOffset (httpRequest.getParameter("offset"));

        String query = this.getQuerySelectAll(selectedFields, sortField, sortSens, paginPacket, offset);

        HashMap response = new HashMap<String, Object>();

        try {
            h2connection.createStatement(nomenclature.getCache());
            ResultSet rs = h2connection.executeQuery(query);
            List<Map> values = nomenclature.getListMap(rs, selectedFields);
            response.put(nomenclature.getResourceName(), values);

            if (nomenclature.getSummary().isEnabled()){
                response.put(nomenclature.getSummary().getNbElementsAttributeName(), String.valueOf(values.size()));
/*
                rs = h2connection.executeQuery( prepareRequest.getTotalRequest() );
                if (rs.next()) {
                    response.put(nomenclature.getSummary().getTotalAttributeName(), rs.getString("TOTAL"));
                }
                */
            }

        }catch (SQLException e){
            throw new ResourceNotFoundException("Err.00003", "!! Nomenclature inexistante !!");
        }

        return response;
    }


    public String getQueryTotal() {
        List<String> request = new ArrayList<String>();

        request.add("SELECT COUNT(*) as TOTAL");
        request.add("FROM "+ this.nomenclature.getDbTable());
        request.add(String.join("\n", this.getClause()));

        return String.join("\n", request);
    }

    public List<String> getClause() {

        List<String> request = new ArrayList<String>();
        request.add("WHERE 1=1");

        List<Clause> clauses = nomenclature.getClause();
        for (Clause c : clauses) {
            if (c.getValues().length > 1) {
                request.add("AND " + c.getName() + " IN ('"+ String.join("', '", c.getValues()) +"')");
            }else {
                request.add("AND " + c.getName() + "='" + String.join("', '", c.getValues()) + "'");
            }
        }

        return request;
    }


    public String getQuerySelectAll (List<String> selectedFields, String sortField, String sortSens, String paginPacket, String offset){
        List<String> request = new ArrayList<String>();

        List<String> selectedColumn = new ArrayList<String>();
        for(String sf : selectedFields){
            selectedColumn.add(this.nomenclature.getOutput().get(sf) + " AS " + sf);
        }
        request.add("SELECT " + String.join(", ", selectedColumn));
        request.add("FROM "+ this.nomenclature.getDbTable());
        request.add(String.join("\n", this.getClause()));
        request.add("ORDER BY "+ this.nomenclature.getOutput().get(sortField) + " " + sortSens);

        if(this.nomenclature.getPaging().isEnabled()){
            request.add("LIMIT " + offset + ", " + paginPacket);
        }

        return String.join("\n", request);
    }

}