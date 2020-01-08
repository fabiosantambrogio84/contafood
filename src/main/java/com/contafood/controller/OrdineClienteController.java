package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.OrdineCliente;
import com.contafood.service.OrdineClienteService;
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
@RequestMapping(path="/ordini-clienti")
public class OrdineClienteController {

    private static Logger LOGGER = LoggerFactory.getLogger(OrdineClienteController.class);

    private final OrdineClienteService ordineClienteService;

    @Autowired
    public OrdineClienteController(final OrdineClienteService ordineClienteService){
        this.ordineClienteService = ordineClienteService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<OrdineCliente> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'ordini-clienti'");
        return ordineClienteService.getAll();
    }

    @RequestMapping(method = GET, path = "/{ordineClienteId}")
    @CrossOrigin
    public OrdineCliente getOne(@PathVariable final Long ordineClienteId) {
        LOGGER.info("Performing GET request for retrieving 'ordineCliente' '{}'", ordineClienteId);
        return ordineClienteService.getOne(ordineClienteId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public OrdineCliente create(@RequestBody final OrdineCliente ordineCliente){
        LOGGER.info("Performing POST request for creating 'ordineCliente'");
        return ordineClienteService.create(ordineCliente);
    }

    @RequestMapping(method = PUT, path = "/{ordineClienteId}")
    @CrossOrigin
    public OrdineCliente update(@PathVariable final Long ordineClienteId, @RequestBody final OrdineCliente ordineCliente){
        LOGGER.info("Performing PUT request for updating 'ordineCliente' '{}'", ordineClienteId);
        if (!Objects.equals(ordineClienteId, ordineCliente.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return ordineClienteService.update(ordineCliente);
    }

    @RequestMapping(method = DELETE, path = "/{ordineClienteId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long ordineClienteId){
        LOGGER.info("Performing DELETE request for deleting 'ordineCliente' '{}'", ordineClienteId);
        ordineClienteService.delete(ordineClienteId);
    }
}
