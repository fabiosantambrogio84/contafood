package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "ordine_cliente_articolo")
public class OrdineClienteArticolo implements Serializable {

    private static final long serialVersionUID = 6840456616585695956L;

    @EmbeddedId
    OrdineClienteArticoloKey id;

    @ManyToOne
    @MapsId("id_ordine_cliente")
    @JoinColumn(name = "id_ordine_cliente")
    @JsonIgnoreProperties("ordineClienteArticoli")
    private OrdineCliente ordineCliente;

    @ManyToOne
    @MapsId("id_articolo")
    @JoinColumn(name = "id_articolo")
    @JsonIgnoreProperties("ordineClienteArticoli")
    private Articolo articolo;

    @Column(name = "num_ordinati")
    private Integer numeroPezziOrdinati;

    @Column(name = "num_da_evadere")
    private Integer numeroPezziDaEvadere;

    @Column(name = "id_ddts")
    private String idDdts;

    @Column(name = "id_ordine_fornitore")
    private Long ordineFornitoreId;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @Transient
    private BigDecimal prezzo;

    public OrdineClienteArticoloKey getId() {
        return id;
    }

    public void setId(OrdineClienteArticoloKey id) {
        this.id = id;
    }

    public OrdineCliente getOrdineCliente() {
        return ordineCliente;
    }

    public void setOrdineCliente(OrdineCliente ordineCliente) {
        this.ordineCliente = ordineCliente;
    }

    public Articolo getArticolo() {
        return articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
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

    public String getIdDdts() {
        return idDdts;
    }

    public void setIdDdts(String idDdts) {
        this.idDdts = idDdts;
    }

    public Long getOrdineFornitoreId() {
        return ordineFornitoreId;
    }

    public void setOrdineFornitoreId(Long ordineFornitoreId) {
        this.ordineFornitoreId = ordineFornitoreId;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public Timestamp getDataAggiornamento() {
        return dataAggiornamento;
    }

    public void setDataAggiornamento(Timestamp dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ordineClienteId: " + id.ordineClienteId);
        result.append(", articoloId: " + id.articoloId);
        result.append(", numeroPezziOrdinati: " + numeroPezziOrdinati);
        result.append(", numeroPezziDaEvadere: " + numeroPezziDaEvadere);
        result.append(", idDdts: " + idDdts);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
