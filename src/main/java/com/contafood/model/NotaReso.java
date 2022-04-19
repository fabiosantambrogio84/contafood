package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = {"notaResoTotali", "notaResoRighe", "notaResoPagamenti"})
@Entity
@Table(name = "nota_reso")
public class NotaReso {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "progressivo")
    private Integer progressivo;

    @Column(name = "anno")
    private Integer anno;

    @Column(name = "data")
    private Date data;

    @ManyToOne
    @JoinColumn(name="id_fornitore")
    private Fornitore fornitore;

    @ManyToOne
    @JoinColumn(name="id_stato")
    private StatoNotaReso statoNotaReso;

    @ManyToOne
    @JoinColumn(name="id_causale")
    private Causale causale;

    @Column(name = "spedito_ade")
    private Boolean speditoAde;

    @Column(name = "totale")
    private BigDecimal totale;

    @Column(name = "totale_acconto")
    private BigDecimal totaleAcconto;

    @Column(name = "totale_quantita")
    private BigDecimal totaleQuantita;

    @Column(name = "note")
    private String note;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @OneToMany(mappedBy = "notaReso")
    @JsonIgnoreProperties("notaReso")
    private Set<NotaResoTotale> notaResoTotali = new HashSet<>();

    @OneToMany(mappedBy = "notaReso")
    @JsonIgnoreProperties("notaReso")
    private Set<NotaResoRiga> notaResoRighe = new HashSet<>();

    @OneToMany(mappedBy = "notaReso")
    @JsonIgnoreProperties("notaReso")
    private Set<Pagamento> notaResoPagamenti = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
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

    public StatoNotaReso getStatoNotaReso() {
        return statoNotaReso;
    }

    public void setStatoNotaReso(StatoNotaReso statoNotaReso) {
        this.statoNotaReso = statoNotaReso;
    }

    public Causale getCausale() {
        return causale;
    }

    public void setCausale(Causale causale) {
        this.causale = causale;
    }

    public Boolean getSpeditoAde() {
        return speditoAde;
    }

    public void setSpeditoAde(Boolean speditoAde) {
        this.speditoAde = speditoAde;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public BigDecimal getTotaleAcconto() {
        return totaleAcconto;
    }

    public void setTotaleAcconto(BigDecimal totaleAcconto) {
        this.totaleAcconto = totaleAcconto;
    }

    public BigDecimal getTotaleQuantita() {
        return totaleQuantita;
    }

    public void setTotaleQuantita(BigDecimal totaleQuantita) {
        this.totaleQuantita = totaleQuantita;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Set<NotaResoTotale> getNotaResoTotali() {
        return notaResoTotali;
    }

    public void setNotaResoTotali(Set<NotaResoTotale> notaResoTotali) {
        this.notaResoTotali = notaResoTotali;
    }

    public Set<NotaResoRiga> getNotaResoRighe() {
        return notaResoRighe;
    }

    public void setNotaResoRighe(Set<NotaResoRiga> notaResoRighe) {
        this.notaResoRighe = notaResoRighe;
    }

    public Set<Pagamento> getNotaResoPagamenti() {
        return notaResoPagamenti;
    }

    public void setNotaResoPagamenti(Set<Pagamento> notaResoPagamenti) {
        this.notaResoPagamenti = notaResoPagamenti;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", progressivo: " + progressivo);
        result.append(", anno: " + anno);
        result.append(", data: " + data);
        result.append(", fornitore: " + fornitore);
        result.append(", stato: " + statoNotaReso);
        result.append(", causale: " + causale);
        result.append(", speditoAde: " + speditoAde);
        result.append(", totale: " + totale);
        result.append(", totaleAcconto: " + totaleAcconto);
        result.append(", totaleQuantita: " + totaleQuantita);
        result.append(", note: " + note);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
