package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.TipoFornitore;
import com.contafood.service.TipoFornitoreService;
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
@RequestMapping(path="/tipi-fornitore")
public class TipoFornitoreController {

    private static Logger LOGGER = LoggerFactory.getLogger(TipoFornitoreController.class);

    private final TipoFornitoreService tipoFornitoreService;

    @Autowired
    public TipoFornitoreController(final TipoFornitoreService tipoFornitoreService){
        this.tipoFornitoreService = tipoFornitoreService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<TipoFornitore> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'tipiFornitore'");
        return tipoFornitoreService.getAll();
    }

    @RequestMapping(method = GET, path = "/{tipoFornitoreId}")
    @CrossOrigin
    public TipoFornitore getOne(@PathVariable final Long tipoFornitoreId) {
        LOGGER.info("Performing GET request for retrieving 'tipoFornitore' '{}'", tipoFornitoreId);
        return tipoFornitoreService.getOne(tipoFornitoreId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public TipoFornitore create(@RequestBody final TipoFornitore tipoFornitore){
        LOGGER.info("Performing POST request for creating 'tipoFornitore'");
        return tipoFornitoreService.create(tipoFornitore);
    }

    @RequestMapping(method = PUT, path = "/{tipoFornitoreId}")
    @CrossOrigin
    public TipoFornitore update(@PathVariable final Long tipoFornitoreId, @RequestBody final TipoFornitore tipoFornitore){
        LOGGER.info("Performing PUT request for updating 'tipoFornitore' '{}'", tipoFornitoreId);
        if (!Objects.equals(tipoFornitoreId, tipoFornitore.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return tipoFornitoreService.update(tipoFornitore);
    }

    @RequestMapping(method = DELETE, path = "/{tipoFornitoreId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long tipoFornitoreId){
        LOGGER.info("Performing DELETE request for deleting 'tipoFornitoreId' '{}'", tipoFornitoreId);
        tipoFornitoreService.delete(tipoFornitoreId);
    }
}
