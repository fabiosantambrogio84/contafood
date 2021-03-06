package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(exclude = {"articoloImmagini", "sconti", "listiniPrezzi", "listiniPrezziVariazioni", "ordineClienteArticoli", "ddtArticoli", "ddtAcquistoArticoli", "fatturaAccompagnatoriaArticoli", "notaAccreditoRighe", "giacenze"})
@Entity
@Table(name = "articolo")
public class Articolo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice")
    private String codice;

    @Column(name = "descrizione")
    private String descrizione;

    @ManyToOne
    @JoinColumn(name="id_categoria")
    private CategoriaArticolo categoria;

    @ManyToOne
    @JoinColumn(name="id_fornitore")
    private Fornitore fornitore;

    @ManyToOne
    @JoinColumn(name="id_aliquota_iva")
    private AliquotaIva aliquotaIva;

    @ManyToOne
    @JoinColumn(name="id_unita_misura")
    private UnitaMisura unitaMisura;

    @Column(name = "data")
    private Date data;

    @Column(name = "quantita_predefinita")
    private Float quantitaPredefinita;

    @Column(name = "prezzo_acquisto")
    private BigDecimal prezzoAcquisto;

    @Column(name = "prezzo_listino_base")
    private BigDecimal prezzoListinoBase;

    @Column(name = "scadenza_giorni")
    private Integer scadenzaGiorni;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "complete_barcode")
    private Boolean completeBarcode;

    @Column(name = "sito_web")
    private Boolean sitoWeb;

    @Column(name = "attivo")
    private Boolean attivo;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @OneToMany(mappedBy = "articolo")
    @JsonIgnore
    private List<ArticoloImmagine> articoloImmagini;

    @OneToMany(mappedBy = "articolo")
    @JsonIgnore
    private List<Sconto> sconti;

    @OneToMany(mappedBy = "articolo")
    @JsonIgnore
    private List<ListinoPrezzo> listiniPrezzi;

    @OneToMany(mappedBy = "articolo")
    @JsonIgnore
    private List<ListinoPrezzoVariazione> listiniPrezziVariazioni;

    @OneToMany(mappedBy = "articolo")
    @JsonIgnore
    private Set<OrdineClienteArticolo> ordineClienteArticoli = new HashSet<>();

    @OneToMany(mappedBy = "articolo")
    @JsonIgnore
    private Set<DdtArticolo> ddtArticoli = new HashSet<>();

    @OneToMany(mappedBy = "articolo")
    @JsonIgnore
    private Set<DdtAcquistoArticolo> ddtAcquistoArticoli = new HashSet<>();

    @OneToMany(mappedBy = "articolo")
    @JsonIgnore
    private Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = new HashSet<>();

    @OneToMany(mappedBy = "articolo")
    @JsonIgnore
    private Set<NotaAccreditoRiga> notaAccreditoRighe = new HashSet<>();

    @OneToMany(mappedBy = "articolo")
    @JsonIgnore
    private Set<GiacenzaArticolo> giacenze = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public CategoriaArticolo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaArticolo categoria) {
        this.categoria = categoria;
    }

    public Fornitore getFornitore() {
        return fornitore;
    }

    public void setFornitore(Fornitore fornitore) {
        this.fornitore = fornitore;
    }

    public AliquotaIva getAliquotaIva() {
        return aliquotaIva;
    }

    public void setAliquotaIva(AliquotaIva aliquotaIva) {
        this.aliquotaIva = aliquotaIva;
    }

    public UnitaMisura getUnitaMisura() {
        return unitaMisura;
    }

    public void setUnitaMisura(UnitaMisura unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Float getQuantitaPredefinita() {
        return quantitaPredefinita;
    }

    public void setQuantitaPredefinita(Float quantitaPredefinita) {
        this.quantitaPredefinita = quantitaPredefinita;
    }

    public BigDecimal getPrezzoAcquisto() {
        return prezzoAcquisto;
    }

    public void setPrezzoAcquisto(BigDecimal prezzoAcquisto) {
        this.prezzoAcquisto = prezzoAcquisto;
    }

    public BigDecimal getPrezzoListinoBase() {
        return prezzoListinoBase;
    }

    public void setPrezzoListinoBase(BigDecimal prezzoListinoBase) {
        this.prezzoListinoBase = prezzoListinoBase;
    }

    public Integer getScadenzaGiorni() {
        return scadenzaGiorni;
    }

    public void setScadenzaGiorni(Integer scadenzaGiorni) {
        this.scadenzaGiorni = scadenzaGiorni;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Boolean getCompleteBarcode() {
        return completeBarcode;
    }

    public void setCompleteBarcode(Boolean completeBarcode) {
        this.completeBarcode = completeBarcode;
    }

    public Boolean getSitoWeb() {
        return sitoWeb;
    }

    public void setSitoWeb(Boolean sitoWeb) {
        this.sitoWeb = sitoWeb;
    }

    public Boolean getAttivo() {
        return attivo;
    }

    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
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

    public List<ArticoloImmagine> getArticoloImmagini() {
        return articoloImmagini;
    }

    public void setArticoloImmagini(List<ArticoloImmagine> articoloImmagini) {
        this.articoloImmagini = articoloImmagini;
    }

    public List<Sconto> getSconti() {
        return sconti;
    }

    public void setSconti(List<Sconto> sconti) {
        this.sconti = sconti;
    }

    public List<ListinoPrezzo> getListiniPrezzi() {
        return listiniPrezzi;
    }

    public void setListiniPrezzi(List<ListinoPrezzo> listiniPrezzi) {
        this.listiniPrezzi = listiniPrezzi;
    }

    public List<ListinoPrezzoVariazione> getListiniPrezziVariazioni() {
        return listiniPrezziVariazioni;
    }

    public void setListiniPrezziVariazioni(List<ListinoPrezzoVariazione> listiniPrezziVariazioni) {
        this.listiniPrezziVariazioni = listiniPrezziVariazioni;
    }

    public Set<OrdineClienteArticolo> getOrdineClienteArticoli() {
        return ordineClienteArticoli;
    }

    public void setOrdineClienteArticoli(Set<OrdineClienteArticolo> ordineClienteArticoli) {
        this.ordineClienteArticoli = ordineClienteArticoli;
    }

    public Set<DdtArticolo> getDdtArticoli() {
        return ddtArticoli;
    }

    public void setDdtArticoli(Set<DdtArticolo> ddtArticoli) {
        this.ddtArticoli = ddtArticoli;
    }

    public Set<DdtAcquistoArticolo> getDdtAcquistoArticoli() {
        return ddtAcquistoArticoli;
    }

    public void setDdtAcquistoArticoli(Set<DdtAcquistoArticolo> ddtAcquistoArticoli) {
        this.ddtAcquistoArticoli = ddtAcquistoArticoli;
    }

    public Set<FatturaAccompagnatoriaArticolo> getFatturaAccompagnatoriaArticoli() {
        return fatturaAccompagnatoriaArticoli;
    }

    public void setFatturaAccompagnatoriaArticoli(Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli) {
        this.fatturaAccompagnatoriaArticoli = fatturaAccompagnatoriaArticoli;
    }

    public Set<NotaAccreditoRiga> getNotaAccreditoRighe() {
        return notaAccreditoRighe;
    }

    public void setNotaAccreditoRighe(Set<NotaAccreditoRiga> notaAccreditoRighe) {
        this.notaAccreditoRighe = notaAccreditoRighe;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", codice: " + codice);
        result.append(", descrizione: " + descrizione);
        result.append(", categoria: " + categoria);
        result.append(", fornitore: " + fornitore);
        result.append(", aliquotaIva: " + aliquotaIva);
        result.append(", unitaMisura: " + unitaMisura);
        result.append(", data: " + data);
        result.append(", quantitaPredefinita: " + quantitaPredefinita);
        result.append(", prezzoAcquisto: " + prezzoAcquisto);
        result.append(", prezzoListinoBase: " + prezzoListinoBase);
        result.append(", scadenzaGiorni: " + scadenzaGiorni);
        result.append(", barcode: " + barcode);
        result.append(", completeBarcode: " + completeBarcode);
        result.append(", sitoWeb: " + sitoWeb);
        result.append(", attivo: " + attivo);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
