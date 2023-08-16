package com.contafood.controller;

import com.contafood.service.CleanupService;
import com.contafood.util.enumeration.CleanupAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@RestController
@RequestMapping(path="/configurazione/cleanup")
public class CleanupController {

    private final CleanupService cleanupService;

    public CleanupController(final CleanupService cleanupService){
        this.cleanupService = cleanupService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public List<Map<String, String>> getActions() {
        log.info("Performing GET request for retrieving list of 'cleanup actions'");
        return CleanupAction.getActions();
    }

    @RequestMapping(method = DELETE, path = "ordini-clienti/evasi-expired")
    @CrossOrigin
    public void deleteEvasiAndExpiredOrdiniClienti(@RequestParam(name = "days", required = false) Integer days) {
        log.info("Performing DELETE request for deleting expired and evasi Ordini Clienti");
        cleanupService.deleteEvasiAndExpiredOrdiniClienti(days);
    }

    @RequestMapping(method = DELETE, path = "etichette")
    @CrossOrigin
    public void deleteEtichette(@RequestParam(name = "days", required = false) Integer days) {
        log.info("Performing DELETE request for deleting Etichette");
        cleanupService.deleteEtichette(days);
    }

}