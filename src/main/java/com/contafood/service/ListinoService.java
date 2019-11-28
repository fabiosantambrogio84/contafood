package com.contafood.service;

import com.contafood.exception.ListinoBaseAlreadyExistingException;
import com.contafood.exception.ListinoTipologiaNotAllowedException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Listino;
import com.contafood.repository.ListinoRepository;
import com.contafood.util.TipologiaListino;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@Service
public class ListinoService {

    private static Logger LOGGER = LoggerFactory.getLogger(ListinoService.class);

    private final ListinoRepository listinoRepository;

    private final static String TIPOLOGIA_BASE = "BASE";

    @Autowired
    public ListinoService(final ListinoRepository listinoRepository){
        this.listinoRepository = listinoRepository;
    }

    public Set<Listino> getAll(){
        LOGGER.info("Retrieving the list of 'listini'");
        Set<Listino> listini = listinoRepository.findAllByOrderByTipologia();
        LOGGER.info("Retrieved {} 'listini'", listini.size());
        return listini;
    }

    public Listino getOne(Long listinoId){
        LOGGER.info("Retrieving 'listino' '{}'", listinoId);
        Listino listino = listinoRepository.findById(listinoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'listino' '{}'", listino);
        return listino;
    }

    public Listino create(Listino listino){
        LOGGER.info("Creating 'listino'");
        checkListinoTipologia(listino.getTipologia());
        if(listino.getTipologia().equals(TipologiaListino.BASE.name())){
            if(checkIfListinoBaseExists()){
                throw new ListinoBaseAlreadyExistingException();
            };
        }
        listino.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Listino createdListino = listinoRepository.save(listino);
        LOGGER.info("Created 'listino' '{}'", createdListino);
        return createdListino;
    }

    public Listino update(Listino listino){
        LOGGER.info("Updating 'listino'");
        checkListinoTipologia(listino.getTipologia());
        if(listino.getTipologia().equals(TipologiaListino.BASE.name())){
            if(checkIfListinoBaseExists()){
                throw new ListinoBaseAlreadyExistingException();
            };
        }
        Listino updatedListino = listinoRepository.save(listino);
        LOGGER.info("Updated 'listino' '{}'", updatedListino);
        return updatedListino;
    }

    public void delete(Long listinoId){
        LOGGER.info("Deleting 'listino' '{}'", listinoId);
        listinoRepository.deleteById(listinoId);
        LOGGER.info("Deleted 'listino' '{}'", listinoId);
    }

    private boolean checkIfListinoBaseExists(){
        Optional<Listino> listino = listinoRepository.findFirstByTipologia(TIPOLOGIA_BASE);
        return listino.isPresent();
    }

    private void checkListinoTipologia(String tipologia){
        try {
            TipologiaListino.valueOf(tipologia);
        } catch (Exception e){
            throw new ListinoTipologiaNotAllowedException();
        }
    }
}
