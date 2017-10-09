package com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling;

public class BadRequestException extends RuntimeException {
    String code;

    public BadRequestException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
