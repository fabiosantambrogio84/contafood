package com.contafood.model.reports;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ListinoPrezzoDataSource {

    private String descrizioneArticolo;

    private String fornitore;

    private String categoriaArticolo;

    private BigDecimal prezzo;

}
