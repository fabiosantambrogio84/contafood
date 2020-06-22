package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = {"ddtAcquistoArticoli", "ddtAcquistoIngredienti"})
@Entity
@Table(name = "ddt_acquisto")
public class DdtAcquisto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "data")
    private Date data;

    @ManyToOne
    @JoinColumn(name="id_fornitore")
    private Fornitore fornitore;

    @Column(name = "numero_colli")
    private Integer numeroColli;

    @Column(name = "totale_imponibile")
    private BigDecimal totaleImponibile;

    @Column(name = "totale")
    private BigDecimal totale;

    @Column(name = "note")
    private String note;

    @Transient
    private Boolean modificaGiacenze;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @OneToMany(mappedBy = "ddtAcquisto")
    @JsonIgnoreProperties("ddtAcquisto")
    private Set<DdtAcquistoArticolo> ddtAcquistoArticoli = new HashSet<>();

    @OneToMany(mappedBy = "ddtAcquisto")
    @JsonIgnoreProperties("ddtAcquisto")
    private Set<DdtAcquistoIngrediente> ddtAcquistoIngredienti = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Fornitore getFornitore() {
        return fornitore;
    }

    public void setFornitore(Fornitore fornitore) {
        this.fornitore = fornitore;
    }

    public Integer getNumeroColli() {
        return numeroColli;
    }

    public void setNumeroColli(Integer numeroColli) {
        this.numeroColli = numeroColli;
    }

    public BigDecimal getTotaleImponibile() {
        return totaleImponibile;
    }

    public void setTotaleImponibile(BigDecimal totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getModificaGiacenze() {
        return modificaGiacenze;
    }

    public void setModificaGiacenze(Boolean modificaGiacenze) {
        this.modificaGiacenze = modificaGiacenze;
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

    public Set<DdtAcquistoArticolo> getDdtAcquistoArticoli() {
        return ddtAcquistoArticoli;
    }

    public void setDdtAcquistoArticoli(Set<DdtAcquistoArticolo> ddtAcquistoArticoli) {
        this.ddtAcquistoArticoli = ddtAcquistoArticoli;
    }

    public Set<DdtAcquistoIngrediente> getDdtAcquistoIngredienti() {
        return ddtAcquistoIngredienti;
    }

    public void setDdtAcquistoIngredienti(Set<DdtAcquistoIngrediente> ddtAcquistoIngredienti) {
        this.ddtAcquistoIngredienti = ddtAcquistoIngredienti;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", numero: " + numero);
        result.append(", data: " + data);
        result.append(", fornitore: " + fornitore);
        result.append(", numeroColli: " + numeroColli);
        result.append(", totaleImponibile: " + totaleImponibile);
        result.append(", totale: " + totale);
        result.append(", note: " + note);
        result.append(", modificaGiacenze: " + modificaGiacenze);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
