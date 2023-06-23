package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.FatturaAccompagnatoriaAcquistoRepository;
import com.contafood.repository.PagamentoRepository;
import com.contafood.util.Utils;
import com.contafood.util.enumeration.Resource;
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

@Slf4j
@Service
public class FatturaAccompagnatoriaAcquistoService {

    private final FatturaAccompagnatoriaAcquistoRepository fatturaAccompagnatoriaAcquistoRepository;
    private final FatturaAccompagnatoriaAcquistoArticoloService fatturaAccompagnatoriaAcquistoArticoloService;
    private final FatturaAccompagnatoriaAcquistoIngredienteService fatturaAccompagnatoriaAcquistoIngredienteService;
    private final FatturaAccompagnatoriaAcquistoTotaleService fatturaAccompagnatoriaAcquistoTotaleService;
    private final StatoFatturaService statoFatturaService;
    private final TipoFatturaService tipoFatturaService;
    private final GiacenzaArticoloService giacenzaArticoloService;
    private final GiacenzaIngredienteService giacenzaIngredienteService;
    private final PagamentoRepository pagamentoRepository;
    private final SimpleDateFormat simpleDateFormat;

    @Autowired
    public FatturaAccompagnatoriaAcquistoService(final FatturaAccompagnatoriaAcquistoRepository fatturaAccompagnatoriaAcquistoRepository,
                                                 final FatturaAccompagnatoriaAcquistoArticoloService fatturaAccompagnatoriaAcquistoArticoloService,
                                                 final FatturaAccompagnatoriaAcquistoIngredienteService fatturaAccompagnatoriaAcquistoIngredienteService,
                                                 final FatturaAccompagnatoriaAcquistoTotaleService fatturaAccompagnatoriaAcquistoTotaleService,
                                                 final StatoFatturaService statoFatturaService,
                                                 final TipoFatturaService tipoFatturaService,
                                                 final GiacenzaArticoloService giacenzaArticoloService,
                                                 final GiacenzaIngredienteService giacenzaIngredienteService,
                                                 final PagamentoRepository pagamentoRepository){
        this.fatturaAccompagnatoriaAcquistoRepository = fatturaAccompagnatoriaAcquistoRepository;
        this.fatturaAccompagnatoriaAcquistoArticoloService = fatturaAccompagnatoriaAcquistoArticoloService;
        this.fatturaAccompagnatoriaAcquistoIngredienteService = fatturaAccompagnatoriaAcquistoIngredienteService;
        this.fatturaAccompagnatoriaAcquistoTotaleService = fatturaAccompagnatoriaAcquistoTotaleService;
        this.statoFatturaService = statoFatturaService;
        this.tipoFatturaService = tipoFatturaService;
        this.giacenzaArticoloService = giacenzaArticoloService;
        this.giacenzaIngredienteService = giacenzaIngredienteService;
        this.pagamentoRepository = pagamentoRepository;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public Set<FatturaAccompagnatoriaAcquisto> getAll(){
        log.info("Retrieving the list of 'fatture accompagnatorie acquisto'");
        Set<FatturaAccompagnatoriaAcquisto> fattureAccompagnatorieAcquisto = fatturaAccompagnatoriaAcquistoRepository.findAllByOrderByAnnoDescNumeroDesc();
        log.info("Retrieved {} 'fatture accompagnatorie acquisto'", fattureAccompagnatorieAcquisto.size());
        return fattureAccompagnatorieAcquisto;
    }

    public FatturaAccompagnatoriaAcquisto getOne(Long fatturaAccompagnatoriaAcquistoId){
        log.info("Retrieving 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquistoId);
        FatturaAccompagnatoriaAcquisto fatturaAccompagnatoriaAcquisto = fatturaAccompagnatoriaAcquistoRepository.findById(fatturaAccompagnatoriaAcquistoId).orElseThrow(ResourceNotFoundException::new);
        log.info("Retrieved 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquisto);
        return fatturaAccompagnatoriaAcquisto;
    }

    @Transactional
    public FatturaAccompagnatoriaAcquisto create(FatturaAccompagnatoriaAcquisto fatturaAccompagnatoriaAcquisto){
        log.info("Creating 'fattura accompagnatoria acquisto'");

        checkExistsByFornitoreAndNumeroAndIdNot(fatturaAccompagnatoriaAcquisto.getFornitore(),fatturaAccompagnatoriaAcquisto.getNumero(), (long) -1);

        fatturaAccompagnatoriaAcquisto.setStatoFattura(statoFatturaService.getDaPagare());
        fatturaAccompagnatoriaAcquisto.setTipoFattura(tipoFatturaService.getAccompagnatoriaAcquisto());
        fatturaAccompagnatoriaAcquisto.setSpeditoAde(false);
        fatturaAccompagnatoriaAcquisto.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        log.info(fatturaAccompagnatoriaAcquisto.getScannerLog());

        FatturaAccompagnatoriaAcquisto createdFatturaAccompagnatoriaAcquisto = fatturaAccompagnatoriaAcquistoRepository.save(fatturaAccompagnatoriaAcquisto);

        createdFatturaAccompagnatoriaAcquisto.getFatturaAccompagnatoriaAcquistoArticoli().stream().forEach(faa -> {
            if(faa.getQuantita() != null && faa.getQuantita() != 0 && faa.getPrezzo() != null){
                faa.getId().setFatturaAccompagnatoriaAcquistoId(createdFatturaAccompagnatoriaAcquisto.getId());
                faa.getId().setUuid(UUID.randomUUID().toString());
                fatturaAccompagnatoriaAcquistoArticoloService.create(faa);

                // compute 'giacenza articolo'
                giacenzaArticoloService.computeGiacenza(faa.getId().getArticoloId(), faa.getLotto(), faa.getScadenza(), faa.getQuantita(), Resource.FATTURA_ACCOMPAGNATORIA_ACQUISTO);
            } else {
                log.info("FatturaAccompagnatoriaAcquistoArticolo not saved because quantity null or zero ({}) or prezzo zero ({})", faa.getQuantita(), faa.getPrezzo());
            }
        });

        createdFatturaAccompagnatoriaAcquisto.getFatturaAccompagnatoriaAcquistoIngredienti().stream().forEach(fai -> {
            if(fai.getQuantita() != null && fai.getQuantita() != 0 && fai.getPrezzo() != null){
                fai.getId().setFatturaAccompagnatoriaAcquistoId(createdFatturaAccompagnatoriaAcquisto.getId());
                fai.getId().setUuid(UUID.randomUUID().toString());
                fatturaAccompagnatoriaAcquistoIngredienteService.create(fai);

                // compute 'giacenza ingrediente'
                giacenzaIngredienteService.computeGiacenza(fai.getId().getIngredienteId(), fai.getLotto(), fai.getScadenza(), fai.getQuantita(), Resource.FATTURA_ACCOMPAGNATORIA_ACQUISTO);
            } else {
                log.info("FatturaAccompagnatoriaAcquistoArticolo not saved because quantity null or zero ({}) or prezzo zero ({})", fai.getQuantita(), fai.getPrezzo());
            }
        });

        createdFatturaAccompagnatoriaAcquisto.getFatturaAccompagnatoriaAcquistoTotali().stream().forEach(fat -> {
            fat.getId().setFatturaAccompagnatoriaAcquistoId(createdFatturaAccompagnatoriaAcquisto.getId());
            fat.getId().setUuid(UUID.randomUUID().toString());
            fatturaAccompagnatoriaAcquistoTotaleService.create(fat);
        });

        computeTotali(createdFatturaAccompagnatoriaAcquisto, createdFatturaAccompagnatoriaAcquisto.getFatturaAccompagnatoriaAcquistoArticoli(), createdFatturaAccompagnatoriaAcquisto.getFatturaAccompagnatoriaAcquistoIngredienti(), createdFatturaAccompagnatoriaAcquisto.getFatturaAccompagnatoriaAcquistoTotali());

        fatturaAccompagnatoriaAcquistoRepository.save(createdFatturaAccompagnatoriaAcquisto);
        log.info("Created 'fattura accompagnatoria acquisto' '{}'", createdFatturaAccompagnatoriaAcquisto);

        return createdFatturaAccompagnatoriaAcquisto;
    }

    @Transactional
    public FatturaAccompagnatoriaAcquisto patch(Map<String,Object> patchFatturaAccompagnatoriaAcquisto) throws Exception{
        log.info("Patching 'fattura accompagnatoria acquisto'");

        Long id = Long.valueOf((Integer) patchFatturaAccompagnatoriaAcquisto.get("id"));
        FatturaAccompagnatoriaAcquisto fatturaAccompagnatoriaAcquisto = fatturaAccompagnatoriaAcquistoRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        for(String key : patchFatturaAccompagnatoriaAcquisto.keySet()){
            Object value = patchFatturaAccompagnatoriaAcquisto.get(key);
            switch (key) {
                case "id":
                    fatturaAccompagnatoriaAcquisto.setId(Long.valueOf((Integer) value));
                    break;
                case "numero":
                    fatturaAccompagnatoriaAcquisto.setNumero((String) value);
                    break;
                case "data":
                    fatturaAccompagnatoriaAcquisto.setData(new Date(simpleDateFormat.parse((String) value).getTime()));
                    break;
                case "note":
                    fatturaAccompagnatoriaAcquisto.setNote((String) value);
                    break;
            }
        }
        checkExistsByFornitoreAndNumeroAndIdNot(fatturaAccompagnatoriaAcquisto.getFornitore(),fatturaAccompagnatoriaAcquisto.getNumero(), fatturaAccompagnatoriaAcquisto.getId());

        FatturaAccompagnatoriaAcquisto patchedFatturaAccompagnatoriaAcquisto = fatturaAccompagnatoriaAcquistoRepository.save(fatturaAccompagnatoriaAcquisto);

        log.info("Patched 'fattura accompagnatoria acquisto' '{}'", patchedFatturaAccompagnatoriaAcquisto);
        return patchedFatturaAccompagnatoriaAcquisto;
    }

    @Transactional
    public void delete(Long fatturaAccompagnatoriaAcquistoId){
        log.info("Deleting 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquistoId);

        Set<FatturaAccompagnatoriaAcquistoArticolo> fatturaAccompagnatoriaAcquistoArticoli = fatturaAccompagnatoriaAcquistoArticoloService.findByFatturaAccompagnatoriaAcquistoId(fatturaAccompagnatoriaAcquistoId);
        Set<FatturaAccompagnatoriaAcquistoIngrediente> fatturaAccompagnatoriaAcquistoIngredienti = fatturaAccompagnatoriaAcquistoIngredienteService.findByFatturaAccompagnatoriaAcquistoId(fatturaAccompagnatoriaAcquistoId);

        pagamentoRepository.deleteByFatturaAccompagnatoriaAcquistoId(fatturaAccompagnatoriaAcquistoId);
        fatturaAccompagnatoriaAcquistoArticoloService.deleteByFatturaAccompagnatoriaAcquistoId(fatturaAccompagnatoriaAcquistoId);
        fatturaAccompagnatoriaAcquistoIngredienteService.deleteByFatturaAccompagnatoriaAcquistoId(fatturaAccompagnatoriaAcquistoId);
        fatturaAccompagnatoriaAcquistoTotaleService.deleteByFatturaAccompagnatoriaAcquistoId(fatturaAccompagnatoriaAcquistoId);
        fatturaAccompagnatoriaAcquistoRepository.deleteById(fatturaAccompagnatoriaAcquistoId);

        for (FatturaAccompagnatoriaAcquistoArticolo fatturaAccompagnatoriaAcquistoArticolo:fatturaAccompagnatoriaAcquistoArticoli) {
            // compute 'giacenza articolo'
            giacenzaArticoloService.computeGiacenza(fatturaAccompagnatoriaAcquistoArticolo.getId().getArticoloId(), fatturaAccompagnatoriaAcquistoArticolo.getLotto(), fatturaAccompagnatoriaAcquistoArticolo.getScadenza(), fatturaAccompagnatoriaAcquistoArticolo.getQuantita(), Resource.FATTURA_ACCOMPAGNATORIA_ACQUISTO);
        }
        for (FatturaAccompagnatoriaAcquistoIngrediente fatturaAccompagnatoriaAcquistoIngrediente:fatturaAccompagnatoriaAcquistoIngredienti) {
            // compute 'giacenza ingrediente'
            giacenzaIngredienteService.computeGiacenza(fatturaAccompagnatoriaAcquistoIngrediente.getId().getIngredienteId(), fatturaAccompagnatoriaAcquistoIngrediente.getLotto(), fatturaAccompagnatoriaAcquistoIngrediente.getScadenza(), fatturaAccompagnatoriaAcquistoIngrediente.getQuantita(), Resource.FATTURA_ACCOMPAGNATORIA_ACQUISTO);
        }

        log.info("Deleted 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquistoId);
    }

    private void checkExistsByFornitoreAndNumeroAndIdNot(Fornitore fornitore, String numero, Long idFattura){
        if(fornitore != null){
            Optional<FatturaAccompagnatoriaAcquisto> fatturaAccompagnatoriaAcquisto = fatturaAccompagnatoriaAcquistoRepository.findByFornitoreIdAndNumeroAndIdNot(fornitore.getId(), numero, idFattura);
            if(fatturaAccompagnatoriaAcquisto.isPresent()){
                throw new ResourceAlreadyExistingException(Resource.FATTURA_ACCOMPAGNATORIA_ACQUISTO);
            }
        }
    }

    private void computeTotali(FatturaAccompagnatoriaAcquisto fatturaAccompagnatoriaAcquisto, Set<FatturaAccompagnatoriaAcquistoArticolo> fatturaAccompagnatoriaAcquistoArticoli, Set<FatturaAccompagnatoriaAcquistoIngrediente> fatturaAccompagnatoriaAcquistoIngredienti, Set<FatturaAccompagnatoriaAcquistoTotale> fatturaAccompagnatoriaAcquistoTotali){
        Map<AliquotaIva, Set<FatturaAccompagnatoriaAcquistoArticolo>> ivaFatturaAccompagnatoriaAcquistoArticoliMap = new HashMap<>();
        fatturaAccompagnatoriaAcquistoArticoli.stream().forEach(faa -> {
            Articolo articolo = fatturaAccompagnatoriaAcquistoArticoloService.getArticolo(faa);
            AliquotaIva iva = articolo.getAliquotaIva();
            Set<FatturaAccompagnatoriaAcquistoArticolo> fatturaAccompagnatoriaAcquistoArticoliByIva;
            if(ivaFatturaAccompagnatoriaAcquistoArticoliMap.containsKey(iva)){
                fatturaAccompagnatoriaAcquistoArticoliByIva = ivaFatturaAccompagnatoriaAcquistoArticoliMap.get(iva);
            } else {
                fatturaAccompagnatoriaAcquistoArticoliByIva = new HashSet<>();
            }
            fatturaAccompagnatoriaAcquistoArticoliByIva.add(faa);
            ivaFatturaAccompagnatoriaAcquistoArticoliMap.put(iva, fatturaAccompagnatoriaAcquistoArticoliByIva);
        });

        Map<AliquotaIva, Set<FatturaAccompagnatoriaAcquistoIngrediente>> ivaFatturaAccompagnatoriaAcquistoIngredientiMap = new HashMap<>();
        fatturaAccompagnatoriaAcquistoIngredienti.stream().forEach(fai -> {
            Ingrediente ingrediente = fatturaAccompagnatoriaAcquistoIngredienteService.getIngrediente(fai);
            AliquotaIva iva = ingrediente.getAliquotaIva();
            Set<FatturaAccompagnatoriaAcquistoIngrediente> fatturaAccompagnatoriaAcquistoIngredientiByIva;
            if(ivaFatturaAccompagnatoriaAcquistoIngredientiMap.containsKey(iva)){
                fatturaAccompagnatoriaAcquistoIngredientiByIva = ivaFatturaAccompagnatoriaAcquistoIngredientiMap.get(iva);
            } else {
                fatturaAccompagnatoriaAcquistoIngredientiByIva = new HashSet<>();
            }
            fatturaAccompagnatoriaAcquistoIngredientiByIva.add(fai);
            ivaFatturaAccompagnatoriaAcquistoIngredientiMap.put(iva, fatturaAccompagnatoriaAcquistoIngredientiByIva);
        });

        float totaleQuantita = 0f;
        BigDecimal totale = new BigDecimal(0);

        for (Map.Entry<AliquotaIva, Set<FatturaAccompagnatoriaAcquistoArticolo>> entry : ivaFatturaAccompagnatoriaAcquistoArticoliMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);
            Set<FatturaAccompagnatoriaAcquistoArticolo> fatturaAccompagnatoriaAcquistoArticoliByIva = entry.getValue();
            for(FatturaAccompagnatoriaAcquistoArticolo fatturaAccompagnatoriaAcquistoArticolo: fatturaAccompagnatoriaAcquistoArticoliByIva){
                BigDecimal imponibile = fatturaAccompagnatoriaAcquistoArticolo.getImponibile() != null ? fatturaAccompagnatoriaAcquistoArticolo.getImponibile() : BigDecimal.ZERO;
                totaleByIva = totaleByIva.add(imponibile);
                totaleQuantita = totaleQuantita + (fatturaAccompagnatoriaAcquistoArticolo.getQuantita() != null ? fatturaAccompagnatoriaAcquistoArticolo.getQuantita() : 0f);
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }
        for (Map.Entry<AliquotaIva, Set<FatturaAccompagnatoriaAcquistoIngrediente>> entry : ivaFatturaAccompagnatoriaAcquistoIngredientiMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);
            Set<FatturaAccompagnatoriaAcquistoIngrediente> fatturaAccompagnatoriaAcquistoIngredientiByIva = entry.getValue();
            for(FatturaAccompagnatoriaAcquistoIngrediente fatturaAccompagnatoriaAcquistoIngrediente: fatturaAccompagnatoriaAcquistoIngredientiByIva){
                BigDecimal imponibile = fatturaAccompagnatoriaAcquistoIngrediente.getImponibile() != null ? fatturaAccompagnatoriaAcquistoIngrediente.getImponibile() : BigDecimal.ZERO;
                totaleByIva = totaleByIva.add(imponibile);
                totaleQuantita = totaleQuantita + (fatturaAccompagnatoriaAcquistoIngrediente.getQuantita() != null ? fatturaAccompagnatoriaAcquistoIngrediente.getQuantita() : 0f);
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }

        BigDecimal totaleIva;
        BigDecimal totaleImponibile;

        totaleIva = fatturaAccompagnatoriaAcquistoTotali.stream().map(FatturaAccompagnatoriaAcquistoTotale::getTotaleIva).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        totaleImponibile = fatturaAccompagnatoriaAcquistoTotali.stream().map(FatturaAccompagnatoriaAcquistoTotale::getTotaleImponibile).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

        fatturaAccompagnatoriaAcquisto.setTotale(Utils.roundPrice(totale));
        fatturaAccompagnatoriaAcquisto.setTotaleAcconto(new BigDecimal(0));
        fatturaAccompagnatoriaAcquisto.setTotaleQuantita(Utils.roundQuantity(new BigDecimal(totaleQuantita)));
        fatturaAccompagnatoriaAcquisto.setTotaleIva(Utils.roundPrice(totaleIva));
        fatturaAccompagnatoriaAcquisto.setTotaleImponibile(Utils.roundPrice(totaleImponibile));
    }
}