package com.contafood.service;

import com.contafood.exception.ArticoloBarcodeCannotStartWithZeroException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.ArticoloRepository;
import com.contafood.repository.GiacenzaRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
    private final GiacenzaRepository giacenzaRepository;

    @Autowired
    public ArticoloService(final ArticoloRepository articoloRepository, final ArticoloImmagineService articoloImmagineService,
                           final ListinoPrezzoService listinoPrezzoService, final ListinoPrezzoVariazioneService listinoPrezzoVariazioneService,
                           final GiacenzaRepository giacenzaRepository){
        this.articoloRepository = articoloRepository;
        this.articoloImmagineService = articoloImmagineService;
        this.listinoPrezzoService = listinoPrezzoService;
        this.listinoPrezzoVariazioneService = listinoPrezzoVariazioneService;
        this.giacenzaRepository = giacenzaRepository;
    }

    public Set<Articolo> getAll(){
        LOGGER.info("Retrieving the list of 'articoli'");
        Set<Articolo> articoli = articoloRepository.findAllByOrderByCodiceAsc();
        LOGGER.info("Retrieved {} 'articoli'", articoli.size());
        return articoli;
    }

    public Set<Articolo> getAllByAttivo(Boolean active){
        LOGGER.info("Retrieving the list of 'articoli' filtered by 'attivo' value '{}'", active);
        Set<Articolo> articoli = articoloRepository.findByAttivoOrderByCodiceAsc(active);
        LOGGER.info("Retrieved {} 'articoli'", articoli.size());
        return articoli;
    }

    public Set<Articolo> getAllByAttivoAndFornitoreId(Boolean active, Long idFornitore){
        LOGGER.info("Retrieving the list of 'articoli' filtered by 'attivo' value '{}' and fornitore '{}'", active, idFornitore);
        Set<Articolo> articoli = articoloRepository.findByAttivoAndFornitoreId(active,idFornitore);
        LOGGER.info("Retrieved {} 'articoli'", articoli.size());
        return articoli;
    }

    public Set<Articolo> getAllByAttivoAndBarcode(Boolean active, String barcode){
        LOGGER.info("Retrieving the list of 'articoli' filtered by 'attivo' '{}' and 'barcode' '{}'", active, barcode);
        Set<Articolo> articoli;
        articoli = articoloRepository.findByAttivoAndBarcodeEqualsAndCompleteBarcodeIsTrue(active, barcode);
        if(articoli == null || articoli.isEmpty()){
            barcode = barcode.substring(0, 7);
            articoli = articoloRepository.findByAttivoAndBarcodeEqualsAndCompleteBarcodeIsFalse(active, barcode);
        }
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

        String barcode = articolo.getBarcode();
        if(!StringUtils.isEmpty(barcode)){
            if(barcode.startsWith("0")){
                LOGGER.error("The barcode '"+barcode+"' is not permitted: it starts with 0");
                throw new ArticoloBarcodeCannotStartWithZeroException();
            }
        }

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
            Float variazionePrezzo = listiniPrezziVariazioni.stream().filter(lpv -> lpv.getVariazionePrezzo() != null).map(lpv -> lpv.getVariazionePrezzo()).findFirst().orElse(null);

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

        String barcode = articolo.getBarcode();
        if(!StringUtils.isEmpty(barcode)){
            if(barcode.startsWith("0")){
                LOGGER.error("The barcode '"+barcode+"' is not permitted: it starts with 0");
                throw new ArticoloBarcodeCannotStartWithZeroException();
            }
        }

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

        LOGGER.info("Deleting 'giacenze' of articolo '{}'", articoloId);
        giacenzaRepository.deleteByArticoloId(articoloId);
        LOGGER.info("Deleted 'giacenze' of articolo '{}'", articoloId);

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
