package com.contafood.repository;

import com.contafood.model.ListinoPrezzoVariazione;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListinoPrezzoVariazioneRepository extends CrudRepository<ListinoPrezzoVariazione, Long> {

    @Override
    List<ListinoPrezzoVariazione> findAll();

    List<ListinoPrezzoVariazione> findByListinoId(Long idListino);

    List<ListinoPrezzoVariazione> findByArticoloId(Long idArticolo);

    List<ListinoPrezzoVariazione> findByListinoIdAndArticoloId(Long idListino, Long idArticolo);

    List<ListinoPrezzoVariazione> findByListinoIdAndArticoloFornitoreId(Long idListino, Long idFornitore);

    List<ListinoPrezzoVariazione> findByListinoIdAndArticoloIdAndArticoloFornitoreId(Long idListino, Long idArticolo, Long idFornitore);

    void deleteByListinoId(Long idListino);

    void deleteByArticoloId(Long idArticolo);

}
