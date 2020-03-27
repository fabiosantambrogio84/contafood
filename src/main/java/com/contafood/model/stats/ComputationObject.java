package com.contafood.model.stats;

import com.contafood.model.Ddt;
import com.contafood.model.FatturaAccompagnatoria;

import java.util.List;

public class ComputationObject {

    private List<Ddt> ddts;

    private List<FatturaAccompagnatoria> fattureAccompagnatorie;

    public List<Ddt> getDdts() {
        return ddts;
    }

    public void setDdts(List<Ddt> ddts) {
        this.ddts = ddts;
    }

    public List<FatturaAccompagnatoria> getFattureAccompagnatorie() {
        return fattureAccompagnatorie;
    }

    public void setFattureAccompagnatorie(List<FatturaAccompagnatoria> fattureAccompagnatorie) {
        this.fattureAccompagnatorie = fattureAccompagnatorie;
    }

}
