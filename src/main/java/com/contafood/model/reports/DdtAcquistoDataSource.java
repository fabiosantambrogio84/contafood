package com.contafood.model.reports;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DdtAcquistoDataSource {

    private String numero;

    private String data;

    private String fornitorePartitaIva;

    private String fornitoreCodiceFiscale;

    private String fornitoreDescrizione;

    private String pagamento;

    private BigDecimal acconto;

    private BigDecimal totale;

    private BigDecimal totaleDaPagare;
}
