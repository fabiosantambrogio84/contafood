package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class BarcodeMaskParsingException extends RuntimeException {

    public BarcodeMaskParsingException(String message) {
        super(message);
    }
}
