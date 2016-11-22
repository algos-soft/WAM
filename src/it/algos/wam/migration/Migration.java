package it.algos.wam.migration;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.Organizzazione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.domain.log.Log;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibQuery;
import it.algos.webbase.web.lib.LibTime;
import org.vaadin.addons.lazyquerycontainer.LazyEntityContainer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Gac on 25 ago 2016.
 * Importa i dati -on line- della vecchia versione del programma (webambulanze)
 * Il nome della croce (company) può essere diverso
 */
public class Migration {

    protected final static String PERSISTENCE_UNIT_NAME = "Webambulanzelocal";
    private final static boolean USA_TRANSAZIONE = false;
    private final static String DB_OLD_LOCAL = "";
    private final static String DB_OLD_SERVER = "";
    private final static String DB_NEW_LOCAL = "";
    private final static String DB_NEW_SERVER = "";
    private final static boolean SIGLA_MAIUSCOLA = false;
    private EntityManager managerOld;
    private EntityManager managerNew;
    private String dbOld = DB_OLD_LOCAL; // cambiare alla fine dei test
    private String dbNew = DB_OLD_LOCAL; // cambiare alla fine dei test

    private List<FunzioneAmb> listaFunzioniOld;
    private List<ServizioAmb> listaServiziOld;
    private List<VolontarioAmb> listaVolontariOld;
    private List<UtenteAmb> listaUtentiOld;
    private List<TurnoAmb> listaTurniOld;


    /**
     * Costruttore
     */
    public Migration() {
        long inizio;
        creaManagers();

        List<CroceAmb> listaVecchieCrociEsistenti = CroceAmb.findAll(managerOld);
        List<CroceAmb> listaVecchieCrociDaImportare = selezionaCrodiDaImportare(listaVecchieCrociEsistenti);

        if (listaVecchieCrociDaImportare != null) {
            for (CroceAmb company : listaVecchieCrociDaImportare) {
                inizio = System.currentTimeMillis();
                inizia(company, company.getSigla().toLowerCase());
                Log.debug("migration", "Croce " + company.getSigla() + " replicata in " + LibTime.difText(inizio));
            }// end of for cycle
        }// end of if cycle

        chiudeManagers();
    }// end of constructor

    /**
     * Costruttore
     *
     * @param siglaCompanyOld nome della company usata in webambulanze
     */
    public Migration(String siglaCompanyOld) {
        this(siglaCompanyOld, siglaCompanyOld);
    }// end of constructor


    /**
     * Costruttore
     *
     * @param siglaCompanyOld nome della company usata in webambulanze
     * @param siglaCompanyNew nome della company usata in wam
     */
    public Migration(String siglaCompanyOld, String siglaCompanyNew) {
        creaManagers();
        CroceAmb companyOld = CroceAmb.findBySigla(siglaCompanyOld, managerOld);
        inizia(companyOld, siglaCompanyNew);
        chiudeManagers();
    }// end of constructor

    /**
     * Creazione di due manager specifici
     * Uno per la lettura delle vecchie classi (Amb)
     * Uno per la cancellazione/scrittura delle nuove classi (WAM)
     * Si apre e si chiude una transazione per ogni company
     */
    private void creaManagers() {
        chiudeManagers();

        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        if (factory != null) {
            managerOld = factory.createEntityManager();
        }// end of if cycle
        managerNew = EM.createEntityManager();
    }// end of method

    /**
     * Chiusura dei due manager specifici
     */
    private void chiudeManagers() {

        if (managerOld != null) {
            managerOld.close();
            managerOld = null;
        }// end of if cycle

        if (managerNew != null) {
            managerNew.close();
            managerNew = null;
        }// end of if cycle

    }// end of method

    /**
     * Seleziona le croci da importare
     * Elimina ALGOS, di servizio
     * Elimina DEMO, da ricreare ex-nove
     * Elimina PAVT, non più attiva
     *
     * @param listaVecchieCrociEsistenti di webambulanze
     *
     * @return croci da importare da webambulanze in WAM
     */
    private List<CroceAmb> selezionaCrodiDaImportare(List<CroceAmb> listaVecchieCrociEsistenti) {
        List<CroceAmb> listaVecchieCrociDaImportare = new ArrayList<>();
        String[] escluse = {"ALGOS", "algos", "DEMO", "demo", "TEST", "test", "PAVT", "pavt"};
        ArrayList<String> listaEscluse = LibArray.fromString(escluse);
        String code;

        for (CroceAmb croce : listaVecchieCrociEsistenti) {
            code = croce.getSigla();
            if (!listaEscluse.contains(code)) {
                listaVecchieCrociDaImportare.add(croce);
            }// end of if cycle
        }// end of for cycle

        return listaVecchieCrociDaImportare;
    }// end of method

