package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class DdtAlreadyExistingException extends RuntimeException {

    public DdtAlreadyExistingException(Integer annoContabile, Integer progressivo) {
        super("Esiste già un DDT con progressivo "+progressivo+" e anno contabile "+annoContabile);
    }
}
