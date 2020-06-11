package com.contafood.controller;

import com.contafood.model.Articolo;
import com.contafood.model.Fornitore;
import com.contafood.model.Giacenza;
import com.contafood.service.GiacenzaService;
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
@RequestMapping(path="/giacenze")
public class GiacenzaController {

    private static Logger LOGGER = LoggerFactory.getLogger(GiacenzaController.class);

    private final GiacenzaService giacenzaService;

    @Autowired
    public GiacenzaController(final GiacenzaService giacenzaService){
        this.giacenzaService = giacenzaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Giacenza> getAll(@RequestParam(name = "articolo", required = false) String articolo,
                                @RequestParam(name = "attivo", required = false) Boolean attivo,
                                @RequestParam(name = "idFornitore", required = false) Integer idFornitore,
                                @RequestParam(name = "lotto", required = false) String lotto,
                                @RequestParam(name = "scadenza", required = false) Date scadenza) {
        LOGGER.info("Performing GET request for retrieving list of 'giacenze'");
        LOGGER.info("Request params: articolo {}, attivo {}, idFornitore {}, lotto {}, scadenza {}",
                articolo, attivo, idFornitore, lotto, scadenza);

        Predicate<Giacenza> isGiacenzaArticoloCodiceOrDescriptionContains = giacenza -> {
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

        Predicate<Giacenza> isGiacenzaArticoloAttivoEquals = giacenza -> {
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

        Predicate<Giacenza> isGiacenzaArticoloFornitoreEquals = giacenza -> {
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

        Predicate<Giacenza> isGiacenzaArticoloLottoContains = giacenza -> {
            if(lotto != null){
                if(giacenza.getLotto() != null){
                    return giacenza.getLotto().contains(lotto);
                } else {
                    return false;
                }
            }
            return true;
        };

        Predicate<Giacenza> isGiacenzaScadenzaEquals = giacenza -> {
            if(scadenza != null){
                if(giacenza.getScadenza() != null){
                    return giacenza.getScadenza().compareTo(scadenza)==0;
                } else {
                    return false;
                }
            }
            return true;
        };

        Set<Giacenza> giacenze = giacenzaService.getAll().stream().filter(isGiacenzaArticoloCodiceOrDescriptionContains
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
    public Giacenza getOne(@PathVariable final Long giacenzaId) {
        LOGGER.info("Performing GET request for retrieving 'giacenza' '{}'", giacenzaId);
        return giacenzaService.getOne(giacenzaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Giacenza create(@RequestBody final Giacenza giacenza){
        LOGGER.info("Performing POST request for creating 'giacenza'");
        return giacenzaService.create(giacenza);
    }

    @RequestMapping(method = DELETE, path = "/{giacenzaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long giacenzaId){
        LOGGER.info("Performing DELETE request for deleting 'giacenza' '{}'", giacenzaId);
        giacenzaService.delete(giacenzaId);
    }

    @RequestMapping(method = POST, path = "/operations/delete")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void bulkDelete(@RequestBody final List<Long> giacenzeIds){
        LOGGER.info("Performing BULK DELETE operation on 'giacenze' (number of elements to delete: {})", giacenzeIds.size());
        giacenzaService.bulkDelete(giacenzeIds);
    }

}
