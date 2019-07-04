package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Unable to retrieve requested resource");
    }

}
