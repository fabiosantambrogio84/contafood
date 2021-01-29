package com.contafood.component;

import com.contafood.model.Cliente;
import com.contafood.model.Fattura;
import com.contafood.service.EmailService;
import com.contafood.service.ReportService;
import com.contafood.service.StampaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.concurrent.CompletableFuture;

@Component
public class AsyncExecutor {

    private final static Logger LOGGER = LoggerFactory.getLogger(AsyncExecutor.class);

    private static final String DELIMITER = ";";
    private static final String NEW_LINE = "\n";

    @Async("threadPoolTaskExecutorReport")
    public CompletableFuture<ReportService.FatturaFile> executeAsyncCreateFatturaReport(StampaService stampaService, Fattura fattura) throws Exception{
        long start = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();
        String prefix = "["+threadName+"] ";

        LOGGER.info(prefix + "Creating asynchronously report file for fattura with id '{}'", fattura.getId());

        ReportService.FatturaFile fatturaFile = new ReportService.FatturaFile();

        String fileName = "Fattura_"+fattura.getProgressivo()+"-"+fattura.getAnno()+".pdf";
        byte[] reportBytes = stampaService.generateFattura(fattura.getId());

        fatturaFile.fileName = fileName;
        fatturaFile.content = reportBytes;

        LOGGER.info(prefix + "Elapsed time : " + (System.currentTimeMillis() - start));
        return CompletableFuture.completedFuture(fatturaFile);
    }

    @Async("threadPoolTaskExecutorReport")
    public CompletableFuture<String> executeAsyncSendFatturaReport(EmailService emailService, StampaService stampaService, Fattura fattura, Session session, boolean isPec) throws Exception{
        long start = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();
        String prefix = "["+threadName+"] ";

        LOGGER.info(prefix + "Sending asynchronously report email for fattura with id '{}'", fattura.getId());

        String result = "";

        StringBuilder stringBuilder = new StringBuilder();

        Transport transport = emailService.connect(session);

        Integer progressivo = fattura.getProgressivo();
        Integer anno = fattura.getAnno();

        stringBuilder.append("Fattura_").append(progressivo).append("/").append(anno).append(DELIMITER);

        Cliente cliente = fattura.getCliente();
        if(!cliente.getDittaIndividuale()){
            stringBuilder.append(cliente.getRagioneSociale()).append(DELIMITER);
        } else {
            stringBuilder.append(cliente.getNome()).append(" ").append(cliente.getCognome()).append(DELIMITER);
        }

        String email = cliente.getEmail();
        if(isPec){
            email = cliente.getEmailPec();
        }
        stringBuilder.append(email).append(DELIMITER);

        try{
            byte[] reportBytes = stampaService.generateFattura(fattura.getId());
            Message message = emailService.createFatturaMessage(session, fattura, isPec, reportBytes);
            emailService.sendMessage(transport, message);

            stringBuilder.append("OK");

        } catch(Exception e){
            e.printStackTrace();
            stringBuilder.append("ERROR").append(DELIMITER).append(e.getMessage());
        } finally {
            transport.close();
        }
        stringBuilder.append(NEW_LINE);

        result = stringBuilder.toString();

        LOGGER.info(prefix + "Elapsed time : " + (System.currentTimeMillis() - start));
        return CompletableFuture.completedFuture(result);
    }
}
