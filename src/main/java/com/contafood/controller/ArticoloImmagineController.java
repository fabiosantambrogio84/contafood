package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Articolo;
import com.contafood.model.ArticoloImmagine;
import com.contafood.service.ArticoloImmagineService;
import com.contafood.service.ArticoloService;
import com.contafood.service.FileStorageService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/articoli-immagini")
public class ArticoloImmagineController {

    private static Logger LOGGER = LoggerFactory.getLogger(ArticoloImmagineController.class);

    private final ArticoloImmagineService articoloImmagineService;

    private final ArticoloService articoloService;

    private final FileStorageService fileStorageService;

    @Autowired
    public ArticoloImmagineController(final ArticoloImmagineService articoloImmagineService, ArticoloService articoloService, FileStorageService fileStorageService){
        this.articoloImmagineService = articoloImmagineService;
        this.articoloService = articoloService;
        this.fileStorageService = fileStorageService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<ArticoloImmagine> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'articoliImmagini'");
        return articoloImmagineService.getAll();
    }

    @RequestMapping(method = GET, path = "/{articoloImmagineId}")
    @CrossOrigin
    public ArticoloImmagine getOne(@PathVariable final Long articoloImmagineId) {
        LOGGER.info("Performing GET request for retrieving 'articoloImmagine' '{}'", articoloImmagineId);
        return articoloImmagineService.getOne(articoloImmagineId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public ArticoloImmagine create(@RequestParam("articoloId") Long articoloId, @RequestParam("file") MultipartFile file){
        LOGGER.info("Performing POST request for creating 'articoloImmagine'");
        Map<String,String> imageMap = fileStorageService.storeArticoloImmagine(articoloId, file);
        Articolo articolo = articoloService.getOne(articoloId);
        ArticoloImmagine articoloImmagine = new ArticoloImmagine();
        articoloImmagine.setArticolo(articolo);
        articoloImmagine.setFileName(imageMap.get("fileName"));
        articoloImmagine.setFilePath(imageMap.get("filePath"));
        articoloImmagine.setFileCompletePath(imageMap.get("fileCompletePath"));
        return articoloImmagineService.create(articoloImmagine);
    }

    /*
    @RequestMapping(method = PUT, path = "/{articoloImmagineId}")
    @CrossOrigin
    public ArticoloImmagine update(@PathVariable final Long articoloImmagineId, @RequestBody final ArticoloImmagine articoloImmagine){
        LOGGER.info("Performing PUT request for updating 'listinoAssociato' '{}'", articoloImmagineId);
        if (!Objects.equals(articoloImmagineId, articoloImmagine.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return articoloImmagineService.update(articoloImmagine);
    }
    */
    @RequestMapping(method = DELETE, path = "/{articoloImmagineId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long articoloImmagineId){
        LOGGER.info("Performing DELETE request for deleting 'articoloImmagine' '{}'", articoloImmagineId);
        articoloImmagineService.delete(articoloImmagineId);
    }
}
