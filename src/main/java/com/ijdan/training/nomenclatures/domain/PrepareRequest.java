package com.ijdan.training.nomenclatures.domain;

import java.util.ArrayList;
import java.util.List;

public class PrepareRequest {
    private String request;

    public PrepareRequest(Nomenclature nomenclature,
                          List<String> selectedFields,
                          String sortField,
                          String sortSens,
                          String paginPacket,
                          String offset) {

        List<String> request = new ArrayList<String>();

        List<String> selectedColumn = new ArrayList<String>();
        for(String sf : selectedFields){
            selectedColumn.add(nomenclature.getOutput().get(sf) + " AS " + sf);
        }
        request.add("SELECT " + String.join(", ", selectedColumn));

        request.add("FROM "+ nomenclature.getDbTable());

        request.add("WHERE 1=1");
        List<Clause> clauses = nomenclature.getClause();
        for (Clause clause : clauses) {
            if (clause.getValues().length > 1) {
                request.add("AND " + clause.getName() + " IN ('"+ String.join("', '", clause.getValues()) +"')");
            }else {
                request.add("AND " + clause.getName() + "='" + String.join("', '", clause.getValues()) + "'");
            }

        }

        request.add("ORDER BY "+ sortField + " " + sortSens);

        request.add("LIMIT " + offset + ", " + paginPacket);

        this.setRequest(String.join("\n", request));
    }

    public String getPreparedRequest(){
        return this.request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
