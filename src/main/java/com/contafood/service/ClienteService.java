package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Cliente;
import com.contafood.model.Fornitore;
import com.contafood.model.ListinoAssociato;
import com.contafood.model.PuntoConsegna;
import com.contafood.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ClienteService {

    private static Logger LOGGER = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    private final PuntoConsegnaService puntoConsegnaService;

    private final ListinoAssociatoService listinoAssociatoService;

    @Autowired
    public ClienteService(final ClienteRepository clienteRepository, final PuntoConsegnaService puntoConsegnaService, final ListinoAssociatoService listinoAssociatoService){
        this.clienteRepository = clienteRepository;
        this.puntoConsegnaService = puntoConsegnaService;
        this.listinoAssociatoService = listinoAssociatoService;
    }

    public Set<Cliente> getAll(){
        LOGGER.info("Retrieving the list of 'clienti'");
        Set<Cliente> clienti = clienteRepository.findAll();
        LOGGER.info("Retrieved {} 'clienti'", clienti.size());
        return clienti;
    }

    public Set<Cliente> getAllWithBloccaDdt(Boolean bloccaDdt){
        LOGGER.info("Retrieving the list of 'clienti' with bloccaDdt {}", bloccaDdt);
        Set<Cliente> clienti = clienteRepository.findByBloccaDdt(bloccaDdt);
        LOGGER.info("Retrieved {} 'clienti'", clienti.size());
        return clienti;
    }

    public Cliente getOne(Long clienteId){
        LOGGER.info("Retrieving 'cliente' '{}'", clienteId);
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'cliente' '{}'", cliente);
        return cliente;
    }

    public Cliente create(Cliente cliente){
        LOGGER.info("Creating 'cliente'");
        Cliente createdCliente = clienteRepository.save(cliente);
        createdCliente.setCodice(createdCliente.getId().intValue());
        clienteRepository.save(createdCliente);
        LOGGER.info("Created 'cliente' '{}'", createdCliente);
        return createdCliente;
    }

    public Cliente update(Cliente cliente){
        LOGGER.info("Updating 'cliente'");
        Cliente updatedCliente = clienteRepository.save(cliente);
        LOGGER.info("Updated 'cliente' '{}'", updatedCliente);
        return updatedCliente;
    }

    public void delete(Long clienteId){
        LOGGER.info("Deleting 'cliente' '{}'", clienteId);
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
