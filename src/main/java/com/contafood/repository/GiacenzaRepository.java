package com.contafood.repository;

import com.contafood.model.Giacenza;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface GiacenzaRepository extends CrudRepository<Giacenza, Long> {

    @Override
    Set<Giacenza> findAll();

    Set<Giacenza> findByCodiceArticoloRicettaAndLotto(String codiceArticoloRicetta, String lotto);

    void deleteByArticoloId(Long idArticolo);

    void deleteByRicettaId(Long idRicetta);

    void deleteByIdIn(List<Long> ids);
}
