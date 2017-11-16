package com.ijdan.training.nomenclatures.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrepareResponse {

    private Nomenclature nomenclature;
    public void setNomenclature(Nomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public HashMap<String, Object> selectAll(List<String> selectedFields, String sortField, String sortSens, String paginPacket, String offset){
        /**
         * Validation des RequestParam
         * */
        //Validation des attributs souhaités en sorite
        selectedFields = nomenclature.getOutputKeys(selectedFields);
        //Validation l'attribut sur lequel le tri est permis
        sortField = nomenclature.getSortField (sortField);
        //Validation du sens du tri
        sortSens = nomenclature.getSortSens (sortSens);
        //Validation du nombre d'éléments par appels souhaité par l'appelant
        paginPacket = nomenclature.getPagingPacket (paginPacket);
        //Validation de l'offset : le positionnement du paquet à retourner
        offset = nomenclature.getOffset (offset);

        String query = this.getQuerySelectAll(selectedFields, sortField, sortSens, paginPacket, offset);

        HashMap response = new HashMap<String, Object>();

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