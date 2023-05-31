package com.contafood.repository;

import com.contafood.model.FatturaAccompagnatoriaAcquistoTotale;
import com.contafood.model.FatturaAccompagnatoriaAcquistoTotaleKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface FatturaAccompagnatoriaAcquistoTotaleRepository extends CrudRepository<FatturaAccompagnatoriaAcquistoTotale, Long> {

    @Override
    Set<FatturaAccompagnatoriaAcquistoTotale> findAll();

    Optional<FatturaAccompagnatoriaAcquistoTotale> findById(FatturaAccompagnatoriaAcquistoTotaleKey id);

    Set<FatturaAccompagnatoriaAcquistoTotale> findByFatturaAccompagnatoriaAcquistoId(Long fatturaAccompagnatoriaAcquistoId);

    void deleteByFatturaAccompagnatoriaAcquistoId(Long fatturaAccompagnatoriaAcquistoId);

}
