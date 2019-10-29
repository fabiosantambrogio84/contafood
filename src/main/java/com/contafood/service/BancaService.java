package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Banca;
import com.contafood.repository.BancaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BancaService {

    private static Logger LOGGER = LoggerFactory.getLogger(BancaService.class);

    private final BancaRepository bancaRepository;

    @Autowired
    public BancaService(final BancaRepository bancaRepository){
        this.bancaRepository = bancaRepository;
    }

    public Set<Banca> getAll(){
        LOGGER.info("Retrieving the list of 'banche'");
        Set<Banca> banche = bancaRepository.findAll();
        LOGGER.info("Retrieved {} 'banche'", banche.size());
        return banche;
    }

    public Banca getOne(Long bancaId){
        LOGGER.info("Retrieving 'banca' '{}'", bancaId);
        Banca banca = bancaRepository.findById(bancaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'banca' '{}'", banca);
        return banca;
    }

    public Banca create(Banca banca){
        LOGGER.info("Creating 'banca'");
        Banca createdBanca = bancaRepository.save(banca);
        LOGGER.info("Created 'banca' '{}'", createdBanca);
        return createdBanca;
    }

    public Banca update(Banca banca){
        LOGGER.info("Updating 'banca'");
        Banca updatedBanca = bancaRepository.save(banca);
        LOGGER.info("Updated 'banca' '{}'", updatedBanca);
        return updatedBanca;
    }

    public void delete(Long bancaId){
        LOGGER.info("Deleting 'banca' '{}'", bancaId);
        bancaRepository.deleteById(bancaId);
        LOGGER.info("Deleted 'banca' '{}'", bancaId);
    }
}
