package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.StatoDdt;
import com.contafood.repository.StatoDdtRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StatoDdtService {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoDdtService.class);

    private final StatoDdtRepository statoDdtRepository;

    private static final String DA_PAGARE = "DA_PAGARE";
    private static final String PARZIALMENTE_PAGATO = "PARZIALMENTE_PAGATO";
    private static final String PAGATO = "PAGATO";

    @Autowired
    public StatoDdtService(final StatoDdtRepository statoDdtRepository){
        this.statoDdtRepository = statoDdtRepository;
    }

    public Set<StatoDdt> getAll(){
        LOGGER.info("Retrieving the list of 'statiDdt'");
        Set<StatoDdt> statiDdt = statoDdtRepository.findAllByOrderByOrdine();
        LOGGER.info("Retrieved {} 'statiDdt'", statiDdt.size());
        return statiDdt;
    }

    public StatoDdt getOne(Long statoDdtId){
        LOGGER.info("Retrieving 'statoDdt' '{}'", statoDdtId);
        StatoDdt statoDdt = statoDdtRepository.findById(statoDdtId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoDdt' '{}'", statoDdt);
        return statoDdt;
    }

    public StatoDdt getDaPagare(){
        LOGGER.info("Retrieving 'statoDdt' 'DA_PAGARE'");
        StatoDdt statoDdt = statoDdtRepository.findByCodice(DA_PAGARE).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoDdt' '{}'", statoDdt);
        return statoDdt;
    }

    public StatoDdt getParzialmentePagato(){
        LOGGER.info("Retrieving 'statoDdt' 'PARZIALMENTE_PAGATO'");
        StatoDdt statoDdt = statoDdtRepository.findByCodice(PARZIALMENTE_PAGATO).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoDdt' '{}'", statoDdt);
        return statoDdt;
    }

    public StatoDdt getPagato(){
        LOGGER.info("Retrieving 'statoDdt' 'PAGATO'");
        StatoDdt statoDdt = statoDdtRepository.findByCodice(PAGATO).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoDdt' '{}'", statoDdt);
        return statoDdt;
    }

    public StatoDdt create(StatoDdt statoDdt){
        LOGGER.info("Creating 'statoDdt'");
        StatoDdt createdStatoDdt = statoDdtRepository.save(statoDdt);
        LOGGER.info("Created 'statoDdt' '{}'", createdStatoDdt);
        return createdStatoDdt;
    }

    public StatoDdt update(StatoDdt statoDdt){
        LOGGER.info("Updating 'statoDdt'");
        StatoDdt updatedStatoDdt = statoDdtRepository.save(statoDdt);
        LOGGER.info("Updated 'statoDdt' '{}'", updatedStatoDdt);
        return updatedStatoDdt;
    }

    public void delete(Long statoDdtId){
        LOGGER.info("Deleting 'statoDdt' '{}'", statoDdtId);
        statoDdtRepository.deleteById(statoDdtId);
        LOGGER.info("Deleted 'statoDdt' '{}'", statoDdtId);
    }
}
