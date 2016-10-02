import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.webbase.web.entity.BaseEntity;
import org.junit.*;

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

    private Funzione funzioneUno;
    private Funzione funzioneDue;
    private Funzione funzioneTre;
    private Funzione funzioneQuattro;
    private Funzione funzioneCinque;

    private List<Funzione> lista = new ArrayList<>();
    private List<Funzione> listaUno = new ArrayList<>();
    private List<Funzione> listaDue = new ArrayList<>();
    private List<Funzione> listaTre = new ArrayList<>();


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
        funz = null;
        chiaviUno = new ArrayList<>();
        chiaviDue = new ArrayList<>();
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
        funz = new Funzione(companyUno, code1, sigla1, desc1);
        singoloRecordPrimaCompany(funz);

        funz = new Funzione(companyUno, code2, sigla1, desc1);
        singoloRecordPrimaCompany(funz);

        funz = new Funzione(companyUno, code3, sigla1, desc2);
        singoloRecordPrimaCompany(funz);

        funz = new Funzione(companyUno, code4, sigla2, desc2);
        singoloRecordPrimaCompany(funz);

        //--seconda company
        funz = new Funzione(companyDue, code1, sigla1, desc1);
        singoloRecordSecondaCompany(funz);

        funz = new Funzione(companyDue, code2, sigla1, desc1);
        singoloRecordSecondaCompany(funz);

        funz = new Funzione(companyDue, code3, sigla3, desc2);
        singoloRecordSecondaCompany(funz);

        funz = new Funzione(companyDue, code4, sigla2, desc2);
        singoloRecordSecondaCompany(funz);

        numPrevisto = chiaviUno.size() + chiaviDue.size();
        numOttenuto = chiavi.size();
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    @Test
    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * Se manca l'ordine di presentazione o è uguale a zero, viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param code        di riferimento interna (obbligatoria)
     * @param sigla       di visibile nel tabellone (obbligatoria)
     * @param descrizione (obbligatoria)
     */
    public void costruttoreMinimo() {
        //-cancello i records per riprovare qui
        cancellaRecords();

        //--prima company
        numPrevisto = Funzione.countByCompany(companyUno, MANAGER);

        // senza nessun parametro
        funz = new Funzione();
        costruttoreNullo(funz);

        // parametro obbligatorio vuoto
        funz = new Funzione(null, code1, sigla1, desc1);
        costruttoreNullo(funz);

        // parametro obbligatorio vuoto
        funz = new Funzione(companyUno, code1, "", desc1);
        costruttoreNullo(funz);

        // parametro obbligatorio vuoto
        funz = new Funzione(companyUno, code1, sigla1, "");
        costruttoreNullo(funz);

        // parametro obbligatorio vuoto
        funz = new Funzione(companyUno, "", "", "");
        costruttoreNullo(funz);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        funz = new Funzione(companyUno, code1, sigla1, desc1);
        costruttoreValidoPrimaCompany(funz, 1, numPrevisto);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        funz = new Funzione(companyUno, code2, sigla2, desc2);
        costruttoreValidoPrimaCompany(funz, 2, numPrevisto);

        //--seconda company
        numPrevisto = Funzione.countByCompany(companyDue, MANAGER);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        funz = new Funzione(companyDue, code2, sigla2, desc2);
        costruttoreValidoSecondaCompany(funz, 1, numPrevisto);

        // campo unico, doppio
        funz = new Funzione(companyUno, code1, sigla1, desc1);
        costruttoreNullo(funz, 2);

//        try { // prova ad eseguire il codice
//            funz.save(companyUno, MANAGER);
//        } catch (Exception unErrore) { // intercetta l'errore
//            System.out.println(unErrore.toString());
//        }// fine del blocco try-catch
//        assertNotNull(funz);
//        assertNull(funz.getId());
//        numOttenuto = Funzione.countByCompany(companyUno, MANAGER);
//        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 2;
        numOttenuto = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 1;
        numOttenuto = Funzione.countByCompany(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 3;
        numOttenuto = Funzione.countByAllCompanies(MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Costruttore completo
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param code        di riferimento interna (obbligatoria)
     * @param sigla       di visibile nel tabellone (obbligatoria)
     * @param descrizione (obbligatoria)
     * @param ordine      di presentazione nelle liste (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param glyph       icona di FontAwesome (facoltative)
     */
    public void costruttoreCompleto() {
        //-cancello i records per riprovare qui
        cancellaRecords();

        //--prima company
        numPrevisto = Funzione.countByCompany(companyDue, MANAGER);

        // tutti i parametri previsti
        numPrevisto = numPrevisto + 1;
        funz = new Funzione(companyDue, code1, sigla1, desc1, 6, FontAwesome.USER);
        funz.save(companyDue, MANAGER);
        assertNotNull(funz);
        assertNotNull(funz.getId());
        ordine = funz.getOrdine();
        assertEquals(ordine, 6);
        numOttenuto = Funzione.countByCompany(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Recupera il numero totale di records della Entity
     * Senza filtri.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param manager the EntityManager to use
     * @return il numero totale di records nella Entity
     */
    public void countByAllCompanies() {
        numPrevisto = chiavi.size();
        numOttenuto = Funzione.countByAllCompanies(MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Recupera il numero di records della Entity
     * Filtrato sulla azienda passata come parametro.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param company di appartenenza (property della superclasse)
     * @param manager the EntityManager to use
     * @return il numero filtrato di records nella Entity
     */
    public void countByCompany() {
        numPrevisto = chiaviUno.size();
        numOttenuto = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviDue.size();
        numOttenuto = Funzione.countByCompany(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviUno.size() + chiaviDue.size();
        numOttenuto = Funzione.countByAllCompanies(MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Recupera il numero di records della Entity, filtrato sul valore della property indicata
     * Filtrato sulla azienda passata come parametro.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param company di appartenenza (property della superclasse)
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the EntityManager to use
     * @return il numero filtrato di records nella Entity
     */
    public void countByCompanyAndProperty() {
        numPrevisto = 3;
        numOttenuto = Funzione.countByCompanyAndProperty(companyUno, Funzione_.sigla, sigla1, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 2;
        numOttenuto = Funzione.countByCompanyAndProperty(companyDue, Funzione_.sigla, sigla1, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 1;
        numOttenuto = Funzione.countByCompanyAndProperty(companyUno, Funzione_.code, code1, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 0;
        numOttenuto = Funzione.countByCompanyAndProperty(companyUno, Funzione_.sigla, sigla3, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 1;
        numOttenuto = Funzione.countByCompanyAndProperty(companyDue, Funzione_.sigla, sigla3, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
     *
     * @param id      valore (unico) della Primary Key
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
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
     *
     * @param codeCompanyUnico di riferimento interna (obbligatoria e unica)
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public void getEntityByCodeCompanyUnico() {
        int pos;
        String key;

        pos = 2;
        key = codeCompanyUnico.get(pos);
        funz = Funzione.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNotNull(funz);
        assertEquals(funz, lista.get(pos));

        pos = 7;
        key = codeCompanyUnico.get(pos);
        funz = Funzione.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNotNull(funz);
        assertEquals(funz, lista.get(pos));

        //--volutamente sbagliato il codeCompanyUnico
        key = "codeCompanyUnico";
        funz = Funzione.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNull(funz);
    }// end of single test

    @Test
    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param code    di riferimento interna (obbligatorio, non unico in generale ma unico all'interno della company)
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public void getEntityByCompanyAndCode() {
        int pos;

        funz = Funzione.getEntityByCompanyAndCode(null, code1, MANAGER);
        assertNull(funz);

        funz = Funzione.getEntityByCompanyAndCode(companyUno, "sbagliato", MANAGER);
        assertNull(funz);

        pos = 0;
        funz = Funzione.getEntityByCompanyAndCode(companyUno, code1, MANAGER);
        assertNotNull(funz);
        assertEquals(funz.getId(), lista.get(pos).getId());

        pos = 6;
        funz = Funzione.getEntityByCompanyAndCode(companyDue, code3, MANAGER);
        assertNotNull(funz);
        assertEquals(funz.getId(), lista.get(pos).getId());
    }// end of single test


    @Test
    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Senza filtri.
     * (non va usata CompanyQuery, altrimenti arriverebbe solo la lista della company corrente)
     *
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
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
     * Filtrato sulla company passata come parametro.
     * Se si arriva qui con una company null, vuol dire che non esiste la company corrente
     *
     * @param company di appartenenza (property della superclasse)
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    public void getListBySingleCompany() {

        listaTre = Funzione.getListBySingleCompany(companyUno, MANAGER);
        assertNotNull(listaTre);
        assertEquals(listaTre, listaUno);
        assertListeUguali(listaTre, listaUno);

        listaTre = Funzione.getListBySingleCompany(companyDue, MANAGER);
        assertNotNull(listaTre);
        assertEquals(listaTre, listaDue);
        assertListeUguali(listaTre, listaDue);
    }// end of single test

    @Test
    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param code        di riferimento interna (obbligatoria)
     * @param sigla       di visibile nel tabellone (obbligatoria)
     * @param descrizione (obbligatoria)
     * @param manager     the EntityManager to use
     * @return istanza della Entity
     */
    public void crea() {
        //-cancello i records per riprovare qui
        cancellaRecords();

        //--prima company
        numPrevisto = Funzione.countByCompany(companyUno, MANAGER);

        // senza tutti i parametri obbligatori
        funz = Funzione.crea(null, code1, sigla1, desc1, MANAGER);
        assertNull(funz);
        funz = Funzione.crea(companyUno, null, sigla1, desc1, MANAGER);
        assertNull(funz);
        funz = Funzione.crea(companyUno, code1, "", desc1, MANAGER);
        assertNull(funz);
        funz = Funzione.crea(companyDue, code1, sigla1, null, MANAGER);
        assertNull(funz);

        //--con i parametri obbligatori
        numPrevisto = 1;
        funz = Funzione.crea(companyUno, code1, sigla1, desc1, MANAGER);
        assertNotNull(funz);
        assertNotNull(funz.getId());
        ordine = funz.getOrdine();
        assertEquals(ordine, 1);
        numOttenuto = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        //--seconda company
        //--tutti i parametri previsti
        numPrevisto = 1;
        funz = Funzione.crea(companyDue, code1, sigla1, desc1, 6, FontAwesome.USER, MANAGER);
        assertNotNull(funz);
        assertNotNull(funz.getId());
        numOttenuto = Funzione.countByCompany(companyDue, MANAGER);
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
        numOttenuto = Funzione.deleteAll(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviDue.size();
        numOttenuto = Funzione.deleteAll(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Saves this entity to the database.
     * <p>
     * If the provided EntityManager has an active transaction, the operation is performed inside the transaction.<br>
     * Otherwise, a new transaction is used to save this single entity.
     *
     * @param company azienda da filtrare
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return the merged Entity (new entity, unmanaged, has the id)
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
        funz = new Funzione(companyUno, "", sigla1, desc1);
        assertNotNull(funz);
        try { // prova ad eseguire il codice
            entity = funz.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(funz.getId());

        //--mancano le properties obbligatorie - NON registra
        funz = new Funzione(companyUno, code1, "", desc1);
        assertNotNull(funz);
        try { // prova ad eseguire il codice
            entity = funz.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(funz.getId());

        //--ci sono i parametri obbligatori
        numPrevisto = 1;
        funz = new Funzione(companyUno, code1, sigla1, desc1);
        assertNotNull(funz);
        entity = funz.save(companyUno, MANAGER);
        assertNotNull(entity);
        assertTrue(entity instanceof Funzione);
        assertNotNull(funz);
        assertNotNull(funz.getId());
        ordine = funz.getOrdine();
        assertEquals(ordine, 1);
        numOttenuto = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Saves this entity to the database.
     * <p>
     * If the provided EntityManager has an active transaction, the operation is performed inside the transaction.<br>
     * Otherwise, a new transaction is used to save this single entity.
     *
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return the merged Entity (new entity, unmanaged, has the id), casted as Funzione
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
        assertNotNull(funz);
        assertNull(funz.getId());

        //--ci sono i parametri obbligatori
        numPrevisto = 1;
        funz = new Funzione(companyUno, code1, sigla1, desc1);
        assertNotNull(funz);
        funz = funz.save(companyUno, MANAGER);
        assertNotNull(funz);
        assertNotNull(funz.getId());
        ordine = funz.getOrdine();
        assertEquals(ordine, 1);
        numOttenuto = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Numero massimo conenuto nella property
     *
     * @param company azienda da filtrare
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return massimo valore
     */
    public void maxOrdine() {
        //--prima company
        numPrevisto = listaUno.size();

        numOttenuto = Funzione.maxOrdine(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        //--seconda company
        numPrevisto = listaDue.size();

        numOttenuto = Funzione.maxOrdine(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Search for the values of a given property of the given Entity class
     * Ordinate sul valore della property indicata
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param manager the EntityManager to use
     */
    public void getListStrByCodeCompanyUnico() {
        listStr = Funzione.getListStrByCodeCompanyUnico(MANAGER);
        assertEquals(listStr.size(), lista.size());
    }// end of single test

    @Test
    /**
     * Search for the values of a given property of the given Entity class
     * Filtrato sulla company passata come parametro.
     * Ordinate sul valore della property indicata
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param company di appartenenza (property della superclasse)
     * @param manager the EntityManager to use
     */
    public void getListStrByCompanyAndCode() {
        listStr = Funzione.getListStrByCompanyAndCode(companyUno, MANAGER);
        assertEquals(listStr.size(), listaUno.size());

        listStr = Funzione.getListStrByCompanyAndCode(companyDue, MANAGER);
        assertEquals(listStr.size(), listaDue.size());
    }// end of single test

    @Test
    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatorio invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza di Funzione con gli stessi valori dei parametri di questa istanza
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
        funz.save(MANAGER);
        chiavi.add(funz.getId());
        lista.add(funz);
        codeCompanyUnico.add(funz.getCodeCompanyUnico());
    }// end of method

    private void costruttoreNullo(Funzione funz) {
        costruttoreNullo(funz, 0);
    }// end of method

    private void costruttoreNullo(Funzione funz, int numPrevisto) {
        try { // prova ad eseguire il codice
            funz.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(funz);
        assertNull(funz.getId());
        numOttenuto = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValidoPrimaCompany(Funzione funz, int ordinePrevisto, int numPrevisto) {
        costruttoreValido(funz, ordinePrevisto);
        numOttenuto = Funzione.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValidoSecondaCompany(Funzione funz, int ordinePrevisto, int numPrevisto) {
        costruttoreValido(funz, ordinePrevisto);
        numOttenuto = Funzione.countByCompany(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreValido(Funzione funz, int ordinePrevisto) {
        try { // prova ad eseguire il codice
            funz.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        assertNotNull(funz);
        assertNotNull(funz.getId());
        ordine = funz.getOrdine();
        assertEquals(ordine, ordinePrevisto);
    }// end of method

}// end of test class