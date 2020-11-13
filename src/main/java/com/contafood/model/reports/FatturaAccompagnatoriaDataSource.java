package com.contafood.model.reports;

public class FatturaAccompagnatoriaDataSource {

    private String numero;

    private String data;

    private String clientePartitaIva;

    private String clienteCodiceFiscale;

    private String clienteDescrizione;

    private String pagamento;

    private String agente;

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

}
