package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Articolo;
import com.contafood.model.Sconto;
import com.contafood.repository.ScontoRepository;
import com.contafood.util.TipologiaSconto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScontoService {

    private static Logger LOGGER = LoggerFactory.getLogger(ScontoService.class);

    private final ScontoRepository scontoRepository;

    private final ArticoloService articoloService;

    @Autowired
    public ScontoService(final ScontoRepository scontoRepository, final ArticoloService articoloService){
        this.scontoRepository = scontoRepository;
        this.articoloService = articoloService;
    }

    public List<Sconto> getAll(){
        LOGGER.info("Retrieving the list of 'sconti'");
        List<Sconto> sconti = scontoRepository.findAll();
        LOGGER.info("Retrieved {} 'sconti'", sconti.size());
        return sconti;
    }

    public List<Sconto> getValidSconti(Long idCliente, Date data){
        Predicate<Sconto> isScontoDataDaLessOrEquals = sconto -> {
            if(data != null && sconto.getDataDal() != null){
                return sconto.getDataDal().compareTo(data)<=0;
            }
            return true;
        };
        Predicate<Sconto> isScontoDataAGreaterOrEquals = sconto -> {
            if(data != null && sconto.getDataAl() != null){
                return sconto.getDataAl().compareTo(data)>=0;
            }
            return true;
        };

        LOGGER.info("Retrieving the list of valid  'sconti' for cliente '{}' and date '{}'", idCliente, data);

        // retrieve the Sconti of tipologia ARTICOLO valid at date
        List<Sconto> validScontiArticoli = scontoRepository.findByTipologiaAndClienteId(TipologiaSconto.ARTICOLO.name(), idCliente)
                .stream()
                .filter(isScontoDataDaLessOrEquals.and(isScontoDataAGreaterOrEquals))
                .collect(Collectors.toList());

        LOGGER.info("Valid Sconti Articolo size {}", validScontiArticoli.size());

        // retrieve the Sconti of tipologia FORNITORE valid at date (without Articolo valued)
        List<Sconto> validScontiFornitoriWithoutArticoli = scontoRepository.findByTipologiaAndClienteId(TipologiaSconto.FORNITORE.name(), idCliente)
                .stream()
                .filter(isScontoDataDaLessOrEquals.and(isScontoDataAGreaterOrEquals))
                .collect(Collectors.toList());

        LOGGER.info("Valid Sconti Fornitore (without id_articolo) size {}", validScontiFornitoriWithoutArticoli.size());

        // create the list of valid Sconti of tipologia FORNITORE with Articolo valued
        List<Sconto> validScontiFornitori = new ArrayList<>();
        validScontiFornitoriWithoutArticoli.forEach(sf -> {
            Set<Articolo> articoliByFornitore = articoloService.getAllByAttivoAndFornitoreId(true, sf.getFornitore().getId());
            articoliByFornitore.forEach(a -> {
                Sconto sconto = new Sconto();
                sconto.setId(sf.getId());
                sconto.setCliente(sf.getCliente());
                sconto.setDataInserimento(sf.getDataInserimento());
                sconto.setDataDal(sf.getDataDal());
                sconto.setDataAl(sf.getDataAl());
                sconto.setFornitore(sf.getFornitore());
                sconto.setTipologia(sf.getTipologia());
                sconto.setValore(sf.getValore());
                sconto.setArticolo(a);
                validScontiFornitori.add(sconto);
            });
        });
        LOGGER.info("Valid Sconti Fornitore (with id_articolo) size {}", validScontiFornitori.size());

        List<Sconto> validScontiArticoloFornitore = Stream.concat(validScontiArticoli.stream(), validScontiFornitori.stream()).collect(Collectors.toList());

        LOGGER.info("Valid Sconti Articolo and Fornitore size {}", validScontiArticoloFornitore.size());

        // create a map with key Articolo.id and value the list of associated Sconti
        Map<Long, List<Sconto>> articoloScontiMap = new HashMap<>();
        validScontiArticoloFornitore.forEach(sa -> {
            Articolo articolo = sa.getArticolo();
            if(articolo != null){
                articoloScontiMap.computeIfAbsent(articolo.getId(), v -> new ArrayList<Sconto>());
                articoloScontiMap.computeIfPresent(articolo.getId(), (key, value) -> {
                    value.add(sa);
                    return value;
                });
            }
        });

        Comparator<Sconto> compareByDataInserimento = new Comparator<Sconto>() {
            @Override
            public int compare(Sconto s1, Sconto s2) {
                Long d1 = s1.getDataInserimento().getTime();
                Long d2 = s2.getDataInserimento().getTime();
                return d2.compareTo(d1);
            }
        };

        List<Sconto> sconti = new ArrayList<>();
        // iterate over map and get the valid prior Sconto for each Articolo
        for (Map.Entry<Long, List<Sconto>> entry : articoloScontiMap.entrySet()) {
            List<Sconto> articoloSconti = entry.getValue();

            Optional<Sconto> optionalSconto = articoloSconti.stream()
                    .filter(s -> s.getTipologia().equals(TipologiaSconto.ARTICOLO.name()))
                    .sorted(compareByDataInserimento)
                    .findFirst();
            if(optionalSconto.isPresent()){
                sconti.add(optionalSconto.get());
            } else {
                articoloSconti.stream()
                        .filter(s -> s.getTipologia().equals(TipologiaSconto.FORNITORE.name()))
                        .sorted(compareByDataInserimento)
                        .findFirst().ifPresent(s -> sconti.add(s));
            }
        }

        LOGGER.info("Retrieved {} 'sconti'", sconti.size());
        return sconti;
    }

    public Sconto getOne(Long scontoId){
        LOGGER.info("Retrieving 'sconto' '{}'", scontoId);
        Sconto sconto = scontoRepository.findById(scontoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'sconto' '{}'", sconto);
        return sconto;
    }

    public List<Sconto> create(List<Sconto> sconti){
        LOGGER.info("Creating 'sconti'");
        sconti.forEach(s -> {
            s.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
            Sconto createdSconto = scontoRepository.save(s);
            LOGGER.info("Created 'sconto' '{}'", createdSconto);
        });

        return sconti;
    }

    public Sconto update(Sconto sconto){
        LOGGER.info("Updating 'sconto'");
        Sconto scontoCurrent = scontoRepository.findById(sconto.getId()).orElseThrow(ResourceNotFoundException::new);
        sconto.setDataInserimento(scontoCurrent.getDataInserimento());
        Sconto updatedSconto = scontoRepository.save(sconto);
        LOGGER.info("Updated 'sconto' '{}'", updatedSconto);
        return updatedSconto;
    }

    public void delete(Long scontoId){
        LOGGER.info("Deleting 'sconto' '{}'", scontoId);
        scontoRepository.deleteById(scontoId);
        LOGGER.info("Deleted 'sconto' '{}'", scontoId);
    }

    public List<Sconto> getAllByTipologia(String tipologia){
        LOGGER.info("Retrieving the list of 'sconti' filtered by tipologia '{}'", tipologia);
        List<Sconto> sconti = scontoRepository.findByTipologia(tipologia);
        LOGGER.info("Retrieved {} 'sconti'", sconti.size());
        return sconti;
    }

}
