import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.webbase.multiazienda.CompanyQuery;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gac on 01 set 2016.
 * .
 */
public  class FunzioneTest extends WamBaseTest {

    Funzione funzioneUno;
    Funzione funzioneDue;
    Funzione funzioneTre;
    Funzione funzioneQuattro;
    Funzione funzioneCinque;

    List<Funzione> listaUno;
    List<Funzione> listaDue;
    List<Funzione> listaTre;


    /**
     * SetUp iniziale eseguito solo una volta alla creazione della classe
     */
    @BeforeClass
    public static void setUpInizialeStaticoEseguitoSoloUnaVoltaAllaCreazioneDellaClasse() {
        // creazione del MANAGER statico per questa singola classe di test
        // creazione di alcune company
        setUp();

        // Prima di inizare a creare e modificare le funzioni, cancello tutte le (eventuali) precedenti
        cancellaFunzioni();
    } // end of setup iniziale


    /**
     * CleanUp finale eseguito solo una volta alla chiusura della classe
     */
    @AfterClass
    public static void cleanUpFinaleStaticoEseguitoAllaChiusuraDellaClasse() {
        // Alla fine, cancello tutte le funzioni create
        cancellaFunzioni();

        cleanUp();
    } // end of cleaup finale

    /**
     * Prima di inizare a creare e modificare le funzioni, cancello tutte le (eventuali) precedenti
     * Alla fine, cancello tutte le funzioni create
     */
    private static void cancellaFunzioni() {
        Funzione.deleteAll(MANAGER);
        CompanyQuery.delete(Funzione.class, MANAGER);
int a=87;
    } // end of cleaup finale

    @Before
    public void setUpFunzioni() {
        resetFunzioni();
    } // end of setup iniziale

    @After
    public void reset() {
    } // end of cleaup finale

