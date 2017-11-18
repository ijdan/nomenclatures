package com.ijdan.training.nomenclatures.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Nomenclature {
    private String resourceName;
    private String enbled;
    private String dbTable;
    private String pk;
    private Map<String, String> output = new HashMap<>();
    private Paging paging;
    private Sort sort;
    private String enabledFieldsSelection;
    private List<Clause> clause;
    private Cache cache;
    private Summary summary;
    private List<String> produces;

    public Nomenclature() {}

    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getEnbled() {
        return enbled;
    }

    public void setEnbled(String enbled) {
        this.enbled = enbled;
    }
    public boolean isEnabled(){
        return this.getEnbled().equals("1");
    }

    public String getDbTable() {
        return dbTable;
    }
    public void setDbTable(String dbTable) {
        this.dbTable = dbTable;
    }

    public String getPk() {
        return pk;
    }
    public void setPk(String pk) {
        this.pk = pk;
    }

    public Map<String, String> getOutput() {
        return output;
    }
    public void setOutput(Map<String, String> output) {
        this.output = output;
    }

    public Paging getPaging() {
        return paging;
    }
    public void setPaging(Paging paging) {
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

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    /**
     * Fixe les attributs de la ressource à restituer
     * */
    public List<String> getOutputKeys (List<String> selectedFields){
        List<String> fields = new ArrayList<>();
        List<String> outputKeys = new ArrayList<>();

        for (Map.Entry<String, String> key : this.getOutput().entrySet()) {
            fields.add( key.getKey() );

            if (selectedFields != null && selectedFields.contains(key.getKey())) {
                outputKeys.add( key.getKey() );
            }
        }
        if (outputKeys.size() > 0){
            return outputKeys;
        }else {
            return fields;
        }
    }


    /**
     * Fixe le positionnement du paquet à demander
     * */
    public String getOffset(String offset) {
        try{
            return Integer.valueOf(offset).toString();
        }catch (NumberFormatException e){
            return "0";
        }
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }


}