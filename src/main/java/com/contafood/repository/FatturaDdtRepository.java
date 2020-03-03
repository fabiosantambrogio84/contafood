package com.contafood.repository;

import com.contafood.model.FatturaDdt;
import com.contafood.model.FatturaDdtKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface FatturaDdtRepository extends CrudRepository<FatturaDdt, Long> {

    @Override
    Set<FatturaDdt> findAll();

    Optional<FatturaDdt> findById(FatturaDdtKey id);

    Set<FatturaDdt> findByDdtId(Long ddtId);

    Set<FatturaDdt> findByFatturaId(Long fatturaId);

    void deleteByDdtId(Long ddtId);

    void deleteByFatturaId(Long fatturaId);
}
