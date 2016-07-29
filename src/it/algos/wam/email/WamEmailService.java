package it.algos.wam.email;

import it.algos.wam.base_email.Attachment;
import it.algos.wam.base_email.EmailService;
import it.algos.wam.entity.wamcompany.WamCompany;
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
    public static boolean sendMail(WamCompany company, String to, String subject, String text) throws EmailException {
        return sendMail(company, to, "", "", subject, text, false, null);
    }


    /**
     * Invia una email di WAM.
     * Usa i parametri di configurazione email dell'applicazione WAM
     * e delega al servizio di invio email generale
     */
    public static boolean sendMail(WamCompany company, String to, String cc, String bcc, String subject,
                                   String text, boolean html, Attachment[] attachments) throws EmailException {

        // qui recuperare i parametri di configurazione email WAM (e toglierli dai parametri richiesti da questo metodo)
        //...
        String hostName = "smtp.algos.it";
        int smtpPort = 25;
        boolean useAuth = true;
        String username = "alex@algos.it";
        String password = "barbapapa";
        String from = company.getSenderAddress();

        // se per la company è previsto backup di tutte le email, aggiunge la mailbox di backup al bcc
        if(company.isSendMailToBackup()){
            String bkMailbox=company.getBackupMailbox();
            if(!bkMailbox.equals("")){
                if(bcc==null){
                    bcc="";
                }
                if(!bcc.equals("")){
                    bcc+=", ";
                }
                bcc+=company.getBackupMailbox();
            }
        }

        // delega al sistema di invio generale
        return EmailService.sendMail(hostName, smtpPort, useAuth, username, password, from, to, cc, bcc, subject, text, html, attachments);

    }

}