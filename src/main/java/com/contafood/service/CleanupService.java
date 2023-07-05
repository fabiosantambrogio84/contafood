package com.contafood.service;

import com.contafood.model.OrdineCliente;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class CleanupService {

    private final OrdineClienteService ordineClienteService;

    public CleanupService(final OrdineClienteService ordineClienteService){
        this.ordineClienteService = ordineClienteService;
    }

    public void deleteEvasiAndExpiredOrdiniClienti(Integer days){
        log.info("Deleting expired and evasi Ordini Clienti");
        Set<OrdineCliente> expiredAndEvasiOrdiniClienti = ordineClienteService.getOrdiniClientiEvasiAndExpired(days);
        expiredAndEvasiOrdiniClienti.forEach(oc -> ordineClienteService.delete(oc.getId()));
        log.info("Successfully deleted expired and evasi Ordini Clienti");
    }

}
