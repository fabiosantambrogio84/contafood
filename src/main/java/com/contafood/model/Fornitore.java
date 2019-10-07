package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(exclude = {"ingredienti"})
@Entity
@Table(name = "fornitore")
public class Fornitore {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice")
    private String codice;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
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

    /*
    @Override
    public int hashCode() {
        return Objects.hash(id, codice, ragioneSociale, ragioneSociale2, dittaIndividuale, nome, cognome, indirizzo, citta, provincia, cap, nazione, partitaIva, codiceFiscale, telefono, telefono2, telefono3, email, emailPec, codiceUnivocoSdi, iban, pagamento, note);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Fornitore that = (Fornitore) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(codice, that.codice) &&
                Objects.equals(ragioneSociale, that.ragioneSociale) &&
                Objects.equals(ragioneSociale2, that.ragioneSociale2) &&
                Objects.equals(dittaIndividuale, that.dittaIndividuale) &&
                Objects.equals(nome, that.nome) &&
                Objects.equals(cognome, that.cognome) &&
                Objects.equals(indirizzo, that.indirizzo) &&
                Objects.equals(citta, that.citta) &&
                Objects.equals(provincia, that.provincia) &&
                Objects.equals(cap, that.cap) &&
                Objects.equals(nazione, that.nazione) &&
                Objects.equals(partitaIva, that.partitaIva) &&
                Objects.equals(codiceFiscale, that.codiceFiscale) &&
                Objects.equals(telefono, that.telefono) &&
                Objects.equals(telefono2, that.telefono2) &&
                Objects.equals(telefono3, that.telefono3) &&
                Objects.equals(email, that.email) &&
                Objects.equals(emailPec, that.emailPec) &&
                Objects.equals(codiceUnivocoSdi, that.codiceUnivocoSdi) &&
                Objects.equals(iban, that.iban) &&
                Objects.equals(pagamento, that.pagamento) &&
                Objects.equals(note, that.note);
    }
    */

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
