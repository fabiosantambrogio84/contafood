package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RicevutaPrivatoArticoloKey implements Serializable {

    private static final long serialVersionUID = 9194839976554964707L;

    @Column(name = "id_ricevuta_privato")
    Long ricevutaPrivatoId;

    @Column(name = "id_articolo")
    Long articoloId;

    @Column(name = "uuid")
    String uuid;

    public RicevutaPrivatoArticoloKey(){}

    public RicevutaPrivatoArticoloKey(Long ricevutaPrivatoId, Long articoloId, String uuid){
        this.ricevutaPrivatoId = ricevutaPrivatoId;
        this.articoloId = articoloId;
        this.uuid = uuid;
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

    @Override
    public int hashCode() {
        return Objects.hash(ricevutaPrivatoId, articoloId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RicevutaPrivatoArticoloKey that = (RicevutaPrivatoArticoloKey) obj;
        return Objects.equals(ricevutaPrivatoId, that.ricevutaPrivatoId) &&
                Objects.equals(articoloId, that.articoloId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ricevutaPrivatoId: " + ricevutaPrivatoId);
        result.append(", articoloId: " + articoloId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
