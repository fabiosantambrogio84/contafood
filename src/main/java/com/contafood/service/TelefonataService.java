package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.OrdineCliente;
import com.contafood.model.OrdineClienteArticolo;
import com.contafood.model.OrdineClienteArticoloKey;
import com.contafood.model.Telefonata;
import com.contafood.repository.TelefonataRepository;
import com.contafood.util.enumeration.GiornoSettimana;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class TelefonataService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TelefonataService.class);

    private final TelefonataRepository telefonataRepository;

    private final OrdineClienteService ordineClienteService;

    @Autowired
    public TelefonataService(final TelefonataRepository telefonataRepository,
                             final OrdineClienteService ordineClienteService){
        this.telefonataRepository = telefonataRepository;
        this.ordineClienteService = ordineClienteService;
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

    public Telefonata patch(Map<String,Object> patchTelefonata){
        LOGGER.info("Patching 'telefonata'");

        Long idTelefonata = Long.valueOf((Integer) patchTelefonata.get("idTelefonata"));
        Boolean eseguito = (Boolean)patchTelefonata.get("eseguito");

        Telefonata telefonata = telefonataRepository.findById(idTelefonata).orElseThrow(ResourceNotFoundException::new);
        telefonata.setEseguito(eseguito);
        if(Boolean.TRUE.equals(eseguito)){
            telefonata.setDataEsecuzione(Timestamp.from(ZonedDateTime.now().toInstant()));
        }
        Telefonata patchedTelefonata = telefonataRepository.save(telefonata);

        LOGGER.info("Patched 'telefonata' '{}'", patchedTelefonata);
        return patchedTelefonata;
    }

    public void updateAfterDeletePuntoConsegna(Long puntoConsegnaId){
        LOGGER.info("Updating 'telefonate' after delete of 'puntoConsegna' '{}'", puntoConsegnaId);
        telefonataRepository.findByPuntoConsegnaId(puntoConsegnaId).stream().forEach(t -> {
            t.setPuntoConsegna(null);
            update(t);
        });
        LOGGER.info("Update 'telefonate' after delete of 'puntoConsegna' '{}'", puntoConsegnaId);
    }

    @Transactional
    public void delete(Long telefonataId){
        LOGGER.info("Deleting 'telefonata' '{}'", telefonataId);
        deleteOrdiniClienti(telefonataId);
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
        if(!telefonateIds.isEmpty()){
            for(Long idTelefonata : telefonateIds){
                deleteOrdiniClienti(idTelefonata);
            }
        }
        telefonataRepository.deleteByIdIn(telefonateIds);
        LOGGER.info("Bulk deleted all the specified 'telefonate");
    }

    public void bulkSetEseguito(List<Long> telefonateIds, Boolean eseguito){
        if(!telefonateIds.isEmpty()) {
            List<Telefonata> telefonate = new ArrayList<>();
            for(Long idTelefonata : telefonateIds){
                Optional<Telefonata> telefonataOptional = telefonataRepository.findById(idTelefonata);
                if(telefonataOptional.isPresent()){
                    Telefonata telefonata = telefonataOptional.get();
                    telefonata.setEseguito(eseguito);
                    telefonate.add(telefonata);
                }
            }
            if(!telefonate.isEmpty()){
                telefonataRepository.saveAll(telefonate);
            }
        }
    }

    private void deleteOrdiniClienti(Long idTelefonata){
        Set<OrdineCliente> ordiniClienti = ordineClienteService.getByIdTelefonata(idTelefonata);
        if(ordiniClienti != null && !ordiniClienti.isEmpty()){
            for(OrdineCliente ordineCliente : ordiniClienti){
                Map<String,Object> map = new HashMap();
                map.put("id", ordineCliente.getId().intValue());
                map.put("idTelefonata", null);

                ordineClienteService.patch(map);
            }
        }
    }
}
