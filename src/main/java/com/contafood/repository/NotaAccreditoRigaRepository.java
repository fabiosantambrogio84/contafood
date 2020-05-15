package com.contafood.repository;

import com.contafood.model.NotaAccreditoRiga;
import com.contafood.model.NotaAccreditoRigaKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface NotaAccreditoRigaRepository extends CrudRepository<NotaAccreditoRiga, Long> {

    @Override
    Set<NotaAccreditoRiga> findAll();

    Optional<NotaAccreditoRiga> findById(NotaAccreditoRigaKey id);

    void deleteByNotaAccreditoId(Long notaAccreditoId);

}
