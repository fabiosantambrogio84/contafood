package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
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

    @Column(name = "barcode_mask_lotto_scadenza")
    private String barcodeMaskLottoScadenza;

    @Column(name = "barcode_regexp_lotto")
    private String barcodeRegexpLotto;

    @Column(name = "barcode_regexp_data_scadenza")
    private String barcodeRegexpDataScadenza;

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
        result.append(", barcodeMaskLottoScadenza: " + barcodeMaskLottoScadenza);
        result.append(", barcodeRegexpLotto: " + barcodeRegexpLotto);
        result.append(", barcodeRegexpDataScadenza: " + barcodeRegexpDataScadenza);
        result.append(", sitoWeb: " + sitoWeb);
        result.append(", attivo: " + attivo);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
