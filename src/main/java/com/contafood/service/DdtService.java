package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Ddt;
import com.contafood.model.DdtArticolo;
import com.contafood.repository.DdtRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class DdtService {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtService.class);

    private final DdtRepository ddtRepository;
    private final DdtArticoloService ddtArticoloService;

    @Autowired
    public DdtService(final DdtRepository ddtRepository, final DdtArticoloService ddtArticoloService){
        this.ddtRepository = ddtRepository;
        this.ddtArticoloService = ddtArticoloService;
    }

    public Set<Ddt> getAll(){
        LOGGER.info("Retrieving the list of 'ddts'");
        Set<Ddt> ddts = ddtRepository.findAllByOrderByAnnoContabileDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'ddts'", ddts.size());
        return ddts;
    }

    public Ddt getOne(Long ddtId){
        LOGGER.info("Retrieving 'ddt' '{}'", ddtId);
        Ddt ddt = ddtRepository.findById(ddtId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'ddt' '{}'", ddt);
        return ddt;
    }

    @Transactional
    public Ddt create(Ddt ddt){
        LOGGER.info("Creating 'ddt'");
        ddt.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Integer annoContabile = ZonedDateTime.now().getYear();
        ddt.setAnnoContabile(annoContabile);
        ddt.setProgressivo(computeProgressivo(annoContabile));

        Ddt createdDdt = ddtRepository.save(ddt);

        createdDdt.getDdtArticoli().stream().forEach(da -> {
            da.getId().setDdtId(createdDdt.getId());
            ddtArticoloService.create(da);
        });

        ddtRepository.save(createdDdt);
        LOGGER.info("Created 'ddt' '{}'", createdDdt);
        return createdDdt;
    }

    @Transactional
    public Ddt update(Ddt ddt){
        LOGGER.info("Updating 'ddt'");
        Set<DdtArticolo> ddtArticoli = ddt.getDdtArticoli();
        ddt.setDdtArticoli(new HashSet<>());
        ddtArticoloService.deleteByDdtId(ddt.getId());

        Ddt ddtCurrent = ddtRepository.findById(ddt.getId()).orElseThrow(ResourceNotFoundException::new);
        ddt.setDataInserimento(ddtCurrent.getDataInserimento());
        ddt.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Ddt updatedDdt = ddtRepository.save(ddt);
        ddtArticoli.stream().forEach(da -> {
            da.getId().setDdtId(updatedDdt.getId());
            ddtArticoloService.create(da);
        });
        LOGGER.info("Updated 'ddt' '{}'", updatedDdt);
        return updatedDdt;
    }

    @Transactional
    public Ddt patch(Map<String,Object> patchDdt){
        LOGGER.info("Patching 'ddt'");

        Long id = Long.valueOf((Integer) patchDdt.get("id"));
        Ddt ddt = ddtRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        patchDdt.forEach((key, value) -> {
            if(key.equals("id")){
                ddt.setId(Long.valueOf((Integer)value));
            } else if(key.equals("dataConsegna")){

            }
        });
        Ddt patchedDdt = ddtRepository.save(ddt);

        LOGGER.info("Patched 'ddt' '{}'", patchedDdt);
        return patchedDdt;
    }

    @Transactional
    public void delete(Long ddtId){
        LOGGER.info("Deleting 'ddt' '{}'", ddtId);
        ddtArticoloService.deleteByDdtId(ddtId);
        ddtRepository.deleteById(ddtId);
        LOGGER.info("Deleted 'ddt' '{}'", ddtId);
    }

    private Integer computeProgressivo(Integer annoContabile){
        List<Ddt> ddts = ddtRepository.findByAnnoContabileOrderByProgressivoDesc(annoContabile);
        if(ddts != null && !ddts.isEmpty()){
            Optional<Ddt> lastDdt = ddts.stream().findFirst();
            if(lastDdt.isPresent()){
                return lastDdt.get().getProgressivo() + 1;
            }
        }
        return 1;
    }

}
