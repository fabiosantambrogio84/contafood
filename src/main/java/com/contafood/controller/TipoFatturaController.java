package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.TipoFattura;
import com.contafood.service.TipoFatturaService;
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
@RequestMapping(path="/tipi-fattura")
public class TipoFatturaController {

    private static Logger LOGGER = LoggerFactory.getLogger(TipoFatturaController.class);

    private final TipoFatturaService tipoFatturaService;

    @Autowired
    public TipoFatturaController(final TipoFatturaService tipoFatturaService){
        this.tipoFatturaService = tipoFatturaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<TipoFattura> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'tipiFattura'");
        return tipoFatturaService.getAll();
    }

    @RequestMapping(method = GET, path = "/{tipoFatturaId}")
    @CrossOrigin
    public TipoFattura getOne(@PathVariable final Long tipoFatturaId) {
        LOGGER.info("Performing GET request for retrieving 'tipoFattura' '{}'", tipoFatturaId);
        return tipoFatturaService.getOne(tipoFatturaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public TipoFattura create(@RequestBody final TipoFattura tipoFattura){
        LOGGER.info("Performing POST request for creating 'tipoFattura'");
        return tipoFatturaService.create(tipoFattura);
    }

    @RequestMapping(method = PUT, path = "/{tipoFatturaId}")
    @CrossOrigin
    public TipoFattura update(@PathVariable final Long tipoFatturaId, @RequestBody final TipoFattura tipoFattura){
        LOGGER.info("Performing PUT request for updating 'tipoFattura' '{}'", tipoFatturaId);
        if (!Objects.equals(tipoFatturaId, tipoFattura.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return tipoFatturaService.update(tipoFattura);
    }

    @RequestMapping(method = DELETE, path = "/{tipoFatturaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long tipoFatturaId){
        LOGGER.info("Performing DELETE request for deleting 'tipoFattura' '{}'", tipoFatturaId);
        tipoFatturaService.delete(tipoFatturaId);
    }
}
