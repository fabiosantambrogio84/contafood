package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
@EqualsAndHashCode(exclude = {"puntiConsegna", "listiniAssociati", "sconti", "telefonate", "clienteArticoli"})
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice")
    private Integer codice;

    @Column(name = "privato")
    private Boolean privato;

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

    @ManyToOne
    @JoinColumn(name="id_listino")
    private Listino listino;

    @Column(name = "estrazione_conad")
    private String estrazioneConad;

    @Column(name = "modalita_invio_fatture")
    private String modalitaInvioFatture;

    @Column(name = "blocca_ddt")
    private Boolean bloccaDdt;

    @Column(name = "nascondi_prezzi")
    private Boolean nascondiPrezzi;

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

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<ListinoAssociato> listiniAssociati;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Sconto> sconti;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Telefonata> telefonate;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnoreProperties("cliente")
    private Set<ClienteArticolo> clienteArticoli = new HashSet<>();

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

    public Boolean getPrivato() {
        return privato;
    }

    public void setPrivato(Boolean privato) {
        this.privato = privato;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getRagioneSociale2() {
        return ragioneSociale2;
    }

    public void setRagioneSociale2(String ragioneSociale2) {
        this.ragioneSociale2 = ragioneSociale2;
    }

    public Boolean getDittaIndividuale() {
        return dittaIndividuale;
    }

    public void setDittaIndividuale(Boolean dittaIndividuale) {
        this.dittaIndividuale = dittaIndividuale;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPec() {
        return emailPec;
    }

    public void setEmailPec(String emailPec) {
        this.emailPec = emailPec;
    }

    public Banca getBanca() {
        return banca;
    }

    public void setBanca(Banca banca) {
        this.banca = banca;
    }

    public String getContoCorrente() {
        return contoCorrente;
    }

    public void setContoCorrente(String contoCorrente) {
        this.contoCorrente = contoCorrente;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Agente getAgente() {
        return agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public Listino getListino() {
        return listino;
    }

    public void setListino(Listino listino) {
        this.listino = listino;
    }

    public String getEstrazioneConad() {
        return estrazioneConad;
    }

    public void setEstrazioneConad(String estrazioneConad) {
        this.estrazioneConad = estrazioneConad;
    }

    public String getModalitaInvioFatture() {
        return modalitaInvioFatture;
    }

    public void setModalitaInvioFatture(String modalitaInvioFatture) {
        this.modalitaInvioFatture = modalitaInvioFatture;
    }

    public Boolean getBloccaDdt() {
        return bloccaDdt;
    }

    public void setBloccaDdt(Boolean bloccaDdt) {
        this.bloccaDdt = bloccaDdt;
    }

    public Boolean getNascondiPrezzi() {
        return nascondiPrezzi;
    }

    public void setNascondiPrezzi(Boolean nascondiPrezzi) {
        this.nascondiPrezzi = nascondiPrezzi;
    }

    public Boolean getRaggruppaRiba() {
        return raggruppaRiba;
    }

    public void setRaggruppaRiba(Boolean raggruppaRiba) {
        this.raggruppaRiba = raggruppaRiba;
    }

    public String getNomeGruppoRiba() {
        return nomeGruppoRiba;
    }

    public void setNomeGruppoRiba(String nomeGruppoRiba) {
        this.nomeGruppoRiba = nomeGruppoRiba;
    }

    public String getCodiceUnivocoSdi() {
        return codiceUnivocoSdi;
    }

    public void setCodiceUnivocoSdi(String codiceUnivocoSdi) {
        this.codiceUnivocoSdi = codiceUnivocoSdi;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public List<PuntoConsegna> getPuntiConsegna() {
        return puntiConsegna;
    }

    public void setPuntiConsegna(List<PuntoConsegna> puntiConsegna) {
        this.puntiConsegna = puntiConsegna;
    }

    public List<ListinoAssociato> getListiniAssociati() {
        return listiniAssociati;
    }

    public void setListiniAssociati(List<ListinoAssociato> listiniAssociati) {
        this.listiniAssociati = listiniAssociati;
    }

    public List<Sconto> getSconti() {
        return sconti;
    }

    public void setSconti(List<Sconto> sconti) {
        this.sconti = sconti;
    }

    public List<Telefonata> getTelefonate() {
        return telefonate;
    }

    public void setTelefonate(List<Telefonata> telefonate) {
        this.telefonate = telefonate;
    }

    public Set<ClienteArticolo> getClienteArticoli() {
        return clienteArticoli;
    }

    public void setClienteArticoli(Set<ClienteArticolo> clienteArticoli) {
        this.clienteArticoli = clienteArticoli;
    }

    @JsonIgnore
    public String getFieldComparing(){
        if(getDittaIndividuale()){
            return getCognome() + " " + getNome();
        } else {
            return getRagioneSociale();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", codice: " + codice);
        result.append(", privato: " + privato);
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
        result.append(", estrazioneConad: " + estrazioneConad);
        result.append(", modalitaInvioFatture: " + modalitaInvioFatture);
        result.append(", bloccaDdt: " + bloccaDdt);
        result.append(", nascondiPrezzi: " + nascondiPrezzi);
        result.append(", raggruppaRiba: " + raggruppaRiba);
        result.append(", nomeGruppoRiba: " + nomeGruppoRiba);
        result.append(", codiceUnivocoSdi: " + codiceUnivocoSdi);
        result.append(", note: " + note);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
