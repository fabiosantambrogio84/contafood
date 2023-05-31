package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.FatturaAcquistoRepository;
import com.contafood.repository.PagamentoRepository;
import com.contafood.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FatturaAcquistoService {

    private final FatturaAcquistoRepository fatturaAcquistoRepository;
    private final FatturaAcquistoDdtAcquistoService fatturaAcquistoDdtAcquistoService;
    private final StatoFatturaService statoFatturaService;
    private final StatoDdtService statoDdtService;
    private final DdtAcquistoService ddtAcquistoService;
    private final PagamentoRepository pagamentoRepository;
    private final SimpleDateFormat simpleDateFormat;

    @Autowired
    public FatturaAcquistoService(final FatturaAcquistoRepository fatturaAcquistoRepository,
                                  final FatturaAcquistoDdtAcquistoService fatturaAcquistoDdtAcquistoService,
                                  final StatoFatturaService statoFatturaService,
                                  final StatoDdtService statoDdtService,
                                  final DdtAcquistoService ddtAcquistoService,
                                  final PagamentoRepository pagamentoRepository){
        this.fatturaAcquistoRepository = fatturaAcquistoRepository;
        this.fatturaAcquistoDdtAcquistoService = fatturaAcquistoDdtAcquistoService;
        this.statoFatturaService = statoFatturaService;
        this.statoDdtService = statoDdtService;
        this.ddtAcquistoService = ddtAcquistoService;
        this.pagamentoRepository = pagamentoRepository;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public FatturaAcquisto getOne(Long fatturaAcquistoId){
        log.info("Retrieving 'fattura acquisto' '{}'", fatturaAcquistoId);
        FatturaAcquisto fatturaAcquisto = fatturaAcquistoRepository.findById(fatturaAcquistoId).orElseThrow(ResourceNotFoundException::new);
        log.info("Retrieved 'fattura acquisto' '{}'", fatturaAcquisto);
        return fatturaAcquisto;
    }

    @Transactional
    public FatturaAcquisto create(FatturaAcquisto fatturaAcquisto){
        log.info("Creating 'fattura acquisto'");

        Integer anno = null;
        if(fatturaAcquisto.getData() != null){
            anno = fatturaAcquisto.getData().toLocalDate().getYear();
        }

        fatturaAcquisto.setAnno(anno);
        fatturaAcquisto.setStatoFattura(statoFatturaService.getDaPagare());
        fatturaAcquisto.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        FatturaAcquisto createdFatturaAcquisto = fatturaAcquistoRepository.save(fatturaAcquisto);

        createdFatturaAcquisto.getFatturaAcquistoDdtAcquisti().forEach(fada -> {
            fada.getId().setFatturaAcquistoId(createdFatturaAcquisto.getId());
            fada.getId().setUuid(UUID.randomUUID().toString());
            fatturaAcquistoDdtAcquistoService.create(fada);
        });

        // compute totali
        computeTotali(createdFatturaAcquisto);

        // compute stato
        computeStato(createdFatturaAcquisto);

        fatturaAcquistoRepository.save(createdFatturaAcquisto);
        log.info("Created 'fattura acquisto' '{}'", createdFatturaAcquisto);

        setFatturaAcquistoDdtAcquistiFatturato(createdFatturaAcquisto, true);
        return createdFatturaAcquisto;
    }

    /*
    @Transactional
    public FatturaAcquisto update(FatturaAcquisto fatturaAcquisto){
        log.info("Updating 'fattura acquisto'");
        checkExistsByAnnoAndProgressivoAndIdNot(fatturaAcquisto.getAnno(), fatturaAcquisto.getProgressivo(), fatturaAcquisto.getId());

        Set<FatturaAcquistoDdtAcquisto> fatturaAcquistoDdtAcquisti = fatturaAcquisto.getFatturaAcquistoDdtAcquisti();
        fatturaAcquisto.setFatturaAcquistoDdtAcquisti(new HashSet<>());
        fatturaAcquistoDdtAcquistoService.deleteByFatturaAcquistoId(fatturaAcquisto.getId());

        FatturaAcquisto fatturaAcquistoCurrent = fatturaAcquistoRepository.findById(fatturaAcquisto.getId()).orElseThrow(ResourceNotFoundException::new);
        fatturaAcquisto.setStatoFattura(fatturaAcquistoCurrent.getStatoFattura());
        fatturaAcquisto.setDataInserimento(fatturaAcquistoCurrent.getDataInserimento());
        fatturaAcquisto.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        FatturaAcquisto updatedFatturaAcquisto = fatturaAcquistoRepository.save(fatturaAcquisto);
        fatturaAcquistoDdtAcquisti.forEach(fada -> {
            fada.getId().setFatturaAcquistoId(updatedFatturaAcquisto.getId());
            fada.getId().setUuid(UUID.randomUUID().toString());
            fatturaAcquistoDdtAcquistoService.create(fada);
        });

        fatturaAcquistoRepository.save(updatedFatturaAcquisto);
        log.info("Updated 'fattura acquisto' '{}'", updatedFatturaAcquisto);
        return updatedFatturaAcquisto;
    }
    */

    @Transactional
    public FatturaAcquisto patch(Map<String,Object> patchFatturaAcquisto) throws Exception{
        log.info("Patching 'fattura acquisto'");

        Long id = Long.valueOf((Integer) patchFatturaAcquisto.get("id"));
        FatturaAcquisto fatturaAcquisto = fatturaAcquistoRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        for(String key : patchFatturaAcquisto.keySet()){
            Object value = patchFatturaAcquisto.get(key);
            switch (key) {
                case "id":
                    fatturaAcquisto.setId(Long.valueOf((Integer) value));
                    break;
                case "numero":
                    fatturaAcquisto.setNumero((String) value);
                    break;
                case "anno":
                    fatturaAcquisto.setAnno((Integer) value);
                    break;
                case "data":
                    fatturaAcquisto.setData(new Date(simpleDateFormat.parse((String) value).getTime()));
                    break;
                case "note":
                    fatturaAcquisto.setNote((String) value);
                    break;
            }
        }
        FatturaAcquisto patchedFatturaAcquisto = fatturaAcquistoRepository.save(fatturaAcquisto);

        log.info("Patched 'fattura acquisto' '{}'", patchedFatturaAcquisto);
        return patchedFatturaAcquisto;
    }

    @Transactional
    public void delete(Long fatturaAcquistoId){
        log.info("Deleting 'fattura acquisto' '{}'", fatturaAcquistoId);

        FatturaAcquisto fatturaAcquisto = fatturaAcquistoRepository.findById(fatturaAcquistoId).orElseThrow(ResourceNotFoundException::new);
        setFatturaAcquistoDdtAcquistiFatturato(fatturaAcquisto, false);

        pagamentoRepository.deleteByFatturaId(fatturaAcquistoId);
        fatturaAcquistoDdtAcquistoService.deleteByFatturaAcquistoId(fatturaAcquistoId);
        fatturaAcquistoRepository.deleteById(fatturaAcquistoId);
        log.info("Deleted 'fattura acquisto' '{}'", fatturaAcquistoId);
    }

    /*
    public void checkStatoFatturaDaPagare(Integer idStato){
        StatoFattura statoFatturaDaPagare = statoFatturaService.getDaPagare();
        if(idStato != statoFatturaDaPagare.getId().intValue()){
            throw new GenericException("Stato diverso da '"+statoFatturaDaPagare.getDescrizione()+"'");
        }
    }
    */

    private void computeStato(FatturaAcquisto fatturaAcquisto){
        StatoDdt ddtStatoPagato = statoDdtService.getPagato();
        StatoDdt ddtStatoDaPagare = statoDdtService.getDaPagare();

        Set<DdtAcquisto> ddtsAcquisto = new HashSet<>();
        fatturaAcquisto.getFatturaAcquistoDdtAcquisti().forEach(fada -> {
            Long idDdtAcquisto = fada.getId().getDdtAcquistoId();
            ddtsAcquisto.add(ddtAcquistoService.getOne(idDdtAcquisto));
        });
        int ddtsAcquistoSize = ddtsAcquisto.size();
        log.info("Fattura ddtsAcquisto size {}", ddtsAcquistoSize);
        Set<DdtAcquisto> ddtsAcquistoPagati = ddtsAcquisto.stream().filter(da -> da.getStatoDdt() != null).filter(da -> da.getStatoDdt().equals(ddtStatoPagato)).collect(Collectors.toSet());
        if(ddtsAcquistoSize == ddtsAcquistoPagati.size()){
            fatturaAcquisto.setStatoFattura(statoFatturaService.getPagata());
            return;
        }
        Set<DdtAcquisto> ddtsAcquistoDaPagare = ddtsAcquisto.stream().filter(da -> da.getStatoDdt() != null).filter(da -> da.getStatoDdt().equals(ddtStatoDaPagare)).collect(Collectors.toSet());
        if(ddtsAcquistoSize == ddtsAcquistoDaPagare.size()){
            fatturaAcquisto.setStatoFattura(statoFatturaService.getDaPagare());
            return;
        }
        fatturaAcquisto.setStatoFattura(statoFatturaService.getParzialmentePagata());
    }

    /*
    private void computeTotaleAcconto(FatturaAcquisto fatturaAcquisto){
        log.info("Computing totaleAcconto...");

        BigDecimal totaleAcconto = BigDecimal.ZERO;

        Set<DdtAcquisto> ddtsAcquisto = new HashSet<>();
        fatturaAcquisto.getFatturaAcquistoDdtAcquisti().forEach(fada -> {
            Long idDdtAcquisto = fada.getId().getDdtAcquistoId();
            ddtsAcquisto.add(ddtAcquistoService.getOne(idDdtAcquisto));
        });
        log.info("Fattura ddts-acquisto size {}", ddtsAcquisto.size());

        for(DdtAcquisto ddtAcquisto: ddtsAcquisto){
            BigDecimal ddtAcquistoTotaleAcconto = ddtAcquisto.getTotaleAcconto();
            if(ddtAcquistoTotaleAcconto == null){
                ddtAcquistoTotaleAcconto = BigDecimal.ZERO;
            }
            totaleAcconto = totaleAcconto.add(ddtAcquistoTotaleAcconto);
        }
        fatturaAcquisto.setTotaleAcconto(Utils.roundPrice(totaleAcconto));
    }
    */

    private void computeTotali(FatturaAcquisto fatturaAcquisto){

        BigDecimal totaleAcconto = BigDecimal.ZERO;
        BigDecimal totaleImponibile = BigDecimal.ZERO;
        BigDecimal totaleIva = BigDecimal.ZERO;

        Set<DdtAcquisto> ddtsAcquisto = new HashSet<>();
        fatturaAcquisto.getFatturaAcquistoDdtAcquisti().forEach(fada -> {
            Long idDdtAcquisto = fada.getId().getDdtAcquistoId();
            ddtsAcquisto.add(ddtAcquistoService.getOne(idDdtAcquisto));
        });

        if(!ddtsAcquisto.isEmpty()){
            Set<DdtAcquistoArticolo> ddtAcquistoArticoli = new HashSet<>();
            Set<DdtAcquistoIngrediente> ddtAcquistoIngredienti = new HashSet<>();

            for(DdtAcquisto ddtAcquisto: ddtsAcquisto){
                BigDecimal ddtAcquistoTotaleAcconto = ddtAcquisto.getTotaleAcconto();
                if(ddtAcquistoTotaleAcconto == null){
                    ddtAcquistoTotaleAcconto = BigDecimal.ZERO;
                }
                totaleAcconto = totaleAcconto.add(ddtAcquistoTotaleAcconto);

                ddtAcquistoArticoli.addAll(ddtAcquisto.getDdtAcquistoArticoli());
                ddtAcquistoIngredienti.addAll(ddtAcquisto.getDdtAcquistoIngredienti());
            }
            if(!ddtAcquistoArticoli.isEmpty()){
                for(DdtAcquistoArticolo ddtAcquistoArticolo : ddtAcquistoArticoli){
                    Articolo articolo = ddtAcquistoArticolo.getArticolo();
                    AliquotaIva aliquotaIva = articolo.getAliquotaIva();

                    BigDecimal ivaValore = aliquotaIva.getValore();
                    BigDecimal imponibile = ddtAcquistoArticolo.getImponibile();
                    totaleImponibile = totaleImponibile.add(imponibile);
                    totaleIva = totaleIva.add(imponibile.multiply(ivaValore.divide(new BigDecimal(100))));
                }
            }
            if(!ddtAcquistoIngredienti.isEmpty()){
                for(DdtAcquistoIngrediente ddtAcquistoIngrediente : ddtAcquistoIngredienti){
                    Ingrediente ingrediente = ddtAcquistoIngrediente.getIngrediente();
                    AliquotaIva aliquotaIva = ingrediente.getAliquotaIva();

                    BigDecimal ivaValore = aliquotaIva.getValore();
                    BigDecimal imponibile = ddtAcquistoIngrediente.getImponibile();
                    totaleImponibile = totaleImponibile.add(imponibile);
                    totaleIva = totaleIva.add(imponibile.multiply(ivaValore.divide(new BigDecimal(100))));
                }

            }
        }

        fatturaAcquisto.setTotaleAcconto(Utils.roundPrice(totaleAcconto));
        fatturaAcquisto.setTotaleIva(Utils.roundPrice(totaleIva));
        fatturaAcquisto.setTotaleImponibile(Utils.roundPrice(totaleImponibile));
    }

    private void setFatturaAcquistoDdtAcquistiFatturato(FatturaAcquisto fatturaAcquisto, boolean fatturato){
        log.info("Setting 'fatturato'={} on all 'ddt-acquisto' of 'fattura acquisto' '{}'", fatturato, fatturaAcquisto.getId());
        fatturaAcquisto.getFatturaAcquistoDdtAcquisti().forEach(fada -> {
            Long idDdtAcquisto = fada.getId().getDdtAcquistoId();
            Map<String,Object> patchDdtAcquisto = new HashMap<>();
            patchDdtAcquisto.put("id", idDdtAcquisto.intValue());
            patchDdtAcquisto.put("fatturato", fatturato);
            ddtAcquistoService.patch(patchDdtAcquisto);
        });
        log.info("Successfully set 'fatturato'={} on all 'ddt-acquisto' of 'fattura acquisto' '{}'", fatturato, fatturaAcquisto.getId());
    }
}