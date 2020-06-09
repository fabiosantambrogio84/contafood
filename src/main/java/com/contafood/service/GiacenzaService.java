package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Articolo;
import com.contafood.model.Giacenza;
import com.contafood.model.Movimentazione;
import com.contafood.repository.GiacenzaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiacenzaService {

    private static Logger LOGGER = LoggerFactory.getLogger(GiacenzaService.class);

    private final GiacenzaRepository giacenzaRepository;
    private final MovimentazioneService movimentazioneService;

    @Autowired
    public GiacenzaService(final GiacenzaRepository giacenzaRepository,
                           final MovimentazioneService movimentazioneService){
        this.giacenzaRepository = giacenzaRepository;
        this.movimentazioneService = movimentazioneService;
    }

    public Set<Giacenza> getAll(){
        LOGGER.info("Retrieving the list of 'giacenze'");
        Set<Giacenza> giacenze = giacenzaRepository.findAll();
        LOGGER.info("Retrieved {} 'giacenze'", giacenze.size());
        return giacenze;
    }

    public Giacenza create(Giacenza giacenza){
        LOGGER.info("Creating 'giacenza'");

        giacenza.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Giacenza createdGiacenza = giacenzaRepository.save(giacenza);

        LOGGER.info("Created 'giacenza' '{}'", createdGiacenza);
        return createdGiacenza;
    }

    public void delete(Long idGiacenza){
        LOGGER.info("Deleting 'giacenza' '{}'", idGiacenza);
        giacenzaRepository.deleteById(idGiacenza);
        LOGGER.info("Deleted 'giacenza' '{}'", idGiacenza);
    }

    @Transactional
    public void deleteByArticoloId(Long idArticolo){
        LOGGER.info("Deleting 'giacenze' of articolo '{}'", idArticolo);
        giacenzaRepository.deleteByArticoloId(idArticolo);
        LOGGER.info("Deleted 'giacenze' of articolo '{}'", idArticolo);
    }

    @Transactional
    public void bulkDelete(List<Long> giacenzeIds){
        LOGGER.info("Bulk deleting all the specified 'giacenze (number of elements to delete: {})'", giacenzeIds.size());
        giacenzaRepository.deleteByIdIn(giacenzeIds);
        LOGGER.info("Bulk deleted all the specified 'giacenze");
    }

    public Giacenza getOne(Long idGiacenza){
        LOGGER.info("Retrieving 'giacenza' with id {}", idGiacenza);

        Giacenza giacenza = giacenzaRepository.findById(idGiacenza).orElseThrow(ResourceNotFoundException::new);
        List<Movimentazione> movimentazioni = movimentazioneService.getMovimentazioni(giacenza).stream().sorted((m1, m2) -> {
            if (m1.getData() == null || m2.getData() == null)
                return 0;
            return m2.getData().toLocalDate().compareTo(m1.getData().toLocalDate());
        }).collect(Collectors.toList());
        giacenza.setMovimentazioni(movimentazioni);

        LOGGER.info("Retrieved 'giacenza' {}", giacenza);
        return giacenza;
    }

    public void computeGiacenza(Long idArticolo, String lotto, Date scadenza, Float quantita){
        LOGGER.info("Compute 'giacenza' for idArticolo '{}',lotto '{}',scadenza '{}',quantita '{}'", idArticolo, lotto, scadenza, quantita);

        LOGGER.info("Retrieving 'giacenza' of idArticolo '{}' and lotto '{}'", idArticolo, lotto);
        Optional<Giacenza> giacenzaOptional = Optional.empty();
        Giacenza giacenza;
        Set<Giacenza> giacenze = giacenzaRepository.findByArticoloIdAndLotto(idArticolo, lotto);
        if(giacenze != null && !giacenze.isEmpty()){
            if(scadenza != null){
                giacenzaOptional = giacenze.stream().filter(g -> g.getScadenza().toLocalDate().compareTo(scadenza.toLocalDate())==0).findFirst();
            } else {
                giacenzaOptional = giacenze.stream().findFirst();
            }
        }
        if(giacenzaOptional.isPresent()){
            giacenza = giacenzaOptional.get();
            LOGGER.info("Retrieved 'giacenza' {}", giacenza);

            Set<Movimentazione> movimentazioni = movimentazioneService.getMovimentazioni(giacenza);
            Float quantitaInput = 0f;
            Float quantitaOutput = 0f;
            Float newQuantita = 0f;

            LOGGER.info("Computing input and output quantities");

            if(movimentazioni != null && !movimentazioni.isEmpty()){
                // 'movimentazioni' in input
                quantitaInput = movimentazioni.stream().filter(m -> m.getInputOutput().equals("INPUT")).map(m -> m.getQuantita()).reduce(0f, Float::sum);

                // 'movimentazioni' in output
                quantitaOutput = movimentazioni.stream().filter(m -> m.getInputOutput().equals("OUTPUT")).map(m -> m.getQuantita()).reduce(0f, Float::sum);

                newQuantita = quantitaInput - quantitaOutput;
            }
            giacenza.setQuantita(newQuantita);
            giacenza = giacenzaRepository.save(giacenza);
            LOGGER.info("Updated 'giacenza' {}", giacenza);

        } else {
            LOGGER.info("Creating a new 'giacenza'");
            Articolo articolo = new Articolo();
            articolo.setId(idArticolo);
            giacenza = new Giacenza();
            giacenza.setArticolo(articolo);
            giacenza.setLotto(lotto);
            giacenza.setScadenza(scadenza);
            giacenza.setQuantita(quantita);
            giacenza.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            giacenza = giacenzaRepository.save(giacenza);
            LOGGER.info("Created a new 'giacenza' {}", giacenza);
        }

    }
}
