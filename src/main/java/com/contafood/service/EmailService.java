package com.contafood.service;

import com.contafood.model.*;
import com.contafood.util.Constants;
import com.contafood.util.EmailConstants;
import com.contafood.util.EmailPecConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Properties;

@Slf4j
@Service
public class EmailService {

    private final ProprietaService proprietaService;
    private final OrdineFornitoreService ordineFornitoreService;

    private String username;
    private String password;

    @Autowired
    public EmailService(final ProprietaService proprietaService,
                        final OrdineFornitoreService ordineFornitoreService){
        this.proprietaService = proprietaService;
        this.ordineFornitoreService = ordineFornitoreService;
    }

    private Properties getProperties(){
        Properties properties = new Properties();

        properties.put("mail.transport.protocol", EmailConstants.PROTOCOL);
        properties.put("mail.smtps.auth", EmailConstants.AUTH);
        properties.put("mail.smtps.timeout", EmailConstants.TIMEOUT);
        properties.put("mail.smtps.connectiontimeout", EmailConstants.CONNECTION_TIMEOUT);

        properties.put("mail.smtps.host", proprietaService.findByNome(EmailConstants.SMTP_HOST_PROPERTY_NAME).getValore());
        properties.put("mail.smtps.port", proprietaService.findByNome(EmailConstants.SMTP_PORT_PROPERTY_NAME).getValore());

        return properties;
    }

    public Session createSession(){
        log.info("Creating session for connecting to SMTPs server...");

        username = proprietaService.findByNome(EmailConstants.SMTP_USER_PROPERTY_NAME).getValore();
        password = proprietaService.findByNome(EmailConstants.SMTP_PASSWORD_PROPERTY_NAME).getValore();

        Properties properties = getProperties();

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        log.info("Successfully created session for connecting to SMTPs server");
        return session;
    }

    public Transport connect(Session session) throws Exception{
        log.info("Connecting to SMTPs server...");

        Transport transport = session.getTransport(EmailPecConstants.PROTOCOL);
        transport.connect(username, password);

        log.info("Successfully connected to SMTPs server");

        return transport;
    }

