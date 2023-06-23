package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FatturaAccompagnatoriaAcquistoIngredienteKey implements Serializable {

    private static final long serialVersionUID = -4977796283879498897L;

    @Column(name = "id_fattura_accom_acquisto")
    Long fatturaAccompagnatoriaAcquistoId;

    @Column(name = "id_ingrediente")
    Long ingredienteId;

    @Column(name = "uuid")
    String uuid;

    public FatturaAccompagnatoriaAcquistoIngredienteKey(){}

    public FatturaAccompagnatoriaAcquistoIngredienteKey(Long fatturaAccompagnatoriaAcquistoId, Long ingredienteId, String uuid){
        this.fatturaAccompagnatoriaAcquistoId = fatturaAccompagnatoriaAcquistoId;
        this.ingredienteId = ingredienteId;
        this.uuid = uuid;
    }

    public Long getFatturaAccompagnatoriaAcquistoId() {
        return fatturaAccompagnatoriaAcquistoId;
    }

    public void setFatturaAccompagnatoriaAcquistoId(Long fatturaAccompagnatoriaAcquistoId) {
        this.fatturaAccompagnatoriaAcquistoId = fatturaAccompagnatoriaAcquistoId;
    }

    public Long getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Long ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fatturaAccompagnatoriaAcquistoId, ingredienteId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FatturaAccompagnatoriaAcquistoIngredienteKey that = (FatturaAccompagnatoriaAcquistoIngredienteKey) obj;
        return Objects.equals(fatturaAccompagnatoriaAcquistoId, that.fatturaAccompagnatoriaAcquistoId) &&
                Objects.equals(ingredienteId, that.ingredienteId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {

        return "{" +
                "fatturaAccompagnatoriaAcquistoId: " + fatturaAccompagnatoriaAcquistoId +
                ", ingredienteId: " + ingredienteId +
                ", uuid: " + uuid +
                "}";
    }
}
