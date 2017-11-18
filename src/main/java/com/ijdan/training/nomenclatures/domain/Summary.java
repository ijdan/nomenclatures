package com.ijdan.training.nomenclatures.domain;

class Summary {
    private String enabled;
    private String nbElementsAttributeName;
    private String totalAttributeName;

    private String getEnabled() {
        return enabled;
    }

    public Boolean isEnabled() {
        return this.getEnabled().equals("1");
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getNbElementsAttributeName() {
        return nbElementsAttributeName;
    }

    public void setNbElementsAttributeName(String nbElementsAttributeName) {
        this.nbElementsAttributeName = nbElementsAttributeName;
    }

    public String getTotalAttributeName() {
        return totalAttributeName;
    }

    public void setTotalAttributeName(String totalAttributeName) {
        this.totalAttributeName = totalAttributeName;
    }
}
