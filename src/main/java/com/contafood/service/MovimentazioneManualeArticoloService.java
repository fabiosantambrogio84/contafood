package com.contafood.service;

import com.contafood.model.DdtArticolo;
import com.contafood.model.GiacenzaArticolo;
import com.contafood.model.MovimentazioneManualeArticolo;
import com.contafood.repository.MovimentazioneManualeArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovimentazioneManualeArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(MovimentazioneManualeArticoloService.class);

    private final MovimentazioneManualeArticoloRepository movimentazioneManualeArticoloRepository;

    @Autowired
    public MovimentazioneManualeArticoloService(final MovimentazioneManualeArticoloRepository movimentazioneManualeArticoloRepository){
        this.movimentazioneManualeArticoloRepository = movimentazioneManualeArticoloRepository;
    }

    @Transactional
    public MovimentazioneManualeArticolo create(GiacenzaArticolo giacenzaArticolo){
        LOGGER.info("Creating 'movimentazione manuale articolo'");

        MovimentazioneManualeArticolo movimentazioneManualeArticolo = new MovimentazioneManualeArticolo();
        movimentazioneManualeArticolo.setArticolo(giacenzaArticolo.getArticolo());
        movimentazioneManualeArticolo.setLotto(giacenzaArticolo.getLotto());
        movimentazioneManualeArticolo.setScadenza(giacenzaArticolo.getScadenza());
        movimentazioneManualeArticolo.setQuantita(giacenzaArticolo.getQuantita());
        movimentazioneManualeArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        MovimentazioneManualeArticolo createdMovimentazioneManualeArticolo = movimentazioneManualeArticoloRepository.save(movimentazioneManualeArticolo);

        LOGGER.info("Created 'movimentazione manuale articolo' '{}'", createdMovimentazioneManualeArticolo);
        return createdMovimentazioneManualeArticolo;
    }

    @Transactional
    public void deleteByArticoloIdIn(List<Long> idArticoli){
        LOGGER.info("Bulk deleting all the 'movimentazioni manuali articolo' by 'idArticolo' (number of elements to delete: {})", idArticoli.size());
        movimentazioneManualeArticoloRepository.deleteByArticoloIdIn(idArticoli);
        LOGGER.info("Bulk deleted all the specified 'movimentazioni manuali articolo");
    }

    public Set<MovimentazioneManualeArticolo> getByArticoloIdAndLottoAndScadenza(Long idArticolo, String lotto, Date scadenza){
        LOGGER.info("Retrieving 'movimentazioni manuali articoli' by 'idArticolo' '{}', 'lotto' '{}' and 'scadenza' '{}'", idArticolo, lotto, scadenza);
        Set<MovimentazioneManualeArticolo> movimentazioniManualiArticoli = movimentazioneManualeArticoloRepository.findByArticoloIdAndLotto(idArticolo, lotto);
        if(movimentazioniManualiArticoli != null && !movimentazioniManualiArticoli.isEmpty()){
            if(scadenza != null){
                movimentazioniManualiArticoli = movimentazioniManualiArticoli.stream()
                        .filter(da -> (da.getScadenza() != null && da.getScadenza().toLocalDate().compareTo(scadenza.toLocalDate())==0)).collect(Collectors.toSet());
            }
        }
        LOGGER.info("Retrieved '{}' 'movimentazioni manuali articoli'", movimentazioniManualiArticoli.size());
        return movimentazioniManualiArticoli;
    }

}
