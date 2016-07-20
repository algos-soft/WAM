package it.algos.wam.bootstrap;

import com.vaadin.server.VaadinSession;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibVers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;

/**
 * Log delle versioni, modifiche e patch installate
 * Executed on container startup
 * Setup non-UI logic here
 * <p>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat od altri) <br>
 * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
 * È OBBLIGATORIO aggiungere questa classe nei listeners del file web.WEB-INF.web.xml
 */
public class VersBootStrap implements ServletContextListener {

    /**
     * Executed on container startup
     * Setup non-UI logic here
     * <p>
     * This method is called prior to the servlet context being
     * initialized (when the Web application is deployed).
     * You can initialize servlet context related data here.
     * <p>
     * Tutte le aggiunte, modifiche e patch vengono inserite con una versione <br>
     * L'ordine di inserimento è FONDAMENTALE
     */
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        int k = 0;

        //--prima installazione del programma
        //--non fa nulla, solo informativo
        if (LibVers.installa(++k)) {
            LibVers.nuova("Setup", "Installazione iniziale");
        }// fine del blocco if

        //--creazione di una croce "demo"
        if (LibVers.installa(++k)) {
            BootService.creaCompanyDemo();
            LibVers.nuova("Demo", "Creazione di una croce demo, visibile a tutti");
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
