package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.AliquotaIva;
import com.contafood.repository.AliquotaIvaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AliquotaIvaService {

    private static Logger LOGGER = LoggerFactory.getLogger(AliquotaIvaService.class);

    private final AliquotaIvaRepository aliquotaIvaRepository;

    @Autowired
    public AliquotaIvaService(final AliquotaIvaRepository aliquotaIvaRepository){
        this.aliquotaIvaRepository = aliquotaIvaRepository;
    }

    public Set<AliquotaIva> getAll(){
        LOGGER.info("Retrieving the list of 'aliquoteIva'");
        Set<AliquotaIva> aliquoteIva = aliquotaIvaRepository.findAllByOrderByValore();
        LOGGER.info("Retrieved {} 'aliquoteIva'", aliquoteIva.size());
        return aliquoteIva;
    }

    public AliquotaIva getOne(Long aliquotaIvaId){
        LOGGER.info("Retrieving 'aliquotaIva' '{}'", aliquotaIvaId);
        AliquotaIva aliquotaIva = aliquotaIvaRepository.findById(aliquotaIvaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'aliquotaIva' '{}'", aliquotaIva);
        return aliquotaIva;
    }

    public AliquotaIva create(AliquotaIva aliquotaIva){
        LOGGER.info("Creating 'aliquotaIva'");
        AliquotaIva createdAliquotaIva = aliquotaIvaRepository.save(aliquotaIva);
        LOGGER.info("Created 'aliquotaIva' '{}'", createdAliquotaIva);
        return createdAliquotaIva;
    }

    public AliquotaIva update(AliquotaIva aliquotaIva){
        LOGGER.info("Updating 'aliquotaIva'");
        AliquotaIva updatedAliquotaIva = aliquotaIvaRepository.save(aliquotaIva);
        LOGGER.info("Updated 'aliquotaIva' '{}'", updatedAliquotaIva);
        return updatedAliquotaIva;
    }

    public void delete(Long aliquotaIvaId){
        LOGGER.info("Deleting 'aliquotaIva' '{}'", aliquotaIvaId);
        aliquotaIvaRepository.deleteById(aliquotaIvaId);
        LOGGER.info("Deleted 'aliquotaIva' '{}'", aliquotaIvaId);
    }
}
