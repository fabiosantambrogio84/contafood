package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.ArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(ArticoloService.class);

    private final ArticoloRepository articoloRepository;

    private final ArticoloImmagineService articoloImmagineService;

    private final ListinoPrezzoService listinoPrezzoService;

    private final ListinoPrezzoVariazioneService listinoPrezzoVariazioneService;

    @Autowired
    public ArticoloService(final ArticoloRepository articoloRepository, final ArticoloImmagineService articoloImmagineService, final ListinoPrezzoService listinoPrezzoService, final ListinoPrezzoVariazioneService listinoPrezzoVariazioneService){
        this.articoloRepository = articoloRepository;
        this.articoloImmagineService = articoloImmagineService;
        this.listinoPrezzoService = listinoPrezzoService;
        this.listinoPrezzoVariazioneService = listinoPrezzoVariazioneService;
    }

    public Set<Articolo> getAll(){
        LOGGER.info("Retrieving the list of 'articoli'");
        Set<Articolo> articoli = articoloRepository.findAll();
        LOGGER.info("Retrieved {} 'articoli'", articoli.size());
        return articoli;
    }

    public Set<Articolo> getAllByAttivo(Boolean active){
        LOGGER.info("Retrieving the list of 'articoli' filtered by 'attivo' value '{}'", active);
        Set<Articolo> articoli = articoloRepository.findByAttivo(active);
        LOGGER.info("Retrieved {} 'articoli'", articoli.size());
        return articoli;
    }

    public Articolo getOne(Long articoloId){
        LOGGER.info("Retrieving 'articolo' '{}'", articoloId);
        Articolo articolo = articoloRepository.findById(articoloId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'articolo' '{}'", articolo);
        return articolo;
    }

    public Articolo create(Articolo articolo){
        LOGGER.info("Creating 'articolo'");
        String codice = articolo.getCodice().toUpperCase();
        articolo.setCodice(codice);
        articolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Articolo createdArticolo = articoloRepository.save(articolo);

        // compute 'listini prezzi'
        LOGGER.info("Computing 'listiniPrezzi' for 'articolo' '{}'", createdArticolo.getId());
        List<ListinoPrezzo> listiniPrezzi = new ArrayList<>();
        Set<Listino> listini = listinoPrezzoService.getAll().stream().map(lp -> lp.getListino()).distinct().collect(Collectors.toSet());
        listini.forEach(l -> {
            ListinoPrezzo listinoPrezzo = new ListinoPrezzo();
            listinoPrezzo.setListino(l);
            listinoPrezzo.setArticolo(createdArticolo);
            listinoPrezzo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

            List<ListinoPrezzoVariazione> listiniPrezziVariazioni = l.getListiniPrezziVariazioni();
            String tipologiaVariazionePrezzo = listiniPrezziVariazioni.stream().filter(lpv -> lpv.getTipologiaVariazionePrezzo() != null).map(lpv -> lpv.getTipologiaVariazionePrezzo()).findFirst().orElse(null);
            Float variazionePrezzo = listiniPrezziVariazioni.stream().map(lpv -> lpv.getVariazionePrezzo()).findFirst().orElse(null);

            BigDecimal newPrezzo = listinoPrezzoService.computePrezzoInListinoCreation(l, createdArticolo, tipologiaVariazionePrezzo, variazionePrezzo);
            listinoPrezzo.setPrezzo(newPrezzo);

            listiniPrezzi.add(listinoPrezzo);
        });
        if(!listiniPrezzi.isEmpty()){
            listinoPrezzoService.create(listiniPrezzi);
        }
        LOGGER.info("Computed 'listiniPrezzi' for 'articolo' '{}'", createdArticolo.getId());

        LOGGER.info("Created 'articolo' '{}'", createdArticolo);
        return createdArticolo;
    }

    public Articolo update(Articolo articolo){
        LOGGER.info("Updating 'articolo'");
        Articolo articoloCurrent = articoloRepository.findById(articolo.getId()).orElseThrow(ResourceNotFoundException::new);
        BigDecimal prezzoListinoBaseCurrent = articoloCurrent.getPrezzoListinoBase();
        articolo.setDataInserimento(articoloCurrent.getDataInserimento());
        String codice = articolo.getCodice().toUpperCase();
        articolo.setCodice(codice);
        Articolo updatedArticolo = articoloRepository.save(articolo);

        if(updatedArticolo.getPrezzoListinoBase() != prezzoListinoBaseCurrent){
            // compute 'listini prezzi'
            listinoPrezzoService.computeListiniPrezziForArticolo(updatedArticolo);
        }

        LOGGER.info("Updated 'articolo' '{}'", updatedArticolo);
        return updatedArticolo;
    }

    @Transactional
    public void delete(Long articoloId){
        LOGGER.info("Deleting 'listiniPrezziVariazioni' of articolo '{}'", articoloId);
        listinoPrezzoVariazioneService.deleteByArticoloId(articoloId);
        LOGGER.info("Deleted 'listiniPrezziVariazioni' of articolo '{}'", articoloId);

        LOGGER.info("Deleting 'listiniPrezzi' of articolo '{}'", articoloId);
        listinoPrezzoService.deleteByArticoloId(articoloId);
        LOGGER.info("Deleted 'listiniPrezzi' of articolo '{}'", articoloId);

        LOGGER.info("Deleting 'articolo' '{}'", articoloId);
        articoloImmagineService.deleteByArticoloId(articoloId);
        articoloRepository.deleteById(articoloId);
        LOGGER.info("Deleted 'articolo' '{}'", articoloId);
    }

    public List<ArticoloImmagine> getArticoloImmagini(Long articoloId){
        LOGGER.info("Retrieving the list of 'articoloImmagini' of the 'articolo' '{}'", articoloId);
        List<ArticoloImmagine> articoloImmagini = articoloImmagineService.getByArticoloId(articoloId);
        LOGGER.info("Retrieved {} 'articoloImmagini'", articoloImmagini.size());
        return articoloImmagini;
    }

    public List<ListinoPrezzo> getArticoloListiniPrezzi(Long articoloId){
        LOGGER.info("Retrieving the list of 'listiniPrezzi' of the 'articolo' '{}'", articoloId);
        List<ListinoPrezzo> listiniPrezzi = listinoPrezzoService.getByArticoloId(articoloId);
        LOGGER.info("Retrieved {} 'listiniPrezzi'", listiniPrezzi.size());
        return listiniPrezzi;
    }

}
