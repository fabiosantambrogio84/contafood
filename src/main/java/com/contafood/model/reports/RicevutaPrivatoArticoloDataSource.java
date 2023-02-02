package com.contafood.model.reports;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RicevutaPrivatoArticoloDataSource {

    private String codiceArticolo;

    private String descrizioneArticolo;

    private String lotto;

    private String udm;

    private Float quantita;

    private Integer pezzi;

    private BigDecimal prezzo;

    private BigDecimal sconto;

    private BigDecimal imponibile;

    private Integer iva;

    private BigDecimal prezzoConIva;

    private BigDecimal totale;

    private BigDecimal ivaValore;
}
