package com.contafood.repository;

import com.contafood.model.Pagamento;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {

    Set<Pagamento> findAllByOrderByDataDesc();

    List<Pagamento> findByDdtIdOrderByDataDesc(Long ddtId);

    void deleteByDdtId(Long ddtId);

}
