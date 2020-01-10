package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.OrdineCliente;
import com.contafood.model.OrdineClienteArticolo;
import com.contafood.model.StatoOrdine;
import com.contafood.repository.OrdineClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrdineClienteService {

    private static Logger LOGGER = LoggerFactory.getLogger(OrdineClienteService.class);

    private final OrdineClienteRepository ordineClienteRepository;
    private final OrdineClienteArticoloService ordineClienteArticoloService;
    private final StatoOrdineService statoOrdineService;

    @Autowired
    public OrdineClienteService(final OrdineClienteRepository ordineClienteRepository, final OrdineClienteArticoloService ordineClienteArticoloService, final StatoOrdineService statoOrdineService){
        this.ordineClienteRepository = ordineClienteRepository;
        this.ordineClienteArticoloService = ordineClienteArticoloService;
        this.statoOrdineService = statoOrdineService;
    }

    public Set<OrdineCliente> getAll(){
        LOGGER.info("Retrieving the list of 'ordini clienti'");
        Set<OrdineCliente> ordiniClienti = ordineClienteRepository.findAllByOrderByAnnoContabileDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'ordini clienti'", ordiniClienti.size());
        return ordiniClienti;
    }

    public Set<OrdineCliente> getAllFilteredBy(Long idAutista, Date dataConsegna){
        LOGGER.info("Retrieving the list of 'ordini clienti' filtered by 'idAutista' {} and 'dataConsegna' {}", idAutista, dataConsegna);
        Set<OrdineCliente> ordiniClienti = ordineClienteRepository.findByAutistaIdAndDataConsegna(idAutista, dataConsegna);
        LOGGER.info("Retrieved {} 'ordini clienti'", ordiniClienti.size());
        return ordiniClienti;
    }

    public OrdineCliente getOne(Long ordineClienteId){
        LOGGER.info("Retrieving 'ordineCliente' '{}'", ordineClienteId);
        OrdineCliente ordineCliente = ordineClienteRepository.findById(ordineClienteId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'ordineCliente' '{}'", ordineCliente);
        return ordineCliente;
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
    public void delete(Long ordineClienteId){
        LOGGER.info("Deleting 'ordineCliente' '{}'", ordineClienteId);
        ordineClienteArticoloService.deleteByOrdineClienteId(ordineClienteId);
        ordineClienteRepository.deleteById(ordineClienteId);
        LOGGER.info("Deleted 'ordineCliente' '{}'", ordineClienteId);
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
