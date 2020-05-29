package com.contafood.repository.views;

import com.contafood.model.views.VFattura;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface VFatturaRepository extends CrudRepository<VFattura, Long> {

    @Override
    Set<VFattura> findAll();

    Set<VFattura> findAllByOrderByAnnoDescProgressivoDesc();

    List<VFattura> findByAnnoOrderByProgressivoDesc(Integer anno);

}
