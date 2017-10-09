package com.ijdan.training.nomenclatures.domain;

import javax.swing.plaf.PanelUI;
import java.util.HashMap;

public class ErrorMessage {
    private String error;
    private String message;
    private String description = "";

    public ErrorMessage(String error, String message, String description) {
        this.error = error;
        this.message = message;
        this.description = description;
    }

    public ErrorMessage(String error, String message) {
        setError(error);;
        setMessage(message);
        setDescription(description);
    }

    public HashMap toHashMap (){
        HashMap hashmap = new HashMap();
        hashmap.put("error", getError());
        hashmap.put("message", getMessage());
        hashmap.put("descripton", getDescription());
        return hashmap;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
