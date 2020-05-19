package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.AliquotaIva;
import com.contafood.service.AliquotaIvaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/aliquote-iva")
public class AliquotaIvaController {

    private static Logger LOGGER = LoggerFactory.getLogger(AliquotaIvaController.class);

    private final AliquotaIvaService aliquotaIvaService;

    @Autowired
    public AliquotaIvaController(final AliquotaIvaService aliquotaIvaService){
        this.aliquotaIvaService = aliquotaIvaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<AliquotaIva> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'aliquote iva'");
        return aliquotaIvaService.getAll();
    }

    @RequestMapping(method = GET, path = "/{aliquotaIvaId}")
    @CrossOrigin
    public AliquotaIva getOne(@PathVariable final Long aliquotaIvaId) {
        LOGGER.info("Performing GET request for retrieving 'aliquota iva' '{}'", aliquotaIvaId);
        return aliquotaIvaService.getOne(aliquotaIvaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public AliquotaIva create(@RequestBody final AliquotaIva aliquotaIva){
        LOGGER.info("Performing POST request for creating 'aliquota iva'");
        return aliquotaIvaService.create(aliquotaIva);
    }

    @RequestMapping(method = PUT, path = "/{aliquotaIvaId}")
    @CrossOrigin
    public AliquotaIva update(@PathVariable final Long aliquotaIvaId, @RequestBody final AliquotaIva aliquotaIva){
        LOGGER.info("Performing PUT request for updating 'aliquota iva' '{}'", aliquotaIvaId);
        if (!Objects.equals(aliquotaIvaId, aliquotaIva.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return aliquotaIvaService.update(aliquotaIva);
    }

    @RequestMapping(method = DELETE, path = "/{aliquotaIvaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long aliquotaIvaId){
        LOGGER.info("Performing DELETE request for deleting 'aliquota iva' '{}'", aliquotaIvaId);
        aliquotaIvaService.delete(aliquotaIvaId);
    }
}
