package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.PagamentoRepository;
import com.contafood.repository.RicevutaPrivatoRepository;
import com.contafood.util.Utils;
import com.contafood.util.enumeration.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Service
public class RicevutaPrivatoService {

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
        log.info("Retrieving the list of 'ricevute privato'");
        Set<RicevutaPrivato> ricevutePrivato = ricevutaPrivatoRepository.findAllByOrderByAnnoDescProgressivoDesc();
        log.info("Retrieved {} 'ricevute privato'", ricevutePrivato.size());
        return ricevutePrivato;
    }

    public RicevutaPrivato getOne(Long ricevutaPrivatoId){
        log.info("Retrieving 'fattura accompagnatoria' '{}'", ricevutaPrivatoId);
        RicevutaPrivato ricevutaPrivato = ricevutaPrivatoRepository.findById(ricevutaPrivatoId).orElseThrow(ResourceNotFoundException::new);
        log.info("Retrieved 'fattura accompagnatoria' '{}'", ricevutaPrivato);
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

    /*
    public List<RicevutaPrivato> getByDataGreaterThanEqual(Date data){
        log.info("Retrieving 'ricevute privato' with 'data' greater or equals to '{}'", data);
        List<RicevutaPrivato> ricevutePrivato = ricevutaPrivatoRepository.findByDataGreaterThanEqualOrderByProgressivoDesc(data);
        log.info("Retrieved {} 'ricevute privato' with 'data' greater or equals to '{}'", ricevutePrivato.size(), data);
        return ricevutePrivato;
    }
    */

    @Transactional
    public RicevutaPrivato create(RicevutaPrivato ricevutaPrivato){
        log.info("Creating 'ricevuta privato'");

        Integer progressivo = ricevutaPrivato.getProgressivo();
        if(progressivo == null){
            progressivo = getProgressivo(ricevutaPrivato.getAnno());
            ricevutaPrivato.setProgressivo(progressivo);
        }

        checkExistsByAnnoAndProgressivoAndIdNot(ricevutaPrivato.getAnno(),ricevutaPrivato.getProgressivo(), -1L);

        if(ricevutaPrivato.getNumeroColli() == null){
            ricevutaPrivato.setNumeroColli(1);
        }
        if(ricevutaPrivato.getDataTrasporto() == null){
            ricevutaPrivato.setDataTrasporto(new Date(System.currentTimeMillis()));
        }
        if(ricevutaPrivato.getOraTrasporto() == null){
            ricevutaPrivato.setOraTrasporto(Time.valueOf("06:00:00"));
        }

        ricevutaPrivato.setStatoRicevutaPrivato(statoRicevutaPrivatoService.getDaPagare());
        ricevutaPrivato.setSpeditoAde(false);
        ricevutaPrivato.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        log.info(ricevutaPrivato.getScannerLog());

        RicevutaPrivato createdRicevutaPrivato = ricevutaPrivatoRepository.save(ricevutaPrivato);

        createdRicevutaPrivato.getRicevutaPrivatoArticoli().stream().forEach(faa -> {
            if(faa.getQuantita() != null && faa.getQuantita() != 0 && faa.getPrezzo() != null){
                faa.getId().setRicevutaPrivatoId(createdRicevutaPrivato.getId());
                faa.getId().setUuid(UUID.randomUUID().toString());
                ricevutaPrivatoArticoloService.create(faa);

                // compute 'giacenza articolo'
                giacenzaArticoloService.computeGiacenza(faa.getId().getArticoloId(), faa.getLotto(), faa.getScadenza(), faa.getQuantita(), Resource.RICEVUTA_PRIVATO);
            } else {
                log.info("RicevutaPrivatoArticolo not saved because quantity null or zero ({}) or prezzo zero ({})", faa.getQuantita(), faa.getPrezzo());
            }
        });

        createdRicevutaPrivato.getRicevutaPrivatoTotali().stream().forEach(fat -> {
            fat.getId().setRicevutaPrivatoId(createdRicevutaPrivato.getId());
            fat.getId().setUuid(UUID.randomUUID().toString());
            ricevutaPrivatoTotaleService.create(fat);
        });

        computeTotali(createdRicevutaPrivato, createdRicevutaPrivato.getRicevutaPrivatoArticoli());

        ricevutaPrivatoRepository.save(createdRicevutaPrivato);
        log.info("Created 'ricevuta privato' '{}'", createdRicevutaPrivato);

        return createdRicevutaPrivato;
    }

    @Transactional
    public RicevutaPrivato update(RicevutaPrivato ricevutaPrivato){
        log.info("Updating 'ricevuta privato'");

        Integer progressivo = ricevutaPrivato.getProgressivo();
        if(progressivo == null){
            progressivo = getProgressivo(ricevutaPrivato.getAnno());
            ricevutaPrivato.setProgressivo(progressivo);
        }

        checkExistsByAnnoAndProgressivoAndIdNot(ricevutaPrivato.getAnno(),ricevutaPrivato.getProgressivo(), ricevutaPrivato.getId());

        if(ricevutaPrivato.getNumeroColli() == null){
            ricevutaPrivato.setNumeroColli(1);
        }
        if(ricevutaPrivato.getDataTrasporto() == null){
            ricevutaPrivato.setDataTrasporto(new Date(System.currentTimeMillis()));
        }
        if(ricevutaPrivato.getOraTrasporto() == null){
            ricevutaPrivato.setOraTrasporto(Time.valueOf("06:00:00"));
        }

        Boolean modificaGiacenze = ricevutaPrivato.getModificaGiacenze();

        Set<RicevutaPrivatoArticolo> ricevutaPrivatoArticoli = ricevutaPrivato.getRicevutaPrivatoArticoli();
        ricevutaPrivato.setRicevutaPrivatoArticoli(new HashSet<>());
        ricevutaPrivatoArticoloService.deleteByRicevutaPrivatoId(ricevutaPrivato.getId());

        Set<RicevutaPrivatoTotale> ricevutaPrivatoTotali = ricevutaPrivato.getRicevutaPrivatoTotali();
        ricevutaPrivato.setRicevutaPrivatoTotali(new HashSet<>());
        ricevutaPrivatoTotaleService.deleteByRicevutaPrivatoId(ricevutaPrivato.getId());

        RicevutaPrivato ricevutaPrivatoCurrent = ricevutaPrivatoRepository.findById(ricevutaPrivato.getId()).orElseThrow(ResourceNotFoundException::new);
        ricevutaPrivato.setStatoRicevutaPrivato(ricevutaPrivatoCurrent.getStatoRicevutaPrivato());
        ricevutaPrivato.setSpeditoAde(ricevutaPrivatoCurrent.getSpeditoAde());
        ricevutaPrivato.setDataInserimento(ricevutaPrivatoCurrent.getDataInserimento());
        ricevutaPrivato.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        log.info(ricevutaPrivato.getScannerLog());

        RicevutaPrivato updatedRicevutaPrivato = ricevutaPrivatoRepository.save(ricevutaPrivato);

        ricevutaPrivatoArticoli.stream().forEach(faa -> {
            if(faa.getQuantita() != null && faa.getQuantita() != 0 && faa.getPrezzo() != null){
                faa.getId().setRicevutaPrivatoId(updatedRicevutaPrivato.getId());
                faa.getId().setUuid(UUID.randomUUID().toString());
                ricevutaPrivatoArticoloService.create(faa);

                if(modificaGiacenze != null && modificaGiacenze.equals(Boolean.TRUE)) {
                    // compute 'giacenza articolo'
                    giacenzaArticoloService.computeGiacenza(faa.getId().getArticoloId(), faa.getLotto(), faa.getScadenza(), faa.getQuantita(), Resource.RICEVUTA_PRIVATO);
                }

            } else {
                log.info("RicevutaPrivatoArticolo not saved because quantity null or zero ({}) or prezzo zero ({})", faa.getQuantita(), faa.getPrezzo());
            }
        });

        ricevutaPrivatoTotali.stream().forEach(fat -> {
            fat.getId().setRicevutaPrivatoId(updatedRicevutaPrivato.getId());
            fat.getId().setUuid(UUID.randomUUID().toString());
            ricevutaPrivatoTotaleService.create(fat);
        });

        computeTotali(updatedRicevutaPrivato, ricevutaPrivatoArticoli);

        ricevutaPrivatoRepository.save(updatedRicevutaPrivato);
        log.info("Updated 'ricevuta privato' '{}'", updatedRicevutaPrivato);

        return updatedRicevutaPrivato;
    }

    @Transactional
    public void delete(Long ricevutaPrivatoId){
        log.info("Deleting 'ricevuta privato' '{}'", ricevutaPrivatoId);

        Set<RicevutaPrivatoArticolo> ricevutaPrivatoArticoli = ricevutaPrivatoArticoloService.findByRicevutaPrivatoId(ricevutaPrivatoId);

        pagamentoRepository.deleteByRicevutaPrivatoId(ricevutaPrivatoId);
        ricevutaPrivatoArticoloService.deleteByRicevutaPrivatoId(ricevutaPrivatoId);
        ricevutaPrivatoTotaleService.deleteByRicevutaPrivatoId(ricevutaPrivatoId);
        ricevutaPrivatoRepository.deleteById(ricevutaPrivatoId);

        for (RicevutaPrivatoArticolo ricevutaPrivatoArticolo:ricevutaPrivatoArticoli) {
            // compute 'giacenza articolo'
            giacenzaArticoloService.computeGiacenza(ricevutaPrivatoArticolo.getId().getArticoloId(), ricevutaPrivatoArticolo.getLotto(), ricevutaPrivatoArticolo.getScadenza(), ricevutaPrivatoArticolo.getQuantita(), Resource.RICEVUTA_PRIVATO);
        }

        log.info("Deleted 'ricevuta privato' '{}'", ricevutaPrivatoId);
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
        Integer totalePezzi = 0;
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
                totalePezzi = totalePezzi + (ricevutaPrivatoArticolo.getNumeroPezzi() != null ? ricevutaPrivatoArticolo.getNumeroPezzi() : 0);

                BigDecimal partialIva = imponibile.multiply(iva.divide(new BigDecimal(100)));
                totaleIva = totaleIva.add(partialIva);

                totaleByIva = totaleByIva.add(imponibile);
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }
        ricevutaPrivato.setTotaleImponibile(Utils.roundPrice(totaleImponibile));
        ricevutaPrivato.setTotaleIva(Utils.roundPrice(totaleIva));
        ricevutaPrivato.setTotaleCosto(Utils.roundPrice(totaleCosto));
        ricevutaPrivato.setTotale(Utils.roundPrice(totale));
        ricevutaPrivato.setTotaleAcconto(new BigDecimal(0));
        ricevutaPrivato.setTotaleQuantita(Utils.roundQuantity(new BigDecimal(totaleQuantita)));
        ricevutaPrivato.setTotalePezzi(totalePezzi);
    }
}
