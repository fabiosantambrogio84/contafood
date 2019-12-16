package com.contafood.service;

import com.contafood.exception.ListinoBaseAlreadyExistingException;
import com.contafood.exception.ListinoBaseCannotHaveVariazionePrezzoException;
import com.contafood.exception.ListinoTipologiaNotAllowedException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.ListinoRepository;
import com.contafood.util.TipologiaListino;
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
import java.util.stream.Collectors;

@Service
public class ListinoService {

    private static Logger LOGGER = LoggerFactory.getLogger(ListinoService.class);

    private final ListinoRepository listinoRepository;

    private final ArticoloService articoloService;

    private final ListinoPrezzoService listinoPrezzoService;

    private final ListinoPrezzoVariazioneService listinoPrezzoVariazioneService;

    private final static String TIPOLOGIA_BASE = "BASE";

    @Autowired
    public ListinoService(final ListinoRepository listinoRepository, final ArticoloService articoloService, final ListinoPrezzoService listinoPrezzoService, final ListinoPrezzoVariazioneService listinoPrezzoVariazioneService){
        this.listinoRepository = listinoRepository;
        this.articoloService = articoloService;
        this.listinoPrezzoService = listinoPrezzoService;
        this.listinoPrezzoVariazioneService = listinoPrezzoVariazioneService;
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

    public List<ListinoPrezzo> getListiniPrezziByListinoId(Long listinoId){
        LOGGER.info("Retrieving 'listiniPrezzi' of 'listino' '{}'", listinoId);
        List<ListinoPrezzo> listiniPrezzi = listinoPrezzoService.getByListinoId(listinoId);
        LOGGER.info("Retrieved '{}' 'listiniPrezzi'", listiniPrezzi.size());
        return listiniPrezzi;
    }

    @Transactional
    public Listino create(Listino listino){
        LOGGER.info("Creating 'listino'");
        checkListinoTipologia(listino.getTipologia());
        List<ListinoPrezzoVariazione> listiniPrezziVariazioni = listino.getListiniPrezziVariazioni();
        if(listino.getTipologia().equals(TipologiaListino.BASE.name())){
            if(checkIfListinoBaseExists()){
                throw new ListinoBaseAlreadyExistingException();
            }
            if(!StringUtils.isEmpty(listino.getTipologiaVariazionePrezzo()) || !listiniPrezziVariazioni.isEmpty()){
                throw new ListinoBaseCannotHaveVariazionePrezzoException();
            }
        }
        listino.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        if(!listiniPrezziVariazioni.isEmpty()){
            listinoPrezzoVariazioneService.create(listiniPrezziVariazioni);
        }
        Listino createdListino = listinoRepository.save(listino);
        LOGGER.info("Created 'listino' '{}'", createdListino);

        LOGGER.info("Populating 'listiniPrezzi' for listino '{}'", createdListino.getId());
        String tipologiaVariazionePrezzo = createdListino.getTipologiaVariazionePrezzo();
        Float variazionePrezzo = null;
        if(!listiniPrezziVariazioni.isEmpty()){
            variazionePrezzo = listiniPrezziVariazioni.stream().map(lpv -> lpv.getVariazionePrezzo()).findFirst().orElse(null);
        }
        final Float v  = variazionePrezzo;
        List<ListinoPrezzo> listiniPrezzi = new ArrayList<>();
        Set<Articolo> articoli = articoloService.getAll();
        articoli.forEach(a -> {
            ListinoPrezzo listinoPrezzo = new ListinoPrezzo();
            listinoPrezzo.setArticolo(a);
            listinoPrezzo.setListino(createdListino);
            listinoPrezzo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            BigDecimal newPrezzo = listinoPrezzoService.computePrezzoInListinoCreation(createdListino, a, tipologiaVariazionePrezzo, v);

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
        List<ListinoPrezzoVariazione> listiniPrezziVariazioni = listino.getListiniPrezziVariazioni();
        if(listino.getTipologia().equals(TipologiaListino.BASE.name())){
            if(checkIfListinoBaseExists()){
                throw new ListinoBaseAlreadyExistingException();
            }
            if(!StringUtils.isEmpty(listino.getTipologiaVariazionePrezzo()) || !listiniPrezziVariazioni.isEmpty()){
                throw new ListinoBaseCannotHaveVariazionePrezzoException();
            }
            listino.setTipologiaVariazionePrezzo(null);
            listino.setListiniPrezziVariazioni(null);
        }
        Listino listinoCurrent = listinoRepository.findById(listino.getId()).orElseThrow(ResourceNotFoundException::new);
        listino.setDataInserimento(listinoCurrent.getDataInserimento());
        if(!listiniPrezziVariazioni.isEmpty()){
            listinoPrezzoVariazioneService.deleteByListinoId(listino.getId());
            listinoPrezzoVariazioneService.create(listiniPrezziVariazioni);
        }
        Listino updatedListino = listinoRepository.save(listino);
        LOGGER.info("Updated 'listino' '{}'", updatedListino);

        LOGGER.info("Populating 'listiniPrezzi' for listino '{}'", updatedListino.getId());
        String tipologiaVariazionePrezzo = updatedListino.getTipologiaVariazionePrezzo();
        Float variazionePrezzo = null;
        List<Long> articoliVariazioni = null;
        Fornitore fornitoreVariazione = null;
        if(!listiniPrezziVariazioni.isEmpty()){
            variazionePrezzo = listiniPrezziVariazioni.stream().map(lpv -> lpv.getVariazionePrezzo()).findFirst().orElse(null);
            articoliVariazioni = listiniPrezziVariazioni.stream().filter(lpv -> lpv.getArticolo() != null).map(lpv -> lpv.getArticolo().getId()).collect(Collectors.toList());
            fornitoreVariazione = listiniPrezziVariazioni.stream().filter(lpv -> lpv.getFornitore() != null).map(lpv -> lpv.getFornitore()).findFirst().orElse(null);
        }

        List<ListinoPrezzo> listiniPrezziToUpdate;
        if(!articoliVariazioni.isEmpty() && fornitoreVariazione != null){
            listiniPrezziToUpdate = listinoPrezzoService.getByListinoIdAndArticoloIdInAndFornitoreId(listino.getId(), articoliVariazioni, fornitoreVariazione.getId());
        } else if(!articoliVariazioni.isEmpty()){
            listiniPrezziToUpdate = listinoPrezzoService.getByListinoIdAndArticoloIdIn(listino.getId(), articoliVariazioni);
        } else if(fornitoreVariazione != null){
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
        listiniPrezzi.forEach(lp -> {
            lp.setPrezzo(listinoPrezzoService.computePrezzo(lp.getArticolo(), tipologiaVariazionePrezzo, variazionePrezzo));
        });
        listinoPrezzoService.bulkInsertOrUpdate(listiniPrezzi);
    }

}
