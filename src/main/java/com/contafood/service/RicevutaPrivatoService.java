package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.AliquotaIva;
import com.contafood.model.Articolo;
import com.contafood.model.RicevutaPrivato;
import com.contafood.model.RicevutaPrivatoArticolo;
import com.contafood.repository.PagamentoRepository;
import com.contafood.repository.RicevutaPrivatoRepository;
import com.contafood.util.enumeration.Resource;
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
public class RicevutaPrivatoService {

    private static Logger LOGGER = LoggerFactory.getLogger(RicevutaPrivatoService.class);

    private final RicevutaPrivatoRepository ricevutaPrivatoRepository;
    private final RicevutaPrivatoArticoloService ricevutaPrivatoArticoloService;
    private final RicevutaPrivatoTotaleService ricevutaPrivatoTotaleService;
    private final StatoRicevutaPrivatoService statoRicevutaPrivatoService;
    private final GiacenzaArticoloService giacenzaArticoloService;
    private final PagamentoRepository pagamentoRepository;

    @Autowired
    public RicevutaPrivatoService(final RicevutaPrivatoRepository ricevutaPrivatoRepository,
                                  final RicevutaPrivatoArticoloService ricevutaPrivatoArticoloService,
                                  final RicevutaPrivatoTotaleService ricevutaPrivatoTotaleService,
                                  final StatoRicevutaPrivatoService statoRicevutaPrivatoService,
                                  final GiacenzaArticoloService giacenzaArticoloService,
                                  final PagamentoRepository pagamentoRepository){
        this.ricevutaPrivatoRepository = ricevutaPrivatoRepository;
        this.ricevutaPrivatoArticoloService = ricevutaPrivatoArticoloService;
        this.ricevutaPrivatoTotaleService = ricevutaPrivatoTotaleService;
        this.statoRicevutaPrivatoService = statoRicevutaPrivatoService;
        this.giacenzaArticoloService = giacenzaArticoloService;
        this.pagamentoRepository = pagamentoRepository;
    }

    public Set<RicevutaPrivato> getAll(){
        LOGGER.info("Retrieving the list of 'ricevute privato'");
        Set<RicevutaPrivato> ricevutePrivato = ricevutaPrivatoRepository.findAllByOrderByAnnoDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'ricevute privato'", ricevutePrivato.size());
        return ricevutePrivato;
    }

