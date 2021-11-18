package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"ingredienti", "listiniAssociati", "articoli", "sconti", "listiniPrezziVariazioni", "confezioni"})
@Entity
@Table(name = "fornitore")
public class Fornitore {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_tipo")
    private TipoFornitore tipoFornitore;

    @Column(name = "codice")
    private Integer codice;

    @Column(name = "ragione_sociale")
    private String ragioneSociale;

    @Column(name = "ragione_sociale_2")
    private String ragioneSociale2;

    @Column(name = "indirizzo")
    private String indirizzo;

    @Column(name = "citta")
    private String citta;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "cap")
    private String cap;

    @Column(name = "nazione")
    private String nazione;

    @Column(name = "partita_iva")
    private String partitaIva;

    @Column(name = "codice_fiscale")
    private String codiceFiscale;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "telefono_2")
    private String telefono2;

    @Column(name = "telefono_3")
    private String telefono3;

    @Column(name = "email")
    private String email;

    @Column(name = "email_pec")
    private String emailPec;

    @Column(name = "email_ordini")
    private String emailOrdini;

    @Column(name = "codice_univoco_sdi")
    private String codiceUnivocoSdi;

    @Column(name = "iban")
    private String iban;

    @Column(name = "pagamento")
    private String pagamento;

    @Column(name = "barcode_mask_lotto_scadenza")
    private String barcodeMaskLottoScadenza;

    @Column(name = "barcode_regexp_lotto")
    private String barcodeRegexpLotto;

    @Column(name = "barcode_regexp_data_scadenza")
    private String barcodeRegexpDataScadenza;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "fornitore")
    @JsonIgnore
    private List<Ingrediente> ingredienti;

    @OneToMany(mappedBy = "fornitore")
    @JsonIgnore
    private List<ListinoAssociato> listiniAssociati;

    @OneToMany(mappedBy = "fornitore")
    @JsonIgnore
    private List<Articolo> articoli;

    @OneToMany(mappedBy = "fornitore")
    @JsonIgnore
    private List<Sconto> sconti;

    @OneToMany(mappedBy = "fornitore")
    @JsonIgnore
    private List<ListinoPrezzoVariazione> listiniPrezziVariazioni;

    @OneToMany(mappedBy = "fornitore")
    @JsonIgnore
    private List<Confezione> confezioni;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", tipoFornitore: " + tipoFornitore);
        result.append(", codice: " + codice);
        result.append(", ragioneSociale: " + ragioneSociale);
        result.append(", ragioneSociale2: " + ragioneSociale2);
        result.append(", indirizzo: " + indirizzo);
        result.append(", citta: " + citta);
        result.append(", provincia: " + provincia);
        result.append(", cap: " + cap);
        result.append(", nazione: " + nazione);
        result.append(", partitaIva: " + partitaIva);
        result.append(", codiceFiscale: " + codiceFiscale);
        result.append(", telefono: " + telefono);
        result.append(", telefono2: " + telefono2);
        result.append(", telefono3: " + telefono3);
        result.append(", email: " + email);
        result.append(", emailPec: " + emailPec);
        result.append(", emailOrdini: " + emailOrdini);
        result.append(", codiceUnivocoSdi: " + codiceUnivocoSdi);
        result.append(", iban: " + iban);
        result.append(", pagamento: " + pagamento);
        result.append(", barcodeMaskLottoScadenza: " + barcodeMaskLottoScadenza);
        result.append(", barcodeRegexpLotto: " + barcodeRegexpLotto);
        result.append(", barcodeRegexpDataScadenza: " + barcodeRegexpDataScadenza);
        result.append(", note: " + note);
        result.append("}");

        return result.toString();
    }
}
