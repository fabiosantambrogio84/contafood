package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RicevutaPrivatoTotaleKey implements Serializable {

    private static final long serialVersionUID = -1628909741302320981L;

    @Column(name = "id_ricevuta_privato")
    Long ricevutaPrivatoId;

    @Column(name = "id_aliquota_iva")
    Long aliquotaIvaId;

    @Column(name = "uuid")
    String uuid;

    public RicevutaPrivatoTotaleKey(){}

    public RicevutaPrivatoTotaleKey(Long ricevutaPrivatoId, Long aliquotaIvaId, String uuid){
        this.ricevutaPrivatoId = ricevutaPrivatoId;
        this.aliquotaIvaId = aliquotaIvaId;
        this.uuid = uuid;
    }

    public Long getRicevutaPrivatoId() {
        return ricevutaPrivatoId;
    }

    public void setRicevutaPrivatoId(Long ricevutaPrivatoId) {
        this.ricevutaPrivatoId = ricevutaPrivatoId;
    }

    public Long getAliquotaIvaId() {
        return aliquotaIvaId;
    }

    public void setAliquotaIvaId(Long aliquotaIvaId) {
        this.aliquotaIvaId = aliquotaIvaId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ricevutaPrivatoId, aliquotaIvaId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RicevutaPrivatoTotaleKey that = (RicevutaPrivatoTotaleKey) obj;
        return Objects.equals(ricevutaPrivatoId, that.ricevutaPrivatoId) &&
                Objects.equals(aliquotaIvaId, that.aliquotaIvaId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ricevutaPrivatoId: " + ricevutaPrivatoId);
        result.append(", aliquotaIvaId: " + aliquotaIvaId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
