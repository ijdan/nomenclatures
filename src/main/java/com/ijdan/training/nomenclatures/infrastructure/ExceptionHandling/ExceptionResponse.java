package com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

/**
 * Mon POJO pour les exceptions
 * */

public class ExceptionResponse {

    private Timestamp timestamp;
    private UUID uuid;
    private String code;
    private String message;

    public ExceptionResponse() {
        this.uuid = UUID.randomUUID();
        this.timestamp = Timestamp.from(Instant.now());
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUuid() {
        this.uuid = UUID.randomUUID();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
