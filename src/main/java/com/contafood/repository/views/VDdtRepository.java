package com.contafood.repository.views;

import com.contafood.model.views.VDdt;
import com.contafood.repository.custom.VDdtCustomRepository;
import com.contafood.repository.custom.VDdtCustomRepositoryImpl;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface VDdtRepository extends CrudRepository<VDdt, Long>, VDdtCustomRepository {

    @Override
    Set<VDdt> findAll();

}