    /**
     * Cerca una company con la sigla siglaCroceNew
     * La cancella, con tutti i dati
     * Cerca una company con la sigla siglaCroceOld
     * Importa i dati
     *
     * @param companyOld      company usata in webambulanze
     * @param siglaCompanyNew nome della company usata in wam
     */
    private void inizia(CroceAmb companyOld, String siglaCompanyNew) {
        creaManagers();
        WamCompany companyNew = WamCompany.findByCode(siglaCompanyNew);
        List<WrapVolontario> listaWrapVolontari = null;

        try { // prova ad eseguire il codice
            managerNew.getTransaction().begin();
            if (companyNew != null) {
                companyNew.delete(managerNew);
            }// fine del blocco if
            managerNew.getTransaction().commit();
        } catch (Exception unErrore) { // intercetta l'errore
            managerNew.getTransaction().rollback();
        }// fine del blocco try-catch

        companyNew = importSingolaCroce(companyOld, siglaCompanyNew, managerNew);

        listaFunzioniOld = FunzioneAmb.findAll(companyOld, managerOld);
        listaServiziOld = ServizioAmb.findAll(companyOld, managerOld);
        listaVolontariOld = VolontarioAmb.getList(companyOld, managerOld);
        listaUtentiOld = UtenteAmb.getList(companyOld, managerOld);
        listaTurniOld = TurnoAmb.findAllRecenti(companyOld, managerOld); //todo provvisorio - levare
//        listaTurniOld = TurnoAmb.findAll(companyOld, managerOld); //todo rimettere

        importFunzioni(companyNew, managerNew);
        importServizi(companyNew, managerNew);
        listaWrapVolontari = importVolontari(companyNew, managerNew);
        importTurni(companyNew, listaWrapVolontari, managerNew);

//        try { // prova ad eseguire il codice
//            managerNew.getTransaction().commit();
//        } catch (Exception unErrore) { // intercetta l'errore
//            managerNew.getTransaction().rollback();
//        }// fine del blocco try-catch
        int a = 87;
    }// end of method

    /**
     * Importa la croce
     *
     * @param companyOld      company usata in webambulanze
     * @param siglaCompanyNew nome della company da usare in wam
     *
     * @return la nuova company
     */
    private WamCompany importSingolaCroce(CroceAmb companyOld, String siglaCompanyNew, EntityManager manager) {
        WamCompany companyNew = null;

        manager.getTransaction().begin();
        try { // prova ad eseguire il codice
            if (SIGLA_MAIUSCOLA) {
                siglaCompanyNew = siglaCompanyNew.toUpperCase();
            } else {
                siglaCompanyNew = siglaCompanyNew.toLowerCase();
            }// end of if/else cycle

            companyNew = new WamCompany();
            companyNew.setCompanyCode(siglaCompanyNew);
            companyNew.setOrganizzazione(recuperaOrganizzazione(companyOld));
            companyNew.setName(companyOld.getDescrizione());
            companyNew.setAddress1(companyOld.getIndirizzo());
            companyNew.setPresidente(companyOld.getPresidente());

            companyNew = (WamCompany) companyNew.save(manager); //todo indispensabile il ritorno, altrimenti NON c'è l'ID

            manager.getTransaction().commit();
        } catch (Exception unErrore) { // intercetta l'errore
            manager.getTransaction().rollback();
        }// fine del blocco try-catch


        return companyNew;
    }// end of method

    /**
     * Patch per carenza di informazioni nella vecchia croce
     */
    private Organizzazione recuperaOrganizzazione(CroceAmb companyOld) {
        Organizzazione organizzazioneNew = Organizzazione.get(companyOld.getOrganizzazione());

        if (companyOld.getSigla().equals("GAPS")) {
            organizzazioneNew = Organizzazione.csv;
        }// end of if cycle

        return organizzazioneNew;
    }// end of method

    /**
     * Importa le funzioni della croce
     *
     * @param companyNew company usata in wam
     */
    private void importFunzioni(WamCompany companyNew, EntityManager manager) {
        List<WrapFunzione> listaWrapFunzioni = new ArrayList<>();
        Funzione funzioneNew;

        //--controlla se la transazione è attiva
        boolean createTransaction;
        createTransaction = LibQuery.isTransactionNotActive(manager);

        try { // prova ad eseguire il codice
            if (createTransaction) {
                manager.getTransaction().begin();
            }// end of if cycle

            if (listaFunzioniOld != null && listaFunzioniOld.size() > 0) {
                for (FunzioneAmb funzioneOld : listaFunzioniOld) {
                    funzioneNew = creaSingolaFunzione(funzioneOld, companyNew, manager);
                    listaWrapFunzioni.add(new WrapFunzione(funzioneOld, funzioneNew));//todo forse da levare
                }// end of for cycle
            }// end of if cycle
            this.recuperaFunzioniDipendenti(listaWrapFunzioni, manager);

            if (createTransaction) {
                manager.getTransaction().commit();
            }// end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
            if (createTransaction) {
                manager.getTransaction().rollback();
            }// end of if cycle
        }// fine del blocco try-catch
    }// end of method

