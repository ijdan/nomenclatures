package com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> resourceNotFound(ResourceNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setCode(HttpStatus.BAD_REQUEST.name() +"."+ ex.getCode());
        response.setMessage(ex.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequest (BadRequestException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setCode(HttpStatus.BAD_REQUEST.name() +"."+  ex.getCode());
        response.setMessage(ex.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<ExceptionResponse> internalError (InternalErrorException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.name() +"."+  ex.getCode());
        response.setMessage(ex.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}