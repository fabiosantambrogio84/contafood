package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.IngredienteRepository;
import com.contafood.repository.ProduzioneRepository;
import com.contafood.repository.RicettaRepository;
import com.contafood.util.Constants;
import com.contafood.util.LottoUtils;
import com.contafood.util.enumeration.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProduzioneService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProduzioneService.class);

    private final ProduzioneRepository produzioneRepository;
    private final ProduzioneIngredienteService produzioneIngredienteService;
    private final ProduzioneConfezioneService produzioneConfezioneService;
    private final GiacenzaArticoloService giacenzaArticoloService;
    private final GiacenzaIngredienteService giacenzaIngredienteService;
    private final ArticoloService articoloService;
    private final FornitoreService fornitoreService;
    private final RicettaRepository ricettaRepository;

    @Autowired
    public ProduzioneService(final ProduzioneRepository produzioneRepository,
                             final ProduzioneIngredienteService produzioneIngredienteService,
                             final ProduzioneConfezioneService produzioneConfezioneService,
                             final GiacenzaArticoloService giacenzaArticoloService,
                             final GiacenzaIngredienteService giacenzaIngredienteService,
                             final ArticoloService articoloService,
                             final FornitoreService fornitoreService,
                             final RicettaRepository ricettaRepository){
        this.produzioneRepository = produzioneRepository;
        this.produzioneIngredienteService = produzioneIngredienteService;
        this.produzioneConfezioneService = produzioneConfezioneService;
        this.giacenzaArticoloService = giacenzaArticoloService;
        this.giacenzaIngredienteService = giacenzaIngredienteService;
        this.articoloService = articoloService;
        this.fornitoreService = fornitoreService;
        this.ricettaRepository = ricettaRepository;
    }

    public Set<Produzione> getAll(){
        LOGGER.info("Retrieving the list of 'produzioni'");
        Set<Produzione> produzioni = produzioneRepository.findAll();
        LOGGER.info("Retrieved {} 'produzioni'", produzioni.size());
        return produzioni;
    }

    public Set<Produzione> getAllByLotto(String lotto){
        LOGGER.info("Retrieving the list of 'produzioni' filtered by 'lotto' '{}'", lotto);
        Set<Produzione> produzioni = produzioneRepository.findAllByLotto(lotto);
        LOGGER.info("Retrieved {} 'produzioni'", produzioni.size());
        return produzioni;
    }

    public Produzione getOne(Long produzioneId){
        LOGGER.info("Retrieving 'produzione' '{}'", produzioneId);
        Produzione produzione = produzioneRepository.findById(produzioneId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'produzione' '{}'", produzione);
        return produzione;
    }

    @Transactional
    public Produzione create(Produzione produzione){
        LOGGER.info("Creating 'produzione'");

        if(produzione.getScopo().equalsIgnoreCase("vendita")){
            LocalDate today = LocalDate.now();
            Integer anno = today.getYear();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uu");
            String yearTwoDigits = dateTimeFormatter.format(today);
            Integer giorno = today.getDayOfYear();
            Integer numeroProgressivo = produzioneRepository.findNextNumeroProgressivoByLottoAnnoAndLottoGiorno(anno, giorno).orElse(1);
            String lotto = LottoUtils.createLottoProduzione(yearTwoDigits, giorno, numeroProgressivo);

            LOGGER.info("Anno {}, giorno {}, numero progressivo {}, lotto {}", anno, giorno, numeroProgressivo, lotto);

            produzione.setLottoAnno(anno);
            produzione.setLottoGiorno(giorno);
            produzione.setLottoNumeroProgressivo(numeroProgressivo);
            produzione.setLotto(lotto);
        }
        produzione.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Produzione createdProduzione = produzioneRepository.save(produzione);
        Long produzioneId = createdProduzione.getId();

        createdProduzione.getProduzioneIngredienti().stream().forEach(pi -> {
            pi.getId().setProduzioneId(produzioneId);
            pi.getId().setUuid(UUID.randomUUID().toString());
            produzioneIngredienteService.create(pi);

            // compute 'giacenza ingrediente'
            giacenzaIngredienteService.computeGiacenza(pi.getId().getIngredienteId(), pi.getLotto(), pi.getScadenza(), pi.getQuantita(), Resource.PRODUZIONE_INGREDIENTE);
        });
        createdProduzione.getProduzioneConfezioni().stream().forEach(pc -> {
            pc.getId().setProduzioneId(produzioneId);
            produzioneConfezioneService.create(pc);
        });
        Integer numeroConfezioni = createdProduzione.getProduzioneConfezioni().stream().collect(Collectors.summingInt(pc -> pc.getNumConfezioni()));

        createdProduzione.setCodice(produzioneId.intValue());
        createdProduzione.setNumeroConfezioni(numeroConfezioni);
        createdProduzione = produzioneRepository.save(produzione);

        // create 'articolo', if not exists
        LOGGER.info("Creating 'articolo' if not already exists...");
        Ricetta ricetta = ricettaRepository.findById(createdProduzione.getRicetta().getId()).orElse(null);
        Fornitore fornitore = fornitoreService.getByRagioneSociale(Constants.DEFAULT_FORNITORE);
        String codiceArticolo = Constants.DEFAULT_FORNITORE_INITIALS + (ricetta != null ? ricetta.getCodice() : "");
        Optional<Articolo> optionalArticolo = articoloService.getByCodice(codiceArticolo);
        Articolo articolo;
        if(!optionalArticolo.isPresent()){
            articolo = new Articolo();
            articolo.setCodice(codiceArticolo);
            articolo.setDescrizione(ricetta.getNome());
            articolo.setFornitore(fornitore);
            articolo.setData(createdProduzione.getDataProduzione());
            articolo.setQuantitaPredefinita(createdProduzione.getQuantitaTotale());
            articolo.setSitoWeb(Boolean.FALSE);
            articolo.setAttivo(Boolean.TRUE);
            articolo = articoloService.create(articolo);
            LOGGER.info("Created 'articolo' '{}' from produzione", articolo);
        } else {
            LOGGER.info("The 'articolo' with 'codice' '{}' already exists", codiceArticolo);
            articolo = optionalArticolo.get();
        }

        // compute 'giacenza articolo'
        giacenzaArticoloService.computeGiacenza(articolo.getId(), createdProduzione.getLotto(), createdProduzione.getScadenza(), createdProduzione.getQuantitaTotale(), Resource.PRODUZIONE);

        LOGGER.info("Created 'produzione' '{}'", createdProduzione);
        return createdProduzione;
    }

    @Transactional
    public Produzione update(Produzione produzione){
        LOGGER.info("Updating 'produzione'");
        Set<ProduzioneIngrediente> produzioneIngredienti = produzione.getProduzioneIngredienti();
        produzione.setProduzioneIngredienti(new HashSet<>());
        produzioneIngredienteService.deleteByProduzioneId(produzione.getId());

        Set<ProduzioneConfezione> produzioneConfezioni = produzione.getProduzioneConfezioni();
        produzione.setProduzioneConfezioni(new HashSet<>());
        produzioneConfezioneService.deleteByProduzioneId(produzione.getId());

        Produzione produzioneCurrente = produzioneRepository.findById(produzione.getId()).orElseThrow(ResourceNotFoundException::new);
        produzione.setDataInserimento(produzioneCurrente.getDataInserimento());
        produzione.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Produzione updatedProduzione = produzioneRepository.save(produzione);
        Long produzioneId = updatedProduzione.getId();

        produzioneIngredienti.stream().forEach(pi -> {
            pi.getId().setProduzioneId(produzioneId);
            pi.getId().setUuid(UUID.randomUUID().toString());
            produzioneIngredienteService.create(pi);

            // compute 'giacenza ingrediente'
            giacenzaIngredienteService.computeGiacenza(pi.getId().getIngredienteId(), pi.getLotto(), pi.getScadenza(), pi.getQuantita(), Resource.PRODUZIONE_INGREDIENTE);
        });
        produzioneConfezioni.stream().forEach(pc -> {
            pc.getId().setProduzioneId(produzioneId);
            produzioneConfezioneService.create(pc);
        });
        Integer numeroConfezioni = updatedProduzione.getProduzioneConfezioni().stream().collect(Collectors.summingInt(pc -> pc.getNumConfezioni()));

        updatedProduzione.setCodice(produzioneId.intValue());
        updatedProduzione.setNumeroConfezioni(numeroConfezioni);
        updatedProduzione = produzioneRepository.save(produzione);

        // create 'articolo', if not exists
        LOGGER.info("Creating 'articolo' if not already exists...");
        Fornitore fornitore = fornitoreService.getByRagioneSociale(Constants.DEFAULT_FORNITORE);
        String codiceArticolo = Constants.DEFAULT_FORNITORE_INITIALS + updatedProduzione.getRicetta().getCodice();
        Optional<Articolo> optionalArticolo = articoloService.getByCodice(codiceArticolo);
        Articolo articolo;
        if(!optionalArticolo.isPresent()){
            articolo = new Articolo();
            articolo.setCodice(codiceArticolo);
            articolo.setDescrizione(updatedProduzione.getRicetta().getNome());
            articolo.setFornitore(fornitore);
            articolo.setData(updatedProduzione.getDataProduzione());
            articolo.setQuantitaPredefinita(updatedProduzione.getQuantitaTotale());
            articolo.setSitoWeb(Boolean.FALSE);
            articolo.setAttivo(Boolean.TRUE);
            articolo = articoloService.create(articolo);
            LOGGER.info("Created 'articolo' '{}' from produzione", articolo);
        } else {
            LOGGER.info("The 'articolo' with 'codice' '{}' already exists", codiceArticolo);
            articolo = optionalArticolo.get();
        }

        // compute 'giacenza articolo'
        giacenzaArticoloService.computeGiacenza(articolo.getId(), updatedProduzione.getLotto(), updatedProduzione.getScadenza(), updatedProduzione.getQuantitaTotale(), Resource.PRODUZIONE);

        LOGGER.info("Updated 'produzione' '{}'", updatedProduzione);
        return updatedProduzione;
    }

    @Transactional
    public void delete(Long produzioneId){
        LOGGER.info("Deleting 'produzione' '{}'", produzioneId);
        Produzione produzione = produzioneRepository.findById(produzioneId).get();
        Set<ProduzioneIngrediente> produzioneIngredienti = produzioneIngredienteService.findByProduzioneId(produzioneId);

        String codiceArticolo = Constants.DEFAULT_FORNITORE_INITIALS + produzione.getRicetta().getCodice();
        Optional<Articolo> optionalArticolo = articoloService.getByCodice(codiceArticolo);
        if(optionalArticolo.isPresent()){
            Articolo articolo = optionalArticolo.get();

            // compute 'giacenza articolo'
            giacenzaArticoloService.computeGiacenza(articolo.getId(), produzione.getLotto(), produzione.getScadenza(), produzione.getQuantitaTotale(), Resource.PRODUZIONE);
        }

        for(ProduzioneIngrediente produzioneIngrediente : produzioneIngredienti){
            // compute 'giacenza ingrediente'
            giacenzaIngredienteService.computeGiacenza(produzioneIngrediente.getId().getIngredienteId(), produzioneIngrediente.getLotto(), produzioneIngrediente.getScadenza(), produzioneIngrediente.getQuantita(), Resource.PRODUZIONE_INGREDIENTE);
        }

        produzioneIngredienteService.deleteByProduzioneId(produzioneId);
        produzioneConfezioneService.deleteByProduzioneId(produzioneId);
        produzioneRepository.deleteById(produzioneId);
        LOGGER.info("Deleted 'produzione' '{}'", produzioneId);
    }

    @Transactional
    public void deleteByRicettaId(Long idRicetta){
        LOGGER.info("Deleting 'produzioni' of 'ricetta' '{}'", idRicetta);
        List<Produzione> produzioniToDelete = produzioneRepository.findByRicettaId(idRicetta);
        produzioniToDelete.forEach(p -> delete(p.getId()));
        LOGGER.info("Deleted 'produzioni' of 'ricetta' '{}'", idRicetta);
    }

    public Set<ProduzioneConfezione> getProduzioneConfezioni(Long produzioneId){
        LOGGER.info("Retrieving 'produzioneConfezioni' for produzione '{}'", produzioneId);
        Set<ProduzioneConfezione> produzioneConfezioni = produzioneConfezioneService.findByProduzioneId(produzioneId);
        LOGGER.info("Retrieved {} 'produzioneConfezioni'", produzioneConfezioni.size());
        return produzioneConfezioni;
    }

}
