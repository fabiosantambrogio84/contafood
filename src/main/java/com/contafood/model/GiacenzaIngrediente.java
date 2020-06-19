package com.contafood.model;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode
@Entity
@Table(name = "giacenza_ingrediente")
public class GiacenzaIngrediente {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_ingrediente")
    private Ingrediente ingrediente;

    @ManyToOne
    @JoinColumn(name="id_ricetta")
    private Ricetta ricetta;

    @Column(name = "codice_articolo_ricetta")
    private String codiceArticoloRicetta;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "scadenza")
    private Date scadenza;

    @Column(name = "quantita")
    private Float quantita;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @Transient
    List<Movimentazione> movimentazioni;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public Ricetta getRicetta() {
        return ricetta;
    }

    public void setRicetta(Ricetta ricetta) {
        this.ricetta = ricetta;
    }

    public String getCodiceArticoloRicetta() {
        return codiceArticoloRicetta;
    }

    public void setCodiceArticoloRicetta(String codiceArticoloRicetta) {
        this.codiceArticoloRicetta = codiceArticoloRicetta;
    }

    public String getLotto() {
        return lotto;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public Float getQuantita() {
        return quantita;
    }

    public void setQuantita(Float quantita) {
        this.quantita = quantita;
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

    public List<Movimentazione> getMovimentazioni() {
        return movimentazioni;
    }

    public void setMovimentazioni(List<Movimentazione> movimentazioni) {
        this.movimentazioni = movimentazioni;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", ingrediente: " + ingrediente);
        result.append(", ricetta: " + ricetta);
        result.append(", codiceArticoloRicetta: " + codiceArticoloRicetta);
        result.append(", lotto: " + lotto);
        result.append(", scadenza: " + scadenza);
        result.append(", quantita: " + quantita);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();

    }
}
