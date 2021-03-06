import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.domain.company.BaseCompany;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import test.BaseTest;

/**
 * Created by gac on 01 set 2016.
 * .
 */
public abstract class WamTest extends BaseTest {

    private final static String DB_NAME = "WAMTEST";

    protected static WamCompany COMPANY_UNO;
    protected static WamCompany COMPANY_DUE;

    /**
     * SetUp iniziale eseguito solo una volta alla creazione della classe
     * <p>
     * Nella superclasse sono già state create due company di tipo BaseCompany
     * Le cancello, perché qui devo usare il tipo WamCompany
     * Npn posso sovrascrivere i metodi creaCompanyUno e creaCompanyDue, perché sono statici
     */
    @BeforeClass
    public static void setUpInizialeStaticoEseguitoSoloUnaVoltaAllaCreazioneDellaClasse() {
        BaseTest.setUpClass(DB_NAME);
        BaseTest.cancellaCompanyUno();
        BaseTest.cancellaCompanyDue();
        WamTest.creaCompanyUno();
        WamTest.creaCompanyDue();
    } // end of setup statico iniziale della classe

    /**
     * CleanUp finale eseguito solo una volta alla chiusura della classe
     */
    @AfterClass
    public static void cleanUpFinaleStaticoEseguitoSoloUnaVoltaAllaChiusuraDellaClasse() {
        WamTest.cancellaCompanyUno();
        WamTest.cancellaCompanyDue();
        BaseTest.cleanUpClass();
    } // end of cleaup statico finale della classe


    /**
     * Crea una prima company di prova per i test
     */
    protected static void creaCompanyUno() {
        COMPANY_UNO = new WamCompany(COMPANY_UNO_CODE, "Company dimostrativa");
        COMPANY_UNO.setEmail("info@crocedemo.it");
        COMPANY_UNO.setContact("Mario Bianchi");
        COMPANY_UNO.setAddress1("Via Turati, 12");
        COMPANY_UNO.setAddress2("20199 Garbagnate Milanese");

        try { // prova ad eseguire il codice
            COMPANY_UNO.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println("Esisteva già companyUno");
        }// fine del blocco try-catch
    }// end of static method

    /**
     * Crea una seconda company di prova per i test
     */
    protected static void creaCompanyDue() {
        COMPANY_DUE = new WamCompany(COMPANY_DUE_CODE, "Company dimostrativa");
        COMPANY_DUE.setContact("Giovanni Rossi");
        COMPANY_DUE.setEmail("info@crocetest.it");
        COMPANY_DUE.setAddress1("Piazza Napoli, 51");
        COMPANY_DUE.setAddress2("20100 Milano");

        try { // prova ad eseguire il codice
            COMPANY_DUE.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println("Esisteva già companyDue");
        }// fine del blocco try-catch
    }// end of static method


    /**
     * Cancella la prima company di prova per i test
     */
    protected static void cancellaCompanyUno() {
        WamTest.cancellaCompany(BaseCompany.findByCode(COMPANY_UNO_CODE, MANAGER));
    }// end of static method

    /**
     * Cancella la seconda company di prova per i test
     */
    protected static void cancellaCompanyDue() {
        WamTest.cancellaCompany(BaseCompany.findByCode(COMPANY_DUE_CODE, MANAGER));
    }// end of static method