    /**
     * Crea la singola funzione
     *
     * @param funzioneOld della companyOld
     * @param companyNew  company usata in wam
     *
     * @return la nuova funzione
     */
    private Funzione creaSingolaFunzione(FunzioneAmb funzioneOld, WamCompany companyNew, EntityManager manager) {
        String code = funzioneOld.getSigla();
        String sigla = funzioneOld.getSigla_visibile();
        int ordine = funzioneOld.getOrdine();
        String descrizione = funzioneOld.getDescrizione();
        FontAwesome glyph = selezionaIcona(descrizione);

        return Funzione.crea(companyNew, code, sigla, descrizione, ordine, glyph, manager, null);
    }// end of method

    /**
     * Elabora la vecchia descrizione per selezionare una icona adeguata
     *
     * @param descrizione usata in webambulanze
     *
     * @return la FontAwesome selezionata
     */
    private FontAwesome selezionaIcona(String descrizione) {
        FontAwesome glyph = null;
        String autista = "utista";
        String soc = "Soccorritore";
        String soc2 = "Primo";
        String ser = "Centralino";
        String ser2 = "Pulizie";
        String ser3 = "Ufficio";

        if (descrizione.contains(autista)) {
            glyph = FontAwesome.AMBULANCE;
        } else {
            if (descrizione.contains(soc) || descrizione.contains(soc2)) {
                glyph = FontAwesome.HEART;
            } else {
                if (descrizione.contains(ser) || descrizione.contains(ser2) || descrizione.contains(ser3)) {
                    glyph = FontAwesome.USER;
                } else {
                    glyph = FontAwesome.STETHOSCOPE;
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if/else cycle

        return glyph;
    }// end of method

    /**
     * Solo dopo che sono state create tutte
     */
    private void recuperaFunzioniDipendenti(List<WrapFunzione> listaWrapFunzioni, EntityManager manager) {
        FunzioneAmb funzOld;
        Funzione funzNew;
        String funzioniDipendentiStr;
        List<String> funzioniDipendentiArray;
        String codeNew;
        WamCompany company;

        for (WrapFunzione wrap : listaWrapFunzioni) {
            funzOld = wrap.getFunzioneOld();
            funzioniDipendentiStr = funzOld.getFunzioni_dipendenti();
            funzioniDipendentiArray = LibArray.fromStringaVirgola(funzioniDipendentiStr);

            if (funzioniDipendentiArray != null) {
                funzNew = wrap.getFunzioneNew();
                company = (WamCompany) funzNew.getCompany();
                for (String siglaOld : funzioniDipendentiArray) {
                    Funzione funzDipNew;
                    codeNew = siglaOld;
                    funzDipNew = Funzione.getEntityByCompanyAndCode(company, codeNew, manager);
                    if (funzDipNew != null) {
                        funzNew.funzioniDipendenti.add(funzDipNew);
                    }// end of if cycle
                }// end of for cycle
                funzNew.save((WamCompany) funzNew.getCompany(), manager);
            }// end of if cycle

        }// end of for cycle
    }// end of method

    /**
     * Importa i servizi della croce
     *
     * @param companyNew company usata in wam
     */
    private void importServizi(WamCompany companyNew, EntityManager manager) {
        //--controlla se la transazione è attiva
        boolean createTransaction;
        createTransaction = LibQuery.isTransactionNotActive(manager);

        try { // prova ad eseguire il codice
            if (createTransaction) {
                manager.getTransaction().begin();
            }// end of if cycle

            if (listaServiziOld != null && listaServiziOld.size() > 0) {
                for (ServizioAmb servizioOld : listaServiziOld) {
                    creaSingoloServizio(servizioOld, companyNew, manager);
                }// end of for cycle
            }// end of if cycle

            if (createTransaction) {
                manager.getTransaction().commit();
            }// end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
            if (createTransaction) {
                manager.getTransaction().rollback();
            }// end of if cycle
        }// fine del blocco try-catch
    }// end of method

    /**
     * Crea il singola servizio
     *
     * @param servizioOld della companyOld
     * @param companyNew  company usata in wam
     *
     * @return il nuovo servizio
     */
    @SuppressWarnings("all")
    private Servizio creaSingoloServizio(ServizioAmb servizioOld, WamCompany companyNew, EntityManager manager) {
        int ordine = servizioOld.getOrdine();
        String sigla = servizioOld.getSigla();
        String descrizione = servizioOld.getDescrizione();
        int numObbligatorie = servizioOld.getFunzioni_obbligatorie();
        List<ServizioFunzione> listaServizioFunzioni = recuperaFunzioni(companyNew, servizioOld, numObbligatorie, manager);
        int colore = 0;
        int oraInizio = servizioOld.getOra_inizio();
        int minutiInizio = servizioOld.getMinuti_inizio();
        int oraFine = servizioOld.getOra_fine();
        int minutiFine = servizioOld.getMinuti_fine();

        return Servizio.crea(companyNew, sigla, true, descrizione, ordine, colore, true, oraInizio, minutiInizio, oraFine, minutiFine, manager, listaServizioFunzioni);
    }// end of method

    /**
     * Crea la lista delle funzioni per il servizio
     *
     * @param servizioOld      della companyOld
     * @param listaFunzioniAll nuove appena create
     *
     * @return lista di nuove funzioni
     */
    @SuppressWarnings("all")
    private List<ServizioFunzione> recuperaFunzioni(WamCompany companyNew, ServizioAmb servizioOld, int numObbligatorie, EntityManager manager) {
        List<ServizioFunzione> listaServizioFunzioni = new ArrayList<>();
        ServizioFunzione servFunz;
        FunzioneAmb funzOld = null;
        String code;
        boolean obbligatoria = false;
        ArrayList<Long> chiavi = new ArrayList();
        chiavi.add(servizioOld.getFunzione1_id());
        chiavi.add(servizioOld.getFunzione2_id());
        chiavi.add(servizioOld.getFunzione3_id());
        chiavi.add(servizioOld.getFunzione4_id());
        int pos = 0;
        Funzione funz;

        for (long key : chiavi) {
            pos++;
            funzOld = null;
            if (key > 0) {
                funzOld = FunzioneAmb.find(key, managerOld);
            }// end of if cycle
            if (funzOld != null) {
                code = funzOld.getSigla();
                funz = Funzione.getEntityByCompanyAndCode(companyNew, code, manager);
                if (funz != null) {
                    servFunz = new ServizioFunzione(companyNew, null, funz, obbligatoria, pos);
                    listaServizioFunzioni.add(servFunz);
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

        return listaServizioFunzioni;
    }// end of method

    /**
     * Importa i volontari della croce
     *
     * @param companyNew company usata in wam
     */
    private List<WrapVolontario> importVolontari(WamCompany companyNew, EntityManager manager) {
        List<WrapVolontario> listaWrapVolontari = new ArrayList<>();
        Volontario volontarioNew = null;

        if (USA_TRANSAZIONE) {
            //--controlla se la transazione è attiva
            boolean createTransaction;
            createTransaction = LibQuery.isTransactionNotActive(manager);

            try { // prova ad eseguire il codice
                if (createTransaction) {
                    manager.getTransaction().begin();
                }// end of if cycle

                if (listaVolontariOld != null && listaVolontariOld.size() > 0) {
                    for (VolontarioAmb volontarioOld : listaVolontariOld) {
                        volontarioNew = creaSingoloVolontario(volontarioOld, companyNew, manager);
                        listaWrapVolontari.add(new WrapVolontario(volontarioOld, volontarioNew));//todo forse non serve
                    }// end of for cycle
                }// end of if cycle

                this.recuperaAdmin(listaWrapVolontari, manager);

                if (createTransaction) {
                    manager.getTransaction().commit();
                }// end of if cycle
            } catch (Exception unErrore) { // intercetta l'errore
                if (createTransaction) {
                    manager.getTransaction().rollback();
                }// end of if cycle
            }// fine del blocco try-catch
        } else {
            if (listaVolontariOld != null && listaVolontariOld.size() > 0) {
                for (VolontarioAmb volontarioOld : listaVolontariOld) {
                    volontarioNew = creaSingoloVolontario(volontarioOld, companyNew, manager);
                    listaWrapVolontari.add(new WrapVolontario(volontarioOld, volontarioNew));//todo forse non serve
                }// end of for cycle
            }// end of if cycle

            this.recuperaAdmin(listaWrapVolontari, manager);
        }// end of if/else cycle

        return listaWrapVolontari;
    }// end of method

    /**
     * Crea il singolo volontario
     *
     * @param companyNew       company usata in wam
     * @param volontarioOld    della companyOld
     * @param listaFunzioniAll nuove appena create
     *
     * @return il nuovo volontario
     */
    @SuppressWarnings("all")
    private Volontario creaSingoloVolontario(VolontarioAmb volontarioOld, WamCompany companyNew, EntityManager manager) {
        Volontario volontario = null;

        String nome = volontarioOld.getNome();
        String cognome = volontarioOld.getCognome();
        Date dataNascita = volontarioOld.getData_nascita();
        String cellulare = volontarioOld.getTelefono_cellulare();
        String telefono = volontarioOld.getTelefono_fisso();
        String email = volontarioOld.getEmail();
        String password = recuperaPassword(volontarioOld);
        String note = volontarioOld.getNote();
        boolean admin = false;
        boolean dipendente = volontarioOld.isDipendente();
        boolean attivo = volontarioOld.isAttivo();
        int oreAnno = volontarioOld.getOre_anno();
        int turniAnno = volontarioOld.getTurni_anno();
        int oreExtra = volontarioOld.getOre_extra();
        Date scadenzaBLSD = null;
        Date scadenzaPNT = null;
        Date scadenzaBPHTP = null;
        List<Funzione> funzioni = recuperaFunzioni(volontarioOld, companyNew, manager);

        if (password.equals("")) {
            attivo = false;
        }// end of if cycle

        try { // prova ad eseguire il codice
            volontario = Volontario.crea(
                    companyNew,
                    manager,
                    nome,
                    cognome,
                    dataNascita,
                    cellulare,
                    telefono,
                    email,
                    password,
                    note,
                    admin,
                    dipendente,
                    attivo,
                    scadenzaBLSD,
                    scadenzaPNT,
                    scadenzaBPHTP,
                    oreAnno,
                    turniAnno,
                    oreExtra,
                    funzioni);
        } catch (Exception unErrore) { // intercetta l'errore
            int a = 87;
        }// fine del blocco try-catch

        return volontario;
    }// end of method

    private List<Funzione> recuperaFunzioni(VolontarioAmb volontarioOld, WamCompany companyNew, EntityManager manager) {
        List<Funzione> funzioniNew = new ArrayList<>();
        List<MiliteFunzioneAmb> listaIncroci;
        long keyMiliteID = volontarioOld.getId();
        long keyFunzioneOld;
        Funzione funzNew;

        listaIncroci = MiliteFunzioneAmb.getListByMilite(keyMiliteID, managerOld);
        if (listaIncroci != null && listaIncroci.size() > 0) {
            for (MiliteFunzioneAmb incrocio : listaIncroci) {
                keyFunzioneOld = incrocio.getFunzione_id();
                FunzioneAmb funzOld = FunzioneAmb.find(keyFunzioneOld, managerOld);
                String code = funzOld.getSigla();
                funzNew = Funzione.getEntityByCompanyAndCode(companyNew, code, manager);
                funzioniNew.add(funzNew);
            }// end of for cycle
        }// end of if cycle

        return funzioniNew;
    }// end of method


    private void recuperaAdmin(List<WrapVolontario> listaWrap, EntityManager manager) {
        UtenteRuoloAmb utenteRuolo;
        long utenteAmbID;
        Volontario volontarioNew;
        UtenteAmb utenteAmb;
        VolontarioAmb volontarioAmb;

        LazyEntityContainer<UtenteRuoloAmb> container = new LazyEntityContainer(managerOld, UtenteRuoloAmb.class, 1000, null, true, true, true);
        container.addContainerProperty("ruolo_id", Long.class, 0L, true, true);
        container.addContainerProperty("utente_id", Long.class, 0L, true, true);

        // filtro
        Container.Filter filter = new Compare.Equal("ruolo_id", 3);
        container.addContainerFilter(filter);
        Collection itemIds = container.getItemIds();
        int size = container.size();

        for (Object id : itemIds) {
            utenteRuolo = container.getEntity(id);
            utenteAmbID = utenteRuolo.getUtente_id();
            utenteAmb = UtenteAmb.find(utenteAmbID, managerOld);
            volontarioAmb = utenteAmb.getMilite();
            if (volontarioAmb != null) {
                volontarioNew = recuperaVolontarioNew(listaWrap, volontarioAmb);
                if (volontarioNew != null) {
                    volontarioNew.setAdmin(true);
                    volontarioNew.save(manager);
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

    }// end of method


    private String recuperaPassword(VolontarioAmb volontarioOld) {
        String password = "";
        UtenteAmb utente = UtenteAmb.getEntityByVolontario(listaUtentiOld, volontarioOld);

        if (utente != null) {
            password = utente.getPass();
        }// end of if cycle

        return password;
    }// end of method


    private List<Turno> importTurni(WamCompany companyNew, List<WrapVolontario> listaWrapVolontari, EntityManager manager) {
        List<Turno> listaTurniNew = new ArrayList<>();
        Turno turnoNew;
        int k = 0;
        int delta = 150;

        if (USA_TRANSAZIONE) {
            //--controlla se la transazione è attiva
            boolean createTransaction;
            createTransaction = LibQuery.isTransactionNotActive(manager);

            try { // prova ad eseguire il codice
                if (createTransaction) {
                    manager.getTransaction().begin();
                }// end of if cycle

                if (listaTurniOld != null && listaTurniOld.size() > 0) {
                    for (TurnoAmb turnoOld : listaTurniOld) {
                        turnoNew = creaSingoloTurno(companyNew, turnoOld, listaWrapVolontari, manager);
                        listaTurniNew.add(turnoNew);
                        k++;
                        if (k > delta) {
                            break;//todo provvisorio
                        }// end of if cycle
                    }// end of for cycle
                }// end of if cycle

                if (createTransaction) {
                    manager.getTransaction().commit();
                }// end of if cycle
            } catch (Exception unErrore) { // intercetta l'errore
                if (createTransaction) {
                    manager.getTransaction().rollback();
                }// end of if cycle
            }// fine del blocco try-catch
        } else {
            if (listaTurniOld != null && listaTurniOld.size() > 0) {
                for (TurnoAmb turnoOld : listaTurniOld) {
                    turnoNew = creaSingoloTurno(companyNew, turnoOld, listaWrapVolontari, manager);
                    listaTurniNew.add(turnoNew);
                    k++;
                    if (k > delta) {
                        break;//todo provvisorio
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of if/else cycle

        return listaTurniNew;
    }// end of method

    private Turno creaSingoloTurno(WamCompany companyNew, TurnoAmb turnoOld, List<WrapVolontario> listaWrapVolontari, EntityManager manager) {
        Turno turnoNew = null;
        Servizio servizio = recuperaServizio(companyNew, turnoOld, manager);
        Date inizio = turnoOld.getInizio();
        Date fine = turnoOld.getFine();
        List<Iscrizione> iscrizioni = null;
        String titoloExtra = turnoOld.getTitolo_extra();
        String localitaExtra = turnoOld.getLocalità_extra();
        String note = turnoOld.getNote();

        turnoNew = Turno.crea(companyNew, servizio, inizio, fine, manager);
        turnoNew.setIscrizioni(recuperaIscrizioni(turnoOld, turnoNew, companyNew, servizio, listaWrapVolontari, manager));
        turnoNew.setTitoloExtra(titoloExtra);
        turnoNew.setLocalitaExtra(localitaExtra);
        turnoNew.setNote(note);
        turnoNew = turnoNew.save(companyNew, manager);

        return turnoNew;
    }// end of method

    private Servizio recuperaServizio(WamCompany company, TurnoAmb turnoOld, EntityManager manager) {
        Servizio servizioNew = null;
        ServizioAmb servizioOld = null;
        String sigla;

        if (turnoOld != null) {
            servizioOld = turnoOld.getTipo_turno();
            if (servizioOld != null) {
                sigla = servizioOld.getSigla();

                if (!sigla.equals("")) {
                    servizioNew = Servizio.getEntityByCompanyAndSigla(company, sigla, manager);
                }// end of if cycle
            }// end of if cycle
        }// end of if cycle

        return servizioNew;
    }// end of method


    private Servizio recuperaServizioNew(List<WrapServizio> listaWrapServizi, ServizioAmb servizioOld) {
        Servizio servizioNew = null;

        if (listaWrapServizi != null && servizioOld != null) {
            for (WrapServizio wrap : listaWrapServizi) {
                if (wrap.getServizioOld().equals(servizioOld)) {
                    servizioNew = wrap.getServizioNew();
                    break;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return servizioNew;
    }// end of method

    private List<Iscrizione> recuperaIscrizioni(TurnoAmb turnoOld, Turno turnoNew, WamCompany company, Servizio servizio, List<WrapVolontario> listaWrapVolontari, EntityManager manager) {
        List<Iscrizione> iscrizioni = new ArrayList<>();
        Iscrizione iscrizione;

        iscrizione = recuperaIscrizione1(turnoOld, turnoNew, company, servizio, listaWrapVolontari, manager);
        if (iscrizione != null) {
            iscrizioni.add(iscrizione);
        }// end of if cycle

        iscrizione = recuperaIscrizione2(turnoOld, turnoNew, company, servizio, listaWrapVolontari, manager);
        if (iscrizione != null) {
            iscrizioni.add(iscrizione);
        }// end of if cycle

        iscrizione = recuperaIscrizione3(turnoOld, turnoNew, company, servizio, listaWrapVolontari, manager);
        if (iscrizione != null) {
            iscrizioni.add(iscrizione);
        }// end of if cycle

        iscrizione = recuperaIscrizione4(turnoOld, turnoNew, company, servizio, listaWrapVolontari, manager);
        if (iscrizione != null) {
            iscrizioni.add(iscrizione);
        }// end of if cycle

        if (iscrizioni.size() == 0) {
            iscrizioni = null;
        }// end of if cycle

        return iscrizioni;
    }// end of method


    private Iscrizione recuperaIscrizione1(TurnoAmb turnoOld, Turno turnoNew, WamCompany company, Servizio serv, List<WrapVolontario> listaWrapVolontari, EntityManager manager) {
        Iscrizione iscrizione = null;
        FunzioneAmb funzioneOld;
        String siglaOld;
        String codeNew;
        Funzione funzioneNew = null;
        VolontarioAmb volontarioOld;
        Volontario volontarioNew;
        ServizioFunzione serFunz = null;
        boolean esisteProblema;

        funzioneOld = turnoOld.getFunzione1();
        if (funzioneOld != null) {
            siglaOld = funzioneOld.getSigla();
            codeNew = siglaOld;
            funzioneNew = Funzione.getEntityByCompanyAndCode(company, codeNew, manager);
        }// end of if cycle

        volontarioOld = turnoOld.getMilite_funzione1();
        esisteProblema = turnoOld.isProblemi_funzione1();

        if (funzioneNew != null && volontarioOld != null) {
            volontarioNew = recuperaVolontarioNew(listaWrapVolontari, volontarioOld);
            serFunz = ServizioFunzione.findByServFunz(company, serv, funzioneNew, manager);
            iscrizione = new Iscrizione(company, turnoNew, volontarioNew, serFunz);
            iscrizione.setEsisteProblema(esisteProblema);
        }// end of if cycle

        return iscrizione;
    }// end of method

    private Iscrizione recuperaIscrizione2(TurnoAmb turnoOld, Turno turnoNew, WamCompany company, Servizio serv, List<WrapVolontario> listaWrapVolontari, EntityManager manager) {
        Iscrizione iscrizione = null;
        FunzioneAmb funzioneOld;
        String siglaOld;
        String codeNew;
        Funzione funzioneNew = null;
        VolontarioAmb volontarioOld;
        Volontario volontarioNew;
        ServizioFunzione serFunz = null;
        boolean esisteProblema;

        funzioneOld = turnoOld.getFunzione2();
        if (funzioneOld != null) {
            siglaOld = funzioneOld.getSigla();
            codeNew = siglaOld;
            funzioneNew = Funzione.getEntityByCompanyAndCode(company, codeNew, manager);
        }// end of if cycle

        volontarioOld = turnoOld.getMilite_funzione2();
        esisteProblema = turnoOld.isProblemi_funzione2();

        if (funzioneNew != null && volontarioOld != null) {
            volontarioNew = recuperaVolontarioNew(listaWrapVolontari, volontarioOld);
            serFunz = ServizioFunzione.findByServFunz(company, serv, funzioneNew, manager);
            iscrizione = new Iscrizione(company, turnoNew, volontarioNew, serFunz);
            iscrizione.setEsisteProblema(esisteProblema);
        }// end of if cycle

        return iscrizione;
    }// end of method

    private Iscrizione recuperaIscrizione3(TurnoAmb turnoOld, Turno turnoNew, WamCompany company, Servizio serv, List<WrapVolontario> listaWrapVolontari, EntityManager manager) {
        Iscrizione iscrizione = null;
        FunzioneAmb funzioneOld;
        String siglaOld;
        String codeNew;
        Funzione funzioneNew = null;
        VolontarioAmb volontarioOld;
        Volontario volontarioNew;
        ServizioFunzione serFunz = null;
        boolean esisteProblema;

        funzioneOld = turnoOld.getFunzione3();
        if (funzioneOld != null) {
            siglaOld = funzioneOld.getSigla();
            codeNew = siglaOld;
            funzioneNew = Funzione.getEntityByCompanyAndCode(company, codeNew, manager);
        }// end of if cycle

        volontarioOld = turnoOld.getMilite_funzione3();
        esisteProblema = turnoOld.isProblemi_funzione3();

        if (funzioneNew != null && volontarioOld != null) {
            volontarioNew = recuperaVolontarioNew(listaWrapVolontari, volontarioOld);
            serFunz = ServizioFunzione.findByServFunz(company, serv, funzioneNew, manager);
            iscrizione = new Iscrizione(company, turnoNew, volontarioNew, serFunz);
            iscrizione.setEsisteProblema(esisteProblema);
        }// end of if cycle

        return iscrizione;
    }// end of method

    private Iscrizione recuperaIscrizione4(TurnoAmb turnoOld, Turno turnoNew, WamCompany company, Servizio serv, List<WrapVolontario> listaWrapVolontari, EntityManager manager) {
        Iscrizione iscrizione = null;
        FunzioneAmb funzioneOld;
        String siglaOld;
        String codeNew;
        Funzione funzioneNew = null;
        VolontarioAmb volontarioOld;
        Volontario volontarioNew;
        ServizioFunzione serFunz = null;
        boolean esisteProblema;

        funzioneOld = turnoOld.getFunzione4();
        if (funzioneOld != null) {
            siglaOld = funzioneOld.getSigla();
            codeNew = siglaOld;
            funzioneNew = Funzione.getEntityByCompanyAndCode(company, codeNew, manager);
        }// end of if cycle

        volontarioOld = turnoOld.getMilite_funzione4();
        esisteProblema = turnoOld.isProblemi_funzione4();

        if (funzioneNew != null && volontarioOld != null) {
            volontarioNew = recuperaVolontarioNew(listaWrapVolontari, volontarioOld);
            serFunz = ServizioFunzione.findByServFunz(company, serv, funzioneNew, manager);
            iscrizione = new Iscrizione(company, turnoNew, volontarioNew, serFunz);
            iscrizione.setEsisteProblema(esisteProblema);
        }// end of if cycle

        return iscrizione;
    }// end of method

    private Volontario recuperaVolontarioNew(List<WrapVolontario> listaWrapVolontari, VolontarioAmb volontarioOld) {
        Volontario volontarioNew = null;

        if (volontarioOld != null) {
            for (WrapVolontario wrap : listaWrapVolontari) {
                if (wrap.getVolontarioOld().equals(volontarioOld)) {
                    volontarioNew = wrap.getVolontarioNew();
                    break;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return volontarioNew;
    }// end of method

    private Volontario recuperaVolontarioNew(List<WrapVolontario> listaWrapVolontari, long volontarioOldID) {
        VolontarioAmb volontarioOld = VolontarioAmb.find(volontarioOldID, managerOld);


        return recuperaVolontarioNew(listaWrapVolontari, volontarioOldID);
    }// end of method

    private class WrapFunzione {
        FunzioneAmb funzioneOld;
        Funzione funzioneNew;

        public WrapFunzione(FunzioneAmb funzioneOld, Funzione funzioneNew) {
            this.setFunzioneOld(funzioneOld);
            this.setFunzioneNew(funzioneNew);
        }// end of constructor

        public FunzioneAmb getFunzioneOld() {
            return funzioneOld;
        }// end of getter method

        public void setFunzioneOld(FunzioneAmb funzioneOld) {
            this.funzioneOld = funzioneOld;
        }//end of setter method

        public Funzione getFunzioneNew() {
            return funzioneNew;
        }// end of getter method

        public void setFunzioneNew(Funzione funzioneNew) {
            this.funzioneNew = funzioneNew;
        }//end of setter method
    }// end of internal class

    private class WrapServizio {
        ServizioAmb servizioOld;
        Servizio servizioNew;

        public WrapServizio(ServizioAmb servizioOld, Servizio servizioNew) {
            this.setServizioOld(servizioOld);
            this.setServizioNew(servizioNew);
        }// end of constructor

        public ServizioAmb getServizioOld() {
            return servizioOld;
        }// end of getter method

        public void setServizioOld(ServizioAmb servizioOld) {
            this.servizioOld = servizioOld;
        }//end of setter method

        public Servizio getServizioNew() {
            return servizioNew;
        }// end of getter method

        public void setServizioNew(Servizio servizioNew) {
            this.servizioNew = servizioNew;
        }//end of setter method
    }// end of internal class

    private class WrapVolontario {
        VolontarioAmb volontarioOld;
        Volontario volontarioNew;

        public WrapVolontario(VolontarioAmb volontarioOld, Volontario volontarioNew) {
            this.setVolontarioOld(volontarioOld);
            this.setVolontarioNew(volontarioNew);
        }// end of constructor

        public VolontarioAmb getVolontarioOld() {
            return volontarioOld;
        }// end of getter method

        public void setVolontarioOld(VolontarioAmb volontarioOld) {
            this.volontarioOld = volontarioOld;
        }//end of setter method

        public Volontario getVolontarioNew() {
            return volontarioNew;
        }// end of getter method

        public void setVolontarioNew(Volontario volontarioNew) {
            this.volontarioNew = volontarioNew;
        }//end of setter method
    }// end of internal class

}// end of class
