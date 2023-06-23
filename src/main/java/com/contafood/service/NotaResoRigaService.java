package com.contafood.service;

import com.contafood.model.AliquotaIva;
import com.contafood.model.NotaResoRiga;
import com.contafood.repository.NotaResoRigaRepository;
import com.contafood.util.AccountingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class NotaResoRigaService {

    private static Logger LOGGER = LoggerFactory.getLogger(NotaResoRigaService.class);

    private final NotaResoRigaRepository notaResoRigaRepository;

    private final AliquotaIvaService aliquotaIvaService;

    @Autowired
    public NotaResoRigaService(final NotaResoRigaRepository notaResoRigaRepository, final AliquotaIvaService aliquotaIvaService){
        this.notaResoRigaRepository = notaResoRigaRepository;
        this.aliquotaIvaService = aliquotaIvaService;
    }

    public Set<NotaResoRiga> findAll(){
        LOGGER.info("Retrieving the list of 'nota reso riga'");
        Set<NotaResoRiga> notaResoRiga = notaResoRigaRepository.findAll();
        LOGGER.info("Retrieved {} 'nota accredito riga'", notaResoRiga.size());
        return notaResoRiga;
    }

    public NotaResoRiga create(NotaResoRiga notaResoRiga){
        LOGGER.info("Creating 'nota reso riga'");
        notaResoRiga.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        notaResoRiga.setImponibile(computeImponibile(notaResoRiga));
        notaResoRiga.setTotale(computeTotale(notaResoRiga));

        NotaResoRiga createdNotaResoRiga = notaResoRigaRepository.save(notaResoRiga);
        LOGGER.info("Created 'nota reso riga' '{}'", createdNotaResoRiga);
        return createdNotaResoRiga;
    }

    public void deleteByNotaResoId(Long notaResoId){
        LOGGER.info("Deleting 'nota reso riga' by 'nota reso' '{}'", notaResoId);
        notaResoRigaRepository.deleteByNotaResoId(notaResoId);
        LOGGER.info("Deleted 'nota reso riga' by 'nota reso' '{}'", notaResoId);
    }

    public AliquotaIva getAliquotaIva(NotaResoRiga notaResoRiga){
        return aliquotaIvaService.getOne(notaResoRiga.getAliquotaIva().getId());
    }

    private BigDecimal computeImponibile(NotaResoRiga notaResoRiga){
        return AccountingUtils.computeImponibile(notaResoRiga.getQuantita(), notaResoRiga.getPrezzo(), notaResoRiga.getSconto());
    }

    private BigDecimal computeTotale(NotaResoRiga notaResoRiga){
        AliquotaIva aliquotaIva = getAliquotaIva(notaResoRiga);
        return AccountingUtils.computeTotale(notaResoRiga.getQuantita(), notaResoRiga.getPrezzo(), notaResoRiga.getSconto(), aliquotaIva);
    }

}
