package com.contafood.model.views;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode()
@Entity
@Table(name = "v_giacenza_articolo")
public class VGiacenzaArticolo {

    @Id
    @Column(name = "id_articolo")
    private Long idArticolo;

    @Column(name = "articolo")
    private String articolo;

    @Column(name = "prezzo_acquisto")
    private BigDecimal prezzoAcquisto;

    @Column(name = "prezzo_listino_base")
    private BigDecimal prezzoListinoBase;

    @Column(name = "quantita_tot")
    private Float quantita;

    @Column(name = "quantita_kg")
    private Float quantitaKg;

    @Column(name = "id_giacenze")
    private String idGiacenze;

    @Column(name = "lotto_giacenze")
    private String lottoGiacenze;

    @Column(name = "scadenza_giacenze")
    private String scadenzaGiacenze;

    @Column(name = "attivo")
    private Boolean attivo;

    @Column(name = "id_fornitore")
    private Long idFornitore;

    @Column(name = "fornitore")
    private String fornitore;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("idArticolo: " + idArticolo);
        result.append(", articolo: " + articolo);
        result.append(", prezzoAcquisto: " + prezzoAcquisto);
        result.append(", prezzoListinoBase: " + prezzoListinoBase);
        result.append(", quantita: " + quantita);
        result.append(", quantitaKg: " + quantitaKg);
        result.append(", idGiacenze: " + idGiacenze);
        result.append(", lottoGiacenze: " + lottoGiacenze);
        result.append(", scadenzaGiacenze: " + scadenzaGiacenze);
        result.append(", attivo: " + attivo);
        result.append(", idFornitore: " + idFornitore);
        result.append(", fornitore: " + fornitore);
        result.append("}");

        return result.toString();
    }
}
