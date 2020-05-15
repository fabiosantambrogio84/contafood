package com.contafood.service;

import com.contafood.model.NotaAccreditoRiga;
import com.contafood.repository.NotaAccreditoRigaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class NotaAccreditoRigaService {

    private static Logger LOGGER = LoggerFactory.getLogger(NotaAccreditoRigaService.class);

    private final NotaAccreditoRigaRepository notaAccreditoRigaRepository;

    @Autowired
    public NotaAccreditoRigaService(final NotaAccreditoRigaRepository notaAccreditoRigaRepository){
        this.notaAccreditoRigaRepository = notaAccreditoRigaRepository;
    }

    public Set<NotaAccreditoRiga> findAll(){
        LOGGER.info("Retrieving the list of 'nota accredito info'");
        Set<NotaAccreditoRiga> notaAccreditoRiga = notaAccreditoRigaRepository.findAll();
        LOGGER.info("Retrieved {} 'nota accredito info'", notaAccreditoRiga.size());
        return notaAccreditoRiga;
    }

    public NotaAccreditoRiga create(NotaAccreditoRiga notaAccreditoRiga){
        LOGGER.info("Creating 'nota accredito info'");
        notaAccreditoRiga.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        NotaAccreditoRiga createdNotaAccreditoRiga = notaAccreditoRigaRepository.save(notaAccreditoRiga);
        LOGGER.info("Created 'nota accredito info' '{}'", createdNotaAccreditoRiga);
        return createdNotaAccreditoRiga;
    }

    public void deleteByNotaAccreditoId(Long notaAccreditoId){
        LOGGER.info("Deleting 'nota accredito info' by 'nota accredito' '{}'", notaAccreditoId);
        notaAccreditoRigaRepository.deleteByNotaAccreditoId(notaAccreditoId);
        LOGGER.info("Deleted 'nota accredito info' by 'nota accredito' '{}'", notaAccreditoId);
    }

}
