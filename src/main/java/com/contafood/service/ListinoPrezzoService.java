package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.ListinoPrezzoRepository;
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
import java.util.Set;

@Service
public class ListinoPrezzoService {

    private static Logger LOGGER = LoggerFactory.getLogger(ListinoPrezzoService.class);

    private final ListinoPrezzoRepository listinoPrezzoRepository;

    @Autowired
    public ListinoPrezzoService(final ListinoPrezzoRepository listinoPrezzoRepository){
        this.listinoPrezzoRepository = listinoPrezzoRepository;
    }

    public List<ListinoPrezzo> getAll(){
        LOGGER.info("Retrieving the list of all 'listiniPrezzi'");
        List<ListinoPrezzo> listiniPrezzi = listinoPrezzoRepository.findAll();
        LOGGER.info("Retrieved {} 'listiniPrezzi'", listiniPrezzi.size());
        return listiniPrezzi;
    }

    public List<ListinoPrezzo> getByListinoId(Long idListino){
        LOGGER.info("Retrieving the list of 'listiniPrezzi' of listino '{}'", idListino);
        List<ListinoPrezzo> listiniPrezzi = listinoPrezzoRepository.findByListinoId(idListino);
        LOGGER.info("Retrieved {} 'listiniPrezzi'", listiniPrezzi.size());
        return listiniPrezzi;
    }

    public List<ListinoPrezzo> getByArticoloId(Long idArticolo){
        LOGGER.info("Retrieving the list of 'listiniPrezzi' of articolo '{}'", idArticolo);
        List<ListinoPrezzo> listiniPrezzi = listinoPrezzoRepository.findByArticoloId(idArticolo);
        LOGGER.info("Retrieved {} 'listiniPrezzi'", listiniPrezzi.size());
        return listiniPrezzi;
    }

    public List<ListinoPrezzo> getByListinoIdAndArticoloCategoriaId(Long idListino, Long idCategoriaArticolo){
        LOGGER.info("Retrieving the list of 'listiniPrezzi' of listino '{}' of articoli with categoria '{}'", idListino, idCategoriaArticolo);
        List<ListinoPrezzo> listiniPrezzi = listinoPrezzoRepository.findByListinoIdAndArticoloCategoriaId(idListino, idCategoriaArticolo);
        LOGGER.info("Retrieved {} 'listiniPrezzi'", listiniPrezzi.size());
        return listiniPrezzi;
    }

    public List<ListinoPrezzo> getByListinoIdAndArticoloFornitoreId(Long idListino, Long idFornitore){
        LOGGER.info("Retrieving the list of 'listiniPrezzi' of listino '{}' of articoli with fornitore '{}'", idListino, idFornitore);
        List<ListinoPrezzo> listiniPrezzi = listinoPrezzoRepository.findByListinoIdAndArticoloFornitoreId(idListino, idFornitore);
        LOGGER.info("Retrieved {} 'listiniPrezzi'", listiniPrezzi.size());
        return listiniPrezzi;
    }

    public List<ListinoPrezzo> getByListinoIdAndArticoloCategoriaIdAndFornitoreId(Long idListino, Long idCategoriaArticolo, Long idFornitore){
        LOGGER.info("Retrieving the list of 'listiniPrezzi' of listino '{}' of articoli with categoria '{}' and fornitore '{}'", idListino, idCategoriaArticolo, idFornitore);
        List<ListinoPrezzo> listiniPrezzi = listinoPrezzoRepository.findByListinoIdAndArticoloCategoriaIdAndArticoloFornitoreId(idListino, idCategoriaArticolo, idFornitore);
        LOGGER.info("Retrieved {} 'listiniPrezzi'", listiniPrezzi.size());
        return listiniPrezzi;
    }


