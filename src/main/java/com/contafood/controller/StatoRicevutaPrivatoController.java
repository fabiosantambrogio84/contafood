package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.StatoRicevutaPrivato;
import com.contafood.service.StatoRicevutaPrivatoService;
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
@RequestMapping(path="/stati-ricevuta-privato")
public class StatoRicevutaPrivatoController {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoRicevutaPrivatoController.class);

    private final StatoRicevutaPrivatoService statoRicevutaPrivatoService;

    @Autowired
    public StatoRicevutaPrivatoController(final StatoRicevutaPrivatoService statoRicevutaPrivatoService){
        this.statoRicevutaPrivatoService = statoRicevutaPrivatoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<StatoRicevutaPrivato> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'statiRicevutaPrivato'");
        return statoRicevutaPrivatoService.getAll();
    }

    @RequestMapping(method = GET, path = "/{statoRicevutaPrivatoId}")
    @CrossOrigin
    public StatoRicevutaPrivato getOne(@PathVariable final Long statoRicevutaPrivatoId) {
        LOGGER.info("Performing GET request for retrieving 'statoFattura' '{}'", statoRicevutaPrivatoId);
        return statoRicevutaPrivatoService.getOne(statoRicevutaPrivatoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public StatoRicevutaPrivato create(@RequestBody final StatoRicevutaPrivato statoRicevutaPrivato){
        LOGGER.info("Performing POST request for creating 'statoFattura'");
        return statoRicevutaPrivatoService.create(statoRicevutaPrivato);
    }

    @RequestMapping(method = PUT, path = "/{statoRicevutaPrivatoId}")
    @CrossOrigin
    public StatoRicevutaPrivato update(@PathVariable final Long statoRicevutaPrivatoId, @RequestBody final StatoRicevutaPrivato statoRicevutaPrivato){
        LOGGER.info("Performing PUT request for updating 'statoFattura' '{}'", statoRicevutaPrivatoId);
        if (!Objects.equals(statoRicevutaPrivatoId, statoRicevutaPrivato.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return statoRicevutaPrivatoService.update(statoRicevutaPrivato);
    }

    @RequestMapping(method = DELETE, path = "/{statoRicevutaPrivatoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long statoRicevutaPrivatoId){
        LOGGER.info("Performing DELETE request for deleting 'statoFattura' '{}'", statoRicevutaPrivatoId);
        statoRicevutaPrivatoService.delete(statoRicevutaPrivatoId);
    }
}
