package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.StatoNotaAccredito;
import com.contafood.service.StatoNotaAccreditoService;
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
@RequestMapping(path="/stati-note-accredito")
public class StatoNotaAccreditoController {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoNotaAccreditoController.class);

    private final StatoNotaAccreditoService statoNotaAccreditoService;

    @Autowired
    public StatoNotaAccreditoController(final StatoNotaAccreditoService statoNotaAccreditoService){
        this.statoNotaAccreditoService = statoNotaAccreditoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<StatoNotaAccredito> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'statiNoteAccredito'");
        return statoNotaAccreditoService.getAll();
    }

    @RequestMapping(method = GET, path = "/{statoNotaAccreditoId}")
    @CrossOrigin
    public StatoNotaAccredito getOne(@PathVariable final Long statoNotaAccreditoId) {
        LOGGER.info("Performing GET request for retrieving 'statoNotaAccreditoId' '{}'", statoNotaAccreditoId);
        return statoNotaAccreditoService.getOne(statoNotaAccreditoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public StatoNotaAccredito create(@RequestBody final StatoNotaAccredito statoNotaAccredito){
        LOGGER.info("Performing POST request for creating 'statoNotaAccredito'");
        return statoNotaAccreditoService.create(statoNotaAccredito);
    }

    @RequestMapping(method = PUT, path = "/{statoNotaAccreditoId}")
    @CrossOrigin
    public StatoNotaAccredito update(@PathVariable final Long statoNotaAccreditoId, @RequestBody final StatoNotaAccredito statoNotaAccredito){
        LOGGER.info("Performing PUT request for updating 'statoNotaAccredito' '{}'", statoNotaAccreditoId);
        if (!Objects.equals(statoNotaAccreditoId, statoNotaAccredito.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return statoNotaAccreditoService.update(statoNotaAccredito);
    }

    @RequestMapping(method = DELETE, path = "/{statoNotaAccreditoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long statoNotaAccreditoId){
        LOGGER.info("Performing DELETE request for deleting 'statoNotaAccredito' '{}'", statoNotaAccreditoId);
        statoNotaAccreditoService.delete(statoNotaAccreditoId);
    }
}
