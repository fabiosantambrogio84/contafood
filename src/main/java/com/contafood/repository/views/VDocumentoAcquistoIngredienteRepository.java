package com.contafood.repository.views;

import com.contafood.model.views.VDocumentoAcquistoIngrediente;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface VDocumentoAcquistoIngredienteRepository extends CrudRepository<VDocumentoAcquistoIngrediente, Long> {

    @Override
    Set<VDocumentoAcquistoIngrediente> findAll();

    Set<VDocumentoAcquistoIngrediente> findAllByLottoIngrediente(String lotto);

}
