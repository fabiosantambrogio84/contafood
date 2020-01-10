package com.contafood.repository;

import com.contafood.model.OrdineCliente;
import com.contafood.model.Ricetta;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface OrdineClienteRepository extends CrudRepository<OrdineCliente, Long> {

    @Override
    Set<OrdineCliente> findAll();

    Set<OrdineCliente> findAllByOrderByAnnoContabileDescProgressivoDesc();

    Set<OrdineCliente> findByAutistaIdAndDataConsegna(Long idAutista, Date dataConsegna);

    List<OrdineCliente> findByAnnoContabileOrderByProgressivoDesc(Integer annoContabile);
}
