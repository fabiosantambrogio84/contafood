package com.contafood.service;

import com.contafood.model.Etichetta;
import com.contafood.model.OrdineCliente;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class CleanupService {

    private final OrdineClienteService ordineClienteService;
    private final EtichettaService etichettaService;

    public CleanupService(final OrdineClienteService ordineClienteService,
                          final EtichettaService etichettaService){
        this.ordineClienteService = ordineClienteService;
        this.etichettaService = etichettaService;
    }

    public void deleteEvasiAndExpiredOrdiniClienti(Integer days){
        log.info("Deleting expired and evasi Ordini Clienti");
        Set<OrdineCliente> expiredAndEvasiOrdiniClienti = ordineClienteService.getOrdiniClientiEvasiAndExpired(days);
        expiredAndEvasiOrdiniClienti.forEach(oc -> ordineClienteService.delete(oc.getId()));
        log.info("Successfully deleted expired and evasi Ordini Clienti");
    }

    public void deleteEtichette(Integer days){
        log.info("Deleting old Etichette");
        List<Etichetta> etichette = etichettaService.getEtichetteToDelete(days);
        etichette.forEach(e -> etichettaService.delete(e.getUuid()));
        log.info("Successfully deleted old Etichette");
    }

}