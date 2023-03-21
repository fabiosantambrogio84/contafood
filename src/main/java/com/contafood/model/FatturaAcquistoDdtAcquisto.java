package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "fattura_acquisto_ddt_acquisto")
public class FatturaAcquistoDdtAcquisto implements Serializable {

    private static final long serialVersionUID = 1861091817781452831L;

    @EmbeddedId
    FatturaAcquistoDdtAcquistoKey id;

    @ManyToOne
    @MapsId("id_fattura_acquisto")
    @JoinColumn(name = "id_fattura_acquisto")
    @JsonIgnoreProperties("fatturaAcquistoDdtAcquisti")
    private FatturaAcquisto fatturaAcquisto;

    @ManyToOne
    @MapsId("id_ddt_acquisto")
    @JoinColumn(name = "id_ddt_acquisto")
    @JsonIgnoreProperties("fatturaAcquistoDdtAcquisti")
    private DdtAcquisto ddtAcquisto;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("fatturaAcquistoId: " + id.fatturaAcquistoId);
        result.append(", ddtAcquistoId: " + id.ddtAcquistoId);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
