package com.contafood.repository;

import com.contafood.model.Produzione;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ProduzioneRepository extends CrudRepository<Produzione, Long> {

    @Override
    Set<Produzione> findAll();
}
