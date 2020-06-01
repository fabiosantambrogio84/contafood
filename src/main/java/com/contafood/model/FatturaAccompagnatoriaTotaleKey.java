package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FatturaAccompagnatoriaTotaleKey implements Serializable {

    private static final long serialVersionUID = 6071270490197582469L;

    @Column(name = "id_fattura_accom")
    Long fatturaAccompagnatoriaId;

    @Column(name = "id_aliquota_iva")
    Long aliquotaIvaId;

    @Column(name = "uuid")
    String uuid;

    public FatturaAccompagnatoriaTotaleKey(){}

    public FatturaAccompagnatoriaTotaleKey(Long fatturaAccompagnatoriaId, Long aliquotaIvaId, String uuid){
        this.fatturaAccompagnatoriaId = fatturaAccompagnatoriaId;
        this.aliquotaIvaId = aliquotaIvaId;
        this.uuid = uuid;
    }

    public Long getFatturaAccompagnatoriaId() {
        return fatturaAccompagnatoriaId;
    }

    public void setFatturaAccompagnatoriaId(Long fatturaAccompagnatoriaId) {
        this.fatturaAccompagnatoriaId = fatturaAccompagnatoriaId;
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
        return Objects.hash(fatturaAccompagnatoriaId, aliquotaIvaId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FatturaAccompagnatoriaTotaleKey that = (FatturaAccompagnatoriaTotaleKey) obj;
        return Objects.equals(fatturaAccompagnatoriaId, that.fatturaAccompagnatoriaId) &&
                Objects.equals(aliquotaIvaId, that.aliquotaIvaId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("fatturaAccompagnatoriaId: " + fatturaAccompagnatoriaId);
        result.append(", aliquotaIvaId: " + aliquotaIvaId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
