package com.contafood.model.reports;

import java.math.BigDecimal;

public class FatturaCommercianteTotaleDataSource {

    private BigDecimal imponibile;

    private Integer iva;

    private BigDecimal imposta;

    public BigDecimal getImponibile() {
        return imponibile;
    }

    public void setImponibile(BigDecimal imponibile) {
        this.imponibile = imponibile;
    }

    public Integer getIva() {
        return iva;
    }

    public void setIva(Integer iva) {
        this.iva = iva;
    }

    public BigDecimal getImposta() {
        return imposta;
    }

    public void setImposta(BigDecimal imposta) {
        this.imposta = imposta;
    }
}
