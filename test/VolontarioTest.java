import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gac on 11 set 2016.
 * .
 */
public class VolontarioTest extends WamTest {

    private Volontario vol;

    private List<Volontario> lista = new ArrayList<>();
    private List<Volontario> listaUno = new ArrayList<>();
    private List<Volontario> listaDue = new ArrayList<>();
    private List<Volontario> listaTre = new ArrayList<>();


    /**
     * SetUp iniziale eseguito solo una volta alla creazione della classe
     */
    @BeforeClass
    public static void setUpInizialeStaticoEseguitoSoloUnaVoltaAllaCreazioneDellaClasse() {
        WamTest.setUpClass();
    } // end of setup statico iniziale della classe


    /**
     * CleanUp finale eseguito solo una volta alla chiusura della classe
     */
    @AfterClass
    public static void cleanUpFinaleStaticoEseguitoSoloUnaVoltaAllaChiusuraDellaClasse() {
        WamTest.cleanUpClass();
    } // end of cleaup statico finale della classe


    /**
     * SetUp eseguito prima dell'esecuzione di ogni metodo
     */
    @Before
    public void setUp() {
        super.setUp();
    } // end of setup prima di ogni metodo di test


    /**
     * CleanUp eseguito dopo l'esecuzione di ogni metodo
     */
    @After
    public void cleanUp() {
        super.cleanUp();
    } // end of cleaup dopo ogni metodo di test

    /**
     * Azzera le variabili d'istanza
     */
    protected void reset() {
        vol = null;
        super.reset();
    }// end of method

    /**
     * Prima di inizare a creare e modificare le funzioni, cancello tutte le (eventuali) precedenti
     * Alla fine, cancello tutte le funzioni create
     */
    protected void cancellaRecords() {
        Volontario.deleteAll(MANAGER);
    }// end of method


    /**
     * Crea alcuni records temporanei per effettuare le prove delle query
     * Uso la Entity
     * Creo alcuni nuovi records nel DB alternativo (WAMTEST)
     */
    protected void creaRecords() {

        //--prima company
        vol = new Volontario(companyUno, nome1, cognome1);
        singoloRecordPrimaCompany(vol);

        vol = new Volontario(companyUno, nome2, cognome2);
        singoloRecordPrimaCompany(vol);

        vol = new Volontario(companyUno, nome3, cognome3);
        singoloRecordPrimaCompany(vol);

        vol = new Volontario(companyUno, nome1, cognome4);
        singoloRecordPrimaCompany(vol);

        //--seconda company
        vol = new Volontario(companyDue, nome1, cognome1);
        singoloRecordSecondaCompany(vol);

        vol = new Volontario(companyDue, nome2, cognome1);
        singoloRecordSecondaCompany(vol);

        vol = new Volontario(companyDue, nome3, cognome2);
        singoloRecordSecondaCompany(vol);

        vol = new Volontario(companyDue, nome4, cognome3);
        singoloRecordSecondaCompany(vol);

    }// end of single test

    @Test
    public void costruttoreMinimo() {
        //-cancello i records per riprovare qui
        cancellaRecords();

        //--nessuna company corrente
        //--crea l'istanza ma non la registra
        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        if (companyCorrente == null) {
            vol = new Volontario(nome1, cognome1);
            assertNotNull(vol);
            vol = (Volontario) vol.save();
            assertNull(vol);
        }// end of if cycle

        //--costruttore senza argomenti
        //--crea l'istanza ma non la registra
        vol = new Volontario();
        assertNotNull(vol);
        vol = (Volontario) vol.save();
        assertNull(vol);

        //--nessuna company passata come pareametro
        //--crea l'istanza ma non la registra
        vol = new Volontario(null, nome1, cognome1);
        assertNotNull(vol);
        vol = (Volontario) vol.save();
        assertNull(vol);

        //--prima company
        numPrevisto = Volontario.countByCompany(companyUno, MANAGER);

        // senza nessun parametro
        vol = new Volontario();
        costruttoreNullo(vol);

        // parametro obbligatorio vuoto
        vol = new Volontario(null, nome1, cognome1);
        costruttoreNullo(vol);

        // parametro obbligatorio vuoto
        vol = new Volontario(companyUno, "", cognome1);
        costruttoreNullo(vol);

        // parametro obbligatorio vuoto
        vol = new Volontario(companyUno, nome1, "");
        costruttoreNullo(vol);

        // parametro obbligatorio vuoto
        vol = new Volontario(null, "", "");
        costruttoreNullo(vol);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        vol = new Volontario(companyUno, nome1, cognome1);
        costruttoreValidoPrimaCompany(vol, numPrevisto);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        vol = new Volontario(companyUno, nome2, cognome2);
        costruttoreValidoPrimaCompany(vol, numPrevisto);

        // campo unico, doppio
        vol = new Volontario(companyUno, nome1, cognome1);
        costruttoreNullo(vol);

    }// end of single test

//    @Test
    // Find entity

