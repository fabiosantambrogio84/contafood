package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NotaAccreditoTotaleKey implements Serializable {

    private static final long serialVersionUID = -1024958553175947038L;

    @Column(name = "id_nota_accredito")
    Long notaAccreditoId;

    @Column(name = "id_aliquota_iva")
    Long aliquotaIvaId;

    @Column(name = "uuid")
    String uuid;

    public NotaAccreditoTotaleKey(){}

    public NotaAccreditoTotaleKey(Long notaAccreditoId, Long aliquotaIvaId, String uuid){
        this.notaAccreditoId = notaAccreditoId;
        this.aliquotaIvaId = aliquotaIvaId;
        this.uuid = uuid;
    }

    public Long getNotaAccreditoId() {
        return notaAccreditoId;
    }

    public void setNotaAccreditoId(Long notaAccreditoId) {
        this.notaAccreditoId = notaAccreditoId;
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
        return Objects.hash(notaAccreditoId, aliquotaIvaId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NotaAccreditoTotaleKey that = (NotaAccreditoTotaleKey) obj;
        return Objects.equals(notaAccreditoId, that.notaAccreditoId) &&
                Objects.equals(aliquotaIvaId, that.aliquotaIvaId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("notaAccreditoId: " + notaAccreditoId);
        result.append(", aliquotaIvaId: " + aliquotaIvaId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
