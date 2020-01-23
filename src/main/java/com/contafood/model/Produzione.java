package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = {"produzioneIngredienti", "produzioneConfezioni"})
@Entity
@Table(name = "produzione")
public class Produzione {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice")
    private Integer codice;

    @Column(name = "data_produzione")
    private Date dataProduzione;

    @ManyToOne
    @JoinColumn(name="id_ricetta")
    private Ricetta ricetta;

    @ManyToOne
    @JoinColumn(name="id_categoria")
    private CategoriaRicetta categoria;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "lotto_anno")
    @JsonIgnoreProperties
    private Integer lottoAnno;

    @Column(name = "lotto_giorno")
    @JsonIgnoreProperties
    private Integer lottoGiorno;

    @Column(name = "lotto_numero_progressivo")
    @JsonIgnoreProperties
    private Integer lottoNumeroProgressivo;

    @Column(name = "scadenza")
    private Date scadenza;

    @Column(name = "quantita_totale")
    private Float quantitaTotale;

    @Column(name = "numero_confezioni")
    private Integer numeroConfezioni;

    @Column(name = "scopo")
    private String scopo;

    @Column(name = "film_chiusura")
    private String filmChiusura;

    @Column(name = "lotto_film_chiusura")
    private String lottoFilmChiusura;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @OneToMany(mappedBy = "produzione")
    @JsonIgnoreProperties("produzione")
    private Set<ProduzioneIngrediente> produzioneIngredienti = new HashSet<>();

    @OneToMany(mappedBy = "produzione")
    @JsonIgnoreProperties("produzione")
    private Set<ProduzioneConfezione> produzioneConfezioni = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodice() {
        return codice;
    }

    public void setCodice(Integer codice) {
        this.codice = codice;
    }

    public Date getDataProduzione() {
        return dataProduzione;
    }

    public void setDataProduzione(Date dataProduzione) {
        this.dataProduzione = dataProduzione;
    }

    public Ricetta getRicetta() {
        return ricetta;
    }

    public void setRicetta(Ricetta ricetta) {
        this.ricetta = ricetta;
    }

    public CategoriaRicetta getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaRicetta categoria) {
        this.categoria = categoria;
    }

    public String getLotto() {
        return lotto;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
    }

    public Integer getLottoAnno() {
        return lottoAnno;
    }

    public void setLottoAnno(Integer lottoAnno) {
        this.lottoAnno = lottoAnno;
    }

    public Integer getLottoGiorno() {
        return lottoGiorno;
    }

    public void setLottoGiorno(Integer lottoGiorno) {
        this.lottoGiorno = lottoGiorno;
    }

    public Integer getLottoNumeroProgressivo() {
        return lottoNumeroProgressivo;
    }

    public void setLottoNumeroProgressivo(Integer lottoNumeroProgressivo) {
        this.lottoNumeroProgressivo = lottoNumeroProgressivo;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public Float getQuantitaTotale() {
        return quantitaTotale;
    }

    public void setQuantitaTotale(Float quantitaTotale) {
        this.quantitaTotale = quantitaTotale;
    }

    public Integer getNumeroConfezioni(){return numeroConfezioni;}

    public void setNumeroConfezioni(Integer numeroConfezioni){
        this.numeroConfezioni = numeroConfezioni;
    }

    public String getScopo(){return scopo;}

    public void setScopo(String scopo){this.scopo = scopo;}

    public Set<ProduzioneIngrediente> getProduzioneIngredienti() {
        return produzioneIngredienti;
    }

    public void setProduzioneIngredienti(Set<ProduzioneIngrediente> produzioneIngredienti) {
        this.produzioneIngredienti = produzioneIngredienti;
    }

    public Set<ProduzioneConfezione> getProduzioneConfezioni() {
        return produzioneConfezioni;
    }

    public void setProduzioneConfezioni(Set<ProduzioneConfezione> produzioneConfezioni) {
        this.produzioneConfezioni = produzioneConfezioni;
    }

    public String getFilmChiusura() {
        return filmChiusura;
    }

    public void setFilmChiusura(String filmChiusura) {
        this.filmChiusura = filmChiusura;
    }

    public String getLottoFilmChiusura() {
        return lottoFilmChiusura;
    }

    public void setLottoFilmChiusura(String lottoFilmChiusura) {
        this.lottoFilmChiusura = lottoFilmChiusura;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public Timestamp getDataAggiornamento() {
        return dataAggiornamento;
    }

    public void setDataAggiornamento(Timestamp dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", codice: " + codice);
        result.append(", dataProduzione: " + dataProduzione);
        result.append(", ricetta: " + ricetta);
        result.append(", categoria: " + categoria);
        result.append(", lotto: " + lotto);
        result.append(", lottoAnno: " + lottoAnno);
        result.append(", lottoGiorno: " + lottoGiorno);
        result.append(", lottoNumeroProgressivo: " + lottoNumeroProgressivo);
        result.append(", scadenza: " + scadenza);
        result.append(", quantitaTotale: " + quantitaTotale);
        result.append(", numeroConfezioni: " + numeroConfezioni);
        result.append(", scopo: " + scopo);
        result.append(", filmChiusura: " + filmChiusura);
        result.append(", lottoFilmChiusura: " + lottoFilmChiusura);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append(", ingredienti: [");
        for(ProduzioneIngrediente produzioneIngrediente: produzioneIngredienti){
            result.append("{");
            result.append(produzioneIngrediente.toString());
            result.append("}");
        }
        result.append("]");
        result.append(", confezioni: [");
        for(ProduzioneConfezione produzioneConfezione: produzioneConfezioni){
            result.append("{");
            result.append(produzioneConfezione.toString());
            result.append("}");
        }
        result.append("]");
        result.append("}");

        return result.toString();
    }
}
