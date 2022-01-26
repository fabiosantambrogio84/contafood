package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Fornitore;
import com.contafood.repository.FornitoreRepository;
import com.contafood.util.BarcodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
public class FornitoreService {

    private final static Logger LOGGER = LoggerFactory.getLogger(FornitoreService.class);

    private final FornitoreRepository fornitoreRepository;
    private final ListinoPrezzoVariazioneService listinoPrezzoVariazioneService;
    private final IngredienteService ingredienteService;

    @Autowired
    public FornitoreService(final FornitoreRepository fornitoreRepository,
                            final ListinoPrezzoVariazioneService listinoPrezzoVariazioneService,
                            final IngredienteService ingredienteService){
        this.fornitoreRepository = fornitoreRepository;
        this.listinoPrezzoVariazioneService = listinoPrezzoVariazioneService;
        this.ingredienteService = ingredienteService;
    }

    public Set<Fornitore> getAll(){
        LOGGER.info("Retrieving the list of 'fornitori'");
        Set<Fornitore> fornitori = fornitoreRepository.findAllByOrderByRagioneSocialeAsc();
        LOGGER.info("Retrieved {} 'fornitori'", fornitori.size());
        return fornitori;
    }

    public Fornitore getOne(Long fornitoreId){
        LOGGER.info("Retrieving 'fornitore' '{}'", fornitoreId);
        Fornitore fornitore = fornitoreRepository.findById(fornitoreId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'fornitore' '{}'", fornitore);
        return fornitore;
    }

    public Fornitore getByRagioneSociale(String ragioneSociale){
        LOGGER.info("Retrieving 'fornitore' with ragioneSociale '{}'", ragioneSociale);
        Fornitore fornitore = fornitoreRepository.findByRagioneSociale(ragioneSociale);
        LOGGER.info("Retrieved 'fornitore' '{}'", fornitore);
        return fornitore;
    }

    public Fornitore create(Fornitore fornitore){
        LOGGER.info("Creating 'fornitore'");
        handleBarcodeMask(fornitore);
        Fornitore createdFornitore = fornitoreRepository.save(fornitore);
        createdFornitore.setCodice(createdFornitore.getId().intValue());
        fornitoreRepository.save(createdFornitore);
        LOGGER.info("Created 'fornitore' '{}'", createdFornitore);
        return createdFornitore;
    }

    public Fornitore update(Fornitore fornitore){
        LOGGER.info("Updating 'fornitore'");
        handleBarcodeMask(fornitore);
        Fornitore updatedFornitore = fornitoreRepository.save(fornitore);
        LOGGER.info("Updated 'fornitore' '{}'", updatedFornitore);
        return updatedFornitore;
    }

    @Transactional
    public void delete(Long fornitoreId){
        LOGGER.info("Disabling 'fornitore' '{}'", fornitoreId);
        Fornitore fornitore = fornitoreRepository.findById(fornitoreId).orElseThrow(ResourceNotFoundException::new);
        fornitore.setAttivo(false);
        fornitoreRepository.save(fornitore);
        LOGGER.info("Disabled 'fornitore' '{}'", fornitoreId);
    }

    private void handleBarcodeMask(Fornitore fornitore){
        if(!StringUtils.isEmpty(fornitore.getBarcodeMaskLottoScadenza())){
            fornitore.setBarcodeRegexpLotto(BarcodeUtils.createRegexpLotto(fornitore.getBarcodeMaskLottoScadenza()));
            fornitore.setBarcodeRegexpDataScadenza(BarcodeUtils.createRegexpDataScadenza(fornitore.getBarcodeMaskLottoScadenza()));
        }
    }
}
