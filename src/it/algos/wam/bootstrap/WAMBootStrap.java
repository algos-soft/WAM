package it.algos.wam.bootstrap;

import it.algos.wam.WAMApp;
import it.algos.wam.daemons.WamScheduler;
import it.algos.wam.settings.ManagerPrefs;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.bootstrap.ABootStrap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.List;

/**
 * Executed on container startup
 * Setup non-UI logic here
 * <p>
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
     * Deve richiamare anche il metodo della superclasse
     */
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        super.contextInitialized(contextEvent);
        ServletContext svltCtx = ABootStrap.getServletContext();

        // registra il servlet context non appena è disponibile
        WAMApp.setServletContext(svltCtx);

        // eventuali modifiche ai flag generali di regolazione
        AlgosApp.USE_SECURITY = false; //@todo per adesso false
        AlgosApp.USE_LOG = false;
        AlgosApp.USE_VERS = true;
        AlgosApp.USE_PREF = true;

        // avvia lo schedulatore che esegue periodicamente i task sul server
//        WamScheduler.getInstance().start();

        // Qui eventuali revisioni dei dati in funzione della versione

        // Esegue dei controlli iniziali per ogni Company
        List<BaseCompany> comps = BaseCompany.query.getList();
        for (BaseCompany company : comps) {
            doForCompany(company);
        }

        // Avvia lo schedulatore che esegue i task periodici sul server
        if (ManagerPrefs.startDaemonAtStartup.getBool()) {
            WamScheduler.getInstance().start();
        }

//        EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("Webambulanze");
//        EntityManager manager = FACTORY.createEntityManager();
//
//        Object alfa= AQuery.queryList(Croce.class, Croce_.sigla,"DEMO",manager);
//
//        manager.close();
    }

    /**
     * Esegue delle operazioni su una data company
     *
     * @param company la company
     */
    private void doForCompany(BaseCompany company) {

    }


//    private void creaDemoCompany() {
//        Company wamcompany = new Company();
//        wamcompany.setCompanyCode(WAMApp.DEMO_COMPANY_CODE);
//        wamcompany.setName("Demo");
//        wamcompany.setAddress1("Via Turati 12");
//        wamcompany.setAddress1("20199 Garbagnate Milanese");
//        wamcompany.setContact("Mario Bianchi");
//        wamcompany.setEmail("info@crocedemo.it");
//        wamcompany.save();
//    }
//

    /**
     * This method is invoked when the Servlet Context
     * (the Web application) is undeployed or
     * WebLogic Server shuts down.
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        // arresta lo scheduler
        WamScheduler.getInstance().stop();

        super.contextDestroyed(servletContextEvent);
    }// end of method

}// end of bootstrap class
