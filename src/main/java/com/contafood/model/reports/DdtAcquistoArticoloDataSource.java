package com.contafood.model.reports;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DdtAcquistoArticoloDataSource {

    private String codiceArticolo;

    private String descrizioneArticolo;

    private String lotto;

    private String dataScadenza;

    private String udm;

    private Float quantita;

    private BigDecimal prezzo;

    private BigDecimal sconto;

    private BigDecimal imponibile;

    private Integer iva;

}
