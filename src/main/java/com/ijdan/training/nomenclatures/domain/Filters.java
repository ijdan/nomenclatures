package com.ijdan.training.nomenclatures.domain;

public class Filters {

    private String enable;
    private String[] fields;

    public Filters() {
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }
}
