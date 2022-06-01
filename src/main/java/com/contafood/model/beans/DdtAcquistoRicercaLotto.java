package com.contafood.model.beans;

import java.sql.Date;

public interface DdtAcquistoRicercaLotto {

    Long getId();
    Date getData();
    Integer getNumero();
    String getFornitore();
    Float getQuantita();
}
