package com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling;

public class ResourceNotFoundException extends RuntimeException {
    String code;

    public ResourceNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
