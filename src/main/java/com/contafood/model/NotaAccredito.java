package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = {"notaAccreditoArticoli", "notaAccreditoTotali", "notaAccreditoRighe", "notaAccreditoPagamenti"})
@Entity
@Table(name = "nota_accredito")
public class NotaAccredito {

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
    @JoinColumn(name="id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name="id_stato")
    private StatoNotaAccredito statoNotaAccredito;

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

    @Column(name = "tipo_riferimento")
    private String tipoRiferimento;

    @Column(name = "documento_riferimento")
    private String documentoRiferimento;

    @Column(name = "data_documento_riferimento")
    private Date dataDocumentoRiferimento;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @OneToMany(mappedBy = "notaAccredito")
    @JsonIgnoreProperties("notaAccredito")
    private Set<NotaAccreditoTotale> notaAccreditoTotali = new HashSet<>();

    @OneToMany(mappedBy = "notaAccredito")
    @JsonIgnoreProperties("notaAccredito")
    private Set<NotaAccreditoRiga> notaAccreditoRighe = new HashSet<>();

    @OneToMany(mappedBy = "notaAccredito")
    @JsonIgnoreProperties("notaAccredito")
    private Set<Pagamento> notaAccreditoPagamenti = new HashSet<>();

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public StatoNotaAccredito getStatoNotaAccredito() {
        return statoNotaAccredito;
    }

    public void setStatoNotaAccredito(StatoNotaAccredito statoNotaAccredito) {
        this.statoNotaAccredito = statoNotaAccredito;
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

    public String getTipoRiferimento() {
        return tipoRiferimento;
    }

    public void setTipoRiferimento(String tipoRiferimento) {
        this.tipoRiferimento = tipoRiferimento;
    }

    public String getDocumentoRiferimento() {
        return documentoRiferimento;
    }

    public void setDocumentoRiferimento(String documentoRiferimento) {
        this.documentoRiferimento = documentoRiferimento;
    }

    public Date getDataDocumentoRiferimento() {
        return dataDocumentoRiferimento;
    }

    public void setDataDocumentoRiferimento(Date dataDocumentoRiferimento) {
        this.dataDocumentoRiferimento = dataDocumentoRiferimento;
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

    public Set<NotaAccreditoTotale> getNotaAccreditoTotali() {
        return notaAccreditoTotali;
    }

    public void setNotaAccreditoTotali(Set<NotaAccreditoTotale> notaAccreditoTotali) {
        this.notaAccreditoTotali = notaAccreditoTotali;
    }

    public Set<NotaAccreditoRiga> getNotaAccreditoRighe() {
        return notaAccreditoRighe;
    }

    public void setNotaAccreditoRighe(Set<NotaAccreditoRiga> notaAccreditoRighe) {
        this.notaAccreditoRighe = notaAccreditoRighe;
    }

    public Set<Pagamento> getNotaAccreditoPagamenti() {
        return notaAccreditoPagamenti;
    }

    public void setNotaAccreditoPagamenti(Set<Pagamento> notaAccreditoPagamenti) {
        this.notaAccreditoPagamenti = notaAccreditoPagamenti;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", progressivo: " + progressivo);
        result.append(", anno: " + anno);
        result.append(", data: " + data);
        result.append(", cliente: " + cliente);
        result.append(", stato: " + statoNotaAccredito);
        result.append(", speditoAde: " + speditoAde);
        result.append(", totale: " + totale);
        result.append(", totaleAcconto: " + totaleAcconto);
        result.append(", totaleQuantita: " + totaleQuantita);
        result.append(", note: " + note);
        result.append(", tipoRiferimento: " + tipoRiferimento);
        result.append(", documentoRiferimento: " + documentoRiferimento);
        result.append(", dataDocumentoRiferimento: " + dataDocumentoRiferimento);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
