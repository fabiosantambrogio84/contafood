package com.contafood.repository;

import com.contafood.model.ArticoloImmagine;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface ArticoloImmagineRepository extends CrudRepository<ArticoloImmagine, Long> {

    @Override
    Set<ArticoloImmagine> findAll();

    List<ArticoloImmagine> findByArticoloId(Long idArticolo);

}
