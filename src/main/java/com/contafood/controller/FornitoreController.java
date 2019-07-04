package com.contafood.controller;

import com.contafood.model.Fornitore;
import com.contafood.service.FornitoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller    // This means that this class is a Controller
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

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public void create(Fornitore fornitore){
        fornitoreService.create(fornitore);
    }
}
