package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Confezione;
import com.contafood.service.ConfezioneService;
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
@RequestMapping(path="/confezioni")
public class ConfezioneController {

    private static Logger LOGGER = LoggerFactory.getLogger(ConfezioneController.class);

    private final ConfezioneService confezioneService;

    @Autowired
    public ConfezioneController(final ConfezioneService confezioneService){
        this.confezioneService = confezioneService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Confezione> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'confezioni'");
        return confezioneService.getAll();
    }

    @RequestMapping(method = GET, path = "/{confezioneId}")
    @CrossOrigin
    public Confezione getOne(@PathVariable final Long confezioneId) {
        LOGGER.info("Performing GET request for retrieving 'confezione' '{}'", confezioneId);
        return confezioneService.getOne(confezioneId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Confezione create(@RequestBody final Confezione confezione){
        LOGGER.info("Performing POST request for creating 'confezione'");
        return confezioneService.create(confezione);
    }

    @RequestMapping(method = PUT, path = "/{confezioneId}")
    @CrossOrigin
    public Confezione update(@PathVariable final Long confezioneId, @RequestBody final Confezione confezione){
        LOGGER.info("Performing PUT request for updating 'confezione' '{}'", confezioneId);
        if (!Objects.equals(confezioneId, confezione.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return confezioneService.update(confezione);
    }

    @RequestMapping(method = DELETE, path = "/{confezioneId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long confezioneId){
        LOGGER.info("Performing DELETE request for deleting 'confezione' '{}'", confezioneId);
        confezioneService.delete(confezioneId);
    }
}
