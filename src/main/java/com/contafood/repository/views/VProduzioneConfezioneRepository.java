package com.contafood.repository.views;

import com.contafood.model.views.VProduzioneConfezione;
import com.contafood.model.views.VProduzioneIngrediente;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface VProduzioneConfezioneRepository extends CrudRepository<VProduzioneConfezione, Long> {

    @Override
    Set<VProduzioneConfezione> findAll();

    Set<VProduzioneConfezione> findAllByLottoConfezione(String lotto);

}
