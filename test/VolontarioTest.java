import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
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

        numPrevisto = chiaviUno.size() + chiaviDue.size();
        numOttenuto = chiavi.size();
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

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
        costruttoreNulloPrimaCompany(vol, numPrevisto);

        // parametro obbligatorio vuoto
        vol = new Volontario(null, nome1, cognome1);
        costruttoreNulloPrimaCompany(vol, numPrevisto);

        // parametro obbligatorio vuoto
        vol = new Volontario(companyUno, "", cognome1);
        costruttoreNulloPrimaCompany(vol, numPrevisto);

        // parametro obbligatorio vuoto
        vol = new Volontario(companyUno, nome1, "");
        costruttoreNulloPrimaCompany(vol, numPrevisto);

        // parametro obbligatorio vuoto
        vol = new Volontario(null, "", "");
        costruttoreNulloPrimaCompany(vol, numPrevisto);

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
        costruttoreNulloPrimaCompany(vol, numPrevisto);

        //--seconda company
        numPrevisto = Volontario.countByCompany(companyDue, MANAGER);

        // parametro obbligatorio vuoto
        vol = new Volontario(companyDue, "", cognome1);
        costruttoreNulloSecondaCompany(vol, numPrevisto);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        vol = new Volontario(companyDue, nome1, cognome1);
        costruttoreValidoSecondaCompany(vol, numPrevisto);

        // parametri obbligatori
        numPrevisto = numPrevisto + 1;
        vol = new Volontario(companyDue, nome2, cognome1);
        costruttoreValidoSecondaCompany(vol, numPrevisto);

        // campo unico, doppio
        vol = new Volontario(companyDue, nome2, cognome1);
        costruttoreNulloSecondaCompany(vol, numPrevisto);

    }// end of single test


    @Test
    /**
     * Costruttore completo
     */
    public void costruttoreCompleto() {
        //-cancello i records per riprovare qui
        cancellaRecords();

        //--prima company
        numPrevisto = Volontario.countByCompany(companyDue, MANAGER);

        // tutti i parametri previsti
        numPrevisto = numPrevisto + 1;
        vol = new Volontario(companyDue, nome1, cognome1, new Date(), "337 451288", false);
        costruttoreValidoSecondaCompany(vol, numPrevisto);
    }// end of single test

    @Test
    /**
     * Recupera il numero totale di records della Entity
     */
    public void countByAllCompanies() {
        numPrevisto = chiavi.size();
        numOttenuto = Volontario.countByAllCompanies(MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Recupera il numero di records della Entity
     */
    public void countByCompany() {
        numPrevisto = chiaviUno.size();
        numOttenuto = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviDue.size();
        numOttenuto = Volontario.countByCompany(companyDue, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviUno.size() + chiaviDue.size();
        numOttenuto = Volontario.countByAllCompanies(MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test

    @Test
    /**
     * Recupera il numero di records della Entity, filtrato sul valore della property indicata
     */
    public void countByCompanyAndProperty() {
        numPrevisto = 2;
        numOttenuto = Volontario.countByCompanyAndProperty(companyUno, Volontario_.nome, nome1, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 1;
        numOttenuto = Volontario.countByCompanyAndProperty(companyUno, Volontario_.nome, nome2, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = 0;
        numOttenuto = Volontario.countByCompanyAndProperty(companyDue, Volontario_.cognome, cognome4, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test


    @Test
    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     */
    public void find() {
        int pos;

        pos = 3;
        vol = Volontario.find(chiavi.get(pos), MANAGER);
        assertEquals(vol, lista.get(pos));

        //--volutamente sbagliato il long
        vol = Volontario.find(1, MANAGER);
        assertNull(vol);

        pos = 7;
        vol = Volontario.find(chiavi.get(pos), MANAGER);
        assertEquals(vol, lista.get(pos));

        pos = 3;
        vol = Volontario.find(chiaviUno.get(pos), MANAGER);
        assertEquals(vol, listaUno.get(pos));

        pos = 3;
        vol = Volontario.find(chiaviDue.get(pos), MANAGER);
        assertEquals(vol, listaDue.get(pos));
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
        vol = Volontario.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNotNull(vol);
        assertEquals(vol, lista.get(pos));

        pos = 7;
        key = listaCodeCompanyUnici.get(pos);
        vol = Volontario.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNotNull(vol);
        assertEquals(vol, lista.get(pos));

        //--volutamente sbagliato il listaCodeCompanyUnici
        key = "listaCodeCompanyUnici";
        vol = Volontario.getEntityByCodeCompanyUnico(key, MANAGER);
        assertNull(vol);
    }// end of single test


    @Test
    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     */
    public void getEntityByCompanyAndNomeAndCognome() {
        int pos;

        vol = Volontario.getEntityByCompanyAndNomeAndCognome(null, nome1, cognome1, MANAGER);
        assertNull(vol);

        vol = Volontario.getEntityByCompanyAndNomeAndCognome(companyUno, "sbagliato", cognome1, MANAGER);
        assertNull(vol);

        pos = 0;
        vol = Volontario.getEntityByCompanyAndNomeAndCognome(companyUno, nome1, cognome1, MANAGER);
        assertNotNull(vol);
        assertEquals(vol.getId(), lista.get(pos).getId());

        pos = 4;
        vol = Volontario.getEntityByCompanyAndNomeAndCognome(companyDue, nome1, cognome1, MANAGER);
        assertNotNull(vol);
        assertEquals(vol.getId(), lista.get(pos).getId());
    }// end of single test

    @Test
    /**
     * Recupera una lista (array) di tutti i records della Entity
     */
    public void getListByAllCompanies() {

        listaTre = Volontario.getListByAllCompanies(MANAGER);
        assertNotNull(listaTre);
        assertEquals(listaTre, lista);
        assertListeUguali(listaTre, lista);
    }// end of single test


    @Test
    /**
     * Recupera una lista (array) di tutti i records della Entity
     */
    public void getListByCompany() {

        listaTre = Volontario.getListByCompany(companyUno, MANAGER);
        assertNotNull(listaTre);
        assertEquals(listaTre, listaUno);
        assertListeUguali(listaTre, listaUno);

        listaTre = Volontario.getListByCompany(companyDue, MANAGER);
        assertNotNull(listaTre);
        assertEquals(listaTre, listaDue);
        assertListeUguali(listaTre, listaDue);
    }// end of single test

    @Test
    /**
     * Search for the values of a given property of the given Entity class
     */
    public void getListStrForCodeCompanyUnico() {
        listStr = Volontario.getListStrForCodeCompanyUnico(MANAGER);
        assertEquals(listStr.size(), lista.size());
    }// end of single test


    @Test
    /**
     * Search for the values of a given property of the given Entity class
     */
    public void getListStrForNicknameByCompany() {
        listStr = Volontario.getListStrForNicknameByCompany(companyUno, MANAGER);
        assertEquals(listStr.size(), listaUno.size());

        listStr = Volontario.getListStrForNicknameByCompany(companyDue, MANAGER);
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
            vol = Volontario.crea(nome1,cognome1);
            assertNull(vol);
        }// end of if cycle

        //--nessuna company passata come pareametro
        //--non registra
        vol = Volontario.crea(null, nome1, cognome1);
        assertNull(vol);

        //--prima company
        numPrevisto = Volontario.countByCompany(companyUno, MANAGER);

        // senza tutti i parametri obbligatori
        vol = Volontario.crea(null, nome1, cognome1, MANAGER);
        assertNull(vol);
        vol = Volontario.crea(companyUno, "", cognome1, MANAGER);
        assertNull(vol);
        vol = Volontario.crea(companyDue, nome1, "", MANAGER);
        assertNull(vol);

        //--con i parametri obbligatori
        numPrevisto = 1;
        vol = Volontario.crea(companyUno, nome1, cognome1, MANAGER);
        assertNotNull(vol);
        assertNotNull(vol.getId());
        numOttenuto = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
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
        numOttenuto = Volontario.deleteAll(MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        //--cancello e ricreo i records per riprovare qui
        cancellaRecords();
        reset();
        creaRecords();

        numPrevisto = chiaviUno.size();
        numOttenuto = Volontario.deleteAll(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);

        numPrevisto = chiaviDue.size();
        numOttenuto = Volontario.deleteAll(companyDue, MANAGER);
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
        vol = new Volontario();
        assertNotNull(vol);
        try { // prova ad eseguire il codice
            entity = vol.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(vol.getId());

        //-mancano le properties obbligatorie - NON registra
        vol = new Volontario(null, nome1,cognome1);
        assertNotNull(vol);
        try { // prova ad eseguire il codice
            entity = vol.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(vol.getId());

        //-mancano le properties obbligatorie - NON registra
        vol = new Volontario(companyUno, "",cognome1);
        assertNotNull(vol);
        try { // prova ad eseguire il codice
            entity = vol.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(vol.getId());

        //--mancano le properties obbligatorie - NON registra
        vol = new Volontario(companyUno, nome1,"");
        assertNotNull(vol);
        try { // prova ad eseguire il codice
            entity = vol.save(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(entity);
        assertNull(vol.getId());

        //--ci sono i parametri obbligatori
        numPrevisto = 1;
        vol = new Volontario(companyUno, nome1,cognome1);
        assertNotNull(vol);
        entity = vol.save(companyUno, MANAGER);
        assertNotNull(entity);
        assertTrue(entity instanceof Volontario);
        assertNotNull(vol);
        assertNotNull(vol.getId());
        numOttenuto = Volontario.countByCompany(companyUno, MANAGER);
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
        vol = new Volontario();
        assertNotNull(vol);
        try { // prova ad eseguire il codice
            vol = vol.saveSafe(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(vol);

        //--ci sono i parametri obbligatori
        numPrevisto = 1;
        vol = new Volontario(companyUno, nome1,cognome1);
        assertNotNull(vol);
        vol = vol.save(companyUno, MANAGER);
        assertNotNull(vol);
        assertNotNull(vol.getId());
        numOttenuto = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of single test



    @Test
    /**
     * Clone di questa istanza
     */
    public void cloneTest() {
        Volontario volClonato = null;

        vol = Volontario.find(chiavi.get(0), MANAGER);
        assertNotNull(vol);

        try { // prova ad eseguire il codice
            volClonato = vol.clone();
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNotNull(volClonato);
        assertVolontariUguali(volClonato, vol);
    }// end of single test
    //------------------------------------------------------------------------------------------------------------------------
    // Utilities
    //------------------------------------------------------------------------------------------------------------------------

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
        vol = vol.saveSafe(MANAGER);
        chiavi.add(vol.getId());
        lista.add(vol);
        listaCodeCompanyUnici.add(vol.getCodeCompanyUnico());
    }// end of method

    private void costruttoreNullo(Volontario vol) {
        try { // prova ad eseguire il codice
            vol = vol.saveSafe(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        assertNull(vol);
    }// end of method

    private void costruttoreNulloPrimaCompany(Volontario vol, int numPrevisto) {
        costruttoreNullo(vol);
        numOttenuto = Volontario.countByCompany(companyUno, MANAGER);
        assertEquals(numOttenuto, numPrevisto);
    }// end of method

    private void costruttoreNulloSecondaCompany(Volontario vol, int numPrevisto) {
        costruttoreNullo(vol);
        numOttenuto = Volontario.countByCompany(companyDue, MANAGER);
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
            vol = vol.saveSafe(MANAGER);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        assertNotNull(vol);
        assertNotNull(vol.getId());
    }// end of method

    private void assertListeUguali(List<Volontario> lista1, List<Volontario> lista2) {
        for (int k = 0; k < lista1.size(); k++) {
            assertVolontariUguali(lista1.get(k), lista2.get(k));
        }// end of for cycle
    }// end of method

    private void assertVolontariUguali(Volontario vol1, Volontario vol2) {
        assertEquals(vol1.getId(), vol2.getId());
        assertEquals(vol1.getCodeCompanyUnico(), vol2.getCodeCompanyUnico());
        assertEquals(vol1.getNome(), vol2.getNome());
        assertEquals(vol1.getCognome(), vol2.getCognome());
        assertEquals(vol1.getNickname(), vol2.getNickname());
    }// end of method


}// end of test class