package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DdtAcquistoIngredienteKey implements Serializable {

    private static final long serialVersionUID = 4595152200365587584L;

    @Column(name = "id_ddt_acquisto")
    Long ddtAcquistoId;

    @Column(name = "id_ingrediente")
    Long ingredienteId;

    @Column(name = "uuid")
    String uuid;

    public DdtAcquistoIngredienteKey(){}

    public DdtAcquistoIngredienteKey(Long ddtAcquistoId, Long ingredienteId, String uuid){
        this.ddtAcquistoId = ddtAcquistoId;
        this.ingredienteId = ingredienteId;
        this.uuid = uuid;
    }

    public Long getDdtAcquistoId() {
        return ddtAcquistoId;
    }

    public void setDdtAcquistoId(Long ddtAcquistoId) {
        this.ddtAcquistoId = ddtAcquistoId;
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
        return Objects.hash(ddtAcquistoId, ingredienteId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DdtAcquistoIngredienteKey that = (DdtAcquistoIngredienteKey) obj;
        return Objects.equals(ddtAcquistoId, that.ddtAcquistoId) &&
                Objects.equals(ingredienteId, that.ingredienteId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ddtAcquistoId: " + ddtAcquistoId);
        result.append(", ingredienteId: " + ingredienteId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
