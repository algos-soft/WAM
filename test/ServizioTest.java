import it.algos.wam.entity.servizio.Servizio;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gac on 08 set 2016.
 * .
 */
public class ServizioTest extends WamTest {

    Servizio servizioUno;
    Servizio servizioDue;
    Servizio servizioTre;
    Servizio servizioQuattro;
    Servizio servizioCinque;

    List<Servizio> listaUno;
    List<Servizio> listaDue;
    List<Servizio> listaTre;

    /**
     * SetUp iniziale eseguito solo una volta alla creazione della classe
     */
    @BeforeClass
    public static void setUpInizialeStaticoEseguitoSoloUnaVoltaAllaCreazioneDellaClasse() {
        // creazione del MANAGER statico per questa singola classe di test
        // creazione di alcune company

        // Prima di inizare a creare e modificare i servizi, cancello tutte le (eventuali) precedenti
        cancellaServizi();
    } // end of setup iniziale


    /**
     * CleanUp finale eseguito solo una volta alla chiusura della classe
     */
    @AfterClass
    public static void cleanUpFinaleStaticoEseguitoAllaChiusuraDellaClasse() {
        // Alla fine, cancello tutte le funzioni create
        cancellaServizi();

    } // end of cleaup finale

    /**
     * Prima di inizare a creare e modificare i servizi, cancello tutte gli (eventuali) precedenti
     * Alla fine, cancello tutte le funzioni create
     */
    private static void cancellaServizi() {
        Servizio.deleteAll(MANAGER);
    } // end of cleaup finale

    @Before
    public void setUpServizi() {
        resetServizi();
    } // end of setup iniziale

