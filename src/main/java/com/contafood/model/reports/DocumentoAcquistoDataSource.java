package com.contafood.model.reports;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class DocumentoAcquistoDataSource {

    private String id;

    private String tipoDocumento;

    private Long idDocumento;

    private String numDocumento;

    private String dataDocumento;

    private Long idFornitore;

    private String ragioneSocialeFornitore;

    private String partitaIvaFornitore;

    private Long idStato;

    private String stato;

    private BigDecimal totaleImponibile;

    private BigDecimal totaleIva;

    private BigDecimal totale;

    private BigDecimal totaleAcconto;

    private Boolean fatturato;
}