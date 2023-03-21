package com.contafood.model.views;

import com.contafood.model.reports.DocumentoAcquistoDataSource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;

@EqualsAndHashCode()
@Data
@Entity
@Table(name = "v_documento_acquisto")
public class VDocumentoAcquisto {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "id_documento")
    private Long idDocumento;

    @Column(name = "num_documento")
    private String numDocumento;

    @Column(name = "data_documento")
    private Date dataDocumento;

    @Column(name = "id_fornitore")
    private Long idFornitore;

    @Column(name = "ragione_sociale_fornitore")
    private String ragioneSocialeFornitore;

    @Column(name = "partita_iva_fornitore")
    private String partitaIvaFornitore;

    @Column(name = "id_stato")
    private Long idStato;

    @Column(name = "stato")
    private String stato;

    @Column(name = "totale_imponibile")
    private BigDecimal totaleImponibile;

    @Column(name = "totale_iva")
    private BigDecimal totaleIva;

    @Column(name = "totale")
    private BigDecimal totale;

    @Column(name = "totale_acconto")
    private BigDecimal totaleAcconto;

    @Column(name = "fatturato")
    private Boolean fatturato;

    @Override
    public String toString() {

        return "{" +
                "id: " + id +
                ", tipoDocumento: " + tipoDocumento +
                ", idDocumento: " + idDocumento +
                ", numDocumento: " + numDocumento +
                ", dataDocumento: " + dataDocumento +
                ", idFornitore: " + idFornitore +
                ", ragioneSocialeFornitore: " + ragioneSocialeFornitore +
                ", partitaIvaFornitore: " + partitaIvaFornitore +
                ", idStato: " + idStato +
                ", stato: " + stato +
                ", totaleImponibile: " + totaleImponibile +
                ", totaleIva: " + totaleIva +
                ", totale: " + totale +
                ", totaleAcconto: " + totaleAcconto +
                ", fatturato: " + fatturato +
                "}";
    }

    public DocumentoAcquistoDataSource toDocumentoAcquistoDataSource(SimpleDateFormat simpleDateFormat){
        DocumentoAcquistoDataSource documentoAcquistoDataSource = new DocumentoAcquistoDataSource();
        documentoAcquistoDataSource.setId(this.getId());
        documentoAcquistoDataSource.setTipoDocumento(this.getTipoDocumento());
        documentoAcquistoDataSource.setIdDocumento(this.getIdDocumento());
        documentoAcquistoDataSource.setNumDocumento(this.getNumDocumento());
        documentoAcquistoDataSource.setDataDocumento(simpleDateFormat.format(this.getDataDocumento()));
        documentoAcquistoDataSource.setIdFornitore(this.getIdFornitore());
        documentoAcquistoDataSource.setRagioneSocialeFornitore(this.getRagioneSocialeFornitore());
        documentoAcquistoDataSource.setPartitaIvaFornitore(this.getPartitaIvaFornitore());
        documentoAcquistoDataSource.setIdStato(this.getIdStato());
        documentoAcquistoDataSource.setStato(this.getStato());
        documentoAcquistoDataSource.setTotaleImponibile(this.getTotaleImponibile());
        documentoAcquistoDataSource.setTotaleIva(this.getTotaleIva());
        documentoAcquistoDataSource.setTotale(this.getTotale());
        documentoAcquistoDataSource.setTotaleAcconto(this.getTotaleAcconto());
        documentoAcquistoDataSource.setFatturato(this.getFatturato());

        return documentoAcquistoDataSource;
    }
}
