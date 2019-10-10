package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProduzioneConfezioneKey implements Serializable {

    @Column(name = "id_produzione")
    Long produzioneId;

    @Column(name = "id_confezione")
    Long confezioneId;

    public ProduzioneConfezioneKey(){}

    public ProduzioneConfezioneKey(Long produzioneId, Long confezioneId){
        this.produzioneId = produzioneId;
        this.confezioneId = confezioneId;
    }

    public Long getProduzioneId() {
        return produzioneId;
    }

    public void setProduzioneId(Long produzioneId) {
        this.produzioneId = produzioneId;
    }

    public Long getConfezioneId() {
        return confezioneId;
    }

    public void setConfezioneId(Long confezioneId) {
        this.confezioneId = confezioneId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(produzioneId, confezioneId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ProduzioneConfezioneKey that = (ProduzioneConfezioneKey) obj;
        return Objects.equals(produzioneId, that.produzioneId) &&
                Objects.equals(confezioneId, that.confezioneId);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("produzioneId: " + produzioneId);
        result.append(", confezioneId: " + confezioneId);
        result.append("}");

        return result.toString();
    }
}
