package com.contafood.controller;

import com.contafood.model.GiacenzaArticolo;
import com.contafood.model.views.VGiacenzaArticolo;
import com.contafood.service.GiacenzaArticoloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/giacenze-articoli")
public class GiacenzaArticoloController {

    private final static Logger LOGGER = LoggerFactory.getLogger(GiacenzaArticoloController.class);

    private final GiacenzaArticoloService giacenzaArticoloService;

    @Autowired
    public GiacenzaArticoloController(final GiacenzaArticoloService giacenzaArticoloService){
        this.giacenzaArticoloService = giacenzaArticoloService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<VGiacenzaArticolo> getAll(@RequestParam(name = "articolo", required = false) String articolo,
                                        @RequestParam(name = "attivo", required = false) Boolean attivo,
                                        @RequestParam(name = "idFornitore", required = false) Integer idFornitore,
                                        @RequestParam(name = "lotto", required = false) String lotto,
                                        @RequestParam(name = "scadenza", required = false) Date scadenza) {
        LOGGER.info("Performing GET request for retrieving list of 'giacenze articoli' (with quantita > 0)");
        LOGGER.info("Request params: articolo {}, attivo {}, idFornitore {}, lotto {}, scadenza {}",
                articolo, attivo, idFornitore, lotto, scadenza);

        Predicate<VGiacenzaArticolo> isGiacenzaQuantitaGreaterOrLessThanZero = giacenza -> giacenza.getQuantita() > 0 || giacenza.getQuantita() < 0;

        Predicate<VGiacenzaArticolo> isGiacenzaArticoloCodiceOrDescriptionContains = giacenza -> {
            if(articolo != null){
                String giacenzaArticolo = giacenza.getArticolo();
                if(giacenzaArticolo != null){
                    return giacenzaArticolo.toLowerCase().contains(articolo.toLowerCase());
                } else {
                    return false;
                }
            }
            return true;
        };

        Predicate<VGiacenzaArticolo> isGiacenzaArticoloAttivoEquals = giacenza -> {
            if(attivo != null){
                return giacenza.getAttivo().equals(attivo);
            }
            return true;
        };

        Predicate<VGiacenzaArticolo> isGiacenzaArticoloFornitoreEquals = giacenza -> {
            if(idFornitore != null){
                Long idFornitoreGiacenza = giacenza.getIdFornitore();
                if(idFornitoreGiacenza != null){
                    return idFornitoreGiacenza.equals(idFornitore.longValue());
                } else {
                    return false;
                }
            }
            return true;
        };

        Predicate<VGiacenzaArticolo> isGiacenzaArticoloLottoContains = giacenza -> {
            if(lotto != null){
                if(giacenza.getLottoGiacenze() != null){
                    return giacenza.getLottoGiacenze().contains(lotto);
                } else {
                    return false;
                }
            }
            return true;
        };

        Predicate<VGiacenzaArticolo> isGiacenzaScadenzaEquals = giacenza -> {
            if(scadenza != null){
                if(giacenza.getScadenzaGiacenze() != null){
                    return giacenza.getScadenzaGiacenze().contains(scadenza.toString());
                } else {
                    return false;
                }
            }
            return true;
        };

        Set<VGiacenzaArticolo> giacenze = giacenzaArticoloService.getAll().stream()
                .filter(isGiacenzaQuantitaGreaterOrLessThanZero
                        .and(isGiacenzaArticoloCodiceOrDescriptionContains)
                        .and(isGiacenzaArticoloAttivoEquals)
                        .and(isGiacenzaArticoloFornitoreEquals)
                        .and(isGiacenzaArticoloLottoContains)
                        .and(isGiacenzaScadenzaEquals))
                .collect(Collectors.toSet());

        LOGGER.info("Retrieved {} 'giacenze'", giacenze.size());
        return giacenze;
    }

    @RequestMapping(method = GET, path = "/{idArticolo}")
    @CrossOrigin
    public Map<String, Object> getOne(@PathVariable final Long idArticolo) {
        LOGGER.info("Performing GET request for retrieving 'giacenza articolo' of articolo '{}'", idArticolo);
        return giacenzaArticoloService.getOne(idArticolo);
    }

    @RequestMapping(method = GET, path = "/{idArticolo}/list")
    @CrossOrigin
    public List<GiacenzaArticolo> getAllByArticolo(@PathVariable final Long idArticolo) {
        LOGGER.info("Performing GET request for retrieving 'giacenza articolo' of articolo '{}'", idArticolo);
        return giacenzaArticoloService.getNotAggregateByArticolo(idArticolo);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public GiacenzaArticolo create(@RequestBody final GiacenzaArticolo giacenzaArticolo){
        LOGGER.info("Performing POST request for creating 'giacenza articolo'");
        return giacenzaArticoloService.create(giacenzaArticolo);
    }

    @RequestMapping(method = PUT)
    @CrossOrigin
    public List<GiacenzaArticolo> updateBulk(@RequestBody final List<GiacenzaArticolo> giacenzeArticolo){
        LOGGER.info("Performing PUT request for bulk updating 'giacenza articolo'");
        return giacenzaArticoloService.updateBulk(giacenzeArticolo);
    }

    @RequestMapping(method = POST, path = "/operations/delete")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void bulkDelete(@RequestBody final List<Long> idArticoli){
        LOGGER.info("Performing BULK DELETE operation on 'giacenze articoli' by 'idArticolo' (number of elements to delete: {})", idArticoli.size());
        giacenzaArticoloService.bulkDelete(idArticoli);
    }

}
