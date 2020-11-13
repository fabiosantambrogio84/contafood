package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RicevutaPrivatoArticoloOrdineClienteKey implements Serializable {

    private static final long serialVersionUID = 6856952350846645312L;

    @Column(name = "id_ricevuta_privato")
    Long ricevutaPrivatoId;

    @Column(name = "id_articolo")
    Long articoloId;

    @Column(name = "uuid")
    String uuid;

    @Column(name = "id_ordine_cliente")
    Long ordineClienteId;

    public RicevutaPrivatoArticoloOrdineClienteKey(){}

    public RicevutaPrivatoArticoloOrdineClienteKey(Long ricevutaPrivatoId, Long articoloId, String uuid, Long ordineClienteId){
        this.ricevutaPrivatoId = ricevutaPrivatoId;
        this.articoloId = articoloId;
        this.uuid = uuid;
        this.ordineClienteId = ordineClienteId;
    }

    public Long getRicevutaPrivatoId() {
        return ricevutaPrivatoId;
    }

    public void setRicevutaPrivatoId(Long ricevutaPrivatoId) {
        this.ricevutaPrivatoId = ricevutaPrivatoId;
    }

    public Long getArticoloId() {
        return articoloId;
    }

    public void setArticoloId(Long articoloId) {
        this.articoloId = articoloId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getOrdineClienteId() {
        return ordineClienteId;
    }

    public void setOrdineClienteId(Long ordineClienteId) {
        this.ordineClienteId = ordineClienteId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ricevutaPrivatoId, articoloId, uuid, ordineClienteId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RicevutaPrivatoArticoloOrdineClienteKey that = (RicevutaPrivatoArticoloOrdineClienteKey) obj;
        return Objects.equals(ricevutaPrivatoId, that.ricevutaPrivatoId) &&
                Objects.equals(articoloId, that.articoloId) && Objects.equals(uuid, that.uuid) && Objects.equals(ordineClienteId, that.ordineClienteId);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ricevutaPrivatoId: " + ricevutaPrivatoId);
        result.append(", articoloId: " + articoloId);
        result.append(", uuid: " + uuid);
        result.append(", ordineClienteId: " + ordineClienteId);
        result.append("}");

        return result.toString();
    }
}
