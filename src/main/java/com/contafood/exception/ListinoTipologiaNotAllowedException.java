package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ListinoTipologiaNotAllowedException extends RuntimeException {

    public ListinoTipologiaNotAllowedException() {
        super("the specified tipologia of listino is not allowed");
    }
}
