package com.contafood.model.views;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode()
@Entity
@Table(name = "v_giacenza_articolo")
public class VGiacenzaArticolo {

    @Id
    @Column(name = "id_articolo")
    private Long idArticolo;

    @Column(name = "articolo")
    private String articolo;

    @Column(name = "quantita_tot")
    private Float quantita;

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

    public Long getIdArticolo() {
        return idArticolo;
    }

    public void setIdArticolo(Long idArticolo) {
        this.idArticolo = idArticolo;
    }

    public String getArticolo() {
        return articolo;
    }

    public void setArticolo(String articolo) {
        this.articolo = articolo;
    }

    public Float getQuantita() {
        return quantita;
    }

    public void setQuantita(Float quantita) {
        this.quantita = quantita;
    }

    public String getIdGiacenze() {
        return idGiacenze;
    }

    public void setIdGiacenze(String idGiacenze) {
        this.idGiacenze = idGiacenze;
    }

    public String getLottoGiacenze() {
        return lottoGiacenze;
    }

    public void setLottoGiacenze(String lottoGiacenze) {
        this.lottoGiacenze = lottoGiacenze;
    }

    public String getScadenzaGiacenze() {
        return scadenzaGiacenze;
    }

    public void setScadenzaGiacenze(String scadenzaGiacenze) {
        this.scadenzaGiacenze = scadenzaGiacenze;
    }

    public Boolean getAttivo() {
        return attivo;
    }

    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
    }

    public Long getIdFornitore() {
        return idFornitore;
    }

    public void setIdFornitore(Long idFornitore) {
        this.idFornitore = idFornitore;
    }

    public String getFornitore() {
        return fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("idArticolo: " + idArticolo);
        result.append(", articolo: " + articolo);
        result.append(", quantita: " + quantita);
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