    /**
     * Ricerca una funzione
     */
//    public void cercaVolontario() {
//        resetVolontari();
//        long key;
//        int numRecords = Volontario.countByAllCompanies(MANAGER);
//        if (numRecords < 1) {
//            return;
//        }// end of if cycle
//
//        Container.Filter filterCompany = new Compare.Equal(Volontario_.company.getName(), companyUno);
//        Container.Filter filterNome = new Compare.Equal(Volontario_.nome.getName(), NOME_UNO);
//        Container.Filter filterCognome = new Compare.Equal(Volontario_.cognome.getName(), COGNOME_UNO);
//        List<? extends BaseEntity> listaBE = AQuery.getList(Volontario.class, MANAGER,filterCompany);
//        List<? extends BaseEntity> listaBEw = AQuery.getList(Volontario.class, MANAGER,filterNome);
//        List<? extends BaseEntity> listaBEr = AQuery.getList(Volontario.class, MANAGER,filterCognome);
//        List<? extends BaseEntity> pippoz = AQuery.getList(Volontario.class, MANAGER,filterCompany,filterCognome);
//
//        volontarioUno = Volontario.findByCompanyAndNomeAndCognome(companyUno, NOME_UNO, COGNOME_UNO, MANAGER);
//        assertNotNull(volontarioUno);
//
//        volontarioDue = Volontario.findByCompanyAndNomeAndCognome(companyDue, NOME_DUE, COGNOME_DUE, MANAGER);
//        assertNotNull(volontarioDue);
//        assertNotSame(volontarioDue, volontarioUno);
//        key = volontarioDue.getId();
//
//        volontarioTre = Volontario.findByCompanyAndNomeAndCognome(companyUno, NOME_DUE, COGNOME_DUE, MANAGER);
//        assertNotNull(volontarioTre);
//        assertNotSame(volontarioTre, volontarioDue);
//
//        volontarioQuattro = Volontario.find(key, MANAGER);
//        assertNotNull(volontarioQuattro);
//        assertNotSame(volontarioQuattro, volontarioTre);
//        assertEquals(volontarioQuattro, volontarioDue);
//    }// end of single test

//    @Test
    // Find list

