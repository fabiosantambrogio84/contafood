package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.GiacenzaIngrediente;
import com.contafood.model.Ingrediente;
import com.contafood.model.Movimentazione;
import com.contafood.repository.GiacenzaIngredienteRepository;
import com.contafood.util.enumeration.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiacenzaIngredienteService {

    private static Logger LOGGER = LoggerFactory.getLogger(GiacenzaIngredienteService.class);

    private final GiacenzaIngredienteRepository giacenzaIngredienteRepository;
    private final MovimentazioneService movimentazioneService;

    @Autowired
    public GiacenzaIngredienteService(final GiacenzaIngredienteRepository giacenzaIngredienteRepository,
                                      final MovimentazioneService movimentazioneService){
        this.giacenzaIngredienteRepository = giacenzaIngredienteRepository;
        this.movimentazioneService = movimentazioneService;
    }

    public Set<GiacenzaIngrediente> getAll(){
        LOGGER.info("Retrieving the list of 'giacenze ingrediente'");
        Set<GiacenzaIngrediente> giacenze = giacenzaIngredienteRepository.findAll();
        LOGGER.info("Retrieved {} 'giacenze ingrediente'", giacenze.size());
        return giacenze;
    }

    public GiacenzaIngrediente create(GiacenzaIngrediente giacenzaIngrediente){
        LOGGER.info("Creating 'giacenza ingrediente'");

        giacenzaIngrediente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        GiacenzaIngrediente createdGiacenzaIngrediente = giacenzaIngredienteRepository.save(giacenzaIngrediente);

        LOGGER.info("Created 'giacenza ingrediente' '{}'", createdGiacenzaIngrediente);
        return createdGiacenzaIngrediente;
    }

    public void delete(Long idGiacenza){
        LOGGER.info("Deleting 'giacenza ingrediente' '{}'", idGiacenza);
        giacenzaIngredienteRepository.deleteById(idGiacenza);
        LOGGER.info("Deleted 'giacenza ingrediente' '{}'", idGiacenza);
    }

    @Transactional
    public void bulkDelete(List<Long> giacenzeIds){
        LOGGER.info("Bulk deleting all the specified 'giacenze ingrediente' (number of elements to delete: {})", giacenzeIds.size());
        giacenzaIngredienteRepository.deleteByIdIn(giacenzeIds);
        LOGGER.info("Bulk deleted all the specified 'giacenze ingrediente");
    }

    public GiacenzaIngrediente getOne(Long idGiacenza){
        LOGGER.info("Retrieving 'giacenza ingrediente' with id {}", idGiacenza);

        GiacenzaIngrediente giacenzaIngrediente = giacenzaIngredienteRepository.findById(idGiacenza).orElseThrow(ResourceNotFoundException::new);
        List<Movimentazione> movimentazioni = movimentazioneService.getMovimentazioniIngredienti(giacenzaIngrediente).stream().collect(Collectors.toList());
        movimentazioni.sort(Comparator.comparing(Movimentazione::getData).reversed());

        giacenzaIngrediente.setMovimentazioni(movimentazioni);

        LOGGER.info("Retrieved 'giacenza ingrediente' {}", giacenzaIngrediente);
        return giacenzaIngrediente;
    }

    public void computeGiacenza(Long idIngrediente, String lotto, Date scadenza, Float quantita, Resource resource){
        LOGGER.info("Compute 'giacenza ingrediente' for idIngrediente '{}', lotto '{}',scadenza '{}',quantita '{}'",
                idIngrediente, lotto, scadenza, quantita);

        LOGGER.info("Retrieving 'giacenza ingrediente' of ingrediente '{}' and lotto '{}'", idIngrediente, lotto);
        Optional<GiacenzaIngrediente> giacenzaOptional = Optional.empty();
        GiacenzaIngrediente giacenzaIngrediente;
        Set<GiacenzaIngrediente> giacenze = giacenzaIngredienteRepository.findByIngredienteIdAndLotto(idIngrediente, lotto);
        if(giacenze != null && !giacenze.isEmpty()){
            if(scadenza != null){
                giacenzaOptional = giacenze.stream().filter(g -> g.getScadenza() != null && g.getScadenza().toLocalDate().compareTo(scadenza.toLocalDate())==0).findFirst();
            } else {
                giacenzaOptional = giacenze.stream().findFirst();
            }
        }
        if(giacenzaOptional.isPresent()){
            giacenzaIngrediente = giacenzaOptional.get();
            LOGGER.info("Retrieved 'giacenza ingrediente' {}", giacenzaIngrediente);

            Set<Movimentazione> movimentazioni = movimentazioneService.getMovimentazioniIngredienti(giacenzaIngrediente);
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
            giacenzaIngrediente.setQuantita(newQuantita);
            Ingrediente ingrediente = new Ingrediente();
            ingrediente.setId(idIngrediente);
            giacenzaIngrediente.setIngrediente(ingrediente);

            giacenzaIngrediente = giacenzaIngredienteRepository.save(giacenzaIngrediente);
            LOGGER.info("Updated 'giacenza ingrediente' {}", giacenzaIngrediente);

        } else {
            LOGGER.info("Creating a new 'giacenza ingrediente'");
            Float newQuantita = quantita;
            if(resource.equals(Resource.PRODUZIONE_INGREDIENTE)){
                newQuantita = newQuantita * -1;
            }

            giacenzaIngrediente = new GiacenzaIngrediente();
            Ingrediente ingrediente = new Ingrediente();
            ingrediente.setId(idIngrediente);
            giacenzaIngrediente.setIngrediente(ingrediente);
            giacenzaIngrediente.setLotto(lotto);
            giacenzaIngrediente.setScadenza(scadenza);
            giacenzaIngrediente.setQuantita(newQuantita);
            giacenzaIngrediente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            giacenzaIngrediente = giacenzaIngredienteRepository.save(giacenzaIngrediente);
            LOGGER.info("Created a new 'giacenza ingrediente' {}", giacenzaIngrediente);
        }

    }
}
