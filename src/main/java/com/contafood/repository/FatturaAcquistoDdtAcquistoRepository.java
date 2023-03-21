package com.contafood.repository;

import com.contafood.model.FatturaAcquistoDdtAcquisto;
import com.contafood.model.FatturaAcquistoDdtAcquistoKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface FatturaAcquistoDdtAcquistoRepository extends CrudRepository<FatturaAcquistoDdtAcquisto, Long> {

    @Override
    Set<FatturaAcquistoDdtAcquisto> findAll();

    Optional<FatturaAcquistoDdtAcquisto> findById(FatturaAcquistoDdtAcquistoKey id);

    Set<FatturaAcquistoDdtAcquisto> findByDdtAcquistoId(Long ddtAcquistoId);

    Set<FatturaAcquistoDdtAcquisto> findByFatturaAcquistoId(Long fatturaAcquistoId);

    void deleteByDdtAcquistoId(Long ddtAcquistoId);

    void deleteByFatturaAcquistoId(Long fatturaAcquistoId);
}
