import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import org.junit.*;
import test.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gac on 01 set 2016.
 * <p>
 * Test per la Entity Funzione
 * I test vengono eseguiti su un DB di prova, usando un apposito EntityManager
 * La Entity usa la multiazienda (company) e quindi estende CompanyEntity e non BaseEntity
 */
public class FunzioneTest extends WamTest {

    private Funzione funz;

    private List<Funzione> lista = new ArrayList<>();
    private List<Funzione> listaUno = new ArrayList<>();
    private List<Funzione> listaDue = new ArrayList<>();
    private List<Funzione> listaTre = new ArrayList<>();

    /**
     * SetUp iniziale eseguito solo una volta alla creazione della classe
     */
    @BeforeClass
    public static void setUpInizialeStaticoEseguitoSoloUnaVoltaAllaCreazioneDellaClasse() {
        WamTest.setUpInizialeStaticoEseguitoSoloUnaVoltaAllaCreazioneDellaClasse();
    } // end of setup statico iniziale della classe

    /**
     * CleanUp finale eseguito solo una volta alla chiusura della classe
     */
    @AfterClass
    public static void cleanUpFinaleStaticoEseguitoSoloUnaVoltaAllaChiusuraDellaClasse() {
        WamTest.cleanUpFinaleStaticoEseguitoSoloUnaVoltaAllaChiusuraDellaClasse();
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
        funz = null;
        super.reset();
    }// end of method

    /**
     * Prima di inizare a creare e modificare le funzioni, cancello tutte le (eventuali) precedenti
     * Alla fine, cancello tutte le funzioni create
     */
    protected void cancellaRecords() {
        Funzione.deleteAll(MANAGER);
    }// end of method

    /**
     * Crea alcuni records temporanei per effettuare le prove delle query
     * Uso la Entity
     * Creo alcuni nuovi records nel DB alternativo (WAMTEST)
     */
    protected void creaRecords() {

        //--prima company
        funz = new Funzione(WamTest.COMPANY_UNO, code1, sigla1, desc1);
        singoloRecordPrimaCompany(funz);

        funz = new Funzione(WamTest.COMPANY_UNO, code2, sigla1, desc1);
        singoloRecordPrimaCompany(funz);

        funz = new Funzione(WamTest.COMPANY_UNO, code3, sigla1, desc2);
        singoloRecordPrimaCompany(funz);

        funz = new Funzione(WamTest.COMPANY_UNO, code4, sigla2, desc2);
        singoloRecordPrimaCompany(funz);

        //--seconda company
        funz = new Funzione(WamTest.COMPANY_DUE, code1, sigla1, desc1);
        singoloRecordSecondaCompany(funz);

        funz = new Funzione(WamTest.COMPANY_DUE, code2, sigla1, desc1);
        singoloRecordSecondaCompany(funz);

        funz = new Funzione(WamTest.COMPANY_DUE, code3, sigla3, desc2);
        singoloRecordSecondaCompany(funz);

        funz = new Funzione(WamTest.COMPANY_DUE, code4, sigla2, desc2);
        singoloRecordSecondaCompany(funz);

        numPrevisto = chiaviUno.size() + chiaviDue.size();
        numOttenuto = chiavi.size();
        assertEquals(numOttenuto, numPrevisto);
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
            funz = new Funzione(code1, sigla1, desc1);
            assertNotNull(funz);
            funz = (Funzione) funz.save();
            assertNull(funz);
        }// end of if cycle

        //--costruttore senza argomenti
        //--crea l'istanza ma non la registra
        funz = new Funzione();
        assertNotNull(funz);
        funz = (Funzione) funz.save();
        assertNull(funz);

        //--nessuna company passata come pareametro
        //--crea l'istanza ma non la registra
        funz = new Funzione(null, code1, sigla1, desc1);
        assertNotNull(funz);
        funz = (Funzione) funz.save();
        assertNull(funz);

        //--prima company
        numPrevisto = Funzione.countByCompany(WamTest.COMPANY_UNO, MANAGER);

        // senza nessun parametro
        funz = new Funzione();
        costruttoreNulloPrimaCompany(funz, numPrevisto);

        // parametro obbligatorio vuoto
        funz = new Funzione(null, code1, sigla1, desc1);
        costruttoreNulloPrimaCompany(funz, numPrevisto);

        // parametro obbligatorio vuoto
        funz = new Funzione(WamTest.COMPANY_UNO, code1, "", desc1);
        costruttoreNulloPrimaCompany(funz, numPrevisto);

        // parametro obbligatorio vuoto
        funz = new Funzione(WamTest.COMPANY_UNO, code1, sigla1, "");
        costruttoreNulloPrimaCompany(funz, numPrevisto);

