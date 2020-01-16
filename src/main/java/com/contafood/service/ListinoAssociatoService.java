package com.contafood.service;

import com.contafood.exception.ListinoAssociatoAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.ListinoAssociato;
import com.contafood.model.OrdineCliente;
import com.contafood.repository.ListinoAssociatoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ListinoAssociatoService {

    private static Logger LOGGER = LoggerFactory.getLogger(ListinoAssociatoService.class);

    private final ListinoAssociatoRepository listinoAssociatoRepository;

    private final FornitoreService fornitoreService;

    private final ListinoService listinoService;

    @Autowired
    public ListinoAssociatoService(final ListinoAssociatoRepository listinoAssociatoRepository, final FornitoreService fornitoreService, final ListinoService listinoService){
        this.listinoAssociatoRepository = listinoAssociatoRepository;
        this.fornitoreService = fornitoreService;
        this.listinoService = listinoService;
    }

    public Set<ListinoAssociato> getAll(){
        LOGGER.info("Retrieving the list of 'listiniAssociati'");
        Set<ListinoAssociato> listiniAssociati = listinoAssociatoRepository.findAll();
        LOGGER.info("Retrieved {} 'listiniAssociati'", listiniAssociati.size());
        return listiniAssociati;
    }

    public ListinoAssociato getOne(Long listinoAssociatoId){
        LOGGER.info("Retrieving 'listinoAssociato' '{}'", listinoAssociatoId);
        ListinoAssociato listinoAssociato = listinoAssociatoRepository.findById(listinoAssociatoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'listinoAssociato' '{}'", listinoAssociato);
        return listinoAssociato;
    }

    public List<ListinoAssociato> create(List<ListinoAssociato> listiniAssociati){
        LOGGER.info("Creating a list of 'listinoAssociato'");
        listiniAssociati.stream().forEach(la -> {
            // Check if for the Cliente the Fornitore is already associated to the Listino
            Long idCliente = null;
            if(la.getCliente() != null){
                idCliente = la.getCliente().getId();
            }
            Long idFornitore = null;
            if(la.getFornitore() != null){
                idFornitore = la.getFornitore().getId();
            }
            Long idListino = null;
            if(la.getListino() != null){
                idListino = la.getListino().getId();
            }
            Optional<ListinoAssociato> alreadyListinoAssociato = listinoAssociatoRepository.findByClienteIdAndFornitoreIdAndListinoId(idCliente,idFornitore,idListino);
            if(alreadyListinoAssociato.isPresent()){
                String fornitore = fornitoreService.getOne(idFornitore).getRagioneSociale();
                String listino = listinoService.getOne(idListino).getNome();
                throw new ListinoAssociatoAlreadyExistingException(fornitore, listino);
            } else {
                la.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
                ListinoAssociato createdListinoAssociato = listinoAssociatoRepository.save(la);
                LOGGER.info("Created 'listinoAssociato' '{}'", createdListinoAssociato);
            }
        });
        return listiniAssociati;
    }

    public ListinoAssociato update(ListinoAssociato listinoAssociato){
        LOGGER.info("Updating 'listinoAssociato'");
        ListinoAssociato listinoAssociatoCurrent = listinoAssociatoRepository.findById(listinoAssociato.getId()).orElseThrow(ResourceNotFoundException::new);
        listinoAssociato.setDataInserimento(listinoAssociatoCurrent.getDataInserimento());
        ListinoAssociato updatedListinoAssociato = listinoAssociatoRepository.save(listinoAssociato);
        LOGGER.info("Updated 'listinoAssociato' '{}'", updatedListinoAssociato);
        return updatedListinoAssociato;
    }

    public void delete(Long listinoAssociatoId){
        LOGGER.info("Deleting 'listinoAssociato' '{}'", listinoAssociatoId);
        listinoAssociatoRepository.deleteById(listinoAssociatoId);
        LOGGER.info("Deleted 'listinoAssociato' '{}'", listinoAssociatoId);
    }

    public List<ListinoAssociato> getByClienteId(Long clienteId){
        LOGGER.info("Retrieving the list of 'listiniAssociati' for 'cliente' '{}'", clienteId);
        List<ListinoAssociato> listiniAssociati = listinoAssociatoRepository.findByClienteId(clienteId);
        LOGGER.info("Retrieved {} 'listiniAssociati'", listiniAssociati.size());
        return listiniAssociati;
    }

    public List<ListinoAssociato> getByFornitoreId(Long fornitoreId){
        LOGGER.info("Retrieving the list of 'listiniAssociati' for 'fornitore' '{}'", fornitoreId);
        List<ListinoAssociato> listiniAssociati = listinoAssociatoRepository.findByFornitoreId(fornitoreId);
        LOGGER.info("Retrieved {} 'listiniAssociati'", listiniAssociati.size());
        return listiniAssociati;
    }
}
