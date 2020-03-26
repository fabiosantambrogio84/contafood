package com.contafood.service;

import com.contafood.model.*;
import com.contafood.model.stats.Statistica;
import com.contafood.model.stats.StatisticaArticolo;
import com.contafood.model.stats.StatisticaFilter;
import com.contafood.util.StatisticaOpzione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class StatisticaService {

    private static Logger LOGGER = LoggerFactory.getLogger(StatisticaService.class);

    private final DdtService ddtService;

    @Autowired
    public StatisticaService(final DdtService ddtService){
        this.ddtService = ddtService;
    }

    public Statistica computeStatistiche(StatisticaFilter statisticaFilter){
        LOGGER.info("Computing statistiche");
        Statistica statistica = new Statistica();
        statistica.setTotaleVenduto(BigDecimal.ZERO);
        statistica.setTotaleQuantitaVenduta(BigDecimal.ZERO);
        statistica.setNumeroRighe(0);

        // retrieve all the DDTs with data >= inputData
        List<Ddt> ddts = ddtService.getByDataGreaterThanEqual(statisticaFilter.getDataDal());
        if(ddts != null && !ddts.isEmpty()){
            Date dataA = statisticaFilter.getDataAl();
            Long idFornitore = statisticaFilter.getIdFornitore();
            List<Long> idsClienti = statisticaFilter.getIdsClienti();
            List<Long> idsArticoli = statisticaFilter.getIdsArticoli();
            StatisticaOpzione opzione = statisticaFilter.getOpzione();

            Predicate<Ddt> isDdtDataALessOrEquals = ddt -> {
                if(dataA != null){
                    return ddt.getData().compareTo(dataA)<=0;
                }
                return true;
            };

            Predicate<Ddt> isDdtFornitoreEquals = ddt -> {
                if(idFornitore != null){
                    Set<DdtArticolo> ddtArticoli = ddt.getDdtArticoli();
                    List<Long> idsFornitori = ddtArticoli.stream().filter(da -> da.getArticolo() != null).map(da -> da.getArticolo()).filter(a -> a.getFornitore() != null).map(a -> a.getFornitore().getId()).collect(Collectors.toList());
                    if(idsFornitori != null && !idsFornitori.isEmpty()){
                        return idsFornitori.contains(idFornitore);
                    }
                }
                return true;
            };

            Predicate<Ddt> isDdtClientiIn = ddt -> {
                if(idsClienti != null && !idsClienti.isEmpty()){
                    Cliente cliente = ddt.getCliente();
                    if(cliente != null){
                        return idsClienti.contains(cliente.getId());
                    }
                }
                return true;
            };

            Predicate<Ddt> isDdtArticoliIn = ddt -> {
                if(idsArticoli != null && !idsArticoli.isEmpty()){
                    Set<DdtArticolo> ddtArticoli = ddt.getDdtArticoli();
                    List<Long> idsDdtArticoli = ddtArticoli.stream().map(da -> da.getId().getArticoloId()).collect(Collectors.toList());
                    for(Long id:idsDdtArticoli){
                        if(idsArticoli.contains(id)){
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            };

            // filter ddts
            List<Ddt> filteredDdts = ddts.stream().filter(isDdtDataALessOrEquals
                    .and(isDdtFornitoreEquals)
                    .and(isDdtClientiIn)
                    .and(isDdtArticoliIn)).collect(Collectors.toList());

            LOGGER.info("Filtered ddts: {}", filteredDdts.size());

            BigDecimal totaleVenduto = filteredDdts.stream().map(ddt -> ddt.getTotale()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totaleQuantitaVenduta = filteredDdts.stream().map(ddt -> ddt.getTotaleQuantita()).reduce(BigDecimal.ZERO, BigDecimal::add);
            long numeroRighe = filteredDdts.stream().flatMap(ddt -> ddt.getDdtArticoli().stream()).count();

            statistica.setTotaleVenduto(totaleVenduto.setScale(2, RoundingMode.CEILING));
            statistica.setTotaleQuantitaVenduta(totaleQuantitaVenduta.setScale(2, RoundingMode.CEILING));
            statistica.setNumeroRighe(Long.valueOf(numeroRighe).intValue());

            if(opzione != null){
                if(opzione.equals(StatisticaOpzione.MOSTRA_DETTAGLIO)){
                    statistica.setDdtArticoli(filteredDdts.stream().flatMap(ddt -> ddt.getDdtArticoli().stream()).collect(Collectors.toList()));
                } else {
                    // raggruppa dettaglio
                    List<StatisticaArticolo> statisticaArticoli = new ArrayList<>();

                    List<DdtArticolo> ddtArticoli = filteredDdts.stream().flatMap(ddt -> ddt.getDdtArticoli().stream()).collect(Collectors.toList());
                    Map<Long, List<DdtArticolo>> ddtArticoliMap = ddtArticoli.stream().collect(Collectors.groupingBy(da -> da.getId().getArticoloId()));

                    for (Map.Entry<Long, List<DdtArticolo>> entry : ddtArticoliMap.entrySet()) {
                        StatisticaArticolo statisticaArticolo = new StatisticaArticolo();

                        List<DdtArticolo> ddtArticoliByArticolo = entry.getValue();

                        String codice = ddtArticoliByArticolo.stream().map(da -> da.getArticolo().getCodice()).findFirst().get();
                        String descrizione = ddtArticoliByArticolo.stream().map(da -> da.getArticolo().getDescrizione()).findFirst().get();
                        Integer numRighe = ddtArticoliByArticolo.size();
                        BigDecimal totVenduto = ddtArticoliByArticolo.stream().map(da -> da.getTotale()).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal totQuantitaVenduta = ddtArticoliByArticolo.stream().map(da -> new BigDecimal(da.getQuantita())).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal totVendutoMedio = totVenduto.divide(new BigDecimal(numRighe), 3, RoundingMode.HALF_UP);

                        statisticaArticolo.setCodice(codice);
                        statisticaArticolo.setDescrizione(descrizione);
                        statisticaArticolo.setNumeroRighe(numRighe);
                        statisticaArticolo.setTotaleVenduto(totVenduto.setScale(2, RoundingMode.CEILING));
                        statisticaArticolo.setTotaleQuantitaVenduta(totQuantitaVenduta.setScale(2, RoundingMode.CEILING));
                        statisticaArticolo.setTotaleVendutoMedio(totVendutoMedio.setScale(2, RoundingMode.CEILING));

                        statisticaArticoli.add(statisticaArticolo);
                    }
                    statistica.setStatisticaArticoli(statisticaArticoli);

                }
            }
        }

        LOGGER.info("Statistica: {}", statistica.toString());
        LOGGER.info("Statistiche successfully computed");
        return statistica;
    }

}
