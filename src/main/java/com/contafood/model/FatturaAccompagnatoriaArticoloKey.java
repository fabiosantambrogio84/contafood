package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FatturaAccompagnatoriaArticoloKey implements Serializable {

    @Column(name = "id_fattura_accom")
    Long fatturaAccompagnatoriaId;

    @Column(name = "id_articolo")
    Long articoloId;

    @Column(name = "uuid")
    String uuid;

    public FatturaAccompagnatoriaArticoloKey(){}

    public FatturaAccompagnatoriaArticoloKey(Long fatturaAccompagnatoriaId, Long articoloId, String uuid){
        this.fatturaAccompagnatoriaId = fatturaAccompagnatoriaId;
        this.articoloId = articoloId;
        this.uuid = uuid;
    }

    public Long getFatturaAccompagnatoriaId() {
        return fatturaAccompagnatoriaId;
    }

    public void setFatturaAccompagnatoriaId(Long fatturaAccompagnatoriaId) {
        this.fatturaAccompagnatoriaId = fatturaAccompagnatoriaId;
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
        return Objects.hash(fatturaAccompagnatoriaId, articoloId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FatturaAccompagnatoriaArticoloKey that = (FatturaAccompagnatoriaArticoloKey) obj;
        return Objects.equals(fatturaAccompagnatoriaId, that.fatturaAccompagnatoriaId) &&
                Objects.equals(articoloId, that.articoloId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("fatturaAccompagnatoriaId: " + fatturaAccompagnatoriaId);
        result.append(", articoloId: " + articoloId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
