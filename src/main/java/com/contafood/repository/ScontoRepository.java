package com.contafood.repository;

import com.contafood.model.Sconto;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface ScontoRepository extends CrudRepository<Sconto, Long> {

    @Override
    List<Sconto> findAll();

    List<Sconto> findByTipologia(String tipologia);

    List<Sconto> findByTipologiaAndClienteId(String tipologia, Long idCliente);
}
