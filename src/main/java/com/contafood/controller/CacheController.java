package com.contafood.controller;

import com.contafood.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/cache")
@Slf4j
public class CacheController {

    private final CacheService cacheService;

    public CacheController(final CacheService cacheService){
        this.cacheService = cacheService;
    }

    @RequestMapping(method = GET, path = "/{cacheName}")
    @CrossOrigin
    public Map<Object, Object> getAsMap(@PathVariable final String cacheName) {
        log.info("Performing GET request for retrieving cache '{}'", cacheName);
        return cacheService.getAsMap(cacheName);
    }

    @RequestMapping(method = DELETE, path = "/{cacheName}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void clear(@PathVariable final String cacheName){
        log.info("Performing DELETE request for clearing cache '{}'", cacheName);
        cacheService.clear(cacheName);
    }

    @RequestMapping(method = DELETE)
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void clearAll(){
        log.info("Performing DELETE request for clearing all caches");
        cacheService.clearAll();
    }
}
