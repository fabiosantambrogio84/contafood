package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Proprieta;
import com.contafood.repository.ProprietaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Slf4j
@Service
public class ProprietaService {

    private final ProprietaRepository proprietaRepository;

    @Autowired
    public ProprietaService(final ProprietaRepository proprietaRepository){
        this.proprietaRepository = proprietaRepository;
    }

    public Set<Proprieta> getAll(){
        log.info("Retrieving the list of 'proprieta'");
        Set<Proprieta> proprieta = proprietaRepository.findAllByOrderByNome();
        log.info("Retrieved {} 'proprieta'", proprieta.size());
        return proprieta;
    }

    public Proprieta getOne(Long proprietaId){
        log.info("Retrieving 'proprieta' '{}'", proprietaId);
        Proprieta proprieta = proprietaRepository.findById(proprietaId).orElseThrow(ResourceNotFoundException::new);
        log.info("Retrieved 'proprieta' '{}'", proprieta);
        return proprieta;
    }

    public Proprieta findByNome(String proprietaNome){
        log.info("Retrieving 'proprieta' by name '{}'", proprietaNome);
        Proprieta proprieta = proprietaRepository.findByNome(proprietaNome).orElseThrow(ResourceNotFoundException::new);
        log.info("Retrieved 'proprieta' by name '{}'", proprieta);
        return proprieta;
    }

    public Proprieta update(Proprieta proprieta){
        log.info("Updating 'proprieta'");
        Proprieta currentProprieta = proprietaRepository.findById(proprieta.getId()).orElseThrow(ResourceNotFoundException::new);
        proprieta.setDataInserimento(currentProprieta.getDataInserimento());
        proprieta.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Proprieta updatedProprieta = proprietaRepository.save(proprieta);
        log.info("Updated 'proprieta' '{}'", updatedProprieta);
        return updatedProprieta;
    }

}