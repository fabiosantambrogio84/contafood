package com.contafood.service;

import com.contafood.model.NotaAccreditoTotale;
import com.contafood.repository.NotaAccreditoTotaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class NotaAccreditoTotaleService {

    private static Logger LOGGER = LoggerFactory.getLogger(NotaAccreditoTotaleService.class);

    private final NotaAccreditoTotaleRepository notaAccreditoTotaleRepository;

    @Autowired
    public NotaAccreditoTotaleService(final NotaAccreditoTotaleRepository notaAccreditoTotaleRepository){
        this.notaAccreditoTotaleRepository = notaAccreditoTotaleRepository;
    }

    public Set<NotaAccreditoTotale> findAll(){
        LOGGER.info("Retrieving the list of 'nota accredito totali'");
        Set<NotaAccreditoTotale> notaAccreditoTotali = notaAccreditoTotaleRepository.findAll();
        LOGGER.info("Retrieved {} 'nota accredito totali'", notaAccreditoTotali.size());
        return notaAccreditoTotali;
    }

    public NotaAccreditoTotale create(NotaAccreditoTotale notaAccreditoTotale){
        LOGGER.info("Creating 'nota accredito totale'");
        notaAccreditoTotale.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        NotaAccreditoTotale createdNotaAccreditoTotale = notaAccreditoTotaleRepository.save(notaAccreditoTotale);
        LOGGER.info("Created 'nota accredito totale' '{}'", createdNotaAccreditoTotale);
        return createdNotaAccreditoTotale;
    }

    public void deleteByNotaAccreditoId(Long notaAccreditoId){
        LOGGER.info("Deleting 'nota accredito totali' by 'nota accredito' '{}'", notaAccreditoId);
        notaAccreditoTotaleRepository.deleteByNotaAccreditoId(notaAccreditoId);
        LOGGER.info("Deleted 'nota accredito totali' by 'nota accredito' '{}'", notaAccreditoId);
    }

}
