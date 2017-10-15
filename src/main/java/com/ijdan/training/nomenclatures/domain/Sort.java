package com.ijdan.training.nomenclatures.domain;

import java.util.List;

public class Sort {
    private String enabled;
    private List<String> fields;
    private List<String> sens;

    public Sort() {

    }

    public List<String> getFields() {
        return fields;
    }
    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getSens() {
        return sens;
    }
    public void setSens(List<String> sens) {
        this.sens = sens;
    }

    public String getEnabled() {
        return enabled;
    }
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return  "fields : <"+ this.getFields().toString() +">" +
                "sens : <"+ this.getSens().toString() +">";
    }
}

