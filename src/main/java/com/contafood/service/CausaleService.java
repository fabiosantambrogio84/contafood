package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Causale;
import com.contafood.repository.CausaleRepository;
import com.contafood.util.enumeration.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class CausaleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CausaleService.class);

    private final CausaleRepository causaleRepository;


    @Autowired
    public CausaleService(final CausaleRepository causaleRepository){
        this.causaleRepository = causaleRepository;
    }

    public Set<Causale> getAll(){
        LOGGER.info("Retrieving the list of 'causali'");
        Set<Causale> causali = causaleRepository.findAllByOrderByDescrizione();
        LOGGER.info("Retrieved {} 'causali'", causali.size());
        return causali;
    }

    public Causale getOne(Long causaleId){
        LOGGER.info("Retrieving 'causale' '{}'", causaleId);
        Causale causale = causaleRepository.findById(causaleId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'causale' '{}'", causale);
        return causale;
    }

    public Causale create(Causale causale){
        LOGGER.info("Creating 'causale'");
        Optional<Causale> optionalCausale = causaleRepository.findByDescrizione(causale.getDescrizione());
        if(optionalCausale.isPresent()){
            throw new ResourceAlreadyExistingException(Resource.CAUSALE);
        }
        causale.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Causale createdCausale = causaleRepository.save(causale);
        LOGGER.info("Created 'causale' '{}'", createdCausale);
        return createdCausale;
    }

    public Causale update(Causale causale){
        LOGGER.info("Updating 'causale'");
        Causale currentCausale = causaleRepository.findById(causale.getId()).orElseThrow(ResourceNotFoundException::new);
        Optional<Causale> optionalCausale = causaleRepository.findByDescrizione(causale.getDescrizione());
        if(optionalCausale.isPresent() && !optionalCausale.get().getId().equals(causale.getId())){
            throw new ResourceAlreadyExistingException(Resource.CAUSALE);
        }
        causale.setDataInserimento(currentCausale.getDataInserimento());
        causale.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Causale updatedCausale = causaleRepository.save(causale);
        LOGGER.info("Updated 'causale' '{}'", updatedCausale);
        return updatedCausale;
    }

    public void delete(Long causaleId){
        LOGGER.info("Deleting 'causale' '{}'", causaleId);
        causaleRepository.deleteById(causaleId);
        LOGGER.info("Deleted 'causale' '{}'", causaleId);
    }
}
