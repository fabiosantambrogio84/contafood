package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ListinoAssociatoAlreadyExistingException extends RuntimeException {

    public ListinoAssociatoAlreadyExistingException(String fornitore, String listino) {
        super("Il listino '"+listino+"' è già associato al fornitore '"+fornitore+"'");
    }
}
