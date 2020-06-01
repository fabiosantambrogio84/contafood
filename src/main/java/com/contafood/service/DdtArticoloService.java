package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.DdtArticoloOrdineClienteRepository;
import com.contafood.repository.DdtArticoloRepository;
import com.contafood.util.AccountingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class DdtArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtArticoloService.class);

    private static final String CONTEXT_CREATE_DDT = "create_ddt";
    private static final String CONTEXT_DELETE_DDT = "delete_ddt";

    private final DdtArticoloRepository ddtArticoloRepository;
    private final ArticoloService articoloService;
    private final DdtArticoloOrdineClienteRepository ddtArticoloOrdineClienteRepository;
    private final OrdineClienteService ordineClienteService;

    @Autowired
    public DdtArticoloService(final DdtArticoloRepository ddtArticoloRepository,
                              final ArticoloService articoloService,
                              final DdtArticoloOrdineClienteRepository ddtArticoloOrdineClienteRepository,
                              final OrdineClienteService ordineClienteService){
        this.ddtArticoloRepository = ddtArticoloRepository;
        this.articoloService = articoloService;
        this.ddtArticoloOrdineClienteRepository = ddtArticoloOrdineClienteRepository;
        this.ordineClienteService = ordineClienteService;
    }

    public Set<DdtArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'ddt articoli'");
        Set<DdtArticolo> ddtArticoli = ddtArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'ddt articoli'", ddtArticoli.size());
        return ddtArticoli;
    }

    @Transactional
    public DdtArticolo create(DdtArticolo ddtArticolo){
        LOGGER.info("Creating 'ddt articolo'");
        ddtArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ddtArticolo.setImponibile(computeImponibile(ddtArticolo));
        ddtArticolo.setCosto(computeCosto(ddtArticolo));
        ddtArticolo.setTotale(computeTotale(ddtArticolo));

        List<Long> idOrdiniClienti = ddtArticolo.getIdOrdiniClienti();

        DdtArticolo createdDdtArticolo = ddtArticoloRepository.save(ddtArticolo);

        if(idOrdiniClienti != null && !idOrdiniClienti.isEmpty()){
            LOGGER.info("Creating 'ddt articoli ordini clienti'");
            for (Long idOrdineCliente: idOrdiniClienti) {
                DdtArticoloOrdineClienteKey ddtArticoloOrdineClienteKey = new DdtArticoloOrdineClienteKey();
                ddtArticoloOrdineClienteKey.setDdtId(createdDdtArticolo.getId().getDdtId());
                ddtArticoloOrdineClienteKey.setArticoloId(createdDdtArticolo.getId().getArticoloId());
                ddtArticoloOrdineClienteKey.setUuid(createdDdtArticolo.getId().getUuid());
                ddtArticoloOrdineClienteKey.setOrdineClienteId(idOrdineCliente);

                DdtArticoloOrdineCliente ddtArticoloOrdineCliente = new DdtArticoloOrdineCliente();
                ddtArticoloOrdineCliente.setId(ddtArticoloOrdineClienteKey);
                ddtArticoloOrdineCliente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

                ddtArticoloOrdineClienteRepository.save(ddtArticoloOrdineCliente);
            }
            LOGGER.info("Created 'ddt articoli ordini clienti'");
        }

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

    @Transactional
    public void deleteByDdtId(Long ddtId){
        LOGGER.info("Deleting 'ddt articolo' by 'ddt' '{}'", ddtId);
        ddtArticoloRepository.deleteByDdtId(ddtId);
        ddtArticoloOrdineClienteRepository.deleteByIdDdtId(ddtId);
        LOGGER.info("Deleted 'ddt articolo' by 'ddt' '{}'", ddtId);
    }

    public Articolo getArticolo(DdtArticolo ddtArticolo){
        Long articoloId = ddtArticolo.getId().getArticoloId();
        return articoloService.getOne(articoloId);
    }

    @Transactional
    public void updateOrdineClienteFromCreateDdt(Long idDdt){
        updateOrdineCliente(idDdt, CONTEXT_CREATE_DDT);
    }

    @Transactional
    public void updateOrdineClienteFromDeleteDdt(Long idDdt){
        updateOrdineCliente(idDdt, CONTEXT_DELETE_DDT);
    }

    private void updateOrdineCliente(Long idDdt, String context){
        LOGGER.info("Updating 'numeroPezziDaEvadere' of 'OrdiniClientiArticoli' related to ddt '{}'", idDdt);

        Set<Long> idOrdiniClienti = new HashSet<>();
        Map<DdtArticoloKey, Integer> ddtArticoliPezziRemaining = new HashMap<>();

        // retrieve the 'DdtArticoloOrdineCliente' of the ddt
        List<DdtArticoloOrdineCliente> ddtArticoliOrdiniClienti = ddtArticoloOrdineClienteRepository.findAllByIdDdtId(idDdt);
        if(ddtArticoliOrdiniClienti != null && !ddtArticoliOrdiniClienti.isEmpty()){
            for(DdtArticoloOrdineCliente ddtArticoloOrdineCliente : ddtArticoliOrdiniClienti){
                LOGGER.info("'DdtArticoloOrdineCliente' {}", ddtArticoloOrdineCliente);

                // add the id of the 'OrdineCliente' to the set of ordini-clienti to compute stato
                Long idOrdineCliente = ddtArticoloOrdineCliente.getId().getOrdineClienteId();
                idOrdiniClienti.add(idOrdineCliente);

                // create 'DdtArticoloKey'
                DdtArticoloKey ddtArticoloKey = new DdtArticoloKey();
                ddtArticoloKey.setDdtId(ddtArticoloOrdineCliente.getId().getDdtId());
                ddtArticoloKey.setArticoloId(ddtArticoloOrdineCliente.getId().getArticoloId());
                ddtArticoloKey.setUuid(ddtArticoloOrdineCliente.getId().getUuid());

                // retrieve the 'DdtArticolo'
                DdtArticolo ddtArticolo = null;
                Optional<DdtArticolo> optionalDdtArticolo = ddtArticoloRepository.findById(ddtArticoloKey);
                if(optionalDdtArticolo.isPresent()){
                    ddtArticolo = optionalDdtArticolo.get();
                    LOGGER.info("Retrieved 'ddtArticolo' {}", ddtArticolo);
                } else {
                    String message = "Unable to retrieve 'DdtArticolo' from key '"+ddtArticoloKey+"'";
                    LOGGER.error(message);
                    throw new RuntimeException(message);
                }

                // create 'OrdineClienteArticoloKey'
                OrdineClienteArticoloKey ordineClienteArticoloKey = new OrdineClienteArticoloKey();
                ordineClienteArticoloKey.setOrdineClienteId(idOrdineCliente);
                ordineClienteArticoloKey.setArticoloId(ddtArticoloOrdineCliente.getId().getArticoloId());

                // retrieve the 'OrdineClienteArticolo'
                OrdineClienteArticolo ordineClienteArticolo = null;
                try{
                    ordineClienteArticolo = ordineClienteService.getOrdineClienteArticolo(ordineClienteArticoloKey);
                    LOGGER.info("Retrieved 'ordineClienteArticolo' {}", ordineClienteArticolo);
                } catch(Exception e){
                    LOGGER.error("Unable to retrieve 'OrdineClienteArticolo' from key '{}'", ordineClienteArticoloKey);
                    throw e;
                }

                Integer numeroPezzi = ddtArticoliPezziRemaining.getOrDefault(ddtArticoloKey, ddtArticolo.getNumeroPezzi());
                Integer numeroPezziDaEvadere = ordineClienteArticolo.getNumeroPezziDaEvadere();
                Integer newNumeroPezziDaEvadere = computeOrdineClienteArticoloNewPezziDaEvadere(context, numeroPezzi, numeroPezziDaEvadere, ordineClienteArticolo.getNumeroPezziOrdinati(), ddtArticoliPezziRemaining, ddtArticoloKey);

                LOGGER.info("Updating 'OrdineClienteArticolo' with id '{}' setting 'numeroPezziDaEvadere' equals to {}", ordineClienteArticolo.getId(), newNumeroPezziDaEvadere);
                ordineClienteArticolo.setNumeroPezziDaEvadere(newNumeroPezziDaEvadere);
                ordineClienteService.saveOrdineClienteArticolo(ordineClienteArticolo);
                LOGGER.info("Updated 'OrdineClienteArticolo' with id '{}' setting 'numeroPezziDaEvadere' equals to {}", ordineClienteArticolo.getId(), newNumeroPezziDaEvadere);
            }

        } else {
            LOGGER.info("No 'DdtArticoloOrdineCliente' rows found for ddt '{}'", idDdt);
        }
        LOGGER.info("Updated 'numeroPezziDaEvadere' of 'OrdiniClientiArticoli' related to ddt '{}'", idDdt);

        // compute stato for all 'OrdiniClienti'
        if(idOrdiniClienti != null && !idOrdiniClienti.isEmpty()){
            for(Long idOrdineCliente : idOrdiniClienti){
                ordineClienteService.computeStatoOrdineCliente(idOrdineCliente);
            }
        }

    }

    private Integer computeOrdineClienteArticoloNewPezziDaEvadere(String context, Integer pezzi, Integer pezziDaEvadere, Integer pezziOrdinati, Map<DdtArticoloKey, Integer> ddtArticoliPezziRemaining, DdtArticoloKey ddtArticoloKey){
        LOGGER.info("Computing 'newPezziDaEvadere' for context {}, pezzi {}, pezziDaEvadere {}, pezziOrdinati {}", context, pezzi, pezziDaEvadere, pezziOrdinati);

        Integer newNumeroPezziDaEvadere = null;
        if(context.equals(CONTEXT_CREATE_DDT)){
            newNumeroPezziDaEvadere = pezziDaEvadere - pezzi;
            if(newNumeroPezziDaEvadere < 0){
                LOGGER.info("Context {}: pezzi da evadere {} - pezzi {} less than 0", context, pezziDaEvadere, pezzi);
                newNumeroPezziDaEvadere = 0;
                ddtArticoliPezziRemaining.putIfAbsent(ddtArticoloKey, Math.abs(pezziDaEvadere - pezzi));
            }
        } else if(context.equals(CONTEXT_DELETE_DDT)){
            newNumeroPezziDaEvadere = pezziDaEvadere + pezzi;
            if(newNumeroPezziDaEvadere > pezziOrdinati){
                newNumeroPezziDaEvadere = pezziOrdinati;
                LOGGER.info("Context {}: pezzi da evadere {} + pezzi {} greater than pezzi ordinati {}", context, pezziDaEvadere, pezzi, pezziOrdinati);
                ddtArticoliPezziRemaining.putIfAbsent(ddtArticoloKey, Math.abs(pezziOrdinati-pezzi));
            }
        }
        LOGGER.info("Context {}: pezzi: {}, pezzi da evadere {} pezzi ordinati {}, nuovi pezzi da evadere {}", context, pezzi, pezziDaEvadere, pezziOrdinati, newNumeroPezziDaEvadere);
        return newNumeroPezziDaEvadere;
    }

    private BigDecimal computeImponibile(DdtArticolo ddtArticolo){

        return AccountingUtils.computeImponibile(ddtArticolo.getQuantita(), ddtArticolo.getPrezzo(), ddtArticolo.getSconto());
    }

    private BigDecimal computeCosto(DdtArticolo ddtArticolo){

        return AccountingUtils.computeCosto(ddtArticolo.getQuantita(), ddtArticolo.getId().getArticoloId(), articoloService);
    }

    private BigDecimal computeTotale(DdtArticolo ddtArticolo){
        return AccountingUtils.computeTotale(ddtArticolo.getQuantita(), ddtArticolo.getPrezzo(), ddtArticolo.getSconto(), null, ddtArticolo.getId().getArticoloId(), articoloService);
    }

}
