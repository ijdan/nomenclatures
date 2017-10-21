package com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling;

import org.json.simple.JSONObject;

public class InternalErrorException extends RuntimeException {
    String code;

    public InternalErrorException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
