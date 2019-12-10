package com.contafood.repository;

import com.contafood.model.Telefonata;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TelefonataRepository extends CrudRepository<Telefonata, Long> {

    @Override
    List<Telefonata> findAll();

    List<Telefonata> findByPuntoConsegnaId(Long idPuntoConsegna);

    void deleteByClienteId(Long idCliente);
}
