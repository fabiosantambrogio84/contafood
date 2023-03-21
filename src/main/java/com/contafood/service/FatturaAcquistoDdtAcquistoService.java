package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.FatturaAcquistoDdtAcquisto;
import com.contafood.repository.FatturaAcquistoDdtAcquistoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Slf4j
@Service
public class FatturaAcquistoDdtAcquistoService {

    private final FatturaAcquistoDdtAcquistoRepository fatturaAcquistoDdtAcquistoRepository;

    @Autowired
    public FatturaAcquistoDdtAcquistoService(final FatturaAcquistoDdtAcquistoRepository fatturaAcquistoDdtAcquistoRepository){
        this.fatturaAcquistoDdtAcquistoRepository = fatturaAcquistoDdtAcquistoRepository;
    }

    public FatturaAcquistoDdtAcquisto create(FatturaAcquistoDdtAcquisto fatturaAcquistoDdtAcquisto){
        log.info("Creating 'fattura acquisto ddt acquisto'");
        fatturaAcquistoDdtAcquisto.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        FatturaAcquistoDdtAcquisto createdFatturaAcquistoDdtAcquisto = fatturaAcquistoDdtAcquistoRepository.save(fatturaAcquistoDdtAcquisto);
        log.info("Created 'fattura acquisto ddt acquisto' '{}'", createdFatturaAcquistoDdtAcquisto);
        return createdFatturaAcquistoDdtAcquisto;
    }

    public FatturaAcquistoDdtAcquisto update(FatturaAcquistoDdtAcquisto fatturaAcquistoDdtAcquisto){
        log.info("Updating 'fattura acquisto ddt acquisto'");
        FatturaAcquistoDdtAcquisto fatturaAcquistoDdtAcquistoCurrent = fatturaAcquistoDdtAcquistoRepository.findById(fatturaAcquistoDdtAcquisto.getId()).orElseThrow(ResourceNotFoundException::new);
        fatturaAcquistoDdtAcquisto.setDataInserimento(fatturaAcquistoDdtAcquistoCurrent.getDataInserimento());
        fatturaAcquistoDdtAcquisto.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        FatturaAcquistoDdtAcquisto updatedFatturaAcquistoDdtAcquisto = fatturaAcquistoDdtAcquistoRepository.save(fatturaAcquistoDdtAcquisto);
        log.info("Updated 'fattura acquisto ddt acquisto' '{}'", updatedFatturaAcquistoDdtAcquisto);
        return updatedFatturaAcquistoDdtAcquisto;
    }

    public void deleteByFatturaAcquistoId(Long fatturaAcquistoId){
        log.info("Deleting 'fattura ddts' by 'fattura' '{}'", fatturaAcquistoId);
        fatturaAcquistoDdtAcquistoRepository.deleteByFatturaAcquistoId(fatturaAcquistoId);
        log.info("Deleted 'fattura ddts' by 'fattura' '{}'", fatturaAcquistoId);
    }

}
