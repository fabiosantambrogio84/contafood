package com.contafood.repository;

import com.contafood.model.DdtAcquisto;
import com.contafood.model.beans.DdtAcquistoRicercaLotto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface DdtAcquistoRepository extends CrudRepository<DdtAcquisto, Long> {

    @Override
    Set<DdtAcquisto> findAll();

    Set<DdtAcquisto> findAllByOrderByNumeroDesc();

    @Query(nativeQuery = true,
            value = "select da.id, da.data, da.numero, da.quantita, fornitore.ragione_sociale fornitore\n" +
                    "from(select ddt_acquisto.id,ddt_acquisto.data,ddt_acquisto.numero,ddt_acquisto.id_fornitore ,sum(ddt_acquisto_articolo.quantita) quantita\n" +
                    "from ddt_acquisto join ddt_acquisto_articolo on ddt_acquisto.id = ddt_acquisto_articolo.id_ddt_acquisto \n" +
                    "where ddt_acquisto_articolo.lotto = ?1 \n" +
                    "group by ddt_acquisto.id,ddt_acquisto.data,ddt_acquisto.numero,ddt_acquisto.id_fornitore) da\n" +
                    "join fornitore on da.id_fornitore=fornitore.id\n" +
                    "order by da.data desc, da.numero desc"
    )
    Set<DdtAcquistoRicercaLotto> findAllByLotto(String lotto);

}
