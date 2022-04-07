package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Cliente;
import com.contafood.model.Listino;
import com.contafood.model.ListinoAssociato;
import com.contafood.model.PuntoConsegna;
import com.contafood.service.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/clienti")
public class ClienteController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(final ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public List<Cliente> getAll(@RequestParam(required = false) Boolean bloccaDdt,
                                @RequestParam(required = false) Boolean privato,
                                @RequestParam(required = false) Integer idListino) {
        LOGGER.info("Performing GET request for retrieving list of 'clienti'");
        LOGGER.info("Request params: bloccaDdt {}, privato {}, idListino {}", bloccaDdt, privato, idListino);

        Predicate<Cliente> isClienteBloccaDdtEquals = cliente -> {
            if(bloccaDdt != null){
                return cliente.getBloccaDdt().equals(bloccaDdt);
            }
            return true;
        };

        Predicate<Cliente> isClientePrivatoEquals = cliente -> {
            if(privato != null){
                return cliente.getPrivato().equals(privato);
            }
            return true;
        };

        Predicate<Cliente> isClienteIdListinoEquals = cliente -> {
            if(idListino != null){
                Listino listino = cliente.getListino();
                if(listino != null){
                    return listino.getId().equals(idListino.longValue());
                }
                return false;
            }
            return true;
        };

        Comparator<Cliente> comparator = Comparator.comparing(Cliente::getFieldComparing);

        Set<Cliente> clienti = clienteService.getAll();
        return clienti.stream().filter(isClienteBloccaDdtEquals
                .and(isClientePrivatoEquals).and(isClienteIdListinoEquals))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = GET, path = "/{clienteId}")
    @CrossOrigin
    public Cliente getOne(@PathVariable final Long clienteId) {
        LOGGER.info("Performing GET request for retrieving 'cliente' '{}'", clienteId);
        return clienteService.getOne(clienteId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Cliente create(@RequestBody final Cliente cliente){
        LOGGER.info("Performing POST request for creating 'cliente'");
        return clienteService.create(cliente);
    }

    @RequestMapping(method = PUT, path = "/{clienteId}")
    @CrossOrigin
    public Cliente update(@PathVariable final Long clienteId, @RequestBody final Cliente cliente){
        LOGGER.info("Performing PUT request for updating 'cliente' '{}'", clienteId);
        if (!Objects.equals(clienteId, cliente.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return clienteService.update(cliente);
    }

    @RequestMapping(method = DELETE, path = "/{clienteId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long clienteId){
        LOGGER.info("Performing DELETE request for deleting 'cliente' '{}'", clienteId);
        clienteService.delete(clienteId);
    }

    @RequestMapping(method = GET, path = "/{clienteId}/punti-consegna")
    @CrossOrigin
    public List<PuntoConsegna> getPuntiConsegna(@PathVariable final Long clienteId) {
        LOGGER.info("Performing GET request for retrieving 'puntiConsegna' of 'cliente' '{}'", clienteId);
        return clienteService.getPuntiConsegna(clienteId);
    }

    @RequestMapping(method = GET, path = "/{clienteId}/listini-associati")
    @CrossOrigin
    public List<ListinoAssociato> getListiniAssociati(@PathVariable final Long clienteId) {
        LOGGER.info("Performing GET request for retrieving 'listiniAssociati' of 'cliente' '{}'", clienteId);
        return clienteService.getListiniAssociati(clienteId);
    }
}
