package com.contafood.service;

import com.contafood.model.*;
import com.contafood.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovimentazioneService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovimentazioneService.class);

    private static final String INPUT = "INPUT";
    private static final String OUTPUT = "OUTPUT";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private final DdtAcquistoArticoloService ddtAcquistoArticoloServiceService;
    private final DdtArticoloService ddtArticoloService;
    private final FatturaAccompagnatoriaArticoloService fatturaAccompagnatoriaArticoloService;
    private final FornitoreService fornitoreService;
    private final ArticoloRepository articoloRepository;
    private final DdtAcquistoRepository ddtAcquistoRepository;
    private final DdtRepository ddtRepository;
    private final FatturaAccompagnatoriaRepository fatturaAccompagnatoriaRepository;
    private final ProduzioneRepository produzioneRepository;

    @Autowired
    public MovimentazioneService(final DdtAcquistoArticoloService ddtAcquistoArticoloServiceService,
                                 final DdtArticoloService ddtArticoloService,
                                 final FatturaAccompagnatoriaArticoloService fatturaAccompagnatoriaArticoloService,
                                 final FornitoreService fornitoreService,
                                 final ArticoloRepository articoloRepository,
                                 final DdtAcquistoRepository ddtAcquistoRepository,
                                 final DdtRepository ddtRepository,
                                 final FatturaAccompagnatoriaRepository fatturaAccompagnatoriaRepository,
                                 final ProduzioneRepository produzioneRepository){
        this.ddtAcquistoArticoloServiceService = ddtAcquistoArticoloServiceService;
        this.ddtArticoloService = ddtArticoloService;
        this.fatturaAccompagnatoriaArticoloService = fatturaAccompagnatoriaArticoloService;
        this.fornitoreService = fornitoreService;
        this.articoloRepository = articoloRepository;
        this.ddtAcquistoRepository = ddtAcquistoRepository;
        this.ddtRepository = ddtRepository;
        this.fatturaAccompagnatoriaRepository = fatturaAccompagnatoriaRepository;
        this.produzioneRepository = produzioneRepository;
    }

    public Set<Movimentazione> getMovimentazioni(Giacenza giacenza){
        LOGGER.info("Retrieving 'movimentazioni' of 'giacenza' '{}'", giacenza.getId());
        Set<Movimentazione> movimentazioni = new HashSet<>();
        Set<DdtAcquistoArticolo> ddtAcquistoArticoli = new HashSet<>();
        Set<DdtArticolo> ddtArticoli = new HashSet<>();
        Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = new HashSet<>();
        Set<Produzione> produzioni = new HashSet<>();

        Articolo articolo = giacenza.getArticolo();
        Ricetta ricetta = giacenza.getRicetta();

        if(articolo != null){
            // retrieve the set of 'DdtAcquistoArticolo'
            ddtAcquistoArticoli = ddtAcquistoArticoloServiceService.getByArticoloIdAndLottoAndScadenza(articolo.getId(), giacenza.getLotto(), giacenza.getScadenza());

            // retrieve the set of 'DdtArticolo'
            ddtArticoloService.getByArticoloIdAndLottoAndScadenza(articolo.getId(), giacenza.getLotto(), giacenza.getScadenza());

            // retrieve the set of 'FatturaAccompagnatoriaArticolo'
            fatturaAccompagnatoriaArticoli = fatturaAccompagnatoriaArticoloService.getByArticoloIdAndLottoAndScadenza(articolo.getId(), giacenza.getLotto(), giacenza.getScadenza());
        }
        if(ricetta != null){
            produzioni = produzioneRepository.findByRicettaIdAndLotto(ricetta.getId(), giacenza.getLotto());
            if(produzioni != null && !produzioni.isEmpty()){
                if(giacenza.getScadenza() != null){
                    produzioni = produzioni.stream()
                            .filter(p -> (p.getScadenza() != null && p.getScadenza().toLocalDate().compareTo(giacenza.getScadenza().toLocalDate())==0)).collect(Collectors.toSet());
                }
            }
        }

        // Create 'movimentazione' from 'DdtAcquistoArticolo'
        if(ddtAcquistoArticoli != null && !ddtAcquistoArticoli.isEmpty()){
            ddtAcquistoArticoli.forEach(daa -> {
                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenza.getId());
                movimentazione.setInputOutput(INPUT);
                if(daa.getDdtAcquisto() != null){
                    movimentazione.setData(daa.getDdtAcquisto().getData());
                }
                movimentazione.setQuantita(daa.getQuantita());
                movimentazione.setDescrizione(createDescrizione(daa));

                movimentazioni.add(movimentazione);
            });
        }

        // Create 'movimentazione' from 'DdtArticolo'
        if(ddtArticoli != null && !ddtArticoli.isEmpty()){
            ddtArticoli.forEach(da -> {
                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenza.getId());
                movimentazione.setInputOutput(OUTPUT);
                if(da.getDdt() != null){
                    movimentazione.setData(da.getDdt().getData());
                }
                movimentazione.setQuantita(da.getQuantita());
                movimentazione.setDescrizione(createDescrizione(da));

                movimentazioni.add(movimentazione);
            });
        }

        // Create 'movimentazione' from 'FatturaAccompagnatoriaArticolo'
        if(fatturaAccompagnatoriaArticoli != null && !fatturaAccompagnatoriaArticoli.isEmpty()){
            fatturaAccompagnatoriaArticoli.forEach(faa -> {
                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenza.getId());
                movimentazione.setInputOutput(OUTPUT);
                if(faa.getFatturaAccompagnatoria() != null){
                    movimentazione.setData(faa.getFatturaAccompagnatoria().getData());
                }
                movimentazione.setQuantita(faa.getQuantita());
                movimentazione.setDescrizione(createDescrizione(faa));

                movimentazioni.add(movimentazione);
            });
        }

        // Create 'movimentazione' from 'Produzione'
        if(produzioni != null && !produzioni.isEmpty()){
            produzioni.forEach(p -> {
                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenza.getId());
                movimentazione.setInputOutput(INPUT);
                movimentazione.setData(p.getDataProduzione());
                movimentazione.setQuantita(p.getQuantitaTotale());
                movimentazione.setDescrizione(createDescrizione(p));

                movimentazioni.add(movimentazione);
            });
        }

        LOGGER.info("Retrieved '{}' 'movimentazioni' for 'giacenza' '{}'", movimentazioni.size(), giacenza.getId());
        return movimentazioni;
    }

    private String createDescrizione(DdtAcquistoArticolo ddtAcquistoArticolo){
        Long idArticolo = ddtAcquistoArticolo.getId().getArticoloId();
        Articolo articolo = articoloRepository.findById(idArticolo).get();
        UnitaMisura unitaMisura = articolo.getUnitaMisura();
        Fornitore fornitore = articolo.getFornitore();
        DdtAcquisto ddtAcquisto = ddtAcquistoRepository.findById(ddtAcquistoArticolo.getId().getDdtAcquistoId()).get();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Acquistato/i <b>").append(ddtAcquistoArticolo.getQuantita()).append("</b>");
        if(unitaMisura != null){
            stringBuilder.append(" ").append(unitaMisura.getEtichetta());
        }
        stringBuilder.append(" il ").append(simpleDateFormat.format(ddtAcquisto.getData()));
        stringBuilder.append(" (DDT acquisto n. ").append(ddtAcquisto.getNumero());
        if(fornitore != null){
            stringBuilder.append(" da ");
            stringBuilder.append(fornitore.getRagioneSociale());
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    private String createDescrizione(DdtArticolo ddtArticolo){
        Long idArticolo = ddtArticolo.getId().getArticoloId();
        Articolo articolo = articoloRepository.findById(idArticolo).get();
        UnitaMisura unitaMisura = articolo.getUnitaMisura();
        Ddt ddt = ddtRepository.findById(ddtArticolo.getId().getDdtId()).get();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Venduto/i <b>").append(ddtArticolo.getQuantita()).append("</b>");
        if(unitaMisura != null){
            stringBuilder.append(" ").append(unitaMisura.getEtichetta());
        }
        stringBuilder.append(" il ").append(simpleDateFormat.format(ddt.getData()));
        stringBuilder.append(" (DDT n. ").append(ddt.getProgressivo()).append("/").append(ddt.getAnnoContabile()).append(")");

        return stringBuilder.toString();
    }

    private String createDescrizione(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo){
        Long idArticolo = fatturaAccompagnatoriaArticolo.getId().getArticoloId();
        Articolo articolo = articoloRepository.findById(idArticolo).get();
        UnitaMisura unitaMisura = articolo.getUnitaMisura();
        FatturaAccompagnatoria fatturaAccompagnatoria = fatturaAccompagnatoriaRepository.findById(fatturaAccompagnatoriaArticolo.getId().getFatturaAccompagnatoriaId()).get();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Venduto/i <b>").append(fatturaAccompagnatoriaArticolo.getQuantita()).append("</b>");
        if(unitaMisura != null){
            stringBuilder.append(" ").append(unitaMisura.getEtichetta());
        }
        stringBuilder.append(" il ").append(simpleDateFormat.format(fatturaAccompagnatoria.getData()));
        stringBuilder.append(" (Fattura accompagnatoria n. ").append(fatturaAccompagnatoria.getProgressivo()).append("/").append(fatturaAccompagnatoria.getAnno()).append(")");

        return stringBuilder.toString();
    }

    private String createDescrizione(Produzione produzione){
        Fornitore fornitore = fornitoreService.getByRagioneSociale("URBANI GIUSEPPE");

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Prodotto/i <b>").append(produzione.getQuantitaTotale()).append("</b> Kg");
        stringBuilder.append(" il ").append(simpleDateFormat.format(produzione.getDataProduzione()));
        stringBuilder.append(" (Produzione n. ").append(produzione.getCodice());
        if(fornitore != null){
            stringBuilder.append(" da ");
            stringBuilder.append(fornitore.getRagioneSociale());
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

}
