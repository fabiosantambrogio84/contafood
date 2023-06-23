package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Articolo;
import com.contafood.model.FatturaAccompagnatoriaAcquistoArticolo;
import com.contafood.repository.FatturaAccompagnatoriaAcquistoArticoloRepository;
import com.contafood.util.AccountingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FatturaAccompagnatoriaAcquistoArticoloService {

    private final FatturaAccompagnatoriaAcquistoArticoloRepository fatturaAccompagnatoriaAcquistoArticoloRepository;
    private final ArticoloService articoloService;

    @Autowired
    public FatturaAccompagnatoriaAcquistoArticoloService(final FatturaAccompagnatoriaAcquistoArticoloRepository fatturaAccompagnatoriaAcquistoArticoloRepository,
                                                         final ArticoloService articoloService){
        this.fatturaAccompagnatoriaAcquistoArticoloRepository = fatturaAccompagnatoriaAcquistoArticoloRepository;
        this.articoloService = articoloService;
    }

    public Set<FatturaAccompagnatoriaAcquistoArticolo> findAll(){
        log.info("Retrieving the list of 'fattura accompagnatoria acquisto articoli'");
        Set<FatturaAccompagnatoriaAcquistoArticolo> fatturaAccompagnatoriaAcquistoArticoli = fatturaAccompagnatoriaAcquistoArticoloRepository.findAll();
        log.info("Retrieved {} 'fattura accompagnatoria acquisto articoli'", fatturaAccompagnatoriaAcquistoArticoli.size());
        return fatturaAccompagnatoriaAcquistoArticoli;
    }

    public Set<FatturaAccompagnatoriaAcquistoArticolo> findByFatturaAccompagnatoriaAcquistoId(Long idFatturaAccompagnatoriaAcquisto){
        log.info("Retrieving the list of 'fattura accompagnatoria articoli' of 'fattura accompagnatoria acquisto' {}", idFatturaAccompagnatoriaAcquisto);
        Set<FatturaAccompagnatoriaAcquistoArticolo> fatturaAccompagnatoriaAcquistoArticoli = fatturaAccompagnatoriaAcquistoArticoloRepository.findByFatturaAccompagnatoriaAcquistoId(idFatturaAccompagnatoriaAcquisto);
        log.info("Retrieved {} 'fattura accompagnatoria articoli'", fatturaAccompagnatoriaAcquistoArticoli.size());
        return fatturaAccompagnatoriaAcquistoArticoli;
    }

    public FatturaAccompagnatoriaAcquistoArticolo create(FatturaAccompagnatoriaAcquistoArticolo fatturaAccompagnatoriaAcquistoArticolo){
        log.info("Creating 'fattura accompagnatoria acquisto articolo'");
        fatturaAccompagnatoriaAcquistoArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        fatturaAccompagnatoriaAcquistoArticolo.setImponibile(computeImponibile(fatturaAccompagnatoriaAcquistoArticolo));
        fatturaAccompagnatoriaAcquistoArticolo.setCosto(computeCosto(fatturaAccompagnatoriaAcquistoArticolo));
        fatturaAccompagnatoriaAcquistoArticolo.setTotale(computeTotale(fatturaAccompagnatoriaAcquistoArticolo));

        FatturaAccompagnatoriaAcquistoArticolo createdFatturaAccompagnatoriaAcquistoArticolo = fatturaAccompagnatoriaAcquistoArticoloRepository.save(fatturaAccompagnatoriaAcquistoArticolo);

        log.info("Created 'fattura accompagnatoria acquisto articolo' '{}'", createdFatturaAccompagnatoriaAcquistoArticolo);
        return createdFatturaAccompagnatoriaAcquistoArticolo;
    }

    public FatturaAccompagnatoriaAcquistoArticolo update(FatturaAccompagnatoriaAcquistoArticolo fatturaAccompagnatoriaAcquistoArticolo){
        log.info("Updating 'fattura accompagnatoria acquisto articolo'");
        FatturaAccompagnatoriaAcquistoArticolo fatturaAccompagnatoriaAcquistoArticoloCurrent = fatturaAccompagnatoriaAcquistoArticoloRepository.findById(fatturaAccompagnatoriaAcquistoArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        fatturaAccompagnatoriaAcquistoArticolo.setDataInserimento(fatturaAccompagnatoriaAcquistoArticoloCurrent.getDataInserimento());
        fatturaAccompagnatoriaAcquistoArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        fatturaAccompagnatoriaAcquistoArticolo.setImponibile(computeImponibile(fatturaAccompagnatoriaAcquistoArticolo));
        fatturaAccompagnatoriaAcquistoArticolo.setCosto(computeCosto(fatturaAccompagnatoriaAcquistoArticolo));
        fatturaAccompagnatoriaAcquistoArticolo.setTotale(computeTotale(fatturaAccompagnatoriaAcquistoArticolo));

        FatturaAccompagnatoriaAcquistoArticolo updatedFatturaAccompagnatoriaAcquistoArticolo = fatturaAccompagnatoriaAcquistoArticoloRepository.save(fatturaAccompagnatoriaAcquistoArticolo);
        log.info("Updated 'fattura accompagnatoria acquisto articolo' '{}'", updatedFatturaAccompagnatoriaAcquistoArticolo);
        return updatedFatturaAccompagnatoriaAcquistoArticolo;
    }

    public void deleteByFatturaAccompagnatoriaAcquistoId(Long fatturaAccompagnatoriaAcquistoId){
        log.info("Deleting 'fattura accompagnatoria acquisto articolo' by 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquistoId);
        fatturaAccompagnatoriaAcquistoArticoloRepository.deleteByFatturaAccompagnatoriaAcquistoId(fatturaAccompagnatoriaAcquistoId);
        log.info("Deleted 'fattura accompagnatoria acquisto articolo' by 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquistoId);
    }

    public Articolo getArticolo(FatturaAccompagnatoriaAcquistoArticolo fatturaAccompagnatoriaAcquistoArticolo){
        Long articoloId = fatturaAccompagnatoriaAcquistoArticolo.getId().getArticoloId();
        return articoloService.getOne(articoloId);
    }

    private BigDecimal computeImponibile(FatturaAccompagnatoriaAcquistoArticolo fatturaAccompagnatoriaAcquistoArticolo){

        return AccountingUtils.computeImponibile(fatturaAccompagnatoriaAcquistoArticolo.getQuantita(), fatturaAccompagnatoriaAcquistoArticolo.getPrezzo(), fatturaAccompagnatoriaAcquistoArticolo.getSconto());
    }

    private BigDecimal computeCosto(FatturaAccompagnatoriaAcquistoArticolo fatturaAccompagnatoriaAcquistoArticolo){

        return AccountingUtils.computeCosto(fatturaAccompagnatoriaAcquistoArticolo.getQuantita(), fatturaAccompagnatoriaAcquistoArticolo.getId().getArticoloId(), articoloService);
    }

    private BigDecimal computeTotale(FatturaAccompagnatoriaAcquistoArticolo fatturaAccompagnatoriaAcquistoArticolo){
        return AccountingUtils.computeTotale(fatturaAccompagnatoriaAcquistoArticolo.getQuantita(), fatturaAccompagnatoriaAcquistoArticolo.getPrezzo(), fatturaAccompagnatoriaAcquistoArticolo.getSconto(), null, fatturaAccompagnatoriaAcquistoArticolo.getId().getArticoloId(), articoloService);
    }

}