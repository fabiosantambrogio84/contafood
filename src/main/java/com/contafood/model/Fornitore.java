package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(exclude = {"ingredienti", "listiniAssociati", "articoli", "sconti"})
@Entity
@Table(name = "fornitore")
public class Fornitore {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice")
    private Integer codice;

    @Column(name = "ragione_sociale")
    private String ragioneSociale;

    @Column(name = "ragione_sociale_2")
    private String ragioneSociale2;

    @Column(name = "indirizzo")
    private String indirizzo;

    @Column(name = "citta")
    private String citta;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "cap")
    private String cap;

    @Column(name = "nazione")
    private String nazione;

    @Column(name = "partita_iva")
    private String partitaIva;

    @Column(name = "codice_fiscale")
    private String codiceFiscale;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "telefono_2")
    private String telefono2;

    @Column(name = "telefono_3")
    private String telefono3;

    @Column(name = "email")
    private String email;

    @Column(name = "email_pec")
    private String emailPec;

    @Column(name = "codice_univoco_sdi")
    private String codiceUnivocoSdi;

    @Column(name = "iban")
    private String iban;

    @Column(name = "pagamento")
    private String pagamento;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "fornitore")
    @JsonIgnore
    private List<Ingrediente> ingredienti;

    @OneToMany(mappedBy = "fornitore")
    @JsonIgnore
    private List<ListinoAssociato> listiniAssociati;

    @OneToMany(mappedBy = "fornitore")
    @JsonIgnore
    private List<Articolo> articoli;

    @OneToMany(mappedBy = "fornitore")
    @JsonIgnore
    private List<Sconto> sconti;

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

    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
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

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getTelefono3() {
        return telefono3;
    }

    public void setTelefono3(String telefono3) {
        this.telefono3 = telefono3;
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

    public String getCodiceUnivocoSdi() {
        return codiceUnivocoSdi;
    }

    public void setCodiceUnivocoSdi(String codiceUnivocoSdi) {
        this.codiceUnivocoSdi = codiceUnivocoSdi;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Ingrediente> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(List<Ingrediente> ingredienti) {
        this.ingredienti = ingredienti;
    }

    public List<ListinoAssociato> getListiniAssociati() {
        return listiniAssociati;
    }

    public void setListiniAssociati(List<ListinoAssociato> listiniAssociati) {
        this.listiniAssociati = listiniAssociati;
    }

    public List<Articolo> getArticoli() {
        return articoli;
    }

    public void setArticoli(List<Articolo> articoli) {
        this.articoli = articoli;
    }

    public List<Sconto> getSconti() {
        return sconti;
    }

    public void setSconti(List<Sconto> sconti) {
        this.sconti = sconti;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", codice: " + codice);
        result.append(", ragioneSociale: " + ragioneSociale);
        result.append(", ragioneSociale2: " + ragioneSociale2);
        result.append(", indirizzo: " + indirizzo);
        result.append(", citta: " + citta);
        result.append(", provincia: " + provincia);
        result.append(", cap: " + cap);
        result.append(", nazione: " + nazione);
        result.append(", partitaIva: " + partitaIva);
        result.append(", codiceFiscale: " + codiceFiscale);
        result.append(", telefono: " + telefono);
        result.append(", telefono2: " + telefono2);
        result.append(", telefono3: " + telefono3);
        result.append(", email: " + email);
        result.append(", emailPec: " + emailPec);
        result.append(", codiceUnivocoSdi: " + codiceUnivocoSdi);
        result.append(", iban: " + iban);
        result.append(", pagamento: " + pagamento);
        result.append(", note: " + note);
        result.append("}");

        return result.toString();

    }
}
