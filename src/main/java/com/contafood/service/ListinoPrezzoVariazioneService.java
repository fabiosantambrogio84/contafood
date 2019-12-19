package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.ListinoPrezzoVariazioneRepository;
import com.contafood.util.TipologiaListinoPrezzoVariazione;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ListinoPrezzoVariazioneService {

    private static Logger LOGGER = LoggerFactory.getLogger(ListinoPrezzoVariazioneService.class);

    private final ListinoPrezzoVariazioneRepository listinoPrezzoVariazioneRepository;

    @Autowired
    public ListinoPrezzoVariazioneService(final ListinoPrezzoVariazioneRepository listinoPrezzoVariazioneRepository){
        this.listinoPrezzoVariazioneRepository = listinoPrezzoVariazioneRepository;
    }

    public List<ListinoPrezzoVariazione> getAll(){
        LOGGER.info("Retrieving the list of all 'listiniPrezziVariazioni'");
        List<ListinoPrezzoVariazione> listiniPrezziVariazioni = listinoPrezzoVariazioneRepository.findAll();
        LOGGER.info("Retrieved {} 'listiniPrezziVariazioni'", listiniPrezziVariazioni.size());
        return listiniPrezziVariazioni;
    }

    public List<ListinoPrezzoVariazione> getByListinoId(Long idListino){
        LOGGER.info("Retrieving the list of 'listiniPrezziVariazioni' of listino '{}'", idListino);
        List<ListinoPrezzoVariazione> listiniPrezziVariazioni = listinoPrezzoVariazioneRepository.findByListinoId(idListino);
        LOGGER.info("Retrieved {} 'listiniPrezziVariazioni'", listiniPrezziVariazioni.size());
        return listiniPrezziVariazioni;
    }
    /*
    public List<ListinoPrezzoVariazione> getByArticoloId(Long idArticolo){
        LOGGER.info("Retrieving the list of 'listiniPrezziVariazioni' of articolo '{}'", idArticolo);
        List<ListinoPrezzoVariazione> listiniPrezziVariazioni = listinoPrezzoVariazioneRepository.findByArticoloId(idArticolo);
        LOGGER.info("Retrieved {} 'listiniPrezziVariazioni'", listiniPrezziVariazioni.size());
        return listiniPrezziVariazioni;
    }

    public List<ListinoPrezzoVariazione> getByListinoIdAndArticoloId(Long idListino, Long idArticolo){
        LOGGER.info("Retrieving the list of 'listiniPrezziVariazioni' of listino '{}' of articoli with id '{}'", idListino, idArticolo);
        List<ListinoPrezzoVariazione> listiniPrezziVariazioni = listinoPrezzoVariazioneRepository.findByListinoIdAndArticoloId(idListino, idArticolo);
        LOGGER.info("Retrieved {} 'listiniPrezziVariazioni'", listiniPrezziVariazioni.size());
        return listiniPrezziVariazioni;
    }

    public List<ListinoPrezzoVariazione> getByListinoIdAndArticoloFornitoreId(Long idListino, Long idFornitore){
        LOGGER.info("Retrieving the list of 'listiniPrezziVariazioni' of listino '{}' of articoli with fornitore '{}'", idListino, idFornitore);
        List<ListinoPrezzoVariazione> listiniPrezziVariazioni = listinoPrezzoVariazioneRepository.findByListinoIdAndArticoloFornitoreId(idListino, idFornitore);
        LOGGER.info("Retrieved {} 'listiniPrezziVariazioni'", listiniPrezziVariazioni.size());
        return listiniPrezziVariazioni;
    }

    public List<ListinoPrezzoVariazione> getByListinoIdAndArticoloIdAndFornitoreId(Long idListino, Long idArticolo, Long idFornitore){
        LOGGER.info("Retrieving the list of 'listiniPrezziVariazioni' of listino '{}' of articoli with id '{}' and fornitore '{}'", idListino, idArticolo, idFornitore);
        List<ListinoPrezzoVariazione> listiniPrezziVariazioni = listinoPrezzoVariazioneRepository.findByListinoIdAndArticoloIdAndArticoloFornitoreId(idListino, idArticolo, idFornitore);
        LOGGER.info("Retrieved {} 'listiniPrezziVariazioni'", listiniPrezziVariazioni.size());
        return listiniPrezziVariazioni;
    }
    */

    public ListinoPrezzoVariazione getOne(Long listinoPrezzoVariazioneId){
        LOGGER.info("Retrieving 'listinoPrezzoVariazione' '{}'", listinoPrezzoVariazioneId);
        ListinoPrezzoVariazione listinoPrezzoVariazione = listinoPrezzoVariazioneRepository.findById(listinoPrezzoVariazioneId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'listinoPrezzoVariazione' '{}'", listinoPrezzoVariazione);
        return listinoPrezzoVariazione;
    }

    public List<ListinoPrezzoVariazione> create(List<ListinoPrezzoVariazione> listiniPrezziVariazioni){
        LOGGER.info("Creating 'listiniPrezziVariazioni'");
        listiniPrezziVariazioni.forEach(lp -> {
            lp.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            lp.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
            ListinoPrezzoVariazione createdListinoPrezzoVariazione = listinoPrezzoVariazioneRepository.save(lp);
            LOGGER.info("Created 'listinoPrezzoVariazione' '{}'", createdListinoPrezzoVariazione);
        });
        return listiniPrezziVariazioni;
    }

    public void delete(Long listinoPrezzoVariazioneId){
        LOGGER.info("Deleting 'listinoPrezzoVariazione' '{}'", listinoPrezzoVariazioneId);
        listinoPrezzoVariazioneRepository.deleteById(listinoPrezzoVariazioneId);
        LOGGER.info("Deleted 'listinoPrezzoVariazione' '{}'", listinoPrezzoVariazioneId);
    }

    public void deleteByListinoId(Long idListino){
        LOGGER.info("Deleting 'listiniPrezziVariazioni' of listino '{}'", idListino);
        listinoPrezzoVariazioneRepository.deleteByListinoId(idListino);
        LOGGER.info("Deleted 'listiniPrezziVariazioni' of listino '{}'", idListino);
    }

    public void deleteByArticoloId(Long idArticolo){
        LOGGER.info("Deleting 'listiniPrezziVariazioni' of articolo '{}'", idArticolo);
        listinoPrezzoVariazioneRepository.deleteByArticoloId(idArticolo);
        LOGGER.info("Deleted 'listiniPrezziVariazioni' of articolo '{}'", idArticolo);
    }
}