        // parametro obbligatorio vuoto
        funz = new Funzione(WamTest.COMPANY_UNO, "", "", "");
        costruttoreNulloPrimaCompany(funz, numPrevisto);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        funz = new Funzione(WamTest.COMPANY_UNO, code1, sigla1, desc1);
        costruttoreValidoPrimaCompany(funz, 1, numPrevisto);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        funz = new Funzione(WamTest.COMPANY_UNO, code2, sigla2, desc2);
        costruttoreValidoPrimaCompany(funz, 2, numPrevisto);

        //--seconda company
        numPrevisto = Funzione.countByCompany(WamTest.COMPANY_DUE, MANAGER);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        funz = new Funzione(WamTest.COMPANY_DUE, code2, sigla2, desc2);
        costruttoreValidoSecondaCompany(funz, 1, numPrevisto);

        // campo unico, doppio
        funz = new Funzione(WamTest.COMPANY_UNO, code1, sigla1, desc1);
        costruttoreNulloSecondaCompany(funz, numPrevisto);

        numPrevisto = 2;
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_UNO, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 1;
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_DUE, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 3;
        numOttenuto = Funzione.countByAllCompanies(MANAGER);
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
        numPrevisto = Funzione.countByCompany(WamTest.COMPANY_DUE, MANAGER);

