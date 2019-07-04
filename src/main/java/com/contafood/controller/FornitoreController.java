package com.contafood.controller;

import com.contafood.model.Fornitore;
import com.contafood.service.FornitoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/fornitori") // This means URL's start with /demo (after Application path)
public class FornitoreController {

    private final FornitoreService fornitoreService;

    @Autowired
    public FornitoreController(final FornitoreService fornitoreService){
        this.fornitoreService = fornitoreService;
    }

    @RequestMapping(method = GET)
    public Set<Fornitore> getAll() {
        return fornitoreService.getAll();
    }

    @RequestMapping(method = GET, path = "/{fornitoreId}")
    public Fornitore getOne(@PathVariable final Long fornitoreId) {
        return fornitoreService.getOne(fornitoreId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public Fornitore create(@RequestBody Fornitore fornitore){
        return fornitoreService.create(fornitore);
    }

    @RequestMapping(method = DELETE, path = "/{fornitoreId}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable final Long fornitoreId){
        fornitoreService.delete(fornitoreId);
    }
}
