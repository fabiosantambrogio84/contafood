package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.StatoDdtAcquisto;
import com.contafood.repository.StatoDdtAcquistoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StatoDdtAcquistoService {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoDdtAcquistoService.class);

    private final StatoDdtAcquistoRepository statoDdtAcquistoRepository;

    private static final String DA_PAGARE = "DA_PAGARE";
    private static final String PARZIALMENTE_PAGATO = "PARZIALMENTE_PAGATO";
    private static final String PAGATO = "PAGATO";

    @Autowired
    public StatoDdtAcquistoService(final StatoDdtAcquistoRepository statoDdtAcquistoRepository){
        this.statoDdtAcquistoRepository = statoDdtAcquistoRepository;
    }

    public Set<StatoDdtAcquisto> getAll(){
        LOGGER.info("Retrieving the list of 'statiDdtAcquisto'");
        Set<StatoDdtAcquisto> statiDdtAcquisto = statoDdtAcquistoRepository.findAllByOrderByOrdine();
        LOGGER.info("Retrieved {} 'statiDdtAcquisto'", statiDdtAcquisto.size());
        return statiDdtAcquisto;
    }

    public StatoDdtAcquisto getOne(Long statoDdtAcquistoId){
        LOGGER.info("Retrieving 'statoDdtAcquisto' '{}'", statoDdtAcquistoId);
        StatoDdtAcquisto statoDdtAcquisto = statoDdtAcquistoRepository.findById(statoDdtAcquistoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoDdtAcquisto' '{}'", statoDdtAcquisto);
        return statoDdtAcquisto;
    }

    public StatoDdtAcquisto getDaPagare(){
        LOGGER.info("Retrieving 'statoDdtAcquisto' 'DA_PAGARE'");
        StatoDdtAcquisto statoDdtAcquisto = statoDdtAcquistoRepository.findByCodice(DA_PAGARE).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoDdtAcquisto' '{}'", statoDdtAcquisto);
        return statoDdtAcquisto;
    }

    public StatoDdtAcquisto getParzialmentePagato(){
        LOGGER.info("Retrieving 'statoDdtAcquisto' 'PARZIALMENTE_PAGATO'");
        StatoDdtAcquisto statoDdtAcquisto = statoDdtAcquistoRepository.findByCodice(PARZIALMENTE_PAGATO).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoDdtAcquisto' '{}'", statoDdtAcquisto);
        return statoDdtAcquisto;
    }

    public StatoDdtAcquisto getPagato(){
        LOGGER.info("Retrieving 'statoDdtAcquisto' 'PAGATO'");
        StatoDdtAcquisto statoDdtAcquisto = statoDdtAcquistoRepository.findByCodice(PAGATO).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoDdtAcquisto' '{}'", statoDdtAcquisto);
        return statoDdtAcquisto;
    }

    public StatoDdtAcquisto create(StatoDdtAcquisto statoDdtAcquisto){
        LOGGER.info("Creating 'statoDdtAcquisto'");
        StatoDdtAcquisto createdStatoDdtAcquisto = statoDdtAcquistoRepository.save(statoDdtAcquisto);
        LOGGER.info("Created 'statoDdtAcquisto' '{}'", createdStatoDdtAcquisto);
        return createdStatoDdtAcquisto;
    }

    public StatoDdtAcquisto update(StatoDdtAcquisto statoDdtAcquisto){
        LOGGER.info("Updating 'statoDdtAcquisto'");
        StatoDdtAcquisto updatedStatoDdtAcquisto = statoDdtAcquistoRepository.save(statoDdtAcquisto);
        LOGGER.info("Updated 'statoDdtAcquisto' '{}'", updatedStatoDdtAcquisto);
        return updatedStatoDdtAcquisto;
    }

    public void delete(Long statoDdtAcquistoId){
        LOGGER.info("Deleting 'statoDdtAcquisto' '{}'", statoDdtAcquistoId);
        statoDdtAcquistoRepository.deleteById(statoDdtAcquistoId);
        LOGGER.info("Deleted 'statoDdtAcquisto' '{}'", statoDdtAcquistoId);
    }
}
