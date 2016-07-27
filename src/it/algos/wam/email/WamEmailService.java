package it.algos.wam.email;

import it.algos.wam.base_email.Attachment;
import it.algos.wam.base_email.EmailService;
import org.apache.commons.mail.EmailException;

/**
 * Servizio di invio delle email da WAM
 * Created by alex on 27-07-2016.
 */
public class WamEmailService {

    /**
     * Invia una email di WAM.
     * Usa i parametri di configurazione email dell'applicazione WAM
     * e delega al servizio di invio email generale
     */
    public static boolean sendMail(String to, String bcc, String subject, String text) throws EmailException {
        return sendMail("", 0, false, "", "", "", to, "", bcc, subject, text, false, null);
    }


    /**
     * Invia una email di WAM.
     * Usa i parametri di configurazione email dell'applicazione WAM
     * e delega al servizio di invio email generale
     */
    public static boolean sendMail(String hostName, int smtpPort, boolean useAuth, String username,
                                   String password, String from, String to, String cc, String bcc, String subject,
                                   String text, boolean html, Attachment[] attachments) throws EmailException {

        // qui recuperare i parametri di configurazione email WAM e toglierli dai parametri richiesti da questo metodo
        //...

        // delega al sistema di invio generale
        return EmailService.sendMail(hostName, smtpPort, useAuth, username, password, from, to, cc, bcc, subject, text, html, attachments);

    }

}