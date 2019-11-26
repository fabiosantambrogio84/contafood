package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.ArticoloImmagine;
import com.contafood.repository.ArticoloImmagineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ArticoloImmagineService {

    private static Logger LOGGER = LoggerFactory.getLogger(ArticoloImmagineService.class);

    private final ArticoloImmagineRepository articoloImmagineRepository;

    private final FileStorageService fileStorageService;

    @Autowired
    public ArticoloImmagineService(final ArticoloImmagineRepository articoloImmagineRepository, final FileStorageService fileStorageService){
        this.articoloImmagineRepository = articoloImmagineRepository;
        this.fileStorageService = fileStorageService;
    }

    public Set<ArticoloImmagine> getAll(){
        LOGGER.info("Retrieving the list of 'articoloImmagini'");
        Set<ArticoloImmagine> articoloImmagini = articoloImmagineRepository.findAll();
        LOGGER.info("Retrieved {} 'articoloImmagini'", articoloImmagini.size());
        return articoloImmagini;
    }

    public ArticoloImmagine getOne(Long articoloImmagineId){
        LOGGER.info("Retrieving 'articoloImmagine' '{}'", articoloImmagineId);
        ArticoloImmagine articoloImmagine = articoloImmagineRepository.findById(articoloImmagineId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'articoloImmagine' '{}'", articoloImmagine);
        return articoloImmagine;
    }

    public ArticoloImmagine create(ArticoloImmagine articoloImmagine){
        LOGGER.info("Creating 'articoloImmagine'");
        articoloImmagine.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ArticoloImmagine createdArticoloImmagine = articoloImmagineRepository.save(articoloImmagine);
        LOGGER.info("Created 'articoloImmagine' '{}'", createdArticoloImmagine);
        return createdArticoloImmagine;
    }

    public ArticoloImmagine update(ArticoloImmagine articoloImmagine){
        LOGGER.info("Updating 'articoloImmagine'");
        ArticoloImmagine articoloImmagineCurrent = articoloImmagineRepository.findById(articoloImmagine.getId()).orElseThrow(ResourceNotFoundException::new);
        articoloImmagine.setDataInserimento(articoloImmagineCurrent.getDataInserimento());
        ArticoloImmagine updatedArticoloImmagine = articoloImmagineRepository.save(articoloImmagine);
        LOGGER.info("Updated 'articoloImmagine' '{}'", updatedArticoloImmagine);
        return updatedArticoloImmagine;
    }

    public void delete(Long articoloImmagineId){
        LOGGER.info("Deleting 'articoloImmagine' '{}'", articoloImmagineId);
        ArticoloImmagine articoloImmagine = articoloImmagineRepository.findById(articoloImmagineId).orElseThrow(ResourceNotFoundException::new);
        fileStorageService.deleteFile(articoloImmagine.getFileCompletePath());
        articoloImmagineRepository.deleteById(articoloImmagineId);
        LOGGER.info("Deleted 'articoloImmagine' '{}'", articoloImmagineId);
    }

    public void deleteByArticoloId(Long articoloId){
        LOGGER.info("Deleting all 'articoliImmagine' of articoloId '{}'", articoloId);
        List<ArticoloImmagine> articoloImmagini = articoloImmagineRepository.findByArticoloId(articoloId);
        articoloImmagini.stream().forEach(ai -> {
            delete(ai.getId());
        });
        LOGGER.info("Deleted all 'articoliImmagine' '{}'", articoloId);
    }

    public List<ArticoloImmagine> getByArticoloId(Long articoloId){
        LOGGER.info("Retrieving the list of 'articoloImmagini' for 'articolo' '{}'", articoloId);
        List<ArticoloImmagine> articoloImmagini = articoloImmagineRepository.findByArticoloId(articoloId);
        LOGGER.info("Retrieved {} 'articoloImmagini'", articoloImmagini.size());
        return articoloImmagini;
    }
}
