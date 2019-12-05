package com.contafood.repository;

import com.contafood.model.ListinoAssociato;
import com.contafood.model.ListinoPrezzo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface ListinoPrezzoRepository extends CrudRepository<ListinoPrezzo, Long> {

    @Override
    Set<ListinoPrezzo> findAll();

    List<ListinoPrezzo> findByListinoId(Long idListino);

    List<ListinoPrezzo> findByArticoloId(Long idArticolo);

    List<ListinoPrezzo> findByArticoloCategoriaId(Long idCategoriaArticolo);

    List<ListinoPrezzo> findByArticoloFornitoreId(Long idFornitore);

    List<ListinoPrezzo> findByArticoloCategoriaIdAndArticoloFornitoreId(Long idCategoriaArticolo, Long idFornitore);

    void deleteByListinoId(Long idListino);

    void deleteByArticoloId(Long idArticolo);

}