    @Test
    // Constructors
    // Count records
    /**
     * Crea una funzione
     * Controlla che ci siano i parametri obbligatori
     * Non riesco a controllare l'inserimento automatico dell'ordine di presentazione
     */
    public void nuovaFunzione() {
        resetFunzioni();
        int numRecTotaliOld = Funzione.countByAllCompanies(MANAGER);
        int numRetTotaliNew;
        int numRecUnoOld = Funzione.countByCompany(companyUno, MANAGER);
        int numRecUnoNew;
        int numRecDueOld = Funzione.countByCompany(companyDue, MANAGER);
        int numRecDueNew;
        int ordine;

        // senza nessun parametro
        funzioneUno = new Funzione();
        try { // prova ad eseguire il codice
            funzioneUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(funzioneUno);
        assertNull(funzioneUno.getId());
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // senza un parametro obbligatorio
        funzioneUno = new Funzione(null, SIGLA_UNO, DESCRIZIONE_UNO);
        try { // prova ad eseguire il codice
            funzioneUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(funzioneUno);
        assertNull(funzioneUno.getId());
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        funzioneUno = new Funzione(companyUno, "", DESCRIZIONE_UNO);
        try { // prova ad eseguire il codice
            funzioneUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(funzioneUno);
        assertNull(funzioneUno.getId());
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        funzioneUno = new Funzione(companyUno, SIGLA_UNO, "");
        try { // prova ad eseguire il codice
            funzioneUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(funzioneUno);
        assertNull(funzioneUno.getId());
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        funzioneUno = new Funzione(companyUno, "", "");
        try { // prova ad eseguire il codice
            funzioneUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(funzioneUno);
        assertNull(funzioneUno.getId());
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametri obbligatori
        funzioneUno = new Funzione(companyUno, SIGLA_UNO, DESCRIZIONE_UNO);
        funzioneUno.save(companyUno, MANAGER);
        assertNotNull(funzioneUno);
        assertNotNull(funzioneUno.getId());
        ordine = funzioneUno.getOrdine();
        assertEquals(ordine, 1);
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 1);

        // parametri obbligatori
        funzioneDue = new Funzione(companyUno, SIGLA_DUE, DESCRIZIONE_DUE);
        funzioneDue.save(companyUno, MANAGER);
        assertNotNull(funzioneDue);
        assertNotNull(funzioneDue.getId());
        ordine = funzioneDue.getOrdine();
        assertEquals(ordine, 2);
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 2);

        // tutti i parametri previsti
        funzioneTre = new Funzione(companyDue, SIGLA_UNO, DESCRIZIONE_UNO, 6, FontAwesome.USER);
        funzioneTre.save(companyDue, MANAGER);
        assertNotNull(funzioneTre);
        assertNotNull(funzioneTre.getId());
        ordine = funzioneTre.getOrdine();
        assertEquals(ordine, 6);
        numRecUnoNew = Funzione.countByCompany(companyDue, MANAGER);
        assertEquals(numRecUnoNew, numRecDueOld + 1);

        // parametri obbligatori
        funzioneQuattro = new Funzione(companyDue, SIGLA_DUE, DESCRIZIONE_DUE);
        funzioneQuattro.save(companyDue, MANAGER);
        assertNotNull(funzioneQuattro);
        assertNotNull(funzioneQuattro.getId());
        ordine = funzioneQuattro.getOrdine();
        assertEquals(ordine, 7);
        numRecDueNew = Funzione.countByCompany(companyDue, MANAGER);
        assertEquals(numRecDueNew, numRecDueOld + 2);

        // campo unico, doppio
        funzioneCinque = new Funzione(companyUno, SIGLA_UNO, DESCRIZIONE_UNO);
        try { // prova ad eseguire il codice
            funzioneCinque.save(companyUno, MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println(unErrore.toString());
        }// fine del blocco try-catch
        assertNotNull(funzioneCinque);
        assertNull(funzioneCinque.getId());
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numRecDueNew, numRecDueOld + 2);

        assertEquals(Funzione.countByCompany(companyUno, MANAGER), numRecUnoOld + 2);
        assertEquals(Funzione.countByCompany(companyDue, MANAGER), numRecDueOld + 2);
        numRetTotaliNew = Funzione.countByAllCompanies(MANAGER);
        assertEquals(numRetTotaliNew, numRecTotaliOld + 4);
    }// end of single test

    @Test
    // Find entity
    /**
     * Ricerca una funzione
     */
    public void cercaFunzione() {
        resetFunzioni();
        long key;
        int numRecords = Funzione.countByAllCompanies(MANAGER);
        if (numRecords < 1) {
            return;
        }// end of if cycle

        funzioneUno = Funzione.getEntityByCompanyAndBySigla(SIGLA_UNO,companyUno, MANAGER);
        assertNotNull(funzioneUno);

        funzioneDue = Funzione.getEntityByCompanyAndBySigla(SIGLA_DUE,companyDue, MANAGER);
        assertNotNull(funzioneDue);
        assertNotSame(funzioneDue, funzioneUno);
        key = funzioneDue.getId();

        funzioneTre = Funzione.getEntityByCompanyAndBySigla(SIGLA_DUE,companyUno, MANAGER);
        assertNotNull(funzioneTre);
        assertNotSame(funzioneTre, funzioneDue);

        funzioneQuattro = Funzione.find(key, MANAGER);
        assertNotNull(funzioneQuattro);
        assertNotSame(funzioneQuattro, funzioneTre);
        assertEquals(funzioneQuattro, funzioneDue);
    }// end of single test

    @Test
    // Find list
    /**
     * Ricerca una lista
     */
    public void cercaLista() {
        resetFunzioni();
        int numRecords = Funzione.countByAllCompanies(MANAGER);
        if (numRecords < 1) {
            return;
        }// end of if cycle

        listaUno = Funzione.getListByAllCompanies(MANAGER);
        assertNotNull(listaUno);
        assertEquals(listaUno.size(), numRecords);

        listaDue = Funzione.getListBySingleCompany(companyUno, MANAGER);
        assertNotNull(listaDue);

        listaTre = Funzione.getListBySingleCompany(companyDue, MANAGER);
        assertNotNull(listaTre);
    }// end of single test


    @Test
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
    public void creaFunzione() {
        resetFunzioni();
        cancellaFunzioni();
        int numRecTotaliOld = Funzione.countByAllCompanies(MANAGER);
        int numRetTotaliNew;
        int numRecUnoOld = Funzione.countByCompany(companyUno, MANAGER);
        int numRecUnoNew;
        int numRecDueOld = Funzione.countByCompany(companyDue, MANAGER);
        int numRecDueNew;
        int ordine;

        // senza un parametro obbligatorio
        try { // prova ad eseguire il codice
            funzioneUno = Funzione.crea(null, SIGLA_UNO, DESCRIZIONE_UNO, MANAGER);
            funzioneUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(funzioneUno);
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametri obbligatori
        funzioneUno = Funzione.crea(companyUno, SIGLA_UNO, DESCRIZIONE_UNO, MANAGER);
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 1);
        ordine = funzioneUno.getOrdine();
        assertEquals(ordine, 1);

        // parametri obbligatori
        funzioneDue = Funzione.crea(companyUno, SIGLA_DUE, DESCRIZIONE_DUE, MANAGER);
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 2);
        ordine = funzioneDue.getOrdine();
        assertEquals(ordine, 2);

        // tutti i parametri previsti
        funzioneTre = Funzione.crea(companyDue, SIGLA_UNO, DESCRIZIONE_UNO, 6, FontAwesome.USER, MANAGER);
        numRecUnoNew = Funzione.countByCompany(companyDue, MANAGER);
        assertEquals(numRecUnoNew, numRecDueOld + 1);
        ordine = funzioneTre.getOrdine();
        assertEquals(ordine, 6);

        // parametri obbligatori
        funzioneQuattro = Funzione.crea(companyDue, SIGLA_DUE, DESCRIZIONE_DUE, MANAGER);
        numRecDueNew = Funzione.countByCompany(companyDue, MANAGER);
        assertEquals(numRecDueNew, numRecDueOld + 2);
        ordine = funzioneQuattro.getOrdine();
        assertEquals(ordine, 7);

        // campo unico, doppio
        funzioneCinque = Funzione.crea(companyUno, SIGLA_UNO, DESCRIZIONE_UNO, MANAGER);
        try { // prova ad eseguire il codice
            funzioneCinque.save(companyUno, MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println(unErrore.toString());
        }// fine del blocco try-catch
        assertNotNull(funzioneCinque);
        assertNotNull(funzioneCinque.getId());
        numRecUnoNew = Funzione.countByCompany(companyUno, MANAGER);

        assertEquals(Funzione.countByCompany(companyUno, MANAGER), numRecUnoOld + 2);
        assertEquals(Funzione.countByCompany(companyDue, MANAGER), numRecDueOld + 2);
        numRetTotaliNew = Funzione.countByAllCompanies(MANAGER);
        assertEquals(numRetTotaliNew, numRecTotaliOld + 4);
    }// end of single test


    /**
     * Annulla le variabili d'istanza
     */
    private void resetFunzioni() {
        funzioneUno = null;
        funzioneDue = null;
        funzioneTre = null;
        funzioneQuattro = null;
        funzioneCinque = null;
    } // end of cleaup finale


}// end of test class