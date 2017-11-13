package com.ijdan.training.nomenclatures.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PrepareRequest {
    private Nomenclature nomenclature;
    private List<String> clause;

    public PrepareRequest(Nomenclature nomenclature) {
        this.nomenclature = nomenclature;
        List<String> request = new ArrayList<String>();
        request.add("WHERE 1=1");

        List<Clause> clauses = nomenclature.getClause();
        for (Clause clause : clauses) {
            if (clause.getValues().length > 1) {
                request.add("AND " + clause.getName() + " IN ('"+ String.join("', '", clause.getValues()) +"')");
            }else {
                request.add("AND " + clause.getName() + "='" + String.join("', '", clause.getValues()) + "'");
            }
        }
        this.clause = request;
    }

    public String getTotalRequest() {
        List<String> request = new ArrayList<String>();

        request.add("SELECT COUNT(*) as TOTAL");

        request.add("FROM "+ this.nomenclature.getDbTable());

        request.add(String.join("\n", this.getClause()));

        return String.join("\n", request);
    }

    public List<String> getClause() {
        return clause;
    }
    public void setClause(List<String> clause) {
        this.clause = clause;
    }

    public String getCallRequest (List<String> selectedFields, String sortField, String sortSens, String paginPacket, String offset){
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
