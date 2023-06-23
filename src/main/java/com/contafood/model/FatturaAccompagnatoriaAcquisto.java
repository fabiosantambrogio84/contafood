package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"fatturaAccompagnatoriaAcquistoArticoli", "fatturaAccompagnatoriaAcquistoIngredienti", "fatturaAccompagnatoriaAcquistoTotali", "fatturaAccompagnatoriaAcquistoPagamenti"})
@Entity
@Table(name = "fattura_accom_acquisto")
public class FatturaAccompagnatoriaAcquisto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "anno")
    private Integer anno;

    @Column(name = "data")
    private Date data;

    @ManyToOne
    @JoinColumn(name="id_tipo")
    private TipoFattura tipoFattura;

    @ManyToOne
    @JoinColumn(name="id_fornitore")
    private Fornitore fornitore;

    @ManyToOne
    @JoinColumn(name="id_stato")
    private StatoFattura statoFattura;

    @ManyToOne
    @JoinColumn(name="id_causale")
    private Causale causale;

    @Column(name = "spedito_ade")
    private Boolean speditoAde;

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

    @Column(name = "totale_imponibile")
    private BigDecimal totaleImponibile;

    @Column(name = "totale_iva")
    private BigDecimal totaleIva;

    @Column(name = "totale_acconto")
    private BigDecimal totaleAcconto;

    @Column(name = "totale")
    private BigDecimal totale;

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

    @OneToMany(mappedBy = "fatturaAccompagnatoriaAcquisto")
    @JsonIgnoreProperties("fatturaAccompagnatoriaAcquisto")
    private Set<FatturaAccompagnatoriaAcquistoArticolo> fatturaAccompagnatoriaAcquistoArticoli = new HashSet<>();

    @OneToMany(mappedBy = "fatturaAccompagnatoriaAcquisto")
    @JsonIgnoreProperties("fatturaAccompagnatoriaAcquisto")
    private Set<FatturaAccompagnatoriaAcquistoIngrediente> fatturaAccompagnatoriaAcquistoIngredienti = new HashSet<>();

    @OneToMany(mappedBy = "fatturaAccompagnatoriaAcquisto")
    @JsonIgnoreProperties("fatturaAccompagnatoriaAcquisto")
    private Set<FatturaAccompagnatoriaAcquistoTotale> fatturaAccompagnatoriaAcquistoTotali = new HashSet<>();

    @OneToMany(mappedBy = "fatturaAccompagnatoriaAcquisto")
    @JsonIgnoreProperties("fatturaAccompagnatoriaAcquisto")
    private Set<Pagamento> fatturaAccompagnatoriaAcquistoPagamenti = new HashSet<>();

    @Override
    public String toString() {

        return "{" +
                "id: " + id +
                ", numero: " + numero +
                ", anno: " + anno +
                ", data: " + data +
                ", tipoFattura: " + tipoFattura +
                ", fornitore: " + fornitore +
                ", statoFattura: " + statoFattura +
                ", causale: " + causale +
                ", speditoAde: " + speditoAde +
                ", numeroColli: " + numeroColli +
                ", tipoTrasporto: " + tipoTrasporto +
                ", dataTrasporto: " + dataTrasporto +
                ", oraTrasporto: " + oraTrasporto +
                ", trasportatore: " + trasportatore +
                ", totaleAcconto: " + totaleAcconto +
                ", totale: " + totale +
                ", totaleQuantita: " + totaleQuantita +
                ", note: " + note +
                ", dataInserimento: " + dataInserimento +
                ", dataAggiornamento: " + dataAggiornamento +
                "}";
    }
}