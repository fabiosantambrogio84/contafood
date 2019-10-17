package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Produzione;
import com.contafood.model.ProduzioneConfezione;
import com.contafood.service.ProduzioneService;
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
@RequestMapping(path="/produzioni")
public class ProduzioneController {

    private static Logger LOGGER = LoggerFactory.getLogger(ProduzioneController.class);

    private final ProduzioneService produzioneService;

    @Autowired
    public ProduzioneController(final ProduzioneService produzioneService){
        this.produzioneService = produzioneService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Produzione> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'produzioni'");
        return produzioneService.getAll();
    }

    @RequestMapping(method = GET, path = "/{produzioneId}")
    @CrossOrigin
    public Produzione getOne(@PathVariable final Long produzioneId) {
        LOGGER.info("Performing GET request for retrieving 'produzione' '{}'", produzioneId);
        return produzioneService.getOne(produzioneId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Produzione create(@RequestBody final Produzione produzione){
        LOGGER.info("Performing POST request for creating 'produzione'");
        return produzioneService.create(produzione);
    }

    @RequestMapping(method = PUT, path = "/{produzioneId}")
    @CrossOrigin
    public Produzione update(@PathVariable final Long produzioneId, @RequestBody final Produzione produzione){
        LOGGER.info("Performing PUT request for updating 'produzione' '{}'", produzioneId);
        if (!Objects.equals(produzioneId, produzione.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return produzioneService.update(produzione);
    }

    @RequestMapping(method = DELETE, path = "/{produzioneId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long produzioneId){
        LOGGER.info("Performing DELETE request for deleting 'produzione' '{}'", produzioneId);
        produzioneService.delete(produzioneId);
    }

    @RequestMapping(method = GET, path = "/{produzioneId}/confezioni")
    @CrossOrigin
    public Set<ProduzioneConfezione> getConfezioni(@PathVariable final Long produzioneId) {
        LOGGER.info("Performing GET request for retrieving 'produzioneConfezioni' for produzione '{}'", produzioneId);
        return produzioneService.getProduzioneConfezioni(produzioneId);
    }
}
