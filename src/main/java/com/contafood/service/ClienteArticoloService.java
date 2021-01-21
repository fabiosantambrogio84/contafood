package com.contafood.service;

import com.contafood.model.Articolo;
import com.contafood.model.ClienteArticolo;
import com.contafood.repository.ClienteArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
public class ClienteArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(ClienteArticoloService.class);

    private final ClienteArticoloRepository clienteArticoloRepository;
    private final ArticoloService articoloService;

    @Autowired
    public ClienteArticoloService(final ClienteArticoloRepository clienteArticoloRepository,
                                  final ArticoloService articoloService){
        this.clienteArticoloRepository = clienteArticoloRepository;
        this.articoloService = articoloService;
    }

    public Set<ClienteArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'cliente articoli'");
        Set<ClienteArticolo> clienteArticoli = clienteArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'cliente articoli'", clienteArticoli.size());
        return clienteArticoli;
    }

    public Set<ClienteArticolo> findByClienteId(Long idCliente){
        LOGGER.info("Retrieving the list of 'cliente articoli' of 'cliente' {}", idCliente);
        Set<ClienteArticolo> clienteArticoli = clienteArticoloRepository.findByClienteId(idCliente);
        LOGGER.info("Retrieved {} 'cliente articoli'", clienteArticoli.size());
        return clienteArticoli;
    }

    @Transactional
    public ClienteArticolo create(ClienteArticolo clienteArticolo){
        LOGGER.info("Creating 'cliente articolo'");
        ClienteArticolo createdClienteArticolo = clienteArticoloRepository.save(clienteArticolo);
        LOGGER.info("Created 'cliente articolo' '{}'", createdClienteArticolo);
        return createdClienteArticolo;
    }

    @Transactional
    public void deleteByClienteId(Long clienteId){
        LOGGER.info("Deleting 'cliente articolo' by 'cliente' '{}'", clienteId);
        clienteArticoloRepository.deleteByClienteId(clienteId);
        LOGGER.info("Deleted 'cliente articolo' by 'cliente' '{}'", clienteId);
    }

    public Articolo getArticolo(ClienteArticolo clienteArticolo){
        Long articoloId = clienteArticolo.getId().getArticoloId();
        return articoloService.getOne(articoloId);
    }

}
