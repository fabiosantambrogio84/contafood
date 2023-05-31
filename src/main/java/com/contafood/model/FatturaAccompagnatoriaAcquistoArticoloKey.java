package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FatturaAccompagnatoriaAcquistoArticoloKey implements Serializable {

    private static final long serialVersionUID = 8482142251423021938L;

    @Column(name = "id_fattura_accom_acquisto")
    Long fatturaAccompagnatoriaAcquistoId;

    @Column(name = "id_articolo")
    Long articoloId;

    @Column(name = "uuid")
    String uuid;

    public FatturaAccompagnatoriaAcquistoArticoloKey(){}

    public FatturaAccompagnatoriaAcquistoArticoloKey(Long fatturaAccompagnatoriaAcquistoId, Long articoloId, String uuid){
        this.fatturaAccompagnatoriaAcquistoId = fatturaAccompagnatoriaAcquistoId;
        this.articoloId = articoloId;
        this.uuid = uuid;
    }

    public Long getFatturaAccompagnatoriaAcquistoId() {
        return fatturaAccompagnatoriaAcquistoId;
    }

    public void setFatturaAccompagnatoriaAcquistoId(Long fatturaAccompagnatoriaAcquistoId) {
        this.fatturaAccompagnatoriaAcquistoId = fatturaAccompagnatoriaAcquistoId;
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
        return Objects.hash(fatturaAccompagnatoriaAcquistoId, articoloId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FatturaAccompagnatoriaAcquistoArticoloKey that = (FatturaAccompagnatoriaAcquistoArticoloKey) obj;
        return Objects.equals(fatturaAccompagnatoriaAcquistoId, that.fatturaAccompagnatoriaAcquistoId) &&
                Objects.equals(articoloId, that.articoloId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {

        return "{" +
                "fatturaAccompagnatoriaAcquistoId: " + fatturaAccompagnatoriaAcquistoId +
                ", articoloId: " + articoloId +
                ", uuid: " + uuid +
                "}";
    }
}
