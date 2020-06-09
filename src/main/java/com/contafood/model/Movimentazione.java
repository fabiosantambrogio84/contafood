package com.contafood.model;

import java.sql.Date;

public class Movimentazione {

    private Long idGiacenza;

    private String inputOutput;

    private Date data;

    private Float quantita;

    private String descrizione;

    public Long getIdGiacenza() {
        return idGiacenza;
    }

    public void setIdGiacenza(Long idGiacenza) {
        this.idGiacenza = idGiacenza;
    }

    public String getInputOutput() {
        return inputOutput;
    }

    public void setInputOutput(String inputOutput) {
        this.inputOutput = inputOutput;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Float getQuantita() {
        return quantita;
    }

    public void setQuantita(Float quantita) {
        this.quantita = quantita;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("idGiacenza: " + idGiacenza);
        result.append(", inputOutput: " + inputOutput);
        result.append(", data: " + data);
        result.append(", quantita: " + quantita);
        result.append(", descrizione: " + descrizione);
        result.append("}");

        return result.toString();

    }
}
