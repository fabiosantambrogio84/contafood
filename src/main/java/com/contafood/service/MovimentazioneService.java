package com.contafood.service;

import com.contafood.model.*;
import com.contafood.repository.*;
import com.contafood.util.Constants;
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

    private final DdtAcquistoArticoloService ddtAcquistoArticoloService;
    private final DdtAcquistoIngredienteService ddtAcquistoIngredienteService;
    private final DdtArticoloService ddtArticoloService;
    private final FatturaAccompagnatoriaArticoloService fatturaAccompagnatoriaArticoloService;
    private final ProduzioneIngredienteService produzioneIngredienteService;
    private final FornitoreService fornitoreService;
    private final ArticoloRepository articoloRepository;
    private final IngredienteRepository ingredienteRepository;
    private final DdtAcquistoRepository ddtAcquistoRepository;
    private final DdtRepository ddtRepository;
    private final FatturaAccompagnatoriaRepository fatturaAccompagnatoriaRepository;
    private final ProduzioneRepository produzioneRepository;

    @Autowired
    public MovimentazioneService(final DdtAcquistoArticoloService ddtAcquistoArticoloService,
                                 final DdtAcquistoIngredienteService ddtAcquistoIngredienteService,
                                 final DdtArticoloService ddtArticoloService,
                                 final FatturaAccompagnatoriaArticoloService fatturaAccompagnatoriaArticoloService,
                                 final ProduzioneIngredienteService produzioneIngredienteService,
                                 final FornitoreService fornitoreService,
                                 final ArticoloRepository articoloRepository,
                                 final IngredienteRepository ingredienteRepository,
                                 final DdtAcquistoRepository ddtAcquistoRepository,
                                 final DdtRepository ddtRepository,
                                 final FatturaAccompagnatoriaRepository fatturaAccompagnatoriaRepository,
                                 final ProduzioneRepository produzioneRepository){
        this.ddtAcquistoArticoloService = ddtAcquistoArticoloService;
        this.ddtAcquistoIngredienteService = ddtAcquistoIngredienteService;
        this.ddtArticoloService = ddtArticoloService;
        this.fatturaAccompagnatoriaArticoloService = fatturaAccompagnatoriaArticoloService;
        this.produzioneIngredienteService = produzioneIngredienteService;
        this.fornitoreService = fornitoreService;
        this.articoloRepository = articoloRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.ddtAcquistoRepository = ddtAcquistoRepository;
        this.ddtRepository = ddtRepository;
        this.fatturaAccompagnatoriaRepository = fatturaAccompagnatoriaRepository;
        this.produzioneRepository = produzioneRepository;
    }

    public Set<Movimentazione> getMovimentazioniArticoli(GiacenzaArticolo giacenzaArticolo){
        LOGGER.info("Retrieving 'movimentazioni' of 'giacenza articolo' '{}'", giacenzaArticolo.getId());
        Set<Movimentazione> movimentazioni = new HashSet<>();
        Set<DdtAcquistoArticolo> ddtAcquistoArticoli = new HashSet<>();
        Set<DdtArticolo> ddtArticoli = new HashSet<>();
        Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = new HashSet<>();
        Set<Produzione> produzioni = new HashSet<>();

        Articolo articolo = articoloRepository.findById(giacenzaArticolo.getArticolo().getId()).get();

        // retrieve the set of 'DdtAcquistoArticolo'
        ddtAcquistoArticoli = ddtAcquistoArticoloService.getByArticoloIdAndLottoAndScadenza(articolo.getId(), giacenzaArticolo.getLotto(), giacenzaArticolo.getScadenza());

        // retrieve the set of 'DdtArticolo'
        ddtArticoloService.getByArticoloIdAndLottoAndScadenza(articolo.getId(), giacenzaArticolo.getLotto(), giacenzaArticolo.getScadenza());

        // retrieve the set of 'FatturaAccompagnatoriaArticolo'
        fatturaAccompagnatoriaArticoli = fatturaAccompagnatoriaArticoloService.getByArticoloIdAndLottoAndScadenza(articolo.getId(), giacenzaArticolo.getLotto(), giacenzaArticolo.getScadenza());

        // retrieve the 'Ricetta' associated to the 'Articolo'
        String codiceRicetta = articolo.getCodice().substring(2);

        // retrieve the set of 'Produzioni'
        produzioni = produzioneRepository.findByRicettaCodiceAndLotto(codiceRicetta, giacenzaArticolo.getLotto());
        if(produzioni != null && !produzioni.isEmpty()){
            if(giacenzaArticolo.getScadenza() != null){
                produzioni = produzioni.stream()
                        .filter(p -> (p.getScadenza() != null && p.getScadenza().toLocalDate().compareTo(giacenzaArticolo.getScadenza().toLocalDate())==0)).collect(Collectors.toSet());
            }
        }

        // Create 'movimentazione' from 'DdtAcquistoArticolo'
        if(ddtAcquistoArticoli != null && !ddtAcquistoArticoli.isEmpty()){
            ddtAcquistoArticoli.forEach(daa -> {
                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenzaArticolo.getId());
                movimentazione.setInputOutput(INPUT);
                if(daa.getDdtAcquisto() != null){
                    movimentazione.setData(daa.getDdtAcquisto().getData());
                }
                movimentazione.setQuantita(daa.getQuantita());
                movimentazione.setDescrizione(createDescrizione(daa, articolo));

                movimentazioni.add(movimentazione);
            });
        }

        // Create 'movimentazione' from 'DdtArticolo'
        if(ddtArticoli != null && !ddtArticoli.isEmpty()){
            ddtArticoli.forEach(da -> {
                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenzaArticolo.getId());
                movimentazione.setInputOutput(OUTPUT);
                if(da.getDdt() != null){
                    movimentazione.setData(da.getDdt().getData());
                }
                movimentazione.setQuantita(da.getQuantita());
                movimentazione.setDescrizione(createDescrizione(da, articolo));

                movimentazioni.add(movimentazione);
            });
        }

        // Create 'movimentazione' from 'FatturaAccompagnatoriaArticolo'
        if(fatturaAccompagnatoriaArticoli != null && !fatturaAccompagnatoriaArticoli.isEmpty()){
            fatturaAccompagnatoriaArticoli.forEach(faa -> {
                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenzaArticolo.getId());
                movimentazione.setInputOutput(OUTPUT);
                if(faa.getFatturaAccompagnatoria() != null){
                    movimentazione.setData(faa.getFatturaAccompagnatoria().getData());
                }
                movimentazione.setQuantita(faa.getQuantita());
                movimentazione.setDescrizione(createDescrizione(faa, articolo));

                movimentazioni.add(movimentazione);
            });
        }

        // Create 'movimentazione' from 'Produzione'
        if(produzioni != null && !produzioni.isEmpty()){
            produzioni.forEach(p -> {
                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenzaArticolo.getId());
                movimentazione.setInputOutput(INPUT);
                movimentazione.setData(p.getDataProduzione());
                movimentazione.setQuantita(p.getQuantitaTotale());
                movimentazione.setDescrizione(createDescrizione(p));

                movimentazioni.add(movimentazione);
            });
        }

        LOGGER.info("Retrieved '{}' 'movimentazioni' for 'giacenza articolo' '{}'", movimentazioni.size(), giacenzaArticolo.getId());
        return movimentazioni;
    }

    public Set<Movimentazione> getMovimentazioniIngredienti(GiacenzaIngrediente giacenzaIngrediente){
        LOGGER.info("Retrieving 'movimentazioni' of 'giacenza ingrediente' '{}'", giacenzaIngrediente.getId());
        Set<Movimentazione> movimentazioni = new HashSet<>();
        Set<DdtAcquistoIngrediente> ddtAcquistoIngredienti = new HashSet<>();
        Set<ProduzioneIngrediente> produzioneIngredienti = new HashSet<>();

        Ingrediente ingrediente = ingredienteRepository.findById(giacenzaIngrediente.getIngrediente().getId()).get();

        // retrieve the set of 'DdtAcquistoIngrediente'
        ddtAcquistoIngredienti = ddtAcquistoIngredienteService.getByIngredienteIdAndLottoAndScadenza(ingrediente.getId(), giacenzaIngrediente.getLotto(), giacenzaIngrediente.getScadenza());

        // retrieve the set of 'ProduzioneIngrediente'
        produzioneIngredienti = produzioneIngredienteService.getByIngredienteIdAndLottoAndScadenza(ingrediente.getId(), giacenzaIngrediente.getLotto(), giacenzaIngrediente.getScadenza());

        // Create 'movimentazione' from 'DdtAcquistoIngrediente'
        if(ddtAcquistoIngredienti != null && !ddtAcquistoIngredienti.isEmpty()){
            ddtAcquistoIngredienti.forEach(dai -> {
                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenzaIngrediente.getId());
                movimentazione.setInputOutput(INPUT);
                if(dai.getDdtAcquisto() != null){
                    movimentazione.setData(dai.getDdtAcquisto().getData());
                }
                movimentazione.setQuantita(dai.getQuantita());
                movimentazione.setDescrizione(createDescrizione(dai, ingrediente));

                movimentazioni.add(movimentazione);
            });
        }

        // Create 'movimentazione' from 'ProduzioneIngrediente'
        if(produzioneIngredienti != null && !produzioneIngredienti.isEmpty()){
            produzioneIngredienti.forEach(pi -> {
                Produzione produzione = produzioneRepository.findById(pi.getProduzione().getId()).get();

                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenzaIngrediente.getId());
                movimentazione.setInputOutput(OUTPUT);
                movimentazione.setData(produzione.getDataProduzione());
                movimentazione.setQuantita(pi.getQuantita());
                movimentazione.setDescrizione(createDescrizione(produzione));

                movimentazioni.add(movimentazione);
            });
        }

        LOGGER.info("Retrieved '{}' 'movimentazioni' for 'giacenza ingrediente' '{}'", movimentazioni.size(), giacenzaIngrediente.getId());
        return movimentazioni;
    }

    private String createDescrizione(DdtAcquistoArticolo ddtAcquistoArticolo, Articolo articolo){
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

    private String createDescrizione(DdtArticolo ddtArticolo, Articolo articolo){
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

    private String createDescrizione(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo, Articolo articolo){
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
        Fornitore fornitore = fornitoreService.getByRagioneSociale(Constants.DEFAULT_FORNITORE);

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

    private String createDescrizione(DdtAcquistoIngrediente ddtAcquistoIngrediente, Ingrediente ingrediente){
        UnitaMisura unitaMisura = ingrediente.getUnitaMisura();
        Fornitore fornitore = ingrediente.getFornitore();
        DdtAcquisto ddtAcquisto = ddtAcquistoRepository.findById(ddtAcquistoIngrediente.getId().getDdtAcquistoId()).get();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Acquistato/i <b>").append(ddtAcquistoIngrediente.getQuantita()).append("</b>");
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

}
