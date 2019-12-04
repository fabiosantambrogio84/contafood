package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ListinoBaseCannotHaveVariazionePrezzoException extends RuntimeException {

    public ListinoBaseCannotHaveVariazionePrezzoException() {
        super("the listino with type 'BASE' cannot have set 'variazionePrezzo'");
    }
}
