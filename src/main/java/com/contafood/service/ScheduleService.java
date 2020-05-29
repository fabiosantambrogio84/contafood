package com.contafood.service;

import com.contafood.model.OrdineCliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private static Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    private final ScontoService scontoService;

    private final OrdineClienteService ordineClienteService;

    @Autowired
    public ScheduleService(final ScontoService scontoService, final OrdineClienteService ordineClienteService){
        this.scontoService = scontoService;
        this.ordineClienteService = ordineClienteService;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void deleteExpiredSconti() {
        LOGGER.info("Executing remove of expired Sconti");
        Date now = new Date(System.currentTimeMillis());
        List<Long> expiredSconti = scontoService.getAll().stream().filter(s -> (s.getDataAl() != null && s.getDataAl().before(now))).map(s -> s.getId()).collect(Collectors.toList());
        expiredSconti.forEach(es -> scontoService.delete(es));
        LOGGER.info("Executed remove of expired Sconti");
    }

    @Scheduled(cron = "0 30 0 * * ?")
    public void deleteEvasiAndExpiredOrdiniClienti(){
        LOGGER.info("Executing remove of expired and evasi Ordini Clienti");
        Set<OrdineCliente> expiredAndEvasiOrdiniClienti = ordineClienteService.getOrdiniClientiEvasiAndExpired();
        expiredAndEvasiOrdiniClienti.forEach(oc -> ordineClienteService.delete(oc.getId()));
        LOGGER.info("Executed remove of expired and evasi Ordini Clienti\"");
    }
}
