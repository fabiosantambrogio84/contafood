package com.contafood.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationEventListener {

    //private final CacheService cacheService;

    //public ApplicationEventListener(final CacheService cacheService){
    //    this.cacheService = cacheService;
    //}

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        /*
        log.info("Initializing cache...");
        Map<String, Integer> result = cacheService.initialize();
        if(!result.isEmpty()){
            for(String cacheName : result.keySet()){
                log.info("Cache {} initialized with {} entries", cacheName, result.get(cacheName));
            }
        } else {
            log.info("No caches has been initialized");
        }
        log.info("Successfully initialized cache");
        */
    }

}
