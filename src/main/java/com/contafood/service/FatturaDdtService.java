package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.FatturaDdt;
import com.contafood.repository.FatturaDdtRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class FatturaDdtService {

    private static Logger LOGGER = LoggerFactory.getLogger(FatturaDdtService.class);

    private final FatturaDdtRepository fatturaDdtRepository;

    @Autowired
    public FatturaDdtService(final FatturaDdtRepository fatturaDdtRepository){
        this.fatturaDdtRepository = fatturaDdtRepository;
    }

    public Set<FatturaDdt> findAll(){
        LOGGER.info("Retrieving the list of 'fattura ddts'");
        Set<FatturaDdt> fatturaDdts = fatturaDdtRepository.findAll();
        LOGGER.info("Retrieved {} 'fattura ddts'", fatturaDdts.size());
        return fatturaDdts;
    }

    public FatturaDdt create(FatturaDdt fatturaDdt){
        LOGGER.info("Creating 'fattura ddt'");
        fatturaDdt.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        FatturaDdt createdFatturaDdt = fatturaDdtRepository.save(fatturaDdt);
        LOGGER.info("Created 'fattura ddt' '{}'", createdFatturaDdt);
        return createdFatturaDdt;
    }

    public FatturaDdt update(FatturaDdt fatturaDdt){
        LOGGER.info("Updating 'fattura ddt'");
        FatturaDdt fatturaDdtCurrent = fatturaDdtRepository.findById(fatturaDdt.getId()).orElseThrow(ResourceNotFoundException::new);
        fatturaDdt.setDataInserimento(fatturaDdtCurrent.getDataInserimento());
        fatturaDdt.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        FatturaDdt updatedFatturaDdt = fatturaDdtRepository.save(fatturaDdt);
        LOGGER.info("Updated 'fattura ddt' '{}'", updatedFatturaDdt);
        return updatedFatturaDdt;
    }

    public void deleteByFatturaId(Long fatturaId){
        LOGGER.info("Deleting 'fattura ddts' by 'fattura' '{}'", fatturaId);
        fatturaDdtRepository.deleteByFatturaId(fatturaId);
        LOGGER.info("Deleted 'fattura ddts' by 'fattura' '{}'", fatturaId);
    }

}
