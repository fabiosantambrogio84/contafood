package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.model.beans.DdtAcquistoRicercaLotto;
import com.contafood.repository.DdtAcquistoRepository;
import com.contafood.util.Utils;
import com.contafood.util.enumeration.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class DdtAcquistoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DdtAcquistoService.class);

    private final DdtAcquistoRepository ddtAcquistoRepository;
    private final DdtAcquistoArticoloService ddtAcquistoArticoloService;
    private final DdtAcquistoIngredienteService ddtAcquistoIngredienteService;
    private final GiacenzaArticoloService giacenzaArticoloService;
    private final GiacenzaIngredienteService giacenzaIngredienteService;
    private final StatoDdtService statoDdtService;

    @Autowired
    public DdtAcquistoService(final DdtAcquistoRepository ddtAcquistoRepository,
                              final DdtAcquistoArticoloService ddtAcquistoArticoloService,
                              final DdtAcquistoIngredienteService ddtAcquistoIngredienteService,
                              final GiacenzaArticoloService giacenzaArticoloService,
                              final GiacenzaIngredienteService giacenzaIngredienteService,
                              final StatoDdtService statoDdtService){
        this.ddtAcquistoRepository = ddtAcquistoRepository;
        this.ddtAcquistoArticoloService = ddtAcquistoArticoloService;
        this.ddtAcquistoIngredienteService = ddtAcquistoIngredienteService;
        this.giacenzaArticoloService = giacenzaArticoloService;
        this.giacenzaIngredienteService = giacenzaIngredienteService;
        this.statoDdtService = statoDdtService;
    }

    public Set<DdtAcquisto> getAll(){
        LOGGER.info("Retrieving the list of 'ddts acquisto'");
        Set<DdtAcquisto> ddtsAcquisto = ddtAcquistoRepository.findAllByOrderByNumeroDesc();
        LOGGER.info("Retrieved {} 'ddts acquisto'", ddtsAcquisto.size());
        return ddtsAcquisto;
    }

    public Set<DdtAcquistoRicercaLotto> getAllByLotto(String lotto){
        LOGGER.info("Retrieving the list of 'ddts acquisto' filtered by 'lotto' '{}'", lotto);
        Set<DdtAcquistoRicercaLotto> ddtsAcquisto = ddtAcquistoRepository.findAllByLotto(lotto);
        LOGGER.info("Retrieved {} 'ddts acquisto'", ddtsAcquisto.size());
        return ddtsAcquisto;
    }

    public DdtAcquisto getOne(Long ddtAcquistoId){
        LOGGER.info("Retrieving 'ddt acquisto' '{}'", ddtAcquistoId);
        DdtAcquisto ddtAcquisto = ddtAcquistoRepository.findById(ddtAcquistoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'ddt acquisto' '{}'", ddtAcquisto);
        return ddtAcquisto;
    }

    @Transactional
    public DdtAcquisto create(DdtAcquisto ddtAcquisto){
        LOGGER.info("Creating 'ddt acquisto'");

        if(ddtAcquisto.getNumeroColli() == null){
            ddtAcquisto.setNumeroColli(1);
        }
        ddtAcquisto.setStatoDdt(statoDdtService.getDaPagare());
        ddtAcquisto.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        DdtAcquisto createdDdtAcquisto = ddtAcquistoRepository.save(ddtAcquisto);

        createdDdtAcquisto.getDdtAcquistoArticoli().forEach(daa -> {
            daa.getId().setDdtAcquistoId(createdDdtAcquisto.getId());
            daa.getId().setUuid(UUID.randomUUID().toString());
            ddtAcquistoArticoloService.create(daa);

            // compute 'giacenza articolo'
            giacenzaArticoloService.computeGiacenza(daa.getId().getArticoloId(), daa.getLotto(), daa.getDataScadenza(), daa.getQuantita(), Resource.DDT_ACQUISTO);
        });

        createdDdtAcquisto.getDdtAcquistoIngredienti().forEach(dai -> {
            dai.getId().setDdtAcquistoId(createdDdtAcquisto.getId());
            dai.getId().setUuid(UUID.randomUUID().toString());
            ddtAcquistoIngredienteService.create(dai);

            // compute 'giacenza ingrediente'
            giacenzaIngredienteService.computeGiacenza(dai.getId().getIngredienteId(), dai.getLotto(), dai.getDataScadenza(), dai.getQuantita(), Resource.DDT_ACQUISTO);
        });

        computeTotali(createdDdtAcquisto, createdDdtAcquisto.getDdtAcquistoArticoli(), createdDdtAcquisto.getDdtAcquistoIngredienti());

        ddtAcquistoRepository.save(createdDdtAcquisto);

        LOGGER.info("Created 'ddt acquisto' '{}'", createdDdtAcquisto);
        return createdDdtAcquisto;
    }

    @Transactional
    public DdtAcquisto update(DdtAcquisto ddtAcquisto){
        LOGGER.info("Updating 'ddt acquisto'");

        Boolean modificaGiacenze = ddtAcquisto.getModificaGiacenze();

        Set<DdtAcquistoArticolo> ddtAcquistoArticoli = ddtAcquisto.getDdtAcquistoArticoli();
        ddtAcquisto.setDdtAcquistoArticoli(new HashSet<>());
        ddtAcquistoArticoloService.deleteByDdtAcquistoId(ddtAcquisto.getId());

        Set<DdtAcquistoIngrediente> ddtAcquistoIngredienti = ddtAcquisto.getDdtAcquistoIngredienti();
        ddtAcquisto.setDdtAcquistoIngredienti(new HashSet<>());
        ddtAcquistoIngredienteService.deleteByDdtAcquistoId(ddtAcquisto.getId());

        DdtAcquisto ddtAcquistoCurrent = ddtAcquistoRepository.findById(ddtAcquisto.getId()).orElseThrow(ResourceNotFoundException::new);
        ddtAcquisto.setDataInserimento(ddtAcquistoCurrent.getDataInserimento());
        ddtAcquisto.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        DdtAcquisto updatedDdtAcquisto = ddtAcquistoRepository.save(ddtAcquisto);
        ddtAcquistoArticoli.forEach(daa -> {
            daa.getId().setDdtAcquistoId(updatedDdtAcquisto.getId());
            daa.getId().setUuid(UUID.randomUUID().toString());

            if(modificaGiacenze != null && modificaGiacenze.equals(Boolean.TRUE)){
                // compute 'giacenza articolo'
                giacenzaArticoloService.computeGiacenza(daa.getId().getArticoloId(), daa.getLotto(), daa.getDataScadenza(), daa.getQuantita(), Resource.DDT_ACQUISTO);
            }
            ddtAcquistoArticoloService.create(daa);
        });
        ddtAcquistoIngredienti.forEach(dai -> {
            dai.getId().setDdtAcquistoId(updatedDdtAcquisto.getId());
            dai.getId().setUuid(UUID.randomUUID().toString());

            if(modificaGiacenze != null && modificaGiacenze.equals(Boolean.TRUE)){
                // compute 'giacenza ingrediente'
                giacenzaIngredienteService.computeGiacenza(dai.getId().getIngredienteId(), dai.getLotto(), dai.getDataScadenza(), dai.getQuantita(), Resource.DDT_ACQUISTO);
            }
            ddtAcquistoIngredienteService.create(dai);
        });

        computeTotali(updatedDdtAcquisto, ddtAcquistoArticoli, ddtAcquistoIngredienti);

        ddtAcquistoRepository.save(updatedDdtAcquisto);
        LOGGER.info("Updated 'ddt acquisto' '{}'", updatedDdtAcquisto);
        return updatedDdtAcquisto;
    }

    @Transactional
    public DdtAcquisto patch(Map<String,Object> patchDdtAcquisto){
        LOGGER.info("Patching 'ddt acquisto'");

        Long id = Long.valueOf((Integer) patchDdtAcquisto.get("id"));
        DdtAcquisto ddtAcquisto = ddtAcquistoRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        patchDdtAcquisto.forEach((key, value) -> {
            if(key.equals("id")){
                ddtAcquisto.setId(Long.valueOf((Integer)value));
            } else if(key.equals("fatturato")){
                if(value != null){
                    ddtAcquisto.setFatturato((Boolean)value);
                }
            }
        });
        DdtAcquisto patchedDdtAcquisto = ddtAcquistoRepository.save(ddtAcquisto);

        LOGGER.info("Patched 'ddt acquisto' '{}'", patchedDdtAcquisto);
        return patchedDdtAcquisto;
    }

    @Transactional
    public void delete(Long ddtAcquistoId, Boolean modificaGiacenze){
        LOGGER.info("Deleting 'ddt acquisto' '{}' ('modificaGiacenze={}')", ddtAcquistoId, modificaGiacenze);
        Set<DdtAcquistoArticolo> ddtAcquistoArticoli = ddtAcquistoArticoloService.findByDdtAcquistoId(ddtAcquistoId);
        Set<DdtAcquistoIngrediente> ddtAcquistoIngredienti = ddtAcquistoIngredienteService.findByDdtAcquistoId(ddtAcquistoId);

        ddtAcquistoArticoloService.deleteByDdtAcquistoId(ddtAcquistoId);
        ddtAcquistoIngredienteService.deleteByDdtAcquistoId(ddtAcquistoId);
        ddtAcquistoRepository.deleteById(ddtAcquistoId);

        if(modificaGiacenze.equals(Boolean.TRUE)){
            for (DdtAcquistoArticolo ddtAcquistoArticolo:ddtAcquistoArticoli) {
                // compute 'giacenza articolo'
                giacenzaArticoloService.computeGiacenza(ddtAcquistoArticolo.getId().getArticoloId(), ddtAcquistoArticolo.getLotto(), ddtAcquistoArticolo.getDataScadenza(), ddtAcquistoArticolo.getQuantita(), Resource.DDT_ACQUISTO);
            }
            for (DdtAcquistoIngrediente ddtAcquistoIngrediente:ddtAcquistoIngredienti) {
                // compute 'giacenza ingrediente'
                giacenzaIngredienteService.computeGiacenza(ddtAcquistoIngrediente.getId().getIngredienteId(), ddtAcquistoIngrediente.getLotto(), ddtAcquistoIngrediente.getDataScadenza(), ddtAcquistoIngrediente.getQuantita(), Resource.DDT_ACQUISTO);
            }
        }

        LOGGER.info("Deleted 'ddt acquisto' '{}'", ddtAcquistoId);
    }

    private void computeTotali(DdtAcquisto ddtAcquisto, Set<DdtAcquistoArticolo> ddtAcquistoArticoli, Set<DdtAcquistoIngrediente> ddtAcquistoIngredienti){
        // create and populate map for DdtAcquistoArticolo
        Map<AliquotaIva, Set<DdtAcquistoArticolo>> ivaDdtArticoliMap = new HashMap<>();
        ddtAcquistoArticoli.forEach(da -> {
            Articolo articolo = ddtAcquistoArticoloService.getArticolo(da);
            AliquotaIva iva = articolo.getAliquotaIva();
            Set<DdtAcquistoArticolo> ddtArticoliByIva;
            if(ivaDdtArticoliMap.containsKey(iva)){
                ddtArticoliByIva = ivaDdtArticoliMap.get(iva);
            } else {
                ddtArticoliByIva = new HashSet<>();
            }
            ddtArticoliByIva.add(da);
            ivaDdtArticoliMap.put(iva, ddtArticoliByIva);
        });

        // create and populate map for DdtAcquistoIngrediente
        Map<AliquotaIva, Set<DdtAcquistoIngrediente>> ivaDdtIngredientiMap = new HashMap<>();
        ddtAcquistoIngredienti.forEach(dai -> {
            Ingrediente ingrediente = ddtAcquistoIngredienteService.getIngrediente(dai);
            AliquotaIva iva = ingrediente.getAliquotaIva();
            Set<DdtAcquistoIngrediente> ddtIngredientiByIva;
            if(ivaDdtIngredientiMap.containsKey(iva)){
                ddtIngredientiByIva = ivaDdtIngredientiMap.get(iva);
            } else {
                ddtIngredientiByIva = new HashSet<>();
            }
            ddtIngredientiByIva.add(dai);
            ivaDdtIngredientiMap.put(iva, ddtIngredientiByIva);
        });

        BigDecimal totaleImponibile = new BigDecimal(0);
        BigDecimal totaleIva = new BigDecimal(0);
        BigDecimal totale = new BigDecimal(0);

        // compute 'totaleImponibile', 'totaleIva' and 'totale'
        for (Map.Entry<AliquotaIva, Set<DdtAcquistoArticolo>> entry : ivaDdtArticoliMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);
            Set<DdtAcquistoArticolo> ddtAcquistoArticoliByIva = entry.getValue();
            for(DdtAcquistoArticolo ddtAcquistoArticolo: ddtAcquistoArticoliByIva){
                totaleImponibile = totaleImponibile.add(ddtAcquistoArticolo.getImponibile());

                BigDecimal partialIva = ddtAcquistoArticolo.getImponibile().multiply(iva.divide(new BigDecimal(100), RoundingMode.HALF_UP));
                totaleIva = totaleIva.add(partialIva);

                totaleByIva = totaleByIva.add(ddtAcquistoArticolo.getImponibile());
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100), RoundingMode.HALF_UP))));
        }
        for (Map.Entry<AliquotaIva, Set<DdtAcquistoIngrediente>> entry : ivaDdtIngredientiMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);
            Set<DdtAcquistoIngrediente> ddtAcquistoingredientiByIva = entry.getValue();
            for(DdtAcquistoIngrediente DdtAcquistoIngrediente: ddtAcquistoingredientiByIva){
                totaleImponibile = totaleImponibile.add(DdtAcquistoIngrediente.getImponibile());

                BigDecimal partialIva = DdtAcquistoIngrediente.getImponibile().multiply(iva.divide(new BigDecimal(100), RoundingMode.HALF_UP));
                totaleIva = totaleIva.add(partialIva);

                totaleByIva = totaleByIva.add(DdtAcquistoIngrediente.getImponibile());
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100), RoundingMode.HALF_UP))));
        }

        ddtAcquisto.setTotaleImponibile(Utils.roundPrice(totaleImponibile));
        ddtAcquisto.setTotaleIva(Utils.roundPrice(totaleIva));
        ddtAcquisto.setTotale(Utils.roundPrice(totale));
        ddtAcquisto.setTotaleAcconto(new BigDecimal(0));
    }
}
