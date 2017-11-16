package com.ijdan.training.nomenclatures.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class Cache {
    private String enabled;
    private String expiration;

    public Cache() {
    }

    public String getEnabled() {
        return enabled;
    }
    @Autowired
    public Boolean isEnabled() {
        return this.getEnabled().equals("1");
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @Autowired
    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
