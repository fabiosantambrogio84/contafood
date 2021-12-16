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

    @Column(name = "totale_iva")
    private BigDecimal totaleIva;

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
        result.append(", totaleIva: " + totaleIva);
        result.append(", totale: " + totale);
        result.append(", note: " + note);
        result.append(", modificaGiacenze: " + modificaGiacenze);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
