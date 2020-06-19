package com.contafood.controller;

import com.contafood.model.Articolo;
import com.contafood.model.Fornitore;
import com.contafood.model.GiacenzaArticolo;
import com.contafood.service.GiacenzaArticoloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/giacenze-articoli")
public class GiacenzaArticoloController {

    private static Logger LOGGER = LoggerFactory.getLogger(GiacenzaArticoloController.class);

    private final GiacenzaArticoloService giacenzaArticoloService;

    @Autowired
    public GiacenzaArticoloController(final GiacenzaArticoloService giacenzaArticoloService){
        this.giacenzaArticoloService = giacenzaArticoloService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<GiacenzaArticolo> getAll(@RequestParam(name = "articolo", required = false) String articolo,
                                        @RequestParam(name = "attivo", required = false) Boolean attivo,
                                        @RequestParam(name = "idFornitore", required = false) Integer idFornitore,
                                        @RequestParam(name = "lotto", required = false) String lotto,
                                        @RequestParam(name = "scadenza", required = false) Date scadenza) {
        LOGGER.info("Performing GET request for retrieving list of 'giacenze articoli'");
        LOGGER.info("Request params: articolo {}, attivo {}, idFornitore {}, lotto {}, scadenza {}",
                articolo, attivo, idFornitore, lotto, scadenza);

        Predicate<GiacenzaArticolo> isGiacenzaArticoloCodiceOrDescriptionContains = giacenza -> {
            if(articolo != null){
                Articolo giacenzaArticolo = giacenza.getArticolo();
                if(giacenzaArticolo != null){
                    if(giacenzaArticolo.getCodice().toLowerCase().contains(articolo.toLowerCase()) || giacenzaArticolo.getDescrizione().toLowerCase().contains(articolo.toLowerCase())){
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        };

        Predicate<GiacenzaArticolo> isGiacenzaArticoloAttivoEquals = giacenza -> {
            if(attivo != null){
                Articolo giacenzaArticolo = giacenza.getArticolo();
                if(giacenzaArticolo != null){
                    return giacenzaArticolo.getAttivo().equals(attivo);
                } else {
                    return false;
                }
            }
            return true;
        };

        Predicate<GiacenzaArticolo> isGiacenzaArticoloFornitoreEquals = giacenza -> {
            if(idFornitore != null){
                Articolo giacenzaArticolo = giacenza.getArticolo();
                if(giacenzaArticolo != null){
                    Fornitore fornitore = giacenzaArticolo.getFornitore();
                    if(fornitore != null){
                        return fornitore.getId().equals(idFornitore.longValue());
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        };

        Predicate<GiacenzaArticolo> isGiacenzaArticoloLottoContains = giacenza -> {
            if(lotto != null){
                if(giacenza.getLotto() != null){
                    return giacenza.getLotto().contains(lotto);
                } else {
                    return false;
                }
            }
            return true;
        };

        Predicate<GiacenzaArticolo> isGiacenzaScadenzaEquals = giacenza -> {
            if(scadenza != null){
                if(giacenza.getScadenza() != null){
                    return giacenza.getScadenza().compareTo(scadenza)==0;
                } else {
                    return false;
                }
            }
            return true;
        };

        Set<GiacenzaArticolo> giacenze = giacenzaArticoloService.getAll().stream().filter(isGiacenzaArticoloCodiceOrDescriptionContains
                .and(isGiacenzaArticoloAttivoEquals)
                .and(isGiacenzaArticoloFornitoreEquals)
                .and(isGiacenzaArticoloLottoContains)
                .and(isGiacenzaScadenzaEquals))
                .collect(Collectors.toSet());

        LOGGER.info("Retrieved {} 'giacenze'", giacenze.size());
        return giacenze;
    }

    @RequestMapping(method = GET, path = "/{giacenzaId}")
    @CrossOrigin
    public GiacenzaArticolo getOne(@PathVariable final Long giacenzaId) {
        LOGGER.info("Performing GET request for retrieving 'giacenza articolo' '{}'", giacenzaId);
        return giacenzaArticoloService.getOne(giacenzaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public GiacenzaArticolo create(@RequestBody final GiacenzaArticolo giacenzaArticolo){
        LOGGER.info("Performing POST request for creating 'giacenza articolo'");
        return giacenzaArticoloService.create(giacenzaArticolo);
    }

    @RequestMapping(method = DELETE, path = "/{giacenzaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long giacenzaId){
        LOGGER.info("Performing DELETE request for deleting 'giacenza articolo' '{}'", giacenzaId);
        giacenzaArticoloService.delete(giacenzaId);
    }

    @RequestMapping(method = POST, path = "/operations/delete")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void bulkDelete(@RequestBody final List<Long> giacenzeIds){
        LOGGER.info("Performing BULK DELETE operation on 'giacenze articoli' (number of elements to delete: {})", giacenzeIds.size());
        giacenzaArticoloService.bulkDelete(giacenzeIds);
    }

}
