package com.contafood.repository;

import com.contafood.model.Articolo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface ArticoloRepository extends CrudRepository<Articolo, Long> {

    @Override
    Set<Articolo> findAll();

    Set<Articolo> findAllByOrderByCodiceAsc();

    Set<Articolo> findByAttivo(Boolean attivo);

    Set<Articolo> findByAttivoOrderByCodiceAsc(Boolean attivo);

    List<Articolo> findByAttivoAndFornitoreId(Boolean attivo, Long idFornitore);
}