        // tutti i parametri previsti
        numPrevisto = numPrevisto + 1;
        funz = new Funzione(WamTest.COMPANY_DUE, code1, sigla1, desc1, 6, FontAwesome.USER);
        costruttoreValidoSecondaCompany(funz, 6, numPrevisto);
    }// end of single test

    @Test
    /**
     * Recupera il numero totale di records della Entity
     */
    public void countByAllCompanies() {
        numPrevisto = chiavi.size();
        numOttenuto = Funzione.countByAllCompanies(MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Recupera il numero di records della Entity
     */
    public void countByCompany() {
        numPrevisto = chiaviUno.size();
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_UNO, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviDue.size();
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_DUE, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviUno.size() + chiaviDue.size();
        numOttenuto = Funzione.countByAllCompanies(MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Recupera il numero di records della Entity, filtrato sul valore della property indicata
     */
    public void countByCompanyAndProperty() {
        numPrevisto = 3;
        numOttenuto = Funzione.countByCompanyAndProperty(WamTest.COMPANY_UNO, Funzione_.sigla, sigla1, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 2;
        numOttenuto = Funzione.countByCompanyAndProperty(WamTest.COMPANY_DUE, Funzione_.sigla, sigla1, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 1;
        numOttenuto = Funzione.countByCompanyAndProperty(WamTest.COMPANY_UNO, Funzione_.code, code1, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 0;
        numOttenuto = Funzione.countByCompanyAndProperty(WamTest.COMPANY_UNO, Funzione_.sigla, sigla3, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 1;
        numOttenuto = Funzione.countByCompanyAndProperty(WamTest.COMPANY_DUE, Funzione_.sigla, sigla3, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     */
    public void find() {
        int pos;

        pos = 3;
        funz = Funzione.find(chiavi.get(pos), MANAGER);
        assertEquals(funz, lista.get(pos));

        //--volutamente sbagliato il long
        funz = Funzione.find(1, MANAGER);
        assertNull(funz);

        pos = 7;
        funz = Funzione.find(chiavi.get(pos), MANAGER);
        assertEquals(funz, lista.get(pos));

        pos = 3;
        funz = Funzione.find(chiaviUno.get(pos), MANAGER);
        assertEquals(funz, listaUno.get(pos));

        pos = 3;
        funz = Funzione.find(chiaviDue.get(pos), MANAGER);
        assertEquals(funz, listaDue.get(pos));
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
        funz = Funzione.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNotNull(funz);
        assertEquals(funz, lista.get(pos));

        pos = 7;
        key = listaCodeCompanyUnici.get(pos);
        funz = Funzione.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNotNull(funz);
        assertEquals(funz, lista.get(pos));

        //--volutamente sbagliato il listaCodeCompanyUnici
        key = "listaCodeCompanyUnici";
        funz = Funzione.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNull(funz);
    }// end of single test

    @Test
    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     */
    public void getEntityByCompanyAndCode() {
        int pos;

        funz = Funzione.getEntityByCompanyAndCode(null, code1, MANAGER);
        assertNull(funz);

        funz = Funzione.getEntityByCompanyAndCode(WamTest.COMPANY_UNO, "sbagliato", MANAGER);
        assertNull(funz);

        pos = 0;
        funz = Funzione.getEntityByCompanyAndCode(WamTest.COMPANY_UNO, code1, MANAGER);
        assertNotNull(funz);
        assertEquals(funz.getId(), lista.get(pos).getId());

        pos = 6;
        funz = Funzione.getEntityByCompanyAndCode(WamTest.COMPANY_DUE, code3, MANAGER);
        assertNotNull(funz);
        assertEquals(funz.getId(), lista.get(pos).getId());
    }// end of single test

    @Test
    /**
     * Recupera una lista (array) di tutti i records della Entity
     */
    public void getListByAllCompanies() {

        listaUno = Funzione.getListByAllCompanies(MANAGER);
        assertNotNull(listaUno);
        assertEquals(listaUno, lista);
        assertListeUguali(listaUno, lista);
    }// end of single test

    @Test
    /**
     * Recupera una lista (array) di tutti i records della Entity
     */
    public void getListByCompany() {

        listaTre = Funzione.getListByCompany(WamTest.COMPANY_UNO, MANAGER);
        assertNotNull(listaTre);
        assertEquals(listaTre, listaUno);
        assertListeUguali(listaTre, listaUno);

        listaTre = Funzione.getListByCompany(WamTest.COMPANY_DUE, MANAGER);
        assertNotNull(listaTre);
        assertEquals(listaTre, listaDue);
        assertListeUguali(listaTre, listaDue);
    }// end of single test

    @Test
    /**
     * Search for the values of a given property of the given Entity class
     */
    public void getListStrForCodeCompanyUnico() {
        listStr = Funzione.getListStrForCodeCompanyUnico(MANAGER);
        assertEquals(listStr.size(), lista.size());
    }// end of single test

    @Test
    /**
     * Search for the values of a given property of the given Entity class
     */
    public void getListStrForCodeByCompany() {
        listStr = Funzione.getListStrForCodeByCompany(WamTest.COMPANY_UNO, MANAGER);
        assertEquals(listStr.size(), listaUno.size());

        listStr = Funzione.getListStrForCodeByCompany(WamTest.COMPANY_DUE, MANAGER);
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
            funz = Funzione.crea(code1, sigla1, desc1);
            assertNull(funz);
        }// end of if cycle

        //--nessuna company passata come pareametro
        //--non registra
        funz = Funzione.crea(null, code1, sigla1, desc1);
        assertNull(funz);

        //--prima company
        numPrevisto = Funzione.countByCompany(WamTest.COMPANY_UNO, MANAGER);

        // senza tutti i parametri obbligatori
        funz = Funzione.crea(null, code1, sigla1, desc1, MANAGER);
        assertNull(funz);
        funz = Funzione.crea(WamTest.COMPANY_UNO, null, sigla1, desc1, MANAGER);
        assertNull(funz);
        funz = Funzione.crea(WamTest.COMPANY_UNO, code1, "", desc1, MANAGER);
        assertNull(funz);
        funz = Funzione.crea(WamTest.COMPANY_UNO, code1, sigla1, null, MANAGER);
        assertNull(funz);

        //--con i parametri obbligatori
        numPrevisto = 1;
        funz = Funzione.crea(WamTest.COMPANY_UNO, code1, sigla1, desc1, MANAGER);
        assertNotNull(funz);
        assertNotNull(funz.getId());
        ordine = funz.getOrdine();
        assertEquals(ordine, 1);
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_UNO, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        //--seconda company
        //--tutti i parametri previsti
        numPrevisto = 1;
        funz = Funzione.crea(WamTest.COMPANY_DUE, code1, sigla1, desc1, 6, FontAwesome.USER, MANAGER);
        assertNotNull(funz);
        assertNotNull(funz.getId());
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_DUE, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
        assertEquals(funz.getCode(), code1);
        assertEquals(funz.getSigla(), sigla1);
        assertEquals(funz.getDescrizione(), desc1);
        assertEquals(funz.getOrdine(), 6);
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
        numOttenuto = Funzione.deleteAll(MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        //--cancello e ricreo i records per riprovare qui
        cancellaRecords();
        reset();
        creaRecords();

        numPrevisto = chiaviUno.size();
        numOttenuto = Funzione.deleteAll(WamTest.COMPANY_UNO, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviDue.size();
        numOttenuto = Funzione.deleteAll(WamTest.COMPANY_DUE, MANAGER);
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
        funz = new Funzione();
        assertNotNull(funz);
        try { // prova ad eseguire il codice
            entity = funz.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(funz.getId());

        //-mancano le properties obbligatorie - NON registra
        funz = new Funzione(null, code1, sigla1, desc1);
        assertNotNull(funz);
        try { // prova ad eseguire il codice
            entity = funz.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(funz.getId());

        //-mancano le properties obbligatorie - NON registra
        funz = new Funzione(WamTest.COMPANY_UNO, "", sigla1, desc1);
        assertNotNull(funz);
        try { // prova ad eseguire il codice
            entity = funz.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(funz.getId());

        //--mancano le properties obbligatorie - NON registra
        funz = new Funzione(WamTest.COMPANY_UNO, code1, "", desc1);
        assertNotNull(funz);
        try { // prova ad eseguire il codice
            entity = funz.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(funz.getId());

        //--ci sono i parametri obbligatori
        numPrevisto = 1;
        funz = new Funzione(WamTest.COMPANY_UNO, code1, sigla1, desc1);
        assertNotNull(funz);
        entity = funz.save(WamTest.COMPANY_UNO, MANAGER);
        assertNotNull(entity);
        assertTrue(entity instanceof Funzione);
        assertNotNull(funz);
        assertNotNull(funz.getId());
        ordine = funz.getOrdine();
        assertEquals(ordine, 1);
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_UNO, MANAGER);
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
        funz = new Funzione();
        assertNotNull(funz);
        try { // prova ad eseguire il codice
            funz = funz.saveSafe(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(funz);

        //--ci sono i parametri obbligatori
        numPrevisto = 1;
        funz = new Funzione(WamTest.COMPANY_UNO, code1, sigla1, desc1);
        assertNotNull(funz);
        funz = funz.save(WamTest.COMPANY_UNO, MANAGER);
        assertNotNull(funz);
        assertNotNull(funz.getId());
        ordine = funz.getOrdine();
        assertEquals(ordine, 1);
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_UNO, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Numero massimo conenuto nella property
     */
    public void maxOrdine() {
        //--prima company
        numPrevisto = listaUno.size();

        numOttenuto = Funzione.maxOrdine(WamTest.COMPANY_UNO, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        //--seconda company
        numPrevisto = listaDue.size();

        numOttenuto = Funzione.maxOrdine(WamTest.COMPANY_DUE, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Clone di questa istanza
     */
    public void cloneTest() {
        Funzione funzClonata = null;

        funz = Funzione.find(chiavi.get(0), MANAGER);
        assertNotNull(funz);

        try { // prova ad eseguire il codice
            funzClonata = funz.clone();
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(funzClonata);
        assertFunzioniUguali(funzClonata, funz);
    }// end of single test

    //------------------------------------------------------------------------------------------------------------------------
    // Utilities
    //------------------------------------------------------------------------------------------------------------------------

    private void singoloRecordPrimaCompany(Funzione funz) {
        singoloRecord(funz);
        chiaviUno.add(funz.getId());
        listaUno.add(funz);
    }// end of method

    private void singoloRecordSecondaCompany(Funzione funz) {
        singoloRecord(funz);
        chiaviDue.add(funz.getId());
        listaDue.add(funz);
    }// end of method

    private void singoloRecord(Funzione funz) {
        funz = funz.saveSafe(MANAGER);
        assertNotNull(funz);
        chiavi.add(funz.getId());
        lista.add(funz);
        listaCodeCompanyUnici.add(funz.getCodeCompanyUnico());
    }// end of method

    private void costruttoreNullo(Funzione funz) {
        try { // prova ad eseguire il codice
            funz = funz.saveSafe(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(funz);
    }// end of method

    private void costruttoreNulloPrimaCompany(Funzione funz, int numPrevisto) {
        costruttoreNullo(funz);
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_UNO, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreNulloSecondaCompany(Funzione funz, int numPrevisto) {
        costruttoreNullo(funz);
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_DUE, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValidoPrimaCompany(Funzione funz, int ordinePrevisto, int numPrevisto) {
        costruttoreValido(funz, ordinePrevisto);
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_UNO, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValidoSecondaCompany(Funzione funz, int ordinePrevisto, int numPrevisto) {
        costruttoreValido(funz, ordinePrevisto);
        numOttenuto = Funzione.countByCompany(WamTest.COMPANY_DUE, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValido(Funzione funz, int ordinePrevisto) {
        try { // prova ad eseguire il codice
            funz = funz.saveSafe(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        assertNotNull(funz);
        assertNotNull(funz.getId());
        ordine = funz.getOrdine();
        assertEquals(ordine, ordinePrevisto);
    }// end of method

    private void assertListeUguali(List<Funzione> lista1, List<Funzione> lista2) {
        for (int k = 0; k < lista1.size(); k++) {
            assertFunzioniUguali(lista1.get(k), lista2.get(k));
        }// end of for cycle
    }// end of method

    private void assertFunzioniUguali(Funzione funz1, Funzione funz2) {
        assertEquals(funz1.getId(), funz2.getId());
        assertEquals(funz1.getCodeCompanyUnico(), funz2.getCodeCompanyUnico());
        assertEquals(funz1.getCode(), funz2.getCode());
        assertEquals(funz1.getSigla(), funz2.getSigla());
        assertEquals(funz1.getDescrizione(), funz2.getDescrizione());
    }// end of method

}// end of test class