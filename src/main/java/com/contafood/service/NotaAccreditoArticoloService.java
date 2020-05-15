package com.contafood.service;

import com.contafood.model.Articolo;
import com.contafood.model.NotaAccreditoArticolo;
import com.contafood.repository.NotaAccreditoArticoloRepository;
import com.contafood.util.AccountingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class NotaAccreditoArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(NotaAccreditoArticoloService.class);

    private final NotaAccreditoArticoloRepository notaAccreditoArticoloRepository;

    private final ArticoloService articoloService;

    @Autowired
    public NotaAccreditoArticoloService(final NotaAccreditoArticoloRepository notaAccreditoArticoloRepository, final ArticoloService articoloService){
        this.notaAccreditoArticoloRepository = notaAccreditoArticoloRepository;
        this.articoloService = articoloService;
    }

    public Set<NotaAccreditoArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'nota accredito articoli'");
        Set<NotaAccreditoArticolo> notaAccreditoArticoli = notaAccreditoArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'nota accredito articoli'", notaAccreditoArticoli.size());
        return notaAccreditoArticoli;
    }

    public NotaAccreditoArticolo create(NotaAccreditoArticolo notaAccreditoArticolo){
        LOGGER.info("Creating 'nota accredito articolo'");
        notaAccreditoArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        notaAccreditoArticolo.setImponibile(computeImponibile(notaAccreditoArticolo));
        notaAccreditoArticolo.setCosto(computeCosto(notaAccreditoArticolo));
        notaAccreditoArticolo.setTotale(computeTotale(notaAccreditoArticolo));

        NotaAccreditoArticolo createdNotaAccreditoArticolo = notaAccreditoArticoloRepository.save(notaAccreditoArticolo);
        LOGGER.info("Created 'nota accredito articolo' '{}'", createdNotaAccreditoArticolo);
        return createdNotaAccreditoArticolo;
    }

    public void deleteByNotaAccreditoId(Long notaAccreditoId){
        LOGGER.info("Deleting 'nota accredito articoli' by 'nota accredito' '{}'", notaAccreditoId);
        notaAccreditoArticoloRepository.deleteByNotaAccreditoId(notaAccreditoId);
        LOGGER.info("Deleted 'nota accredito articoli' by 'nota accredito' '{}'", notaAccreditoId);
    }

    public Articolo getArticolo(NotaAccreditoArticolo notaAccreditoArticolo){
        Long articoloId = notaAccreditoArticolo.getId().getArticoloId();
        return articoloService.getOne(articoloId);
    }

    private BigDecimal computeImponibile(NotaAccreditoArticolo notaAccreditoArticolo){

        return AccountingUtils.computeImponibile(notaAccreditoArticolo.getQuantita(), notaAccreditoArticolo.getPrezzo(), notaAccreditoArticolo.getSconto());
    }

    private BigDecimal computeCosto(NotaAccreditoArticolo notaAccreditoArticolo){

        return AccountingUtils.computeCosto(notaAccreditoArticolo.getQuantita(), notaAccreditoArticolo.getId().getArticoloId(), articoloService);
    }

    private BigDecimal computeTotale(NotaAccreditoArticolo notaAccreditoArticolo){

        return AccountingUtils.computeTotale(notaAccreditoArticolo.getQuantita(), notaAccreditoArticolo.getPrezzo(), notaAccreditoArticolo.getSconto(), notaAccreditoArticolo.getId().getArticoloId(), articoloService);
    }

}
