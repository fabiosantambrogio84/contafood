package com.contafood.model.reports;

import java.math.BigDecimal;

public class NotaAccreditoDataSource {

    private String numero;

    private String data;

    private String clientePartitaIva;

    private String clienteCodiceFiscale;

    private String clienteDescrizione;

    private String pagamento;

    private String agente;

    private BigDecimal acconto;

    private BigDecimal totale;

    private BigDecimal totaleDaPagare;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getClientePartitaIva() {
        return clientePartitaIva;
    }

    public void setClientePartitaIva(String clientePartitaIva) {
        this.clientePartitaIva = clientePartitaIva;
    }

    public String getClienteCodiceFiscale() {
        return clienteCodiceFiscale;
    }

    public void setClienteCodiceFiscale(String clienteCodiceFiscale) {
        this.clienteCodiceFiscale = clienteCodiceFiscale;
    }

    public String getClienteDescrizione() {
        return clienteDescrizione;
    }

    public void setClienteDescrizione(String clienteDescrizione) {
        this.clienteDescrizione = clienteDescrizione;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public BigDecimal getAcconto() {
        return acconto;
    }

    public void setAcconto(BigDecimal acconto) {
        this.acconto = acconto;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public BigDecimal getTotaleDaPagare() {
        return totaleDaPagare;
    }

    public void setTotaleDaPagare(BigDecimal totaleDaPagare) {
        this.totaleDaPagare = totaleDaPagare;
    }
}
