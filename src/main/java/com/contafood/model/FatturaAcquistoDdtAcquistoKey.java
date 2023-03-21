package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FatturaAcquistoDdtAcquistoKey implements Serializable {

    private static final long serialVersionUID = 4203275973353853503L;

    @Column(name = "id_fattura_acquisto")
    Long fatturaAcquistoId;

    @Column(name = "id_ddt_acquisto")
    Long ddtAcquistoId;

    @Column(name = "uuid")
    String uuid;

    public FatturaAcquistoDdtAcquistoKey(){}

    public FatturaAcquistoDdtAcquistoKey(Long fatturaAcquistoId, Long ddtAcquistoId, String uuid){
        this.fatturaAcquistoId = fatturaAcquistoId;
        this.ddtAcquistoId = ddtAcquistoId;
        this.uuid = uuid;
    }

    public Long getFatturaAcquistoId() {
        return fatturaAcquistoId;
    }

    public void setFatturaAcquistoId(Long fatturaAcquistoId) {
        this.fatturaAcquistoId = fatturaAcquistoId;
    }

    public Long getDdtAcquistoId() {
        return ddtAcquistoId;
    }

    public void setDdtAcquistoId(Long ddtAcquistoId) {
        this.ddtAcquistoId = ddtAcquistoId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fatturaAcquistoId, ddtAcquistoId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FatturaAcquistoDdtAcquistoKey that = (FatturaAcquistoDdtAcquistoKey) obj;
        return Objects.equals(fatturaAcquistoId, that.fatturaAcquistoId) &&
                Objects.equals(ddtAcquistoId, that.ddtAcquistoId) &&
                Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("fatturaId: " + fatturaAcquistoId);
        result.append(", ddtId: " + ddtAcquistoId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
