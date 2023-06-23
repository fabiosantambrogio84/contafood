package com.contafood.repository;

import com.contafood.model.FatturaAccompagnatoriaAcquistoArticolo;
import com.contafood.model.FatturaAccompagnatoriaAcquistoArticoloKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface FatturaAccompagnatoriaAcquistoArticoloRepository extends CrudRepository<FatturaAccompagnatoriaAcquistoArticolo, Long> {

    @Override
    Set<FatturaAccompagnatoriaAcquistoArticolo> findAll();

    Optional<FatturaAccompagnatoriaAcquistoArticolo> findById(FatturaAccompagnatoriaAcquistoArticoloKey id);

    Set<FatturaAccompagnatoriaAcquistoArticolo> findByFatturaAccompagnatoriaAcquistoId(Long idFatturaAccompagnatoriaAcquisto);

    Set<FatturaAccompagnatoriaAcquistoArticolo> findByArticoloId(Long articoloId);

    void deleteByFatturaAccompagnatoriaAcquistoId(Long fatturaAccompagnatoriaAcquistoId);

    void deleteByArticoloId(Long articoloId);
}
