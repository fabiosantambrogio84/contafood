package com.contafood.controller;

import com.contafood.model.*;
import com.contafood.service.NotaAccreditoService;
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
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/note-accredito")
public class NotaAccreditoController {

    private static Logger LOGGER = LoggerFactory.getLogger(NotaAccreditoController.class);

    private final NotaAccreditoService notaAccreditoService;

    @Autowired
    public NotaAccreditoController(final NotaAccreditoService notaAccreditoService){
        this.notaAccreditoService = notaAccreditoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<NotaAccredito> getAll(@RequestParam(name = "dataDa", required = false) Date dataDa,
                           @RequestParam(name = "dataA", required = false) Date dataA,
                           @RequestParam(name = "progressivo", required = false) Integer progressivo,
                           @RequestParam(name = "importo", required = false) Float importo,
                           @RequestParam(name = "cliente", required = false) String cliente,
                           @RequestParam(name = "agente", required = false) Integer idAgente) {
        LOGGER.info("Performing GET request for retrieving list of 'note accredito'");
        LOGGER.info("Request params: dataDa {}, dataA {}, progressivo {}, importo {}, cliente {}, agente {}",
                dataDa, dataA, progressivo, importo, cliente, idAgente);

        Predicate<NotaAccredito> isNotaAccreditoDataDaGreaterOrEquals = notaAccredito -> {
            if(dataDa != null){
                return notaAccredito.getData().compareTo(dataDa)>=0;
            }
            return true;
        };
        Predicate<NotaAccredito> isNotaAccreditoDataALessOrEquals = notaAccredito -> {
            if(dataA != null){
                return notaAccredito.getData().compareTo(dataA)<=0;
            }
            return true;
        };
        Predicate<NotaAccredito> isNotaAccreditoProgressivoEquals = notaAccredito -> {
            if(progressivo != null){
                return notaAccredito.getProgressivo().equals(progressivo);
            }
            return true;
        };
        Predicate<NotaAccredito> isNotaAccreditoImportoEquals = notaAccredito -> {
            if(importo != null){
                return notaAccredito.getTotale().compareTo(new BigDecimal(importo).setScale(2, RoundingMode.HALF_DOWN))==0;
            }
            return true;
        };
        Predicate<NotaAccredito> isNotaAccreditoClienteContains = notaAccredito -> {
            if(cliente != null){
                Cliente notaAccreditoCliente = notaAccredito.getCliente();
                if(notaAccreditoCliente != null){
                    if((notaAccreditoCliente.getRagioneSociale().toLowerCase()).contains(cliente.toLowerCase())){
                        return true;
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<NotaAccredito> isNotaAccreditoAgenteEquals = notaAccredito -> {
            if(idAgente != null){
                Cliente notaAccreditoCliente = notaAccredito.getCliente();
                if(notaAccreditoCliente != null){
                    Agente agente = notaAccreditoCliente.getAgente();
                    if(agente != null){
                        if(agente.getId().equals(Long.valueOf(idAgente))){
                            return true;
                        }
                    }
                }
                return false;
            }
            return true;
        };

        Set<NotaAccredito> noteAccredito = notaAccreditoService.getAll();
        return noteAccredito.stream().filter(isNotaAccreditoDataDaGreaterOrEquals
                .and(isNotaAccreditoDataALessOrEquals)
                .and(isNotaAccreditoProgressivoEquals)
                .and(isNotaAccreditoImportoEquals)
                .and(isNotaAccreditoClienteContains)
                .and(isNotaAccreditoAgenteEquals)).collect(Collectors.toSet());
    }

    @RequestMapping(method = GET, path = "/{notaAccreditoId}")
    @CrossOrigin
    public NotaAccredito getOne(@PathVariable final Long notaAccreditoId) {
        LOGGER.info("Performing GET request for retrieving 'nota accredito' '{}'", notaAccreditoId);
        return notaAccreditoService.getOne(notaAccreditoId);
    }

    @RequestMapping(method = GET, path = "/progressivo")
    @CrossOrigin
    public Map<String, Integer> getAnnoAndProgressivo() {
        LOGGER.info("Performing GET request for retrieving 'anno' and 'progressivo' for a new 'nota accredito'");
        return notaAccreditoService.getAnnoAndProgressivo();
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public NotaAccredito create(@RequestBody final NotaAccredito notaAccredito){
        LOGGER.info("Performing POST request for creating 'nota accredito'");
        return notaAccreditoService.create(notaAccredito);
    }

    @RequestMapping(method = DELETE, path = "/{notaAccreditoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long notaAccreditoId){
        LOGGER.info("Performing DELETE request for deleting 'nota accredito' '{}'", notaAccreditoId);
        notaAccreditoService.delete(notaAccreditoId);
    }

}
