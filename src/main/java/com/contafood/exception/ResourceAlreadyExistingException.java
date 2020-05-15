package com.contafood.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ResourceAlreadyExistingException extends RuntimeException {

    public ResourceAlreadyExistingException(String resource, Integer anno, Integer progressivo) {
        super(String.format("%s già presente con progressivo %d e anno %d", StringUtils.capitalize(resource), progressivo, anno));
    }

}
