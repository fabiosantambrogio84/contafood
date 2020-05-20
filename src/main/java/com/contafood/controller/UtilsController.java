package com.contafood.controller;

import com.contafood.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/utils")
public class UtilsController {

    private static Logger LOGGER = LoggerFactory.getLogger(UtilsController.class);

    @RequestMapping(method = GET, path = "/province")
    @CrossOrigin
    public List<String> getProvince() {
        LOGGER.info("Performing GET request for retrieving list of 'province'");
        return Provincia.labels();
    }

    @RequestMapping(method = GET, path = "/tipologie-sconti")
    @CrossOrigin
    public List<TipologiaSconto> getTipologieSconto() {
        LOGGER.info("Performing GET request for retrieving list of 'tipologie-sconti'");
        return Arrays.asList(TipologiaSconto.values());
    }

    @RequestMapping(method = GET, path = "/tipologie-listini-prezzi-variazioni")
    @CrossOrigin
    public List<TipologiaListinoPrezzoVariazione> getTipologieListinoPrezzoVariazione() {
        LOGGER.info("Performing GET request for retrieving list of 'tipologie-listini-prezzi-variazioni'");
        return Arrays.asList(TipologiaListinoPrezzoVariazione.values());
    }

    @RequestMapping(method = GET, path = "/giorni-settimana")
    @CrossOrigin
    public List<HashMap> getGiorniSettimana() {
        LOGGER.info("Performing GET request for retrieving list of 'giorni-settimana'");
        return GiornoSettimana.giorni();
    }

    @RequestMapping(method = GET, path = "/tipologie-ordini")
    @CrossOrigin
    public List<TipologiaOrdine> getTipologieOrdine() {
        LOGGER.info("Performing GET request for retrieving list of 'tipologie-ordini'");
        return Arrays.asList(TipologiaOrdine.values());
    }

    @RequestMapping(method = GET, path = "/tipologie-trasporto-ddt")
    @CrossOrigin
    public List<String> getTipologieTrasportoDdt() {
        LOGGER.info("Performing GET request for retrieving list of 'tipologie-trasporto-ddt'");
        return TipologiaTrasportoDdt.labels();
    }

    @RequestMapping(method = GET, path = "/tipologie-pagamenti")
    @CrossOrigin
    public List<Map<String, Object>> getTipologiePagamenti() {
        LOGGER.info("Performing GET request for retrieving list of 'tipologie-pagamenti'");
        List<TipologiaPagamento> tipologiePagamenti = Arrays.asList(TipologiaPagamento.values());
        tipologiePagamenti.sort(Comparator.comparing(TipologiaPagamento::getLabel));

        List<Map<String, Object>> result = new ArrayList<>();
        for(TipologiaPagamento tipologiaPagamento:tipologiePagamenti){
            Map<String, Object> tipologiaPagamentoMap = new HashMap<>();
            tipologiaPagamentoMap.put("label", tipologiaPagamento.getLabel());
            tipologiaPagamentoMap.put("value", tipologiaPagamento);

            result.add(tipologiaPagamentoMap);
        }

        return result;
    }

    @RequestMapping(method = GET, path = "/statistiche-periodi")
    @CrossOrigin
    public List<Map<String, Object>> getStatistichePeriodi() {
        LOGGER.info("Performing GET request for retrieving list of 'statistiche periodi'");
        return StatisticaPeriodo.getAll();
    }

    @RequestMapping(method = GET, path = "/statistiche-opzioni")
    @CrossOrigin
    public List<Map<String, Object>> getStatisticheOpzioni() {
        LOGGER.info("Performing GET request for retrieving list of 'statistiche opzioni'");
        return StatisticaOpzione.getAll();
    }
}
