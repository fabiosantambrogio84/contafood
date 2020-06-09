package com.contafood.repository;

import com.contafood.model.FatturaAccompagnatoriaArticolo;
import com.contafood.model.FatturaAccompagnatoriaArticoloKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface FatturaAccompagnatoriaArticoloRepository extends CrudRepository<FatturaAccompagnatoriaArticolo, Long> {

    @Override
    Set<FatturaAccompagnatoriaArticolo> findAll();

    Optional<FatturaAccompagnatoriaArticolo> findById(FatturaAccompagnatoriaArticoloKey id);

    Set<FatturaAccompagnatoriaArticolo> findByFatturaAccompagnatoriaId(Long idFatturaAccompagnatoria);

    Set<FatturaAccompagnatoriaArticolo> findByArticoloId(Long articoloId);

    Set<FatturaAccompagnatoriaArticolo> findByArticoloIdAndLotto(Long articoloId, String lotto);

    void deleteByFatturaAccompagnatoriaId(Long fatturaAccompagnatoriaId);

    void deleteByArticoloId(Long articoloId);
}
