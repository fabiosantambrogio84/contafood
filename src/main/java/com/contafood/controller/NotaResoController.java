package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Fornitore;
import com.contafood.model.NotaReso;
import com.contafood.model.StatoNotaReso;
import com.contafood.service.NotaResoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/note-reso")
public class NotaResoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(NotaResoController.class);

    private final NotaResoService notaResoService;

    @Autowired
    public NotaResoController(final NotaResoService notaResoService){
        this.notaResoService = notaResoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<NotaReso> getAll(@RequestParam(name = "dataDa", required = false) Date dataDa,
                                @RequestParam(name = "dataA", required = false) Date dataA,
                                @RequestParam(name = "progressivo", required = false) Integer progressivo,
                                @RequestParam(name = "importo", required = false) Float importo,
                                @RequestParam(name = "fornitore", required = false) String fornitore,
                                @RequestParam(name = "agente", required = false) Integer idAgente,
                                @RequestParam(name = "stato", required = false) Integer idStato ) {
        LOGGER.info("Performing GET request for retrieving list of 'note accredito'");
        LOGGER.info("Request params: dataDa {}, dataA {}, progressivo {}, importo {}, fornitore {}, agente {}, stato {}",
                dataDa, dataA, progressivo, importo, fornitore, idAgente, idStato);

        Predicate<NotaReso> isNotaResoDataDaGreaterOrEquals = notaReso -> {
            if(dataDa != null){
                return notaReso.getData().compareTo(dataDa)>=0;
            }
            return true;
        };
        Predicate<NotaReso> isNotaResoDataALessOrEquals = notaReso -> {
            if(dataA != null){
                return notaReso.getData().compareTo(dataA)<=0;
            }
            return true;
        };
        Predicate<NotaReso> isNotaResoProgressivoEquals = notaReso -> {
            if(progressivo != null){
                return notaReso.getProgressivo().equals(progressivo);
            }
            return true;
        };
        Predicate<NotaReso> isNotaResoImportoEquals = notaReso -> {
            if(importo != null){
                return notaReso.getTotale().compareTo(new BigDecimal(importo).setScale(2, RoundingMode.HALF_DOWN))==0;
            }
            return true;
        };
        Predicate<NotaReso> isNotaResoFornitoreContains = notaReso -> {
            if(fornitore != null){
                Fornitore notaResoFornitore = notaReso.getFornitore();
                if(notaResoFornitore != null){
                    return (notaResoFornitore.getRagioneSociale().toLowerCase()).contains(fornitore.toLowerCase());
                }
                return false;
            }
            return true;
        };
        Predicate<NotaReso> isNotaResoStatoEquals = notaReso -> {
            if(idStato != null){
                StatoNotaReso statoNotaReso = notaReso.getStatoNotaReso();
                if(statoNotaReso != null){
                    return statoNotaReso.getId().equals(Long.valueOf(idStato));
                }
                return false;
            }
            return true;
        };

        Set<NotaReso> noteReso = notaResoService.getAll();
        return noteReso.stream().filter(isNotaResoDataDaGreaterOrEquals
                .and(isNotaResoDataALessOrEquals)
                .and(isNotaResoProgressivoEquals)
                .and(isNotaResoImportoEquals)
                .and(isNotaResoFornitoreContains)
                .and(isNotaResoStatoEquals)).collect(Collectors.toSet());
    }

    @RequestMapping(method = GET, path = "/{notaResoId}")
    @CrossOrigin
    public NotaReso getOne(@PathVariable final Long notaResoId) {
        LOGGER.info("Performing GET request for retrieving 'nota reso' '{}'", notaResoId);
        return notaResoService.getOne(notaResoId);
    }

    @RequestMapping(method = GET, path = "/progressivo")
    @CrossOrigin
    public Map<String, Integer> getAnnoAndProgressivo() {
        LOGGER.info("Performing GET request for retrieving 'anno' and 'progressivo' for a new 'nota reso'");
        return notaResoService.getAnnoAndProgressivo();
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public NotaReso create(@RequestBody final NotaReso notaReso){
        LOGGER.info("Performing POST request for creating 'nota reso'");
        return notaResoService.create(notaReso);
    }

    @RequestMapping(method = PUT, path = "/{notaResoId}")
    @CrossOrigin
    public NotaReso update(@PathVariable final Long notaResoId, @RequestBody final NotaReso notaReso){
        LOGGER.info("Performing PUT request for updating 'nota reso' '{}'", notaResoId);
        if (!Objects.equals(notaResoId, notaReso.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return notaResoService.update(notaReso);
    }

    @RequestMapping(method = DELETE, path = "/{notaResoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long notaResoId){
        LOGGER.info("Performing DELETE request for deleting 'nota reso' '{}'", notaResoId);
        notaResoService.delete(notaResoId);
    }

}
