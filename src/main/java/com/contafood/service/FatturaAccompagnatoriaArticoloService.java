package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Articolo;
import com.contafood.model.FatturaAccompagnatoriaArticolo;
import com.contafood.repository.FatturaAccompagnatoriaArticoloRepository;
import com.contafood.util.AccountingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class FatturaAccompagnatoriaArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(FatturaAccompagnatoriaArticoloService.class);

    private final FatturaAccompagnatoriaArticoloRepository fatturaAccompagnatoriaArticoloRepository;

    private final ArticoloService articoloService;

    @Autowired
    public FatturaAccompagnatoriaArticoloService(final FatturaAccompagnatoriaArticoloRepository fatturaAccompagnatoriaArticoloRepository, final ArticoloService articoloService){
        this.fatturaAccompagnatoriaArticoloRepository = fatturaAccompagnatoriaArticoloRepository;
        this.articoloService = articoloService;
    }

    public Set<FatturaAccompagnatoriaArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'fattura accompagnatoria articoli'");
        Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = fatturaAccompagnatoriaArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'fattura accompagnatoria articoli'", fatturaAccompagnatoriaArticoli.size());
        return fatturaAccompagnatoriaArticoli;
    }

    public FatturaAccompagnatoriaArticolo create(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        LOGGER.info("Creating 'fattura accompagnatoria articolo'");
        fatturaAccompagnatoriaArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        fatturaAccompagnatoriaArticolo.setImponibile(computeImponibile(fatturaAccompagnatoriaArticolo));
        fatturaAccompagnatoriaArticolo.setCosto(computeCosto(fatturaAccompagnatoriaArticolo));
        fatturaAccompagnatoriaArticolo.setTotale(computeTotale(fatturaAccompagnatoriaArticolo));

        FatturaAccompagnatoriaArticolo createdFatturaAccompagnatoriaArticolo = fatturaAccompagnatoriaArticoloRepository.save(fatturaAccompagnatoriaArticolo);
        LOGGER.info("Created 'fattura accompagnatoria articolo' '{}'", createdFatturaAccompagnatoriaArticolo);
        return createdFatturaAccompagnatoriaArticolo;
    }

    public FatturaAccompagnatoriaArticolo update(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        LOGGER.info("Updating 'fattura accompagnatoria articolo'");
        FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticoloCurrent = fatturaAccompagnatoriaArticoloRepository.findById(fatturaAccompagnatoriaArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        fatturaAccompagnatoriaArticolo.setDataInserimento(fatturaAccompagnatoriaArticoloCurrent.getDataInserimento());
        fatturaAccompagnatoriaArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        fatturaAccompagnatoriaArticolo.setImponibile(computeImponibile(fatturaAccompagnatoriaArticolo));
        fatturaAccompagnatoriaArticolo.setCosto(computeCosto(fatturaAccompagnatoriaArticolo));
        fatturaAccompagnatoriaArticolo.setTotale(computeTotale(fatturaAccompagnatoriaArticolo));

        FatturaAccompagnatoriaArticolo updatedFatturaAccompagnatoriaArticolo = fatturaAccompagnatoriaArticoloRepository.save(fatturaAccompagnatoriaArticolo);
        LOGGER.info("Updated 'fattura accompagnatoria articolo' '{}'", updatedFatturaAccompagnatoriaArticolo);
        return updatedFatturaAccompagnatoriaArticolo;
    }

    public void deleteByFatturaAccompagnatoriaId(Long fatturaAccompagnatoriaId){
        LOGGER.info("Deleting 'fattura articolo' by 'fattura accompagnatoria' '{}'", fatturaAccompagnatoriaId);
        fatturaAccompagnatoriaArticoloRepository.deleteByFatturaAccompagnatoriaId(fatturaAccompagnatoriaId);
        LOGGER.info("Deleted 'ddt articolo' by 'fattura accompagnatoria' '{}'", fatturaAccompagnatoriaId);
    }

    public Articolo getArticolo(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        Long articoloId = fatturaAccompagnatoriaArticolo.getId().getArticoloId();
        return articoloService.getOne(articoloId);
    }

    private BigDecimal computeImponibile(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){

        return AccountingUtils.computeImponibile(fatturaAccompagnatoriaArticolo.getQuantita(), fatturaAccompagnatoriaArticolo.getPrezzo(), fatturaAccompagnatoriaArticolo.getSconto());
    }

    private BigDecimal computeCosto(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){

        return AccountingUtils.computeCosto(fatturaAccompagnatoriaArticolo.getQuantita(), fatturaAccompagnatoriaArticolo.getId().getArticoloId(), articoloService);
    }

    private BigDecimal computeTotale(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        return AccountingUtils.computeTotale(fatturaAccompagnatoriaArticolo.getQuantita(), fatturaAccompagnatoriaArticolo.getPrezzo(), fatturaAccompagnatoriaArticolo.getSconto(), fatturaAccompagnatoriaArticolo.getId().getArticoloId(), articoloService);
    }

}
