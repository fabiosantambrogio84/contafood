package com.contafood.service;

import com.contafood.model.Articolo;
import com.contafood.model.views.VOrdineClienteStatsMonth;
import com.contafood.model.views.VOrdineClienteStatsWeek;
import com.contafood.repository.views.VOrdineClienteStatsMonthRepository;
import com.contafood.repository.views.VOrdineClienteStatsWeekRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdineClienteStatsService {

    private static Logger LOGGER = LoggerFactory.getLogger(OrdineClienteStatsService.class);

    private final VOrdineClienteStatsWeekRepository vOrdineClienteStatsWeekRepository;
    private final VOrdineClienteStatsMonthRepository vOrdineClienteStatsMonthRepository;

    @Autowired
    public OrdineClienteStatsService(final VOrdineClienteStatsWeekRepository vOrdineClienteStatsWeekRepository, final VOrdineClienteStatsMonthRepository vOrdineClienteStatsMonthRepository){
        this.vOrdineClienteStatsWeekRepository = vOrdineClienteStatsWeekRepository;
        this.vOrdineClienteStatsMonthRepository = vOrdineClienteStatsMonthRepository;
    }

    public List<Articolo> getArticoliOrdiniClientiStatsWeekByIdClienteAndIdPuntoConsegna(Long idCliente, Long idPuntoConsegna){
        LOGGER.info("Retrieving the list of 'articoli' for 'ordini clienti statistiche settimana' cliente '{}' and puntoConsegna '{}'", idCliente, idPuntoConsegna);
        Set<Articolo> articoli = new HashSet<>();

        List<Long> idPuntiConsegna = new ArrayList<>();
        idPuntiConsegna.add(-1L);
        idPuntiConsegna.add(idPuntoConsegna);

        List<VOrdineClienteStatsWeek> vOrdineClienteStatsWeeks = vOrdineClienteStatsWeekRepository.findByIdClienteAndIdPuntoConsegnaIn(idCliente, idPuntiConsegna);

        vOrdineClienteStatsWeeks.stream().filter(ws -> ws.getIdArticolo() != null).forEach(ws -> {
            Articolo articolo = new Articolo();
            articolo.setId(ws.getIdArticolo());
            articolo.setCodice(ws.getCodice());
            articolo.setDescrizione(ws.getDescrizione());
            articolo.setPrezzoListinoBase(ws.getPrezzoListinoBase());

            articoli.add(articolo);
        });

        LOGGER.info("Retrieved {} 'articoli' for 'ordini clienti statistiche settimana'", articoli.size());
        return articoli.stream()
                .sorted(Comparator.comparing(Articolo::getCodice))
                .collect(Collectors.toList());
    }

    public List<Articolo> getArticoliOrdiniClientiStatsMonthByIdClienteAndIdPuntoConsegna(Long idCliente, Long idPuntoConsegna){
        LOGGER.info("Retrieving the list of 'articoli' for 'ordini clienti statistiche mese' cliente '{}' and puntoConsegna '{}'", idCliente, idPuntoConsegna);
        Set<Articolo> articoli = new HashSet<>();

        List<Long> idPuntiConsegna = new ArrayList<>();
        idPuntiConsegna.add(-1L);
        idPuntiConsegna.add(idPuntoConsegna);

        List<VOrdineClienteStatsMonth> vOrdineClienteStatsMonths = vOrdineClienteStatsMonthRepository.findByIdClienteAndIdPuntoConsegnaIn(idCliente, idPuntiConsegna);

        vOrdineClienteStatsMonths.stream().filter(ms -> ms.getIdArticolo() != null).forEach(ms -> {
            Articolo articolo = new Articolo();
            articolo.setId(ms.getIdArticolo());
            articolo.setCodice(ms.getCodice());
            articolo.setDescrizione(ms.getDescrizione());
            articolo.setPrezzoListinoBase(ms.getPrezzoListinoBase());

            articoli.add(articolo);
        });

        LOGGER.info("Retrieved {} 'articoli' for 'ordini clienti statistiche mese'", articoli.size());
        return articoli.stream()
                .sorted(Comparator.comparing(Articolo::getCodice))
                .collect(Collectors.toList());
    }
}
