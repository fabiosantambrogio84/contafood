package com.contafood.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class DdtPagamentoExceedingException extends RuntimeException {

    public DdtPagamentoExceedingException() {
        super("L'importo del pagamento, sommato agli importi già pagati, supera il totale del DDT");
    }
}
