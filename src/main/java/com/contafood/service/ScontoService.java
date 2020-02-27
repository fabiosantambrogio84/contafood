package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Sconto;
import com.contafood.repository.ScontoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ScontoService {

    private static Logger LOGGER = LoggerFactory.getLogger(ScontoService.class);

    private final ScontoRepository scontoRepository;

    @Autowired
    public ScontoService(final ScontoRepository scontoRepository){
        this.scontoRepository = scontoRepository;
    }

    public List<Sconto> getAll(){
        LOGGER.info("Retrieving the list of 'sconti'");
        List<Sconto> sconti = scontoRepository.findAll();
        LOGGER.info("Retrieved {} 'sconti'", sconti.size());
        return sconti;
    }

    public List<Sconto> getByTipologiaClienteIdAndDateDalAndDateAl(String tipologia, Long idCliente, Date date){
        LOGGER.info("Retrieving the list of 'sconti' of tipologia '{}' for cliente '{}', valid from '{}' to '{}'", tipologia, idCliente, date);
        List<Sconto> sconti = scontoRepository.findByTipologiaAndClienteIdAndDataDalGreaterThanEqualAndDataAlLessThanEqual(tipologia, idCliente, date, date);
        LOGGER.info("Retrieved {} 'sconti'", sconti.size());
        return sconti;
    }

    public Sconto getOne(Long scontoId){
        LOGGER.info("Retrieving 'sconto' '{}'", scontoId);
        Sconto sconto = scontoRepository.findById(scontoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'sconto' '{}'", sconto);
        return sconto;
    }

    public List<Sconto> create(List<Sconto> sconti){
        LOGGER.info("Creating 'sconti'");
        sconti.forEach(s -> {
            s.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            Sconto createdSconto = scontoRepository.save(s);
            LOGGER.info("Created 'sconto' '{}'", createdSconto);
        });

        return sconti;
    }

    public Sconto update(Sconto sconto){
        LOGGER.info("Updating 'sconto'");
        Sconto scontoCurrent = scontoRepository.findById(sconto.getId()).orElseThrow(ResourceNotFoundException::new);
        sconto.setDataInserimento(scontoCurrent.getDataInserimento());
        Sconto updatedSconto = scontoRepository.save(sconto);
        LOGGER.info("Updated 'sconto' '{}'", updatedSconto);
        return updatedSconto;
    }

    public void delete(Long scontoId){
        LOGGER.info("Deleting 'sconto' '{}'", scontoId);
        scontoRepository.deleteById(scontoId);
        LOGGER.info("Deleted 'sconto' '{}'", scontoId);
    }

    public List<Sconto> getAllByTipologia(String tipologia){
        LOGGER.info("Retrieving the list of 'sconti' filtered by tipologia '{}'", tipologia);
        List<Sconto> sconti = scontoRepository.findByTipologia(tipologia);
        LOGGER.info("Retrieved {} 'sconti'", sconti.size());
        return sconti;
    }
}
