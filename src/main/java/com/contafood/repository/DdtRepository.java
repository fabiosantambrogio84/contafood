package com.contafood.repository;

import com.contafood.model.Ddt;
import com.contafood.model.beans.DdtRicercaLotto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DdtRepository extends CrudRepository<Ddt, Long> {

    @Override
    Set<Ddt> findAll();

    Set<Ddt> findAllByOrderByAnnoContabileDescProgressivoDesc();

    List<Ddt> findByDataGreaterThanEqualOrderByProgressivoDesc(Date data);

    @Query(nativeQuery = true,
            value = "select d.* from ddt d where d.anno_contabile = ?1 and d.progressivo = ?2 and d.id <> ?3 for update"
    )
    Optional<Ddt> findByAnnoContabileAndProgressivoAndIdNot(Integer annoContabile, Integer progressivo, Long idDdt);

    @Query(nativeQuery = true,
            value = "select d.progressivo from ddt d where d.anno_contabile = ?1 order by d.anno_contabile desc limit 1 for update"
    )
    Integer getLastProgressivoByAnnoContabile(Integer annoContabile);

    @Query(nativeQuery = true,
            value = "select d.progressivo from ddt d group by d.progressivo having count(d.id) > 1 order by d.progressivo"
    )
    List<Integer> getProgressiviDuplicates();

    @Query(nativeQuery = true,
            value = "select d.id,d.progressivo,d.data,cliente.ragione_sociale as cliente,d.quantita\n" +
                    "from(select ddt.id, ddt.anno_contabile, ddt.progressivo, ddt.data, ddt.id_cliente, sum(ddt_articolo.quantita) quantita\n" +
                    "from ddt join ddt_articolo on ddt.id = ddt_articolo.id_ddt\n" +
                    "where ddt_articolo.lotto = ?1\n" +
                    "group by ddt.id, ddt.anno_contabile, ddt.progressivo, ddt.data, ddt.id_cliente) d\n" +
                    "join cliente on d.id_cliente = cliente.id\n" +
                    "order by d.anno_contabile desc, d.progressivo desc"
    )
    Set<DdtRicercaLotto> findAllByLotto(String lotto);
}
