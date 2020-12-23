package com.contafood.exception;

import com.contafood.util.enumeration.Resource;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ZipNoResourceFoundException extends RuntimeException {

    public ZipNoResourceFoundException(Resource resource) {
        super(String.format("Nessuna %s presente nelle date selezionate", resource.getLabel()));
    }

}
