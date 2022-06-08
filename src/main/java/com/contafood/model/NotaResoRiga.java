package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "nota_reso_riga")
public class NotaResoRiga implements Serializable {

    private static final long serialVersionUID = -8462905891304101780L;

    @EmbeddedId
    NotaResoRigaKey id;

    @ManyToOne
    @MapsId("id_nota_reso")
    @JoinColumn(name = "id_nota_reso")
    @JsonIgnoreProperties("id_nota_reso")
    private NotaReso notaReso;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "scadenza")
    private Date scadenza;

    @ManyToOne
    @JoinColumn(name="id_unita_misura")
    private UnitaMisura unitaMisura;

    @Column(name = "quantita")
    private Float quantita;

    @Column(name = "prezzo")
    private BigDecimal prezzo;

    @Column(name = "sconto")
    private BigDecimal sconto;

    @ManyToOne
    @JoinColumn(name="id_aliquota_iva")
    private AliquotaIva aliquotaIva;

    @Column(name = "imponibile")
    private BigDecimal imponibile;

    @Column(name = "totale")
    private BigDecimal totale;

    @ManyToOne
    @JoinColumn(name="id_articolo")
    private Articolo articolo;

    @ManyToOne
    @JoinColumn(name="id_ingrediente")
    private Ingrediente ingrediente;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("notaResoId: " + id.notaResoId);
        result.append(", descrizione: " + descrizione);
        result.append(", lotto: " + lotto);
        result.append(", scadenza: " + scadenza);
        result.append(", unitaMisura: " + unitaMisura);
        result.append(", quantita: " + quantita);
        result.append(", prezzo: " + prezzo);
        result.append(", sconto: " + sconto);
        result.append(", aliquotaIva: " + aliquotaIva);
        result.append(", imponibile: " + imponibile);
        result.append(", totale: " + totale);
        result.append(", articolo: " + articolo);
        result.append(", ingrediente: " + ingrediente);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
