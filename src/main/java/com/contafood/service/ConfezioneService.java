package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Confezione;
import com.contafood.repository.ConfezioneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ConfezioneService {

    private static Logger LOGGER = LoggerFactory.getLogger(ConfezioneService.class);

    private final ConfezioneRepository confezioneRepository;

    @Autowired
    public ConfezioneService(final ConfezioneRepository confezioneRepository){
        this.confezioneRepository = confezioneRepository;
    }

    public Set<Confezione> getAll(){
        LOGGER.info("Retrieving the list of 'confezioni'");
        Set<Confezione> confezioni = confezioneRepository.findAll();
        LOGGER.info("Retrieved {} 'confezioni'", confezioni.size());
        return confezioni;
    }

    public Confezione getOne(Long confezioneId){
        LOGGER.info("Retrieving 'confezione' '{}'", confezioneId);
        Confezione confezione = confezioneRepository.findById(confezioneId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'confezione' '{}'", confezione);
        return confezione;
    }

    public Confezione create(Confezione confezione){
        LOGGER.info("Creating 'confezione'");
        Confezione createdConfezione = confezioneRepository.save(confezione);
        LOGGER.info("Created 'confezione' '{}'", confezione);
        return createdConfezione;
    }

    public Confezione update(Confezione confezione){
        LOGGER.info("Updating 'confezione'");
        Confezione updatedConfezione = confezioneRepository.save(confezione);
        LOGGER.info("Created 'confezione' '{}'", updatedConfezione);
        return updatedConfezione;
    }

    public void delete(Long confezioneId){
        LOGGER.info("Deleting 'confezione' '{}'", confezioneId);
        confezioneRepository.deleteById(confezioneId);
        LOGGER.info("Deleted 'confezione' '{}'", confezioneId);
    }
}
