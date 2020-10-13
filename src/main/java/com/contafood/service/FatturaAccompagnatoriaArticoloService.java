package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.FatturaAccompagnatoriaArticoloOrdineClienteRepository;
import com.contafood.repository.FatturaAccompagnatoriaArticoloRepository;
import com.contafood.util.AccountingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FatturaAccompagnatoriaArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(FatturaAccompagnatoriaArticoloService.class);

    private static final String CONTEXT_CREATE_FATTURA_ACCOMPAGNATORIA = "create_fattura_accompagnatoria";
    private static final String CONTEXT_DELETE_FATTURA_ACCOMPAGNATORIA = "create_fattura_accompagnatoria";

    private final FatturaAccompagnatoriaArticoloRepository fatturaAccompagnatoriaArticoloRepository;
    private final ArticoloService articoloService;
    private final FatturaAccompagnatoriaArticoloOrdineClienteRepository fatturaAccompagnatoriaArticoloOrdineClienteRepository;
    private final OrdineClienteService ordineClienteService;

    @Autowired
    public FatturaAccompagnatoriaArticoloService(final FatturaAccompagnatoriaArticoloRepository fatturaAccompagnatoriaArticoloRepository,
                                                 final ArticoloService articoloService,
                                                 final FatturaAccompagnatoriaArticoloOrdineClienteRepository fatturaAccompagnatoriaArticoloOrdineClienteRepository,
                                                 final OrdineClienteService ordineClienteService){
        this.fatturaAccompagnatoriaArticoloRepository = fatturaAccompagnatoriaArticoloRepository;
        this.articoloService = articoloService;
        this.fatturaAccompagnatoriaArticoloOrdineClienteRepository = fatturaAccompagnatoriaArticoloOrdineClienteRepository;
        this.ordineClienteService = ordineClienteService;
    }

    public Set<FatturaAccompagnatoriaArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'fattura accompagnatoria articoli'");
        Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = fatturaAccompagnatoriaArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'fattura accompagnatoria articoli'", fatturaAccompagnatoriaArticoli.size());
        return fatturaAccompagnatoriaArticoli;
    }

    public Set<FatturaAccompagnatoriaArticolo> findByFatturaAccompagnatoriaId(Long idFatturaAccompagnatoria){
        LOGGER.info("Retrieving the list of 'fattura accompagnatoria articoli' of 'fattura accompagnatoria' {}", idFatturaAccompagnatoria);
        Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = fatturaAccompagnatoriaArticoloRepository.findByFatturaAccompagnatoriaId(idFatturaAccompagnatoria);
        LOGGER.info("Retrieved {} 'fattura accompagnatoria articoli'", fatturaAccompagnatoriaArticoli.size());
        return fatturaAccompagnatoriaArticoli;
    }

    public FatturaAccompagnatoriaArticolo create(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        LOGGER.info("Creating 'fattura accompagnatoria articolo'");
        fatturaAccompagnatoriaArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        fatturaAccompagnatoriaArticolo.setImponibile(computeImponibile(fatturaAccompagnatoriaArticolo));
        fatturaAccompagnatoriaArticolo.setCosto(computeCosto(fatturaAccompagnatoriaArticolo));
        fatturaAccompagnatoriaArticolo.setTotale(computeTotale(fatturaAccompagnatoriaArticolo));

        //List<Long> idOrdiniClienti = fatturaAccompagnatoriaArticolo.getIdOrdiniClienti();

        FatturaAccompagnatoriaArticolo createdFatturaAccompagnatoriaArticolo = fatturaAccompagnatoriaArticoloRepository.save(fatturaAccompagnatoriaArticolo);

        /*if(idOrdiniClienti != null && !idOrdiniClienti.isEmpty()){
            LOGGER.info("Creating 'fattura accompagnatoria articoli ordini clienti'");
            for (Long idOrdineCliente: idOrdiniClienti) {
                FatturaAccompagnatoriaArticoloOrdineClienteKey fatturaAccompagnatoriaArticoloOrdineClienteKey = new FatturaAccompagnatoriaArticoloOrdineClienteKey();
                fatturaAccompagnatoriaArticoloOrdineClienteKey.setFatturaAccompagnatoriaId(createdFatturaAccompagnatoriaArticolo.getId().getFatturaAccompagnatoriaId());
                fatturaAccompagnatoriaArticoloOrdineClienteKey.setArticoloId(createdFatturaAccompagnatoriaArticolo.getId().getArticoloId());
                fatturaAccompagnatoriaArticoloOrdineClienteKey.setUuid(createdFatturaAccompagnatoriaArticolo.getId().getUuid());
                fatturaAccompagnatoriaArticoloOrdineClienteKey.setOrdineClienteId(idOrdineCliente);

                FatturaAccompagnatoriaArticoloOrdineCliente fatturaAccompagnatoriaArticoloOrdineCliente = new FatturaAccompagnatoriaArticoloOrdineCliente();
                fatturaAccompagnatoriaArticoloOrdineCliente.setId(fatturaAccompagnatoriaArticoloOrdineClienteKey);
                fatturaAccompagnatoriaArticoloOrdineCliente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

                fatturaAccompagnatoriaArticoloOrdineClienteRepository.save(fatturaAccompagnatoriaArticoloOrdineCliente);
            }
            LOGGER.info("Created 'ddt articoli ordini clienti'");
        }*/

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
        LOGGER.info("Deleting 'fattura accompagnatoria articolo' by 'fattura accompagnatoria' '{}'", fatturaAccompagnatoriaId);
        fatturaAccompagnatoriaArticoloRepository.deleteByFatturaAccompagnatoriaId(fatturaAccompagnatoriaId);
        LOGGER.info("Deleted 'fattura accompagnatoria articolo' by 'fattura accompagnatoria' '{}'", fatturaAccompagnatoriaId);
    }

    public Articolo getArticolo(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        Long articoloId = fatturaAccompagnatoriaArticolo.getId().getArticoloId();
        return articoloService.getOne(articoloId);
    }

    public Set<FatturaAccompagnatoriaArticolo> getByArticoloIdAndLottoAndScadenza(Long idArticolo, String lotto, Date scadenza){
        LOGGER.info("Retrieving 'fattura accompagnatoria articoli' by 'idArticolo' '{}', 'lotto' '{}' and 'scadenza' '{}'", idArticolo, lotto, scadenza);
        Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = fatturaAccompagnatoriaArticoloRepository.findByArticoloIdAndLotto(idArticolo, lotto);
        if(fatturaAccompagnatoriaArticoli != null && !fatturaAccompagnatoriaArticoli.isEmpty()){
            if(scadenza != null){
                fatturaAccompagnatoriaArticoli = fatturaAccompagnatoriaArticoli.stream()
                        .filter(faa -> (faa.getScadenza() != null && faa.getScadenza().toLocalDate().compareTo(scadenza.toLocalDate())==0)).collect(Collectors.toSet());
            }
        }
        LOGGER.info("Retrieved '{}' 'fattura accompagnatoria articoli'", fatturaAccompagnatoriaArticoli.size());
        return fatturaAccompagnatoriaArticoli;
    }

    /*
    @Transactional
    public void updateOrdineClienteFromCreateFatturaAccompagnatoria(Long idFatturaAccompagnatoria){
        updateOrdineCliente(idFatturaAccompagnatoria, CONTEXT_CREATE_FATTURA_ACCOMPAGNATORIA);
    }

    @Transactional
    public void updateOrdineClienteFromDeleteFatturaAccompagnatoria(Long idFatturaAccompagnatoria){
        updateOrdineCliente(idFatturaAccompagnatoria, CONTEXT_DELETE_FATTURA_ACCOMPAGNATORIA);
    }

    private void updateOrdineCliente(Long idFatturaAccompagnatoria, String context){
        LOGGER.info("Updating 'numeroPezziDaEvadere' of 'OrdiniClientiArticoli' related to fattura accompagnatoria '{}'", idFatturaAccompagnatoria);

        Set<Long> idOrdiniClienti = new HashSet<>();
        Map<FatturaAccompagnatoriaArticoloKey, Integer> fatturaAccompagnatoriaArticoliPezziRemaining = new HashMap<>();

        // retrieve the 'FatturaAccompagnatoriaArticoloOrdineCliente' of the fattura accompagnatoria
        List<FatturaAccompagnatoriaArticoloOrdineCliente> fatturaAccompagnatoriaArticoliOrdiniClienti = fatturaAccompagnatoriaArticoloOrdineClienteRepository.findAllByIdFatturaAccompagnatoriaId(idFatturaAccompagnatoria);
        if(fatturaAccompagnatoriaArticoliOrdiniClienti != null && !fatturaAccompagnatoriaArticoliOrdiniClienti.isEmpty()){
            for(FatturaAccompagnatoriaArticoloOrdineCliente fatturaAccompagnatoriaArticoloOrdineCliente : fatturaAccompagnatoriaArticoliOrdiniClienti){
                LOGGER.info("'FatturaAccompagnatoriaArticoloOrdineCliente' {}", fatturaAccompagnatoriaArticoloOrdineCliente);

                // add the id of the 'OrdineCliente' to the set of ordini-clienti to compute stato
                Long idOrdineCliente = fatturaAccompagnatoriaArticoloOrdineCliente.getId().getOrdineClienteId();
                idOrdiniClienti.add(idOrdineCliente);

                // create 'FatturaAccompagnatoriaArticoloKey'
                FatturaAccompagnatoriaArticoloKey fatturaAccompagnatoriaArticoloKey = new FatturaAccompagnatoriaArticoloKey();
                fatturaAccompagnatoriaArticoloKey.setFatturaAccompagnatoriaId(fatturaAccompagnatoriaArticoloOrdineCliente.getId().getFatturaAccompagnatoriaId());
                fatturaAccompagnatoriaArticoloKey.setArticoloId(fatturaAccompagnatoriaArticoloOrdineCliente.getId().getArticoloId());
                fatturaAccompagnatoriaArticoloKey.setUuid(fatturaAccompagnatoriaArticoloOrdineCliente.getId().getUuid());

                // retrieve the 'FatturaAccompagnatoriaArticolo'
                FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo = null;
                Optional<FatturaAccompagnatoriaArticolo> optionalFatturaAccompagnatoriaArticolo = fatturaAccompagnatoriaArticoloRepository.findById(fatturaAccompagnatoriaArticoloKey);
                if(optionalFatturaAccompagnatoriaArticolo.isPresent()){
                    fatturaAccompagnatoriaArticolo = optionalFatturaAccompagnatoriaArticolo.get();
                    LOGGER.info("Retrieved 'fatturaAccompagnatoriaArticolo' {}", fatturaAccompagnatoriaArticolo);
                } else {
                    String message = "Unable to retrieve 'FatturaAccompagnatoria' from key '"+fatturaAccompagnatoriaArticoloKey+"'";
                    LOGGER.error(message);
                    throw new RuntimeException(message);
                }

                // create 'OrdineClienteArticoloKey'
                OrdineClienteArticoloKey ordineClienteArticoloKey = new OrdineClienteArticoloKey();
                ordineClienteArticoloKey.setOrdineClienteId(idOrdineCliente);
                ordineClienteArticoloKey.setArticoloId(fatturaAccompagnatoriaArticoloOrdineCliente.getId().getArticoloId());

                // retrieve the 'OrdineClienteArticolo'
                OrdineClienteArticolo ordineClienteArticolo = null;
                try{
                    ordineClienteArticolo = ordineClienteService.getOrdineClienteArticolo(ordineClienteArticoloKey);
                    LOGGER.info("Retrieved 'ordineClienteArticolo' {}", ordineClienteArticolo);
                } catch(Exception e){
                    LOGGER.error("Unable to retrieve 'OrdineClienteArticolo' from key '{}'", ordineClienteArticoloKey);
                    throw e;
                }

                Integer numeroPezzi = fatturaAccompagnatoriaArticoliPezziRemaining.getOrDefault(fatturaAccompagnatoriaArticoloKey, fatturaAccompagnatoriaArticolo.getNumeroPezzi());
                Integer numeroPezziDaEvadere = ordineClienteArticolo.getNumeroPezziDaEvadere();
                Integer newNumeroPezziDaEvadere = computeOrdineClienteArticoloNewPezziDaEvadere(context, numeroPezzi, numeroPezziDaEvadere, ordineClienteArticolo.getNumeroPezziOrdinati(), fatturaAccompagnatoriaArticoliPezziRemaining, fatturaAccompagnatoriaArticoloKey);

                LOGGER.info("Updating 'OrdineClienteArticolo' with id '{}' setting 'numeroPezziDaEvadere' equals to {}", ordineClienteArticolo.getId(), newNumeroPezziDaEvadere);
                ordineClienteArticolo.setNumeroPezziDaEvadere(newNumeroPezziDaEvadere);
                ordineClienteService.saveOrdineClienteArticolo(ordineClienteArticolo);
                LOGGER.info("Updated 'OrdineClienteArticolo' with id '{}' setting 'numeroPezziDaEvadere' equals to {}", ordineClienteArticolo.getId(), newNumeroPezziDaEvadere);
            }

        } else {
            LOGGER.info("No 'FatturaAccompagnatoriaArticoloOrdineCliente' rows found for fattura accompagnatoria '{}'", idFatturaAccompagnatoria);
        }
        LOGGER.info("Updated 'numeroPezziDaEvadere' of 'OrdiniClientiArticoli' related to fattura accompagnatoria '{}'", idFatturaAccompagnatoria);

        // compute stato for all 'OrdiniClienti'
        if(idOrdiniClienti != null && !idOrdiniClienti.isEmpty()){
            for(Long idOrdineCliente : idOrdiniClienti){
                ordineClienteService.computeStatoOrdineCliente(idOrdineCliente);
            }
        }

    }*/

    private Integer computeOrdineClienteArticoloNewPezziDaEvadere(String context, Integer pezzi, Integer pezziDaEvadere, Integer pezziOrdinati, Map<FatturaAccompagnatoriaArticoloKey, Integer> fatturaAccompagnatoriaArticoliPezziRemaining, FatturaAccompagnatoriaArticoloKey fatturaAccompagnatoriaArticoloKey){
        LOGGER.info("Computing 'newPezziDaEvadere' for context {}, pezzi {}, pezziDaEvadere {}, pezziOrdinati {}", context, pezzi, pezziDaEvadere, pezziOrdinati);

        Integer newNumeroPezziDaEvadere = null;
        if(context.equals(CONTEXT_CREATE_FATTURA_ACCOMPAGNATORIA)){
            newNumeroPezziDaEvadere = pezziDaEvadere - pezzi;
            if(newNumeroPezziDaEvadere < 0){
                LOGGER.info("Context {}: pezzi da evadere {} - pezzi {} less than 0", context, pezziDaEvadere, pezzi);
                newNumeroPezziDaEvadere = 0;
                fatturaAccompagnatoriaArticoliPezziRemaining.putIfAbsent(fatturaAccompagnatoriaArticoloKey, Math.abs(pezziDaEvadere - pezzi));
            }
        } else if(context.equals(CONTEXT_DELETE_FATTURA_ACCOMPAGNATORIA)){
            newNumeroPezziDaEvadere = pezziDaEvadere + pezzi;
            if(newNumeroPezziDaEvadere > pezziOrdinati){
                newNumeroPezziDaEvadere = pezziOrdinati;
                LOGGER.info("Context {}: pezzi da evadere {} + pezzi {} greater than pezzi ordinati {}", context, pezziDaEvadere, pezzi, pezziOrdinati);
                fatturaAccompagnatoriaArticoliPezziRemaining.putIfAbsent(fatturaAccompagnatoriaArticoloKey, Math.abs(pezziOrdinati-pezzi));
            }
        }
        LOGGER.info("Context {}: pezzi: {}, pezzi da evadere {} pezzi ordinati {}, nuovi pezzi da evadere {}", context, pezzi, pezziDaEvadere, pezziOrdinati, newNumeroPezziDaEvadere);
        return newNumeroPezziDaEvadere;
    }


    private BigDecimal computeImponibile(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){

        return AccountingUtils.computeImponibile(fatturaAccompagnatoriaArticolo.getQuantita(), fatturaAccompagnatoriaArticolo.getPrezzo(), fatturaAccompagnatoriaArticolo.getSconto());
    }

    private BigDecimal computeCosto(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){

        return AccountingUtils.computeCosto(fatturaAccompagnatoriaArticolo.getQuantita(), fatturaAccompagnatoriaArticolo.getId().getArticoloId(), articoloService);
    }

    private BigDecimal computeTotale(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        return AccountingUtils.computeTotale(fatturaAccompagnatoriaArticolo.getQuantita(), fatturaAccompagnatoriaArticolo.getPrezzo(), fatturaAccompagnatoriaArticolo.getSconto(), null, fatturaAccompagnatoriaArticolo.getId().getArticoloId(), articoloService);
    }

}
