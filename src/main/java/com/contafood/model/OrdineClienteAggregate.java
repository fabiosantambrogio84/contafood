package com.contafood.model;

import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode
public class OrdineClienteAggregate {

    private Long idArticolo;

    private String articolo;

    private BigDecimal prezzoListinoBase;

    private Integer numeroPezziOrdinati;

    private Integer numeroPezziDaEvadere;

    private Integer numeroPezziEvasi;

    private String idsOrdiniClienti;

    private String codiciOrdiniClienti;

    public Long getIdArticolo() {
        return idArticolo;
    }

    public void setIdArticolo(Long idArticolo) {
        this.idArticolo = idArticolo;
    }

    public String getArticolo() {
        return articolo;
    }

    public void setArticolo(String articolo) {
        this.articolo = articolo;
    }

    public BigDecimal getPrezzoListinoBase() {
        return prezzoListinoBase;
    }

    public void setPrezzoListinoBase(BigDecimal prezzoListinoBase) {
        this.prezzoListinoBase = prezzoListinoBase;
    }

    public Integer getNumeroPezziOrdinati() {
        return numeroPezziOrdinati;
    }

    public void setNumeroPezziOrdinati(Integer numeroPezziOrdinati) {
        this.numeroPezziOrdinati = numeroPezziOrdinati;
    }

    public Integer getNumeroPezziDaEvadere() {
        return numeroPezziDaEvadere;
    }

    public void setNumeroPezziDaEvadere(Integer numeroPezziDaEvadere) {
        this.numeroPezziDaEvadere = numeroPezziDaEvadere;
    }

    public Integer getNumeroPezziEvasi() {
        return numeroPezziEvasi;
    }

    public void setNumeroPezziEvasi(Integer numeroPezziEvasi) {
        this.numeroPezziEvasi = numeroPezziEvasi;
    }

    public String getIdsOrdiniClienti() {
        return idsOrdiniClienti;
    }

    public void setIdsOrdiniClienti(String idsOrdiniClienti) {
        this.idsOrdiniClienti = idsOrdiniClienti;
    }

    public String getCodiciOrdiniClienti() {
        return codiciOrdiniClienti;
    }

    public void setCodiciOrdiniClienti(String codiciOrdiniClienti) {
        this.codiciOrdiniClienti = codiciOrdiniClienti;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("idArticolo: " + idArticolo);
        result.append(", articolo: " + articolo);
        result.append(", prezzoListinoBase: " + prezzoListinoBase);
        result.append(", numeroPezziOrdinati: " + numeroPezziOrdinati);
        result.append(", numeroPezziDaEvadere: " + numeroPezziDaEvadere);
        result.append(", numeroPezziEvasi: " + numeroPezziEvasi);
        result.append(", idsOrdiniClienti: " + idsOrdiniClienti);
        result.append(", codiciOrdiniClienti: " + codiciOrdiniClienti);
        result.append("}");

        return result.toString();
    }
}
