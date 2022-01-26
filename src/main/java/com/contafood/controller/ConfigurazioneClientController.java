package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.ConfigurazioneClient;
import com.contafood.service.ConfigurazioneClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping(path="/configurazione/app-client")
public class ConfigurazioneClientController {

    private static Logger LOGGER = LoggerFactory.getLogger(ConfigurazioneClientController.class);

    private final ConfigurazioneClientService configurazioneClientService;

    public ConfigurazioneClientController(final ConfigurazioneClientService configurazioneClientService){
        this.configurazioneClientService = configurazioneClientService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<ConfigurazioneClient> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'configurazioni client'");
        return configurazioneClientService.getAll();
    }

    @RequestMapping(method = GET, path = "/{idConfigurazione}")
    @CrossOrigin
    public ConfigurazioneClient getOne(@PathVariable final Long idConfigurazione) {
        LOGGER.info("Performing GET request for retrieving 'configurazione client' '{}'", idConfigurazione);
        return configurazioneClientService.getOne(idConfigurazione);
    }

    @RequestMapping(method = PUT, path = "/{idConfigurazione}")
    @CrossOrigin
    public ConfigurazioneClient update(@PathVariable final Long idConfigurazione, @RequestBody final ConfigurazioneClient configurazioneClient){
        LOGGER.info("Performing PUT request for updating 'configurazione client' '{}'", idConfigurazione);
        if (!Objects.equals(idConfigurazione, configurazioneClient.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return configurazioneClientService.update(configurazioneClient);
    }
}
