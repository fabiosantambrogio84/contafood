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

        // filter ddts articoli
        List<DdtArticolo> filteredDdtArticoli = filterDdtArticoli(statisticaFilter);

        // filter fatture accompagnatorie articoli
        List<FatturaAccompagnatoriaArticolo> filteredFattureAccompagnatorieArticoli = filterFattureAccompagnatorieArticoli(statisticaFilter);

        if(!isNullOrEmpty(filteredDdtArticoli) && !isNullOrEmpty(filteredFattureAccompagnatorieArticoli)){
            ComputationObject computationObject = new ComputationObject();
            computationObject.setDdtArticoli(filteredDdtArticoli);
            computationObject.setFattureAccompagnatorieArticoli(filteredFattureAccompagnatorieArticoli);

            BigDecimal totaleVenduto = computeTotaleVenduto(computationObject);
            Float totaleQuantitaVenduta = computeTotaleQuantitaVenduta(computationObject);
            long numeroRighe = computeNumeroRighe(computationObject);

            statistica.setTotaleVenduto(totaleVenduto.setScale(2, RoundingMode.HALF_DOWN));
            statistica.setTotaleQuantitaVenduta(new BigDecimal(totaleQuantitaVenduta).setScale(2, RoundingMode.HALF_DOWN));
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

    private List<DdtArticolo> filterDdtArticoli(StatisticaFilter statisticaFilter){
        LOGGER.info("Retrieving 'ddts articoli' for statistiche computation...");
        List<Ddt> filteredDdts = new ArrayList<>();
        List<DdtArticolo> filteredDdtArticoli = new ArrayList<>();

        // retrieve all the DDTs with data >= inputData
        LOGGER.info("Retrieving 'ddts' with 'data' >= {} ...", statisticaFilter.getDataDal());
        List<Ddt> ddts = ddtService.getByDataGreaterThanEqual(statisticaFilter.getDataDal());
        LOGGER.info("Retrieved {} 'ddts' with 'data' >= {}", ddts.size(), statisticaFilter.getDataDal());

        if(ddts != null && !ddts.isEmpty()) {
            // filter ddts
            Date dataA = statisticaFilter.getDataAl();
            List<Long> idsClienti = statisticaFilter.getIdsClienti();

            LOGGER.info("Filtering 'ddts' by 'dataAl' <= {} and 'idsClienti' in {}  ...", dataA, idsClienti);

            Predicate<Ddt> isDdtDataALessOrEquals = ddt -> {
                if (dataA != null) {
                    return ddt.getData().compareTo(dataA) <= 0;
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

            // filter ddts
            filteredDdts = ddts.stream().filter(isDdtDataALessOrEquals
                    .and(isDdtClientiIn)).collect(Collectors.toList());
        }
        LOGGER.info("Retrieved {} filtered 'ddts' for statistiche computation", filteredDdts.size());

        if(filteredDdts != null && !filteredDdts.isEmpty()){
            Long idFornitore = statisticaFilter.getIdFornitore();
            List<Long> idsArticoli = statisticaFilter.getIdsArticoli();

            LOGGER.info("Filtering 'ddts articoli' by 'idFornitore' = {} and 'idsArticoli' in {}  ...", idFornitore, idsArticoli);

            Set<DdtArticolo> ddtArticoli = filteredDdts.stream().flatMap(ddt -> ddt.getDdtArticoli().stream()).collect(Collectors.toSet());

            Predicate<DdtArticolo> isDdtArticoloFornitoreEquals = ddtArticolo -> {
                if (idFornitore != null) {
                    Articolo articolo = ddtArticolo.getArticolo();
                    if(articolo != null){
                        Fornitore fornitore = articolo.getFornitore();
                        if(fornitore != null){
                            return fornitore.getId().equals(idFornitore);
                        }
                    }
                }
                return true;
            };

            Predicate<DdtArticolo> isDdtArticoloArticoliIn = ddtArticolo -> {
                if (idsArticoli != null && !idsArticoli.isEmpty()) {
                    Articolo articolo = ddtArticolo.getArticolo();
                    if(articolo != null){
                        return idsArticoli.contains(articolo.getId());
                    }
                }
                return true;
            };

            filteredDdtArticoli = ddtArticoli.stream().filter(isDdtArticoloFornitoreEquals.and(isDdtArticoloArticoliIn)).collect(Collectors.toList());

        }
        LOGGER.info("Retrieved {} filtered 'ddts articoli' for statistiche computation", filteredDdtArticoli.size());

        return filteredDdtArticoli;
    }

    private List<FatturaAccompagnatoriaArticolo> filterFattureAccompagnatorieArticoli(StatisticaFilter statisticaFilter){
        LOGGER.info("Retrieving 'fattureAccompagnatorieArticoli' for statistiche computation...");
        List<FatturaAccompagnatoria> filteredFattureAccompagnatorie = new ArrayList<>();
        List<FatturaAccompagnatoriaArticolo> filteredFattureAccompagnatorieArticoli = new ArrayList<>();

        // retrieve all the FattureAccompagnatorie with data >= inputData
        LOGGER.info("Retrieving 'fattureAccompagnatorie' with 'data' >= {} ...", statisticaFilter.getDataDal());
        List<FatturaAccompagnatoria> fattureAccompagnatorie = fatturaAccompagnatoriaService.getByDataGreaterThanEqual(statisticaFilter.getDataDal());
        LOGGER.info("Retrieved {} 'fattureAccompagnatorie' with 'data' >= {}", fattureAccompagnatorie.size(), statisticaFilter.getDataDal());

        if(fattureAccompagnatorie != null && !fattureAccompagnatorie.isEmpty()) {
            // filter fatture accompagnatorie
            Date dataA = statisticaFilter.getDataAl();
            List<Long> idsClienti = statisticaFilter.getIdsClienti();

            LOGGER.info("Filtering 'fattureAccompagnatorie' by 'dataAl' <= {} and 'idsClienti' in {}  ...", dataA, idsClienti);

            Predicate<FatturaAccompagnatoria> isFatturaAccompagnatoriaDataALessOrEquals = fatturaAccompagnatoria -> {
                if (dataA != null) {
                    return fatturaAccompagnatoria.getData().compareTo(dataA) <= 0;
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

            // filter fatture accompagnatorie
            filteredFattureAccompagnatorie = fattureAccompagnatorie.stream().filter(isFatturaAccompagnatoriaDataALessOrEquals
                    .and(isFatturaAccompagnatoriaClientiIn)).collect(Collectors.toList());
        }

        LOGGER.info("Retrieved {} filtered'fattureAccompagnatorie' for statistiche computation", filteredFattureAccompagnatorie.size());

        if(filteredFattureAccompagnatorie != null && !filteredFattureAccompagnatorie.isEmpty()){
            Long idFornitore = statisticaFilter.getIdFornitore();
            List<Long> idsArticoli = statisticaFilter.getIdsArticoli();

            LOGGER.info("Filtering 'fattureAccompagnatorieArticoli' by 'idFornitore' = {} and 'idsArticoli' in {}  ...", idFornitore, idsArticoli);

            Set<FatturaAccompagnatoriaArticolo> fattureAccompagnatorieArticoli = filteredFattureAccompagnatorie.stream().flatMap(fatturaAccompagnatoria -> fatturaAccompagnatoria.getFatturaAccompagnatoriaArticoli().stream()).collect(Collectors.toSet());

            Predicate<FatturaAccompagnatoriaArticolo> isFatturaAccompagnatoriaArticoloFornitoreEquals = fatturaAccompagnatoria -> {
                if (idFornitore != null) {
                    Articolo articolo = fatturaAccompagnatoria.getArticolo();
                    if(articolo != null){
                        Fornitore fornitore = articolo.getFornitore();
                        if(fornitore != null){
                            return fornitore.getId().equals(idFornitore);
                        }
                    }
                }
                return true;
            };

            Predicate<FatturaAccompagnatoriaArticolo> isFatturaAccompagnatoriaArticoliIn = FatturaAccompagnatoriaArticolo -> {
                if (idsArticoli != null && !idsArticoli.isEmpty()) {
                    Articolo articolo = FatturaAccompagnatoriaArticolo.getArticolo();
                    if(articolo != null){
                        return idsArticoli.contains(articolo.getId());
                    }
                }
                return true;
            };

            filteredFattureAccompagnatorieArticoli = fattureAccompagnatorieArticoli.stream().filter(isFatturaAccompagnatoriaArticoloFornitoreEquals.and(isFatturaAccompagnatoriaArticoliIn)).collect(Collectors.toList());

        }


        return filteredFattureAccompagnatorieArticoli;
    }

    private BigDecimal computeTotaleVenduto(ComputationObject computationObject){
        BigDecimal totaleVenduto = BigDecimal.ZERO;

        BigDecimal totaleVendutoDdts = computationObject.getDdtArticoli().stream().map(da -> da.getTotale()).reduce(BigDecimal.ZERO, BigDecimal::add);
        LOGGER.info("Totale venduto ddts {}", totaleVendutoDdts);

        BigDecimal totaleVendutoFattureAccompagnatorie = computationObject.getFattureAccompagnatorieArticoli().stream().map(fa -> fa.getTotale()).reduce(BigDecimal.ZERO, BigDecimal::add);
        LOGGER.info("Totale venduto fatture accompagnatorie {}", totaleVendutoFattureAccompagnatorie);

        totaleVenduto = totaleVendutoDdts.add(totaleVendutoFattureAccompagnatorie);
        LOGGER.info("Totale venduto {}", totaleVenduto);
        return totaleVenduto;
    }

    private Float computeTotaleQuantitaVenduta(ComputationObject computationObject){
        Float totaleQuantitaVenduta = 0f;

        Float totaleQuantitaVendutaDdts = computationObject.getDdtArticoli().stream().map(da -> da.getQuantita()).reduce(0f, (fa1, fa2) -> fa1 + fa2);
        LOGGER.info("Totale quantita venduta ddts {}", totaleQuantitaVendutaDdts);

        Float totaleQuantitaVendutaFattureAccompagnatorie = computationObject.getFattureAccompagnatorieArticoli().stream().map(fa -> fa.getQuantita()).reduce(0f, (fa1, fa2) -> fa1 + fa2);
        LOGGER.info("Totale quantita venduta fatture accompagnatorie {}", totaleQuantitaVendutaFattureAccompagnatorie);

        totaleQuantitaVenduta = totaleQuantitaVendutaDdts + totaleQuantitaVendutaFattureAccompagnatorie;
        LOGGER.info("Totale quantita venduta {}", totaleQuantitaVenduta);
        return totaleQuantitaVenduta;
    }

    private long computeNumeroRighe(ComputationObject computationObject){
        long numeroRighe = 0l;

        long numeroRigheDdts = computationObject.getDdtArticoli().stream().count();
        LOGGER.info("Numero righe ddts {}", numeroRigheDdts);

        long numeroRigheFattureAccompagnatorie = computationObject.getFattureAccompagnatorieArticoli().stream().count();
        LOGGER.info("Numero righe fatture accompagnatorie {}", numeroRigheFattureAccompagnatorie);

        numeroRighe = numeroRigheDdts + numeroRigheFattureAccompagnatorie;
        LOGGER.info("Numero righe {}", numeroRighe);
        return numeroRighe;
    }

    private List<StatisticaArticolo> createStatisticaArticoli(ComputationObject computationObject){
        LOGGER.info("Computing 'statistica mostra dettaglio'...");
        List<StatisticaArticolo> statisticaArticoli = new ArrayList<>();

        for(DdtArticolo ddtArticolo: computationObject.getDdtArticoli()){
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

        for(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo: computationObject.getFattureAccompagnatorieArticoli()){
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

        LOGGER.info("Computed 'statistica mostra dettaglio'");

        return statisticaArticoli;
    }

    private List<StatisticaArticoloGroup> createStatisticaArticoliGroups(ComputationObject computationObject){
        LOGGER.info("Computing 'statistica raggruppa dettaglio'...");
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
            BigDecimal totVendutoMedio = totVenduto.divide(new BigDecimal(numRighe), 3, RoundingMode.HALF_DOWN);

            StatisticaArticoloGroup statisticaArticoloGroup = new StatisticaArticoloGroupBuilder()
                    .setCodice(codice)
                    .setDescrizione(descrizione)
                    .setNumeroRighe(numRighe)
                    .setTotaleQuantitaVenduta(totQuantitaVenduta.setScale(2, RoundingMode.HALF_DOWN))
                    .setTotaleVenduto(totVenduto.setScale(2, RoundingMode.HALF_DOWN))
                    .setTotaleVendutoMedio(totVendutoMedio.setScale(2, RoundingMode.HALF_DOWN))
                    .build();

            statisticaArticoliGroups.add(statisticaArticoloGroup);
        }

        // sort list by codice articolo desc
        Collections.sort(statisticaArticoliGroups, Comparator.comparing(StatisticaArticoloGroup::getCodice));

        LOGGER.info("Computed 'statistica raggruppa dettaglio'");
        return statisticaArticoliGroups;
    }

    private static boolean isNullOrEmpty(List<?> list){
        if(list != null && !list.isEmpty()){
            return false;
        }
        return true;
    }

}
