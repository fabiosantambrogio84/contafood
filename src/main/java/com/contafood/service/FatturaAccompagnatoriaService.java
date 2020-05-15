package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.AliquotaIva;
import com.contafood.model.Articolo;
import com.contafood.model.FatturaAccompagnatoria;
import com.contafood.model.FatturaAccompagnatoriaArticolo;
import com.contafood.model.views.VFattura;
import com.contafood.repository.FatturaAccompagnatoriaRepository;
import com.contafood.repository.VFatturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
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
    private final VFatturaRepository vFatturaRepository;

    @Autowired
    public FatturaAccompagnatoriaService(final FatturaAccompagnatoriaRepository fatturaAccompagnatoriaRepository, final FatturaAccompagnatoriaArticoloService fatturaAccompagnatoriaArticoloService, final FatturaAccompagnatoriaTotaleService fatturaAccompagnatoriaTotaleService, final StatoFatturaService statoFatturaService, final TipoFatturaService tipoFatturaService, final VFatturaRepository vFatturaRepository){
        this.fatturaAccompagnatoriaRepository = fatturaAccompagnatoriaRepository;
        this.fatturaAccompagnatoriaArticoloService = fatturaAccompagnatoriaArticoloService;
        this.fatturaAccompagnatoriaTotaleService = fatturaAccompagnatoriaTotaleService;
        this.statoFatturaService = statoFatturaService;
        this.tipoFatturaService = tipoFatturaService;
        this.vFatturaRepository = vFatturaRepository;
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
        List<VFattura> fattureAccompagnatorie = vFatturaRepository.findByAnnoOrderByProgressivoDesc(anno);
        if(fattureAccompagnatorie != null && !fattureAccompagnatorie.isEmpty()){
            Optional<VFattura> lastFatturaAccompagnatoria = fattureAccompagnatorie.stream().findFirst();
            if(lastFatturaAccompagnatoria.isPresent()){
                progressivo = lastFatturaAccompagnatoria.get().getProgressivo() + 1;
            }
        }
        HashMap<String, Integer> result = new HashMap<>();
        result.put("anno", anno);
        result.put("progressivo", progressivo);

        return result;
    }

    public List<FatturaAccompagnatoria> getByDataGreaterThanEqual(Date data){
        LOGGER.info("Retrieving 'fattureAccompagnatorie' with 'data' greater or equals to '{}'", data);
        List<FatturaAccompagnatoria> fattureAccompagnatorie = fatturaAccompagnatoriaRepository.findByDataGreaterThanEqualOrderByProgressivoDesc(data);
        LOGGER.info("Retrieved {} 'fattureAccompagnatorie' with 'data' greater or equals to '{}'", fattureAccompagnatorie.size(), data);
        return fattureAccompagnatorie;
    }

    @Transactional
    public FatturaAccompagnatoria create(FatturaAccompagnatoria fatturaAccompagnatoria){
        LOGGER.info("Creating 'fattura accompagnatoria'");

        checkExistsByAnnoAndProgressivoAndIdNot(fatturaAccompagnatoria.getAnno(),fatturaAccompagnatoria.getProgressivo(), Long.valueOf(-1));

        fatturaAccompagnatoria.setStatoFattura(statoFatturaService.getDaPagare());
        fatturaAccompagnatoria.setTipoFattura(tipoFatturaService.getAccompagnatoria());
        fatturaAccompagnatoria.setSpeditoAde(false);
        fatturaAccompagnatoria.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        LOGGER.info(fatturaAccompagnatoria.getScannerLog());

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

        computeTotali(createdFatturaAccompagnatoria, createdFatturaAccompagnatoria.getFatturaAccompagnatoriaArticoli());

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
            throw new ResourceAlreadyExistingException("fattura", anno, progressivo);
        }
    }

    private void computeTotali(FatturaAccompagnatoria fatturaAccompagnatoria, Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli){
        Map<AliquotaIva, Set<FatturaAccompagnatoriaArticolo>> ivaFatturaAccompagnatoriaArticoliMap = new HashMap<>();
        fatturaAccompagnatoriaArticoli.stream().forEach(faa -> {
            Articolo articolo = fatturaAccompagnatoriaArticoloService.getArticolo(faa);
            AliquotaIva iva = articolo.getAliquotaIva();
            Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoliByIva;
            if(ivaFatturaAccompagnatoriaArticoliMap.containsKey(iva)){
                fatturaAccompagnatoriaArticoliByIva = ivaFatturaAccompagnatoriaArticoliMap.get(iva);
            } else {
                fatturaAccompagnatoriaArticoliByIva = new HashSet<>();
            }
            fatturaAccompagnatoriaArticoliByIva.add(faa);
            ivaFatturaAccompagnatoriaArticoliMap.put(iva, fatturaAccompagnatoriaArticoliByIva);
        });
        Float totaleQuantita = 0f;
        BigDecimal totale = new BigDecimal(0);
        for (Map.Entry<AliquotaIva, Set<FatturaAccompagnatoriaArticolo>> entry : ivaFatturaAccompagnatoriaArticoliMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);
            Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoliByIva = entry.getValue();
            for(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo: fatturaAccompagnatoriaArticoliByIva){
                totaleByIva = totaleByIva.add(fatturaAccompagnatoriaArticolo.getImponibile());
                totaleQuantita = totaleQuantita + fatturaAccompagnatoriaArticolo.getQuantita();
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }
        fatturaAccompagnatoria.setTotale(totale.setScale(2, RoundingMode.HALF_DOWN));
        fatturaAccompagnatoria.setTotaleAcconto(new BigDecimal(0));
        fatturaAccompagnatoria.setTotaleQuantita(new BigDecimal(totaleQuantita).setScale(2, RoundingMode.HALF_DOWN));
    }

}
