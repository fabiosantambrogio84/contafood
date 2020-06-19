package com.contafood.repository;

import com.contafood.model.GiacenzaArticolo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface GiacenzaArticoloRepository extends CrudRepository<GiacenzaArticolo, Long> {

    @Override
    Set<GiacenzaArticolo> findAll();

    Set<GiacenzaArticolo> findByArticoloIdAndLotto(Long idArticolo, String lotto);

    void deleteByArticoloId(Long idArticolo);

    void deleteByIdIn(List<Long> ids);
}
