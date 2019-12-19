package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.ScontoArticolo;
import com.contafood.repository.ScontoArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ScontoArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(ScontoArticoloService.class);

    private final ScontoArticoloRepository scontoArticoloRepository;

    @Autowired
    public ScontoArticoloService(final ScontoArticoloRepository scontoArticoloRepository){
        this.scontoArticoloRepository = scontoArticoloRepository;
    }

    public List<ScontoArticolo> getAll(){
        LOGGER.info("Retrieving the list of all 'scontiArticoli'");
        List<ScontoArticolo> scontiArticoli = scontoArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'scontiArticoli'", scontiArticoli.size());
        return scontiArticoli;
    }

    public List<ScontoArticolo> getByScontoId(Long idSconto){
        LOGGER.info("Retrieving the list of 'scontiArticoli' of sconto '{}'", idSconto);
        List<ScontoArticolo> scontiArticoli = scontoArticoloRepository.findByScontoId(idSconto);
        LOGGER.info("Retrieved {} 'scontiArticoli'", scontiArticoli.size());
        return scontiArticoli;
    }

    public List<ScontoArticolo> getByArticoloId(Long idArticolo){
        LOGGER.info("Retrieving the list of 'scontiArticoli' of articolo '{}'", idArticolo);
        List<ScontoArticolo> scontiArticoli = scontoArticoloRepository.findByArticoloId(idArticolo);
        LOGGER.info("Retrieved {} 'scontiArticoli'", scontiArticoli.size());
        return scontiArticoli;
    }

    public ScontoArticolo getOne(Long idScontoArticolo){
        LOGGER.info("Retrieving 'scontoArticolo' '{}'", idScontoArticolo);
        ScontoArticolo scontoArticolo = scontoArticoloRepository.findById(idScontoArticolo).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'scontoArticolo' '{}'", scontoArticolo);
        return scontoArticolo;
    }

    public List<ScontoArticolo> create(List<ScontoArticolo> scontiArticoli){
        LOGGER.info("Creating 'scontiArticoli'");
        scontiArticoli.forEach(sa-> {
            sa.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            sa.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
            ScontoArticolo createdScontoArticolo = scontoArticoloRepository.save(sa);
            LOGGER.info("Created 'scontoArticolo' '{}'", createdScontoArticolo);
        });
        return scontiArticoli;
    }

    public ScontoArticolo update(ScontoArticolo scontoArticolo){
        LOGGER.info("Updating 'scontoArticolo'");
        ScontoArticolo scontoArticoloCurrent = scontoArticoloRepository.findById(scontoArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        scontoArticolo.setDataInserimento(scontoArticoloCurrent.getDataInserimento());
        scontoArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ScontoArticolo updatedScontoArticolo = scontoArticoloRepository.save(scontoArticolo);
        LOGGER.info("Updated 'scontoArticolo' '{}'", updatedScontoArticolo);
        return updatedScontoArticolo;
    }

    public List<ScontoArticolo> bulkInsertOrUpdate(List<ScontoArticolo> scontiArticoli){
        LOGGER.info("Inserting/updating 'scontiArticoli'");
        scontoArticoloRepository.saveAll(scontiArticoli);
        LOGGER.info("Inserted/updated 'scontiArticoli' '{}'", scontiArticoli);
        return scontiArticoli;
    }

    public void delete(Long scontoArticoloId){
        LOGGER.info("Deleting 'scontoArticolo' '{}'", scontoArticoloId);
        scontoArticoloRepository.deleteById(scontoArticoloId);
        LOGGER.info("Deleted 'scontoArticolo' '{}'", scontoArticoloId);
    }

    public void deleteByScontoId(Long idSconto){
        LOGGER.info("Deleting 'scontiArticoli' of sconto '{}'", idSconto);
        scontoArticoloRepository.deleteByScontoId(idSconto);
        LOGGER.info("Deleted 'scontiArticoli' of sconto '{}'", idSconto);
    }

    public void deleteByArticoloId(Long idArticolo){
        LOGGER.info("Deleting 'scontiArticoli' of articolo '{}'", idArticolo);
        scontoArticoloRepository.deleteByArticoloId(idArticolo);
        LOGGER.info("Deleted 'scontiArticoli' of articolo '{}'", idArticolo);
    }

}
