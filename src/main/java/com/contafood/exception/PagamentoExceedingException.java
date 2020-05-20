package com.contafood.exception;

import com.contafood.util.Resource;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class PagamentoExceedingException extends RuntimeException {

    public PagamentoExceedingException(Resource resource) {
        super("L'importo del pagamento, sommato agli importi già pagati, supera il totale "+(resource.equals(Resource.DDT)?"del ":"della ")+resource.getLabel());
    }
}
