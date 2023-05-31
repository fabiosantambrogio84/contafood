package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FatturaAccompagnatoriaAcquistoTotaleKey implements Serializable {

    private static final long serialVersionUID = 6914999914555003959L;

    @Column(name = "id_fattura_accom_acquisto")
    Long fatturaAccompagnatoriaAcquistoId;

    @Column(name = "id_aliquota_iva")
    Long aliquotaIvaId;

    @Column(name = "uuid")
    String uuid;

    public FatturaAccompagnatoriaAcquistoTotaleKey(){}

    public FatturaAccompagnatoriaAcquistoTotaleKey(Long fatturaAccompagnatoriaAcquistoId, Long aliquotaIvaId, String uuid){
        this.fatturaAccompagnatoriaAcquistoId = fatturaAccompagnatoriaAcquistoId;
        this.aliquotaIvaId = aliquotaIvaId;
        this.uuid = uuid;
    }

    public Long getFatturaAccompagnatoriaAcquistoId() {
        return fatturaAccompagnatoriaAcquistoId;
    }

    public void setFatturaAccompagnatoriaAcquistoId(Long fatturaAccompagnatoriaAcquistoId) {
        this.fatturaAccompagnatoriaAcquistoId = fatturaAccompagnatoriaAcquistoId;
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
        return Objects.hash(fatturaAccompagnatoriaAcquistoId, aliquotaIvaId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FatturaAccompagnatoriaAcquistoTotaleKey that = (FatturaAccompagnatoriaAcquistoTotaleKey) obj;
        return Objects.equals(fatturaAccompagnatoriaAcquistoId, that.fatturaAccompagnatoriaAcquistoId) &&
                Objects.equals(aliquotaIvaId, that.aliquotaIvaId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {

        return "{" +
                "fatturaAccompagnatoriaAcquistoId: " + fatturaAccompagnatoriaAcquistoId +
                ", aliquotaIvaId: " + aliquotaIvaId +
                ", uuid: " + uuid +
                "}";
    }
}