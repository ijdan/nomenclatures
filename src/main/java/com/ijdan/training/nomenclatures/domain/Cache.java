package com.ijdan.training.nomenclatures.domain;

public class Cache {
    private String enabled;
    private String expiration;

    public Cache() {
    }

    public String getEnabled() {
        return enabled;
    }
    public Boolean isEnabled() {
        return this.getEnabled().equals("1");
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
