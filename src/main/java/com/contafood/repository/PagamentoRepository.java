package com.contafood.repository;

import com.contafood.model.Pagamento;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {

    Set<Pagamento> findAllByOrderByDataDesc();

    Set<Pagamento> findByDdtIdOrderByDataDesc(Long ddtId);

    Set<Pagamento> findByDdtAcquistoIdOrderByDataDesc(Long ddtAcquistoId);

    Set<Pagamento> findByNotaAccreditoIdOrderByDataDesc(Long notaAccreditoId);

    Set<Pagamento> findByNotaResoIdOrderByDataDesc(Long notaResoId);

    Set<Pagamento> findByRicevutaPrivatoIdOrderByDataDesc(Long ricevutaPrivatoId);

    Set<Pagamento> findByFatturaIdOrderByDataDesc(Long fatturaId);

    Set<Pagamento> findByFatturaAccompagnatoriaIdOrderByDataDesc(Long fatturaAccompagnatoriaId);

    Set<Pagamento> findByFatturaAcquistoIdOrderByDataDesc(Long fatturaAcquistoId);

    void deleteByDdtId(Long ddtId);

    void deleteByNotaAccreditoId(Long notaAccreditoId);

    void deleteByNotaResoId(Long notaResoId);

    void deleteByFatturaId(Long fatturaId);

    void deleteByFatturaAccompagnatoriaId(Long fatturaAccompagnatoriaId);

    void deleteByRicevutaPrivatoId(Long ricevutaPrivatoId);

}
