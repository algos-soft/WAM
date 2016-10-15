import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gac on 08 set 2016.
 * <p>
 * Test per la Entity Servizio
 * I test vengono eseguiti su un DB di prova, usando un apposito EntityManager
 * La Entity usa la multiazienda (company) e quindi estende CompanyEntity e non BaseEntity
 */
public class ServizioTest extends WamTest {

    private Servizio serv;

    private List<Servizio> lista = new ArrayList<>();
    private List<Servizio> listaUno = new ArrayList<>();
    private List<Servizio> listaDue = new ArrayList<>();
    private List<Servizio> listaTre = new ArrayList<>();

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
        serv = null;
        super.reset();
    }// end of method

    /**
     * Prima di inizare a creare e modificare le funzioni, cancello tutte le (eventuali) precedenti
     * Alla fine, cancello tutte le funzioni create
     */
    protected void cancellaRecords() {
        Servizio.deleteAll(MANAGER);
    }// end of method

    /**
     * Crea alcuni records temporanei per effettuare le prove delle query
     * Uso la Entity
     * Creo alcuni nuovi records nel DB alternativo (WAMTEST)
     */
    protected void creaRecords() {

        //--prima company
        serv = new Servizio(companyUno, sigla1, desc1);
        singoloRecordPrimaCompany(serv);

        serv = new Servizio(companyUno, sigla2, desc2);
        singoloRecordPrimaCompany(serv);

        serv = new Servizio(companyUno, sigla3, desc3);
        singoloRecordPrimaCompany(serv);

        serv = new Servizio(companyUno, sigla4, desc1);
        singoloRecordPrimaCompany(serv);

        //--seconda company
        serv = new Servizio(companyDue, sigla1, desc1);
        singoloRecordSecondaCompany(serv);

        serv = new Servizio(companyDue, sigla2, desc2);
        singoloRecordSecondaCompany(serv);

        serv = new Servizio(companyDue, sigla3, desc3);
        singoloRecordSecondaCompany(serv);

        serv = new Servizio(companyDue, sigla4, desc1);
        singoloRecordSecondaCompany(serv);

    }// end of method


    @Test
    /**
     * Costruttore minimo con tutte le properties obbligatorie
     */
    public void costruttoreMinimo() {
        //-cancello i records per riprovare qui
        cancellaRecords();

        //--nessuna company corrente
        //--crea l'istanza ma non la registra
        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        if (companyCorrente == null) {
            serv = new Servizio(sigla1, desc1);
            assertNotNull(serv);
            serv = (Servizio) serv.save();
            assertNull(serv);
        }// end of if cycle

        //--costruttore senza argomenti
        //--crea l'istanza ma non la registra
        serv = new Servizio();
        assertNotNull(serv);
        serv = (Servizio) serv.save();
        assertNull(serv);

        //--nessuna company passata come pareametro
        //--crea l'istanza ma non la registra
        serv = new Servizio(null, sigla1, desc1);
        serv = (Servizio) serv.save();
        assertNull(serv);

        //--prima company
        numPrevisto = Servizio.countByCompany(companyUno, MANAGER);

        // senza nessun parametro
        serv = new Servizio();
        costruttoreNulloPrimaCompany(serv, numPrevisto);

        // parametro obbligatorio vuoto
        serv = new Servizio(null, sigla1, desc1);
        costruttoreNulloPrimaCompany(serv, numPrevisto);

        // parametro obbligatorio vuoto
        serv = new Servizio(companyUno, "", desc1);
        costruttoreNulloPrimaCompany(serv, numPrevisto);

        // parametro obbligatorio vuoto
        serv = new Servizio(companyUno, "", "");
        costruttoreNulloPrimaCompany(serv, numPrevisto);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        serv = new Servizio(companyUno, sigla1, desc1);
        costruttoreValidoPrimaCompany(serv, 1, numPrevisto);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        serv = new Servizio(companyUno, sigla2, desc3);
        costruttoreValidoPrimaCompany(serv, 2, numPrevisto);

        // campo unico, doppio
        serv = new Servizio(companyUno, sigla1, desc1);
        costruttoreNulloPrimaCompany(serv, numPrevisto);

        //--seconda company
        numPrevisto = Servizio.countByCompany(companyDue, MANAGER);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        serv = new Servizio(companyDue, sigla2, desc2);
        costruttoreValidoSecondaCompany(serv, 1, numPrevisto);

        numPrevisto = 2;
        numOttenuto = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 1;
        numOttenuto = Servizio.countByCompany(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 3;
        numOttenuto = Servizio.countByAllCompanies(MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Costruttore completo
     */
    public void costruttoreCompleto() {
        //-cancello i records per riprovare qui
        cancellaRecords();

        //--prima company
        numPrevisto = Servizio.countByCompany(companyDue, MANAGER);

        // tutti i parametri previsti
        numPrevisto = numPrevisto + 1;
        serv = new Servizio(companyDue, sigla1, desc1, 6, 0, true, 7, 14);
        costruttoreValidoSecondaCompany(serv, 6, numPrevisto);
    }// end of single test


    @Test
    /**
     * Recupera il numero totale di records della Entity
     */
    public void countByAllCompanies() {
        numPrevisto = chiavi.size();
        numOttenuto = Servizio.countByAllCompanies(MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Recupera il numero di records della Entity
     */
    public void countByCompany() {
        numPrevisto = chiaviUno.size();
        numOttenuto = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviDue.size();
        numOttenuto = Servizio.countByCompany(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviUno.size() + chiaviDue.size();
        numOttenuto = Servizio.countByAllCompanies(MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Recupera il numero di records della Entity, filtrato sul valore della property indicata
     */
    public void countByCompanyAndProperty() {
        numPrevisto = 1;
        numOttenuto = Servizio.countByCompanyAndProperty(companyUno, Servizio_.sigla, sigla1, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 2;
        numOttenuto = Servizio.countByCompanyAndProperty(companyUno, Servizio_.descrizione, desc1, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 2;
        numOttenuto = Servizio.countByCompanyAndProperty(companyDue, Servizio_.descrizione, desc1, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     */
    public void find() {
        int pos;

        pos = 3;
        serv = Servizio.find(chiavi.get(pos), MANAGER);
        assertEquals(serv, lista.get(pos));

        //--volutamente sbagliato il long
        serv = Servizio.find(1, MANAGER);
        assertNull(serv);

        pos = 7;
        serv = Servizio.find(chiavi.get(pos), MANAGER);
        assertEquals(serv, lista.get(pos));

        pos = 3;
        serv = Servizio.find(chiaviUno.get(pos), MANAGER);
        assertEquals(serv, listaUno.get(pos));

        pos = 3;
        serv = Servizio.find(chiaviDue.get(pos), MANAGER);
        assertEquals(serv, listaDue.get(pos));
    }// end of single test

    @Test
    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     */
    public void getEntityByCodeCompanyUnico() {
        int pos;
        String key;

        pos = 2;
        key = listaCodeCompanyUnici.get(pos);
        serv = Servizio.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNotNull(serv);
        assertEquals(serv, lista.get(pos));

        pos = 7;
        key = listaCodeCompanyUnici.get(pos);
        serv = Servizio.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNotNull(serv);
        assertEquals(serv, lista.get(pos));

        //--volutamente sbagliato il listaCodeCompanyUnici
        key = "listaCodeCompanyUnici";
        serv = Servizio.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNull(serv);
    }// end of single test

    @Test
    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     */
    public void getEntityByCompanyAndSigla() {
        int pos;

        serv = Servizio.getEntityByCompanyAndSigla(null, sigla1, MANAGER);
        assertNull(serv);

        serv = Servizio.getEntityByCompanyAndSigla(companyUno, "sbagliato", MANAGER);
        assertNull(serv);

        pos = 0;
        serv = Servizio.getEntityByCompanyAndSigla(companyUno, sigla1, MANAGER);
        assertNotNull(serv);
        assertEquals(serv.getId(), lista.get(pos).getId());

        pos = 6;
        serv = Servizio.getEntityByCompanyAndSigla(companyDue, sigla3, MANAGER);
        assertNotNull(serv);
        assertEquals(serv.getId(), lista.get(pos).getId());
    }// end of single test

    @Test
    /**
     * Recupera una lista (array) di tutti i records della Entity
     */
    public void getListByAllCompanies() {

        listaTre = Servizio.getListByAllCompanies(MANAGER);
        assertNotNull(listaTre);
        assertEquals(listaTre, lista);
        assertListeUguali(listaTre, lista);
    }// end of single test


    @Test
    /**
     * Recupera una lista (array) di tutti i records della Entity
     */
    public void getListByCompany() {

        listaTre = Servizio.getListByCompany(companyUno, MANAGER);
        assertNotNull(listaTre);
        assertEquals(listaTre, listaUno);
        assertListeUguali(listaTre, listaUno);

        listaTre = Servizio.getListByCompany(companyDue, MANAGER);
        assertNotNull(listaTre);
        assertEquals(listaTre, listaDue);
        assertListeUguali(listaTre, listaDue);
    }// end of single test

    @Test
    /**
     * Search for the values of a given property of the given Entity class
     */
    public void getListStrForCodeCompanyUnico() {
        listStr = Servizio.getListStrForCodeCompanyUnico(MANAGER);
        assertEquals(listStr.size(), lista.size());
    }// end of single test


    @Test
    /**
     * Search for the values of a given property of the given Entity class
     */
    public void getListStrForSiglaByCompany() {
        listStr = Servizio.getListStrForSiglaByCompany(companyUno, MANAGER);
        assertEquals(listStr.size(), listaUno.size());

        listStr = Servizio.getListStrForSiglaByCompany(companyDue, MANAGER);
        assertEquals(listStr.size(), listaDue.size());
    }// end of single test

    @Test
    /**
     * Creazione iniziale di una istanza della Entity
     */
    public void crea() {
        //-cancello i records per riprovare qui
        cancellaRecords();

        //--nessuna company corrente
        //--non registra
        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        if (companyCorrente == null) {
            serv = Servizio.crea(sigla1, desc1);
            assertNull(serv);
        }// end of if cycle

        //--nessuna company passata come pareametro
        //--non registra
        serv = Servizio.crea(null, sigla1, desc1);
        assertNull(serv);

        //--prima company
        numPrevisto = Servizio.countByCompany(companyUno, MANAGER);

        // senza tutti i parametri obbligatori
        serv = Servizio.crea(null, sigla1, desc1, MANAGER);
        assertNull(serv);
        serv = Servizio.crea(companyUno, "", desc1, MANAGER);
        assertNull(serv);
        serv = Servizio.crea(companyDue, sigla1, "", MANAGER);
        assertNull(serv);

        //--con i parametri obbligatori
        numPrevisto = 1;
        serv = Servizio.crea(companyUno, sigla1, desc1, MANAGER);
        assertNotNull(serv);
        assertNotNull(serv.getId());
        ordine = serv.getOrdine();
        assertEquals(ordine, 1);
        numOttenuto = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        //--seconda company
        //--tutti i parametri previsti
        numPrevisto = 1;
        serv = Servizio.crea(companyDue, sigla1, desc1, 6, 0, true, 7, 14, MANAGER);
        assertNotNull(serv);
        assertNotNull(serv.getId());
        numOttenuto = Servizio.countByCompany(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
        assertEquals(serv.getSigla(), sigla1);
        assertEquals(serv.getDescrizione(), desc1);
        assertEquals(serv.getOrdine(), 6);
    }// end of single test

    @Test
    /**
     * Delete all the records for the Entity class
     * Bulk delete records with CriteriaDelete
     *
     * @param manager the EntityManager to use
     */
    public void delete() {
        numPrevisto = chiavi.size();
        numOttenuto = Servizio.deleteAll(MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        //--cancello e ricreo i records per riprovare qui
        cancellaRecords();
        reset();
        creaRecords();

        numPrevisto = chiaviUno.size();
        numOttenuto = Servizio.deleteAll(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviDue.size();
        numOttenuto = Servizio.deleteAll(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Saves this entity to the database.
     */
    public void save() {
        BaseEntity entity = null;

        //--cancello i records per riprovare qui
        cancellaRecords();
        reset();

        //--prima company
        //-mancano le properties obbligatorie - NON registra
        serv = new Servizio();
        assertNotNull(serv);
        try { // prova ad eseguire il codice
            entity = serv.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(serv.getId());

        //-mancano le properties obbligatorie - NON registra
        serv = new Servizio(null, sigla1, desc1);
        assertNotNull(serv);
        try { // prova ad eseguire il codice
            entity = serv.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(serv.getId());

        //-mancano le properties obbligatorie - NON registra
        serv = new Servizio(companyUno, sigla1, "");
        assertNotNull(serv);
        try { // prova ad eseguire il codice
            entity = serv.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(serv.getId());

        //--mancano le properties obbligatorie - NON registra
        serv = new Servizio(companyUno, "", desc1);
        assertNotNull(serv);
        try { // prova ad eseguire il codice
            entity = serv.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(serv.getId());

        //--ci sono i parametri obbligatori
        numPrevisto = 1;
        serv = new Servizio(companyUno, sigla1, desc1);
        assertNotNull(serv);
        entity = serv.save(companyUno, MANAGER);
        assertNotNull(entity);
        assertTrue(entity instanceof Servizio);
        assertNotNull(serv);
        assertNotNull(serv.getId());
        ordine = serv.getOrdine();
        assertEquals(ordine, 1);
        numOttenuto = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Saves this entity to the database.
     */
    public void saveSafe() {
        //--cancello i records per riprovare qui
        cancellaRecords();
        reset();

        //--prima company
        //--mancano le properties obbligatorie - NON registra
        serv = new Servizio();
        assertNotNull(serv);
        try { // prova ad eseguire il codice
            serv = serv.saveSafe(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(serv);

        //--ci sono i parametri obbligatori
        numPrevisto = 1;
        serv = new Servizio(companyUno, sigla1, desc1);
        assertNotNull(serv);
        serv = serv.save(companyUno, MANAGER);
        assertNotNull(serv);
        assertNotNull(serv.getId());
        ordine = serv.getOrdine();
        assertEquals(ordine, 1);
        numOttenuto = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Numero massimo conenuto nella property
     */
    public void maxOrdine() {
        //--prima company
        numPrevisto = listaUno.size();

        numOttenuto = Servizio.maxOrdine(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        //--seconda company
        numPrevisto = listaDue.size();

        numOttenuto = Servizio.maxOrdine(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Clone di questa istanza
6     */
    public void cloneTest() {
        Servizio servClonato = null;

        serv = Servizio.find(chiavi.get(0), MANAGER);
        assertNotNull(serv);

        try { // prova ad eseguire il codice
            servClonato = serv.clone();
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(servClonato);
        assertServiziUguali(servClonato, serv);
    }// end of single test

    //------------------------------------------------------------------------------------------------------------------------
    // Utilities
    //------------------------------------------------------------------------------------------------------------------------


    private void singoloRecordPrimaCompany(Servizio serv) {
        singoloRecord(serv);
        chiaviUno.add(serv.getId());
        listaUno.add(serv);
    }// end of method

    private void singoloRecordSecondaCompany(Servizio serv) {
        singoloRecord(serv);
        chiaviDue.add(serv.getId());
        listaDue.add(serv);
    }// end of method

    private void singoloRecord(Servizio serv) {
        serv = serv.saveSafe(MANAGER);
        chiavi.add(serv.getId());
        lista.add(serv);
        listaCodeCompanyUnici.add(serv.getCodeCompanyUnico());
    }// end of method


    private void costruttoreNullo(Servizio serv) {
        try { // prova ad eseguire il codice
            serv = serv.saveSafe(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(serv);
    }// end of method

    private void costruttoreNulloPrimaCompany(Servizio serv, int numPrevisto) {
        costruttoreNullo(serv);
        numOttenuto = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreNulloSecondaCompany(Servizio serv, int numPrevisto) {
        costruttoreNullo(serv);
        numOttenuto = Servizio.countByCompany(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValidoPrimaCompany(Servizio serv, int ordinePrevisto, int numPrevisto) {
        costruttoreValido(serv, ordinePrevisto);
        numOttenuto = Servizio.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValidoSecondaCompany(Servizio serv, int ordinePrevisto, int numPrevisto) {
        costruttoreValido(serv, ordinePrevisto);
        numOttenuto = Servizio.countByCompany(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValido(Servizio serv, int ordinePrevisto) {
        try { // prova ad eseguire il codice
            serv = serv.saveSafe(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        assertNotNull(serv);
        assertNotNull(serv.getId());
        ordine = serv.getOrdine();
        assertEquals(ordine, ordinePrevisto);
    }// end of method

    private void assertListeUguali(List<Servizio> lista1, List<Servizio> lista2) {
        for (int k = 0; k < lista1.size(); k++) {
            assertServiziUguali(lista1.get(k), lista2.get(k));
        }// end of for cycle
    }// end of method


    private void assertServiziUguali(Servizio serv1, Servizio serv2) {
        assertEquals(serv1.getId(), serv2.getId());
        assertEquals(serv1.getCodeCompanyUnico(), serv2.getCodeCompanyUnico());
        assertEquals(serv1.getSigla(), serv2.getSigla());
        assertEquals(serv1.getDescrizione(), serv2.getDescrizione());
    }// end of method

}// end of test class