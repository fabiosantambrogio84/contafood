package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Banca;
import com.contafood.repository.BancaRepository;
import com.contafood.util.enumeration.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BancaService {

    private static Logger LOGGER = LoggerFactory.getLogger(BancaService.class);

    private final BancaRepository bancaRepository;

    @Autowired
    public BancaService(final BancaRepository bancaRepository){
        this.bancaRepository = bancaRepository;
    }

    public List<Banca> getAll(){
        LOGGER.info("Retrieving the list of 'banche'");
        List<Banca> banche = bancaRepository.findAllByOrderByNomeAscAbiAscCabAsc();
        //banche.sort(Comparator.comparing(Banca::getAbi));
        //banche.sort(Comparator.comparing(Banca::getCab));
        LOGGER.info("Retrieved {} 'banche'", banche.size());
        return banche;
    }

    public Banca getOne(Long bancaId){
        LOGGER.info("Retrieving 'banca' '{}'", bancaId);
        Banca banca = bancaRepository.findById(bancaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'banca' '{}'", banca);
        return banca;
    }

    public Banca create(Banca banca){
        LOGGER.info("Creating 'banca'");
        banca.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Optional<Banca> bancaByAbiAndCab = bancaRepository.findByAbiEqualsAndCabEquals(banca.getAbi(), banca.getCab());
        if(bancaByAbiAndCab.isPresent()){
            throw new ResourceAlreadyExistingException(Resource.BANCA, banca.getAbi(), banca.getCab());
        }
        Banca createdBanca = bancaRepository.save(banca);
        LOGGER.info("Created 'banca' '{}'", createdBanca);
        return createdBanca;
    }

    public Banca update(Banca banca){
        LOGGER.info("Updating 'banca'");
        Banca currentBanca = bancaRepository.findById(banca.getId()).orElseThrow(ResourceNotFoundException::new);
        banca.setDataInserimento(currentBanca.getDataInserimento());
        Optional<Banca> bancaByAbiAndCab = bancaRepository.findByAbiEqualsAndCabEquals(banca.getAbi(), banca.getCab());
        if(bancaByAbiAndCab.isPresent() && !bancaByAbiAndCab.get().getId().equals(banca.getId())){
            throw new ResourceAlreadyExistingException(Resource.BANCA, banca.getAbi(), banca.getCab());
        }
        Banca updatedBanca = bancaRepository.save(banca);
        LOGGER.info("Updated 'banca' '{}'", updatedBanca);
        return updatedBanca;
    }

    public void delete(Long bancaId){
        LOGGER.info("Deleting 'banca' '{}'", bancaId);
        bancaRepository.deleteById(bancaId);
        LOGGER.info("Deleted 'banca' '{}'", bancaId);
    }
}
