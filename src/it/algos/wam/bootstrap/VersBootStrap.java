package it.algos.wam.bootstrap;

import it.algos.wam.WAMApp;
import it.algos.webbase.web.lib.LibPref;
import it.algos.webbase.web.lib.LibVers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
            LibVers.nuova("DemoWam", "Creazione di una croce demo, visibile a tutti");
        }// fine del blocco if

        //--crea una nuova preferenza, globale per tutte le company
        if (LibVers.installa(++k)) {
            LibPref.newVersBool(WAMApp.DISPLAY_FOOTER_INFO, true, "Visualizza nel footer copyright ed informazioni sul programma");
        }// fine del blocco if

        //--crea una nuova preferenza, globale per tutte le company
        if (LibVers.installa(++k)) {
            LibPref.newVersBool(WAMApp.DISPLAY_TOOLTIPS, true, "Visualizza i toolTips di aiuto nel rollover sui campi del Form (occorre riavviare)");
        }// fine del blocco if

        //--crea una nuova preferenza, globale per tutte le company
        if (LibVers.installa(++k)) {
            LibPref.newVersBool(WAMApp.DISPLAY_FIELD_ORDINE, true, "Visualizza il campo ordine nel Form (solo per dev ed admin)");
        }// fine del blocco if

        //--crea una nuova preferenza, globale per tutte le company
        if (LibVers.installa(++k)) {
            LibPref.newVersBool(WAMApp.DISPLAY_LISTE_COLLEGATE, true, "Visualizza un TabSheet con Form e Liste");
        }// fine del blocco if

        //--crea una nuova preferenza, globale per tutte le company
        if (LibVers.installa(++k)) {
            LibPref.newVersBool(WAMApp.USA_FORM_LAYOUT, false, "Usa il layout form nei Form (campi con label a sinistra)");
        }// fine del blocco if

        //--crea una nuova preferenza, globale per tutte le company
        if (LibVers.installa(++k)) {
            LibPref.newVersBool(WAMApp.USA_REFRESH_DEMO, true, "Ricostruisce periodicamente la company demo");
        }// fine del blocco if

        //--crea una nuova preferenza, globale per tutte le company
        if (LibVers.installa(++k)) {
            LibPref.newVersBool(WAMApp.ATTIVA_MIGRATION, true, "Importa periodicamente le croci da webambulanze");
        }// fine del blocco if

        //--crea una nuova preferenza, globale per tutte le company
        if (LibVers.installa(++k)) {
            LibPref.newVersBool(WAMApp.USA_MIGRATION_COMPLETA, false, "Importa tutti i turni da webambulanze (invece che solo l'ultima settimana)");
        }// fine del blocco if

        //--crea una nuova preferenza, globale per tutte le company
        if (LibVers.installa(++k)) {
            LibPref.newVersBool(WAMApp.CLICK_BOTTONI_IN_LISTA, false, "Bottone per modificare i bottoni (icona e colore) direttamente da Funzioni e Servizi. Di default false.");
        }// fine del blocco if


//        //--creazione di una croce "test"
//        if (LibVers.installa(++k)) {
//            BootService.creaCompanyTest();
//            LibVers.nuova("Test", "Creazione di una croce test, visibile a tutti");
//        }// fine del blocco if

//        //--cancellazione della croce "test"
//        if (LibVers.installa(++k)) {
//            WamCompany company = WamCompany.findByCode(WAMApp.TEST_COMPANY_CODE);
//            company.delete();
//        }// fine del blocco if

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
