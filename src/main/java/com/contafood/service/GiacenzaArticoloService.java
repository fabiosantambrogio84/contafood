package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Articolo;
import com.contafood.model.GiacenzaArticolo;
import com.contafood.model.Movimentazione;
import com.contafood.model.Ricetta;
import com.contafood.repository.ArticoloRepository;
import com.contafood.repository.GiacenzaArticoloRepository;
import com.contafood.repository.RicettaRepository;
import com.contafood.util.enumeration.Resource;
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
public class GiacenzaArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(GiacenzaArticoloService.class);

    private final GiacenzaArticoloRepository giacenzaArticoloRepository;
    private final MovimentazioneService movimentazioneService;

    @Autowired
    public GiacenzaArticoloService(final GiacenzaArticoloRepository giacenzaArticoloRepository,
                                   final MovimentazioneService movimentazioneService){
        this.giacenzaArticoloRepository = giacenzaArticoloRepository;
        this.movimentazioneService = movimentazioneService;
    }

    public Set<GiacenzaArticolo> getAll(){
        LOGGER.info("Retrieving the list of 'giacenze articolo'");
        Set<GiacenzaArticolo> giacenze = giacenzaArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'giacenze articolo'", giacenze.size());
        return giacenze;
    }

    public GiacenzaArticolo create(GiacenzaArticolo giacenzaArticolo){
        LOGGER.info("Creating 'giacenza articolo'");

        giacenzaArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        GiacenzaArticolo createdGiacenzaArticolo = giacenzaArticoloRepository.save(giacenzaArticolo);

        LOGGER.info("Created 'giacenza articolo' '{}'", createdGiacenzaArticolo);
        return createdGiacenzaArticolo;
    }

    public void delete(Long idGiacenza){
        LOGGER.info("Deleting 'giacenza articolo' '{}'", idGiacenza);
        giacenzaArticoloRepository.deleteById(idGiacenza);
        LOGGER.info("Deleted 'giacenza articolo' '{}'", idGiacenza);
    }

    @Transactional
    public void bulkDelete(List<Long> giacenzeIds){
        LOGGER.info("Bulk deleting all the specified 'giacenze articolo' (number of elements to delete: {})", giacenzeIds.size());
        giacenzaArticoloRepository.deleteByIdIn(giacenzeIds);
        LOGGER.info("Bulk deleted all the specified 'giacenze articolo");
    }

    public GiacenzaArticolo getOne(Long idGiacenza){
        LOGGER.info("Retrieving 'giacenza articolo' with id {}", idGiacenza);

        GiacenzaArticolo giacenzaArticolo = giacenzaArticoloRepository.findById(idGiacenza).orElseThrow(ResourceNotFoundException::new);
        List<Movimentazione> movimentazioni = movimentazioneService.getMovimentazioniArticoli(giacenzaArticolo).stream().collect(Collectors.toList());
        movimentazioni.sort(Comparator.comparing(Movimentazione::getData).reversed());

        giacenzaArticolo.setMovimentazioni(movimentazioni);

        LOGGER.info("Retrieved 'giacenza articolo' {}", giacenzaArticolo);
        return giacenzaArticolo;
    }

    public void computeGiacenza(Long idArticolo, String lotto, Date scadenza, Float quantita, Resource resource){
        LOGGER.info("Compute 'giacenza articolo' for idArticolo '{}', lotto '{}',scadenza '{}',quantita '{}'",
                idArticolo, lotto, scadenza, quantita);

        LOGGER.info("Retrieving 'giacenza articolo' of articolo '{}' and lotto '{}'", idArticolo, lotto);
        Optional<GiacenzaArticolo> giacenzaOptional = Optional.empty();
        GiacenzaArticolo giacenzaArticolo;
        Set<GiacenzaArticolo> giacenze = giacenzaArticoloRepository.findByArticoloIdAndLotto(idArticolo, lotto);
        if(giacenze != null && !giacenze.isEmpty()){
            if(scadenza != null){
                giacenzaOptional = giacenze.stream().filter(g -> g.getScadenza() != null && g.getScadenza().toLocalDate().compareTo(scadenza.toLocalDate())==0).findFirst();
            } else {
                giacenzaOptional = giacenze.stream().findFirst();
            }
        }
        if(giacenzaOptional.isPresent()){
            giacenzaArticolo = giacenzaOptional.get();
            LOGGER.info("Retrieved 'giacenza articolo' {}", giacenzaArticolo);

            Set<Movimentazione> movimentazioni = movimentazioneService.getMovimentazioniArticoli(giacenzaArticolo);
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
            giacenzaArticolo.setQuantita(newQuantita);
            Articolo articolo = new Articolo();
            articolo.setId(idArticolo);
            giacenzaArticolo.setArticolo(articolo);

            giacenzaArticolo = giacenzaArticoloRepository.save(giacenzaArticolo);
            LOGGER.info("Updated 'giacenza articolo' {}", giacenzaArticolo);

        } else {
            LOGGER.info("Creating a new 'giacenza articolo'");
            Float newQuantita = quantita;
            if(resource.equals(Resource.DDT) || resource.equals(Resource.FATTURA_ACCOMPAGNATORIA)){
                newQuantita = newQuantita * -1;
            }

            giacenzaArticolo = new GiacenzaArticolo();
            Articolo articolo = new Articolo();
            articolo.setId(idArticolo);
            giacenzaArticolo.setArticolo(articolo);
            giacenzaArticolo.setLotto(lotto);
            giacenzaArticolo.setScadenza(scadenza);
            giacenzaArticolo.setQuantita(newQuantita);
            giacenzaArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            giacenzaArticolo = giacenzaArticoloRepository.save(giacenzaArticolo);
            LOGGER.info("Created a new 'giacenza articolo' {}", giacenzaArticolo);
        }

    }
}
