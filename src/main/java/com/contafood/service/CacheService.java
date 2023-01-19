package com.contafood.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class CacheService {

    private final CacheManager cacheManager;

    public CacheService(final CacheManager cacheManager){
        this.cacheManager = cacheManager;
    }

    /*
    public Map initialize(){
        Map<String, Integer> result = new HashMap<>();

        for(String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            int cacheSize;
            try{
                ConcurrentHashMap nativeCache = (ConcurrentHashMap)cache.getNativeCache();
                cacheSize = nativeCache.size();
            } catch(Exception e){
                cacheSize = 0;
            }
            result.put(cacheName, cacheSize);
        }
        return result;
    }*/

    public Map getAsMap(String cacheName){
        Cache cache = cacheManager.getCache(cacheName);
        return (ConcurrentHashMap)cache.getNativeCache();
    }

    public void clear(String cacheName){
        Cache cache = cacheManager.getCache(cacheName);
        if(cache != null){
            cache.clear();
        }
    }

    public void clearAll(){
        for(String cacheName : cacheManager.getCacheNames()){
            clear(cacheName);
        }
    }
}
