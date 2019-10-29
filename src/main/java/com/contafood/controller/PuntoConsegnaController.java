package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Banca;
import com.contafood.model.PuntoConsegna;
import com.contafood.service.BancaService;
import com.contafood.service.PuntoConsegnaService;
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
@RequestMapping(path="/punti-consegna")
public class PuntoConsegnaController {

    private static Logger LOGGER = LoggerFactory.getLogger(PuntoConsegnaController.class);

    private final PuntoConsegnaService puntoConsegnaService;

    @Autowired
    public PuntoConsegnaController(final PuntoConsegnaService puntoConsegnaService){
        this.puntoConsegnaService = puntoConsegnaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<PuntoConsegna> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'puntiConsegna'");
        return puntoConsegnaService.getAll();
    }

    @RequestMapping(method = GET, path = "/{puntoConsegnaId}")
    @CrossOrigin
    public PuntoConsegna getOne(@PathVariable final Long puntoConsegnaId) {
        LOGGER.info("Performing GET request for retrieving 'puntoConsegna' '{}'", puntoConsegnaId);
        return puntoConsegnaService.getOne(puntoConsegnaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public PuntoConsegna create(@RequestBody final PuntoConsegna puntoConsegna){
        LOGGER.info("Performing POST request for creating 'puntoConsegna'");
        return puntoConsegnaService.create(puntoConsegna);
    }

    @RequestMapping(method = PUT, path = "/{puntoConsegnaId}")
    @CrossOrigin
    public PuntoConsegna update(@PathVariable final Long puntoConsegnaId, @RequestBody final PuntoConsegna puntoConsegna){
        LOGGER.info("Performing PUT request for updating 'puntoConsegna' '{}'", puntoConsegnaId);
        if (!Objects.equals(puntoConsegnaId, puntoConsegna.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return puntoConsegnaService.update(puntoConsegna);
    }

    @RequestMapping(method = DELETE, path = "/{puntoConsegnaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long puntoConsegnaId){
        LOGGER.info("Performing DELETE request for deleting 'puntoConsegna' '{}'", puntoConsegnaId);
        puntoConsegnaService.delete(puntoConsegnaId);
    }
}
