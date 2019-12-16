package com.contafood.repository;

import com.contafood.model.ListinoPrezzo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListinoPrezzoRepository extends CrudRepository<ListinoPrezzo, Long> {

    @Override
    List<ListinoPrezzo> findAll();

    List<ListinoPrezzo> findByListinoId(Long idListino);

    List<ListinoPrezzo> findByArticoloId(Long idArticolo);

    List<ListinoPrezzo> findByListinoIdAndArticoloIdIn(Long idListino, List<Long> idArticoli);

    List<ListinoPrezzo> findByListinoIdAndArticoloFornitoreId(Long idListino, Long idFornitore);

    List<ListinoPrezzo> findByListinoIdAndArticoloIdInAndArticoloFornitoreId(Long idListino, List<Long> idArticoli, Long idFornitore);

    void deleteByListinoId(Long idListino);

    void deleteByArticoloId(Long idArticolo);

}