    public RicevutaPrivato getOne(Long ricevutaPrivatoId){
        LOGGER.info("Retrieving 'fattura accompagnatoria' '{}'", ricevutaPrivatoId);
        RicevutaPrivato ricevutaPrivato = ricevutaPrivatoRepository.findById(ricevutaPrivatoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'fattura accompagnatoria' '{}'", ricevutaPrivato);
        return ricevutaPrivato;
    }

    public Map<String, Integer> getAnnoAndProgressivo(){
        Integer anno = ZonedDateTime.now().getYear();
        Integer progressivo = getProgressivo(anno);
        HashMap<String, Integer> result = new HashMap<>();
        result.put("anno", anno);
        result.put("progressivo", progressivo);

        return result;
    }

    private Integer getProgressivo(Integer anno) {
        Integer progressivo = 1;
        List<RicevutaPrivato> ricevutePrivato = ricevutaPrivatoRepository.findByAnnoOrderByProgressivoDesc(anno);
        if(ricevutePrivato != null && !ricevutePrivato.isEmpty()){
            Optional<RicevutaPrivato> lastRicevutaPrivato = ricevutePrivato.stream().findFirst();
            if(lastRicevutaPrivato.isPresent()){
                progressivo = lastRicevutaPrivato.get().getProgressivo() + 1;
            }
        }
        return progressivo;
    }

    public List<RicevutaPrivato> getByDataGreaterThanEqual(Date data){
        LOGGER.info("Retrieving 'ricevute privato' with 'data' greater or equals to '{}'", data);
        List<RicevutaPrivato> ricevutePrivato = ricevutaPrivatoRepository.findByDataGreaterThanEqualOrderByProgressivoDesc(data);
        LOGGER.info("Retrieved {} 'ricevute privato' with 'data' greater or equals to '{}'", ricevutePrivato.size(), data);
        return ricevutePrivato;
    }

    @Transactional
    public RicevutaPrivato create(RicevutaPrivato ricevutaPrivato){
        LOGGER.info("Creating 'ricevuta privato'");

        Integer progressivo = ricevutaPrivato.getProgressivo();
        if(progressivo == null){
            progressivo = getProgressivo(ricevutaPrivato.getAnno());
            ricevutaPrivato.setProgressivo(progressivo);
        }

        checkExistsByAnnoAndProgressivoAndIdNot(ricevutaPrivato.getAnno(),ricevutaPrivato.getProgressivo(), Long.valueOf(-1));

        ricevutaPrivato.setStatoRicevutaPrivato(statoRicevutaPrivatoService.getDaPagare());
        ricevutaPrivato.setSpeditoAde(false);
        ricevutaPrivato.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        LOGGER.info(ricevutaPrivato.getScannerLog());

        RicevutaPrivato createdRicevutaPrivato = ricevutaPrivatoRepository.save(ricevutaPrivato);

        createdRicevutaPrivato.getRicevutaPrivatoArticoli().stream().forEach(faa -> {
            if(faa.getQuantita() != null && faa.getQuantita() != 0 && faa.getPrezzo() != null){
                faa.getId().setRicevutaPrivatoId(createdRicevutaPrivato.getId());
                faa.getId().setUuid(UUID.randomUUID().toString());
                ricevutaPrivatoArticoloService.create(faa);

                // compute 'giacenza articolo'
                giacenzaArticoloService.computeGiacenza(faa.getId().getArticoloId(), faa.getLotto(), faa.getScadenza(), faa.getQuantita(), Resource.RICEVUTA_PRIVATO);
            } else {
                LOGGER.info("RicevutaPrivatoArticolo not saved because quantity null or zero ({}) or prezzo zero ({})", faa.getQuantita(), faa.getPrezzo());
            }
        });

        createdRicevutaPrivato.getRicevutaPrivatoTotali().stream().forEach(fat -> {
            fat.getId().setRicevutaPrivatoId(createdRicevutaPrivato.getId());
            fat.getId().setUuid(UUID.randomUUID().toString());
            ricevutaPrivatoTotaleService.create(fat);
        });

        computeTotali(createdRicevutaPrivato, createdRicevutaPrivato.getRicevutaPrivatoArticoli());

        ricevutaPrivatoRepository.save(createdRicevutaPrivato);
        LOGGER.info("Created 'ricevuta privato' '{}'", createdRicevutaPrivato);

        return createdRicevutaPrivato;
    }

    @Transactional
    public void delete(Long ricevutaPrivatoId){
        LOGGER.info("Deleting 'ricevuta privato' '{}'", ricevutaPrivatoId);

        Set<RicevutaPrivatoArticolo> ricevutaPrivatoArticoli = ricevutaPrivatoArticoloService.findByRicevutaPrivatoId(ricevutaPrivatoId);

        pagamentoRepository.deleteByRicevutaPrivatoId(ricevutaPrivatoId);
        ricevutaPrivatoArticoloService.deleteByRicevutaPrivatoId(ricevutaPrivatoId);
        ricevutaPrivatoTotaleService.deleteByRicevutaPrivatoId(ricevutaPrivatoId);
        ricevutaPrivatoRepository.deleteById(ricevutaPrivatoId);

        for (RicevutaPrivatoArticolo ricevutaPrivatoArticolo:ricevutaPrivatoArticoli) {
            // compute 'giacenza articolo'
            giacenzaArticoloService.computeGiacenza(ricevutaPrivatoArticolo.getId().getArticoloId(), ricevutaPrivatoArticolo.getLotto(), ricevutaPrivatoArticolo.getScadenza(), ricevutaPrivatoArticolo.getQuantita(), Resource.RICEVUTA_PRIVATO);
        }

        LOGGER.info("Deleted 'ricevuta privato' '{}'", ricevutaPrivatoId);
    }

    private void checkExistsByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFattura){
        Optional<RicevutaPrivato> ricevutaPrivato = ricevutaPrivatoRepository.findByAnnoAndProgressivoAndIdNot(anno, progressivo, idFattura);
        if(ricevutaPrivato.isPresent()){
            throw new ResourceAlreadyExistingException(Resource.FATTURA_ACCOMPAGNATORIA, anno, progressivo);
        }
    }

