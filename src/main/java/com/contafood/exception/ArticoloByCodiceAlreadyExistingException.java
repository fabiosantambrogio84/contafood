package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ArticoloByCodiceAlreadyExistingException extends RuntimeException {

    public ArticoloByCodiceAlreadyExistingException(String codice) {
        super("L'articolo con codice "+codice+" è già presente");
    }
}