    /**
     * Ricerca una lista
     */
    public void cercaLista() {
        resetVolontari();
        int numRecords = Volontario.countByAllCompanies(MANAGER);
        if (numRecords < 1) {
            return;
        }// end of if cycle

//        listaUno = Volontario.findByAllCompanies(MANAGER);
//        assertNotNull(listaUno);
//        assertEquals(listaUno.size(), numRecords);
//
//        listaDue = Volontario.findBySingleCompany(companyUno, MANAGER);
//        assertNotNull(listaDue);
//
//        listaTre = Volontario.findBySingleCompany(companyDue, MANAGER);
//        assertNotNull(listaTre);
    }// end of single test


//    @Test
    // New and save

//    public void creaVolontario() {
//        resetVolontari();
//        int numRecTotaliOld = Volontario.countByAllCompanies(MANAGER);
//        int numRetTotaliNew;
//        int numRecUnoOld = Volontario.countByCompany(companyUno, MANAGER);
//        int numRecUnoNew;
//        int numRecDueOld = Volontario.countByCompany(companyDue, MANAGER);
//        int numRecDueNew;
//        int ordine;
//
//        // senza un parametro obbligatorio
//        try { // prova ad eseguire il codice
//            volontarioUno = Volontario.crea(null, sigla1, desc1, MANAGER);
//            volontarioUno.save(MANAGER);
//        } catch (Exception unErrore) { // intercetta l'errore
//        }// fine del blocco try-catch
//        assertNull(volontarioUno);
//        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
//        assertEquals(numRecUnoNew, numRecUnoOld);
//
//        // parametri obbligatori
//        volontarioUno = Volontario.crea(companyUno, sigla1, desc1, MANAGER);
//        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
//        assertEquals(numRecUnoNew, numRecUnoOld + 1);
//
//        // parametri obbligatori
//        volontarioDue = Volontario.crea(companyUno, sigla2, desc2, MANAGER);
//        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
//        assertEquals(numRecUnoNew, numRecUnoOld + 2);
//
////         tutti i parametri previsti
////        volontarioTre = Volontario.crea(companyDue, SIGLA_UNO, DESCRIZIONE_UNO, 6, FontAwesome.USER, MANAGER);
////        numRecUnoNew = Volontario.countBySingleCompany(companyDue, MANAGER);
////        assertEquals(numRecUnoNew, numRecDueOld + 1);
//
//        // parametri obbligatori
//        volontarioQuattro = Volontario.crea(companyDue, sigla2, desc2, MANAGER);
//        numRecDueNew = Volontario.countByCompany(companyDue, MANAGER);
//        assertEquals(numRecDueNew, numRecDueOld + 2);
//
//        // campo unico, doppio
//        volontarioCinque = Volontario.crea(companyUno, sigla1, desc1, MANAGER);
//        try { // prova ad eseguire il codice
//            volontarioCinque.save(companyUno, MANAGER);
//        } catch (Exception unErrore) { // intercetta l'errore
//            System.out.println(unErrore.toString());
//        }// fine del blocco try-catch
//        assertNotNull(volontarioCinque);
//        assertNotNull(volontarioCinque.getId());
//        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
//
//        assertEquals(Volontario.countByCompany(companyUno, MANAGER), numRecUnoOld + 2);
//        assertEquals(Volontario.countByCompany(companyDue, MANAGER), numRecDueOld + 2);
//        numRetTotaliNew = Volontario.countByAllCompanies(MANAGER);
//        assertEquals(numRetTotaliNew, numRecTotaliOld + 4);
//    }// end of single test


    private void singoloRecordPrimaCompany(Volontario vol) {
        singoloRecord(vol);
        chiaviUno.add(vol.getId());
        listaUno.add(vol);
    }// end of method

    private void singoloRecordSecondaCompany(Volontario vol) {
        singoloRecord(vol);
        chiaviDue.add(vol.getId());
        listaDue.add(vol);
    }// end of method

    private void singoloRecord(Volontario vol) {
        vol.save(MANAGER);
        chiavi.add(vol.getId());
        lista.add(vol);
        listaCodeCompanyUnici.add(vol.getCodeCompanyUnico());
    }// end of method

    private void costruttoreNullo(Volontario vol) {
        costruttoreNullo(vol, 0);
    }// end of method

    private void costruttoreNullo(Volontario vol, int numPrevisto) {
        try { // prova ad eseguire il codice
            vol.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(vol);
        assertNull(vol.getId());
        numOttenuto = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValidoPrimaCompany(Volontario vol, int numPrevisto) {
        costruttoreValido(vol);
        numOttenuto = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValidoSecondaCompany(Volontario vol, int numPrevisto) {
        costruttoreValido(vol);
        numOttenuto = Volontario.countByCompany(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValido(Volontario vol) {
        try { // prova ad eseguire il codice
            vol.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        assertNotNull(vol);
        assertNotNull(vol.getId());
    }// end of method

    /**
     * Annulla le variabili d'istanza
     */
    private void resetVolontari() {
//        volontarioUno = null;
//        volontarioDue = null;
//        volontarioTre = null;
//        volontarioQuattro = null;
//        volontarioCinque = null;
    } // end of cleaup finale


}// end of test class