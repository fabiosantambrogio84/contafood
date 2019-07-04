package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class CannotChangeResourceIdException extends RuntimeException {

    public CannotChangeResourceIdException() {
        super("cannot change the resource id on PUT");
    }
}
