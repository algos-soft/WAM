package it.algos.wam.bootstrap;

import it.algos.wam.lib.WamRuoli;
import it.algos.webbase.domain.ruolo.TipoRuolo;
import it.algos.webbase.web.bootstrap.SecurityBootStrap;

import javax.servlet.ServletContextEvent;

/**
 * Security dell'applicazione
 * Executed on container startup
 * Setup non-UI logic here
 * <p>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat od altri) <br>
 * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
 * Se usata, è OBBLIGATORIO aggiungere questa classe nei listeners del file web.WEB-INF.web.xml
 * Se non utilizzata, può essere cancellata:
 * 1) - deve essere cancellata come classe
 * 2) - deve essere cancellata come listener del file web.WEB-INF.web.xml
 */
public class SecBootStrap extends SecurityBootStrap {

    /**
     * Executed on container startup
     * Setup non-UI logic here
     * <p>
     * This method is called prior to the servlet context being
     * initialized (when the Web application is deployed).
     * You can initialize servlet context related data here.
     * <p>
     * Viene sovrascritta dalla sottoclasse:
     * - per controllare e creare eventuali ruoli specifici <br>
     * - per controllare gli utenti abilitati esistenti e creare quelli eventualmente mancanti <br>
     * Deve (DEVE) richiamare anche il metodo della superclasse (questo)
     * prima (PRIMA) di eseguire le regolazioni specifiche <br>
     */
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        super.contextInitialized(contextEvent);

        // controlla e crea eventuali ruoli mancanti
        this.creaRuoli();

        // controlla e crea utenti abilitati
//        super.creaUtente("nickname", "password", "nuovoRuolo");
    }// end of method


    /**
     * This method is invoked when the Servlet Context
     * (the Web application) is undeployed or
     * WebLogic Server shuts down.
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }// end of method

    /**
     * Controllo esistenza dei ruoli specifici di questo progetto <br>
     * Se mancano, li crea <br>
     */
    private void creaRuoli() {
        for (String nome : WamRuoli.getAllNames()) {
            super.creaRuolo(nome);
        }// fine del ciclo for
    }// end of private static method

}// end of bootstrap class
