package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.*;
import com.contafood.service.DdtAcquistoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/ddts-acquisto")
public class DdtAcquistoController {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtAcquistoController.class);

    private final DdtAcquistoService ddtAcquistoService;

    @Autowired
    public DdtAcquistoController(final DdtAcquistoService ddtAcquistoService){
        this.ddtAcquistoService = ddtAcquistoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<DdtAcquisto> getAll(@RequestParam(name = "numero", required = false) Integer numero,
                                    @RequestParam(name = "fornitore", required = false) String fornitore,
                                    @RequestParam(name = "lotto", required = false) String lotto) {
        LOGGER.info("Performing GET request for retrieving list of 'ddts acquisto'");
        LOGGER.info("Request params: numero {}, fornitore {}, lotto {}",
                numero, fornitore, lotto);

        if(!StringUtils.isEmpty(lotto)){
            return ddtAcquistoService.getAllByLotto(lotto);
        }

        Predicate<DdtAcquisto> isDdtAcquistoNumeroEquals = ddtAcquisto -> {
            if(numero != null){
                return ddtAcquisto.getNumero().equals(numero);
            }
            return true;
        };
        Predicate<DdtAcquisto> isDdtAcquistoFornitoreContains = ddtAcquisto -> {
            if(fornitore != null){
                Fornitore ddtAcquistoFornitore = ddtAcquisto.getFornitore();
                if(ddtAcquistoFornitore != null){
                    if(ddtAcquistoFornitore.getRagioneSociale() != null){
                        if((ddtAcquistoFornitore.getRagioneSociale().toLowerCase()).contains(fornitore.toLowerCase())){
                            return true;
                        }
                    }else {
                        if(ddtAcquistoFornitore.getRagioneSociale2() != null){
                            if((ddtAcquistoFornitore.getRagioneSociale2().toLowerCase()).contains(fornitore.toLowerCase())){
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
            return true;
        };

        Set<DdtAcquisto> ddtsAcquisto = ddtAcquistoService.getAll();
        return ddtsAcquisto.stream().filter(isDdtAcquistoNumeroEquals
                .and(isDdtAcquistoFornitoreContains)).collect(Collectors.toSet());
    }

    @RequestMapping(method = GET, path = "/{ddtAcquistoId}")
    @CrossOrigin
    public DdtAcquisto getOne(@PathVariable final Long ddtAcquistoId) {
        LOGGER.info("Performing GET request for retrieving 'ddt acquisto' '{}'", ddtAcquistoId);
        return ddtAcquistoService.getOne(ddtAcquistoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public DdtAcquisto create(@RequestBody final DdtAcquisto ddtAcquisto){
        LOGGER.info("Performing POST request for creating 'ddt acquisto'");
        return ddtAcquistoService.create(ddtAcquisto);
    }

    @RequestMapping(method = PUT, path = "/{ddtAcquistoId}")
    @CrossOrigin
    public DdtAcquisto update(@PathVariable final Long ddtAcquistoId, @RequestBody final DdtAcquisto ddtAcquisto){
        LOGGER.info("Performing PUT request for updating 'ddt acquisto' '{}'", ddtAcquistoId);
        if (!Objects.equals(ddtAcquistoId, ddtAcquisto.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return ddtAcquistoService.update(ddtAcquisto);
    }

    @RequestMapping(method = DELETE, path = "/{ddtAcquistoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long ddtAcquistoId,
                       @RequestParam(name = "modificaGiacenze", required = false) Boolean modificaGiacenze){
        LOGGER.info("Performing DELETE request for deleting 'ddt acquisto' '{}'", ddtAcquistoId);
        LOGGER.info("Request params: modificaGiacenze={}", modificaGiacenze);
        ddtAcquistoService.delete(ddtAcquistoId, (modificaGiacenze != null ? modificaGiacenze : Boolean.FALSE));
    }

}
