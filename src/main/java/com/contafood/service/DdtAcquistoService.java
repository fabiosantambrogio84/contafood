package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.AliquotaIva;
import com.contafood.model.Articolo;
import com.contafood.model.DdtAcquisto;
import com.contafood.model.DdtAcquistoArticolo;
import com.contafood.repository.DdtAcquistoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class DdtAcquistoService {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtAcquistoService.class);

    private final DdtAcquistoRepository ddtAcquistoRepository;
    private final DdtAcquistoArticoloService ddtAcquistoArticoloService;

    @Autowired
    public DdtAcquistoService(final DdtAcquistoRepository ddtAcquistoRepository, final DdtAcquistoArticoloService ddtAcquistoArticoloService){
        this.ddtAcquistoRepository = ddtAcquistoRepository;
        this.ddtAcquistoArticoloService = ddtAcquistoArticoloService;
    }

    public Set<DdtAcquisto> getAll(){
        LOGGER.info("Retrieving the list of 'ddts acquisto'");
        Set<DdtAcquisto> ddtsAcquisto = ddtAcquistoRepository.findAllByOrderByNumeroDesc();
        LOGGER.info("Retrieved {} 'ddts acquisto'", ddtsAcquisto.size());
        return ddtsAcquisto;
    }

    public Set<DdtAcquisto> getAllByLotto(String lotto){
        LOGGER.info("Retrieving the list of 'ddts acquisto' filtered by 'lotto' '{}'", lotto);
        Set<DdtAcquisto> ddtsAcquisto = ddtAcquistoRepository.findAllByLotto(lotto);
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

        ddtAcquisto.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        DdtAcquisto createdDdtAcquisto = ddtAcquistoRepository.save(ddtAcquisto);

        createdDdtAcquisto.getDdtAcquistoArticoli().stream().forEach(daa -> {
            daa.getId().setDdtAcquistoId(createdDdtAcquisto.getId());
            daa.getId().setUuid(UUID.randomUUID().toString());
            ddtAcquistoArticoloService.create(daa);
        });

        computeTotali(createdDdtAcquisto, createdDdtAcquisto.getDdtAcquistoArticoli());

        ddtAcquistoRepository.save(createdDdtAcquisto);
        LOGGER.info("Created 'ddt acquisto' '{}'", createdDdtAcquisto);
        return createdDdtAcquisto;
    }

    @Transactional
    public DdtAcquisto update(DdtAcquisto ddtAcquisto){
        LOGGER.info("Updating 'ddt acquisto'");

        Set<DdtAcquistoArticolo> ddtAcquistoArticoli = ddtAcquisto.getDdtAcquistoArticoli();
        ddtAcquisto.setDdtAcquistoArticoli(new HashSet<>());
        ddtAcquistoArticoloService.deleteByDdtAcquistoId(ddtAcquisto.getId());

        DdtAcquisto ddtAcquistoCurrent = ddtAcquistoRepository.findById(ddtAcquisto.getId()).orElseThrow(ResourceNotFoundException::new);
        ddtAcquisto.setDataInserimento(ddtAcquistoCurrent.getDataInserimento());
        ddtAcquisto.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        DdtAcquisto updatedDdtAcquisto = ddtAcquistoRepository.save(ddtAcquisto);
        ddtAcquistoArticoli.stream().forEach(daa -> {
            daa.getId().setDdtAcquistoId(updatedDdtAcquisto.getId());
            daa.getId().setUuid(UUID.randomUUID().toString());
            ddtAcquistoArticoloService.create(daa);
        });

        computeTotali(updatedDdtAcquisto, ddtAcquistoArticoli);

        ddtAcquistoRepository.save(updatedDdtAcquisto);
        LOGGER.info("Updated 'ddt acquisto' '{}'", updatedDdtAcquisto);
        return updatedDdtAcquisto;
    }

    @Transactional
    public void delete(Long ddtAcquistoId){
        LOGGER.info("Deleting 'ddt acquisto' '{}'", ddtAcquistoId);
        ddtAcquistoArticoloService.deleteByDdtAcquistoId(ddtAcquistoId);
        ddtAcquistoRepository.deleteById(ddtAcquistoId);
        LOGGER.info("Deleted 'ddt acquisto' '{}'", ddtAcquistoId);
    }

    private void computeTotali(DdtAcquisto ddtAcquisto, Set<DdtAcquistoArticolo> ddtAcquistoArticoli){
        Map<AliquotaIva, Set<DdtAcquistoArticolo>> ivaDdtArticoliMap = new HashMap<>();
        ddtAcquistoArticoli.stream().forEach(da -> {
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
        BigDecimal totaleImponibile = new BigDecimal(0);
        BigDecimal totaleIva = new BigDecimal(0);
        BigDecimal totale = new BigDecimal(0);
        for (Map.Entry<AliquotaIva, Set<DdtAcquistoArticolo>> entry : ivaDdtArticoliMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);
            Set<DdtAcquistoArticolo> ddtAcquistoArticoliByIva = entry.getValue();
            for(DdtAcquistoArticolo ddtAcquistoArticolo: ddtAcquistoArticoliByIva){
                totaleImponibile = totaleImponibile.add(ddtAcquistoArticolo.getImponibile());

                BigDecimal partialIva = ddtAcquistoArticolo.getImponibile().multiply(iva.divide(new BigDecimal(100)));
                totaleIva = totaleIva.add(partialIva);

                totaleByIva = totaleByIva.add(ddtAcquistoArticolo.getImponibile());
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }
        ddtAcquisto.setTotaleImponibile(totaleImponibile.setScale(2, RoundingMode.HALF_DOWN));
        ddtAcquisto.setTotale(totale.setScale(2, RoundingMode.HALF_DOWN));
    }

}
