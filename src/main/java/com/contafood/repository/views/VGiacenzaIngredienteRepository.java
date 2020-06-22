package com.contafood.repository.views;

import com.contafood.model.views.VGiacenzaIngrediente;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface VGiacenzaIngredienteRepository extends CrudRepository<VGiacenzaIngrediente, Long> {

    @Override
    Set<VGiacenzaIngrediente> findAll();

    Set<VGiacenzaIngrediente> findByIdIngrediente(Long idIngrediente);

}
