package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.ScontoFornitore;
import com.contafood.repository.ScontoFornitoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ScontoFornitoreService {

    private static Logger LOGGER = LoggerFactory.getLogger(ScontoFornitoreService.class);

    private final ScontoFornitoreRepository scontoFornitoreRepository;

    @Autowired
    public ScontoFornitoreService(final ScontoFornitoreRepository scontoFornitoreRepository){
        this.scontoFornitoreRepository = scontoFornitoreRepository;
    }

    public List<ScontoFornitore> getAll(){
        LOGGER.info("Retrieving the list of all 'scontiFornitori'");
        List<ScontoFornitore> scontiFornitori = scontoFornitoreRepository.findAll();
        LOGGER.info("Retrieved {} 'scontiFornitori'", scontiFornitori.size());
        return scontiFornitori;
    }

    public List<ScontoFornitore> getByScontoId(Long idSconto){
        LOGGER.info("Retrieving the list of 'scontiFornitori' of sconto '{}'", idSconto);
        List<ScontoFornitore> scontiFornitori = scontoFornitoreRepository.findByScontoId(idSconto);
        LOGGER.info("Retrieved {} 'scontiFornitori'", scontiFornitori.size());
        return scontiFornitori;
    }

    public List<ScontoFornitore> getByFornitoreId(Long idFornitore){
        LOGGER.info("Retrieving the list of 'scontiFornitori' of fornitore '{}'", idFornitore);
        List<ScontoFornitore> scontiFornitori = scontoFornitoreRepository.findByFornitoreId(idFornitore);
        LOGGER.info("Retrieved {} 'scontiFornitori'", scontiFornitori.size());
        return scontiFornitori;
    }

    public ScontoFornitore getOne(Long idScontoFornitore){
        LOGGER.info("Retrieving 'scontoFornitore' '{}'", idScontoFornitore);
        ScontoFornitore scontoFornitore = scontoFornitoreRepository.findById(idScontoFornitore).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'scontoFornitore' '{}'", scontoFornitore);
        return scontoFornitore;
    }

    public List<ScontoFornitore> create(List<ScontoFornitore> scontiFornitori){
        LOGGER.info("Creating 'scontiFornitori'");
        scontiFornitori.forEach(sf-> {
            sf.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            sf.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
            ScontoFornitore createdScontoFornitore = scontoFornitoreRepository.save(sf);
            LOGGER.info("Created 'sconteFornitore' '{}'", createdScontoFornitore);
        });
        return scontiFornitori;
    }

    public ScontoFornitore update(ScontoFornitore scontoFornitore){
        LOGGER.info("Updating 'scontoFornitore'");
        ScontoFornitore scontoFornitoreCurrent = scontoFornitoreRepository.findById(scontoFornitore.getId()).orElseThrow(ResourceNotFoundException::new);
        scontoFornitore.setDataInserimento(scontoFornitoreCurrent.getDataInserimento());
        scontoFornitore.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ScontoFornitore updatedScontoArticolo = scontoFornitoreRepository.save(scontoFornitore);
        LOGGER.info("Updated 'scontoFornitore' '{}'", updatedScontoArticolo);
        return updatedScontoArticolo;
    }

    public List<ScontoFornitore> bulkInsertOrUpdate(List<ScontoFornitore> scontiFornitori){
        LOGGER.info("Inserting/updating 'scontiFornitori'");
        scontoFornitoreRepository.saveAll(scontiFornitori);
        LOGGER.info("Inserted/updated 'scontiFornitori' '{}'", scontiFornitori);
        return scontiFornitori;
    }

    public void delete(Long scontoFornitoreId){
        LOGGER.info("Deleting 'scontoFornitore' '{}'", scontoFornitoreId);
        scontoFornitoreRepository.deleteById(scontoFornitoreId);
        LOGGER.info("Deleted 'scontoFornitore' '{}'", scontoFornitoreId);
    }

    public void deleteByScontoId(Long idSconto){
        LOGGER.info("Deleting 'scontiFornitori' of sconto '{}'", idSconto);
        scontoFornitoreRepository.deleteByScontoId(idSconto);
        LOGGER.info("Deleted 'scontiFornitori' of sconto '{}'", idSconto);
    }

    public void deleteByFornitoreId(Long idFornitore){
        LOGGER.info("Deleting 'scontiFornitori' of articolo '{}'", idFornitore);
        scontoFornitoreRepository.deleteByFornitoreId(idFornitore);
        LOGGER.info("Deleted 'scontiFornitori' of articolo '{}'", idFornitore);
    }

}
