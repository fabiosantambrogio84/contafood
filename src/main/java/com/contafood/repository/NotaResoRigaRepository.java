package com.contafood.repository;

import com.contafood.model.NotaResoRiga;
import com.contafood.model.NotaResoRigaKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface NotaResoRigaRepository extends CrudRepository<NotaResoRiga, Long> {

    @Override
    Set<NotaResoRiga> findAll();

    Optional<NotaResoRiga> findById(NotaResoRigaKey id);

    void deleteByNotaResoId(Long notaResoId);

}
