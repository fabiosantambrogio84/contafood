package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.GiacenzaIngrediente;
import com.contafood.model.Ingrediente;
import com.contafood.model.Movimentazione;
import com.contafood.model.MovimentazioneManualeIngrediente;
import com.contafood.model.views.VGiacenzaIngrediente;
import com.contafood.repository.GiacenzaIngredienteRepository;
import com.contafood.repository.views.VGiacenzaIngredienteRepository;
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
public class GiacenzaIngredienteService {

    private static Logger LOGGER = LoggerFactory.getLogger(GiacenzaIngredienteService.class);

    private final GiacenzaIngredienteRepository giacenzaIngredienteRepository;
    private final VGiacenzaIngredienteRepository vGiacenzaIngredienteRepository;
    private final MovimentazioneService movimentazioneService;
    private final MovimentazioneManualeIngredienteService movimentazioneManualeIngredienteService;

    @Autowired
    public GiacenzaIngredienteService(final GiacenzaIngredienteRepository giacenzaIngredienteRepository,
                                      final VGiacenzaIngredienteRepository vGiacenzaIngredienteRepository,
                                      final MovimentazioneService movimentazioneService,
                                      final MovimentazioneManualeIngredienteService movimentazioneManualeIngredienteService){
        this.giacenzaIngredienteRepository = giacenzaIngredienteRepository;
        this.vGiacenzaIngredienteRepository = vGiacenzaIngredienteRepository;
        this.movimentazioneService = movimentazioneService;
        this.movimentazioneManualeIngredienteService = movimentazioneManualeIngredienteService;
    }

    public Set<VGiacenzaIngrediente> getAll(){
        LOGGER.info("Retrieving the list of 'giacenze ingrediente'");
        Set<VGiacenzaIngrediente> giacenze = vGiacenzaIngredienteRepository.findAll();
        LOGGER.info("Retrieved {} 'giacenze ingrediente'", giacenze.size());
        return giacenze;
    }

    public Set<GiacenzaIngrediente> getAllNotAggregate(){
        LOGGER.info("Retrieving the list of 'giacenze ingrediente'");
        Set<GiacenzaIngrediente> giacenze = giacenzaIngredienteRepository.findAll();
        LOGGER.info("Retrieved {} 'giacenze ingrediente'", giacenze.size());
        return giacenze;
    }

    @Transactional
    public GiacenzaIngrediente create(GiacenzaIngrediente giacenzaIngrediente){
        LOGGER.info("Creating 'giacenza ingrediente'");

        // create movimentazione manuale ingrediente
        movimentazioneManualeIngredienteService.create(giacenzaIngrediente);

        computeGiacenza(giacenzaIngrediente.getIngrediente().getId(), giacenzaIngrediente.getLotto(), giacenzaIngrediente.getScadenza(), giacenzaIngrediente.getQuantita(), Resource.MOVIMENTAZIONE_MANUALE_INGREDIENTE);

        //giacenzaIngrediente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        //GiacenzaIngrediente createdGiacenzaIngrediente = giacenzaIngredienteRepository.save(giacenzaIngrediente);

        LOGGER.info("Created 'giacenza ingrediente' '{}'", giacenzaIngrediente);
        return giacenzaIngrediente;
    }

    public void delete(Long idGiacenza){
        LOGGER.info("Deleting 'giacenza ingrediente' '{}'", idGiacenza);
        giacenzaIngredienteRepository.deleteById(idGiacenza);
        LOGGER.info("Deleted 'giacenza ingrediente' '{}'", idGiacenza);
    }

    @Transactional
    public void bulkDelete(List<Long> idIngredienti){
        LOGGER.info("Bulk deleting all the specified 'giacenze ingrediente' by 'idFornitore' (number of elements to delete: {})", idIngredienti.size());
        movimentazioneManualeIngredienteService.deleteByIngredienteIdIn(idIngredienti);
        giacenzaIngredienteRepository.deleteByIngredienteIdIn(idIngredienti);
        LOGGER.info("Bulk deleted all the specified 'giacenze ingrediente");
    }

    public Map<String, Object> getOne(Long idIngrediente){
        LOGGER.info("Retrieving 'giacenza ingrediente' of ingrediente {}", idIngrediente);

        HashMap<String, Object> result = new HashMap<>();

        VGiacenzaIngrediente giacenzaIngrediente = vGiacenzaIngredienteRepository.findById(idIngrediente).orElseThrow(ResourceNotFoundException::new);
        Set<GiacenzaIngrediente> giacenzeIngredienti = giacenzaIngredienteRepository.findByIngredienteId(idIngrediente);

        List<Movimentazione> movimentazioni = new ArrayList<>();
        Set<Movimentazione> movimentazioniIngrediente = new HashSet<>();
        if(giacenzeIngredienti != null && !giacenzeIngredienti.isEmpty()){
            giacenzeIngredienti.stream().forEach(gi -> {
                movimentazioniIngrediente.addAll(movimentazioneService.getMovimentazioniIngrediente(gi));
            });
        }
        if(!movimentazioniIngrediente.isEmpty()){
            movimentazioni = movimentazioniIngrediente.stream().collect(Collectors.toList());
            movimentazioni.sort(Comparator.comparing(Movimentazione::getData).reversed());
        }

        result.put("ingrediente", giacenzaIngrediente.getIngrediente());
        result.put("quantita", giacenzaIngrediente.getQuantita());
        result.put("movimentazioni", movimentazioni);

        LOGGER.info("Retrieved 'giacenza ingrediente' of ingrediente {}", idIngrediente);
        return result;
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

            Set<Movimentazione> movimentazioni = movimentazioneService.getMovimentazioniIngrediente(giacenzaIngrediente);
            Float quantitaInput = 0f;
            Float quantitaOutput = 0f;
            Float newQuantita = 0f;

            LOGGER.info("Computing input and output quantities");

            if(movimentazioni != null && !movimentazioni.isEmpty()){
                // 'movimentazioni' in input
                quantitaInput = movimentazioni.stream().filter(m -> m.getInputOutput().equals("INPUT") && m.getQuantita() != null).map(m -> m.getQuantita()).reduce(0f, Float::sum);

                // 'movimentazioni' in output
                quantitaOutput = movimentazioni.stream().filter(m -> m.getInputOutput().equals("OUTPUT") && m.getQuantita() != null).map(m -> m.getQuantita()).reduce(0f, Float::sum);

                quantita = (quantita != null ? quantita : 0f);

                switch(resource){
                    case DDT_ACQUISTO:
                        quantitaInput = quantitaInput + quantita;
                        break;
                    case PRODUZIONE_INGREDIENTE:
                        quantitaOutput = quantitaOutput + quantita;
                        break;
                    default:
                        break;
                }
                newQuantita = quantitaInput - quantitaOutput;
            } else {
                newQuantita = (quantita != null ? quantita : 0f);
            }
            giacenzaIngrediente.setQuantita(newQuantita);
            Ingrediente ingrediente = new Ingrediente();
            ingrediente.setId(idIngrediente);
            giacenzaIngrediente.setIngrediente(ingrediente);

            giacenzaIngrediente = giacenzaIngredienteRepository.save(giacenzaIngrediente);
            LOGGER.info("Updated 'giacenza ingrediente' {}", giacenzaIngrediente);

        } else {
            LOGGER.info("Creating a new 'giacenza ingrediente'");
            Float newQuantita = 0f;
            if(quantita != null){
                newQuantita = quantita;
            }
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
