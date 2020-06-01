package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FatturaDdtKey implements Serializable {

    private static final long serialVersionUID = -1264333928344274658L;

    @Column(name = "id_fattura")
    Long fatturaId;

    @Column(name = "id_ddt")
    Long ddtId;

    @Column(name = "uuid")
    String uuid;

    public FatturaDdtKey(){}

    public FatturaDdtKey(Long fatturaId, Long ddtId, String uuid){
        this.fatturaId = fatturaId;
        this.ddtId = ddtId;
        this.uuid = uuid;
    }

    public Long getFatturaId() {
        return fatturaId;
    }

    public void setFatturaId(Long fatturaId) {
        this.fatturaId = fatturaId;
    }

    public Long getDdtId() {
        return ddtId;
    }

    public void setDdtId(Long ddtId) {
        this.ddtId = ddtId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fatturaId, ddtId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FatturaDdtKey that = (FatturaDdtKey) obj;
        return Objects.equals(fatturaId, that.fatturaId) &&
                Objects.equals(ddtId, that.ddtId) &&
                Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("fatturaId: " + fatturaId);
        result.append(", ddtId: " + ddtId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
