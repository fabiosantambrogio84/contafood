package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"fatturaAcquistoDdtAcquisti", "fatturaAcquistoPagamenti"})
@Entity
@Table(name = "fattura_acquisto")
public class FatturaAcquisto {

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
    @JoinColumn(name="id_fornitore")
    private Fornitore fornitore;

    @ManyToOne
    @JoinColumn(name="id_stato")
    private StatoFattura statoFattura;

    @ManyToOne
    @JoinColumn(name="id_causale")
    private Causale causale;

    @Column(name = "totale_imponibile")
    private BigDecimal totaleImponibile;

    @Column(name = "totale_acconto")
    private BigDecimal totaleAcconto;

    @Column(name = "totale_iva")
    private BigDecimal totaleIva;

    @Column(name = "totale")
    private BigDecimal totale;

    @Column(name = "note")
    private String note;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @OneToMany(mappedBy = "fatturaAcquisto")
    @JsonIgnoreProperties("fatturaAcquisto")
    private Set<FatturaAcquistoDdtAcquisto> fatturaAcquistoDdtAcquisti = new HashSet<>();

    @OneToMany(mappedBy = "fatturaAcquisto")
    @JsonIgnoreProperties("fatturaAcquisto")
    private Set<Pagamento> fatturaAcquistoPagamenti = new HashSet<>();

    @Override
    public String toString() {

        return "{" +
                "id: " + id +
                ", numero: " + numero +
                ", anno: " + anno +
                ", data: " + data +
                ", fornitore: " + fornitore +
                ", statoFattura: " + statoFattura +
                ", causale: " + causale +
                ", totaleImponibile: " + totaleImponibile +
                ", totaleAcconto: " + totaleAcconto +
                ", totaleIva: " + totaleIva +
                ", totale: " + totale +
                ", note: " + note +
                ", dataInserimento: " + dataInserimento +
                ", dataAggiornamento: " + dataAggiornamento +
                "}";
    }
}
