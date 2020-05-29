package com.contafood.repository;

import com.contafood.model.DdtArticoloOrdineCliente;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface DdtArticoloOrdineClienteRepository extends CrudRepository<DdtArticoloOrdineCliente, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM contafood.ddt_articolo_ordine_cliente WHERE id_ddt = ?1"
    )
    Set<DdtArticoloOrdineCliente> findByDdtArticoloDdtId(Long idDdt);

    //Set<DdtArticoloOrdineCliente> findByOrdineClienteId(Long idOrdineCliente);

    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM contafood.ddt_articolo_ordine_cliente WHERE id_ddt = ?1"
            )
    void deleteByDdtArticoloDdtId(Long idDdt);

    void deleteByOrdineClienteId(Long idOrdineCliente);
}
