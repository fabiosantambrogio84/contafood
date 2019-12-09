package com.contafood.repository;

import com.contafood.model.ListinoPrezzo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface ListinoPrezzoRepository extends CrudRepository<ListinoPrezzo, Long> {

    @Override
    List<ListinoPrezzo> findAll();

    List<ListinoPrezzo> findByListinoId(Long idListino);

    List<ListinoPrezzo> findByArticoloId(Long idArticolo);

    List<ListinoPrezzo> findByListinoIdAndArticoloCategoriaId(Long idListino, Long idCategoriaArticolo);

    List<ListinoPrezzo> findByListinoIdAndArticoloFornitoreId(Long idListino, Long idFornitore);

    List<ListinoPrezzo> findByListinoIdAndArticoloCategoriaIdAndArticoloFornitoreId(Long idListino, Long idCategoriaArticolo, Long idFornitore);

    void deleteByListinoId(Long idListino);

    void deleteByArticoloId(Long idArticolo);

}
