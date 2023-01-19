package com.contafood.repository.views;

import com.contafood.model.views.VProduzione;
import com.contafood.repository.custom.VProduzioneCustomRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface VProduzioneRepository extends CrudRepository<VProduzione, Long>, VProduzioneCustomRepository {

    @Override
    Set<VProduzione> findAll();

    Set<VProduzione> findAllByLotto(String lotto);

}
