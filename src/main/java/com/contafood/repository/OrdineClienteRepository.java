package com.contafood.repository;

import com.contafood.model.OrdineCliente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrdineClienteRepository extends CrudRepository<OrdineCliente, Long> {

    @Override
    Set<OrdineCliente> findAll();

    Set<OrdineCliente> findAllByOrderByAnnoContabileDescProgressivoDesc();

    Set<OrdineCliente> findByStatoOrdineId(Long idStato);

    List<OrdineCliente> findByAnnoContabileOrderByProgressivoDesc(Integer annoContabile);

    Optional<OrdineCliente> findByAnnoContabileAndProgressivoAndIdNot(Integer annoContabile, Integer progressivo, Long idOrdineCliente);

    Set<OrdineCliente> findByClienteIdAndPuntoConsegnaId(Long idCliente, Long idPuntoConsegna);
}
