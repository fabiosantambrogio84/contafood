package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.FatturaAccompagnatoriaAcquistoTotale;
import com.contafood.repository.FatturaAccompagnatoriaAcquistoTotaleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Slf4j
@Service
public class FatturaAccompagnatoriaAcquistoTotaleService {

    private final FatturaAccompagnatoriaAcquistoTotaleRepository fatturaAccompagnatoriaAcquistoTotaleRepository;

    @Autowired
    public FatturaAccompagnatoriaAcquistoTotaleService(final FatturaAccompagnatoriaAcquistoTotaleRepository fatturaAccompagnatoriaAcquistoTotaleRepository){
        this.fatturaAccompagnatoriaAcquistoTotaleRepository = fatturaAccompagnatoriaAcquistoTotaleRepository;
    }

    public Set<FatturaAccompagnatoriaAcquistoTotale> findAll(){
        log.info("Retrieving the list of 'fattura accompagnatoria acquisto totali'");
        Set<FatturaAccompagnatoriaAcquistoTotale> fatturaAccompagnatoriaAcquistoTotali = fatturaAccompagnatoriaAcquistoTotaleRepository.findAll();
        log.info("Retrieved {} 'fattura accompagnatoria acquisto totali'", fatturaAccompagnatoriaAcquistoTotali.size());
        return fatturaAccompagnatoriaAcquistoTotali;
    }

    public FatturaAccompagnatoriaAcquistoTotale create(FatturaAccompagnatoriaAcquistoTotale fatturaAccompagnatoriaAcquistoTotale){
        log.info("Creating 'fattura accompagnatoria acquisto totale'");
        fatturaAccompagnatoriaAcquistoTotale.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        FatturaAccompagnatoriaAcquistoTotale createdFatturaAccompagnatoriaAcquistoTotale = fatturaAccompagnatoriaAcquistoTotaleRepository.save(fatturaAccompagnatoriaAcquistoTotale);
        log.info("Created 'fattura accompagnatoria acquisto totale' '{}'", createdFatturaAccompagnatoriaAcquistoTotale);
        return createdFatturaAccompagnatoriaAcquistoTotale;
    }

    public FatturaAccompagnatoriaAcquistoTotale update(FatturaAccompagnatoriaAcquistoTotale fatturaAccompagnatoriaAcquistoTotale){
        log.info("Updating 'fattura accompagnatoria acquisto totale'");
        FatturaAccompagnatoriaAcquistoTotale fatturaAccompagnatoriaAcquistoTotaleCurrent = fatturaAccompagnatoriaAcquistoTotaleRepository.findById(fatturaAccompagnatoriaAcquistoTotale.getId()).orElseThrow(ResourceNotFoundException::new);
        fatturaAccompagnatoriaAcquistoTotale.setDataInserimento(fatturaAccompagnatoriaAcquistoTotaleCurrent.getDataInserimento());
        fatturaAccompagnatoriaAcquistoTotale.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        FatturaAccompagnatoriaAcquistoTotale updatedFatturaAccompagnatoriaAcquistoTotale = fatturaAccompagnatoriaAcquistoTotaleRepository.save(fatturaAccompagnatoriaAcquistoTotale);
        log.info("Updated 'fattura accompagnatoria acquisto totale' '{}'", updatedFatturaAccompagnatoriaAcquistoTotale);
        return updatedFatturaAccompagnatoriaAcquistoTotale;
    }

    public void deleteByFatturaAccompagnatoriaAcquistoId(Long fatturaAccompagnatoriaAcquistoId){
        log.info("Deleting 'fattura totali' by 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquistoId);
        fatturaAccompagnatoriaAcquistoTotaleRepository.deleteByFatturaAccompagnatoriaAcquistoId(fatturaAccompagnatoriaAcquistoId);
        log.info("Deleted 'fattura totali' by 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquistoId);
    }

}