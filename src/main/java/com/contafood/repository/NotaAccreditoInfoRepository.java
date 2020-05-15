package com.contafood.repository;

import com.contafood.model.NotaAccreditoInfo;
import com.contafood.model.NotaAccreditoInfoKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface NotaAccreditoInfoRepository extends CrudRepository<NotaAccreditoInfo, Long> {

    @Override
    Set<NotaAccreditoInfo> findAll();

    Optional<NotaAccreditoInfo> findById(NotaAccreditoInfoKey id);

    void deleteByNotaAccreditoId(Long notaAccreditoId);

}
