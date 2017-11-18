package com.ijdan.training.nomenclatures.domain;

class Clause {
    private String name;
    private String[] values;

    public Clause() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String[] getValues() {
        return values;
    }
    public void setValues(String[] values) {
        this.values = values;
    }

}