    @After
    public void reset() {
    } // end of cleaup finale


//    @Test
    // Constructors
    // Count records
    /**
     * Crea una funzione
     * Controlla che ci siano i parametri obbligatori
     * Non riesco a controllare l'inserimento automatico dell'ordine di presentazione
     */
    public void nuovoServizio() {
        resetServizi();
        int numRecTotaliOld = Servizio.countByAllCompanies(MANAGER);
        int numRetTotaliNew;
        int numRecUnoOld = Servizio.countByCompany(companyUno, MANAGER);
        int numRecUnoNew;
        int numRecDueOld = Servizio.countByCompany(companyDue, MANAGER);
        int numRecDueNew;
        int ordine;

        // senza nessun parametro
        servizioUno = new Servizio();
        try { // prova ad eseguire il codice
            servizioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(servizioUno);
        assertNull(servizioUno.getId());
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // senza un parametro obbligatorio
        servizioUno = new Servizio(null, sigla1, desc1);
        try { // prova ad eseguire il codice
            servizioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(servizioUno);
        assertNull(servizioUno.getId());
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        servizioUno = new Servizio(companyUno, "", desc1);
        try { // prova ad eseguire il codice
            servizioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(servizioUno);
        assertNull(servizioUno.getId());
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        servizioUno = new Servizio(companyUno, sigla1, "");
        try { // prova ad eseguire il codice
            servizioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(servizioUno);
        assertNull(servizioUno.getId());
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametro obbligatorio vuoto
        servizioUno = new Servizio(companyUno, "", "");
        try { // prova ad eseguire il codice
            servizioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(servizioUno);
        assertNull(servizioUno.getId());
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametri obbligatori
        servizioUno = new Servizio(companyUno, sigla1, desc1);
        servizioUno.save(companyUno, MANAGER);
        assertNotNull(servizioUno);
        assertNotNull(servizioUno.getId());
        ordine = servizioUno.getOrdine();
        assertEquals(ordine, 1);
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 1);

        // parametri obbligatori
        servizioDue = new Servizio(companyUno, sigla2, desc2);
        servizioDue.save(companyUno, MANAGER);
        assertNotNull(servizioDue);
        assertNotNull(servizioDue.getId());
        ordine = servizioDue.getOrdine();
        assertEquals(ordine, 2);
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 2);

        // tutti i parametri previsti
        servizioTre = new Servizio(companyDue, sigla1, desc1, 6, 0);
        servizioTre.save(companyDue, MANAGER);
        assertNotNull(servizioTre);
        assertNotNull(servizioTre.getId());
        ordine = servizioTre.getOrdine();
        assertEquals(ordine, 6);
        numRecUnoNew = Servizio.countByCompany(companyDue, MANAGER);
        assertEquals(numRecUnoNew, numRecDueOld + 1);

        // parametri obbligatori
        servizioQuattro = new Servizio(companyDue, sigla2, desc2);
        servizioQuattro.save(companyDue, MANAGER);
        assertNotNull(servizioQuattro);
        assertNotNull(servizioQuattro.getId());
        ordine = servizioQuattro.getOrdine();
        assertEquals(ordine, 7);
        numRecDueNew = Servizio.countByCompany(companyDue, MANAGER);
        assertEquals(numRecDueNew, numRecDueOld + 2);

        // campo unico, doppio
        servizioCinque = new Servizio(companyUno, sigla1, desc1);
        try { // prova ad eseguire il codice
            servizioCinque.save(companyUno, MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println(unErrore.toString());
        }// fine del blocco try-catch
        assertNotNull(servizioCinque);
        assertNull(servizioCinque.getId());
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numRecDueNew, numRecDueOld + 2);

        assertEquals(Servizio.countByCompany(companyUno, MANAGER), numRecUnoOld + 2);
        assertEquals(Servizio.countByCompany(companyDue, MANAGER), numRecDueOld + 2);
        numRetTotaliNew = Servizio.countByAllCompanies(MANAGER);
        assertEquals(numRetTotaliNew, numRecTotaliOld + 4);
    }// end of single test


//    @Test
    // Find entity
    /**
     * Ricerca una funzione
     */
    public void cercaServizio() {
        resetServizi();
        long key;
        int numRecords = Servizio.countByAllCompanies(MANAGER);
        if (numRecords < 1) {
            return;
        }// end of if cycle

        servizioUno = Servizio.getEntityByCompanyAndSigla(companyUno, sigla1, MANAGER);
        assertNotNull(servizioUno);

        servizioDue = Servizio.getEntityByCompanyAndSigla(companyDue, sigla2, MANAGER);
        assertNotNull(servizioDue);
        assertNotSame(servizioDue, servizioUno);
        key = servizioDue.getId();

        servizioTre = Servizio.getEntityByCompanyAndSigla(companyUno, sigla2, MANAGER);
        assertNotNull(servizioTre);
        assertNotSame(servizioTre, servizioDue);

        servizioQuattro = Servizio.find(key, MANAGER);
        assertNotNull(servizioQuattro);
        assertNotSame(servizioQuattro, servizioTre);
        assertEquals(servizioQuattro, servizioDue);
    }// end of single test

//    @Test
    // Find list
    /**
     * Ricerca una lista
     */
    public void cercaLista() {
        resetServizi();
        int numRecords = Servizio.countByAllCompanies(MANAGER);
        if (numRecords < 1) {
            return;
        }// end of if cycle

        listaUno = Servizio.getListByAllCompanies(MANAGER);
        assertNotNull(listaUno);
        assertEquals(listaUno.size(), numRecords);

        listaDue = Servizio.getListBySingleCompany(companyUno, MANAGER);
        assertNotNull(listaDue);

        listaTre = Servizio.getListBySingleCompany(companyDue, MANAGER);
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
     * @param manager     the EntityManager to use
     * @return istanza della Entity
     */
    public void creaServizio() {
        resetServizi();
        cancellaServizi();
        int numRecTotaliOld = Servizio.countByAllCompanies(MANAGER);
        int numRetTotaliNew;
        int numRecUnoOld = Servizio.countByCompany(companyUno, MANAGER);
        int numRecUnoNew;
        int numRecDueOld = Servizio.countByCompany(companyDue, MANAGER);
        int numRecDueNew;
        int ordine;

        // senza un parametro obbligatorio
        try { // prova ad eseguire il codice
            servizioUno = Servizio.crea(null, sigla1, desc1, MANAGER);
            servizioUno.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(servizioUno);
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld);

        // parametri obbligatori
        servizioUno = Servizio.crea(companyUno, sigla1, desc1, MANAGER);
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 1);
        ordine = servizioUno.getOrdine();
        assertEquals(ordine, 1);

        // parametri obbligatori
        servizioDue = Servizio.crea(companyUno, sigla2, desc2, MANAGER);
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numRecUnoNew, numRecUnoOld + 2);
        ordine = servizioDue.getOrdine();
        assertEquals(ordine, 2);

        // tutti i parametri previsti
        servizioTre = Servizio.crea(companyDue, sigla1, desc1, 6, 0, false, 0, 0, MANAGER);
        numRecUnoNew = Servizio.countByCompany(companyDue, MANAGER);
        assertEquals(numRecUnoNew, numRecDueOld + 1);
        ordine = servizioTre.getOrdine();
        assertEquals(ordine, 6);

        // parametri obbligatori
        servizioQuattro = Servizio.crea(companyDue, sigla2, desc2, MANAGER);
        numRecDueNew = Servizio.countByCompany(companyDue, MANAGER);
        assertEquals(numRecDueNew, numRecDueOld + 2);
        ordine = servizioQuattro.getOrdine();
        assertEquals(ordine, 7);

        // campo unico, doppio
        servizioCinque = Servizio.crea(companyUno, sigla1, desc1, MANAGER);
        try { // prova ad eseguire il codice
            servizioCinque.save(companyUno, MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println(unErrore.toString());
        }// fine del blocco try-catch
        assertNotNull(servizioCinque);
        assertNotNull(servizioCinque.getId());
        numRecUnoNew = Servizio.countByCompany(companyUno, MANAGER);

        assertEquals(Servizio.countByCompany(companyUno, MANAGER), numRecUnoOld + 2);
        assertEquals(Servizio.countByCompany(companyDue, MANAGER), numRecDueOld + 2);
        numRetTotaliNew = Servizio.countByAllCompanies(MANAGER);
        assertEquals(numRetTotaliNew, numRecTotaliOld + 4);
    }// end of single test


    /**
     * Annulla le variabili d'istanza
     */
    private void resetServizi() {
        servizioUno = null;
        servizioDue = null;
        servizioTre = null;
        servizioQuattro = null;
        servizioCinque = null;
    } // end of cleaup finale

}// end of test class