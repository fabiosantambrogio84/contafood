package com.contafood.model.stats;

import com.contafood.model.DdtArticolo;
import com.contafood.model.FatturaAccompagnatoriaArticolo;

import java.util.List;

public class ComputationObject {

    private List<DdtArticolo> ddtArticoli;

    private List<FatturaAccompagnatoriaArticolo> fattureAccompagnatorieArticoli;

    public List<DdtArticolo> getDdtArticoli() {
        return ddtArticoli;
    }

    public void setDdtArticoli(List<DdtArticolo> ddtArticoli) {
        this.ddtArticoli = ddtArticoli;
    }

    public List<FatturaAccompagnatoriaArticolo> getFattureAccompagnatorieArticoli() {
        return fattureAccompagnatorieArticoli;
    }

    public void setFattureAccompagnatorieArticoli(List<FatturaAccompagnatoriaArticolo> fattureAccompagnatorieArticoli) {
        this.fattureAccompagnatorieArticoli = fattureAccompagnatorieArticoli;
    }
}
