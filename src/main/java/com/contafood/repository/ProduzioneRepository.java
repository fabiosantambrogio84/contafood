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

    @Query(nativeQuery = true,
            value = "select distinct produzione.* from produzione join produzione_ingrediente on produzione.id = produzione_ingrediente.id_produzione join produzione_confezione on produzione.id = produzione_confezione.id_produzione where produzione_ingrediente.lotto = ?1 or produzione_confezione.lotto = ?1 or produzione.lotto = ?1 or produzione.lotto_film_chiusura  = ?1 order by produzione.lotto_anno desc, produzione.lotto_giorno desc, produzione.lotto_numero_progressivo desc"
    )
    Set<Produzione> findAllByLotto(String lotto);
}
