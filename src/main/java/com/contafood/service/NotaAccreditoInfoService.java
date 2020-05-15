package com.contafood.service;

import com.contafood.model.NotaAccreditoInfo;
import com.contafood.repository.NotaAccreditoInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class NotaAccreditoInfoService {

    private static Logger LOGGER = LoggerFactory.getLogger(NotaAccreditoInfoService.class);

    private final NotaAccreditoInfoRepository notaAccreditoInfoRepository;

    @Autowired
    public NotaAccreditoInfoService(final NotaAccreditoInfoRepository notaAccreditoInfoRepository){
        this.notaAccreditoInfoRepository = notaAccreditoInfoRepository;
    }

    public Set<NotaAccreditoInfo> findAll(){
        LOGGER.info("Retrieving the list of 'nota accredito info'");
        Set<NotaAccreditoInfo> notaAccreditoInfo = notaAccreditoInfoRepository.findAll();
        LOGGER.info("Retrieved {} 'nota accredito info'", notaAccreditoInfo.size());
        return notaAccreditoInfo;
    }

    public NotaAccreditoInfo create(NotaAccreditoInfo notaAccreditoInfo){
        LOGGER.info("Creating 'nota accredito info'");
        notaAccreditoInfo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        NotaAccreditoInfo createdNotaAccreditoInfo = notaAccreditoInfoRepository.save(notaAccreditoInfo);
        LOGGER.info("Created 'nota accredito info' '{}'", createdNotaAccreditoInfo);
        return createdNotaAccreditoInfo;
    }

    public void deleteByNotaAccreditoId(Long notaAccreditoId){
        LOGGER.info("Deleting 'nota accredito info' by 'nota accredito' '{}'", notaAccreditoId);
        notaAccreditoInfoRepository.deleteByNotaAccreditoId(notaAccreditoId);
        LOGGER.info("Deleted 'nota accredito info' by 'nota accredito' '{}'", notaAccreditoId);
    }

}
