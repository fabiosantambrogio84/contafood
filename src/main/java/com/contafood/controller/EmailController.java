package com.contafood.controller;

import com.contafood.model.Ddt;
import com.contafood.model.Fattura;
import com.contafood.model.NotaAccredito;
import com.contafood.model.OrdineFornitore;
import com.contafood.service.EmailService;
import com.contafood.service.StampaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SuppressWarnings({"unused"})
@RestController
@RequestMapping(path="/emails")
public class EmailController {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

    private final StampaService stampaService;

    private final EmailService emailService;

    @Autowired
    public EmailController(final StampaService stampaService,
                           final EmailService emailService){
        this.stampaService = stampaService;
        this.emailService = emailService;
    }

    @RequestMapping(method = GET, path = "/ddts/{idDdt}")
    @CrossOrigin
    public void sendEmailDdt(@PathVariable final Long idDdt) throws Exception{

        LOGGER.info("Sending email for ddt '{}'", idDdt);

        byte[] reportBytes = stampaService.generateDdt(idDdt);
        Ddt ddt = stampaService.getDdt(idDdt);
        emailService.sendEmailDdt(ddt, reportBytes);

        LOGGER.info("Successfully sent email for ddt '{}'", idDdt);
    }

    @RequestMapping(method = GET, path = "/fatture/{idFattura}")
    @CrossOrigin
    public void sendEmailFattura(@PathVariable final Long idFattura) throws Exception{

        LOGGER.info("Sending email for fattura '{}'", idFattura);

        byte[] reportBytes = stampaService.generateFattura(idFattura);
        Fattura fattura = stampaService.getFattura(idFattura);
        emailService.sendEmailFattura(fattura, reportBytes);

        LOGGER.info("Successfully sent email for fattura '{}'", idFattura);
    }

    @RequestMapping(method = GET, path = "/note-accredito/{idNotaAccredito}")
    @CrossOrigin
    public void sendEmailNotaAccredito(@PathVariable final Long idNotaAccredito) throws Exception{

        LOGGER.info("Sending email for notaAccredito '{}'", idNotaAccredito);

        byte[] reportBytes = stampaService.generateNotaAccredito(idNotaAccredito);
        NotaAccredito notaAccredito = stampaService.getNotaAccredito(idNotaAccredito);
        emailService.sendEmailNotaAccredito(notaAccredito, reportBytes);

        LOGGER.info("Successfully sent email for notaAccredito '{}'", idNotaAccredito);
    }

    @RequestMapping(method = GET, path = "/ordini-fornitori/{idOrdineFornitore}")
    @CrossOrigin
    public void sendEmailOrdineFornitore(@PathVariable final Long idOrdineFornitore) throws Exception{

        LOGGER.info("Sending email for ordine-fornitore '{}'", idOrdineFornitore);

        byte[] reportBytes = stampaService.generateOrdineFornitore(idOrdineFornitore);
        OrdineFornitore ordineFornitore = stampaService.getOrdineFornitore(idOrdineFornitore);
        emailService.sendEmailOrdineFornitore(ordineFornitore, reportBytes);

        LOGGER.info("Successfully sent email for ordine-fornitore '{}'", idOrdineFornitore);
    }

}


