package com.contafood.model.reports;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FatturaTotaleDataSource {

    private Integer aliquotaIva;

    private BigDecimal totaleImponibile;

    private BigDecimal totaleIva;

    private BigDecimal totaleIvaNotRounded;

}
