package com.contafood.service;

import com.contafood.model.GiacenzaArticolo;
import com.contafood.model.GiacenzaIngrediente;
import com.contafood.model.OrdineCliente;
import com.contafood.model.Sconto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    private final ScontoService scontoService;
    private final OrdineClienteService ordineClienteService;
    private final GiacenzaArticoloService giacenzaArticoloService;
    private final GiacenzaIngredienteService giacenzaIngredienteService;

    public ScheduleService(final ScontoService scontoService,
                           final OrdineClienteService ordineClienteService,
                           final GiacenzaArticoloService giacenzaArticoloService,
                           final GiacenzaIngredienteService giacenzaIngredienteService){
        this.scontoService = scontoService;
        this.ordineClienteService = ordineClienteService;
        this.giacenzaArticoloService = giacenzaArticoloService;
        this.giacenzaIngredienteService = giacenzaIngredienteService;
    }

    @Scheduled(cron = "0 50 17 * * ?")
    public void deleteExpiredSconti() {
        LOGGER.info("Executing remove of expired Sconti");
        Date now = new Date(System.currentTimeMillis());
        List<Long> expiredSconti = scontoService.getAll().stream().filter(s -> (s.getDataAl() != null && s.getDataAl().before(now))).map(Sconto::getId).collect(Collectors.toList());
        expiredSconti.forEach(scontoService::delete);
        LOGGER.info("Executed remove of expired Sconti");
    }

    @Scheduled(cron = "0 30 23 * * ?")
    public void deleteEvasiAndExpiredOrdiniClienti(){
        LOGGER.info("Executing remove of expired and evasi Ordini Clienti");
        Set<OrdineCliente> expiredAndEvasiOrdiniClienti = ordineClienteService.getOrdiniClientiEvasiAndExpired(1);
        expiredAndEvasiOrdiniClienti.forEach(oc -> ordineClienteService.delete(oc.getId()));
        LOGGER.info("Executed remove of expired and evasi Ordini Clienti");
    }

    @Scheduled(cron = "0 30 17 * * ?")
    public void deleteExpiredZeroGiacenze(){
        LOGGER.info("Executing remove of expired and zero Giacenze");
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LOGGER.info("Date to check: {}", yesterday);

        Set<GiacenzaArticolo> giacenzeArticoli = giacenzaArticoloService.getAllNotAggregate();
        if(giacenzeArticoli != null && !giacenzeArticoli.isEmpty()){
            giacenzeArticoli.stream()
                    .filter(g -> (g.getQuantita().equals(0f) && g.getScadenza() == null)||(g.getQuantita().equals(0f) && g.getScadenza() != null && g.getScadenza().toLocalDate().compareTo(yesterday)<0))
                    .forEach(g -> giacenzaArticoloService.delete(g.getId()));
        }
        LOGGER.info("Deleted Giacenze Articoli");

        Set<GiacenzaIngrediente> giacenzeIngredienti = giacenzaIngredienteService.getAllNotAggregate();
        if(giacenzeIngredienti != null && !giacenzeIngredienti.isEmpty()){
            giacenzeIngredienti.stream()
                    .filter(g -> (g.getQuantita().equals(0f) && g.getScadenza() == null)||(g.getQuantita().equals(0f) && g.getScadenza() != null && g.getScadenza().toLocalDate().compareTo(yesterday)<0))
                    .forEach(g -> giacenzaIngredienteService.delete(g.getId()));
        }
        LOGGER.info("Deleted Giacenze Ingredienti");
        LOGGER.info("Executed remove of expired and zero Giacenze");
    }

    /*
    @Scheduled(cron = "0 35 17 * * ?")
    public void deleteExpiredTelefonate(){
        LOGGER.info("Executing remove of expired Telefonate");
        LocalDateTime limitDatetime = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LOGGER.info("Date to check: {}", limitDatetime);
        List<Telefonata> telefonate = telefonataService.getAll();
        if(!telefonate.isEmpty()){
            telefonate.stream()
                    .filter(t -> t.getDataEsecuzione().toLocalDateTime().isBefore(limitDatetime))
                    .forEach(t -> telefonataService.delete(t.getId()));
        }
        LOGGER.info("Executed remove of expired Telefonate");
    }
    */

}
