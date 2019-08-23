package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Parametro;
import com.contafood.repository.ParametroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class ParametroService {

    private static Logger LOGGER = LoggerFactory.getLogger(ParametroService.class);

    private final ParametroRepository parametroRepository;

    @Autowired
    public ParametroService(final ParametroRepository parametroRepository){
        this.parametroRepository = parametroRepository;
    }

    public Set<Parametro> getAll(){
        LOGGER.info("Retrieving the list of 'parametri'");
        Set<Parametro> parametri = parametroRepository.findAll();
        LOGGER.info("Retrieved {} 'parametri'", parametri.size());
        return parametri;
    }

    public Parametro getOne(Long parametroId){
        LOGGER.info("Retrieving 'parametro' '{}'", parametroId);
        Parametro parametro = parametroRepository.findById(parametroId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'parametro' '{}'", parametro);
        return parametro;
    }

    public Parametro findByNome(String parametroNome){
        LOGGER.info("Retrieving 'parametro' by name '{}'", parametroNome);
        Parametro parametro = parametroRepository.findByNome(parametroNome).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'parametro' by name '{}'", parametro);
        return parametro;
    }

    public Parametro update(Parametro parametro){
        LOGGER.info("Updating 'parametro'");
        Parametro updatedParametro = parametroRepository.save(parametro);
        LOGGER.info("Updated 'parametro' '{}'", updatedParametro);
        return updatedParametro;
    }

}
