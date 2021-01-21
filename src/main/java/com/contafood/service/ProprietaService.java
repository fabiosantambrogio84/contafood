package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Proprieta;
import com.contafood.repository.ProprietaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class ProprietaService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProprietaService.class);

    private final ProprietaRepository proprietaRepository;

    @Autowired
    public ProprietaService(final ProprietaRepository proprietaRepository){
        this.proprietaRepository = proprietaRepository;
    }

    public Set<Proprieta> getAll(){
        LOGGER.info("Retrieving the list of 'proprieta'");
        Set<Proprieta> proprieta = proprietaRepository.findAllByOrderByNome();
        LOGGER.info("Retrieved {} 'proprieta'", proprieta.size());
        return proprieta;
    }

    public Proprieta getOne(Long proprietaId){
        LOGGER.info("Retrieving 'proprieta' '{}'", proprietaId);
        Proprieta proprieta = proprietaRepository.findById(proprietaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'proprieta' '{}'", proprieta);
        return proprieta;
    }

    public Proprieta findByNome(String proprietaNome){
        LOGGER.info("Retrieving 'proprieta' by name '{}'", proprietaNome);
        Proprieta proprieta = proprietaRepository.findByNome(proprietaNome).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'proprieta' by name '{}'", proprieta);
        return proprieta;
    }

    public Proprieta update(Proprieta proprieta){
        LOGGER.info("Updating 'proprieta'");
        proprieta.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Proprieta updatedProprieta = proprietaRepository.save(proprieta);
        LOGGER.info("Updated 'proprieta' '{}'", updatedProprieta);
        return updatedProprieta;
    }

}
