package it.algos.wam.bootstrap;

import it.algos.webbase.web.lib.LibVers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
  * Log delle versioni, modifiche e patch installate
  * Executed on container startup
  * Setup non-UI logic here
  * <p/>
  * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat od altri) <br>
  * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
  * È OBBLIGATORIO aggiungere questa classe nei listeners del file web.WEB-INF.web.xml
  */
public class VersBootStrap implements ServletContextListener{

    /**
     * Executed on container startup
     * Setup non-UI logic here
     * <p/>
     * This method is called prior to the servlet context being
     * initialized (when the Web application is deployed).
     * You can initialize servlet context related data here.
     * <p/>
     * Tutte le aggiunte, modifiche e patch vengono inserite con una versione <br>
     * L'ordine di inserimento è FONDAMENTALE
     */
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {

        //--prima installazione del programma
        //--non fa nulla, solo informativo
        if (LibVers.installa(1)) {
            LibVers.nuova("Setup", "Installazione iniziale");
        }// fine del blocco if

    }// end of method


    /**
     * This method is invoked when the Servlet Context
     * (the Web application) is undeployed or
     * WebLogic Server shuts down.
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }// end of method

}// end of bootstrap class
