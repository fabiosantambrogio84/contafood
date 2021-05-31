package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = {"ddtArticoli", "ddtPagamenti", "fatturaDdts"})
@Entity
@Table(name = "ddt")
public class
Ddt {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "progressivo")
    private Integer progressivo;

    @Column(name = "anno_contabile")
    private Integer annoContabile;

    @Column(name = "data")
    private Date data;

    @ManyToOne
    @JoinColumn(name="id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name="id_punto_consegna")
    private PuntoConsegna puntoConsegna;

    @ManyToOne
    @JoinColumn(name="id_autista")
    private Autista autista;

    @ManyToOne
    @JoinColumn(name="id_stato")
    private StatoDdt statoDdt;

    @ManyToOne
    @JoinColumn(name="id_causale")
    private Causale causale;

    @Column(name = "numero_colli")
    private Integer numeroColli;

    @Column(name = "tipo_trasporto")
    private String tipoTrasporto;

    @Column(name = "data_trasporto")
    private Date dataTrasporto;

    @Column(name = "ora_trasporto")
    private Time oraTrasporto;

    @Column(name = "trasportatore")
    private String trasportatore;

    @Column(name = "fatturato")
    private Boolean fatturato;

    @Column(name = "totale_imponibile")
    private BigDecimal totaleImponibile;

    @Column(name = "totale_iva")
    private BigDecimal totaleIva;

    @Column(name = "totale_costo")
    private BigDecimal totaleCosto;

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

    @Transient
    private String scannerLog;

    @Transient
    private Boolean modificaGiacenze;

    @OneToMany(mappedBy = "ddt")
    @JsonIgnoreProperties("ddt")
    private Set<DdtArticolo> ddtArticoli = new HashSet<>();

    @OneToMany(mappedBy = "ddt")
    @JsonIgnoreProperties("ddt")
    private Set<Pagamento> ddtPagamenti = new HashSet<>();

    @OneToMany(mappedBy = "ddt")
    @JsonIgnore
    private Set<FatturaDdt> fatturaDdts = new HashSet<>();

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

    public Integer getAnnoContabile() {
        return annoContabile;
    }

    public void setAnnoContabile(Integer annoContabile) {
        this.annoContabile = annoContabile;
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

    public PuntoConsegna getPuntoConsegna() {
        return puntoConsegna;
    }

    public void setPuntoConsegna(PuntoConsegna puntoConsegna) {
        this.puntoConsegna = puntoConsegna;
    }

    public Autista getAutista() {
        return autista;
    }

    public void setAutista(Autista autista) {
        this.autista = autista;
    }

    public StatoDdt getStatoDdt() {
        return statoDdt;
    }

    public void setStatoDdt(StatoDdt statoDdt) {
        this.statoDdt = statoDdt;
    }

    public Causale getCausale() {
        return causale;
    }

    public void setCausale(Causale causale) {
        this.causale = causale;
    }

    public Integer getNumeroColli() {
        return numeroColli;
    }

    public void setNumeroColli(Integer numeroColli) {
        this.numeroColli = numeroColli;
    }

    public String getTipoTrasporto() {
        return tipoTrasporto;
    }

    public void setTipoTrasporto(String tipoTrasporto) {
        this.tipoTrasporto = tipoTrasporto;
    }

    public Date getDataTrasporto() {
        return dataTrasporto;
    }

    public void setDataTrasporto(Date dataTrasporto) {
        this.dataTrasporto = dataTrasporto;
    }

    public Time getOraTrasporto() {
        return oraTrasporto;
    }

    public void setOraTrasporto(Time oraTrasporto) {
        this.oraTrasporto = oraTrasporto;
    }

    public String getTrasportatore() {
        return trasportatore;
    }

    public void setTrasportatore(String trasportatore) {
        this.trasportatore = trasportatore;
    }

    public Boolean getFatturato() {
        return fatturato;
    }

    public void setFatturato(Boolean fatturato) {
        this.fatturato = fatturato;
    }

    public BigDecimal getTotaleImponibile() {
        return totaleImponibile;
    }

    public void setTotaleImponibile(BigDecimal totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
    }

    public BigDecimal getTotaleIva() {
        return totaleIva;
    }

    public void setTotaleIva(BigDecimal totaleIva) {
        this.totaleIva = totaleIva;
    }

    public BigDecimal getTotaleCosto() {
        return totaleCosto;
    }

    public void setTotaleCosto(BigDecimal totaleCosto) {
        this.totaleCosto = totaleCosto;
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

    public String getScannerLog() {
        return scannerLog;
    }

    public void setScannerLog(String scannerLog) {
        this.scannerLog = scannerLog;
    }

    public Boolean getModificaGiacenze() {
        return modificaGiacenze;
    }

    public void setModificaGiacenze(Boolean modificaGiacenze) {
        this.modificaGiacenze = modificaGiacenze;
    }

    public Set<DdtArticolo> getDdtArticoli() {
        return ddtArticoli;
    }

    public void setDdtArticoli(Set<DdtArticolo> ddtArticoli) {
        this.ddtArticoli = ddtArticoli;
    }

    public Set<Pagamento> getDdtPagamenti() {
        return ddtPagamenti;
    }

    public void setDdtPagamenti(Set<Pagamento> ddtPagamenti) {
        this.ddtPagamenti = ddtPagamenti;
    }

    public Set<FatturaDdt> getFatturaDdts() {
        return fatturaDdts;
    }

    public void setFatturaDdts(Set<FatturaDdt> fatturaDdts) {
        this.fatturaDdts = fatturaDdts;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", progressivo: " + progressivo);
        result.append(", annoContabile: " + annoContabile);
        result.append(", data: " + data);
        result.append(", cliente: " + cliente);
        result.append(", puntoConsegna: " + puntoConsegna);
        result.append(", autista: " + autista);
        result.append(", statoDdt: " + statoDdt);
        result.append(", causale: " + causale);
        result.append(", numeroColli: " + numeroColli);
        result.append(", tipoTrasporto: " + tipoTrasporto);
        result.append(", dataTrasporto: " + dataTrasporto);
        result.append(", oraTrasporto: " + oraTrasporto);
        result.append(", trasportatore: " + trasportatore);
        result.append(", fatturato: " + fatturato);
        result.append(", totaleImponibile: " + totaleImponibile);
        result.append(", totaleIva: " + totaleIva);
        result.append(", totaleCosto: " + totaleCosto);
        result.append(", totale: " + totale);
        result.append(", totaleAcconto: " + totaleAcconto);
        result.append(", note: " + note);
        result.append(", modificaGiacenze: " + modificaGiacenze);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
