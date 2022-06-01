package com.contafood.model.beans;

import java.sql.Date;

public interface DdtRicercaLotto {

    Long getId();
    Integer getProgressivo();
    Date getData();
    String getCliente();
    Float getQuantita();
}
