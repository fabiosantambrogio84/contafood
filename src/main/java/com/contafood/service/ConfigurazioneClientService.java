package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.ConfigurazioneClient;
import com.contafood.repository.ConfigurazioneClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class ConfigurazioneClientService {

    private static Logger LOGGER = LoggerFactory.getLogger(ConfigurazioneClientService.class);

    private final ConfigurazioneClientRepository configurazioneClientRepository;

    public ConfigurazioneClientService(final ConfigurazioneClientRepository configurazioneClientRepository){
        this.configurazioneClientRepository = configurazioneClientRepository;
    }

    public Set<ConfigurazioneClient> getAll(){
        LOGGER.info("Retrieving the list of 'configurazioni client'");
        Set<ConfigurazioneClient> configurazioni = configurazioneClientRepository.findAllByOrderByDescrizione();
        LOGGER.info("Retrieved {} 'configurazioni client'", configurazioni.size());
        return configurazioni;
    }

    public ConfigurazioneClient getOne(Long idConfigurazioneClient){
        LOGGER.info("Retrieving 'configurazione client' '{}'", idConfigurazioneClient);
        ConfigurazioneClient configurazione = configurazioneClientRepository.findById(idConfigurazioneClient).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'configurazione client' '{}'", configurazione);
        return configurazione;
    }

    public ConfigurazioneClient update(ConfigurazioneClient configurazioneClient){
        LOGGER.info("Updating 'configurazione client'");
        ConfigurazioneClient currentConfigurazioneClient = configurazioneClientRepository.findById(configurazioneClient.getId()).orElseThrow(ResourceNotFoundException::new);
        configurazioneClient.setCodice(currentConfigurazioneClient.getCodice());
        configurazioneClient.setDataInserimento(currentConfigurazioneClient.getDataInserimento());
        configurazioneClient.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ConfigurazioneClient updatedConfigurazioneClient = configurazioneClientRepository.save(configurazioneClient);
        LOGGER.info("Updated 'configurazione client' '{}'", updatedConfigurazioneClient);
        return updatedConfigurazioneClient;
    }

}
