package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Proprieta;
import com.contafood.service.ProprietaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping(path="/configurazione/proprieta")
public class ProprietaController {

    private static Logger LOGGER = LoggerFactory.getLogger(ProprietaController.class);

    private final ProprietaService proprietaService;

    @Autowired
    public ProprietaController(final ProprietaService proprietaService){
        this.proprietaService = proprietaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Proprieta> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'proprieta'");
        return proprietaService.getAll();
    }

    @RequestMapping(method = GET, path = "/{proprietaId}")
    @CrossOrigin
    public Proprieta getOne(@PathVariable final Long proprietaId) {
        LOGGER.info("Performing GET request for retrieving 'proprieta' '{}'", proprietaId);
        return proprietaService.getOne(proprietaId);
    }

    @RequestMapping(method = GET, params = "nome")
    @CrossOrigin
    public Proprieta getOneByName(@RequestParam("nome") String proprietaName) {
        LOGGER.info("Performing GET request for retrieving 'proprieta' by name '{}'", proprietaName);
        return proprietaService.findByNome(proprietaName);
    }

    @RequestMapping(method = PUT, path = "/{proprietaId}")
    @CrossOrigin
    public Proprieta update(@PathVariable final Long proprietaId, @RequestBody final Proprieta proprieta){
        LOGGER.info("Performing PUT request for updating 'proprieta' '{}'", proprietaId);
        if (!Objects.equals(proprietaId, proprieta.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return proprietaService.update(proprieta);
    }
}
