package com.contafood.exception;

import com.contafood.util.enumeration.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ResourceAlreadyExistingException extends RuntimeException {

    public ResourceAlreadyExistingException(Resource resource) {
        super(String.format("%s già presente", StringUtils.capitalize(resource.getLabel())));
    }

    public ResourceAlreadyExistingException(Resource resource, Integer anno, Integer progressivo) {
        super(String.format("%s già presente con progressivo %d e anno %d", StringUtils.capitalize(resource.getLabel()), progressivo, anno));
    }

    public ResourceAlreadyExistingException(Resource resource, String abi, String cab) {
        super(String.format("%s già presente con abi %s e cab %s", StringUtils.capitalize(resource.getLabel()), abi, cab));
    }

}
