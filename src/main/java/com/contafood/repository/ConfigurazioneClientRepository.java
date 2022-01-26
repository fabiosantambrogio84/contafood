package com.contafood.repository;

import com.contafood.model.ConfigurazioneClient;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ConfigurazioneClientRepository extends CrudRepository<ConfigurazioneClient, Long> {

    Set<ConfigurazioneClient> findAllByOrderByDescrizione();
}
