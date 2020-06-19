package com.contafood.controller;

import com.contafood.model.*;
import com.contafood.service.GiacenzaIngredienteService;
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
@RequestMapping(path="/giacenze-ingredienti")
public class GiacenzaIngredienteController {

    private static Logger LOGGER = LoggerFactory.getLogger(GiacenzaIngredienteController.class);

    private final GiacenzaIngredienteService giacenzaIngredienteService;

    @Autowired
    public GiacenzaIngredienteController(final GiacenzaIngredienteService giacenzaIngredienteService){
        this.giacenzaIngredienteService = giacenzaIngredienteService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<GiacenzaIngrediente> getAll(@RequestParam(name = "ingrediente", required = false) String ingrediente,
                                           @RequestParam(name = "attivo", required = false) Boolean attivo,
                                           @RequestParam(name = "idFornitore", required = false) Integer idFornitore,
                                           @RequestParam(name = "lotto", required = false) String lotto,
                                           @RequestParam(name = "scadenza", required = false) Date scadenza) {
        LOGGER.info("Performing GET request for retrieving list of 'giacenze ingredienti'");
        LOGGER.info("Request params: ingrediente {}, attivo {}, idFornitore {}, lotto {}, scadenza {}",
                ingrediente, attivo, idFornitore, lotto, scadenza);

        Predicate<GiacenzaIngrediente> isGiacenzaIngredienteCodiceOrDescriptionContains = giacenza -> {
            if(ingrediente != null){
                Ingrediente giacenzaIngrediente = giacenza.getIngrediente();
                if(giacenzaIngrediente != null){
                    if(giacenzaIngrediente.getCodice().toLowerCase().contains(ingrediente.toLowerCase()) || giacenzaIngrediente.getDescrizione().toLowerCase().contains(ingrediente.toLowerCase())){
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

        Predicate<GiacenzaIngrediente> isGiacenzaIngredienteAttivoEquals = giacenza -> {
            if(attivo != null){
                Ingrediente giacenzaIngrediente = giacenza.getIngrediente();
                if(giacenzaIngrediente != null){
                    return giacenzaIngrediente.getAttivo().equals(attivo);
                } else {
                    return false;
                }
            }
            return true;
        };

        Predicate<GiacenzaIngrediente> isGiacenzaIngredienteFornitoreEquals = giacenza -> {
            if(idFornitore != null){
                Ingrediente giacenzaIngrediente = giacenza.getIngrediente();
                if(giacenzaIngrediente != null){
                    Fornitore fornitore = giacenzaIngrediente.getFornitore();
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

        Predicate<GiacenzaIngrediente> isGiacenzaIngredienteLottoContains = giacenza -> {
            if(lotto != null){
                if(giacenza.getLotto() != null){
                    return giacenza.getLotto().contains(lotto);
                } else {
                    return false;
                }
            }
            return true;
        };

        Predicate<GiacenzaIngrediente> isGiacenzaIngredienteEquals = giacenza -> {
            if(scadenza != null){
                if(giacenza.getScadenza() != null){
                    return giacenza.getScadenza().compareTo(scadenza)==0;
                } else {
                    return false;
                }
            }
            return true;
        };

        Set<GiacenzaIngrediente> giacenze = giacenzaIngredienteService.getAll().stream().filter(isGiacenzaIngredienteCodiceOrDescriptionContains
                .and(isGiacenzaIngredienteAttivoEquals)
                .and(isGiacenzaIngredienteFornitoreEquals)
                .and(isGiacenzaIngredienteLottoContains)
                .and(isGiacenzaIngredienteEquals))
                .collect(Collectors.toSet());

        LOGGER.info("Retrieved {} 'giacenze ingredienti'", giacenze.size());
        return giacenze;
    }

    @RequestMapping(method = GET, path = "/{giacenzaId}")
    @CrossOrigin
    public GiacenzaIngrediente getOne(@PathVariable final Long giacenzaId) {
        LOGGER.info("Performing GET request for retrieving 'giacenza ingrediente' '{}'", giacenzaId);
        return giacenzaIngredienteService.getOne(giacenzaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public GiacenzaIngrediente create(@RequestBody final GiacenzaIngrediente giacenzaIngrediente){
        LOGGER.info("Performing POST request for creating 'giacenza ingrediente'");
        return giacenzaIngredienteService.create(giacenzaIngrediente);
    }

    @RequestMapping(method = DELETE, path = "/{giacenzaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long giacenzaId){
        LOGGER.info("Performing DELETE request for deleting 'giacenza ingrediente' '{}'", giacenzaId);
        giacenzaIngredienteService.delete(giacenzaId);
    }

    @RequestMapping(method = POST, path = "/operations/delete")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void bulkDelete(@RequestBody final List<Long> giacenzeIds){
        LOGGER.info("Performing BULK DELETE operation on 'giacenze ingrediente' (number of elements to delete: {})", giacenzeIds.size());
        giacenzaIngredienteService.bulkDelete(giacenzeIds);
    }

}
