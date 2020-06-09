package com.contafood.repository;

import com.contafood.model.Giacenza;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface GiacenzaRepository extends CrudRepository<Giacenza, Long> {

    @Override
    Set<Giacenza> findAll();

    Set<Giacenza> findByArticoloId(Long idArticolo);

    Set<Giacenza> findByArticoloIdAndLotto(Long idArticolo, String lotto);

    void deleteByArticoloId(Long idArticolo);

    void deleteByIdIn(List<Long> ids);
}
