package com.contafood.service;

import com.contafood.model.Cliente;
import com.contafood.model.Fattura;
import com.contafood.util.Constants;
import com.contafood.util.EmailPecConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

@Service
public class EmailPecService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailPecService.class);

    private final ProprietaService proprietaService;

    private final String username;

    private final String password;


    @Autowired
    public EmailPecService(final ProprietaService proprietaService){
        this.proprietaService = proprietaService;
        this.username = proprietaService.findByNome(EmailPecConstants.PEC_SMTP_USER_PROPERTY_NAME).getValore();
        this.password = proprietaService.findByNome(EmailPecConstants.PEC_SMTP_PASSWORD_PROPERTY_NAME).getValore();
    }

    private Properties getProperties(){
        Properties properties = new Properties();

        properties.put("mail.transport.protocol", EmailPecConstants.PROTOCOL);
        properties.put("mail.smtps.auth", EmailPecConstants.AUTH);
        properties.put("mail.smtps.ssl.enable", EmailPecConstants.SSL_ENABLE);
        properties.put("mail.smtps.ssl.trust", "*");
        properties.put("mail.smtps.timeout", EmailPecConstants.TIMEOUT);
        properties.put("mail.smtps.connectiontimeout", EmailPecConstants.CONNECTION_TIMEOUT);

        properties.put("mail.smtps.host", proprietaService.findByNome(EmailPecConstants.PEC_SMTP_HOST_PROPERTY_NAME).getValore());
        properties.put("mail.smtps.port", proprietaService.findByNome(EmailPecConstants.PEC_SMTP_PORT_PROPERTY_NAME).getValore());

        return properties;
    }

    public Session createSession(){
        LOGGER.info("Creating session for connecting to SMTPs server...");

        Properties properties = getProperties();

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        LOGGER.info("Successfully created session for connecting to SMTPs server");
        return session;
    }

    public Transport connect(Session session) throws Exception{
        LOGGER.info("Connecting to SMTPs server...");

        Transport transport = session.getTransport(EmailPecConstants.PROTOCOL);
        transport.connect(username, password);

        LOGGER.info("Successfully connected to SMTPs server");

        return transport;
    }

    public Message createFatturaMessage(Session session, Fattura fattura, boolean isPec, byte[] reportBytes) throws Exception{

        Message message;
        try{
            Cliente cliente = fattura.getCliente();

            String email = cliente.getEmail();
            if(isPec){
                email = cliente.getEmailPec();
            }

            String emailSubject = "Fattura num. "+fattura.getProgressivo()+"-"+fattura.getAnno();

            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EmailPecConstants.FROM_ADDRESS));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(emailSubject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(EmailPecConstants.FATTURA_BODY, EmailPecConstants.BODY_TYPE);

            ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(reportBytes, Constants.MEDIA_TYPE_APPLICATION_PDF);
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.setDataHandler(new DataHandler(byteArrayDataSource));
            attachmentBodyPart.setFileName(byteArrayDataSource.getName());

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

    public void sendMessage(Transport transport, Message message) throws Exception{
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
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
