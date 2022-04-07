package com.contafood.component;

import com.contafood.model.Cliente;
import com.contafood.model.Fattura;
import com.contafood.service.EmailPecService;
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
import java.util.Set;
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
    public void executeAsyncSendFattureReport(EmailService emailService, EmailPecService emailPecService, StampaService stampaService, Set<Fattura> fatture, String txtFileName, boolean isPec) throws Exception{
        long start = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();
        String prefix = "["+threadName+"] ";

        LOGGER.info(prefix + "Sending asynchronously report email for a set of fatture");

        String txtContent = "";
        StringBuilder stringBuilder = new StringBuilder();

        Session pecSession;
        Transport pecTransport = null;

        try {
            pecSession = emailPecService.createSession();
            pecTransport = emailPecService.connect(pecSession);

            for(Fattura fattura : fatture){

                try{
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

                    byte[] reportBytes = stampaService.generateFattura(fattura.getId());
                    Message message = emailPecService.createFatturaMessage(pecSession, fattura, isPec, reportBytes);
                    emailPecService.sendMessage(pecTransport, message);

                    stringBuilder.append("OK");

                    stringBuilder.append(NEW_LINE);

                    txtContent += stringBuilder.toString();

                    stringBuilder.setLength(0);

                    Thread.sleep(1000);

                } catch(Exception e){
                    e.printStackTrace();
                    stringBuilder.append("ERROR").append(DELIMITER).append(e.getMessage());
                }
            }

        } catch(Exception e){
            LOGGER.error("Error opening session to SMTP server for PEC", e);
            txtContent = "Errore nell'apertura della sessione sul server SMTP PEC";
        } finally {
            if(pecTransport != null) {
                pecTransport.close();
            }
        }

        try{
            emailService.sendEmailRecapReportFatture(txtFileName, txtContent.getBytes());
        } catch(Exception e){
            LOGGER.error("Error sending recap email", e);
        }

        LOGGER.info(prefix + "Elapsed time : " + (System.currentTimeMillis() - start));
    }
}
