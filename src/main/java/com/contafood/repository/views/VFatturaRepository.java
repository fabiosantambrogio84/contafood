package com.contafood.repository.views;

import com.contafood.model.views.VFattura;
import com.contafood.repository.custom.VFatturaCustomRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface VFatturaRepository extends CrudRepository<VFattura, Long>, VFatturaCustomRepository {

    @Override
    Set<VFattura> findAll();

    Set<VFattura> findAllByOrderByAnnoDescProgressivoDesc();

}
