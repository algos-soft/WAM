import it.algos.wam.entity.wamcompany.WamCompany;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by gac on 01 set 2016.
 * .
 */
public abstract class WamBaseTest {

    protected static EntityManager MANAGER;

    protected final static String COMPANY_UNO = "Alfa";
    protected final static String COMPANY_DUE = "Beta";

    protected final static String SIGLA_UNO = "Prima";
    protected final static String SIGLA_DUE = "Seconda";
    protected final static String DESCRIZIONE_UNO = "Prima descrizione";
    protected final static String DESCRIZIONE_DUE = "Seconda descrizione";

    protected final static String NOME_UNO = "Mario";
    protected final static String NOME_DUE = "Ilaria";
    protected final static String COGNOME_UNO = "Bramieri";
    protected final static String COGNOME_DUE = "Torricelli";


    // alcuni parametri utilizzati
    protected static WamCompany companyUno;
    protected static WamCompany companyDue;

    protected String previsto = "";
    protected String ottenuto = "";


    protected static void setUp() {
        creaManager();

        creaCompanyUno();
        creaCompanyDue();
    } // end of setup iniziale

    /**
     * Creazione di un MANAGER specifico
     * DEVE essere chiuso (must be close by caller method)
     */
    protected static void creaManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("WAMTEST");

        if (factory != null) {
            MANAGER = factory.createEntityManager();
        }// end of if cycle
    }// end of static method


    /**
     * Crea una prima company di prova per i test
     */
    private static void creaCompanyUno() {

        companyUno = new WamCompany(COMPANY_UNO, "Company dimostrativa", "info@crocedemo.it");
        companyUno.setAddress1("Via Turati, 12");
        companyUno.setAddress1("20199 Garbagnate Milanese");
        companyUno.setContact("Mario Bianchi");

        try { // prova ad eseguire il codice
            companyUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println("Esisteva già companyUno");
        }// fine del blocco try-catch
    }// end of single test

    /**
     * Crea una seconda company di prova per i test
     */
    private static void creaCompanyDue() {

        companyDue = new WamCompany(COMPANY_DUE, "Company di test", "info@crocetest.it");
        companyDue.setAddress1("Piazza Napoli, 51");
        companyDue.setAddress1("20100 Milano");
        companyDue.setContact("Giovanni Rossi");

        try { // prova ad eseguire il codice
            companyDue.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println("Esisteva già companyDue");
        }// fine del blocco try-catch
    }// end of single test


    protected static void cleanUp() {
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
     * Chiusura finale del MANAGER
     */
    private static void closeManager() {
        MANAGER.close();
        MANAGER = null;
    }// end of static method


}// end of abstract class