    public ListinoPrezzo getOne(Long listinoPrezzoId){
        LOGGER.info("Retrieving 'listinoPrezzo' '{}'", listinoPrezzoId);
        ListinoPrezzo listinoPrezzo = listinoPrezzoRepository.findById(listinoPrezzoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'listinoPrezzo' '{}'", listinoPrezzo);
        return listinoPrezzo;
    }

    public List<ListinoPrezzo> create(List<ListinoPrezzo> listiniPrezzi){
        LOGGER.info("Creating 'listiniPrezzi'");
        listiniPrezzi.forEach(lp -> {
            lp.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            ListinoPrezzo createdListinoPrezzo = listinoPrezzoRepository.save(lp);
            LOGGER.info("Created 'listinoPrezzo' '{}'", createdListinoPrezzo);
        });
        return listiniPrezzi;

    }

    public ListinoPrezzo update(ListinoPrezzo listinoPrezzo){
        LOGGER.info("Updating 'listinoPrezzo'");
        ListinoPrezzo listinoPrezzoCurrent = listinoPrezzoRepository.findById(listinoPrezzo.getId()).orElseThrow(ResourceNotFoundException::new);
        listinoPrezzo.setDataInserimento(listinoPrezzoCurrent.getDataInserimento());
        ListinoPrezzo updatedListinoPrezzo = listinoPrezzoRepository.save(listinoPrezzo);
        LOGGER.info("Updated 'listinoPrezzo' '{}'", updatedListinoPrezzo);
        return updatedListinoPrezzo;
    }

    public List<ListinoPrezzo> bulkInsertOrUpdate(List<ListinoPrezzo> listiniPrezzi){
        LOGGER.info("Inserting/updating 'listiniPrezzi'");
        listinoPrezzoRepository.saveAll(listiniPrezzi);
        LOGGER.info("Inserted/updated 'listiniPrezzi' '{}'", listiniPrezzi);
        return listiniPrezzi;
    }

    public void delete(Long listinoPrezzoId){
        LOGGER.info("Deleting 'listinoPrezzo' '{}'", listinoPrezzoId);
        listinoPrezzoRepository.deleteById(listinoPrezzoId);
        LOGGER.info("Deleted 'listinoPrezzo' '{}'", listinoPrezzoId);
    }

    public void deleteByListinoId(Long idListino){
        LOGGER.info("Deleting 'listiniPrezzi' of listino '{}'", idListino);
        listinoPrezzoRepository.deleteByListinoId(idListino);
        LOGGER.info("Deleted 'listiniPrezzi' of listino '{}'", idListino);
    }

    public void deleteByArticoloId(Long idArticolo){
        LOGGER.info("Deleting 'listiniPrezzi' of articolo '{}'", idArticolo);
        listinoPrezzoRepository.deleteByArticoloId(idArticolo);
        LOGGER.info("Deleted 'listiniPrezzi' of articolo '{}'", idArticolo);
    }

    public BigDecimal computePrezzo(Articolo articolo, String tipologiaVariazionePrezzo, Float variazionePrezzo){
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
        return newPrezzo;
    }

    public BigDecimal computePrezzoInListinoCreation(Listino listino, Articolo articolo, String tipologiaVariazionePrezzo, Float variazionePrezzo){
        BigDecimal newPrezzo = articolo.getPrezzoListinoBase();
        if(!StringUtils.isEmpty(tipologiaVariazionePrezzo)){
            boolean applyVariazione = true;
            CategoriaArticolo categoriaArticoloVariazione = listino.getCategoriaArticoloVariazione();
            Fornitore fornitoreVariazione = listino.getFornitoreVariazione();
            if(categoriaArticoloVariazione != null || fornitoreVariazione != null){
                if(categoriaArticoloVariazione != null && !articolo.getCategoria().getId().equals(categoriaArticoloVariazione.getId())){
                    applyVariazione = false;
                }
                if(fornitoreVariazione != null && !articolo.getFornitore().getId().equals(fornitoreVariazione.getId())){
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
        return newPrezzo;
    }

    public void computeListiniPrezziForArticolo(Articolo articolo){
        LOGGER.info("Recomputing 'listiniPrezzi' of articolo '{}'", articolo.getId());
        List<ListinoPrezzo> listiniPrezzi = listinoPrezzoRepository.findByArticoloId(articolo.getId());
        listiniPrezzi.forEach(lp -> {
            Listino listino = lp.getListino();
            lp.setPrezzo(computePrezzo(articolo, listino.getTipologiaVariazionePrezzo(), listino.getVariazionePrezzo()));
        });
        bulkInsertOrUpdate(listiniPrezzi);
        LOGGER.info("Recomputed 'listiniPrezzi' of articolo '{}'", articolo.getId());
    }
}
