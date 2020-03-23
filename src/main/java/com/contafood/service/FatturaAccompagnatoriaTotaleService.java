package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.FatturaAccompagnatoriaTotale;
import com.contafood.repository.FatturaAccompagnatoriaTotaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class FatturaAccompagnatoriaTotaleService {

    private static Logger LOGGER = LoggerFactory.getLogger(FatturaAccompagnatoriaTotaleService.class);

    private final FatturaAccompagnatoriaTotaleRepository fatturaAccompagnatoriaTotaleRepository;

    @Autowired
    public FatturaAccompagnatoriaTotaleService(final FatturaAccompagnatoriaTotaleRepository fatturaAccompagnatoriaTotaleRepository){
        this.fatturaAccompagnatoriaTotaleRepository = fatturaAccompagnatoriaTotaleRepository;
    }

    public Set<FatturaAccompagnatoriaTotale> findAll(){
        LOGGER.info("Retrieving the list of 'fattura accompagnatoria totali'");
        Set<FatturaAccompagnatoriaTotale> fatturaAccompagnatoriaTotali = fatturaAccompagnatoriaTotaleRepository.findAll();
        LOGGER.info("Retrieved {} 'fattura accompagnatoria totali'", fatturaAccompagnatoriaTotali.size());
        return fatturaAccompagnatoriaTotali;
    }

    public FatturaAccompagnatoriaTotale create(FatturaAccompagnatoriaTotale fatturaAccompagnatoriaTotale){
        LOGGER.info("Creating 'fattura accompagnatoria totale'");
        fatturaAccompagnatoriaTotale.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        FatturaAccompagnatoriaTotale createdFatturaAccompagnatoriaTotale = fatturaAccompagnatoriaTotaleRepository.save(fatturaAccompagnatoriaTotale);
        LOGGER.info("Created 'fattura accompagnatoria totale' '{}'", createdFatturaAccompagnatoriaTotale);
        return createdFatturaAccompagnatoriaTotale;
    }

    public FatturaAccompagnatoriaTotale update(FatturaAccompagnatoriaTotale fatturaAccompagnatoriaTotale){
        LOGGER.info("Updating 'fattura accompagnatoria totale'");
        FatturaAccompagnatoriaTotale fatturaAccompagnatoriaTotaleCurrent = fatturaAccompagnatoriaTotaleRepository.findById(fatturaAccompagnatoriaTotale.getId()).orElseThrow(ResourceNotFoundException::new);
        fatturaAccompagnatoriaTotale.setDataInserimento(fatturaAccompagnatoriaTotaleCurrent.getDataInserimento());
        fatturaAccompagnatoriaTotale.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        FatturaAccompagnatoriaTotale updatedFatturaAccompagnatoriaTotale = fatturaAccompagnatoriaTotaleRepository.save(fatturaAccompagnatoriaTotale);
        LOGGER.info("Updated 'fattura accompagnatoria totale' '{}'", updatedFatturaAccompagnatoriaTotale);
        return updatedFatturaAccompagnatoriaTotale;
    }

    public void deleteByFatturaAccompagnatoriaId(Long fatturaAccompagnatoriaId){
        LOGGER.info("Deleting 'fattura articolo' by 'fattura accompagnatoria' '{}'", fatturaAccompagnatoriaId);
        fatturaAccompagnatoriaTotaleRepository.deleteByFatturaAccompagnatoriaId(fatturaAccompagnatoriaId);
        LOGGER.info("Deleted 'ddt articolo' by 'fattura accompagnatoria' '{}'", fatturaAccompagnatoriaId);
    }

}
