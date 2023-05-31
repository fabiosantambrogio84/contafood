package com.contafood.model.reports;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FatturaAccompagnatoriaAcquistoRigaDataSource {

    private String codiceArticolo;

    private String descrizioneArticolo;

    private String scadenza;

    private String lotto;

    private String udm;

    private Float quantita;

    private BigDecimal prezzo;

    private BigDecimal sconto;

    private BigDecimal imponibile;

    private Integer iva;

}
