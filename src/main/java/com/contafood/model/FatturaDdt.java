package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "fattura_ddt")
public class FatturaDdt implements Serializable {

    private static final long serialVersionUID = -3593111937916990146L;

    @EmbeddedId
    FatturaDdtKey id;

    @ManyToOne
    @MapsId("id_fattura")
    @JoinColumn(name = "id_fattura")
    @JsonIgnoreProperties("fatturaDdts")
    private Fattura fattura;

    @ManyToOne
    @MapsId("id_ddt")
    @JoinColumn(name = "id_ddt")
    @JsonIgnoreProperties("fatturaDdts")
    private Ddt ddt;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public FatturaDdtKey getId() {
        return id;
    }

    public void setId(FatturaDdtKey id) {
        this.id = id;
    }

    public Fattura getFattura() {
        return fattura;
    }

    public void setFattura(Fattura fattura) {
        this.fattura = fattura;
    }

    public Ddt getDdt() {
        return ddt;
    }

    public void setDdt(Ddt ddt) {
        this.ddt = ddt;
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("fatturaId: " + id.fatturaId);
        result.append(", ddtId: " + id.ddtId);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
