package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.*;
import com.contafood.model.views.VOrdineFornitoreArticolo;
import com.contafood.service.OrdineClienteService;
import com.contafood.service.jpa.NativeQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/ordini-clienti")
@SuppressWarnings("unused")
public class OrdineClienteController {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrdineClienteController.class);

    private final OrdineClienteService ordineClienteService;

    private final NativeQueryService nativeQueryService;

    @Autowired
    public OrdineClienteController(final OrdineClienteService ordineClienteService, final NativeQueryService nativeQueryService){
        this.ordineClienteService = ordineClienteService;
        this.nativeQueryService = nativeQueryService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public List<OrdineCliente> getAll( @RequestParam(name = "cliente", required = false) String cliente,
                                      @RequestParam(name = "dataConsegna", required = false) Date dataConsegna,
                                      @RequestParam(name = "idAutista", required = false) Integer idAutista,
                                      @RequestParam(name = "idStato", required = false) Integer idStato,
                                      @RequestParam(name = "idCliente", required = false) Integer idCliente,
                                      @RequestParam(name = "idPuntoConsegna", required = false) Integer idPuntoConsegna,
                                      @RequestParam(name = "dataConsegnaLessOrEqual", required = false) Date dataConsegnaLessOrEqual,
                                      @RequestParam(name = "idStatoNot", required = false) Integer idStatoNot) {
        LOGGER.info("Performing GET request for retrieving list of 'ordini-clienti'");
        LOGGER.info("Request params: cliente {}, dataConsegna {}, idAutista {}, idStato {}, idCliente {}, idPuntoConsegna {}, dataConsegnaLessOrEqual {}, idStatoNot {}",
                cliente, dataConsegna, idAutista, idStato, idCliente, idPuntoConsegna, dataConsegnaLessOrEqual, idStatoNot);

        Predicate<OrdineCliente> isOrdineClienteClienteContains = ordineCliente -> {
            if(cliente != null){
                Cliente ordineClienteCliente = ordineCliente.getCliente();
                if(ordineClienteCliente != null){
                    return (ordineClienteCliente.getRagioneSociale().toLowerCase()).contains(cliente.toLowerCase());
                }
                return false;
            }
            return true;
        };
        Predicate<OrdineCliente> isOrdineClienteDataConsegnaEquals = ordineCliente -> {
            if(dataConsegna != null){
                return ordineCliente.getDataConsegna().compareTo(dataConsegna) == 0;
            }
            return true;
        };
        Predicate<OrdineCliente> isOrdineClienteAutistaEquals = ordineCliente -> {
            if(idAutista != null){
                Autista autista = ordineCliente.getAutista();
                if(autista != null){
                    return autista.getId().equals(Long.valueOf(idAutista));
                }
                return false;
            }
            return true;
        };
        Predicate<OrdineCliente> isOrdineClienteStatoEquals = ordineCliente -> {
            if(idStato != null){
                StatoOrdine statoOrdine = ordineCliente.getStatoOrdine();
                if(statoOrdine != null){
                    return statoOrdine.getId().equals(Long.valueOf(idStato));
                }
                return false;
            }
            return true;
        };

        Comparator<OrdineCliente> compare = Comparator.comparing(OrdineCliente::getProgressivo).reversed().thenComparing(OrdineCliente::getAnnoContabile).reversed();

        if(idCliente != null && idPuntoConsegna != null && dataConsegnaLessOrEqual != null && idStatoNot != null){
            return ordineClienteService.getByIdClienteAndIdPuntoConsegnaAndDataConsegnaLessOrEqualAndIdStatoNot(
                    Long.valueOf(idCliente), Long.valueOf(idPuntoConsegna), dataConsegnaLessOrEqual, Long.valueOf(idStatoNot)
            ).stream().sorted(compare).collect(Collectors.toList());
        } else {
            return ordineClienteService.getAll().stream().filter(isOrdineClienteClienteContains
                    .and(isOrdineClienteDataConsegnaEquals)
                    .and(isOrdineClienteAutistaEquals)
                    .and(isOrdineClienteStatoEquals)).sorted(compare).collect(Collectors.toList());
        }
    }

    @RequestMapping(method = GET, path = "/aggregate")
    @CrossOrigin
    public List<OrdineClienteAggregate> getAllAggregate(@RequestParam(name = "idCliente", required = false) Integer idCliente,
                                                        @RequestParam(name = "idPuntoConsegna", required = false) Integer idPuntoConsegna,
                                                        @RequestParam(name = "dataConsegnaLessOrEqual", required = false) Date dataConsegnaLessOrEqual,
                                                        @RequestParam(name = "idStatoNot", required = false) Integer idStatoNot) {
        LOGGER.info("Performing GET request for retrieving list of 'ordini-clienti aggregate'");
        LOGGER.info("Request params: idCliente {}, idPuntoConsegna {}, dataConsegnaLessOrEqual {}, idStatoNot {}",
                idCliente, idPuntoConsegna, dataConsegnaLessOrEqual, idStatoNot);

        return nativeQueryService.getOrdiniClientiAggregate(idCliente, idPuntoConsegna, dataConsegnaLessOrEqual, idStatoNot);
    }

    @RequestMapping(method = GET, path = "/{ordineClienteId}")
    @CrossOrigin
    public OrdineCliente getOne(@PathVariable final Long ordineClienteId) {
        LOGGER.info("Performing GET request for retrieving 'ordineCliente' '{}'", ordineClienteId);
        return ordineClienteService.getOne(ordineClienteId);
    }

    @RequestMapping(method = GET, path = "/progressivo")
    @CrossOrigin
    public Map<String, Integer> getAnnoContabileAndProgressivo() {
        LOGGER.info("Performing GET request for retrieving 'annoContabile' and 'progressivo' for a new ordine-cliente");
        return ordineClienteService.getAnnoContabileAndProgressivo();
    }

    @RequestMapping(method = GET, path = "/articoli-for-ordine-fornitore")
    @CrossOrigin
    public List<VOrdineFornitoreArticolo> getArticoliForOrdineFornitore(@RequestParam(name = "idFornitore") Integer idFornitore,
                                                                        @RequestParam(name = "dataFrom") Date dataFrom,
                                                                        @RequestParam(name = "dataTo") Date dataTo) {
        LOGGER.info("Performing GET request for retrieving 'articoli-for-ordine-fornitore' for fornitore '{}', dataFrom '{}' and dataTo '{}'", idFornitore, dataFrom, dataTo);
        return ordineClienteService.getArticoliForOrdineFornitore(idFornitore.longValue(), dataFrom, dataTo);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public OrdineCliente create(@RequestBody final OrdineCliente ordineCliente){
        LOGGER.info("Performing POST request for creating 'ordineCliente'");
        return ordineClienteService.create(ordineCliente);
    }

    @RequestMapping(method = PUT, path = "/{ordineClienteId}")
    @CrossOrigin
    public OrdineCliente update(@PathVariable final Long ordineClienteId, @RequestBody final OrdineCliente ordineCliente){
        LOGGER.info("Performing PUT request for updating 'ordineCliente' '{}'", ordineClienteId);
        if (!Objects.equals(ordineClienteId, ordineCliente.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return ordineClienteService.update(ordineCliente);
    }

    @RequestMapping(method = PATCH, path = "/{ordineClienteId}")
    @CrossOrigin
    public OrdineCliente patch(@PathVariable final Long ordineClienteId, @RequestBody final Map<String,Object> patchOrdineCliente){
        LOGGER.info("Performing PATCH request for updating 'ordineCliente' '{}'", ordineClienteId);
        Long id = Long.valueOf((Integer) patchOrdineCliente.get("id"));
        if (!Objects.equals(ordineClienteId, id)) {
            throw new CannotChangeResourceIdException();
        }
        return ordineClienteService.patch(patchOrdineCliente);
    }

    @RequestMapping(method = POST, path = "/aggregate")
    @ResponseStatus(CREATED)
    @CrossOrigin
    public List<OrdineClienteAggregate> updateAggregate(@RequestBody final List<OrdineClienteAggregate> ordiniClientiAggregati) {
        LOGGER.info("Performing POST request for updating list of 'ordini-clienti aggregate'");
        LOGGER.info("Updating {} 'ordini-clienti aggregate'", ordiniClientiAggregati.size());

        return ordineClienteService.updateAggregate(ordiniClientiAggregati);
    }

    @RequestMapping(method = DELETE, path = "/{ordineClienteId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long ordineClienteId){
        LOGGER.info("Performing DELETE request for deleting 'ordineCliente' '{}'", ordineClienteId);
        ordineClienteService.delete(ordineClienteId);
    }
}
