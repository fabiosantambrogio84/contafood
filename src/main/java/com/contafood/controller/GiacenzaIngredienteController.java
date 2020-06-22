package com.contafood.controller;

import com.contafood.model.Fornitore;
import com.contafood.model.GiacenzaIngrediente;
import com.contafood.model.Ingrediente;
import com.contafood.model.views.VGiacenzaIngrediente;
import com.contafood.service.GiacenzaIngredienteService;
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
    public Set<VGiacenzaIngrediente> getAll(@RequestParam(name = "ingrediente", required = false) String ingrediente,
                                           @RequestParam(name = "attivo", required = false) Boolean attivo,
                                           @RequestParam(name = "idFornitore", required = false) Integer idFornitore,
                                           @RequestParam(name = "lotto", required = false) String lotto,
                                           @RequestParam(name = "scadenza", required = false) Date scadenza) {
        LOGGER.info("Performing GET request for retrieving list of 'giacenze ingredienti'");
        LOGGER.info("Request params: ingrediente {}, attivo {}, idFornitore {}, lotto {}, scadenza {}",
                ingrediente, attivo, idFornitore, lotto, scadenza);

        Predicate<VGiacenzaIngrediente> isGiacenzaQuantitaGreaterOrLessThanZero = giacenza -> {
            return giacenza.getQuantita() > 0 || giacenza.getQuantita() < 0;
        };

        Predicate<VGiacenzaIngrediente> isGiacenzaIngredienteCodiceOrDescriptionContains = giacenza -> {
            if(ingrediente != null){
                String giacenzaIngrediente = giacenza.getIngrediente();
                if(giacenzaIngrediente != null){
                    if(giacenzaIngrediente.toLowerCase().contains(ingrediente.toLowerCase())){
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

        Predicate<VGiacenzaIngrediente> isGiacenzaIngredienteAttivoEquals = giacenza -> {
            if(attivo != null){
                return giacenza.getAttivo().equals(attivo);
            }
            return true;
        };

        Predicate<VGiacenzaIngrediente> isGiacenzaIngredienteFornitoreEquals = giacenza -> {
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

        Predicate<VGiacenzaIngrediente> isGiacenzaIngredienteLottoContains = giacenza -> {
            if(lotto != null){
                if(giacenza.getLottoGiacenze() != null){
                    return giacenza.getLottoGiacenze().contains(lotto);
                } else {
                    return false;
                }
            }
            return true;
        };

        Predicate<VGiacenzaIngrediente> isGiacenzaIngredienteEquals = giacenza -> {
            if(scadenza != null){
                if(giacenza.getScadenzaGiacenze() != null){
                    return giacenza.getScadenzaGiacenze().contains(scadenza.toString());
                } else {
                    return false;
                }
            }
            return true;
        };

        Set<VGiacenzaIngrediente> giacenze = giacenzaIngredienteService.getAll().stream()
                .filter(isGiacenzaQuantitaGreaterOrLessThanZero
                        .and(isGiacenzaIngredienteCodiceOrDescriptionContains)
                        .and(isGiacenzaIngredienteAttivoEquals)
                        .and(isGiacenzaIngredienteFornitoreEquals)
                        .and(isGiacenzaIngredienteLottoContains)
                        .and(isGiacenzaIngredienteEquals))
                .collect(Collectors.toSet());

        LOGGER.info("Retrieved {} 'giacenze ingredienti'", giacenze.size());
        return giacenze;
    }

    @RequestMapping(method = GET, path = "/{idIngrediente}")
    @CrossOrigin
    public Map<String, Object> getOne(@PathVariable final Long idIngrediente) {
        LOGGER.info("Performing GET request for retrieving 'giacenza ingrediente' of ingrediente '{}'", idIngrediente);
        return giacenzaIngredienteService.getOne(idIngrediente);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public GiacenzaIngrediente create(@RequestBody final GiacenzaIngrediente giacenzaIngrediente){
        LOGGER.info("Performing POST request for creating 'giacenza ingrediente'");
        return giacenzaIngredienteService.create(giacenzaIngrediente);
    }

    @RequestMapping(method = POST, path = "/operations/delete")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void bulkDelete(@RequestBody final List<Long> idIngredienti){
        LOGGER.info("Performing BULK DELETE operation on 'giacenze ingrediente' by 'idIngrediente' (number of elements to delete: {})", idIngredienti.size());
        giacenzaIngredienteService.bulkDelete(idIngredienti);
    }

}
