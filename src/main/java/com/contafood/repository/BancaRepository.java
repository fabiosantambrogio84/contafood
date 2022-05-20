package com.contafood.repository;

import com.contafood.model.Banca;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BancaRepository extends CrudRepository<Banca, Long> {

    List<Banca> findAllByOrderByNomeAscAbiAscCabAsc();

    Optional<Banca> findByAbiEqualsAndCabEquals(String abi, String cab);
}
