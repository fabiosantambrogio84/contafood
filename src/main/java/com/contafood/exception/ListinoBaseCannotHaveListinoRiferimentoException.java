package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ListinoBaseCannotHaveListinoRiferimentoException extends RuntimeException {

    public ListinoBaseCannotHaveListinoRiferimentoException() {
        super("the listino with type 'BASE' cannot have set 'listinoRiferimento'");
    }
}
