package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.model.views.VProduzione;
import com.contafood.repository.ProduzioneRepository;
import com.contafood.repository.RicettaRepository;
import com.contafood.repository.views.VProduzioneRepository;
import com.contafood.util.Constants;
import com.contafood.util.LottoUtils;
import com.contafood.util.Utils;
import com.contafood.util.enumeration.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProduzioneService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProduzioneService.class);

    private final ProduzioneRepository produzioneRepository;
    private final VProduzioneRepository vProduzioneRepository;
    private final ProduzioneIngredienteService produzioneIngredienteService;
    private final ProduzioneConfezioneService produzioneConfezioneService;
    private final GiacenzaArticoloService giacenzaArticoloService;
    private final GiacenzaIngredienteService giacenzaIngredienteService;
    private final ArticoloService articoloService;
    private final IngredienteService ingredienteService;
    private final FornitoreService fornitoreService;
    private final ConfezioneService confezioneService;
    private final UnitaMisuraService unitaMisuraService;
    private final AliquotaIvaService aliquotaIvaService;
    private final RicettaRepository ricettaRepository;

    @Autowired
    public ProduzioneService(final ProduzioneRepository produzioneRepository,
                             final VProduzioneRepository vProduzioneRepository,
                             final ProduzioneIngredienteService produzioneIngredienteService,
                             final ProduzioneConfezioneService produzioneConfezioneService,
                             final GiacenzaArticoloService giacenzaArticoloService,
                             final GiacenzaIngredienteService giacenzaIngredienteService,
                             final ArticoloService articoloService,
                             final IngredienteService ingredienteService,
                             final FornitoreService fornitoreService,
                             final ConfezioneService confezioneService,
                             final UnitaMisuraService unitaMisuraService,
                             final AliquotaIvaService aliquotaIvaService,
                             final RicettaRepository ricettaRepository){
        this.produzioneRepository = produzioneRepository;
        this.vProduzioneRepository = vProduzioneRepository;
        this.produzioneIngredienteService = produzioneIngredienteService;
        this.produzioneConfezioneService = produzioneConfezioneService;
        this.giacenzaArticoloService = giacenzaArticoloService;
        this.giacenzaIngredienteService = giacenzaIngredienteService;
        this.articoloService = articoloService;
        this.ingredienteService = ingredienteService;
        this.fornitoreService = fornitoreService;
        this.confezioneService = confezioneService;
        this.unitaMisuraService = unitaMisuraService;
        this.aliquotaIvaService = aliquotaIvaService;
        this.ricettaRepository = ricettaRepository;
    }

    public Set<VProduzione> getAll(){
        LOGGER.info("Retrieving the list of 'produzioni'");
        Set<VProduzione> produzioni = vProduzioneRepository.findAll();
        LOGGER.info("Retrieved {} 'produzioni'", produzioni.size());
        return produzioni;
    }

    public Set<VProduzione> getAllByLotto(String lotto){
        LOGGER.info("Retrieving the list of 'produzioni' filtered by 'lotto' '{}'", lotto);
        Set<VProduzione> produzioni = vProduzioneRepository.findAllByLotto(lotto);
        LOGGER.info("Retrieved {} 'produzioni'", produzioni.size());
        return produzioni;
    }

    public Produzione getOne(Long produzioneId){
        LOGGER.info("Retrieving 'produzione' '{}'", produzioneId);
        Produzione produzione = produzioneRepository.findById(produzioneId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'produzione' '{}'", produzione);
        return produzione;
    }

    @Transactional
    public Produzione create(Produzione produzione){
        LOGGER.info("Creating 'produzione'");

        produzione.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Produzione createdProduzione = produzioneRepository.save(produzione);
        String tipologia = createdProduzione.getTipologia();
        Long produzioneId = createdProduzione.getId();
        Date dataProduzione = createdProduzione.getDataProduzione();
        Date scadenzaProduzione = createdProduzione.getScadenza();
        Long idRicetta = createdProduzione.getRicetta().getId();

        if(produzione.getScopo().equalsIgnoreCase("vendita")){
            LocalDate today = LocalDate.now();
            Integer anno = today.getYear();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uu");
            String yearTwoDigits = dateTimeFormatter.format(today);
            Integer giorno = today.getDayOfYear();
            Integer codice = produzioneRepository.findNextCodiceByLottoAnno(anno).orElse(1);
            String lotto = LottoUtils.createLottoProduzione(yearTwoDigits, codice);

            LOGGER.info("Anno {}, codice {}, lotto {}", anno, codice, lotto);

            createdProduzione.setCodice(codice);
            createdProduzione.setLottoAnno(anno);
            createdProduzione.setLottoGiorno(giorno);
            createdProduzione.setLottoNumeroProgressivo(codice);
            createdProduzione.setLotto(lotto);
        }
        final String createdLotto = createdProduzione.getLotto();

        createdProduzione.getProduzioneIngredienti().stream().forEach(pi -> {
            pi.getId().setProduzioneId(produzioneId);
            pi.getId().setUuid(UUID.randomUUID().toString());

            // compute 'giacenza ingrediente'
            giacenzaIngredienteService.computeGiacenza(pi.getId().getIngredienteId(), pi.getLotto(), pi.getScadenza(), pi.getQuantita(), Resource.PRODUZIONE_INGREDIENTE);

            produzioneIngredienteService.create(pi);
        });
        createdProduzione.getProduzioneConfezioni().stream().forEach(pc -> {
            pc.getId().setProduzioneId(produzioneId);
            pc.setLottoProduzione(createdLotto);

            if(tipologia.equals("SCORTA")){
                Confezione confezione = confezioneService.getOne(pc.getId().getConfezioneId());

                // create, or retrieve, the associated Ingrediente
                Ingrediente ingrediente = getOrCreateIngrediente(confezione, idRicetta);
                pc.setIngrediente(ingrediente);

                float quantita = 0f;
                if(confezione.getPeso() != null){
                    BigDecimal quantitaBd = BigDecimal.valueOf(confezione.getPeso() / 1000);
                    quantitaBd = Utils.roundQuantity(quantitaBd);
                    quantita = quantitaBd.floatValue() * pc.getNumConfezioniProdotte();
                }

                // compute 'giacenza ingrediente'
                giacenzaIngredienteService.computeGiacenza(ingrediente.getId(), createdLotto, scadenzaProduzione, quantita, Resource.PRODUZIONE_SCORTA);

            } else {
                // create, or retrieve, the associated Articolo
                Articolo articolo = getOrCreateArticolo(pc, idRicetta, dataProduzione);
                pc.setArticolo(articolo);

                // compute 'giacenza articolo'
                giacenzaArticoloService.computeGiacenza(articolo.getId(), createdLotto, scadenzaProduzione, pc.getNumConfezioniProdotte().floatValue(), Resource.PRODUZIONE);

            }
            produzioneConfezioneService.create(pc);
        });
        Integer numeroConfezioni = createdProduzione.getProduzioneConfezioni().stream().collect(Collectors.summingInt(ProduzioneConfezione::getNumConfezioni));

        createdProduzione.setNumeroConfezioni(numeroConfezioni);
        createdProduzione = produzioneRepository.save(produzione);

        LOGGER.info("Created 'produzione' '{}'", createdProduzione);
        return createdProduzione;
    }

    /*
    @Transactional
    public Produzione update(Produzione produzione){
        LOGGER.info("Updating 'produzione'");
        Set<ProduzioneIngrediente> produzioneIngredienti = produzione.getProduzioneIngredienti();
        produzione.setProduzioneIngredienti(new HashSet<>());
        produzioneIngredienteService.deleteByProduzioneId(produzione.getId());

        Set<ProduzioneConfezione> produzioneConfezioni = produzione.getProduzioneConfezioni();
        produzione.setProduzioneConfezioni(new HashSet<>());
        produzioneConfezioneService.deleteByProduzioneId(produzione.getId());

        Produzione produzioneCurrente = produzioneRepository.findById(produzione.getId()).orElseThrow(ResourceNotFoundException::new);
        produzione.setDataInserimento(produzioneCurrente.getDataInserimento());
        produzione.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Produzione updatedProduzione = produzioneRepository.save(produzione);
        Long produzioneId = updatedProduzione.getId();

        produzioneIngredienti.stream().forEach(pi -> {
            pi.getId().setProduzioneId(produzioneId);
            pi.getId().setUuid(UUID.randomUUID().toString());
            produzioneIngredienteService.create(pi);

            // compute 'giacenza ingrediente'
            giacenzaIngredienteService.computeGiacenza(pi.getId().getIngredienteId(), pi.getLotto(), pi.getScadenza(), pi.getQuantita(), Resource.PRODUZIONE_INGREDIENTE);
        });
        produzioneConfezioni.stream().forEach(pc -> {
            pc.getId().setProduzioneId(produzioneId);
            produzioneConfezioneService.create(pc);
        });
        Integer numeroConfezioni = updatedProduzione.getProduzioneConfezioni().stream().collect(Collectors.summingInt(ProduzioneConfezione::getNumConfezioni));

        updatedProduzione.setCodice(produzioneId.intValue());
        updatedProduzione.setNumeroConfezioni(numeroConfezioni);
        updatedProduzione = produzioneRepository.save(produzione);

        // create 'articolo', if not exists
        LOGGER.info("Creating 'articolo' if not already exists...");
        Fornitore fornitore = fornitoreService.getByRagioneSociale(Constants.DEFAULT_FORNITORE);
        String codiceArticolo = Constants.DEFAULT_FORNITORE_INITIALS + updatedProduzione.getRicetta().getCodice();
        Optional<Articolo> optionalArticolo = articoloService.getByCodice(codiceArticolo);
        Articolo articolo;
        if(!optionalArticolo.isPresent()){
            articolo = new Articolo();
            articolo.setCodice(codiceArticolo);
            articolo.setDescrizione(updatedProduzione.getRicetta().getNome());
            articolo.setFornitore(fornitore);
            articolo.setData(updatedProduzione.getDataProduzione());
            articolo.setQuantitaPredefinita(updatedProduzione.getQuantitaTotale());
            articolo.setSitoWeb(Boolean.FALSE);
            articolo.setAttivo(Boolean.TRUE);
            articolo = articoloService.create(articolo);
            LOGGER.info("Created 'articolo' '{}' from produzione", articolo);
        } else {
            LOGGER.info("The 'articolo' with 'codice' '{}' already exists", codiceArticolo);
            articolo = optionalArticolo.get();
        }

        // compute 'giacenza articolo'
        giacenzaArticoloService.computeGiacenza(articolo.getId(), updatedProduzione.getLotto(), updatedProduzione.getScadenza(), updatedProduzione.getQuantitaTotale(), Resource.PRODUZIONE);

        LOGGER.info("Updated 'produzione' '{}'", updatedProduzione);
        return updatedProduzione;
    }
     */

    @Transactional
    public void delete(Long produzioneId){
        LOGGER.info("Deleting 'produzione' '{}'", produzioneId);
        Produzione produzione = produzioneRepository.findById(produzioneId).orElseThrow(ResourceNotFoundException::new);
        String tipologia = produzione.getTipologia();

        Set<ProduzioneConfezione> produzioneConfezioni = produzioneConfezioneService.findByProduzioneId(produzioneId);
        if(produzioneConfezioni != null && !produzioneConfezioni.isEmpty()){
            for(ProduzioneConfezione produzioneConfezione : produzioneConfezioni){
                Ricetta ricetta = ricettaRepository.findById(produzione.getRicetta().getId()).orElse(null);
                Confezione confezione = confezioneService.getOne(produzioneConfezione.getId().getConfezioneId());

                if(tipologia.equals("SCORTA")){
                    String codiceIngrediente = createCodiceIngrediente(ricetta, confezione);
                    Optional<Ingrediente> optionalIngrediente = ingredienteService.getByCodice(codiceIngrediente);
                    if(optionalIngrediente.isPresent()){
                        Ingrediente ingrediente = optionalIngrediente.get();

                        float quantita = 0f;
                        if(confezione.getPeso() != 0){
                            BigDecimal quantitaBd = BigDecimal.valueOf(confezione.getPeso() / 1000);
                            quantitaBd = Utils.roundQuantity(quantitaBd);
                            quantita = quantitaBd.floatValue() * produzioneConfezione.getNumConfezioniProdotte();
                        }

                        // compute 'giacenza ingrediente'
                        giacenzaIngredienteService.computeGiacenza(ingrediente.getId(), produzioneConfezione.getLotto(), produzione.getScadenza(), (quantita *-1 ), Resource.PRODUZIONE_SCORTA);
                    }

                } else {
                    String codiceArticolo = createCodiceArticolo(ricetta, confezione);
                    Optional<Articolo> optionalArticolo = articoloService.getByCodice(codiceArticolo);
                    if(optionalArticolo.isPresent()){
                        Articolo articolo = optionalArticolo.get();

                        // compute 'giacenza articolo'
                        giacenzaArticoloService.computeGiacenza(articolo.getId(), produzioneConfezione.getLotto(), produzione.getScadenza(), (produzioneConfezione.getNumConfezioniProdotte() != null ? (produzioneConfezione.getNumConfezioniProdotte()*-1) : 0f), Resource.PRODUZIONE);
                    }
                }
            }
        }

        Set<ProduzioneIngrediente> produzioneIngredienti = produzioneIngredienteService.findByProduzioneId(produzioneId);

        for(ProduzioneIngrediente produzioneIngrediente : produzioneIngredienti){
            // compute 'giacenza ingrediente'
            giacenzaIngredienteService.computeGiacenza(produzioneIngrediente.getId().getIngredienteId(), produzioneIngrediente.getLotto(), produzioneIngrediente.getScadenza(), produzioneIngrediente.getQuantita()*-1, Resource.PRODUZIONE_INGREDIENTE);
        }

        produzioneIngredienteService.deleteByProduzioneId(produzioneId);
        produzioneConfezioneService.deleteByProduzioneId(produzioneId);
        produzioneRepository.deleteById(produzioneId);
        LOGGER.info("Deleted 'produzione' '{}'", produzioneId);
    }

    @Transactional
    public void deleteByRicettaId(Long idRicetta){
        LOGGER.info("Deleting 'produzioni' of 'ricetta' '{}'", idRicetta);
        List<Produzione> produzioniToDelete = produzioneRepository.findByRicettaId(idRicetta);
        produzioniToDelete.forEach(p -> delete(p.getId()));
        LOGGER.info("Deleted 'produzioni' of 'ricetta' '{}'", idRicetta);
    }

    public Set<ProduzioneConfezione> getProduzioneConfezioni(Long produzioneId){
        LOGGER.info("Retrieving 'produzioneConfezioni' for produzione '{}'", produzioneId);
        Set<ProduzioneConfezione> produzioneConfezioni = produzioneConfezioneService.findByProduzioneId(produzioneId);
        LOGGER.info("Retrieved {} 'produzioneConfezioni'", produzioneConfezioni.size());
        return produzioneConfezioni;
    }

    private Articolo getOrCreateArticolo(ProduzioneConfezione produzioneConfezione, Long idRicetta, Date dataProduzione){
        LOGGER.info("Creating or retrieving associated 'articolo'...");

        // retrieve Ricetta
        Ricetta ricetta = ricettaRepository.findById(idRicetta).orElse(null);

        // retrieve default Fornitore
        Fornitore fornitore = fornitoreService.getByRagioneSociale(Constants.DEFAULT_FORNITORE);

        // retrieve Confezione
        Confezione confezione = confezioneService.getOne(produzioneConfezione.getId().getConfezioneId());

        String codiceArticolo = createCodiceArticolo(ricetta, confezione);

        Optional<Articolo> optionalArticolo = articoloService.getByCodice(codiceArticolo);
        Articolo articolo;

        if(!optionalArticolo.isPresent()){
            articolo = new Articolo();
            articolo.setCodice(codiceArticolo);
            articolo.setDescrizione(createDescrizioneArticolo(ricetta, confezione));
            articolo.setFornitore(fornitore);
            articolo.setData(dataProduzione);
            articolo.setQuantitaPredefinita(1f);
            articolo.setSitoWeb(Boolean.FALSE);
            articolo.setAttivo(Boolean.TRUE);
            articolo = articoloService.create(articolo);
            LOGGER.info("Created 'articolo' '{}' from produzione", articolo);
        } else {
            LOGGER.info("Retrieved 'articolo' with 'codice' '{}'", codiceArticolo);
            articolo = optionalArticolo.get();
        }

        return articolo;
    }

    private Ingrediente getOrCreateIngrediente(Confezione confezione, Long idRicetta){
        LOGGER.info("Creating or retrieving associated 'ingrediente'...");

        // retrieve Ricetta
        Ricetta ricetta = ricettaRepository.findById(idRicetta).orElse(null);

        // retrieve default Fornitore
        Fornitore fornitore = fornitoreService.getByRagioneSociale(Constants.DEFAULT_FORNITORE);

        String codiceIngrediente = createCodiceIngrediente(ricetta, confezione);

        Optional<Ingrediente> optionalIngrediente = ingredienteService.getByCodice(codiceIngrediente);
        Ingrediente ingrediente;

        if(!optionalIngrediente.isPresent()){
            ingrediente = new Ingrediente();
            ingrediente.setCodice(codiceIngrediente);
            ingrediente.setDescrizione(createDescrizioneIngrediente(ricetta, confezione));
            ingrediente.setFornitore(fornitore);
            ingrediente.setPrezzo(createPrezzoIngredienteScorta(ricetta));
            ingrediente.setUnitaMisura(unitaMisuraService.getByNome("kg"));
            ingrediente.setAliquotaIva(aliquotaIvaService.getOne(2L)); // 10%
            ingrediente.setAttivo(Boolean.TRUE);
            ingrediente = ingredienteService.create(ingrediente);
            LOGGER.info("Created 'ingrediente' '{}' from produzione", ingrediente);
        } else {
            LOGGER.info("Retrieved 'ingrediente' with 'codice' '{}'", codiceIngrediente);
            ingrediente = optionalIngrediente.get();
        }

        return ingrediente;
    }

    private String createCodiceArticolo(Ricetta ricetta, Confezione confezione){
        float pesoConfezione = confezione.getPeso()/1000;
        String peso = Float.toString(pesoConfezione).replace(".", ",");
        if(peso.contains(",0")){
            peso = StringUtils.substringBefore(peso,",");
        }

        return Constants.DEFAULT_FORNITORE_INITIALS + (ricetta != null ? ricetta.getCodice() : "")+peso;
    }

    private String createCodiceIngrediente(Ricetta ricetta, Confezione confezione){
        float pesoConfezione = confezione.getPeso()/1000;
        String peso = Float.toString(pesoConfezione).replace(".", ",");
        if(peso.contains(",0")){
            peso = StringUtils.substringBefore(peso,",");
        }

        return Constants.DEFAULT_FORNITORE_INITIALS + (ricetta != null ? ricetta.getCodice() : "")+peso;
    }

    private String createDescrizioneArticolo(Ricetta ricetta, Confezione confezione){
        float pesoConfezione = confezione.getPeso()/1000;
        String peso = Float.toString(pesoConfezione).replace(".", ",");
        if(peso.contains(",0")){
            peso = StringUtils.substringBefore(peso,",");
        }

        return ricetta.getNome()+" "+peso+"kg";
    }

    private String createDescrizioneIngrediente(Ricetta ricetta, Confezione confezione){
        float pesoConfezione = confezione.getPeso()/1000;
        String peso = Float.toString(pesoConfezione).replace(".", ",");
        if(peso.contains(",0")){
            peso = StringUtils.substringBefore(peso,",");
        }

        return ricetta.getNome()+" "+peso+"kg";
    }

    private BigDecimal createPrezzoIngredienteScorta(Ricetta ricetta){
        BigDecimal prezzo = new BigDecimal(1);
        BigDecimal costoTotale = ricetta.getCostoTotale();
        float pesoTotale = ricetta.getPesoTotale() != null ? ricetta.getPesoTotale() : 1f;

        if(costoTotale != null){
            prezzo = costoTotale.divide(BigDecimal.valueOf(pesoTotale));
        }
        return prezzo;
    }

}
