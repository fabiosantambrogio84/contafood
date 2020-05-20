package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NotaResoTotaleKey implements Serializable {

    @Column(name = "id_nota_reso")
    Long notaResoId;

    @Column(name = "id_aliquota_iva")
    Long aliquotaIvaId;

    @Column(name = "uuid")
    String uuid;

    public NotaResoTotaleKey(){}

    public NotaResoTotaleKey(Long notaResoId, Long aliquotaIvaId, String uuid){
        this.notaResoId = notaResoId;
        this.aliquotaIvaId = aliquotaIvaId;
        this.uuid = uuid;
    }

    public Long getNotaResoId() {
        return notaResoId;
    }

    public void setNotaResoId(Long notaResoId) {
        this.notaResoId = notaResoId;
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
        return Objects.hash(notaResoId, aliquotaIvaId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NotaResoTotaleKey that = (NotaResoTotaleKey) obj;
        return Objects.equals(notaResoId, that.notaResoId) &&
                Objects.equals(aliquotaIvaId, that.aliquotaIvaId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("notaResoId: " + notaResoId);
        result.append(", aliquotaIvaId: " + aliquotaIvaId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
