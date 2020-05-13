package com.contafood.repository;

import com.contafood.model.Articolo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface ArticoloRepository extends CrudRepository<Articolo, Long> {

    @Override
    Set<Articolo> findAll();

    Set<Articolo> findAllByOrderByCodiceAsc();

    Set<Articolo> findByAttivoOrderByCodiceAsc(Boolean attivo);

    Set<Articolo> findByAttivoAndFornitoreId(Boolean attivo, Long idFornitore);

    Set<Articolo> findByAttivoAndBarcodeEqualsAndCompleteBarcodeIsTrue(Boolean attivo, String barcode);

    Set<Articolo> findByAttivoAndBarcodeEqualsAndCompleteBarcodeIsFalse(Boolean attivo, String barcode);
}
