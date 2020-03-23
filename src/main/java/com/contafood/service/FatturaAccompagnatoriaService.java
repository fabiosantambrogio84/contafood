package com.contafood.service;

import com.contafood.exception.FatturaAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.FatturaAccompagnatoria;
import com.contafood.repository.FatturaAccompagnatoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class FatturaAccompagnatoriaService {

    private static Logger LOGGER = LoggerFactory.getLogger(FatturaAccompagnatoriaService.class);

    private final FatturaAccompagnatoriaRepository fatturaAccompagnatoriaRepository;
    private final FatturaAccompagnatoriaArticoloService fatturaAccompagnatoriaArticoloService;
    private final FatturaAccompagnatoriaTotaleService fatturaAccompagnatoriaTotaleService;
    private final StatoFatturaService statoFatturaService;
    private final TipoFatturaService tipoFatturaService;

    @Autowired
    public FatturaAccompagnatoriaService(final FatturaAccompagnatoriaRepository fatturaAccompagnatoriaRepository, final FatturaAccompagnatoriaArticoloService fatturaAccompagnatoriaArticoloService, final FatturaAccompagnatoriaTotaleService fatturaAccompagnatoriaTotaleService, final StatoFatturaService statoFatturaService, final TipoFatturaService tipoFatturaService){
        this.fatturaAccompagnatoriaRepository = fatturaAccompagnatoriaRepository;
        this.fatturaAccompagnatoriaArticoloService = fatturaAccompagnatoriaArticoloService;
        this.fatturaAccompagnatoriaTotaleService = fatturaAccompagnatoriaTotaleService;
        this.statoFatturaService = statoFatturaService;
        this.tipoFatturaService = tipoFatturaService;
    }

    public Set<FatturaAccompagnatoria> getAll(){
        LOGGER.info("Retrieving the list of 'fatture accompagnatorie'");
        Set<FatturaAccompagnatoria> fattureAccompagnatorie = fatturaAccompagnatoriaRepository.findAllByOrderByAnnoDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'fatture accompagnatorie'", fattureAccompagnatorie.size());
        return fattureAccompagnatorie;
    }

    public FatturaAccompagnatoria getOne(Long fatturaAccompagnatoriaId){
        LOGGER.info("Retrieving 'fattura accompagnatoria' '{}'", fatturaAccompagnatoriaId);
        FatturaAccompagnatoria fatturaAccompagnatoria = fatturaAccompagnatoriaRepository.findById(fatturaAccompagnatoriaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'fattura accompagnatoria' '{}'", fatturaAccompagnatoria);
        return fatturaAccompagnatoria;
    }

    public Map<String, Integer> getAnnoAndProgressivo(){
        Integer anno = ZonedDateTime.now().getYear();
        Integer progressivo = 1;
        List<FatturaAccompagnatoria> fattureAccompagnatorie = fatturaAccompagnatoriaRepository.findByAnnoOrderByProgressivoDesc(anno);
        if(fattureAccompagnatorie != null && !fattureAccompagnatorie.isEmpty()){
            Optional<FatturaAccompagnatoria> lastFatturaAccompagnatoria = fattureAccompagnatorie.stream().findFirst();
            if(lastFatturaAccompagnatoria.isPresent()){
                progressivo = lastFatturaAccompagnatoria.get().getProgressivo() + 1;
            }
        }
        HashMap<String, Integer> result = new HashMap<>();
        result.put("anno", anno);
        result.put("progressivo", progressivo);

        return result;
    }

    @Transactional
    public FatturaAccompagnatoria create(FatturaAccompagnatoria fatturaAccompagnatoria){
        LOGGER.info("Creating 'fattura accompagnatoria'");

        checkExistsByAnnoAndProgressivoAndIdNot(fatturaAccompagnatoria.getAnno(),fatturaAccompagnatoria.getProgressivo(), Long.valueOf(-1));

        fatturaAccompagnatoria.setStatoFattura(statoFatturaService.getDaPagare());
        fatturaAccompagnatoria.setSpeditoAde(false);
        fatturaAccompagnatoria.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        FatturaAccompagnatoria createdFatturaAccompagnatoria = fatturaAccompagnatoriaRepository.save(fatturaAccompagnatoria);

        createdFatturaAccompagnatoria.getFatturaAccompagnatoriaArticoli().stream().forEach(faa -> {
            faa.getId().setFatturaAccompagnatoriaId(createdFatturaAccompagnatoria.getId());
            faa.getId().setUuid(UUID.randomUUID().toString());
            fatturaAccompagnatoriaArticoloService.create(faa);
        });

        createdFatturaAccompagnatoria.getFatturaAccompagnatoriaTotali().stream().forEach(fat -> {
            fat.getId().setFatturaAccompagnatoriaId(createdFatturaAccompagnatoria.getId());
            fat.getId().setUuid(UUID.randomUUID().toString());
            fatturaAccompagnatoriaTotaleService.create(fat);
        });

        fatturaAccompagnatoriaRepository.save(createdFatturaAccompagnatoria);
        LOGGER.info("Created 'fattura accompagnatoria' '{}'", createdFatturaAccompagnatoria);

        return createdFatturaAccompagnatoria;
    }

    @Transactional
    public void delete(Long fatturaAccompagnatoriaId){
        LOGGER.info("Deleting 'fattura accompagnatoria' '{}'", fatturaAccompagnatoriaId);

        fatturaAccompagnatoriaArticoloService.deleteByFatturaAccompagnatoriaId(fatturaAccompagnatoriaId);
        fatturaAccompagnatoriaTotaleService.deleteByFatturaAccompagnatoriaId(fatturaAccompagnatoriaId);
        fatturaAccompagnatoriaRepository.deleteById(fatturaAccompagnatoriaId);
        LOGGER.info("Deleted 'fattura accompagnatoria' '{}'", fatturaAccompagnatoriaId);
    }

    private void checkExistsByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFattura){
        Optional<FatturaAccompagnatoria> fatturaAccompagnatoria = fatturaAccompagnatoriaRepository.findByAnnoAndProgressivoAndIdNot(anno, progressivo, idFattura);
        if(fatturaAccompagnatoria.isPresent()){
            throw new FatturaAlreadyExistingException(anno, progressivo);
        }
    }

}
