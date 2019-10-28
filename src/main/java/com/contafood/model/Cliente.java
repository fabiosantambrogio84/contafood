package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(exclude = {"puntiConsegna"})
@Getter
@Setter
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice")
    private Integer codice;

    @Column(name = "ragione_sociale")
    private String ragioneSociale;

    @Column(name = "ragione_sociale_2")
    private String ragioneSociale2;

    @Column(name = "ditta_individuale")
    private Boolean dittaIndividuale;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Column(name = "indirizzo")
    private String indirizzo;

    @Column(name = "citta")
    private String citta;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "cap")
    private String cap;

    @Column(name = "partita_iva")
    private String partitaIva;

    @Column(name = "codice_fiscale")
    private String codiceFiscale;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "email_pec")
    private String emailPec;

    @ManyToOne
    @JoinColumn(name="id_banca")
    private Banca banca;

    @Column(name = "conto_corrente")
    private String contoCorrente;

    @ManyToOne
    @JoinColumn(name="id_tipo_pagamento")
    private TipoPagamento tipoPagamento;

    @ManyToOne
    @JoinColumn(name="id_agente")
    private Agente agente;

    @Column(name = "blocca_ddt")
    private Boolean bloccaDdt;

    @Column(name = "nascondi_prezzi")
    private Boolean nascondiPrezzi;

    @Column(name = "estrazione_conad")
    private Boolean estrazioneConad;

    @Column(name = "raggruppa_riba")
    private Boolean raggruppaRiba;

    @Column(name = "nome_gruppo_riba")
    private String nomeGruppoRiba;

    @Column(name = "codice_univoco_sdi")
    private String codiceUnivocoSdi;

    @Column(name = "note")
    private String note;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<PuntoConsegna> puntiConsegna;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", codice: " + codice);
        result.append(", ragioneSociale: " + ragioneSociale);
        result.append(", ragioneSociale2: " + ragioneSociale2);
        result.append(", dittaIndividuale: " + dittaIndividuale);
        result.append(", nome: " + nome);
        result.append(", cognome: " + cognome);
        result.append(", indirizzo: " + indirizzo);
        result.append(", citta: " + citta);
        result.append(", provincia: " + provincia);
        result.append(", cap: " + cap);
        result.append(", partitaIva: " + partitaIva);
        result.append(", codiceFiscale: " + codiceFiscale);
        result.append(", telefono: " + telefono);
        result.append(", email: " + email);
        result.append(", emailPec: " + emailPec);
        result.append(", banca: " + banca);
        result.append(", contoCorrente: " + contoCorrente);
        result.append(", tipoPagamento: " + tipoPagamento);
        result.append(", agente: " + agente);
        result.append(", bloccaDdt: " + bloccaDdt);
        result.append(", nascondiPrezzi: " + nascondiPrezzi);
        result.append(", estrazioneConad: " + estrazioneConad);
        result.append(", raggruppaRiba: " + raggruppaRiba);
        result.append(", nomeGruppoRiba: " + nomeGruppoRiba);
        result.append(", codiceUnivocoSdi: " + codiceUnivocoSdi);
        result.append(", note: " + note);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();

    }
}
