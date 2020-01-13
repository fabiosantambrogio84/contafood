package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Sconto;
import com.contafood.model.Telefonata;
import com.contafood.repository.TelefonataRepository;
import com.contafood.util.GiornoSettimana;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TelefonataService {

    private static Logger LOGGER = LoggerFactory.getLogger(TelefonataService.class);

    private final TelefonataRepository telefonataRepository;

    @Autowired
    public TelefonataService(final TelefonataRepository telefonataRepository){
        this.telefonataRepository = telefonataRepository;
    }

    public List<Telefonata> getAll(){
        LOGGER.info("Retrieving the list of 'telefonate'");
        List<Telefonata> telefonate = telefonataRepository.findAll();
        LOGGER.info("Retrieved {} 'telefonate'", telefonate.size());
        return telefonate;
    }

    public Telefonata getOne(Long telefonataId){
        LOGGER.info("Retrieving 'telefonata' '{}'", telefonataId);
        Telefonata telefonata = telefonataRepository.findById(telefonataId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'telefonata' '{}'", telefonata);
        return telefonata;
    }

    public Telefonata create(Telefonata telefonata){
        LOGGER.info("Creating 'telefonata'");
        telefonata.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        if(telefonata.getGiornoOrdinale() == null){
            telefonata.setGiornoOrdinale(GiornoSettimana.getValueByLabel(telefonata.getGiorno()));
        }
        if(telefonata.getGiornoConsegnaOrdinale() == null){
            telefonata.setGiornoConsegnaOrdinale(GiornoSettimana.getValueByLabel(telefonata.getGiornoConsegna()));
        }
        Telefonata createdTelefonata = telefonataRepository.save(telefonata);
        LOGGER.info("Created 'telefonata' '{}'", createdTelefonata);

        return telefonata;
    }

    public Telefonata update(Telefonata telefonata){
        LOGGER.info("Updating 'telefonata'");
        Telefonata telefonataCurrent = telefonataRepository.findById(telefonata.getId()).orElseThrow(ResourceNotFoundException::new);
        telefonata.setDataInserimento(telefonataCurrent.getDataInserimento());
        Telefonata updatedTelefonata = telefonataRepository.save(telefonata);
        LOGGER.info("Updated 'telefonata' '{}'", updatedTelefonata);
        return updatedTelefonata;
    }

    public void updateAfterDeletePuntoConsegna(Long puntoConsegnaId){
        LOGGER.info("Updating 'telefonate' after delete of 'puntoConsegna' '{}'", puntoConsegnaId);
        telefonataRepository.findByPuntoConsegnaId(puntoConsegnaId).stream().forEach(t -> {
            t.setPuntoConsegna(null);
            update(t);
        });
        LOGGER.info("Update 'telefonate' after delete of 'puntoConsegna' '{}'", puntoConsegnaId);
    }

    public void delete(Long telefonataId){
        LOGGER.info("Deleting 'telefonata' '{}'", telefonataId);
        telefonataRepository.deleteById(telefonataId);
        LOGGER.info("Deleted 'telefonata' '{}'", telefonataId);
    }

    public void deleteByClienteId(Long clienteId){
        LOGGER.info("Deleting all 'telefonate' of 'cliente' '{}'", clienteId);
        telefonataRepository.deleteByClienteId(clienteId);
        LOGGER.info("Deleted all 'telefonate' of 'cliente' '{}'", clienteId);
    }

    @Transactional
    public void bulkDelete(List<Long> telefonateIds){
        LOGGER.info("Bulk deleting all the specified 'telefonate (number of elements to delete: {})'", telefonateIds.size());
        telefonataRepository.deleteByIdIn(telefonateIds);
        LOGGER.info("Bulk deleted all the specified 'telefonate");
    }
}
