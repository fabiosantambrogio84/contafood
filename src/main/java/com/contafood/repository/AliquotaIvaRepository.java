package com.contafood.repository;

import com.contafood.model.AliquotaIva;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface AliquotaIvaRepository extends CrudRepository<AliquotaIva, Long> {

    Set<AliquotaIva> findAllByOrderByValore();
}
