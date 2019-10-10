package com.contafood.service;

import com.contafood.model.ProduzioneConfezione;
import com.contafood.repository.ProduzioneConfezioneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProduzioneConfezioneService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProduzioneConfezioneService.class);

    private final ProduzioneConfezioneRepository produzioneConfezioneRepository;

    @Autowired
    public ProduzioneConfezioneService(final ProduzioneConfezioneRepository produzioneConfezioneRepository){
        this.produzioneConfezioneRepository = produzioneConfezioneRepository;
    }

    public Set<ProduzioneConfezione> findAll(){
        LOGGER.info("Retrieving the list of 'produzione confezioni'");
        Set<ProduzioneConfezione> produzioneConfezioni = produzioneConfezioneRepository.findAll();
        LOGGER.info("Retrieved {} 'produzione confezioni'", produzioneConfezioni.size());
        return produzioneConfezioni;
    }

    public Set<ProduzioneConfezione> findByProduzioneId(Long produzioneId){
        LOGGER.info("Retrieving the list of 'produzione confezioni' for 'produzione' '{}'", produzioneId);
        Set<ProduzioneConfezione> produzioneConfezioni = produzioneConfezioneRepository.findByProduzioneId(produzioneId);
        LOGGER.info("Retrieved {} 'produzione confezioni' for 'produzione' '{}'", produzioneConfezioni.size(), produzioneId);
        return produzioneConfezioni;
    }

    public Set<ProduzioneConfezione> findByConfezioneId(Long confezioneId){
        LOGGER.info("Retrieving the list of 'produzione confezioni' for 'confezione' '{}'", confezioneId);
        Set<ProduzioneConfezione> produzioneConfezioni = produzioneConfezioneRepository.findByConfezioneId(confezioneId);
        LOGGER.info("Retrieved {} 'produzione confezioni' for 'confezione' '{}'", produzioneConfezioni.size(), confezioneId);
        return produzioneConfezioni;
    }

    public ProduzioneConfezione create(ProduzioneConfezione produzioneConfezione){
        LOGGER.info("Creating 'produzione confezione'");
        ProduzioneConfezione createdProduzioneConfezione = produzioneConfezioneRepository.save(produzioneConfezione);
        LOGGER.info("Created 'produzione confezione' '{}'", createdProduzioneConfezione);
        return createdProduzioneConfezione;
    }

    public ProduzioneConfezione update(ProduzioneConfezione produzioneConfezione){
        LOGGER.info("Updating 'produzione confezione'");
        ProduzioneConfezione updatedProduzioneConfezione = produzioneConfezioneRepository.save(produzioneConfezione);
        LOGGER.info("Updated 'produzione confezione' '{}'", updatedProduzioneConfezione);
        return updatedProduzioneConfezione;
    }

    public void deleteByProduzioneId(Long produzioneId){
        LOGGER.info("Deleting 'produzione confezione' by 'produzione' '{}'", produzioneId);
        produzioneConfezioneRepository.deleteByProduzioneId(produzioneId);
        LOGGER.info("Deleted 'produzione confezione' by 'produzione' '{}'", produzioneId);
    }

    public void deleteByConfezioneId(Long confezioneId){
        LOGGER.info("Deleting 'produzione confezione' by 'confezione' '{}'", confezioneId);
        produzioneConfezioneRepository.deleteByConfezioneId(confezioneId);
        LOGGER.info("Deleted 'produzione confezione' by 'confezione' '{}'", confezioneId);
    }
}
