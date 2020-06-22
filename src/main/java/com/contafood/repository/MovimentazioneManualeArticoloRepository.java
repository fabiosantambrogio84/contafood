package com.contafood.repository;

import com.contafood.model.MovimentazioneManualeArticolo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface MovimentazioneManualeArticoloRepository extends CrudRepository<MovimentazioneManualeArticolo, Long> {

    @Override
    Set<MovimentazioneManualeArticolo> findAll();

    Set<MovimentazioneManualeArticolo> findByArticoloIdAndLotto(Long idArticolo, String lotto);

    Set<MovimentazioneManualeArticolo> findByArticoloId(Long idArticolo);

    void deleteByArticoloId(Long idArticolo);

    void deleteByArticoloIdIn(List<Long> idArticoli);
}
