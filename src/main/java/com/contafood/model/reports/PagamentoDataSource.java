package com.contafood.model.reports;

import java.math.BigDecimal;

public class PagamentoDataSource {

    private String data;

    private String clienteFornitore;

    private String descrizione;

    private BigDecimal importo;

    private String tipo;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getClienteFornitore() {
        return clienteFornitore;
    }

    public void setClienteFornitore(String clienteFornitore) {
        this.clienteFornitore = clienteFornitore;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


}
