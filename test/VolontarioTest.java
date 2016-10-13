import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.junit.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gac on 11 set 2016.
 * .
 */
public class VolontarioTest extends WamTest {

    private Volontario vol;

    Volontario volontarioUno;
    Volontario volontarioDue;
    Volontario volontarioTre;
    Volontario volontarioQuattro;
    Volontario volontarioCinque;

    List<Volontario> listaUno;
    List<Volontario> listaDue;
    List<Volontario> listaTre;


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
        chiaviUno = new ArrayList<>();
        chiaviDue = new ArrayList<>();
        super.reset();
    }// end of method

    /**
     * Prima di inizare a creare e modificare le funzioni, cancello tutte le (eventuali) precedenti
     * Alla fine, cancello tutte le funzioni create
     */
    protected void cancellaRecords() {
        Volontario.deleteAll(MANAGER);
    }// end of method


    @Test
    // Constructors
    // Count records
    /**
     * Crea un volontario
     * Controlla che ci siano i parametri obbligatori
     */
    public void nuovaVolontario() {
        resetVolontari();
        int numRecTotaliOld = Volontario.countByAllCompanies(MANAGER);
        int numRetTotaliNew;
        int numRecUnoOld = Volontario.countByCompany(companyUno, MANAGER);
        int numRecUnoNew;
        int numRecDueOld = Volontario.countByCompany(companyDue, MANAGER);
        int numRecDueNew;
        int ordine;

        // senza nessun parametro
        volontarioUno = new Volontario();
        try { // prova ad eseguire il codice
            volontarioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(volontarioUno);
        assertNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // senza un parametro obbligatorio
        volontarioUno = new Volontario(null, NOME_UNO, COGNOME_UNO);
        try { // prova ad eseguire il codice
            volontarioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(volontarioUno);
        assertNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        volontarioUno = new Volontario(companyUno, "", COGNOME_UNO);
        try { // prova ad eseguire il codice
            volontarioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(volontarioUno);
        assertNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        volontarioUno = new Volontario(companyUno, NOME_UNO, "");
        try { // prova ad eseguire il codice
            volontarioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(volontarioUno);
        assertNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        volontarioUno = new Volontario(companyUno, "", "");
        try { // prova ad eseguire il codice
            volontarioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(volontarioUno);
        assertNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametri obbligatori
        volontarioUno = new Volontario(companyUno, NOME_UNO, COGNOME_UNO);
        volontarioUno.save(companyUno, MANAGER);
        assertNotNull(volontarioUno);
        assertNotNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 1);

        // parametri obbligatori
        volontarioDue = new Volontario(companyUno, NOME_DUE, COGNOME_DUE);
        volontarioDue.save(companyUno, MANAGER);
        assertNotNull(volontarioDue);
        assertNotNull(volontarioDue.getId());
        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 2);

        // tutti i parametri previsti
        volontarioTre = new Volontario(companyDue, NOME_UNO, COGNOME_UNO, new Date(), "", false);
        volontarioTre.save(companyDue, MANAGER);
        assertNotNull(volontarioTre);
        assertNotNull(volontarioTre.getId());
        numRecUnoNew = Volontario.countByCompany(companyDue, MANAGER);
        assertEquals(numRecUnoNew, numRecDueOld + 1);

        // parametri obbligatori
        volontarioQuattro = new Volontario(companyDue, NOME_DUE, COGNOME_DUE);
        volontarioQuattro.save(companyDue, MANAGER);
        assertNotNull(volontarioQuattro);
        assertNotNull(volontarioQuattro.getId());
        numRecDueNew = Volontario.countByCompany(companyDue, MANAGER);
        assertEquals(numRecDueNew, numRecDueOld + 2);

        // campo unico, doppio
        volontarioCinque = new Volontario(companyUno, NOME_UNO, COGNOME_UNO);
        try { // prova ad eseguire il codice
            volontarioCinque.save(companyUno, MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println(unErrore.toString());
        }// fine del blocco try-catch
        assertNotNull(volontarioCinque);
        assertNull(volontarioCinque.getId());
        numRecUnoNew = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numRecDueNew, numRecDueOld + 2);

        assertEquals(Volontario.countByCompany(companyUno, MANAGER), numRecUnoOld + 2);
        assertEquals(Volontario.countByCompany(companyDue, MANAGER), numRecDueOld + 2);
        numRetTotaliNew = Volontario.countByAllCompanies(MANAGER);
        assertEquals(numRetTotaliNew, numRecTotaliOld + 4);
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

        listaUno = Volontario.findByAllCompanies(MANAGER);
        assertNotNull(listaUno);
        assertEquals(listaUno.size(), numRecords);

        listaDue = Volontario.findBySingleCompany(companyUno, MANAGER);
        assertNotNull(listaDue);

        listaTre = Volontario.findBySingleCompany(companyDue, MANAGER);
        assertNotNull(listaTre);
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


    /**
     * Annulla le variabili d'istanza
     */
    private void resetVolontari() {
        volontarioUno = null;
        volontarioDue = null;
        volontarioTre = null;
        volontarioQuattro = null;
        volontarioCinque = null;
    } // end of cleaup finale


}// end of test class