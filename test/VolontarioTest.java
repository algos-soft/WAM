import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.junit.*;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gac on 11 set 2016.
 * .
 */
public class VolontarioTest extends WamTest {

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
        // creazione del MANAGER statico per questa singola classe di test
        // creazione di alcune company
        WamTest.setUpClass();

        // Prima di iniziare a creare e modificare i volontari, cancello tutte gli (eventuali) precedenti
//        cancellaVolontari();
    } // end of setup iniziale


    /**
     * CleanUp finale eseguito solo una volta alla chiusura della classe
     */
    @AfterClass
    public static void cleanUpFinaleStaticoEseguitoAllaChiusuraDellaClasse() {
        // Alla fine, cancello tutte i volontari creati
        cancellaVolontari();

    } // end of cleaup finale

    /**
     * Prima di iniziare a creare e modificare i volontari, cancello tutte gli (eventuali) precedenti
     * Alla fine, cancello tutte i volontari creati
     */
    private static void cancellaVolontari() {
        Volontario.deleteAll(MANAGER);
    } // end of cleaup finale

    @Before
    public void setUpVolontari() {
        resetVolontari();
    } // end of setup iniziale

    @After
    public void reset() {
    } // end of cleaup finale


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
        int numRecUnoOld = Volontario.countBySingleCompany(companyUno, MANAGER);
        int numRecUnoNew;
        int numRecDueOld = Volontario.countBySingleCompany(companyDue, MANAGER);
        int numRecDueNew;
        int ordine;

        cancellaVolontari();
        // senza nessun parametro
        volontarioUno = new Volontario();
        try { // prova ad eseguire il codice
            volontarioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(volontarioUno);
        assertNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // senza un parametro obbligatorio
        volontarioUno = new Volontario(null, NOME_UNO, COGNOME_UNO);
        try { // prova ad eseguire il codice
            volontarioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(volontarioUno);
        assertNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        volontarioUno = new Volontario(companyUno, "", COGNOME_UNO);
        try { // prova ad eseguire il codice
            volontarioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(volontarioUno);
        assertNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        volontarioUno = new Volontario(companyUno, NOME_UNO, "");
        try { // prova ad eseguire il codice
            volontarioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(volontarioUno);
        assertNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        volontarioUno = new Volontario(companyUno, "", "");
        try { // prova ad eseguire il codice
            volontarioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(volontarioUno);
        assertNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametri obbligatori
        volontarioUno = new Volontario(companyUno, NOME_UNO, COGNOME_UNO);
        volontarioUno.save(companyUno, MANAGER);
        assertNotNull(volontarioUno);
        assertNotNull(volontarioUno.getId());
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 1);

        // parametri obbligatori
        volontarioDue = new Volontario(companyUno, NOME_DUE, COGNOME_DUE);
        volontarioDue.save(companyUno, MANAGER);
        assertNotNull(volontarioDue);
        assertNotNull(volontarioDue.getId());
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 2);

        // tutti i parametri previsti
        volontarioTre = new Volontario(companyDue, NOME_UNO, COGNOME_UNO, new Date(), "", false);
        volontarioTre.save(companyDue, MANAGER);
        assertNotNull(volontarioTre);
        assertNotNull(volontarioTre.getId());
        numRecUnoNew = Volontario.countBySingleCompany(companyDue, MANAGER);
        assertEquals(numRecUnoNew, numRecDueOld + 1);

        // parametri obbligatori
        volontarioQuattro = new Volontario(companyDue, NOME_DUE, COGNOME_DUE);
        volontarioQuattro.save(companyDue, MANAGER);
        assertNotNull(volontarioQuattro);
        assertNotNull(volontarioQuattro.getId());
        numRecDueNew = Volontario.countBySingleCompany(companyDue, MANAGER);
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
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);
        assertEquals(numRecDueNew, numRecDueOld + 2);

        assertEquals(Volontario.countBySingleCompany(companyUno, MANAGER), numRecUnoOld + 2);
        assertEquals(Volontario.countBySingleCompany(companyDue, MANAGER), numRecDueOld + 2);
        numRetTotaliNew = Volontario.countByAllCompanies(MANAGER);
        assertEquals(numRetTotaliNew, numRecTotaliOld + 4);
    }// end of single test

    @Test
    // Find entity

    /**
     * Ricerca una funzione
     */
    public void cercaVolontario() {
        resetVolontari();
        long key;
        int numRecords = Volontario.countByAllCompanies(MANAGER);
        if (numRecords < 1) {
            return;
        }// end of if cycle

        Container.Filter filterCompany = new Compare.Equal(Volontario_.company.getName(), companyUno);
        Container.Filter filterNome = new Compare.Equal(Volontario_.nome.getName(), NOME_UNO);
        Container.Filter filterCognome = new Compare.Equal(Volontario_.cognome.getName(), COGNOME_UNO);
        List<? extends BaseEntity> listaBE = AQuery.getList(Volontario.class, MANAGER,filterCompany);
        List<? extends BaseEntity> listaBEw = AQuery.getList(Volontario.class, MANAGER,filterNome);
        List<? extends BaseEntity> listaBEr = AQuery.getList(Volontario.class, MANAGER,filterCognome);
        List<? extends BaseEntity> pippoz = AQuery.getList(Volontario.class, MANAGER,filterCompany,filterCognome);

        volontarioUno = Volontario.findByCompanyAndNomeAndCognome(companyUno, NOME_UNO, COGNOME_UNO, MANAGER);
        assertNotNull(volontarioUno);

        volontarioDue = Volontario.findByCompanyAndNomeAndCognome(companyDue, NOME_DUE, COGNOME_DUE, MANAGER);
        assertNotNull(volontarioDue);
        assertNotSame(volontarioDue, volontarioUno);
        key = volontarioDue.getId();

        volontarioTre = Volontario.findByCompanyAndNomeAndCognome(companyUno, NOME_DUE, COGNOME_DUE, MANAGER);
        assertNotNull(volontarioTre);
        assertNotSame(volontarioTre, volontarioDue);

        volontarioQuattro = Volontario.find(key, MANAGER);
        assertNotNull(volontarioQuattro);
        assertNotSame(volontarioQuattro, volontarioTre);
        assertEquals(volontarioQuattro, volontarioDue);
    }// end of single test

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

    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       di riferimento interna (obbligatoria)
     * @param descrizione (obbligatoria)
     * @param ordine      di presentazione nelle liste
     * @param glyph       dell'icona (facoltativo)
     * @param MANAGER     the EntityManager to use
     * @return istanza della Entity
     */
    public void creaVolontario() {
        resetVolontari();
        cancellaVolontari();
        int numRecTotaliOld = Volontario.countByAllCompanies(MANAGER);
        int numRetTotaliNew;
        int numRecUnoOld = Volontario.countBySingleCompany(companyUno, MANAGER);
        int numRecUnoNew;
        int numRecDueOld = Volontario.countBySingleCompany(companyDue, MANAGER);
        int numRecDueNew;
        int ordine;

        // senza un parametro obbligatorio
        try { // prova ad eseguire il codice
            volontarioUno = Volontario.crea(null, sigla1, desc1, MANAGER);
            volontarioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(volontarioUno);
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametri obbligatori
        volontarioUno = Volontario.crea(companyUno, sigla1, desc1, MANAGER);
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 1);

        // parametri obbligatori
        volontarioDue = Volontario.crea(companyUno, sigla2, desc2, MANAGER);
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 2);

//         tutti i parametri previsti
//        volontarioTre = Volontario.crea(companyDue, SIGLA_UNO, DESCRIZIONE_UNO, 6, FontAwesome.USER, MANAGER);
//        numRecUnoNew = Volontario.countBySingleCompany(companyDue, MANAGER);
//        assertEquals(numRecUnoNew, numRecDueOld + 1);

        // parametri obbligatori
        volontarioQuattro = Volontario.crea(companyDue, sigla2, desc2, MANAGER);
        numRecDueNew = Volontario.countBySingleCompany(companyDue, MANAGER);
        assertEquals(numRecDueNew, numRecDueOld + 2);

        // campo unico, doppio
        volontarioCinque = Volontario.crea(companyUno, sigla1, desc1, MANAGER);
        try { // prova ad eseguire il codice
            volontarioCinque.save(companyUno, MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println(unErrore.toString());
        }// fine del blocco try-catch
        assertNotNull(volontarioCinque);
        assertNotNull(volontarioCinque.getId());
        numRecUnoNew = Volontario.countBySingleCompany(companyUno, MANAGER);

        assertEquals(Volontario.countBySingleCompany(companyUno, MANAGER), numRecUnoOld + 2);
        assertEquals(Volontario.countBySingleCompany(companyDue, MANAGER), numRecDueOld + 2);
        numRetTotaliNew = Volontario.countByAllCompanies(MANAGER);
        assertEquals(numRetTotaliNew, numRecTotaliOld + 4);
    }// end of single test


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