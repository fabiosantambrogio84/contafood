package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "fattura_accom_articolo_ordine_cliente")
public class FatturaAccompagnatoriaArticoloOrdineCliente implements Serializable {

    private static final long serialVersionUID = 119474159135638986L;

    @EmbeddedId
    FatturaAccompagnatoriaArticoloOrdineClienteKey id;

    @ManyToOne
    @JoinColumns( {
        @JoinColumn(name = "id_fattura_accom", referencedColumnName = "id_fattura_accom", insertable = false, updatable = false),
        @JoinColumn(name = "id_articolo", referencedColumnName = "id_articolo", insertable = false, updatable = false),
        @JoinColumn(name = "uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    })
    @JsonIgnoreProperties("fatturaAccompagnatoriaArticoliOrdiniClienti")
    private FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo;

    @ManyToOne
    @MapsId("id_ordine_cliente")
    @JoinColumn(name = "id_ordine_cliente")
    @JsonIgnoreProperties("fatturaAccompagnatoriaArticoliOrdiniClienti")
    private OrdineCliente ordineCliente;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    public FatturaAccompagnatoriaArticoloOrdineClienteKey getId() {
        return id;
    }

    public void setId(FatturaAccompagnatoriaArticoloOrdineClienteKey id) {
        this.id = id;
    }

    public FatturaAccompagnatoriaArticolo getFatturaAccompagnatoriaArticolo() {
        return fatturaAccompagnatoriaArticolo;
    }

    public void setFatturaAccompagnatoriaArticolo(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo) {
        this.fatturaAccompagnatoriaArticolo = fatturaAccompagnatoriaArticolo;
    }

    public OrdineCliente getOrdineCliente() {
        return ordineCliente;
    }

    public void setOrdineCliente(OrdineCliente ordineCliente) {
        this.ordineCliente = ordineCliente;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("fatturaAccompagnatoriaId: " + id.fatturaAccompagnatoriaId);
        result.append(", articoloId: " + id.articoloId);
        result.append(", uuid: " + id.uuid);
        result.append(", ordineClienteId: " + id.ordineClienteId);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
