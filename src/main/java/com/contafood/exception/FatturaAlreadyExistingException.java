package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class FatturaAlreadyExistingException extends RuntimeException {

    public FatturaAlreadyExistingException(Integer anno, Integer progressivo) {
        super("Esiste già una fattura con progressivo "+progressivo+" e anno "+anno);
    }
}
