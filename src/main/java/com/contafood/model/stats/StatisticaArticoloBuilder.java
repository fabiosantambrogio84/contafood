package com.contafood.model.stats;

import java.math.BigDecimal;

public class StatisticaArticoloBuilder {

    private String tipologia;

    private Long idArticolo;

    private Integer progressivo;

    private String codice;

    private String descrizione;

    private String lotto;

    private Float quantita;

    private BigDecimal prezzo;

    private BigDecimal totale;

    public StatisticaArticoloBuilder setTipologia(String tipologia) {
        this.tipologia = tipologia;
        return this;
    }

    public StatisticaArticoloBuilder setIdArticolo(Long idArticolo) {
        this.idArticolo = idArticolo;
        return this;
    }

    public StatisticaArticoloBuilder setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
        return this;
    }

    public StatisticaArticoloBuilder setCodice(String codice) {
        this.codice = codice;
        return this;
    }

    public StatisticaArticoloBuilder setDescrizione(String descrizione) {
        this.descrizione = descrizione;
        return this;
    }

    public StatisticaArticoloBuilder setLotto(String lotto) {
        this.lotto = lotto;
        return this;
    }

    public StatisticaArticoloBuilder setQuantita(Float quantita) {
        this.quantita = quantita;
        return this;
    }

    public StatisticaArticoloBuilder setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
        return this;
    }

    public StatisticaArticoloBuilder setTotale(BigDecimal totale) {
        this.totale = totale;
        return this;
    }

    public StatisticaArticolo build(){
        StatisticaArticolo statisticaArticolo = new StatisticaArticolo();
        statisticaArticolo.setTipologia(this.tipologia);
        statisticaArticolo.setIdArticolo(this.idArticolo);
        statisticaArticolo.setProgressivo(this.progressivo);
        statisticaArticolo.setCodice(this.codice);
        statisticaArticolo.setDescrizione(this.descrizione);
        statisticaArticolo.setLotto(this.lotto);
        statisticaArticolo.setQuantita(this.quantita);
        statisticaArticolo.setPrezzo(this.prezzo);
        statisticaArticolo.setTotale(this.totale);

        return statisticaArticolo;
    }
}
