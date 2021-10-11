package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.OrdineClienteArticolo;
import com.contafood.model.OrdineClienteArticoloKey;
import com.contafood.repository.OrdineClienteArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

@Service
public class OrdineClienteArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(OrdineClienteArticoloService.class);

    private final OrdineClienteArticoloRepository ordineClienteArticoloRepository;

    @Autowired
    public OrdineClienteArticoloService(final OrdineClienteArticoloRepository ordineClienteArticoloRepository){
        this.ordineClienteArticoloRepository = ordineClienteArticoloRepository;
    }

    public Set<OrdineClienteArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'ordine cliente articoli'");
        Set<OrdineClienteArticolo> ordineClienteArticoli = ordineClienteArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'ordine cliente articoli'", ordineClienteArticoli.size());
        return ordineClienteArticoli;
    }

    public OrdineClienteArticolo create(OrdineClienteArticolo ordineClienteArticolo){
        LOGGER.info("Creating 'ordine cliente articolo'");
        ordineClienteArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ordineClienteArticolo.setNumeroPezziDaEvadere(ordineClienteArticolo.getNumeroPezziOrdinati());
        OrdineClienteArticolo createdOrdineClienteArticolo = ordineClienteArticoloRepository.save(ordineClienteArticolo);
        LOGGER.info("Created 'ordine cliente articolo' '{}'", createdOrdineClienteArticolo);
        return createdOrdineClienteArticolo;
    }

    public OrdineClienteArticolo update(OrdineClienteArticolo ordineClienteArticolo){
        LOGGER.info("Updating 'ordine cliente articolo'");
        OrdineClienteArticolo ordineClienteArticoloCurrent = ordineClienteArticoloRepository.findById(ordineClienteArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        ordineClienteArticolo.setDataInserimento(ordineClienteArticoloCurrent.getDataInserimento());
        ordineClienteArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ordineClienteArticolo.setNumeroPezziDaEvadere(ordineClienteArticolo.getNumeroPezziOrdinati());
        OrdineClienteArticolo updatedOrdineClienteArticolo = ordineClienteArticoloRepository.save(ordineClienteArticolo);
        LOGGER.info("Updated 'ordine cliente articolo' '{}'", updatedOrdineClienteArticolo);
        return updatedOrdineClienteArticolo;
    }

    public OrdineClienteArticolo save(OrdineClienteArticolo ordineClienteArticolo){
        LOGGER.info("Saving 'ordine cliente articolo'");
        ordineClienteArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        OrdineClienteArticolo savedOrdineClienteArticolo = ordineClienteArticoloRepository.save(ordineClienteArticolo);
        LOGGER.info("Saved 'ordine cliente articolo' '{}'", savedOrdineClienteArticolo);
        return savedOrdineClienteArticolo;
    }

    public void saveAll(Set<OrdineClienteArticolo> ordiniClientiArticoli){
        LOGGER.info("Saving set of 'ordine cliente articolo'");
        ordineClienteArticoloRepository.saveAll(ordiniClientiArticoli);
        LOGGER.info("Saved set of 'ordine cliente articolo'");
    }

    //@Transactional
    public OrdineClienteArticolo patch(Map<String,Object> patchOrdineClienteArticolo){
        LOGGER.info("Patching 'ordineClienteArticolo'");

        Long idOrdineCliente = Long.valueOf((Integer) patchOrdineClienteArticolo.get("idOrdineCliente"));
        Long idArticolo = Long.valueOf((Integer) patchOrdineClienteArticolo.get("idArticolo"));
        OrdineClienteArticoloKey ordineClienteArticoloKey = new OrdineClienteArticoloKey();
        ordineClienteArticoloKey.setOrdineClienteId(idOrdineCliente);
        ordineClienteArticoloKey.setArticoloId(idArticolo);

        OrdineClienteArticolo ordineClienteArticolo = getOrdineClienteArticolo(ordineClienteArticoloKey);
        patchOrdineClienteArticolo.forEach((key, value) -> {
            if(key.equals("idOrdineFornitore")){
                ordineClienteArticolo.setOrdineFornitoreId(Long.valueOf((Integer)value));
            }
        });
        OrdineClienteArticolo patchedOrdineClienteArticolo = ordineClienteArticoloRepository.save(ordineClienteArticolo);

        LOGGER.info("Patched 'ordineClienteArticolo' '{}'", patchedOrdineClienteArticolo);
        return patchedOrdineClienteArticolo;
    }

    public OrdineClienteArticolo getOrdineClienteArticolo(OrdineClienteArticoloKey ordineClienteArticoloKey){
        LOGGER.info("Retrieving 'ordine cliente articolo' by 'key' '{}'", ordineClienteArticoloKey);
        OrdineClienteArticolo ordineClienteArticolo = ordineClienteArticoloRepository.findById(ordineClienteArticoloKey).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'ordine cliente articolo' by 'key' '{}'", ordineClienteArticoloKey);
        return ordineClienteArticolo;
    }

    public Set<OrdineClienteArticolo> getOrdineClienteArticoli(Long idOrdineCliente){
        LOGGER.info("Retrieving 'ordine cliente articoli' of 'OrdineCliente' {}", idOrdineCliente);
        Set<OrdineClienteArticolo> ordineClienteArticoli = ordineClienteArticoloRepository.findByOrdineClienteId(idOrdineCliente);
        LOGGER.info("Retrieved {} 'ordine cliente articoli' of 'OrdineCliente' {}", ordineClienteArticoli.size(), idOrdineCliente);
        return ordineClienteArticoli;
    }

    public Set<OrdineClienteArticolo> getOrdineClienteArticoliByIdOrdineFornitore(Long idOrdineFornitore){
        LOGGER.info("Retrieving 'ordine cliente articoli' by 'IdOrdineFornitore' {}", idOrdineFornitore);
        Set<OrdineClienteArticolo> ordineClienteArticoli = ordineClienteArticoloRepository.findByOrdineFornitoreId(idOrdineFornitore);
        LOGGER.info("Retrieved {} 'ordine cliente articoli' by 'IdOrdineFornitore' {}", ordineClienteArticoli.size(), idOrdineFornitore);
        return ordineClienteArticoli;
    }

    public void deleteByOrdineClienteId(Long ordineClienteId){
        LOGGER.info("Deleting 'ordine cliente articolo' by 'ordineCliente' '{}'", ordineClienteId);
        ordineClienteArticoloRepository.deleteByOrdineClienteId(ordineClienteId);
        LOGGER.info("Deleted 'ordine cliente articolo' by 'ordineCliente' '{}'", ordineClienteId);
    }

}
