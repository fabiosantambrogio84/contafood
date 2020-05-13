package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Articolo;
import com.contafood.model.ArticoloImmagine;
import com.contafood.model.Fornitore;
import com.contafood.model.ListinoPrezzo;
import com.contafood.service.ArticoloService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/articoli")
public class ArticoloController {

    private static Logger LOGGER = LoggerFactory.getLogger(ArticoloController.class);

    private final ArticoloService articoloService;

    @Autowired
    public ArticoloController(final ArticoloService articoloService){
        this.articoloService = articoloService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public List<Articolo> getAll(@RequestParam(name = "attivo", required = false) Boolean active,
                                @RequestParam(name = "idFornitore", required = false) Integer idFornitore,
                                @RequestParam(name = "barcode", required = false) String barcode) {
        LOGGER.info("Performing GET request for retrieving list of 'articoli'");
        LOGGER.info("Query parameter: 'attivo' '{}', 'idFornitore' '{}', 'barcode' '{}'", active, idFornitore, barcode);

        Predicate<Articolo> isArticoloIdFornitoreEquals = articolo -> {
            if(idFornitore != null){
                Fornitore articoloFornitore = articolo.getFornitore();
                if(articoloFornitore != null){
                    if(articoloFornitore.getId().equals(Long.valueOf(idFornitore))){
                        return true;
                    }
                }
                return false;
            }
            return true;
        };

        Set<Articolo> articoli;
        if(active != null && !StringUtils.isEmpty(barcode)){
            articoli = articoloService.getAllByAttivoAndBarcode(active, barcode);
        } else {
            if(active != null){
                articoli = articoloService.getAllByAttivo(active);
            } else {
                articoli = articoloService.getAll();
            }
            articoli = articoli.stream().filter(isArticoloIdFornitoreEquals).collect(Collectors.toSet());
        }
        return articoli.stream().sorted(Comparator.comparing(Articolo::getCodice)).collect(Collectors.toList());
    }

    @RequestMapping(method = GET, path = "/{articoloId}")
    @CrossOrigin
    public Articolo getOne(@PathVariable final Long articoloId) {
        LOGGER.info("Performing GET request for retrieving 'articolo' '{}'", articoloId);
        return articoloService.getOne(articoloId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Articolo create(@RequestBody final Articolo articolo){
        LOGGER.info("Performing POST request for creating 'articolo'");
        return articoloService.create(articolo);
    }

    @RequestMapping(method = PUT, path = "/{articoloId}")
    @CrossOrigin
    public Articolo update(@PathVariable final Long articoloId, @RequestBody final Articolo articolo){
        LOGGER.info("Performing PUT request for updating 'articolo' '{}'", articoloId);
        if (!Objects.equals(articoloId, articolo.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return articoloService.update(articolo);
    }

    @RequestMapping(method = DELETE, path = "/{articoloId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long articoloId){
        LOGGER.info("Performing DELETE request for deleting 'articolo' '{}'", articoloId);
        articoloService.delete(articoloId);
    }

    @RequestMapping(method = GET, path = "/{articoloId}/immagini")
    @CrossOrigin
    public List<ArticoloImmagine> getArticoloImmagini(@PathVariable final Long articoloId) {
        LOGGER.info("Performing GET request for retrieving 'articoloImmagini' of 'articolo' '{}'", articoloId);
        return articoloService.getArticoloImmagini(articoloId);
    }

    @RequestMapping(method = GET, path = "/{articoloId}/listini-prezzi")
    @CrossOrigin
    public List<ListinoPrezzo> getArticoloListiniPrezzi(@PathVariable final Long articoloId) {
        LOGGER.info("Performing GET request for retrieving 'listiniPrezzi' of 'articolo' '{}'", articoloId);
        return articoloService.getArticoloListiniPrezzi(articoloId);
    }
}
