package com.contafood.model.reports;

import java.math.BigDecimal;

public class NotaAccreditoTotaleDataSource {

    private Integer aliquotaIva;

    private BigDecimal totaleImponibile;

    private BigDecimal totaleIva;

    public Integer getAliquotaIva() {
        return aliquotaIva;
    }

    public void setAliquotaIva(Integer aliquotaIva) {
        this.aliquotaIva = aliquotaIva;
    }

    public BigDecimal getTotaleImponibile() {
        return totaleImponibile;
    }

    public void setTotaleImponibile(BigDecimal totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
    }

    public BigDecimal getTotaleIva() {
        return totaleIva;
    }

    public void setTotaleIva(BigDecimal totaleIva) {
        this.totaleIva = totaleIva;
    }
}
