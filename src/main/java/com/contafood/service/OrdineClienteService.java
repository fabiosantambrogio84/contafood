package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.model.views.VOrdineClienteArticolo;
import com.contafood.model.views.VOrdineClienteArticoloDaEvadere;
import com.contafood.model.views.VOrdineFornitoreArticolo;
import com.contafood.repository.DdtArticoloOrdineClienteRepository;
import com.contafood.repository.OrdineClienteRepository;
import com.contafood.repository.views.VOrdineClienteArticoloDaEvadereRepository;
import com.contafood.repository.views.VOrdineClienteArticoloRepository;
import com.contafood.util.enumeration.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OrdineClienteService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrdineClienteService.class);

    private final OrdineClienteRepository ordineClienteRepository;
    private final OrdineClienteArticoloService ordineClienteArticoloService;
    private final StatoOrdineService statoOrdineService;
    private final ListinoPrezzoService listinoPrezzoService;
    private final DdtArticoloOrdineClienteRepository ddtArticoloOrdineClienteRepository;
    private final VOrdineClienteArticoloRepository vOrdineClienteArticoloRepository;
    private final VOrdineClienteArticoloDaEvadereRepository vOrdineClienteArticoloDaEvadereRepository;

    public OrdineClienteService(final OrdineClienteRepository ordineClienteRepository,
                                final OrdineClienteArticoloService ordineClienteArticoloService,
                                final StatoOrdineService statoOrdineService,
                                final ListinoPrezzoService listinoPrezzoService,
                                final DdtArticoloOrdineClienteRepository ddtArticoloOrdineClienteRepository,
                                final VOrdineClienteArticoloRepository vOrdineClienteArticoloRepository,
                                final VOrdineClienteArticoloDaEvadereRepository vOrdineClienteArticoloDaEvadereRepository){
        this.ordineClienteRepository = ordineClienteRepository;
        this.ordineClienteArticoloService = ordineClienteArticoloService;
        this.statoOrdineService = statoOrdineService;
        this.listinoPrezzoService = listinoPrezzoService;
        this.ddtArticoloOrdineClienteRepository = ddtArticoloOrdineClienteRepository;
        this.vOrdineClienteArticoloRepository = vOrdineClienteArticoloRepository;
        this.vOrdineClienteArticoloDaEvadereRepository = vOrdineClienteArticoloDaEvadereRepository;
    }

    public Set<OrdineCliente> getAll(){
        LOGGER.info("Retrieving the list of 'ordini clienti'");
        Set<OrdineCliente> ordiniClienti = ordineClienteRepository.findAllByOrderByAnnoContabileDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'ordini clienti'", ordiniClienti.size());
        return ordiniClienti;
    }

    public OrdineCliente getOne(Long ordineClienteId){
        LOGGER.info("Retrieving 'ordineCliente' '{}'", ordineClienteId);
        OrdineCliente ordineCliente = ordineClienteRepository.findById(ordineClienteId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'ordineCliente' '{}'", ordineCliente);
        LOGGER.info("Setting prices...");
        Listino listino = ordineCliente.getCliente().getListino();
        if(listino != null){
            List<ListinoPrezzo> listiniPrezzi = listinoPrezzoService.getByListinoId(listino.getId());
            if(!listiniPrezzi.isEmpty()){
                final Map<Long, BigDecimal> articoloPrezzoMap = listiniPrezzi
                        .stream()
                        .collect(Collectors.toMap(lp -> lp.getArticolo().getId(), ListinoPrezzo::getPrezzo));

                ordineCliente.getOrdineClienteArticoli().forEach(a -> a.setPrezzo(articoloPrezzoMap.get(a.getId().getArticoloId())));
            }
        }
        LOGGER.info("Prices successfully set");
        return ordineCliente;
    }

    public Set<OrdineCliente> getByIdClienteAndIdPuntoConsegnaAndDataConsegnaLessOrEqualAndIdStatoNot(Long idCliente, Long idPuntoConsegna, Date dataConsegna, Long idStato){
        LOGGER.info("Retrieving the list of 'ordini clienti' with idCliente '{}', idPuntoConsegna '{}', dataConsegna <= '{}', idStato '{}'", idCliente, idPuntoConsegna, dataConsegna, idStato);
        Set<OrdineCliente> ordiniClienti = ordineClienteRepository.findByClienteIdAndPuntoConsegnaId(idCliente, idPuntoConsegna);

        Predicate<OrdineCliente> isOrdineClienteDataConsegnaLessOrEquals = ordineCliente -> {
            if(dataConsegna != null){
                return ordineCliente.getDataConsegna().compareTo(dataConsegna)<=0;
            }
            return true;
        };

        Predicate<OrdineCliente> isOrdineClienteStatoNotEquals = ordineCliente -> {
            if(idStato != null){
                StatoOrdine statoOrdine = ordineCliente.getStatoOrdine();
                if(statoOrdine != null){
                    return !statoOrdine.getId().equals(idStato);
                }
                return false;
            }
            return true;
        };

        ordiniClienti = ordiniClienti.stream().filter(isOrdineClienteDataConsegnaLessOrEquals.and(isOrdineClienteStatoNotEquals)).collect(Collectors.toSet());

        LOGGER.info("Retrieved {} 'ordini clienti'", ordiniClienti.size());
        return ordiniClienti;
    }

    public Set<OrdineCliente> getByIdTelefonata(Long idTelefonata){
        return ordineClienteRepository.findByTelefonataId(idTelefonata);
    }

    public Set<OrdineCliente> getOrdiniClientiEvasiAndExpired(Integer days){
        Integer finalDays = days != null ? days : 1;
        LOGGER.info("Retrieving the list of 'ordini clienti' with stato 'EVASO' and expired (dataConsegna <= now-{})", days);
        Set<OrdineCliente> ordiniClienti = ordineClienteRepository.findByStatoOrdineId(statoOrdineService.getEvaso().getId());
        ordiniClienti = ordiniClienti.stream().filter(oc -> oc.getDataConsegna().compareTo(Date.valueOf(LocalDate.now().minusDays(finalDays)))<=0).collect(Collectors.toSet());
        LOGGER.info("Retrieved {} 'ordini clienti'", ordiniClienti.size());
        return ordiniClienti;
    }

    public Map<String, Integer> getAnnoContabileAndProgressivo(){
        Integer annoContabile = ZonedDateTime.now().getYear();
        int progressivo = getProgressivo(annoContabile);

        HashMap<String, Integer> result = new HashMap<>();
        result.put("annoContabile", annoContabile);
        result.put("progressivo", progressivo);

        return result;
    }

    private Integer getProgressivo(Integer annoContabile){
        int progressivo = 1;
        List<OrdineCliente> ordiniClienti = ordineClienteRepository.findByAnnoContabileOrderByProgressivoDesc(annoContabile);
        if(ordiniClienti != null && !ordiniClienti.isEmpty()){
            Optional<OrdineCliente> lastOrdineCliente = ordiniClienti.stream().findFirst();
            if(lastOrdineCliente.isPresent()){
                progressivo = lastOrdineCliente.get().getProgressivo() + 1;
            }
        }
        return progressivo;
    }

    @Transactional
    public OrdineCliente create(OrdineCliente ordineCliente){
        LOGGER.info("Creating 'ordineCliente'");

        Integer progressivo = ordineCliente.getProgressivo();
        if(progressivo == null){
            progressivo = getProgressivo(ordineCliente.getAnnoContabile());
            ordineCliente.setProgressivo(progressivo);
        }

        checkExistsByAnnoContabileAndProgressivoAndIdNot(ordineCliente.getAnnoContabile(), ordineCliente.getProgressivo(), -1L);

        ordineCliente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        StatoOrdine statoOrdineDaEvadere = statoOrdineService.getDaEvadere();
        ordineCliente.setStatoOrdine(statoOrdineDaEvadere);

        OrdineCliente createdOrdineCliente = ordineClienteRepository.save(ordineCliente);

        createdOrdineCliente.getOrdineClienteArticoli().stream().forEach(oca -> {
            oca.getId().setOrdineClienteId(createdOrdineCliente.getId());
            ordineClienteArticoloService.create(oca);
        });

        ordineClienteRepository.save(createdOrdineCliente);
        LOGGER.info("Created 'ordineCliente' '{}'", createdOrdineCliente);
        return createdOrdineCliente;
    }

    @Transactional
    public OrdineCliente update(OrdineCliente ordineCliente){
        LOGGER.info("Updating 'ordineCliente'");

        Integer progressivo = ordineCliente.getProgressivo();
        if(progressivo.equals(null)){
            progressivo = getProgressivo(ordineCliente.getAnnoContabile());
            ordineCliente.setProgressivo(progressivo);
        }

        checkExistsByAnnoContabileAndProgressivoAndIdNot(ordineCliente.getAnnoContabile(), ordineCliente.getProgressivo(), ordineCliente.getId());

        Set<OrdineClienteArticolo> ordineClienteArticoli = ordineCliente.getOrdineClienteArticoli();
        ordineCliente.setOrdineClienteArticoli(new HashSet<>());
        ordineClienteArticoloService.deleteByOrdineClienteId(ordineCliente.getId());

        OrdineCliente ordineClienteCurrent = ordineClienteRepository.findById(ordineCliente.getId()).orElseThrow(ResourceNotFoundException::new);
        ordineCliente.setDataInserimento(ordineClienteCurrent.getDataInserimento());
        ordineCliente.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        OrdineCliente updatedOrdineCliente = ordineClienteRepository.save(ordineCliente);
        ordineClienteArticoli.stream().forEach(oca -> {
            oca.getId().setOrdineClienteId(updatedOrdineCliente.getId());
            ordineClienteArticoloService.create(oca);
        });
        LOGGER.info("Updated 'ordineCliente' '{}'", updatedOrdineCliente);
        return updatedOrdineCliente;
    }

    @Transactional
    public OrdineCliente patch(Map<String,Object> patchOrdineCliente){
        LOGGER.info("Patching 'ordineCliente'");

        Long id = Long.valueOf((Integer) patchOrdineCliente.get("id"));
        OrdineCliente ordineCliente = ordineClienteRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        patchOrdineCliente.forEach((key, value) -> {
            switch (key) {
                case "id":
                    ordineCliente.setId(Long.valueOf((Integer) value));
                    break;
                case "dataConsegna":
                    ordineCliente.setDataConsegna(Date.valueOf((String) value));
                    break;
                case "idAutista":
                    Autista autista = new Autista();
                    autista.setId(Long.valueOf((Integer) value));
                    ordineCliente.setAutista(autista);
                    break;
                case "idTelefonata":
                    if (value != null) {
                        Telefonata telefonata = new Telefonata();
                        if (value instanceof Long) {
                            telefonata.setId((Long) value);
                        } else {
                            telefonata.setId(Long.valueOf((Integer) value));
                        }
                        ordineCliente.setTelefonata(telefonata);
                    } else {
                        ordineCliente.setTelefonata(null);
                    }

                    break;
            }
        });
        OrdineCliente patchedOrdineCliente = ordineClienteRepository.save(ordineCliente);

        LOGGER.info("Patched 'ordineCliente' '{}'", patchedOrdineCliente);
        return patchedOrdineCliente;
    }

    @Transactional
    public void delete(Long ordineClienteId){
        LOGGER.info("Deleting 'ordineCliente' '{}'", ordineClienteId);
        ddtArticoloOrdineClienteRepository.deleteByOrdineClienteId(ordineClienteId);
        ordineClienteArticoloService.deleteByOrdineClienteId(ordineClienteId);
        ordineClienteRepository.deleteById(ordineClienteId);
        LOGGER.info("Deleted 'ordineCliente' '{}'", ordineClienteId);
    }

    @Transactional
    public List<OrdineClienteAggregate> updateAggregate(List<OrdineClienteAggregate> ordiniClientiAggregati){
        LOGGER.info("Updating {} 'ordini-clienti aggregate'", ordiniClientiAggregati.size());

        if(ordiniClientiAggregati != null && !ordiniClientiAggregati.isEmpty()){

            // update pezzi da evadere
            for(OrdineClienteAggregate ordineClienteAggregate : ordiniClientiAggregati){
                Set<Long> idOrdiniClienti = new HashSet<>();
                if(ordineClienteAggregate.getIdsOrdiniClienti() != null && !StringUtils.isEmpty(ordineClienteAggregate.getIdsOrdiniClienti())){
                    Arrays.stream(ordineClienteAggregate.getIdsOrdiniClienti().split(",")).forEach(id -> idOrdiniClienti.add(Long.valueOf(id)));
                }
                Integer numPezziEvasi = ordineClienteAggregate.getNumeroPezziDaEvadere();
                numPezziEvasi = numPezziEvasi != null ? numPezziEvasi : 0;

                for(Long idOrdineCliente : idOrdiniClienti){

                    // create 'OrdineClienteArticoloKey'
                    OrdineClienteArticoloKey ordineClienteArticoloKey = new OrdineClienteArticoloKey();
                    ordineClienteArticoloKey.setOrdineClienteId(idOrdineCliente);
                    ordineClienteArticoloKey.setArticoloId(ordineClienteAggregate.getIdArticolo());

                    // retrieve the 'OrdineClienteArticolo'
                    OrdineClienteArticolo ordineClienteArticolo = null;
                    try{
                        ordineClienteArticolo = getOrdineClienteArticolo(ordineClienteArticoloKey);
                        LOGGER.info("Retrieved 'ordineClienteArticolo' {}", ordineClienteArticolo);
                    } catch(Exception e){
                        LOGGER.error("Unable to retrieve 'OrdineClienteArticolo' from key '{}'", ordineClienteArticoloKey);
                        throw e;
                    }

                    Integer numPezziOrdinati = ordineClienteArticolo.getNumeroPezziOrdinati();

                    LOGGER.info("Numero pezzi ordinati: {}, numero pezzi evasi {}", numPezziOrdinati, numPezziEvasi);

                    int newNumPezziDaEvadere = numPezziOrdinati - numPezziEvasi;
                    if(newNumPezziDaEvadere > 0){
                        //numPezziEvasi = (numPezziOrdinati - numPezziEvasi);
                        numPezziEvasi = 0;
                    } else if(newNumPezziDaEvadere < 0){
                        numPezziEvasi = Math.abs(numPezziOrdinati - numPezziEvasi);
                        newNumPezziDaEvadere = 0;
                    } else {
                        numPezziEvasi = 0;
                    }
                    ordineClienteArticolo.setNumeroPezziDaEvadere(newNumPezziDaEvadere);
                    ordineClienteArticolo.setIdDdts(ordineClienteAggregate.getIdsDdts());

                    LOGGER.info("Updating ordine cliente {}, articolo {} setting 'numPezziDaEvadere'={}", idOrdineCliente, ordineClienteAggregate.getIdArticolo(), newNumPezziDaEvadere);
                    saveOrdineClienteArticolo(ordineClienteArticolo);
                }
            }

            // computeStatoOrdineCliente
            Set<Long> idOrdiniClienti = new HashSet<>();
            ordiniClientiAggregati.forEach(oca -> {
                String idsOrdiniClienti = oca.getIdsOrdiniClienti();
                if(idsOrdiniClienti != null && !StringUtils.isEmpty(idsOrdiniClienti)){
                    Arrays.stream(idsOrdiniClienti.split(",")).forEach(id -> idOrdiniClienti.add(Long.valueOf(id)));
                }
            });
            if(idOrdiniClienti != null && !idOrdiniClienti.isEmpty()){
                for(Long idOrdineCliente : idOrdiniClienti){
                    computeStatoOrdineCliente(idOrdineCliente);
                }
            }
        }
        LOGGER.info("Updated {} 'ordini-clienti aggregate'", ordiniClientiAggregati.size());
        return ordiniClientiAggregati;
    }

    private void checkExistsByAnnoContabileAndProgressivoAndIdNot(Integer annoContabile, Integer progressivo, Long idOrdineCliente){
        Optional<OrdineCliente> ordineCliente = ordineClienteRepository.findByAnnoContabileAndProgressivoAndIdNot(annoContabile, progressivo, idOrdineCliente);
        if(ordineCliente.isPresent()){
            throw new ResourceAlreadyExistingException(Resource.ORDINE_CLIENTE, annoContabile, progressivo);
        }
    }

    public OrdineClienteArticolo getOrdineClienteArticolo(OrdineClienteArticoloKey ordineClienteArticoloKey){
        return ordineClienteArticoloService.getOrdineClienteArticolo(ordineClienteArticoloKey);
    }

    public void saveOrdineClienteArticolo(OrdineClienteArticolo ordineClienteArticolo){
        ordineClienteArticoloService.save(ordineClienteArticolo);
    }

    public void computeStatoOrdineCliente(Long idOrdineCliente){
        LOGGER.info("Computing stato of 'OrdineCliente' {}", idOrdineCliente);
        OrdineCliente ordineCliente = getOne(idOrdineCliente);
        Set<OrdineClienteArticolo> ordineClienteArticoli = ordineClienteArticoloService.getOrdineClienteArticoli(idOrdineCliente);
        if(ordineClienteArticoli != null && !ordineClienteArticoli.isEmpty()){
            StatoOrdine statoOrdine = statoOrdineService.getDaEvadere();
            Integer sumNumeroPezziOrdinati = ordineClienteArticoli.stream().map(OrdineClienteArticolo::getNumeroPezziOrdinati).reduce(0, Integer::sum);
            if(sumNumeroPezziOrdinati > 0){
                Integer sumNumeroPezziDaEvadere = ordineClienteArticoli.stream().map(OrdineClienteArticolo::getNumeroPezziDaEvadere).reduce(0, Integer::sum);
                if(sumNumeroPezziDaEvadere == 0){
                    statoOrdine = statoOrdineService.getEvaso();
                } else if(sumNumeroPezziDaEvadere < sumNumeroPezziOrdinati){
                    statoOrdine = statoOrdineService.getParzialmenteEvaso();
                }
            }
            LOGGER.info("Setting stato {} to 'OrdineCliente' {}", statoOrdine.getCodice(), idOrdineCliente);
            ordineCliente.setStatoOrdine(statoOrdine);
            ordineClienteRepository.save(ordineCliente);
            LOGGER.info("Set stato {} to 'OrdineCliente' {}", statoOrdine.getCodice(), idOrdineCliente);
        }

        LOGGER.info("Computed stato of 'OrdineCliente' {}", idOrdineCliente);
    }

    public List<VOrdineFornitoreArticolo> getArticoliForOrdineFornitore(Long idFornitore, Date dataFrom, Date dataTo){
        List<VOrdineFornitoreArticolo> vOrdineFornitoriArticoli = new ArrayList<>();

        List<VOrdineClienteArticolo> vOrdineClienteArticoli = vOrdineClienteArticoloRepository.findByIdFornitoreAndDataConsegnaBetween(idFornitore, dataFrom, dataTo);
        if(!vOrdineClienteArticoli.isEmpty()){
            Map<String, ArticoloGrouped> articoliGrouped = new HashMap<>();
            for(VOrdineClienteArticolo vOrdineClienteArticolo : vOrdineClienteArticoli){
                String key = vOrdineClienteArticolo.getIdArticolo() + ";"
                        + vOrdineClienteArticolo.getCodiceArticolo() + ";"
                        + vOrdineClienteArticolo.getDescrizioneArticolo();

                ArticoloGrouped articoloGrouped = articoliGrouped.getOrDefault(key, null);
                if(articoloGrouped != null){
                    articoloGrouped.setIdsOrdiniClienti(articoloGrouped.getIdsOrdiniClienti() + ";" + vOrdineClienteArticolo.getIdOrdineCliente().toString());
                    articoloGrouped.setNumeroPezzi(articoloGrouped.getNumeroPezzi() + vOrdineClienteArticolo.getNumeroPezziDaEvadere());
                } else {
                    articoloGrouped = new ArticoloGrouped();
                    articoloGrouped.setIdsOrdiniClienti(vOrdineClienteArticolo.getIdOrdineCliente().toString());
                    articoloGrouped.setNumeroPezzi(vOrdineClienteArticolo.getNumeroPezziDaEvadere());
                }
                articoliGrouped.put(key, articoloGrouped);
            }

            if(!articoliGrouped.isEmpty()){
                for(String key : articoliGrouped.keySet()){
                    VOrdineFornitoreArticolo vOrdineFornitoreArticolo = new VOrdineFornitoreArticolo();
                    String[] keyTokens = key.split(";");
                    vOrdineFornitoreArticolo.setIdArticolo(Long.valueOf(keyTokens[0]));
                    vOrdineFornitoreArticolo.setCodiceArticolo(keyTokens[1]);
                    vOrdineFornitoreArticolo.setDescrizioneArticolo(keyTokens[2]);
                    vOrdineFornitoreArticolo.setId(UUID.randomUUID().toString());

                    ArticoloGrouped articoloGrouped = articoliGrouped.get(key);
                    vOrdineFornitoreArticolo.setIdOrdiniClienti(articoloGrouped.getIdsOrdiniClienti());
                    vOrdineFornitoreArticolo.setNumeroPezziOrdinati(articoloGrouped.getNumeroPezzi());

                    vOrdineFornitoriArticoli.add(vOrdineFornitoreArticolo);
                }
            }
        }

        return vOrdineFornitoriArticoli;
    }

    public Set<VOrdineClienteArticoloDaEvadere> getOrdiniArticoliDaEvadereByIdCliente(Integer idCliente){
        return vOrdineClienteArticoloDaEvadereRepository.findAllByIdCliente(idCliente);
    }

    /*
    private Integer computeProgressivo(Integer annoContabile){
        List<OrdineCliente> ordiniClienti = ordineClienteRepository.findByAnnoContabileOrderByProgressivoDesc(annoContabile);
        if(ordiniClienti != null && !ordiniClienti.isEmpty()){
            Optional<OrdineCliente> lastOrdineCliente = ordiniClienti.stream().findFirst();
            if(lastOrdineCliente.isPresent()){
                return lastOrdineCliente.get().getProgressivo() + 1;
            }
        }
        return 1;
    }*/

    private static class ArticoloGrouped {
        private String idsOrdiniClienti;
        private Integer numeroPezzi;

        public String getIdsOrdiniClienti() {
            return idsOrdiniClienti;
        }

        public void setIdsOrdiniClienti(String idsOrdiniClienti) {
            this.idsOrdiniClienti = idsOrdiniClienti;
        }

        public Integer getNumeroPezzi() {
            return numeroPezzi;
        }

        public void setNumeroPezzi(Integer numeroPezzi) {
            this.numeroPezzi = numeroPezzi;
        }
    }

}
