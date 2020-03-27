package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.AliquotaIva;
import com.contafood.model.Articolo;
import com.contafood.model.FatturaAccompagnatoriaArticolo;
import com.contafood.repository.FatturaAccompagnatoriaArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class FatturaAccompagnatoriaArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(FatturaAccompagnatoriaArticoloService.class);

    private final FatturaAccompagnatoriaArticoloRepository fatturaAccompagnatoriaArticoloRepository;

    private final ArticoloService articoloService;

    @Autowired
    public FatturaAccompagnatoriaArticoloService(final FatturaAccompagnatoriaArticoloRepository fatturaAccompagnatoriaArticoloRepository, final ArticoloService articoloService){
        this.fatturaAccompagnatoriaArticoloRepository = fatturaAccompagnatoriaArticoloRepository;
        this.articoloService = articoloService;
    }

    public Set<FatturaAccompagnatoriaArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'fattura accompagnatoria articoli'");
        Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = fatturaAccompagnatoriaArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'fattura accompagnatoria articoli'", fatturaAccompagnatoriaArticoli.size());
        return fatturaAccompagnatoriaArticoli;
    }

    public FatturaAccompagnatoriaArticolo create(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        LOGGER.info("Creating 'fattura accompagnatoria articolo'");
        fatturaAccompagnatoriaArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        fatturaAccompagnatoriaArticolo.setImponibile(computeImponibile(fatturaAccompagnatoriaArticolo));
        fatturaAccompagnatoriaArticolo.setCosto(computeCosto(fatturaAccompagnatoriaArticolo));
        fatturaAccompagnatoriaArticolo.setTotale(computeTotale(fatturaAccompagnatoriaArticolo));

        FatturaAccompagnatoriaArticolo createdFatturaAccompagnatoriaArticolo = fatturaAccompagnatoriaArticoloRepository.save(fatturaAccompagnatoriaArticolo);
        LOGGER.info("Created 'fattura accompagnatoria articolo' '{}'", createdFatturaAccompagnatoriaArticolo);
        return createdFatturaAccompagnatoriaArticolo;
    }

    public FatturaAccompagnatoriaArticolo update(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        LOGGER.info("Updating 'fattura accompagnatoria articolo'");
        FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticoloCurrent = fatturaAccompagnatoriaArticoloRepository.findById(fatturaAccompagnatoriaArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        fatturaAccompagnatoriaArticolo.setDataInserimento(fatturaAccompagnatoriaArticoloCurrent.getDataInserimento());
        fatturaAccompagnatoriaArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        fatturaAccompagnatoriaArticolo.setImponibile(computeImponibile(fatturaAccompagnatoriaArticolo));
        fatturaAccompagnatoriaArticolo.setCosto(computeCosto(fatturaAccompagnatoriaArticolo));
        fatturaAccompagnatoriaArticolo.setTotale(computeTotale(fatturaAccompagnatoriaArticolo));

        FatturaAccompagnatoriaArticolo updatedFatturaAccompagnatoriaArticolo = fatturaAccompagnatoriaArticoloRepository.save(fatturaAccompagnatoriaArticolo);
        LOGGER.info("Updated 'fattura accompagnatoria articolo' '{}'", updatedFatturaAccompagnatoriaArticolo);
        return updatedFatturaAccompagnatoriaArticolo;
    }

    public void deleteByFatturaAccompagnatoriaId(Long fatturaAccompagnatoriaId){
        LOGGER.info("Deleting 'fattura articolo' by 'fattura accompagnatoria' '{}'", fatturaAccompagnatoriaId);
        fatturaAccompagnatoriaArticoloRepository.deleteByFatturaAccompagnatoriaId(fatturaAccompagnatoriaId);
        LOGGER.info("Deleted 'ddt articolo' by 'fattura accompagnatoria' '{}'", fatturaAccompagnatoriaId);
    }

    public Articolo getArticolo(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        Long articoloId = fatturaAccompagnatoriaArticolo.getId().getArticoloId();
        return articoloService.getOne(articoloId);
    }

    private BigDecimal computeImponibile(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        BigDecimal imponibile = new BigDecimal(0);

        // imponibile = (quantita*prezzo)-sconto
        Float quantita = fatturaAccompagnatoriaArticolo.getQuantita();
        if(quantita == null){
            quantita = 0F;
        }
        BigDecimal prezzo = fatturaAccompagnatoriaArticolo.getPrezzo();
        if(prezzo == null){
            prezzo = new BigDecimal(0);
        }
        BigDecimal sconto = fatturaAccompagnatoriaArticolo.getSconto();
        if(sconto == null){
            sconto = new BigDecimal(0);
        }
        BigDecimal quantitaPerPrezzo = prezzo.multiply(BigDecimal.valueOf(quantita));
        BigDecimal scontoValue = sconto.divide(BigDecimal.valueOf(100)).multiply(quantitaPerPrezzo);

        imponibile = quantitaPerPrezzo.subtract(scontoValue).setScale(2, RoundingMode.CEILING);
        return imponibile;
    }

    private BigDecimal computeCosto(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        BigDecimal costo = new BigDecimal(0);

        // costo = (quantita*prezzo_acquisto)
        Float quantita = fatturaAccompagnatoriaArticolo.getQuantita();
        if(quantita == null){
            quantita = 0F;
        }
        BigDecimal prezzoAcquisto = new BigDecimal(0);
        Long articoloId = fatturaAccompagnatoriaArticolo.getId().getArticoloId();
        if(articoloId != null){
            Articolo articolo = articoloService.getOne(articoloId);
            LOGGER.info("Compute costo for 'articolo' {}", articolo);
            if(articolo != null){
                prezzoAcquisto = articolo.getPrezzoAcquisto();
            }
        }
        LOGGER.info("Prezzo acquisto '{}'", prezzoAcquisto);
        costo = (prezzoAcquisto.multiply(BigDecimal.valueOf(quantita))).setScale(2, RoundingMode.CEILING);
        return costo;
    }

    private BigDecimal computeTotale(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        BigDecimal totale = new BigDecimal(0);

        // imponibile = (quantita*prezzo)-sconto
        Float quantita = fatturaAccompagnatoriaArticolo.getQuantita();
        if(quantita == null){
            quantita = 0F;
        }
        BigDecimal prezzo = fatturaAccompagnatoriaArticolo.getPrezzo();
        if(prezzo == null){
            prezzo = new BigDecimal(0);
        }
        BigDecimal sconto = fatturaAccompagnatoriaArticolo.getSconto();
        if(sconto == null){
            sconto = new BigDecimal(0);
        }
        BigDecimal quantitaPerPrezzo = prezzo.multiply(BigDecimal.valueOf(quantita));
        BigDecimal scontoValue = sconto.divide(BigDecimal.valueOf(100)).multiply(quantitaPerPrezzo);

        BigDecimal imponibile = quantitaPerPrezzo.subtract(scontoValue).setScale(2, RoundingMode.CEILING);

        BigDecimal aliquotaIvaValore = new BigDecimal(0);
        Long articoloId = fatturaAccompagnatoriaArticolo.getId().getArticoloId();
        if(articoloId != null){
            Articolo articolo = articoloService.getOne(articoloId);
            LOGGER.info("Compute costo for 'articolo' {}", articolo);
            if(articolo != null){
                AliquotaIva aliquotaIVa = articolo.getAliquotaIva();
                if(aliquotaIVa != null){
                    aliquotaIvaValore = articolo.getAliquotaIva().getValore();
                }
            }
        }

        BigDecimal ivaValue = aliquotaIvaValore.divide(BigDecimal.valueOf(100)).multiply(imponibile);
        totale = imponibile.add(ivaValue).setScale(2, RoundingMode.CEILING);

        return totale;
    }

}
