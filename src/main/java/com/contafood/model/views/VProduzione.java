package com.contafood.model.views;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@EqualsAndHashCode()
@Entity
@Table(name = "v_produzione")
public class VProduzione {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id_produzione")
    private Long idProduzione;

    @Column(name = "codice_produzione")
    private String codiceProduzione;

    @Column(name = "data_produzione")
    private Date dataProduzione;

    @Column(name = "id_confezione")
    private Long idConfezione;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "scadenza")
    private Date scadenza;

    @Column(name = "id_articolo")
    private Long idArticolo;

    @Column(name = "codice_articolo")
    private String codiceArticolo;

    @Column(name = "descrizione_articolo")
    private String descrizioneArticolo;

    @Column(name = "num_confezioni_prodotte")
    private Integer numConfezioniProdotte;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdProduzione() {
        return idProduzione;
    }

    public void setIdProduzione(Long idProduzione) {
        this.idProduzione = idProduzione;
    }

    public String getCodiceProduzione() {
        return codiceProduzione;
    }

    public void setCodiceProduzione(String codiceProduzione) {
        this.codiceProduzione = codiceProduzione;
    }

    public Date getDataProduzione() {
        return dataProduzione;
    }

    public void setDataProduzione(Date dataProduzione) {
        this.dataProduzione = dataProduzione;
    }

    public Long getIdConfezione() {
        return idConfezione;
    }

    public void setIdConfezione(Long idConfezione) {
        this.idConfezione = idConfezione;
    }

    public String getLotto() {
        return lotto;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public Long getIdArticolo() {
        return idArticolo;
    }

    public void setIdArticolo(Long idArticolo) {
        this.idArticolo = idArticolo;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public String getDescrizioneArticolo() {
        return descrizioneArticolo;
    }

    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.descrizioneArticolo = descrizioneArticolo;
    }

    public Integer getNumConfezioniProdotte() {
        return numConfezioniProdotte;
    }

    public void setNumConfezioniProdotte(Integer numConfezioniProdotte) {
        this.numConfezioniProdotte = numConfezioniProdotte;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", idProduzione: " + idProduzione);
        result.append(", codiceProduzione: " + codiceProduzione);
        result.append(", dataProduzione: " + dataProduzione);
        result.append(", idConfezione: " + idConfezione);
        result.append(", lotto: " + lotto);
        result.append(", scadenza: " + scadenza);
        result.append(", idArticolo: " + idArticolo);
        result.append(", codiceArticolo: " + codiceArticolo);
        result.append(", descrizioneArticolo: " + descrizioneArticolo);
        result.append(", numConfezioniProdotte: " + numConfezioniProdotte);
        result.append("}");

        return result.toString();
    }
}
