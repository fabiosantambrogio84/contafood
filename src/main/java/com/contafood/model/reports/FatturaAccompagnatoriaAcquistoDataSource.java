package com.contafood.model.reports;

import lombok.Data;

@Data
public class FatturaAccompagnatoriaAcquistoDataSource {

    private String numero;

    private String data;

    private String fornitorePartitaIva;

    private String fornitoreCodiceFiscale;

    private String fornitoreDescrizione;

    private String causale;

    private String pagamento;

}
