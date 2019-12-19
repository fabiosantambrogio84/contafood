package com.contafood.model;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode
@Entity
@Table(name = "sconto_fornitore")
public class ScontoFornitore {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_sconto")
    private Sconto sconto;

    @ManyToOne
    @JoinColumn(name="id_fornitore")
    private Fornitore fornitore;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sconto getSconto() {
        return sconto;
    }

    public void setSconto(Sconto sconto) {
        this.sconto = sconto;
    }

    public Fornitore getFornitore() {
        return fornitore;
    }

    public void setFornitore(Fornitore fornitore) {
        this.fornitore = fornitore;
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
        result.append("id: " + id);
        result.append(", sconto: " + sconto);
        result.append(", fornitore: " + fornitore);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
