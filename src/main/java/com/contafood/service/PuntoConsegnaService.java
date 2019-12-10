package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.PuntoConsegna;
import com.contafood.repository.PuntoConsegnaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class PuntoConsegnaService {

    private static Logger LOGGER = LoggerFactory.getLogger(PuntoConsegnaService.class);

    private final PuntoConsegnaRepository puntoConsegnaRepository;

    private final TelefonataService telefonataService;

    @Autowired
    public PuntoConsegnaService(final PuntoConsegnaRepository puntoConsegnaRepository, final TelefonataService telefonataService){
        this.puntoConsegnaRepository = puntoConsegnaRepository;
        this.telefonataService = telefonataService;
    }

    public Set<PuntoConsegna> getAll(){
        LOGGER.info("Retrieving the list of 'puntiConsegna'");
        Set<PuntoConsegna> puntiConsegna = puntoConsegnaRepository.findAll();
        LOGGER.info("Retrieved {} 'puntiConsegna'", puntiConsegna.size());
        return puntiConsegna;
    }

    public PuntoConsegna getOne(Long puntoConsegnaId){
        LOGGER.info("Retrieving 'puntoConsegna' '{}'", puntoConsegnaId);
        PuntoConsegna puntoConsegna = puntoConsegnaRepository.findById(puntoConsegnaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'puntoConsegna' '{}'", puntoConsegna);
        return puntoConsegna;
    }

    public PuntoConsegna create(PuntoConsegna puntoConsegna){
        LOGGER.info("Creating 'puntoConsegna'");
        PuntoConsegna createdPuntoConsegna = puntoConsegnaRepository.save(puntoConsegna);
        LOGGER.info("Created 'puntoConsegna' '{}'", createdPuntoConsegna);
        return createdPuntoConsegna;
    }

    public PuntoConsegna update(PuntoConsegna puntoConsegna){
        LOGGER.info("Updating 'puntoConsegna'");
        PuntoConsegna updatedPuntoConsegna = puntoConsegnaRepository.save(puntoConsegna);
        LOGGER.info("Updated 'puntoConsegna' '{}'", updatedPuntoConsegna);
        return updatedPuntoConsegna;
    }

    @Transactional
    public void delete(Long puntoConsegnaId){
        LOGGER.info("Deleting 'puntoConsegna' '{}'", puntoConsegnaId);
        telefonataService.updateAfterDeletePuntoConsegna(puntoConsegnaId);
        puntoConsegnaRepository.deleteById(puntoConsegnaId);
        LOGGER.info("Deleted 'puntoConsegna' '{}'", puntoConsegnaId);
    }

    public List<PuntoConsegna> getByClienteId(Long clienteId){
        LOGGER.info("Retrieving the list of 'puntiConsegna' for 'cliente' '{}'", clienteId);
        List<PuntoConsegna> puntiConsegna = puntoConsegnaRepository.findByClienteId(clienteId);
        LOGGER.info("Retrieved {} 'puntiConsegna'", puntiConsegna.size());
        return puntiConsegna;
    }
}
