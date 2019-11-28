package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ListinoBaseAlreadyExistingException extends RuntimeException {

    public ListinoBaseAlreadyExistingException() {
        super("a listino with type 'BASE' already exists");
    }
}
