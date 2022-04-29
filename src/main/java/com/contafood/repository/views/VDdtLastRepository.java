package com.contafood.repository.views;

import com.contafood.model.views.VDdtLast;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VDdtLastRepository extends CrudRepository<VDdtLast, Long> {

    @Query(value = "SELECT * FROM v_ddt_last", nativeQuery = true)
    Optional<VDdtLast> find();

}
