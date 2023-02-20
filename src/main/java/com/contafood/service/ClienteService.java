package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Cliente;
import com.contafood.model.ClienteArticolo;
import com.contafood.model.ListinoAssociato;
import com.contafood.model.PuntoConsegna;
import com.contafood.repository.ClienteRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class ClienteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    private final PuntoConsegnaService puntoConsegnaService;

    private final ListinoAssociatoService listinoAssociatoService;

    private final TelefonataService telefonataService;

    private final ClienteArticoloService clienteArticoloService;

    @Autowired
    public ClienteService(final ClienteRepository clienteRepository,
                          final PuntoConsegnaService puntoConsegnaService,
                          final ListinoAssociatoService listinoAssociatoService,
                          final TelefonataService telefonataService,
                          final ClienteArticoloService clienteArticoloService){
        this.clienteRepository = clienteRepository;
        this.puntoConsegnaService = puntoConsegnaService;
        this.listinoAssociatoService = listinoAssociatoService;
        this.telefonataService = telefonataService;
        this.clienteArticoloService = clienteArticoloService;
    }

    public Set<Cliente> getAll(){
        LOGGER.info("Retrieving the list of 'clienti'");
        Set<Cliente> clienti = clienteRepository.findAllByOrderByRagioneSocialeAsc();
        LOGGER.info("Retrieved {} 'clienti'", clienti.size());
        return clienti;
    }

    /*public Set<Cliente> getAllWithBloccaDdt(Boolean bloccaDdt){
        LOGGER.info("Retrieving the list of 'clienti' with bloccaDdt {}", bloccaDdt);
        Set<Cliente> clienti = clienteRepository.findByBloccaDdt(bloccaDdt);
        LOGGER.info("Retrieved {} 'clienti'", clienti.size());
        return clienti;
    }*/

    public Cliente getOne(Long clienteId){
        LOGGER.info("Retrieving 'cliente' '{}'", clienteId);
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'cliente' '{}'", cliente);
        return cliente;
    }

    @Transactional
    public Cliente create(Cliente cliente){
        LOGGER.info("Creating 'cliente'");
        cliente.setCognome(cliente.getCognome().trim());
        cliente.setNome(cliente.getNome().trim());
        cliente.setRagioneSociale(cliente.getRagioneSociale().trim());
        cliente.setRagioneSociale2(cliente.getRagioneSociale2().trim());
        cliente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Cliente createdCliente = clienteRepository.save(cliente);
        createdCliente.setCodice(createdCliente.getId().intValue());

        // create 'cliente-articoli'
        createdCliente.getClienteArticoli().stream().forEach(ca -> {
            ca.getId().setClienteId(createdCliente.getId());
            ca.getId().setUuid(UUID.randomUUID().toString());
            clienteArticoloService.create(ca);
        });

        clienteRepository.save(createdCliente);
        LOGGER.info("Created 'cliente' '{}'", createdCliente);
        return createdCliente;
    }

    @Transactional
    public Cliente update(Cliente cliente){
        LOGGER.info("Updating 'cliente'");

        // delete current ClienteArticoli set
        Set<ClienteArticolo> clienteArticoli = cliente.getClienteArticoli();
        cliente.setClienteArticoli(new HashSet<>());
        clienteArticoloService.deleteByClienteId(cliente.getId());

        Cliente currentCliente = clienteRepository.findById(cliente.getId()).orElseThrow(ResourceNotFoundException::new);
        cliente.setDataInserimento(currentCliente.getDataInserimento());
        Cliente updatedCliente = clienteRepository.save(cliente);

        clienteArticoli.stream().forEach(ca -> {
            ca.getId().setClienteId(updatedCliente.getId());
            ca.getId().setUuid(UUID.randomUUID().toString());
            clienteArticoloService.create(ca);
        });

        LOGGER.info("Updated 'cliente' '{}'", updatedCliente);
        return updatedCliente;
    }

    @Transactional
    public Cliente patch(Map<String,Object> patchCliente){
        LOGGER.info("Patching 'cliente'");

        Long id = Long.valueOf((Integer) patchCliente.get("id"));
        Cliente cliente = clienteRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        patchCliente.forEach((key, value) -> {
            if(key.equals("id")){
                cliente.setId(Long.valueOf((Integer)value));
            } else if(key.equals("noteDocumenti")){
                String noteDocumenti = !StringUtils.isEmpty((String)value) ? (String)value : null;
                cliente.setNoteDocumenti(noteDocumenti);
            }
        });
        Cliente patchedCliente = clienteRepository.save(cliente);

        LOGGER.info("Patched 'cliente' '{}'", patchedCliente);
        return patchedCliente;
    }

    @Transactional
    public void delete(Long clienteId){
        LOGGER.info("Deleting 'cliente' '{}'", clienteId);
        listinoAssociatoService.deleteByClienteId(clienteId);
        puntoConsegnaService.deleteByClienteId(clienteId);
        clienteArticoloService.deleteByClienteId(clienteId);
        telefonataService.deleteByClienteId(clienteId);
        clienteRepository.deleteById(clienteId);
        LOGGER.info("Deleted 'cliente' '{}'", clienteId);
    }

    public List<PuntoConsegna> getPuntiConsegna(Long clienteId){
        LOGGER.info("Retrieving the list of 'puntiConsegna' of the 'cliente' '{}'", clienteId);
        List<PuntoConsegna> puntiConsegna = puntoConsegnaService.getByClienteId(clienteId);
        LOGGER.info("Retrieved {} 'puntiConsegna'", puntiConsegna.size());
        return puntiConsegna;
    }

    public List<ListinoAssociato> getListiniAssociati(Long clienteId){
        LOGGER.info("Retrieving the list of 'listiniAssociati' of the 'cliente' '{}'", clienteId);
        List<ListinoAssociato> listiniAssociati = listinoAssociatoService.getByClienteId(clienteId);
        LOGGER.info("Retrieved {} 'listiniAssociati'", listiniAssociati.size());
        return listiniAssociati;
    }



}
