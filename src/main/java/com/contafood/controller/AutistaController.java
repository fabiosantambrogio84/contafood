package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Autista;
import com.contafood.model.Fornitore;
import com.contafood.service.AutistaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/autisti")
public class AutistaController {

    private static Logger LOGGER = LoggerFactory.getLogger(AutistaController.class);

    private final AutistaService autistaService;

    @Autowired
    public AutistaController(final AutistaService autistaService){
        this.autistaService = autistaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public List<Autista> getAll(@RequestParam(name = "attivo", required = false) Boolean active) {
        LOGGER.info("Performing GET request for retrieving list of 'autisti'");
        LOGGER.info("Request params: attivo {}", active);

        Predicate<Autista> isAutistaAttivoEquals = autista -> {
            if(active != null){
                return autista.getAttivo().equals(active);
            }
            return true;
        };

        return autistaService.getAll().stream()
                .filter(isAutistaAttivoEquals)
                .sorted(Comparator.comparing(Autista::getCognome).thenComparing(Autista::getNome))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = GET, path = "/{autistaId}")
    @CrossOrigin
    public Autista getOne(@PathVariable final Long autistaId) {
        LOGGER.info("Performing GET request for retrieving 'autista' '{}'", autistaId);
        return autistaService.getOne(autistaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Autista create(@RequestBody final Autista autista){
        LOGGER.info("Performing POST request for creating 'autista'");
        return autistaService.create(autista);
    }

    @RequestMapping(method = PUT, path = "/{autistaId}")
    @CrossOrigin
    public Autista update(@PathVariable final Long autistaId, @RequestBody final Autista autista){
        LOGGER.info("Performing PUT request for updating 'autista' '{}'", autistaId);
        if (!Objects.equals(autistaId, autista.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return autistaService.update(autista);
    }

    @RequestMapping(method = DELETE, path = "/{autistaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long autistaId){
        LOGGER.info("Performing DELETE request for deleting 'autista' '{}'", autistaId);
        autistaService.delete(autistaId);
    }
}
