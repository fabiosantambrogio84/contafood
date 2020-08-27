package com.contafood.service;

import com.contafood.model.*;
import com.contafood.repository.*;
import com.contafood.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    private final ProduzioneConfezioneRepository produzioneConfezioneRepository;
    private final MovimentazioneManualeArticoloService movimentazioneManualeArticoloService;
    private final MovimentazioneManualeIngredienteService movimentazioneManualeIngredienteService;

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
                                 final ProduzioneRepository produzioneRepository,
                                 final ProduzioneConfezioneRepository produzioneConfezioneRepository,
                                 final MovimentazioneManualeArticoloService movimentazioneManualeArticoloService,
                                 final MovimentazioneManualeIngredienteService movimentazioneManualeIngredienteService){
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
        this.produzioneConfezioneRepository = produzioneConfezioneRepository;
        this.movimentazioneManualeArticoloService = movimentazioneManualeArticoloService;
        this.movimentazioneManualeIngredienteService = movimentazioneManualeIngredienteService;
    }

    public Set<Movimentazione> getMovimentazioniArticolo(GiacenzaArticolo giacenzaArticolo){
        LOGGER.info("Retrieving 'movimentazioni' of 'giacenza articolo' '{}'", giacenzaArticolo.getId());
        Set<Movimentazione> movimentazioni = new HashSet<>();
        Set<DdtAcquistoArticolo> ddtAcquistoArticoli = new HashSet<>();
        Set<DdtArticolo> ddtArticoli = new HashSet<>();
        Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = new HashSet<>();
        Set<ProduzioneConfezione> produzioneConfezioni = new HashSet<>();
        Set<MovimentazioneManualeArticolo> movimentazioniManualiArticoli = new HashSet<>();

        Articolo articolo = articoloRepository.findById(giacenzaArticolo.getArticolo().getId()).get();

        // retrieve the set of 'DdtAcquistoArticolo'
        ddtAcquistoArticoli = ddtAcquistoArticoloService.getByArticoloIdAndLottoAndScadenza(articolo.getId(), giacenzaArticolo.getLotto(), giacenzaArticolo.getScadenza());

        // retrieve the set of 'DdtArticolo'
        ddtArticoli = ddtArticoloService.getByArticoloIdAndLottoAndScadenza(articolo.getId(), giacenzaArticolo.getLotto(), giacenzaArticolo.getScadenza());

        // retrieve the set of 'FatturaAccompagnatoriaArticolo'
        fatturaAccompagnatoriaArticoli = fatturaAccompagnatoriaArticoloService.getByArticoloIdAndLottoAndScadenza(articolo.getId(), giacenzaArticolo.getLotto(), giacenzaArticolo.getScadenza());

        // retrieve the 'Ricetta' associated to the 'Articolo'
        String codiceRicetta = articolo.getCodice().substring(2);

        // retrieve the set of 'ProduzioniConfezioni'
        produzioneConfezioni = produzioneConfezioneRepository.findByArticoloIdAndLotto(articolo.getId(), giacenzaArticolo.getLotto());
        if(produzioneConfezioni != null && !produzioneConfezioni.isEmpty()){
            Set<Long> produzioniIds = new HashSet<>();
            produzioneConfezioni.forEach(pc -> {
                Produzione produzione = produzioneRepository.findById(pc.getId().getProduzioneId()).get();
                if(giacenzaArticolo.getScadenza() != null){
                    if(produzione.getScadenza() != null && produzione.getScadenza().toLocalDate().compareTo(giacenzaArticolo.getScadenza().toLocalDate())==0){
                        produzioniIds.add(produzione.getId());
                    }
                } else {
                    produzioniIds.add(produzione.getId());
                }
            });
            produzioneConfezioni = produzioneConfezioni.stream().filter(pc -> (produzioniIds.contains(pc.getId().getProduzioneId()))).collect(Collectors.toSet());

        }

        // retrieve the set of 'MovimentazioniManualiArticoli'
        movimentazioniManualiArticoli = movimentazioneManualeArticoloService.getByArticoloIdAndLottoAndScadenza(articolo.getId(), giacenzaArticolo.getLotto(), giacenzaArticolo.getScadenza());

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
                movimentazione.setDescrizione(createDescrizione(daa, giacenzaArticolo, articolo));

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
                movimentazione.setDescrizione(createDescrizione(da, giacenzaArticolo, articolo));

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
                movimentazione.setDescrizione(createDescrizione(faa, giacenzaArticolo, articolo));

                movimentazioni.add(movimentazione);
            });
        }

        // Create 'movimentazione' from 'ProduzioneConfezione'
        if(produzioneConfezioni != null && !produzioneConfezioni.isEmpty()){
            produzioneConfezioni.forEach(pc -> {
                Produzione produzione = produzioneRepository.findById(pc.getId().getProduzioneId()).get();

                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenzaArticolo.getId());
                movimentazione.setInputOutput(INPUT);
                movimentazione.setData(produzione.getDataProduzione());
                movimentazione.setQuantita(pc.getNumConfezioniProdotte() != null ? pc.getNumConfezioniProdotte().floatValue() : 0f);
                movimentazione.setDescrizione(createDescrizione(produzione, giacenzaArticolo.getLotto(), giacenzaArticolo.getScadenza(), (pc.getNumConfezioniProdotte() != null ? pc.getNumConfezioniProdotte().floatValue() : 0f)));

                movimentazioni.add(movimentazione);
            });
        }

        // Create 'movimentazione' from 'MovimentazioneManualeArticolo'
        if(movimentazioniManualiArticoli != null && !movimentazioniManualiArticoli.isEmpty()){
            movimentazioniManualiArticoli.forEach(mma -> {
                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenzaArticolo.getId());
                movimentazione.setInputOutput(INPUT);
                movimentazione.setData(new Date(mma.getDataInserimento().getTime()));
                movimentazione.setQuantita(mma.getQuantita());
                movimentazione.setDescrizione(createDescrizione(mma, giacenzaArticolo, articolo));

                movimentazioni.add(movimentazione);
            });
        }

        LOGGER.info("Retrieved '{}' 'movimentazioni' for 'giacenza articolo' '{}'", movimentazioni.size(), giacenzaArticolo.getId());
        return movimentazioni;
    }

    public Set<Movimentazione> getMovimentazioniIngrediente(GiacenzaIngrediente giacenzaIngrediente){
        LOGGER.info("Retrieving 'movimentazioni' of 'giacenza ingrediente' '{}'", giacenzaIngrediente.getId());
        Set<Movimentazione> movimentazioni = new HashSet<>();
        Set<DdtAcquistoIngrediente> ddtAcquistoIngredienti = new HashSet<>();
        Set<ProduzioneIngrediente> produzioneIngredienti = new HashSet<>();
        Set<MovimentazioneManualeIngrediente> movimentazioniManualiIngredienti = new HashSet<>();

        Ingrediente ingrediente = ingredienteRepository.findById(giacenzaIngrediente.getIngrediente().getId()).get();

        // retrieve the set of 'DdtAcquistoIngrediente'
        ddtAcquistoIngredienti = ddtAcquistoIngredienteService.getByIngredienteIdAndLottoAndScadenza(ingrediente.getId(), giacenzaIngrediente.getLotto(), giacenzaIngrediente.getScadenza());

        // retrieve the set of 'ProduzioneIngrediente'
        produzioneIngredienti = produzioneIngredienteService.getByIngredienteIdAndLottoAndScadenza(ingrediente.getId(), giacenzaIngrediente.getLotto(), giacenzaIngrediente.getScadenza());

        // retrieve the set of 'MovimentazioneManualeIngrediente'
        movimentazioniManualiIngredienti = movimentazioneManualeIngredienteService.getByIngredienteIdAndLottoAndScadenza(ingrediente.getId(), giacenzaIngrediente.getLotto(), giacenzaIngrediente.getScadenza());

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
                movimentazione.setDescrizione(createDescrizione(dai, giacenzaIngrediente, ingrediente));

                movimentazioni.add(movimentazione);
            });
        }

        // Create 'movimentazione' from 'ProduzioneIngrediente'
        if(produzioneIngredienti != null && !produzioneIngredienti.isEmpty()){
            produzioneIngredienti.forEach(pi -> {
                Produzione produzione = produzioneRepository.findById(pi.getId().getProduzioneId()).get();

                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenzaIngrediente.getId());
                movimentazione.setInputOutput(OUTPUT);
                movimentazione.setData(produzione.getDataProduzione());
                movimentazione.setQuantita(pi.getQuantita());
                movimentazione.setDescrizione(createDescrizione(produzione, giacenzaIngrediente.getLotto(), giacenzaIngrediente.getScadenza(), pi.getQuantita()));

                movimentazioni.add(movimentazione);
            });
        }

        // Create 'movimentazione' for 'MovimentazioneManualeIngrediente'
        if(movimentazioniManualiIngredienti != null && !movimentazioniManualiIngredienti.isEmpty()){
            movimentazioniManualiIngredienti.forEach(mmi -> {
                Movimentazione movimentazione = new Movimentazione();
                movimentazione.setIdGiacenza(giacenzaIngrediente.getId());
                movimentazione.setInputOutput(INPUT);
                movimentazione.setData(new Date(mmi.getDataInserimento().getTime()));
                movimentazione.setQuantita(mmi.getQuantita());
                movimentazione.setDescrizione(createDescrizione(mmi, giacenzaIngrediente, ingrediente));

                movimentazioni.add(movimentazione);
            });
        }

        LOGGER.info("Retrieved '{}' 'movimentazioni' for 'giacenza ingrediente' '{}'", movimentazioni.size(), giacenzaIngrediente.getId());
        return movimentazioni;
    }

    private String createDescrizione(DdtAcquistoArticolo ddtAcquistoArticolo, GiacenzaArticolo giacenzaArticolo, Articolo articolo){
        UnitaMisura unitaMisura = articolo.getUnitaMisura();
        Fornitore fornitore = articolo.getFornitore();
        DdtAcquisto ddtAcquisto = ddtAcquistoRepository.findById(ddtAcquistoArticolo.getId().getDdtAcquistoId()).get();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Acquistato/i <b>").append(ddtAcquistoArticolo.getQuantita()).append("</b>");
        if(unitaMisura != null){
            stringBuilder.append(" ").append(unitaMisura.getEtichetta());
        }
        stringBuilder.append(" il ").append(simpleDateFormat.format(ddtAcquisto.getData()));
        stringBuilder.append(" lotto <b>").append(giacenzaArticolo.getLotto()).append("</b>");
        stringBuilder.append(" scadenza <b>");
        if(giacenzaArticolo.getScadenza() != null){
            stringBuilder.append(simpleDateFormat.format(giacenzaArticolo.getScadenza()));
        } else {
            stringBuilder.append("ND");
        }
        stringBuilder.append("</b>");
        stringBuilder.append(" (DDT acquisto n. ").append(ddtAcquisto.getNumero());
        if(fornitore != null){
            stringBuilder.append(" da ");
            stringBuilder.append(fornitore.getRagioneSociale());
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    private String createDescrizione(DdtArticolo ddtArticolo, GiacenzaArticolo giacenzaArticolo, Articolo articolo){
        UnitaMisura unitaMisura = articolo.getUnitaMisura();
        Ddt ddt = ddtRepository.findById(ddtArticolo.getId().getDdtId()).get();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Venduto/i <b>").append(ddtArticolo.getQuantita()).append("</b>");
        if(unitaMisura != null){
            stringBuilder.append(" ").append(unitaMisura.getEtichetta());
        }
        stringBuilder.append(" il ").append(simpleDateFormat.format(ddt.getData()));
        stringBuilder.append(" lotto <b>").append(giacenzaArticolo.getLotto()).append("</b>");
        stringBuilder.append(" scadenza <b>");
        if(giacenzaArticolo.getScadenza() != null){
            stringBuilder.append(simpleDateFormat.format(giacenzaArticolo.getScadenza()));
        } else {
            stringBuilder.append("ND");
        }
        stringBuilder.append("</b>");
        stringBuilder.append(" (DDT n. ").append(ddt.getProgressivo()).append("/").append(ddt.getAnnoContabile()).append(")");

        return stringBuilder.toString();
    }

    private String createDescrizione(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo, GiacenzaArticolo giacenzaArticolo, Articolo articolo){
        UnitaMisura unitaMisura = articolo.getUnitaMisura();
        FatturaAccompagnatoria fatturaAccompagnatoria = fatturaAccompagnatoriaRepository.findById(fatturaAccompagnatoriaArticolo.getId().getFatturaAccompagnatoriaId()).get();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Venduto/i <b>").append(fatturaAccompagnatoriaArticolo.getQuantita()).append("</b>");
        if(unitaMisura != null){
            stringBuilder.append(" ").append(unitaMisura.getEtichetta());
        }
        stringBuilder.append(" il ").append(simpleDateFormat.format(fatturaAccompagnatoria.getData()));
        stringBuilder.append(" lotto <b>").append(giacenzaArticolo.getLotto()).append("</b>");
        stringBuilder.append(" scadenza <b>");
        if(giacenzaArticolo.getScadenza() != null){
            stringBuilder.append(simpleDateFormat.format(giacenzaArticolo.getScadenza()));
        } else {
            stringBuilder.append("ND");
        }
        stringBuilder.append("</b>");
        stringBuilder.append(" (Fattura accompagnatoria n. ").append(fatturaAccompagnatoria.getProgressivo()).append("/").append(fatturaAccompagnatoria.getAnno()).append(")");

        return stringBuilder.toString();
    }

    private String createDescrizione(Produzione produzione, String lotto, Date scadenza, Float quantita){
        Fornitore fornitore = fornitoreService.getByRagioneSociale(Constants.DEFAULT_FORNITORE);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Prodotto/i <b>").append(quantita).append("</b> pz");
        stringBuilder.append(" il ").append(simpleDateFormat.format(produzione.getDataProduzione()));
        stringBuilder.append(" lotto <b>").append(lotto).append("</b>");
        stringBuilder.append(" scadenza <b>");
        if(scadenza != null){
            stringBuilder.append(simpleDateFormat.format(scadenza));
        } else {
            stringBuilder.append("ND");
        }
        stringBuilder.append("</b>");
        stringBuilder.append(" (Produzione n. ").append(produzione.getCodice());
        if(fornitore != null){
            stringBuilder.append(" da ");
            stringBuilder.append(fornitore.getRagioneSociale());
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    private String createDescrizione(MovimentazioneManualeArticolo movimentazioneManualeArticolo, GiacenzaArticolo giacenzaArticolo, Articolo articolo){
        UnitaMisura unitaMisura = articolo.getUnitaMisura();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Inserito/i <b>").append(movimentazioneManualeArticolo.getQuantita()).append("</b>");
        if(unitaMisura != null){
            stringBuilder.append(" ").append(unitaMisura.getEtichetta());
        }
        stringBuilder.append(" il ").append(simpleDateFormat.format(new Date(movimentazioneManualeArticolo.getDataInserimento().getTime())));
        stringBuilder.append(" lotto <b>").append(giacenzaArticolo.getLotto()).append("</b>");
        stringBuilder.append(" scadenza <b>");
        if(movimentazioneManualeArticolo.getScadenza() != null){
            stringBuilder.append(simpleDateFormat.format(movimentazioneManualeArticolo.getScadenza()));
        } else {
            stringBuilder.append("ND");
        }
        stringBuilder.append("</b>");
        stringBuilder.append(" (Inserimento manuale)");

        return stringBuilder.toString();
    }

    private String createDescrizione(DdtAcquistoIngrediente ddtAcquistoIngrediente, GiacenzaIngrediente giacenzaIngrediente, Ingrediente ingrediente){
        UnitaMisura unitaMisura = ingrediente.getUnitaMisura();
        Fornitore fornitore = ingrediente.getFornitore();
        DdtAcquisto ddtAcquisto = ddtAcquistoRepository.findById(ddtAcquistoIngrediente.getId().getDdtAcquistoId()).get();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Acquistato/i <b>").append(ddtAcquistoIngrediente.getQuantita()).append("</b>");
        if(unitaMisura != null){
            stringBuilder.append(" ").append(unitaMisura.getEtichetta());
        } else {
            stringBuilder.append("ND");
        }
        stringBuilder.append(" il ").append(simpleDateFormat.format(ddtAcquisto.getData()));
        stringBuilder.append(" lotto <b>").append(giacenzaIngrediente.getLotto()).append("</b>");
        stringBuilder.append(" scadenza <b>");
        if(giacenzaIngrediente.getScadenza() != null){
            stringBuilder.append(simpleDateFormat.format(giacenzaIngrediente.getScadenza()));
        } else {
            stringBuilder.append("ND");
        }
        stringBuilder.append(" (DDT acquisto n. ").append(ddtAcquisto.getNumero());
        if(fornitore != null){
            stringBuilder.append(" da ");
            stringBuilder.append(fornitore.getRagioneSociale());
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    private String createDescrizione(MovimentazioneManualeIngrediente movimentazioneManualeIngrediente, GiacenzaIngrediente giacenzaIngrediente, Ingrediente ingrediente){
        UnitaMisura unitaMisura = ingrediente.getUnitaMisura();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Inserito/i <b>").append(movimentazioneManualeIngrediente.getQuantita()).append("</b>");
        if(unitaMisura != null){
            stringBuilder.append(" ").append(unitaMisura.getEtichetta());
        }
        stringBuilder.append(" il ").append(simpleDateFormat.format(new Date(movimentazioneManualeIngrediente.getDataInserimento().getTime())));
        stringBuilder.append(" lotto <b>").append(giacenzaIngrediente.getLotto()).append("</b>");
        stringBuilder.append(" scadenza <b>");
        if(movimentazioneManualeIngrediente.getScadenza() != null){
            stringBuilder.append(simpleDateFormat.format(movimentazioneManualeIngrediente.getScadenza()));
        } else {
            stringBuilder.append("ND");
        }
        stringBuilder.append("</b>");
        stringBuilder.append(" (Inserimento manuale)");

        return stringBuilder.toString();
    }

}
