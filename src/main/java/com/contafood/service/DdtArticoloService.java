package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.AliquotaIva;
import com.contafood.model.Articolo;
import com.contafood.model.DdtArticolo;
import com.contafood.repository.DdtArticoloRepository;
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
public class DdtArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtArticoloService.class);

    private final DdtArticoloRepository ddtArticoloRepository;

    private final ArticoloService articoloService;

    @Autowired
    public DdtArticoloService(final DdtArticoloRepository ddtArticoloRepository, final ArticoloService articoloService){
        this.ddtArticoloRepository = ddtArticoloRepository;
        this.articoloService = articoloService;
    }

    public Set<DdtArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'ddt articoli'");
        Set<DdtArticolo> ddtArticoli = ddtArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'ddt articoli'", ddtArticoli.size());
        return ddtArticoli;
    }

    public DdtArticolo create(DdtArticolo ddtArticolo){
        LOGGER.info("Creating 'ddt articolo'");
        ddtArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ddtArticolo.setImponibile(computeImponibile(ddtArticolo));
        ddtArticolo.setCosto(computeCosto(ddtArticolo));
        ddtArticolo.setTotale(computeTotale(ddtArticolo));

        DdtArticolo createdDdtArticolo = ddtArticoloRepository.save(ddtArticolo);
        LOGGER.info("Created 'ddt articolo' '{}'", createdDdtArticolo);
        return createdDdtArticolo;
    }

    public DdtArticolo update(DdtArticolo ddtArticolo){
        LOGGER.info("Updating 'ddt articolo'");
        DdtArticolo ddtArticoloCurrent = ddtArticoloRepository.findById(ddtArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        ddtArticolo.setDataInserimento(ddtArticoloCurrent.getDataInserimento());
        ddtArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ddtArticolo.setImponibile(computeImponibile(ddtArticolo));
        ddtArticolo.setCosto(computeCosto(ddtArticolo));
        ddtArticolo.setTotale(computeTotale(ddtArticolo));

        DdtArticolo updatedDdtArticolo = ddtArticoloRepository.save(ddtArticolo);
        LOGGER.info("Updated 'ddt articolo' '{}'", updatedDdtArticolo);
        return updatedDdtArticolo;
    }

    public void deleteByDdtId(Long ddtId){
        LOGGER.info("Deleting 'ddt articolo' by 'ddt' '{}'", ddtId);
        ddtArticoloRepository.deleteByDdtId(ddtId);
        LOGGER.info("Deleted 'ddt articolo' by 'ddt' '{}'", ddtId);
    }

    public Articolo getArticolo(DdtArticolo ddtArticolo){
        Long articoloId = ddtArticolo.getId().getArticoloId();
        return articoloService.getOne(articoloId);
    }

    private BigDecimal computeImponibile(DdtArticolo ddtArticolo){
        BigDecimal imponibile = new BigDecimal(0);

        // imponibile = (quantita*prezzo)-sconto
        Float quantita = ddtArticolo.getQuantita();
        if(quantita == null){
            quantita = 0F;
        }
        BigDecimal prezzo = ddtArticolo.getPrezzo();
        if(prezzo == null){
            prezzo = new BigDecimal(0);
        }
        BigDecimal sconto = ddtArticolo.getSconto();
        if(sconto == null){
            sconto = new BigDecimal(0);
        }
        BigDecimal quantitaPerPrezzo = prezzo.multiply(BigDecimal.valueOf(quantita));
        BigDecimal scontoValue = sconto.divide(BigDecimal.valueOf(100)).multiply(quantitaPerPrezzo);

        imponibile = quantitaPerPrezzo.subtract(scontoValue).setScale(2, RoundingMode.CEILING);
        return imponibile;
    }

    private BigDecimal computeCosto(DdtArticolo ddtArticolo){
        BigDecimal costo = new BigDecimal(0);

        // costo = (quantita*prezzo_acquisto)
        Float quantita = ddtArticolo.getQuantita();
        if(quantita == null){
            quantita = 0F;
        }
        BigDecimal prezzoAcquisto = new BigDecimal(0);
        Long articoloId = ddtArticolo.getId().getArticoloId();
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

    private BigDecimal computeTotale(DdtArticolo ddtArticolo){
        BigDecimal totale = new BigDecimal(0);

        // imponibile = (quantita*prezzo)-sconto
        Float quantita = ddtArticolo.getQuantita();
        if(quantita == null){
            quantita = 0F;
        }
        BigDecimal prezzo = ddtArticolo.getPrezzo();
        if(prezzo == null){
            prezzo = new BigDecimal(0);
        }
        BigDecimal sconto = ddtArticolo.getSconto();
        if(sconto == null){
            sconto = new BigDecimal(0);
        }
        BigDecimal quantitaPerPrezzo = prezzo.multiply(BigDecimal.valueOf(quantita));
        BigDecimal scontoValue = sconto.divide(BigDecimal.valueOf(100)).multiply(quantitaPerPrezzo);

        BigDecimal imponibile = quantitaPerPrezzo.subtract(scontoValue).setScale(2, RoundingMode.CEILING);

        BigDecimal aliquotaIvaValore = new BigDecimal(0);
        Long articoloId = ddtArticolo.getId().getArticoloId();
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
