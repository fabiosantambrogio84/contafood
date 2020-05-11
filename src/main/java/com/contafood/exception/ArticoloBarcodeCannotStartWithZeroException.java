package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ArticoloBarcodeCannotStartWithZeroException extends RuntimeException {

    public ArticoloBarcodeCannotStartWithZeroException() {
        super("Il barcode dell'articolo non può iniziare con 0");
    }
}
