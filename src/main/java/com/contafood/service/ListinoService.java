package com.contafood.service;

import com.contafood.exception.ListinoBaseAlreadyExistingException;
import com.contafood.exception.ListinoBaseCannotHaveVariazionePrezzoException;
import com.contafood.exception.ListinoTipologiaNotAllowedException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.ListinoRepository;
import com.contafood.util.TipologiaListino;
import com.contafood.util.TipologiaListinoPrezzoVariazione;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ListinoService {

    private static Logger LOGGER = LoggerFactory.getLogger(ListinoService.class);

    private final ListinoRepository listinoRepository;

    private final ArticoloService articoloService;

    private final ListinoPrezzoService listinoPrezzoService;

    private final static String TIPOLOGIA_BASE = "BASE";

    @Autowired
    public ListinoService(final ListinoRepository listinoRepository, final ArticoloService articoloService, final ListinoPrezzoService listinoPrezzoService){
        this.listinoRepository = listinoRepository;
        this.articoloService = articoloService;
        this.listinoPrezzoService = listinoPrezzoService;
    }

    public Set<Listino> getAll(){
        LOGGER.info("Retrieving the list of 'listini'");
        Set<Listino> listini = listinoRepository.findAllByOrderByTipologia();
        LOGGER.info("Retrieved {} 'listini'", listini.size());
        return listini;
    }

    public Listino getOne(Long listinoId){
        LOGGER.info("Retrieving 'listino' '{}'", listinoId);
        Listino listino = listinoRepository.findById(listinoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'listino' '{}'", listino);
        return listino;
    }

    @Transactional
    public Listino create(Listino listino){
        LOGGER.info("Creating 'listino'");
        checkListinoTipologia(listino.getTipologia());
        if(listino.getTipologia().equals(TipologiaListino.BASE.name())){
            if(checkIfListinoBaseExists()){
                throw new ListinoBaseAlreadyExistingException();
            };
            if(!StringUtils.isEmpty(listino.getTipologiaVariazionePrezzo()) || listino.getVariazionePrezzo() != null){
                throw new ListinoBaseCannotHaveVariazionePrezzoException();
            }
        }
        listino.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Listino createdListino = listinoRepository.save(listino);
        LOGGER.info("Created 'listino' '{}'", createdListino);

        LOGGER.info("Populating 'listiniPrezzi' for listino '{}'", createdListino.getId());
        String tipologiaVariazionePrezzo = createdListino.getTipologiaVariazionePrezzo();
        Float variazionePrezzo = createdListino.getVariazionePrezzo();

        List<ListinoPrezzo> listiniPrezzi = new ArrayList<>();
        Set<Articolo> articoli = articoloService.getAll();
        articoli.forEach(a -> {
            ListinoPrezzo listinoPrezzo = new ListinoPrezzo();
            listinoPrezzo.setArticolo(a);
            listinoPrezzo.setListino(createdListino);
            listinoPrezzo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            BigDecimal newPrezzo = a.getPrezzoListinoBase();

            if(!StringUtils.isEmpty(tipologiaVariazionePrezzo)){
                boolean applyVariazione = true;
                CategoriaArticolo categoriaArticoloVariazione = createdListino.getCategoriaArticoloVariazione();
                Fornitore fornitoreVariazione = createdListino.getFornitoreVariazione();
                if(categoriaArticoloVariazione != null || fornitoreVariazione != null){
                    if(categoriaArticoloVariazione != null && !a.getCategoria().getId().equals(categoriaArticoloVariazione.getId())){
                        applyVariazione = false;
                    }
                    if(fornitoreVariazione != null && !a.getFornitore().getId().equals(fornitoreVariazione.getId())){
                        applyVariazione = false;
                    }
                }
                if(applyVariazione){
                    TipologiaListinoPrezzoVariazione tipologiaListinoPrezzoVariazione = TipologiaListinoPrezzoVariazione.valueOf(tipologiaVariazionePrezzo);
                    if(TipologiaListinoPrezzoVariazione.PERCENTUALE.equals(tipologiaListinoPrezzoVariazione)){
                        BigDecimal variazione = newPrezzo.multiply(BigDecimal.valueOf(variazionePrezzo/100));
                        newPrezzo = newPrezzo.add(variazione);
                    } else {
                        newPrezzo = newPrezzo.add(BigDecimal.valueOf(variazionePrezzo));
                    }
                }
            }

            listinoPrezzo.setPrezzo(newPrezzo);

            listiniPrezzi.add(listinoPrezzo);
        });
        if(!listiniPrezzi.isEmpty()){
            listinoPrezzoService.create(listiniPrezzi);
        }
        LOGGER.info("Successfully populated 'listiniPrezzi' for listino '{}'", createdListino.getId());

        return createdListino;
    }

    @Transactional
    public Listino update(Listino listino){
        LOGGER.info("Updating 'listino'");
        checkListinoTipologia(listino.getTipologia());
        if(listino.getTipologia().equals(TipologiaListino.BASE.name())){
            if(checkIfListinoBaseExists()){
                throw new ListinoBaseAlreadyExistingException();
            };
            if(!StringUtils.isEmpty(listino.getTipologiaVariazionePrezzo()) || listino.getVariazionePrezzo() != null){
                throw new ListinoBaseCannotHaveVariazionePrezzoException();
            }
            listino.setTipologiaVariazionePrezzo(null);
            listino.setVariazionePrezzo(null);
            listino.setCategoriaArticoloVariazione(null);
            listino.setFornitoreVariazione(null);
        }
        Listino listinoCurrent = listinoRepository.findById(listino.getId()).orElseThrow(ResourceNotFoundException::new);
        listino.setDataInserimento(listinoCurrent.getDataInserimento());
        Listino updatedListino = listinoRepository.save(listino);
        LOGGER.info("Updated 'listino' '{}'", updatedListino);

        LOGGER.info("Populating 'listiniPrezzi' for listino '{}'", updatedListino.getId());
        String tipologiaVariazionePrezzo = updatedListino.getTipologiaVariazionePrezzo();
        Float variazionePrezzo = updatedListino.getVariazionePrezzo();
        CategoriaArticolo categoriaArticoloVariazione = updatedListino.getCategoriaArticoloVariazione();
        Fornitore fornitoreVariazione = updatedListino.getFornitoreVariazione();

        List<ListinoPrezzo> listiniPrezziToUpdate = new ArrayList<>();
        if(categoriaArticoloVariazione != null && fornitoreVariazione != null){
            listiniPrezziToUpdate = listinoPrezzoService.getByListinoIdAndArticoloCategoriaIdAndFornitoreId(listino.getId(), categoriaArticoloVariazione.getId(), fornitoreVariazione.getId());
        } else if(categoriaArticoloVariazione != null && fornitoreVariazione == null){
            listiniPrezziToUpdate = listinoPrezzoService.getByListinoIdAndArticoloCategoriaId(listino.getId(), categoriaArticoloVariazione.getId());
        } else if(categoriaArticoloVariazione == null && fornitoreVariazione != null){
            listiniPrezziToUpdate = listinoPrezzoService.getByListinoIdAndArticoloFornitoreId(listino.getId(), fornitoreVariazione.getId());
        } else {
            listiniPrezziToUpdate = listinoPrezzoService.getByListinoId(listino.getId());
        }

        if(!listiniPrezziToUpdate.isEmpty()){
            insertOrUpdateListiniPrezzi(listiniPrezziToUpdate, tipologiaVariazionePrezzo, variazionePrezzo);
        }
        LOGGER.info("Populated 'listiniPrezzi' for listino '{}'", updatedListino.getId());


        return updatedListino;
    }

    @Transactional
    public void delete(Long listinoId){
        LOGGER.info("Deleting 'listiniPrezzi' of listino '{}'", listinoId);
        listinoPrezzoService.deleteByListinoId(listinoId);
        LOGGER.info("Deleted 'listiniPrezzi' of listino '{}'", listinoId);

        LOGGER.info("Deleting 'listino' '{}'", listinoId);
        listinoRepository.deleteById(listinoId);
        LOGGER.info("Deleted 'listino' '{}'", listinoId);
    }

    private boolean checkIfListinoBaseExists(){
        Optional<Listino> listino = listinoRepository.findFirstByTipologia(TIPOLOGIA_BASE);
        return listino.isPresent();
    }

    private void checkListinoTipologia(String tipologia){
        try {
            TipologiaListino.valueOf(tipologia);
        } catch (Exception e){
            throw new ListinoTipologiaNotAllowedException();
        }
    }

    private void insertOrUpdateListiniPrezzi(List<ListinoPrezzo> listiniPrezzi, String tipologiaVariazionePrezzo, Float variazionePrezzo){
        listiniPrezzi.forEach(lptu -> {
            Articolo articolo = lptu.getArticolo();
            BigDecimal newPrezzo = articolo.getPrezzoListinoBase();

            if(!StringUtils.isEmpty(tipologiaVariazionePrezzo)){
                TipologiaListinoPrezzoVariazione tipologiaListinoPrezzoVariazione = TipologiaListinoPrezzoVariazione.valueOf(tipologiaVariazionePrezzo);
                if(TipologiaListinoPrezzoVariazione.PERCENTUALE.equals(tipologiaListinoPrezzoVariazione)){
                    BigDecimal variazione = newPrezzo.multiply(BigDecimal.valueOf(variazionePrezzo/100));
                    newPrezzo = newPrezzo.add(variazione);
                } else {
                    newPrezzo = newPrezzo.add(BigDecimal.valueOf(variazionePrezzo));
                }
            }
            lptu.setPrezzo(newPrezzo);
        });
        listinoPrezzoService.bulkInsertOrUpdate(listiniPrezzi);
    }

}
