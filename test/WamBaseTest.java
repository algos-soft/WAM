import it.algos.wam.entity.wamcompany.WamCompany;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by gac on 01 set 2016.
 * .
 */
public abstract class WamBaseTest {

    // alcuni parametri utilizzati
    protected final static String COMPANY_UNO = "Alfa";
    protected final static String COMPANY_DUE = "Beta";

    protected final static EntityManager MANAGER = getManager();
    protected final static WamCompany companyUno = creaCompanyUno();
    ;
    protected final static WamCompany companyDue = creaCompanyDue();
    ;

    protected String previsto = "";
    protected String ottenuto = "";

    /**
     * Creazione di un manager specifico
     * DEVE essere chiuso (must be close by caller method)
     */
    private static EntityManager getManager() {
        EntityManager manager = null;
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("WAMTEST");

        if (factory != null) {
            manager = factory.createEntityManager();
        }// end of if cycle

        return manager;
    }// end of static method

//    protected static void setUp() {
//        creaCompanyUno();
//        creaCompanyDue();
//    } // end of setup iniziale

    /**
     * Crea una prima company di prova per i test
     */
    private static WamCompany creaCompanyUno() {
        WamCompany company = null;

        company = new WamCompany(COMPANY_UNO, "Company dimostrativa", "info@crocedemo.it");
        company.setAddress1("Via Turati, 12");
        company.setAddress1("20199 Garbagnate Milanese");
        company.setContact("Mario Bianchi");

        try { // prova ad eseguire il codice
            company.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println("Esisteva già companyUno");
        }// fine del blocco try-catch

        return company;
    }// end of single test

    /**
     * Crea una seconda company di prova per i test
     */
    private static WamCompany creaCompanyDue() {
        WamCompany company = null;

        company = new WamCompany(COMPANY_DUE, "Company di test", "info@crocetest.it");
        company.setAddress1("Piazza Napoli, 51");
        company.setAddress1("20100 Milano");
        company.setContact("Giovanni Rossi");

        try { // prova ad eseguire il codice
            company.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println("Esisteva già companyDue");
        }// fine del blocco try-catch

        return company;
    }// end of single test

    protected static void cleanUpFinaleAllaChiusuraDelTest() {
        cancellaCompanyUno();
        cancellaCompanyDue();

        closeManager();
    } // end of cleaup finale

    /**
     * Cancella la prima company di prova per i test
     */
    private static void cancellaCompanyUno() {
        cancellaCompany(WamCompany.findByCode(COMPANY_UNO, MANAGER));
    }// end of single test

    /**
     * Cancella la seconda company di prova per i test
     */
    private static void cancellaCompanyDue() {
        cancellaCompany(WamCompany.findByCode(COMPANY_DUE, MANAGER));
    }// end of single test

    /**
     * Cancella una company
     */
    private static void cancellaCompany(WamCompany company) {
        if (company != null) {
            company.delete(MANAGER);
        }// end of if cycle
    }// end of single test

    /**
     * Chiusura finale del manager
     */
    private static void closeManager() {
        MANAGER.close();
    }// end of static method

}// end of abstract class