package com.contafood.service;

import com.contafood.model.NotaResoTotale;
import com.contafood.repository.NotaResoTotaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class NotaResoTotaleService {

    private static Logger LOGGER = LoggerFactory.getLogger(NotaResoTotaleService.class);

    private final NotaResoTotaleRepository notaResoTotaleRepository;

    @Autowired
    public NotaResoTotaleService(final NotaResoTotaleRepository notaResoTotaleRepository){
        this.notaResoTotaleRepository = notaResoTotaleRepository;
    }

    public Set<NotaResoTotale> findAll(){
        LOGGER.info("Retrieving the list of 'nota reso totali'");
        Set<NotaResoTotale> notaResoTotali = notaResoTotaleRepository.findAll();
        LOGGER.info("Retrieved {} 'nota reso totali'", notaResoTotali.size());
        return notaResoTotali;
    }

    public NotaResoTotale create(NotaResoTotale notaResoTotale){
        LOGGER.info("Creating 'nota reso totale'");
        notaResoTotale.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        NotaResoTotale createdNotaResoTotale = notaResoTotaleRepository.save(notaResoTotale);
        LOGGER.info("Created 'nota reso totale' '{}'", createdNotaResoTotale);
        return createdNotaResoTotale;
    }

    public void deleteByNotaResoId(Long notaResoId){
        LOGGER.info("Deleting 'nota reso totali' by 'nota reso' '{}'", notaResoId);
        notaResoTotaleRepository.deleteByNotaResoId(notaResoId);
        LOGGER.info("Deleted 'nota reso totali' by 'nota reso' '{}'", notaResoId);
    }

}
