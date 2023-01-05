package com.contafood.repository.views;

import com.contafood.model.views.VDocumentoAcquistoIngrediente;
import com.contafood.model.views.VDocumentoVenditaIngrediente;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface VDocumentoVenditaIngredienteRepository extends CrudRepository<VDocumentoVenditaIngrediente, Long> {

    @Override
    Set<VDocumentoVenditaIngrediente> findAll();

    Set<VDocumentoVenditaIngrediente> findAllByLottoIngrediente(String lotto);

}
