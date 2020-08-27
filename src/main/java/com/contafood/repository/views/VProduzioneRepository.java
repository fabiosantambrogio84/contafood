package com.contafood.repository.views;

import com.contafood.model.views.VProduzione;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface VProduzioneRepository extends CrudRepository<VProduzione, Long> {

    @Override
    Set<VProduzione> findAll();

    Set<VProduzione> findAllByOrderByCodiceProduzioneDesc();

    Set<VProduzione> findAllByLotto(String lotto);

}