    private void closeTransport(Transport transport){
        if(transport != null){
            try {
                transport.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Message createMessage(Session session, String emailTo, String emailSubject, String emailBody, String attachmentName,  byte[] reportBytes) throws Exception{

        Message message;
        try{
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(emailSubject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(emailBody, EmailConstants.BODY_TYPE);

            ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(reportBytes, Constants.MEDIA_TYPE_APPLICATION_PDF);
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.setDataHandler(new DataHandler(byteArrayDataSource));
            attachmentBodyPart.setFileName(attachmentName);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }

        return message;
    }

    private Message createFatturaMessage(Session session, Fattura fattura, byte[] reportBytes) throws Exception{

        Cliente cliente = fattura.getCliente();

        String emailTo = cliente.getEmail();
        String emailSubject = "Fattura num. "+fattura.getProgressivo()+"-"+fattura.getAnno();
        String attachmentName = "Fattura_num_"+fattura.getProgressivo()+"-"+fattura.getAnno();
        String emailBody = "In allegato il pdf della fattura.<br/>Cordiali saluti";

        return createMessage(session, emailTo, emailSubject,emailBody, attachmentName, reportBytes);
    }

    private Message createDdtMessage(Session session, Ddt ddt, byte[] reportBytes) throws Exception{

        Cliente cliente = ddt.getCliente();

        String emailTo = cliente.getEmail();
        String emailSubject = "DDT num. "+ddt.getProgressivo()+"-"+ddt.getAnnoContabile();
        String attachmentName = "DDT_num_"+ddt.getProgressivo()+"-"+ddt.getAnnoContabile();
        String emailBody = "In allegato il pdf del DDT.<br/>Cordiali saluti";

        return createMessage(session, emailTo, emailSubject, emailBody, attachmentName, reportBytes);
    }

    private Message createNotaAccreditoMessage(Session session, NotaAccredito notaAccredito, byte[] reportBytes) throws Exception{

        Cliente cliente = notaAccredito.getCliente();

        String emailTo = cliente.getEmail();
        String emailSubject = "Nota accredito num. "+notaAccredito.getProgressivo()+"-"+notaAccredito.getAnno();
        String attachmentName = "Nota_accredito_num_"+notaAccredito.getProgressivo()+"-"+notaAccredito.getAnno();
        String emailBody = "In allegato il pdf della nota di accredito.<br/>Cordiali saluti";

        return createMessage(session, emailTo, emailSubject, emailBody, attachmentName, reportBytes);
    }

    private Message createOrdineFornitoreMessage(Session session, OrdineFornitore ordineFornitore, byte[] reportBytes) throws Exception{

        Fornitore fornitore = ordineFornitore.getFornitore();

        String emailTo = fornitore.getEmailOrdini();
        String emailSubject = "Ordine num. "+ordineFornitore.getProgressivo()+"-"+ordineFornitore.getAnnoContabile();
        String attachmentName = "Ordine_fornitore_num_"+ordineFornitore.getProgressivo()+"-"+ordineFornitore.getAnnoContabile();
        String emailBody = "In allegato il pdf dell'ordine.<br/>Cordiali saluti";

        return createMessage(session, emailTo, emailSubject, emailBody, attachmentName, reportBytes);
    }

    private Message createRecapReportFattureMessage(Session session, String txtFileName, byte[] reportBytes) throws Exception{

        String emailTo = Constants.DEFAULT_EMAIL;
        String emailSubject = "Riepilogo spedizione fatture: file "+txtFileName;
        String emailBody = "In allegato il file txt contenente il riepilogo dell'invio email delle fatture.<br/>Cordiali saluti";

        return createMessage(session, emailTo, emailSubject, emailBody, txtFileName, reportBytes);
    }

    private void sendMessage(Transport transport, Message message) throws Exception{
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
    }

    public void sendEmailDdt(Ddt ddt, byte[] reportBytes) throws Exception{
        Session session = createSession();
        Transport transport = connect(session);
        Message message = createDdtMessage(session, ddt, reportBytes);
        sendMessage(transport, message);
        closeTransport(transport);
    }

    public void sendEmailFattura(Fattura fattura, byte[] reportBytes) throws Exception{
        Session session = createSession();
        Transport transport = connect(session);
        Message message = createFatturaMessage(session, fattura, reportBytes);
        sendMessage(transport, message);
        closeTransport(transport);
    }

    public void sendEmailNotaAccredito(NotaAccredito notaAccredito, byte[] reportBytes) throws Exception{
        Session session = createSession();
        Transport transport = connect(session);
        Message message = createNotaAccreditoMessage(session, notaAccredito, reportBytes);
        sendMessage(transport, message);
        closeTransport(transport);
    }

    public void sendEmailOrdineFornitore(OrdineFornitore ordineFornitore, byte[] reportBytes) throws Exception{
        Session session = createSession();
        Transport transport = connect(session);
        Message message = createOrdineFornitoreMessage(session, ordineFornitore, reportBytes);
        sendMessage(transport, message);
        closeTransport(transport);

        ordineFornitore.setEmailInviata(Constants.EMAIL_INVIATA_OK);
        ordineFornitore.setDataInvioEmail(Timestamp.from(ZonedDateTime.now().toInstant()));
        ordineFornitoreService.patch(ordineFornitore);
    }

    public void sendEmailRecapReportFatture(String fileName, byte[] reportBytes) throws Exception{
        Session session = createSession();
        Transport transport = connect(session);
        Message message = createRecapReportFattureMessage(session, fileName, reportBytes);
        sendMessage(transport, message);
        closeTransport(transport);
    }

    /*
    public static void main(String[] args) throws Exception{
        Properties properties = new Properties();

        properties.put("mail.transport.protocol", EmailConstants.PROTOCOL);
        properties.put("mail.smtps.auth", EmailConstants.AUTH);
        properties.put("mail.smtps.ssl.enable", EmailConstants.SSL_ENABLE);
        properties.put("mail.smtps.ssl.trust", "*");
        properties.put("mail.debug", true);

        properties.put("mail.smtps.host", "sendm.cert.legalmail.it");
        properties.put("mail.smtps.port", "465");

        String username = "M1691525";
        String password = "mABEPI.0812";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EmailConstants.FROM_ADDRESS));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse("fabio.santa84@gmail.com"));
        message.setSubject("TEST");

        String msg = "This is my first email using JavaMailer";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport transport = session.getTransport(EmailConstants.PROTOCOL);
        transport.connect(username, password);
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

    }*/
}