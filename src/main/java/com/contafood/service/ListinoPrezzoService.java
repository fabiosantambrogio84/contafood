package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.ListinoAssociato;
import com.contafood.model.ListinoPrezzo;
import com.contafood.model.Sconto;
import com.contafood.repository.ListinoPrezzoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
