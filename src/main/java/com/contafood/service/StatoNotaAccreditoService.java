package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.StatoNotaAccredito;
import com.contafood.repository.StatoNotaAccreditoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StatoNotaAccreditoService {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoNotaAccreditoService.class);

    private final StatoNotaAccreditoRepository statoNotaAccreditoRepository;

    private static final String DA_PAGARE = "DA_PAGARE";
    private static final String PARZIALMENTE_PAGATA = "PARZIALMENTE_PAGATA";
    private static final String PAGATA = "PAGATA";

    @Autowired
    public StatoNotaAccreditoService(final StatoNotaAccreditoRepository statoNotaAccreditoRepository){
        this.statoNotaAccreditoRepository = statoNotaAccreditoRepository;
    }

    public Set<StatoNotaAccredito> getAll(){
        LOGGER.info("Retrieving the list of 'statiNotaAccredito'");
        Set<StatoNotaAccredito> statoNotaAccredito = statoNotaAccreditoRepository.findAllByOrderByOrdine();
        LOGGER.info("Retrieved {} 'statiNotaAccredito'", statoNotaAccredito.size());
        return statoNotaAccredito;
    }

    public StatoNotaAccredito getOne(Long statoNotaAccreditoId){
        LOGGER.info("Retrieving 'statoNotaAccredito' '{}'", statoNotaAccreditoId);
        StatoNotaAccredito statoNotaAccredito = statoNotaAccreditoRepository.findById(statoNotaAccreditoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoNotaAccredito' '{}'", statoNotaAccredito);
        return statoNotaAccredito;
    }

    public StatoNotaAccredito getDaPagare(){
        LOGGER.info("Retrieving 'statoNotaAccredito' 'DA_PAGARE'");
        StatoNotaAccredito statoNotaAccredito = statoNotaAccreditoRepository.findByCodice(DA_PAGARE).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoNotaAccredito' '{}'", statoNotaAccredito);
        return statoNotaAccredito;
    }

    public StatoNotaAccredito getParzialmentePagata(){
        LOGGER.info("Retrieving 'statoNotaAccredito' 'PARZIALMENTE_PAGATA'");
        StatoNotaAccredito statoNotaAccredito = statoNotaAccreditoRepository.findByCodice(PARZIALMENTE_PAGATA).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoNotaAccredito' '{}'", statoNotaAccredito);
        return statoNotaAccredito;
    }

    public StatoNotaAccredito getPagato(){
        LOGGER.info("Retrieving 'statoNotaAccredito' 'PAGATA'");
        StatoNotaAccredito statoNotaAccredito = statoNotaAccreditoRepository.findByCodice(PAGATA).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoNotaAccredito' '{}'", statoNotaAccredito);
        return statoNotaAccredito;
    }

    public StatoNotaAccredito create(StatoNotaAccredito statoNotaAccredito){
        LOGGER.info("Creating 'statoNotaAccredito'");
        StatoNotaAccredito createdStatoNotaAccredito = statoNotaAccreditoRepository.save(statoNotaAccredito);
        LOGGER.info("Created 'statoNotaAccredito' '{}'", createdStatoNotaAccredito);
        return createdStatoNotaAccredito;
    }

    public StatoNotaAccredito update(StatoNotaAccredito statoNotaAccredito){
        LOGGER.info("Updating 'statoNotaAccredito'");
        StatoNotaAccredito updatedStatoNotaAccredito = statoNotaAccreditoRepository.save(statoNotaAccredito);
        LOGGER.info("Updated 'statoNotaAccredito' '{}'", updatedStatoNotaAccredito);
        return updatedStatoNotaAccredito;
    }

    public void delete(Long statoNotaAccreditoId){
        LOGGER.info("Deleting 'statoNotaAccredito' '{}'", statoNotaAccreditoId);
        statoNotaAccreditoRepository.deleteById(statoNotaAccreditoId);
        LOGGER.info("Deleted 'statoNotaAccredito' '{}'", statoNotaAccreditoId);
    }
}
