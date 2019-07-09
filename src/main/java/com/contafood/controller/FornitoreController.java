package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Fornitore;
import com.contafood.service.FornitoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/fornitori")
public class FornitoreController {

    private final FornitoreService fornitoreService;

    @Autowired
    public FornitoreController(final FornitoreService fornitoreService){
        this.fornitoreService = fornitoreService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Fornitore> getAll() {
        return fornitoreService.getAll();
    }

    @RequestMapping(method = GET, path = "/{fornitoreId}")
    @CrossOrigin
    public Fornitore getOne(@PathVariable final Long fornitoreId) {
        return fornitoreService.getOne(fornitoreId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Fornitore create(@RequestBody final Fornitore fornitore){
        return fornitoreService.create(fornitore);
    }

    @RequestMapping(method = PUT, path = "/{fornitoreId}")
    @CrossOrigin
    public Fornitore update(@PathVariable final Long fornitoreId, @RequestBody final Fornitore fornitore){
        if (!Objects.equals(fornitoreId, fornitore.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return fornitoreService.update(fornitore);
    }

    @RequestMapping(method = DELETE, path = "/{fornitoreId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long fornitoreId){
        fornitoreService.delete(fornitoreId);
    }
}
