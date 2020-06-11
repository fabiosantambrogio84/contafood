package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Articolo;
import com.contafood.model.Giacenza;
import com.contafood.model.Movimentazione;
import com.contafood.model.Ricetta;
import com.contafood.repository.ArticoloRepository;
import com.contafood.repository.GiacenzaRepository;
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
public class GiacenzaService {

    private static Logger LOGGER = LoggerFactory.getLogger(GiacenzaService.class);

    private final GiacenzaRepository giacenzaRepository;
    private final ArticoloRepository articoloRepository;
    private final RicettaRepository ricettaRepository;
    private final MovimentazioneService movimentazioneService;

    @Autowired
    public GiacenzaService(final GiacenzaRepository giacenzaRepository,
                           final ArticoloRepository articoloRepository,
                           final RicettaRepository ricettaRepository,
                           final MovimentazioneService movimentazioneService){
        this.giacenzaRepository = giacenzaRepository;
        this.articoloRepository = articoloRepository;
        this.ricettaRepository = ricettaRepository;
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
    public void bulkDelete(List<Long> giacenzeIds){
        LOGGER.info("Bulk deleting all the specified 'giacenze (number of elements to delete: {})'", giacenzeIds.size());
        giacenzaRepository.deleteByIdIn(giacenzeIds);
        LOGGER.info("Bulk deleted all the specified 'giacenze");
    }

    public Giacenza getOne(Long idGiacenza){
        LOGGER.info("Retrieving 'giacenza' with id {}", idGiacenza);

        Giacenza giacenza = giacenzaRepository.findById(idGiacenza).orElseThrow(ResourceNotFoundException::new);
        List<Movimentazione> movimentazioni = movimentazioneService.getMovimentazioni(giacenza).stream().collect(Collectors.toList());
        movimentazioni.sort(Comparator.comparing(Movimentazione::getData).reversed());

        giacenza.setMovimentazioni(movimentazioni);

        LOGGER.info("Retrieved 'giacenza' {}", giacenza);
        return giacenza;
    }

    public void computeGiacenza(Long idArticolo, Long idRicetta, String lotto, Date scadenza, Float quantita, Resource resource){
        LOGGER.info("Compute 'giacenza' for idArticolo '{}',idRicetta '{}', lotto '{}',scadenza '{}',quantita '{}'",
                idArticolo, idRicetta, lotto, scadenza, quantita);

        String codiceArticoloRicetta = null;
        if(idArticolo != null){
            codiceArticoloRicetta = articoloRepository.findById(idArticolo).get().getCodice();
        } else if(idRicetta != null){
            codiceArticoloRicetta = ricettaRepository.findById(idRicetta).get().getCodice();
            codiceArticoloRicetta = "UR"+codiceArticoloRicetta;
        }

        LOGGER.info("Retrieving 'giacenza' of codiceArticoloRicetta '{}' and lotto '{}'", codiceArticoloRicetta, lotto);
        Optional<Giacenza> giacenzaOptional = Optional.empty();
        Giacenza giacenza;
        Set<Giacenza> giacenze = giacenzaRepository.findByCodiceArticoloRicettaAndLotto(codiceArticoloRicetta, lotto);
        if(giacenze != null && !giacenze.isEmpty()){
            if(scadenza != null){
                giacenzaOptional = giacenze.stream().filter(g -> g.getScadenza() != null && g.getScadenza().toLocalDate().compareTo(scadenza.toLocalDate())==0).findFirst();
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

            if(idRicetta != null){
                Ricetta ricetta = new Ricetta();
                ricetta.setId(idRicetta);
                giacenza.setRicetta(ricetta);
            } else if(idArticolo != null){
                Articolo articolo = new Articolo();
                articolo.setId(idArticolo);
                giacenza.setArticolo(articolo);
            }

            giacenza = giacenzaRepository.save(giacenza);
            LOGGER.info("Updated 'giacenza' {}", giacenza);

        } else {
            LOGGER.info("Creating a new 'giacenza'");
            Float newQuantita = quantita;
            if(resource.equals(Resource.DDT) || resource.equals(Resource.FATTURA_ACCOMPAGNATORIA)){
                newQuantita = newQuantita * -1;
            }

            giacenza = new Giacenza();
            if(idRicetta != null){
                Ricetta ricetta = new Ricetta();
                ricetta.setId(idRicetta);
                giacenza.setRicetta(ricetta);
            } else if(idArticolo != null){
                Articolo articolo = new Articolo();
                articolo.setId(idArticolo);
                giacenza.setArticolo(articolo);
            }
            giacenza.setCodiceArticoloRicetta(codiceArticoloRicetta);
            giacenza.setLotto(lotto);
            giacenza.setScadenza(scadenza);
            giacenza.setQuantita(newQuantita);
            giacenza.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            giacenza = giacenzaRepository.save(giacenza);
            LOGGER.info("Created a new 'giacenza' {}", giacenza);
        }

    }
}
