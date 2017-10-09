package com.ijdan.training.nomenclatures.domain;

import java.util.HashMap;
import java.util.List;

public class Structure {
    private String resource;
    private String table;
    private String id;
    private HashMap<String, String> output = new HashMap<String, String>();
    private Paging paging;
    private Sort sort;
    private Filters filters;
    private List<Clause> clause;

    public HashMap<String, String> getOutput() {
        return output;
    }

    public void setOutput(HashMap<String, String> output) {
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

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    public List<Clause> getClause() {
        return clause;
    }

    public void setClause(List<Clause> clause) {
        this.clause = clause;
    }

    public Structure() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

}