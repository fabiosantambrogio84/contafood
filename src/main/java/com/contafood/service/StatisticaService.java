package com.contafood.service;

import com.contafood.model.*;
import com.contafood.model.stats.*;
import com.contafood.util.StatisticaOpzione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class StatisticaService {

    private static Logger LOGGER = LoggerFactory.getLogger(StatisticaService.class);

    private final DdtService ddtService;

    private final FatturaAccompagnatoriaService fatturaAccompagnatoriaService;

    @Autowired
    public StatisticaService(final DdtService ddtService, final FatturaAccompagnatoriaService fatturaAccompagnatoriaService){
        this.ddtService = ddtService;
        this.fatturaAccompagnatoriaService = fatturaAccompagnatoriaService;
    }

    public Statistica computeStatistiche(StatisticaFilter statisticaFilter){
        LOGGER.info("Computing statistiche");
        Statistica statistica = new Statistica();
        statistica.setTotaleVenduto(BigDecimal.ZERO);
        statistica.setTotaleQuantitaVenduta(BigDecimal.ZERO);
        statistica.setNumeroRighe(0);

        // filter ddts
        List<Ddt> filteredDdts = filterDdts(statisticaFilter);

        // filter fatture accompagnatorie
        List<FatturaAccompagnatoria> filteredFattureAccompagnatorie = filterFattureAccompagnatorie(statisticaFilter);

        if(!isNullOrEmpty(filteredDdts) && !isNullOrEmpty(filteredFattureAccompagnatorie)){
            ComputationObject computationObject = new ComputationObject();
            computationObject.setDdts(filteredDdts);
            computationObject.setFattureAccompagnatorie(filteredFattureAccompagnatorie);

            BigDecimal totaleVenduto = computeTotaleVenduto(computationObject);
            BigDecimal totaleQuantitaVenduta = computeTotaleQuantitaVenduta(computationObject);
            long numeroRighe = computeNumeroRighe(computationObject);

            statistica.setTotaleVenduto(totaleVenduto.setScale(2, RoundingMode.CEILING));
            statistica.setTotaleQuantitaVenduta(totaleQuantitaVenduta.setScale(2, RoundingMode.CEILING));
            statistica.setNumeroRighe(Long.valueOf(numeroRighe).intValue());

            StatisticaOpzione opzione = statisticaFilter.getOpzione();
            if(opzione != null){
                if(opzione.equals(StatisticaOpzione.MOSTRA_DETTAGLIO)){
                    // mostra dettaglio
                    statistica.setStatisticaArticoli(createStatisticaArticoli(computationObject));
                } else {
                    // raggruppa dettaglio
                    statistica.setStatisticaArticoloGroups(createStatisticaArticoliGroups(computationObject));
                }
            }
        }

        LOGGER.info("Statistica: {}", statistica.toString());
        LOGGER.info("Statistiche successfully computed");
        return statistica;
    }

    private List<Ddt> filterDdts(StatisticaFilter statisticaFilter){
        LOGGER.info("Retrieving 'ddts' for statistiche computation...");
        List<Ddt> filteredDdts = new ArrayList<>();

        // retrieve all the DDTs with data >= inputData
        List<Ddt> ddts = ddtService.getByDataGreaterThanEqual(statisticaFilter.getDataDal());
        if(ddts != null && !ddts.isEmpty()) {
            Date dataA = statisticaFilter.getDataAl();
            Long idFornitore = statisticaFilter.getIdFornitore();
            List<Long> idsClienti = statisticaFilter.getIdsClienti();
            List<Long> idsArticoli = statisticaFilter.getIdsArticoli();

            Predicate<Ddt> isDdtDataALessOrEquals = ddt -> {
                if (dataA != null) {
                    return ddt.getData().compareTo(dataA) <= 0;
                }
                return true;
            };

            Predicate<Ddt> isDdtFornitoreEquals = ddt -> {
                if (idFornitore != null) {
                    Set<DdtArticolo> ddtArticoli = ddt.getDdtArticoli();
                    List<Long> idsFornitori = ddtArticoli.stream().filter(da -> da.getArticolo() != null).map(da -> da.getArticolo()).filter(a -> a.getFornitore() != null).map(a -> a.getFornitore().getId()).collect(Collectors.toList());
                    if (idsFornitori != null && !idsFornitori.isEmpty()) {
                        return idsFornitori.contains(idFornitore);
                    }
                }
                return true;
            };

            Predicate<Ddt> isDdtClientiIn = ddt -> {
                if (idsClienti != null && !idsClienti.isEmpty()) {
                    Cliente cliente = ddt.getCliente();
                    if (cliente != null) {
                        return idsClienti.contains(cliente.getId());
                    }
                }
                return true;
            };

            Predicate<Ddt> isDdtArticoliIn = ddt -> {
                if (idsArticoli != null && !idsArticoli.isEmpty()) {
                    Set<DdtArticolo> ddtArticoli = ddt.getDdtArticoli();
                    List<Long> idsDdtArticoli = ddtArticoli.stream().map(da -> da.getId().getArticoloId()).collect(Collectors.toList());
                    for (Long id : idsDdtArticoli) {
                        if (idsArticoli.contains(id)) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            };

            // filter ddts
            filteredDdts = ddts.stream().filter(isDdtDataALessOrEquals
                    .and(isDdtFornitoreEquals)
                    .and(isDdtClientiIn)
                    .and(isDdtArticoliIn)).collect(Collectors.toList());
        }
        LOGGER.info("Retrieved {} 'ddts' for statistiche computation", filteredDdts.size());
        return filteredDdts;
    }

    private List<FatturaAccompagnatoria> filterFattureAccompagnatorie(StatisticaFilter statisticaFilter){
        LOGGER.info("Retrieving 'fattureAccompagnatorie' for statistiche computation...");
        List<FatturaAccompagnatoria> filteredFattureAccompagnatorie = new ArrayList<>();

        // retrieve all the FattureAccompagnatorie with data >= inputData
        List<FatturaAccompagnatoria> fattureAccompagnatorie = fatturaAccompagnatoriaService.getByDataGreaterThanEqual(statisticaFilter.getDataDal());
        if(fattureAccompagnatorie != null && !fattureAccompagnatorie.isEmpty()) {
            Date dataA = statisticaFilter.getDataAl();
            Long idFornitore = statisticaFilter.getIdFornitore();
            List<Long> idsClienti = statisticaFilter.getIdsClienti();
            List<Long> idsArticoli = statisticaFilter.getIdsArticoli();

            Predicate<FatturaAccompagnatoria> isFatturaAccompagnatoriaDataALessOrEquals = fatturaAccompagnatoria -> {
                if (dataA != null) {
                    return fatturaAccompagnatoria.getData().compareTo(dataA) <= 0;
                }
                return true;
            };

            Predicate<FatturaAccompagnatoria> isFatturaAccompagnatoriaFornitoreEquals = fatturaAccompagnatoria -> {
                if (idFornitore != null) {
                    Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = fatturaAccompagnatoria.getFatturaAccompagnatoriaArticoli();
                    List<Long> idsFornitori = fatturaAccompagnatoriaArticoli.stream().filter(fa -> fa.getArticolo() != null).map(fa -> fa.getArticolo()).filter(a -> a.getFornitore() != null).map(a -> a.getFornitore().getId()).collect(Collectors.toList());
                    if (idsFornitori != null && !idsFornitori.isEmpty()) {
                        return idsFornitori.contains(idFornitore);
                    }
                }
                return true;
            };

            Predicate<FatturaAccompagnatoria> isFatturaAccompagnatoriaClientiIn = fatturaAccompagnatoria -> {
                if (idsClienti != null && !idsClienti.isEmpty()) {
                    Cliente cliente = fatturaAccompagnatoria.getCliente();
                    if (cliente != null) {
                        return idsClienti.contains(cliente.getId());
                    }
                }
                return true;
            };

            Predicate<FatturaAccompagnatoria> isFatturaAccompagnatoriaArticoliIn = fatturaAccompagnatoria -> {
                if (idsArticoli != null && !idsArticoli.isEmpty()) {
                    Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = fatturaAccompagnatoria.getFatturaAccompagnatoriaArticoli();
                    List<Long> idsDdtArticoli = fatturaAccompagnatoriaArticoli.stream().map(fa -> fa.getId().getArticoloId()).collect(Collectors.toList());
                    for (Long id : idsDdtArticoli) {
                        if (idsArticoli.contains(id)) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            };

            // filter fatture accompagnatorie
            filteredFattureAccompagnatorie = fattureAccompagnatorie.stream().filter(isFatturaAccompagnatoriaDataALessOrEquals
                    .and(isFatturaAccompagnatoriaFornitoreEquals)
                    .and(isFatturaAccompagnatoriaClientiIn)
                    .and(isFatturaAccompagnatoriaArticoliIn)).collect(Collectors.toList());
        }

        LOGGER.info("Retrieved {} 'fattureAccompagnatorie' for statistiche computation", filteredFattureAccompagnatorie.size());
        return filteredFattureAccompagnatorie;
    }

    private BigDecimal computeTotaleVenduto(ComputationObject computationObject){
        BigDecimal totaleVenduto = BigDecimal.ZERO;

        BigDecimal totaleVendutoDdts = computationObject.getDdts().stream().map(ddt -> ddt.getTotale()).reduce(BigDecimal.ZERO, BigDecimal::add);
        LOGGER.info("Totale venduto ddts {}", totaleVendutoDdts);

        BigDecimal totaleVendutoFattureAccompagnatorie = computationObject.getFattureAccompagnatorie().stream().map(fa -> fa.getTotale()).reduce(BigDecimal.ZERO, BigDecimal::add);
        LOGGER.info("Totale venduto fatture accompagnatorie {}", totaleVendutoFattureAccompagnatorie);

        totaleVenduto = totaleVendutoDdts.add(totaleVendutoFattureAccompagnatorie);
        LOGGER.info("Totale venduto {}", totaleVenduto);
        return totaleVenduto;
    }

    private BigDecimal computeTotaleQuantitaVenduta(ComputationObject computationObject){
        BigDecimal totaleQuantitaVenduta = BigDecimal.ZERO;

        BigDecimal totaleQuantitaVendutaDdts = computationObject.getDdts().stream().map(ddt -> ddt.getTotaleQuantita()).reduce(BigDecimal.ZERO, BigDecimal::add);
        LOGGER.info("Totale quantita venduta ddts {}", totaleQuantitaVendutaDdts);

        BigDecimal totaleQuantitaVendutaFattureAccompagnatorie = computationObject.getFattureAccompagnatorie().stream().map(fa -> fa.getTotaleQuantita()).reduce(BigDecimal.ZERO, BigDecimal::add);
        LOGGER.info("Totale quantita venduta fatture accompagnatorie {}", totaleQuantitaVendutaFattureAccompagnatorie);

        totaleQuantitaVenduta = totaleQuantitaVendutaDdts.add(totaleQuantitaVendutaFattureAccompagnatorie);
        LOGGER.info("Totale quantita venduta {}", totaleQuantitaVenduta);
        return totaleQuantitaVenduta;
    }

    private long computeNumeroRighe(ComputationObject computationObject){
        long numeroRighe = 0L;

        long numeroRigheDdts = computationObject.getDdts().stream().flatMap(ddt -> ddt.getDdtArticoli().stream()).count();
        LOGGER.info("Numero righe ddts {}", numeroRigheDdts);

        long numeroRigheFattureAccompagnatorie = computationObject.getFattureAccompagnatorie().stream().flatMap(fa -> fa.getFatturaAccompagnatoriaArticoli().stream()).count();
        LOGGER.info("Numero righe fatture accompagnatorie {}", numeroRigheFattureAccompagnatorie);

        numeroRighe = numeroRigheDdts + numeroRigheFattureAccompagnatorie;
        LOGGER.info("Numero righe {}", numeroRighe);
        return numeroRighe;
    }

    private List<StatisticaArticolo> createStatisticaArticoli(ComputationObject computationObject){
        List<StatisticaArticolo> statisticaArticoli = new ArrayList<>();

        List<DdtArticolo> ddtArticoli = computationObject.getDdts().stream().flatMap(ddt -> ddt.getDdtArticoli().stream()).collect(Collectors.toList());
        List<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = computationObject.getFattureAccompagnatorie().stream().flatMap(fa -> fa.getFatturaAccompagnatoriaArticoli().stream()).collect(Collectors.toList());

        for(DdtArticolo ddtArticolo: ddtArticoli){
            StatisticaArticolo statisticaArticolo = new StatisticaArticoloBuilder()
                    .setTipologia("DDT")
                    .setIdArticolo(ddtArticolo.getId().getArticoloId())
                    .setProgressivo(ddtArticolo.getDdt().getProgressivo())
                    .setCodice(ddtArticolo.getArticolo().getCodice())
                    .setDescrizione(ddtArticolo.getArticolo().getDescrizione())
                    .setLotto(ddtArticolo.getLotto())
                    .setPrezzo(ddtArticolo.getPrezzo())
                    .setQuantita(ddtArticolo.getQuantita())
                    .setTotale(ddtArticolo.getTotale())
                    .build();

            statisticaArticoli.add(statisticaArticolo);
        }

        for(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo: fatturaAccompagnatoriaArticoli){
            StatisticaArticolo statisticaArticolo = new StatisticaArticoloBuilder()
                    .setTipologia("FATTURA_ACCOMPAGNATORIA")
                    .setIdArticolo(fatturaAccompagnatoriaArticolo.getId().getArticoloId())
                    .setProgressivo(fatturaAccompagnatoriaArticolo.getFatturaAccompagnatoria().getProgressivo())
                    .setCodice(fatturaAccompagnatoriaArticolo.getArticolo().getCodice())
                    .setDescrizione(fatturaAccompagnatoriaArticolo.getArticolo().getDescrizione())
                    .setLotto(fatturaAccompagnatoriaArticolo.getLotto())
                    .setPrezzo(fatturaAccompagnatoriaArticolo.getPrezzo())
                    .setQuantita(fatturaAccompagnatoriaArticolo.getQuantita())
                    .setTotale(fatturaAccompagnatoriaArticolo.getTotale())
                    .build();

            statisticaArticoli.add(statisticaArticolo);
        }

        // sort list by progressivo desc and codice articolo asc
        Collections.sort(statisticaArticoli, Comparator.comparing(StatisticaArticolo::getProgressivo).reversed()
                .thenComparing(StatisticaArticolo::getCodice));

        return statisticaArticoli;
    }

    private List<StatisticaArticoloGroup> createStatisticaArticoliGroups(ComputationObject computationObject){
        List<StatisticaArticoloGroup> statisticaArticoliGroups = new ArrayList<>();

        List<StatisticaArticolo> statisticaArticoli = createStatisticaArticoli(computationObject);

        Map<Long, List<StatisticaArticolo>> statisticaArticoliMap = statisticaArticoli.stream().collect(Collectors.groupingBy(sa -> sa.getIdArticolo()));

        for (Map.Entry<Long, List<StatisticaArticolo>> entry : statisticaArticoliMap.entrySet()) {
            List<StatisticaArticolo> statisticaArticoliByArticolo = entry.getValue();

            String codice = statisticaArticoliByArticolo.stream().map(sa -> sa.getCodice()).findFirst().get();
            String descrizione = statisticaArticoliByArticolo.stream().map(sa -> sa.getDescrizione()).findFirst().get();
            Integer numRighe = statisticaArticoliByArticolo.size();
            BigDecimal totVenduto = statisticaArticoliByArticolo.stream().map(sa -> sa.getTotale()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totQuantitaVenduta = statisticaArticoliByArticolo.stream().map(sa -> new BigDecimal(sa.getQuantita())).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totVendutoMedio = totVenduto.divide(new BigDecimal(numRighe), 3, RoundingMode.HALF_UP);

            StatisticaArticoloGroup statisticaArticoloGroup = new StatisticaArticoloGroupBuilder()
                    .setCodice(codice)
                    .setDescrizione(descrizione)
                    .setNumeroRighe(numRighe)
                    .setTotaleQuantitaVenduta(totQuantitaVenduta)
                    .setTotaleVenduto(totVenduto)
                    .setTotaleVendutoMedio(totVendutoMedio)
                    .build();

            statisticaArticoliGroups.add(statisticaArticoloGroup);
        }

        // sort list by codice articolo desc
        Collections.sort(statisticaArticoliGroups, Comparator.comparing(StatisticaArticoloGroup::getCodice));

        return statisticaArticoliGroups;
    }

    private static boolean isNullOrEmpty(List<?> list){
        if(list != null && !list.isEmpty()){
            return false;
        }
        return true;
    }

}
