package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.DdtArticoloOrdineClienteRepository;
import com.contafood.repository.OrdineClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OrdineClienteService {

    private static Logger LOGGER = LoggerFactory.getLogger(OrdineClienteService.class);

    private final OrdineClienteRepository ordineClienteRepository;
    private final OrdineClienteArticoloService ordineClienteArticoloService;
    private final StatoOrdineService statoOrdineService;
    private final DdtArticoloOrdineClienteRepository ddtArticoloOrdineClienteRepository;

    @Autowired
    public OrdineClienteService(final OrdineClienteRepository ordineClienteRepository, final OrdineClienteArticoloService ordineClienteArticoloService,
                                final StatoOrdineService statoOrdineService, final DdtArticoloOrdineClienteRepository ddtArticoloOrdineClienteRepository){
        this.ordineClienteRepository = ordineClienteRepository;
        this.ordineClienteArticoloService = ordineClienteArticoloService;
        this.statoOrdineService = statoOrdineService;
        this.ddtArticoloOrdineClienteRepository = ddtArticoloOrdineClienteRepository;
    }

    public Set<OrdineCliente> getAll(){
        LOGGER.info("Retrieving the list of 'ordini clienti'");
        Set<OrdineCliente> ordiniClienti = ordineClienteRepository.findAllByOrderByAnnoContabileDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'ordini clienti'", ordiniClienti.size());
        return ordiniClienti;
    }

    public OrdineCliente getOne(Long ordineClienteId){
        LOGGER.info("Retrieving 'ordineCliente' '{}'", ordineClienteId);
        OrdineCliente ordineCliente = ordineClienteRepository.findById(ordineClienteId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'ordineCliente' '{}'", ordineCliente);
        return ordineCliente;
    }

    public Set<OrdineCliente> getByIdClienteAndIdPuntoConsegnaAndDataConsegnaLessOrEqualAndIdStatoNot(Long idCliente, Long idPuntoConsegna, Date dataConsegna, Long idStato){
        LOGGER.info("Retrieving the list of 'ordini clienti' with idCliente '{}', idPuntoConsegna '{}', dataConsegna <= '{}', idStato '{}'", idCliente, idPuntoConsegna, dataConsegna, idStato);
        Set<OrdineCliente> ordiniClienti = ordineClienteRepository.findByClienteIdAndPuntoConsegnaId(idCliente, idPuntoConsegna);

        Predicate<OrdineCliente> isOrdineClienteDataConsegnaLessOrEquals = ordineCliente -> {
            if(dataConsegna != null){
                return ordineCliente.getDataConsegna().compareTo(dataConsegna)<=0;
            }
            return true;
        };

        Predicate<OrdineCliente> isOrdineClienteStatoNotEquals = ordineCliente -> {
            if(idStato != null){
                StatoOrdine statoOrdine = ordineCliente.getStatoOrdine();
                if(statoOrdine != null){
                    return !statoOrdine.getId().equals(idStato);
                }
                return false;
            }
            return true;
        };

        ordiniClienti = ordiniClienti.stream().filter(isOrdineClienteDataConsegnaLessOrEquals.and(isOrdineClienteStatoNotEquals)).collect(Collectors.toSet());

        LOGGER.info("Retrieved {} 'ordini clienti'", ordiniClienti.size());
        return ordiniClienti;
    }

    public Set<OrdineCliente> getOrdiniClientiEvasiAndExpired(){
        LOGGER.info("Retrieving the list of 'ordini clienti' with stato 'EVASO' and expired (dataConsegna+2 >= now)");
        Set<OrdineCliente> ordiniClienti = ordineClienteRepository.findByStatoOrdineId(statoOrdineService.getEvaso().getId());
        ordiniClienti = ordiniClienti.stream().filter(oc -> oc.getDataConsegna().compareTo(Date.valueOf(LocalDate.now().minusDays(2)))<= 0).collect(Collectors.toSet());
        LOGGER.info("Retrieved {} 'ordini clienti'", ordiniClienti.size());
        return ordiniClienti;
    }

    @Transactional
    public OrdineCliente create(OrdineCliente ordineCliente){
        LOGGER.info("Creating 'ordineCliente'");
        ordineCliente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Integer annoContabile = ZonedDateTime.now().getYear();
        ordineCliente.setAnnoContabile(annoContabile);
        ordineCliente.setProgressivo(computeProgressivo(annoContabile));

        StatoOrdine statoOrdineDaEvadere = statoOrdineService.getDaEvadere();
        ordineCliente.setStatoOrdine(statoOrdineDaEvadere);

        OrdineCliente createdOrdineCliente = ordineClienteRepository.save(ordineCliente);

        createdOrdineCliente.getOrdineClienteArticoli().stream().forEach(oca -> {
            oca.getId().setOrdineClienteId(createdOrdineCliente.getId());
            ordineClienteArticoloService.create(oca);
        });

        ordineClienteRepository.save(createdOrdineCliente);
        LOGGER.info("Created 'ordineCliente' '{}'", createdOrdineCliente);
        return createdOrdineCliente;
    }

    @Transactional
    public OrdineCliente update(OrdineCliente ordineCliente){
        LOGGER.info("Updating 'ordineCliente'");
        Set<OrdineClienteArticolo> ordineClienteArticoli = ordineCliente.getOrdineClienteArticoli();
        ordineCliente.setOrdineClienteArticoli(new HashSet<>());
        ordineClienteArticoloService.deleteByOrdineClienteId(ordineCliente.getId());

        OrdineCliente ordineClienteCurrent = ordineClienteRepository.findById(ordineCliente.getId()).orElseThrow(ResourceNotFoundException::new);
        ordineCliente.setDataInserimento(ordineClienteCurrent.getDataInserimento());
        ordineCliente.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        OrdineCliente updatedOrdineCliente = ordineClienteRepository.save(ordineCliente);
        ordineClienteArticoli.stream().forEach(oca -> {
            oca.getId().setOrdineClienteId(updatedOrdineCliente.getId());
            ordineClienteArticoloService.create(oca);
        });
        LOGGER.info("Updated 'ordineCliente' '{}'", updatedOrdineCliente);
        return updatedOrdineCliente;
    }

    @Transactional
    public OrdineCliente patch(Map<String,Object> patchOrdineCliente){
        LOGGER.info("Patching 'ordineCliente'");

        Long id = Long.valueOf((Integer) patchOrdineCliente.get("id"));
        OrdineCliente ordineCliente = ordineClienteRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        patchOrdineCliente.forEach((key, value) -> {
            if(key.equals("id")){
                ordineCliente.setId(Long.valueOf((Integer)value));
            } else if(key.equals("dataConsegna")){
                ordineCliente.setDataConsegna(Date.valueOf((String)value));
            } else if(key.equals("idAutista")){
                Autista autista = new Autista();
                autista.setId(Long.valueOf((Integer)value));
                ordineCliente.setAutista(autista);
            }
        });
        OrdineCliente patchedOrdineCliente = ordineClienteRepository.save(ordineCliente);

        LOGGER.info("Patched 'ordineCliente' '{}'", patchedOrdineCliente);
        return patchedOrdineCliente;
    }

    @Transactional
    public void delete(Long ordineClienteId){
        LOGGER.info("Deleting 'ordineCliente' '{}'", ordineClienteId);
        ddtArticoloOrdineClienteRepository.deleteByOrdineClienteId(ordineClienteId);
        ordineClienteArticoloService.deleteByOrdineClienteId(ordineClienteId);
        ordineClienteRepository.deleteById(ordineClienteId);
        LOGGER.info("Deleted 'ordineCliente' '{}'", ordineClienteId);
    }

    public OrdineClienteArticolo getOrdineClienteArticolo(OrdineClienteArticoloKey ordineClienteArticoloKey){
        return ordineClienteArticoloService.getOrdineClienteArticolo(ordineClienteArticoloKey);
    }

    public void saveOrdineClienteArticolo(OrdineClienteArticolo ordineClienteArticolo){
        ordineClienteArticoloService.save(ordineClienteArticolo);
    }

    public void computeStatoOrdineCliente(Long idOrdineCliente){
        LOGGER.info("Computing stato of 'OrdineCliente' {}", idOrdineCliente);
        OrdineCliente ordineCliente = getOne(idOrdineCliente);
        Set<OrdineClienteArticolo> ordineClienteArticoli = ordineClienteArticoloService.getOrdineClienteArticoli(idOrdineCliente);
        if(ordineClienteArticoli != null && !ordineClienteArticoli.isEmpty()){
            StatoOrdine statoOrdine = statoOrdineService.getDaEvadere();
            Integer sumNumeroPezziOrdinati = ordineClienteArticoli.stream().map(oca -> oca.getNumeroPezziOrdinati()).reduce(0, Integer::sum);
            if(sumNumeroPezziOrdinati > 0){
                Integer sumNumeroPezziDaEvadere = ordineClienteArticoli.stream().map(oca -> oca.getNumeroPezziDaEvadere()).reduce(0, Integer::sum);
                if(sumNumeroPezziDaEvadere == 0){
                    statoOrdine = statoOrdineService.getEvaso();
                } else {
                    statoOrdine = statoOrdineService.getParzialmenteEvaso();
                }
            }
            LOGGER.info("Setting stato {} to 'OrdineCliente' {}", statoOrdine.getCodice(), idOrdineCliente);
            ordineCliente.setStatoOrdine(statoOrdine);
            ordineClienteRepository.save(ordineCliente);
            LOGGER.info("Set stato {} to 'OrdineCliente' {}", statoOrdine.getCodice(), idOrdineCliente);
        }

        LOGGER.info("Computed stato of 'OrdineCliente' {}", idOrdineCliente);
    }

    private Integer computeProgressivo(Integer annoContabile){
        List<OrdineCliente> ordiniClienti = ordineClienteRepository.findByAnnoContabileOrderByProgressivoDesc(annoContabile);
        if(ordiniClienti != null && !ordiniClienti.isEmpty()){
            Optional<OrdineCliente> lastOrdineCliente = ordiniClienti.stream().findFirst();
            if(lastOrdineCliente.isPresent()){
                return lastOrdineCliente.get().getProgressivo() + 1;
            }
        }
        return 1;
    }

}
