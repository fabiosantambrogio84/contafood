package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Articolo;
import com.contafood.model.Fornitore;
import com.contafood.service.FornitoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/fornitori")
public class FornitoreController {

    private static Logger LOGGER = LoggerFactory.getLogger(FornitoreController.class);

    private final FornitoreService fornitoreService;

    @Autowired
    public FornitoreController(final FornitoreService fornitoreService){
        this.fornitoreService = fornitoreService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Fornitore> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'fornitori'");
        return fornitoreService.getAll();
    }

    @RequestMapping(method = GET, path = "/{fornitoreId}")
    @CrossOrigin
    public Fornitore getOne(@PathVariable final Long fornitoreId) {
        LOGGER.info("Performing GET request for retrieving 'fornitore' '{}'", fornitoreId);
        return fornitoreService.getOne(fornitoreId);
    }

    @RequestMapping(method = GET, path = "/{fornitoreId}/articoli")
    @CrossOrigin
    public List<Articolo> getArticoli(@PathVariable final Long fornitoreId, @RequestParam(name = "attivo", required = false) Boolean active) {
        LOGGER.info("Performing GET request for retrieving 'articoli' of 'fornitore' '{}'", fornitoreId);
        LOGGER.info("Query parameter 'attivo' equal to '{}'", active);
        if(active != null){
            List<Articolo> articoli = fornitoreService.getOne(fornitoreId).getArticoli();
            return articoli.stream().filter(a -> {
                if(active != null){
                    return active.equals(a.getAttivo());
                }
                return true;
            }).collect(Collectors.toList());
        } else {
            return fornitoreService.getOne(fornitoreId).getArticoli();
        }
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Fornitore create(@RequestBody final Fornitore fornitore){
        LOGGER.info("Performing POST request for creating 'fornitore'");
        return fornitoreService.create(fornitore);
    }

    @RequestMapping(method = PUT, path = "/{fornitoreId}")
    @CrossOrigin
    public Fornitore update(@PathVariable final Long fornitoreId, @RequestBody final Fornitore fornitore){
        LOGGER.info("Performing PUT request for updating 'fornitore' '{}'", fornitoreId);
        if (!Objects.equals(fornitoreId, fornitore.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return fornitoreService.update(fornitore);
    }

    @RequestMapping(method = DELETE, path = "/{fornitoreId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long fornitoreId){
        LOGGER.info("Performing DELETE request for deleting 'fornitore' '{}'", fornitoreId);
        fornitoreService.delete(fornitoreId);
    }
}
