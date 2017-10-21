package com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling;

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
