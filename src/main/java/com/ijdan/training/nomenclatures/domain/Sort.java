package com.ijdan.training.nomenclatures.domain;

import java.util.List;

class Sort {
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

    /**
     * Fixe l'attibut sur lequel appliquer le tri
     * */
    public String getSortField (String sortFields){
        if ( sortFields !=  null && !sortFields.isEmpty() && this.getFields().contains(sortFields) ){
            return sortFields;
        }else {
            return this.getFields().get(0);
        }
    }

    /**
     * Fixe le sens du tri
     * */
    public String getSortSens (String sortSens){
        if ( sortSens !=  null && !sortSens.isEmpty() && this.getSens().contains(sortSens) ){
            return sortSens;
        }else {
            return this.getSens().get(0);
        }

    }

    @Override
    public String toString() {
        return  "fields : <"+ this.getFields().toString() +">" +
                "sens : <"+ this.getSens().toString() +">";
    }
}