    /**
     * Cancella una company
     */
    private static void cancellaCompany(BaseCompany company) {
        if (company != null) {
            company.delete(MANAGER);
        }// end of if cycle
    }// end of static method


//    protected final static String COMPANY_UNO = "Alfa";
//    protected final static String COMPANY_DUE = "Beta";
//    protected static EntityManager MANAGER;
//    // alcuni parametri utilizzati
//    protected static WamCompany companyUno;
//    protected static WamCompany companyDue;
//    protected String code1 = "uno";
//    protected String code2 = "due";
//    protected String code3 = "tre";
//    protected String code4 = "quattro";
//    protected String sigla1 = "Prima";
//    protected String sigla2 = "Seconda";
//    protected String sigla3 = "Terza";
//    protected String sigla4 = "Quarta";
//    protected String desc1 = "Prima descrizione";
//    protected String desc2 = "Seconda descrizione";
//    protected String desc3 = "Terza descrizione";
//    protected String nome1 = "Mario";
//    protected String nome2 = "Ilaria";
//    protected String nome3 = "Giovanni";
//    protected String nome4 = "Roberto";
//    protected String cognome1 = "Bramieri";
//    protected String cognome2 = "Torricelli";
//    protected String cognome3 = "Rossi";
//    protected String cognome4 = "Mazzacurati";
//    protected int numSorgente = 0;
//    protected int numPrevisto = 0;
//    protected int numOttenuto = 0;
//    protected int ordine;
//    protected String previsto = "";
//    protected String ottenuto = "";
//
//    protected ArrayList<Long> chiavi = new ArrayList<>();
//    protected ArrayList<Long> chiaviUno = new ArrayList<>();
//    protected ArrayList<Long> chiaviDue = new ArrayList<>();
//    protected List<String> listStr = new ArrayList<>();
//
//    protected ArrayList<String> listaCodeCompanyUnici = new ArrayList<>();
//
//    /**
//     * SetUp iniziale eseguito solo una volta alla creazione della sottoclasse
//     */
//    protected static void setUpClass() {
//        // creazione del MANAGER statico per questa singola classe di test
//        creaManager();
//
//        // crea alcune company di prova
//        creaCompanyUno();
//        creaCompanyDue();
//    } // end of setup statico iniziale della sottoclasse
//
//
//    /**
//     * Cleanup finale eseguito solo una volta alla chiusura della sottoclasse
//     */
//    protected static void cleanUpClass() {
//        cancellaCompanyUno();
//        cancellaCompanyDue();
//
//        closeManager();
//    } // end of cleaup finale
//
//    /**
//     * Creazione di un MANAGER specifico
//     * DEVE essere chiuso (must be close by caller method)
//     */
//    private static void creaManager() {
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory("WAMTEST");
//
//        if (factory != null) {
//            MANAGER = factory.createEntityManager();
//        }// end of if cycle
//    }// end of static method
//
//
//    /**
//     * Crea una prima company di prova per i test
//     */
//    private static void creaCompanyUno() {
//
//        companyUno = new WamCompany(COMPANY_UNO, "Company dimostrativa", "info@crocedemo.it");
//        companyUno.setAddress1("Via Turati, 12");
//        companyUno.setAddress1("20199 Garbagnate Milanese");
//        companyUno.setContact("Mario Bianchi");
//
//        try { // prova ad eseguire il codice
//            companyUno.save(MANAGER);
//        } catch (Exception unErrore) { // intercetta l'errore
//            System.out.println("Esisteva già companyUno");
//        }// fine del blocco try-catch
//    }// end of single test
//
//    /**
//     * Crea una seconda company di prova per i test
//     */
//    private static void creaCompanyDue() {
//
//        companyDue = new WamCompany(COMPANY_DUE, "Company di test", "info@crocetest.it");
//        companyDue.setAddress1("Piazza Napoli, 51");
//        companyDue.setAddress1("20100 Milano");
//        companyDue.setContact("Giovanni Rossi");
//
//        try { // prova ad eseguire il codice
//            companyDue.save(MANAGER);
//        } catch (Exception unErrore) { // intercetta l'errore
//            System.out.println("Esisteva già companyDue");
//        }// fine del blocco try-catch
//    }// end of single test
//
//    /**
//     * Cancella la prima company di prova per i test
//     */
//    private static void cancellaCompanyUno() {
//        cancellaCompany(WamCompany.findByCode(COMPANY_UNO, MANAGER));
//    }// end of single test
//
//    /**
//     * Cancella la seconda company di prova per i test
//     */
//    private static void cancellaCompanyDue() {
//        cancellaCompany(WamCompany.findByCode(COMPANY_DUE, MANAGER));
//    }// end of single test
//
//    /**
//     * Cancella una company
//     */
//    private static void cancellaCompany(WamCompany company) {
//        if (company != null) {
//            company.delete(MANAGER);
//        }// end of if cycle
//    }// end of single test
//
//
//    /**
//     * Chiusura finale del MANAGER
//     */
//    protected static void closeManager() {
//        MANAGER.close();
//        MANAGER = null;
//    }// end of static method
//
//    /**
//     * SetUp eseguito prima dell'esecuzione di ogni metodo
//     */
//    public void setUp() {
//        cancellaRecords();
//        reset();
//        creaRecords();
//    } // end of setup iniziale
//
//
//    /**
//     * CleanUp eseguito dopo l'esecuzione di ogni metodo
//     */
//    public void cleanUp() {
//        reset();
//        cancellaRecords();
//    } // end of cleaup dopo ogni metodo di test
//
//
//    /**
//     * Prima di inizare a creare e modificare le funzioni, cancello tutte le (eventuali) precedenti
//     * Alla fine, cancello tutte le funzioni create
//     */
//    protected void cancellaRecords() {
//    } // end of cleaup finale
//
//    /**
//     * Azzera le variabili d'istanza
//     */
//    protected void reset() {
//        chiavi = new ArrayList<>();
//        chiaviUno = new ArrayList<>();
//        chiaviDue = new ArrayList<>();
//    }// end of method
//
//    protected void creaRecords() {
//    } // end of cleaup finale


}// end of abstract class