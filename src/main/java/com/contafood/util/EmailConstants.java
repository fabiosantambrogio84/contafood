package com.contafood.util;

public interface EmailConstants {

    String PEC_SMTP_HOST_PROPERTY_NAME = "PEC_SMTP_HOST";

    String PEC_SMTP_PORT_PROPERTY_NAME = "PEC_SMTP_PORT";

    String PEC_SMTP_USER_PROPERTY_NAME = "PEC_SMTP_USER";

    String PEC_SMTP_PASSWORD_PROPERTY_NAME = "PEC_SMTP_PASSWORD";

    String PROTOCOL = "smtps";

    Boolean AUTH = true;

    String SSL_ENABLE = "true";

    String TIMEOUT = "20000";

    String CONNECTION_TIMEOUT = "20000";

    String BODY_TYPE = "text/html";

    String FROM_ADDRESS = "urbanialimentari@legalmail.it";

    String FATTURA_BODY = "In allegato il pdf della fattura.<br/>Cordiali saluti";

}
