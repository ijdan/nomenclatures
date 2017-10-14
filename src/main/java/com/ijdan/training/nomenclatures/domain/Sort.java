package com.ijdan.training.nomenclatures.domain;

public class Sort {
    private String[] fields;
    private String[] sens;

    public Sort() {

    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public String[] getSens() {
        return sens;
    }

    public void setSens(String[] sens) {
        this.sens = sens;
    }

    @Override
    public String toString() {
        return  "fields : <"+ this.getFields().toString() +">" +
                "sens : <"+ this.getSens().toString() +">";
    }
}