    private void computeTotali(RicevutaPrivato ricevutaPrivato, Set<RicevutaPrivatoArticolo> ricevutaPrivatoArticoli){
        Map<AliquotaIva, Set<RicevutaPrivatoArticolo>> ivaRicevutaPrivatoArticoliMap = new HashMap<>();
        ricevutaPrivatoArticoli.stream().forEach(faa -> {
            Articolo articolo = ricevutaPrivatoArticoloService.getArticolo(faa);
            AliquotaIva iva = articolo.getAliquotaIva();
            Set<RicevutaPrivatoArticolo> ricevutaPrivatoArticoliByIva;
            if(ivaRicevutaPrivatoArticoliMap.containsKey(iva)){
                ricevutaPrivatoArticoliByIva = ivaRicevutaPrivatoArticoliMap.get(iva);
            } else {
                ricevutaPrivatoArticoliByIva = new HashSet<>();
            }
            ricevutaPrivatoArticoliByIva.add(faa);
            ivaRicevutaPrivatoArticoliMap.put(iva, ricevutaPrivatoArticoliByIva);
        });
        Float totaleQuantita = 0f;
        BigDecimal totaleImponibile = new BigDecimal(0);
        BigDecimal totaleIva = new BigDecimal(0);
        BigDecimal totaleCosto = new BigDecimal(0);
        BigDecimal totale = new BigDecimal(0);
        for (Map.Entry<AliquotaIva, Set<RicevutaPrivatoArticolo>> entry : ivaRicevutaPrivatoArticoliMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);
            Set<RicevutaPrivatoArticolo> ricevutaPrivatoArticoliByIva = entry.getValue();
            for(RicevutaPrivatoArticolo ricevutaPrivatoArticolo: ricevutaPrivatoArticoliByIva){
                BigDecimal imponibile = ricevutaPrivatoArticolo.getImponibile() != null ? ricevutaPrivatoArticolo.getImponibile() : BigDecimal.ZERO;
                BigDecimal costo = ricevutaPrivatoArticolo.getCosto() != null ? ricevutaPrivatoArticolo.getCosto() : BigDecimal.ZERO;

                totaleImponibile = totaleImponibile.add(imponibile);
                totaleCosto = totaleCosto.add(costo);
                totaleQuantita = totaleQuantita + (ricevutaPrivatoArticolo.getQuantita() != null ? ricevutaPrivatoArticolo.getQuantita() : 0f);

                BigDecimal partialIva = imponibile.multiply(iva.divide(new BigDecimal(100)));
                totaleIva = totaleIva.add(partialIva);

                totaleByIva = totaleByIva.add(imponibile);
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }
        ricevutaPrivato.setTotaleImponibile(totaleImponibile.setScale(2, RoundingMode.HALF_DOWN));
        ricevutaPrivato.setTotaleIva(totaleIva.setScale(2, RoundingMode.HALF_DOWN));
        ricevutaPrivato.setTotaleCosto(totaleCosto.setScale(2, RoundingMode.HALF_DOWN));
        ricevutaPrivato.setTotale(totale.setScale(2, RoundingMode.HALF_DOWN));
        ricevutaPrivato.setTotaleAcconto(new BigDecimal(0));
        ricevutaPrivato.setTotaleQuantita(new BigDecimal(totaleQuantita).setScale(2, RoundingMode.HALF_DOWN));
    }

}
