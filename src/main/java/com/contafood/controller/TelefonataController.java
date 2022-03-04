package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Telefonata;
import com.contafood.service.TelefonataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/telefonate")
public class TelefonataController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TelefonataController.class);

    private final TelefonataService telefonataService;

    @Autowired
    public TelefonataController(final TelefonataService telefonataService){
        this.telefonataService = telefonataService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public List<Telefonata> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'telefonate'");
        return telefonataService.getAll();
    }

    @RequestMapping(method = GET, path = "/{telefonataId}")
    @CrossOrigin
    public Telefonata getOne(@PathVariable final Long telefonataId) {
        LOGGER.info("Performing GET request for retrieving 'telefonata' '{}'", telefonataId);
        return telefonataService.getOne(telefonataId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Telefonata create(@RequestBody final Telefonata telefonata){
        LOGGER.info("Performing POST request for creating 'telefonata'");
        return telefonataService.create(telefonata);
    }

    @RequestMapping(method = PUT, path = "/{telefonataId}")
    @CrossOrigin
    public Telefonata update(@PathVariable final Long telefonataId, @RequestBody final Telefonata telefonata){
        LOGGER.info("Performing PUT request for updating 'telefonata' '{}'", telefonataId);
        if (!Objects.equals(telefonataId, telefonata.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return telefonataService.update(telefonata);
    }

    @RequestMapping(method = PATCH, path = "/{telefonataId}")
    @CrossOrigin
    public Telefonata patch(@PathVariable final Long telefonataId, @RequestBody final Map<String,Object> patchTelefonata){
        LOGGER.info("Performing PATCH request for updating 'telefonata' '{}'", telefonataId);
        Long id = Long.valueOf((Integer)patchTelefonata.get("idTelefonata"));
        if (!Objects.equals(telefonataId, id)) {
            throw new CannotChangeResourceIdException();
        }
        return telefonataService.patch(patchTelefonata);
    }

    @RequestMapping(method = DELETE, path = "/{telefonataId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long telefonataId){
        LOGGER.info("Performing DELETE request for deleting 'telefonata' '{}'", telefonataId);
        telefonataService.delete(telefonataId);
    }

    @RequestMapping(method = POST, path = "/operations/delete")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void bulkDelete(@RequestBody final List<Long> telefonateIds){
        LOGGER.info("Performing BULK DELETE operation on 'telefonate' (number of elements to delete: {})", telefonateIds.size());
        telefonataService.bulkDelete(telefonateIds);
    }
}
