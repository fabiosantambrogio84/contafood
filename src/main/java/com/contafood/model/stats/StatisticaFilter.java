package com.contafood.model.stats;

import com.contafood.util.enumeration.StatisticaOpzione;

import java.sql.Date;
import java.util.List;

public class StatisticaFilter {

    private List<Long> idsClienti;

    private List<Long> idsArticoli;

    private Long idFornitore;

    private Date dataDal;

    private Date dataAl;

    private StatisticaOpzione opzione;

    public List<Long> getIdsClienti() {
        return idsClienti;
    }

    public void setIdsClienti(List<Long> idsClienti) {
        this.idsClienti = idsClienti;
    }

    public List<Long> getIdsArticoli() {
        return idsArticoli;
    }

    public void setIdsArticoli(List<Long> idsArticoli) {
        this.idsArticoli = idsArticoli;
    }

    public Long getIdFornitore() {
        return idFornitore;
    }

    public void setIdFornitore(Long idFornitore) {
        this.idFornitore = idFornitore;
    }

    public Date getDataDal() {
        return dataDal;
    }

    public void setDataDal(Date dataDal) {
        this.dataDal = dataDal;
    }

    public Date getDataAl() {
        return dataAl;
    }

    public void setDataAl(Date dataAl) {
        this.dataAl = dataAl;
    }

    public StatisticaOpzione getOpzione() {
        return opzione;
    }

    public void setOpzione(StatisticaOpzione opzione) {
        this.opzione = opzione;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("idsClienti: " + idsClienti);
        result.append(", idsArticoli: " + idsArticoli);
        result.append(", idFornitore: " + idFornitore);
        result.append(", dataDal: " + dataDal);
        result.append(", dataAl: " + dataAl);
        result.append(", opzione: " + opzione);
        result.append("}");

        return result.toString();

    }
}
