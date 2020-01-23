package com.contafood.repository;

import com.contafood.model.Produzione;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProduzioneRepository extends CrudRepository<Produzione, Long> {

    @Override
    Set<Produzione> findAll();

    @Query(value = "SELECT MAX(lotto_numero_progressivo) + 1 FROM produzione WHERE lotto_anno = ?1 AND lotto_giorno = ?2", nativeQuery = true)
    Optional<Integer> findNextNumeroProgressivoByLottoAnnoAndLottoGiorno(Integer lottoAnno, Integer lottoGiorno);

    List<Produzione> findByRicettaId(Long idRicetta);
}
