package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Banca;
import com.contafood.service.BancaService;
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
@RequestMapping(path="/banche")
public class BancaController {

    private static Logger LOGGER = LoggerFactory.getLogger(BancaController.class);

    private final BancaService bancaService;

    @Autowired
    public BancaController(final BancaService bancaService){
        this.bancaService = bancaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Banca> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'banche'");
        return bancaService.getAll();
    }

    @RequestMapping(method = GET, path = "/{bancaId}")
    @CrossOrigin
    public Banca getOne(@PathVariable final Long bancaId) {
        LOGGER.info("Performing GET request for retrieving 'banca' '{}'", bancaId);
        return bancaService.getOne(bancaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Banca create(@RequestBody final Banca banca){
        LOGGER.info("Performing POST request for creating 'banca'");
        return bancaService.create(banca);
    }

    @RequestMapping(method = PUT, path = "/{bancaId}")
    @CrossOrigin
    public Banca update(@PathVariable final Long bancaId, @RequestBody final Banca banca){
        LOGGER.info("Performing PUT request for updating 'banca' '{}'", bancaId);
        if (!Objects.equals(bancaId, banca.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return bancaService.update(banca);
    }

    @RequestMapping(method = DELETE, path = "/{bancaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long bancaId){
        LOGGER.info("Performing DELETE request for deleting 'banca' '{}'", bancaId);
        bancaService.delete(bancaId);
    }
}
