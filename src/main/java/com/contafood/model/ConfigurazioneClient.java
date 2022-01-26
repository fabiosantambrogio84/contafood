package com.contafood.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "configurazione_client")
public class ConfigurazioneClient {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice")
    private String codice;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "abilitato")
    private Boolean abilitato;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", codice: " + codice);
        result.append(", descrizione: " + descrizione);
        result.append(", abilitato: " + abilitato);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();

    }
}
