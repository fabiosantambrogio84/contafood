package com.contafood.service;

import com.contafood.exception.FatturaAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.FatturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FatturaService {

    private static Logger LOGGER = LoggerFactory.getLogger(FatturaService.class);

    private final FatturaRepository fatturaRepository;
    private final FatturaDdtService fatturaDdtService;
    private final StatoFatturaService statoFatturaService;
    private final StatoDdtService statoDdtService;
    private final DdtService ddtService;

    @Autowired
    public FatturaService(final FatturaRepository fatturaRepository, final FatturaDdtService fatturaDdtService, final StatoFatturaService statoFatturaService, final StatoDdtService statoDdtService, final DdtService ddtService){
        this.fatturaRepository = fatturaRepository;
        this.fatturaDdtService = fatturaDdtService;
        this.statoFatturaService = statoFatturaService;
        this.statoDdtService = statoDdtService;
        this.ddtService = ddtService;
    }

    public Set<Fattura> getAll(){
        LOGGER.info("Retrieving the list of 'fatture'");
        Set<Fattura> fatture = fatturaRepository.findAllByOrderByAnnoDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'fatture'", fatture.size());
        return fatture;
    }

    public Fattura getOne(Long fatturaId){
        LOGGER.info("Retrieving 'fattura' '{}'", fatturaId);
        Fattura fattura = fatturaRepository.findById(fatturaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'fattura' '{}'", fattura);
        return fattura;
    }

    public Map<String, Integer> getAnnoAndProgressivo(){
        Integer anno = ZonedDateTime.now().getYear();
        Integer progressivo = 1;
        List<Fattura> fatture = fatturaRepository.findByAnnoOrderByProgressivoDesc(anno);
        if(fatture != null && !fatture.isEmpty()){
            Optional<Fattura> lastFattura = fatture.stream().findFirst();
            if(lastFattura.isPresent()){
                progressivo = lastFattura.get().getProgressivo() + 1;
            }
        }
        HashMap<String, Integer> result = new HashMap<>();
        result.put("anno", anno);
        result.put("progressivo", progressivo);

        return result;
    }

    public Set<Pagamento> getFatturaDdtPagamenti(Long idFattura){
        Set<Pagamento> pagamenti = new HashSet<>();
        Fattura fattura = fatturaRepository.findById(idFattura).orElseThrow(ResourceNotFoundException::new);
        Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();
        fatturaDdts.forEach(fd -> {
            pagamenti.addAll(fd.getDdt().getDdtPagamenti());
        });
        return pagamenti;
    }

    public Set<DdtArticolo> getFatturaDdtArticoli(Long idFattura){
        Set<DdtArticolo> ddtArticoli = new HashSet<>();
        Fattura fattura = fatturaRepository.findById(idFattura).orElseThrow(ResourceNotFoundException::new);
        Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();
        fatturaDdts.forEach(fd -> {
            ddtArticoli.addAll(fd.getDdt().getDdtArticoli());
        });
        return ddtArticoli;
    }

    @Transactional
    public Fattura create(Fattura fattura){
        LOGGER.info("Creating 'fattura'");

        checkExistsByAnnoAndProgressivoAndIdNot(fattura.getAnno(),fattura.getProgressivo(), Long.valueOf(-1));

        fattura.setStatoFattura(statoFatturaService.getDaPagare());
        fattura.setSpeditoAde(false);
        fattura.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Fattura createdFattura = fatturaRepository.save(fattura);

        createdFattura.getFatturaDdts().stream().forEach(fd -> {
            fd.getId().setFatturaId(createdFattura.getId());
            fd.getId().setUuid(UUID.randomUUID().toString());
            fatturaDdtService.create(fd);
        });

        computeStato(createdFattura);

        fatturaRepository.save(createdFattura);
        LOGGER.info("Created 'fattura' '{}'", createdFattura);
        return createdFattura;
    }

    @Transactional
    public Fattura update(Fattura fattura){
        LOGGER.info("Updating 'fattura'");
        checkExistsByAnnoAndProgressivoAndIdNot(fattura.getAnno(), fattura.getProgressivo(), fattura.getId());

        Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();
        fattura.setFatturaDdts(new HashSet<>());
        fatturaDdtService.deleteByFatturaId(fattura.getId());

        Fattura fatturaCurrent = fatturaRepository.findById(fattura.getId()).orElseThrow(ResourceNotFoundException::new);
        fattura.setStatoFattura(fatturaCurrent.getStatoFattura());
        fattura.setDataInserimento(fatturaCurrent.getDataInserimento());
        fattura.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Fattura updatedFattura = fatturaRepository.save(fattura);
        fatturaDdts.stream().forEach(fd -> {
            fd.getId().setFatturaId(updatedFattura.getId());
            fd.getId().setUuid(UUID.randomUUID().toString());
            fatturaDdtService.create(fd);
        });

        fatturaRepository.save(updatedFattura);
        LOGGER.info("Updated 'fattura' '{}'", updatedFattura);
        return updatedFattura;
    }

    @Transactional
    public void delete(Long fatturaId){
        LOGGER.info("Deleting 'fattura' '{}'", fatturaId);
        fatturaDdtService.deleteByFatturaId(fatturaId);
        fatturaRepository.deleteById(fatturaId);
        LOGGER.info("Deleted 'fattura' '{}'", fatturaId);
    }

    private void checkExistsByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFattura){
        Optional<Fattura> fattura = fatturaRepository.findByAnnoAndProgressivoAndIdNot(anno, progressivo, idFattura);
        if(fattura.isPresent()){
            throw new FatturaAlreadyExistingException(anno, progressivo);
        }
    }

    private void computeStato(Fattura fattura){
        StatoDdt ddtStatoPagato = statoDdtService.getPagato();
        StatoDdt ddtStatoDaPagare = statoDdtService.getDaPagare();

        Set<Ddt> ddts = new HashSet<>();
        fattura.getFatturaDdts().forEach(fd -> {
            Long idDdt = fd.getId().getDdtId();
            ddts.add(ddtService.getOne(idDdt));
        });
        LOGGER.info("Fattura ddts size {}", ddts.size());
        int ddtsSize = ddts.size();
        Set<Ddt> ddtsPagati = ddts.stream().filter(d -> d.getStatoDdt().equals(ddtStatoPagato)).collect(Collectors.toSet());
        if(ddtsSize == ddtsPagati.size()){
            fattura.setStatoFattura(statoFatturaService.getPagata());
            return;
        }
        Set<Ddt> ddtsDaPagare = ddts.stream().filter(d -> d.getStatoDdt().equals(ddtStatoDaPagare)).collect(Collectors.toSet());
        if(ddtsSize == ddtsDaPagare.size()){
            fattura.setStatoFattura(statoFatturaService.getDaPagare());
            return;
        }
        fattura.setStatoFattura(statoFatturaService.getParzialmentePagata());
    }
}
