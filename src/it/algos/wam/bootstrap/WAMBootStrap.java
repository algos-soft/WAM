package it.algos.wam.bootstrap;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.company.Company;
import it.algos.wam.entity.company.Company_;
import it.algos.wam.entity.milite.Milite;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.bootstrap.ABootStrap;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.toolbar.Toolbar;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.List;

/**
 * Bootstrap dell'applicazione
 * Executed on container startup
 * Setup non-UI logic here
 * <p>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat od altri) <br>
 * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
 * È OBBLIGATORIO aggiungere questa classe nei listeners del file web.WEB-INF.web.xml
 */
public class WAMBootStrap extends ABootStrap {

    /**
     * Executed on container startup
     * Setup non-UI logic here
     * <p>
     * This method is called prior to the servlet context being
     * initialized (when the Web application is deployed).
     * You can initialize servlet context related data here.
     * <p>
     * Viene normalmente sovrascritta dalla sottoclasse per regolare alcuni flag dell'applicazione <br>
     * Deve (DEVE) richiamare anche il metodo della superclasse (questo)
     * prima (PRIMA) di eseguire le regolazioni specifiche <br>
     */
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        super.contextInitialized(contextEvent);
        ServletContext svltCtx = ABootStrap.getServletContext();

        // registra il servlet context non appena è disponibile
        WAMApp.setServletContext(svltCtx);

        // eventuali modifiche allle dimensioni dei bottoni
        Toolbar.ALTEZZA_BOTTONI = 40;
        Toolbar.LARGHEZZA_BOTTONI = 140;

        // eventuali modifiche ai flag generali di regolazione
        AlgosApp.USE_SECURITY = false; //@todo per adesso false
        AlgosApp.USE_LOG = false;
        AlgosApp.USE_VERS = true;
        AlgosApp.USE_PREF = true;

        //@todo la creazione della demo è in VersBootStrap
//        if (Company.getDemo() == null) {
//            creaDemoCompany();
//        }

    }// end of method



//    private void creaDemoCompany() {
//        Company company = new Company();
//        company.setCompanyCode(WAMApp.DEMO_COMPANY_CODE);
//        company.setName("Demo");
//        company.setAddress1("Via Turati 12");
//        company.setAddress1("20199 Garbagnate Milanese");
//        company.setContact("Mario Bianchi");
//        company.setEmail("info@crocedemo.it");
//        company.save();
//    }
//
    /**
     * This method is invoked when the Servlet Context
     * (the Web application) is undeployed or
     * WebLogic Server shuts down.
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
    }// end of method

}// end of bootstrap class
