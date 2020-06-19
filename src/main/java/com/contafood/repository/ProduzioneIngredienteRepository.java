package com.contafood.repository;

import com.contafood.model.ProduzioneIngrediente;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ProduzioneIngredienteRepository extends CrudRepository<ProduzioneIngrediente, Long> {

    @Override
    Set<ProduzioneIngrediente> findAll();

    Set<ProduzioneIngrediente> findByProduzioneId(Long produzioneId);

    Set<ProduzioneIngrediente> findByIngredienteIdAndLotto(Long ingredienteId, String lotto);

    void deleteByProduzioneId(Long produzioneId);

    void deleteByIngredienteId(Long ingredienteId);
}
