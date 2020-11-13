package com.contafood.util;

import com.contafood.model.AliquotaIva;
import com.contafood.model.Articolo;
import com.contafood.service.ArticoloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AccountingUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(AccountingUtils.class);

    public static BigDecimal computeImponibile(Float quantita, BigDecimal prezzo, BigDecimal sconto){
        BigDecimal imponibile = new BigDecimal(0);

        // imponibile = (quantita*prezzo)-sconto
        if(quantita == null){
            quantita = 0F;
        }
        if(prezzo == null){
            prezzo = new BigDecimal(0);
        }
        if(sconto == null){
            sconto = new BigDecimal(0);
        }
        BigDecimal quantitaPerPrezzo = prezzo.multiply(BigDecimal.valueOf(quantita));
        BigDecimal scontoValue = sconto.divide(BigDecimal.valueOf(100)).multiply(quantitaPerPrezzo);

        imponibile = quantitaPerPrezzo.subtract(scontoValue).setScale(2, RoundingMode.HALF_DOWN);
        return imponibile;
    }

    public static BigDecimal computeCosto(Float quantita, Long idArticolo, ArticoloService articoloService){
        BigDecimal costo = new BigDecimal(0);

        if(quantita == null){
            quantita = 0F;
        }
        BigDecimal prezzoAcquisto = new BigDecimal(0);
        if(idArticolo != null){
            Articolo articolo = articoloService.getOne(idArticolo);
            LOGGER.info("Compute costo for 'articolo' {}", articolo);
            if(articolo != null){
                prezzoAcquisto = articolo.getPrezzoAcquisto();
            }
        }
        LOGGER.info("Prezzo acquisto '{}'", prezzoAcquisto);
        costo = (prezzoAcquisto.multiply(BigDecimal.valueOf(quantita))).setScale(2, RoundingMode.HALF_DOWN);
        return costo;
    }

    public static BigDecimal computeTotale(Float quantita, BigDecimal prezzo, BigDecimal sconto, AliquotaIva aliquotaIva, Long idArticolo, ArticoloService articoloService){
        BigDecimal totale = new BigDecimal(0);

        BigDecimal imponibile = computeImponibile(quantita, prezzo, sconto);

        BigDecimal aliquotaIvaValore = new BigDecimal(0);
        if(aliquotaIva != null){
            aliquotaIvaValore = aliquotaIva.getValore();
        } else {
            if (idArticolo != null) {
                Articolo articolo = articoloService.getOne(idArticolo);
                LOGGER.info("Compute costo for 'articolo' {}", articolo);
                if (articolo != null) {
                    AliquotaIva aliquotaIVa = articolo.getAliquotaIva();
                    if (aliquotaIVa != null) {
                        aliquotaIvaValore = articolo.getAliquotaIva().getValore();
                    }
                }
            }
        }

        BigDecimal ivaValue = aliquotaIvaValore.divide(BigDecimal.valueOf(100)).multiply(imponibile);
        totale = imponibile.add(ivaValue).setScale(2, RoundingMode.HALF_DOWN);

        return totale;
    }

}
