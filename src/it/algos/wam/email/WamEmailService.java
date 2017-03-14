package it.algos.wam.email;

import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.lib.LibWam;
import it.algos.wam.settings.CompanyPrefs;
import it.algos.wam.settings.ManagerPrefs;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.email.Attachment;
import it.algos.webbase.web.email.EmailService;
import it.algos.webbase.web.lib.LibDate;
import org.apache.commons.mail.EmailException;
import org.jfree.util.Log;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Servizio di invio delle email da WAM
 * Created by alex on 27-07-2016.
 */
public class WamEmailService {


    /**
     * Invia una email di avvenuta iscrizione.
     * Effettiva se true il flag della company
     * Effettiva se esiste la mail dell'admin della company (backupMailbox)
     */
    public static boolean newIscrizione(Iscrizione iscrizione) {
        boolean cont = CompanyPrefs.sendMailToBackup.getBool();
        WamCompany company = LibWam.getCompany();
        Date data = new Date();
        String giorno = LibDate.toStringDMMMYY(data);
        String ora = LibDate.toStringHHMM(data);
        String text = "";
        String to = "";
        String subject = "Iscrizione";
        String aCapo = "<br>";

        if (cont) {
            to = CompanyPrefs.backupMailbox.getString(company);
            if (to.equals("")) {
                cont = false;
                Log.warn(company + ": indirizzo backup non configurato");
            }// end of if cycle
        }// end of if cycle

        if (cont) {
            text += "Oggi ";
            text += giorno;
            text += " alle ";
            text += ora;
            text += ",";
            text += aCapo;
            text += iscrizione.getNewMail();
        }// end of if cycle

        if (cont) {
            try { // prova ad eseguire il codice
                sendMail(company, to, subject, text);
            } catch (Exception unErrore) { // intercetta l'errore
                int a = 87;
            }// fine del blocco try-catch
        }// end of if cycle

        return cont;
    }// end of method


    /**
     * Invia una email di WAM.
     * Usa i parametri di configurazione email dell'applicazione WAM
     * e delega al servizio di invio email generale
     */
    public static boolean sendMail(String to, String subject, String text) throws EmailException {
        return sendMail((WamCompany) CompanySessionLib.getCompany(), to, "", "", subject, text, false, null);
    }// end of method


    /**
     * Invia una email di WAM.
     * Usa i parametri di configurazione email dell'applicazione WAM
     * e delega al servizio di invio email generale
     */
    public static boolean sendMail(WamCompany company, String to, String subject, String text) throws EmailException {
        return sendMail(company, to, "", "", subject, text, false, null);
    }// end of method


    /**
     * Invia una email di WAM.
     * Usa i parametri di configurazione email dell'applicazione WAM
     * e delega al servizio di invio email generale
     */
    public static boolean sendMail(WamCompany company, String to, String cc, String bcc, String subject,
                                   String text, boolean html, Attachment[] attachments) throws EmailException {

        // qui recuperare i parametri di configurazione email WAM (e toglierli dai parametri richiesti da questo metodo)
        String hostName = ManagerPrefs.smtpServer.getString();
        int smtpPort = ManagerPrefs.smtpPort.getInt();
        boolean useAuth = ManagerPrefs.smtpUseAuth.getBool();
        String username = ManagerPrefs.smtpUserName.getString();
        String password = ManagerPrefs.smtpPassword.getString();
        String from = CompanyPrefs.senderAddress.getString(company);

        // se per la company è previsto backup di tutte le email, aggiunge la mailbox di backup al bcc
        boolean backup = CompanyPrefs.sendMailToBackup.getBool(company);
        if (backup) {
            String bkMailbox = CompanyPrefs.backupMailbox.getString(company);
            if (!bkMailbox.equals("")) {
                if (bcc == null) {
                    bcc = "";
                }
                if (!bcc.equals("")) {
                    bcc += ", ";
                }
                bcc += bkMailbox;
            }
        }
        it.algos.webbase.domain.log.Log.info("mail","inviata");

        // delega al sistema di invio generale
        return EmailService.sendMail(hostName, smtpPort, useAuth, username, password, from, to, cc, bcc, subject, text, html, attachments);

    }


}// end of class