package com.ijdan.training.nomenclatures.domain;

import java.util.HashMap;
import java.util.List;

public class Nomenclature {
    private String resourceName;
    private String dbTable;
    private HashMap<String, String> output = new HashMap<>();
    private String paging;
    private Sort sort;
    private String enabledFieldsSelection;
    private List<Clause> clause;

    public Nomenclature() {
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDbTable() {
        return dbTable;
    }

    public void setDbTable(String dbTable) {
        this.dbTable = dbTable;
    }

    public HashMap<String, String> getOutput() {
        return output;
    }

    public void setOutput(HashMap<String, String> output) {
        this.output = output;
    }

    public String getPaging() {
        return paging;
    }

    public void setPaging(String paging) {
        this.paging = paging;
    }
    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public String getEnabledFieldsSelection() {
        return enabledFieldsSelection;
    }

    public void setEnabledFieldsSelection(String enabledFieldsSelection) {
        this.enabledFieldsSelection = enabledFieldsSelection;
    }

    public List<Clause> getClause() {
        return clause;
    }

    public void setClause(List<Clause> clause) {
        this.clause = clause;
    }

    @Override
    public String toString() {
        return  "resourceName : <"+ this.getResourceName() +">" +
                "dbTable : <"+ this.getDbTable() +">" +
                "output : <"+ this.getOutput().toString() +">" +
                "sort : <"+ this.getSort().toString() +">" +
                "paging : <"+ this.getPaging() +">" +
                "resourceName : <"+ resourceName +">" +
                "enabledFieldsSelection : <"+ this.getEnabledFieldsSelection() +">";
    }


}