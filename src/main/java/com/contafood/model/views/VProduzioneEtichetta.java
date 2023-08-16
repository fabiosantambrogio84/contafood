package com.contafood.model.views;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Data
@EqualsAndHashCode()
@Entity
@Table(name = "v_produzione_etichetta")
public class VProduzioneEtichetta {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "scadenza")
    private Date scadenza;

    @Column(name = "articolo")
    private String articolo;

    @Column(name = "ingredienti")
    private String ingredienti;

    @Override
    public String toString() {

        return "{" +
                "id: " + id +
                ", lotto: " + lotto +
                ", scadenza: " + scadenza +
                ", articolo: " + articolo +
                ", ingredienti: " + ingredienti +
                "}";
    }
}