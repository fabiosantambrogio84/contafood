package com.contafood.controller;

import com.contafood.model.stats.Statistica;
import com.contafood.model.stats.StatisticaFilter;
import com.contafood.service.StatisticaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path="/statistiche")
public class StatisticaController {

    private static Logger LOGGER = LoggerFactory.getLogger(StatisticaController.class);

    private final StatisticaService statisticaService;

    @Autowired
    public StatisticaController(final StatisticaService statisticaService){
        this.statisticaService = statisticaService;
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Statistica compute(@RequestBody final StatisticaFilter statisticaFilter){
        LOGGER.info("Performing POST request for computing statistiche with filters '{}'", statisticaFilter);
        return statisticaService.computeStatistiche(statisticaFilter);
    }

}
