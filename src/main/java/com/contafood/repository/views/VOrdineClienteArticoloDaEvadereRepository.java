package com.contafood.repository.views;

import com.contafood.model.views.VOrdineClienteArticoloDaEvadere;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface VOrdineClienteArticoloDaEvadereRepository extends CrudRepository<VOrdineClienteArticoloDaEvadere, Long> {

    @Override
    Set<VOrdineClienteArticoloDaEvadere> findAll();

    @Query(nativeQuery = true,
            value = "select * from v_ordine_cliente_articolo_da_evadere where id_cliente = ?1 order by anno_contabile desc, progressivo desc, codice_articolo"
    )
    Set<VOrdineClienteArticoloDaEvadere> findAllByIdCliente(Integer idCliente);

}
