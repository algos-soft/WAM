package it.algos.wam.migration;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.Organizzazione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.query.SortProperty;
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
    private final static String DB_OLD_LOCAL = "";
    private final static String DB_OLD_SERVER = "";
    private final static String DB_NEW_LOCAL = "";
    private final static String DB_NEW_SERVER = "";
    private final static boolean SIGLA_MAIUSCOLA = false;
    private static EntityManager MANAGER = getManager();
    private String dbOld = DB_OLD_LOCAL; // cambiare alla fine dei test
    private String dbNew = DB_OLD_LOCAL; // cambiare alla fine dei test

    /**
     * Costruttore
     */
    public Migration() {
        List<CroceAmb> listaVecchieCrociEsistenti = CroceAmb.findAll(MANAGER);
        List<CroceAmb> listaVecchieCrociDaImportare = selezionaCrodiDaImportare(listaVecchieCrociEsistenti);

        if (listaVecchieCrociDaImportare != null) {
            for (CroceAmb company : listaVecchieCrociDaImportare) {
                inizia(company, company.getSigla().toLowerCase());
            }// end of for cycle
        }// end of if cycle

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
        CroceAmb companyOld = CroceAmb.findBySigla(siglaCompanyOld, MANAGER);
        inizia(companyOld, siglaCompanyNew);
    }// end of constructor

    /**
     * Creazione di un manager specifico
     * DEVE essere chiuso (must be close by caller method)
     *
     * @return manager specifico
     */
    private static EntityManager getManager() {
        EntityManager manager = null;
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        if (factory != null) {
            manager = factory.createEntityManager();
        }// end of if cycle

        return manager;
    }// end of method

    /**
     * Seleziona le croci da importare
     * Elimina ALGOS, di servizio
     * Elimina DEMO, da ricreare ex-nove
     * Elimina PAVT, non più attiva
     *
     * @param listaVecchieCrociEsistenti di webambulanze
     * @return croci da importare da webambulanze in WAM
     */
    private List<CroceAmb> selezionaCrodiDaImportare(List<CroceAmb> listaVecchieCrociEsistenti) {
        List<CroceAmb> listaVecchieCrociDaImportare = new ArrayList<>();
        String[] escluse = {"ALGOS", "DEMO", "PAVT"};
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
        WamCompany companyNew = WamCompany.findByCode(siglaCompanyNew);
        List<WrapFunzione> listaWrapFunzioni;
        List<Funzione> listaFunzioniNew;
        List<WrapServizio> listaWrapServizi;
        List<WrapVolontario> listaWrapVolontari;

        if (companyNew != null) {
            companyNew.delete();
        }// fine del blocco if

        companyNew = importSingolaCroce(companyOld, siglaCompanyNew);
        listaWrapFunzioni = importFunzioni(companyOld, companyNew);
        listaFunzioniNew = getSoloFunzioniNew(listaWrapFunzioni);
        listaWrapServizi = importServizi(companyOld, companyNew, listaFunzioniNew);
        listaWrapVolontari = importaVolontari(companyOld, companyNew, listaWrapFunzioni);
        importaTurni(companyOld, companyNew, listaWrapServizi, listaWrapVolontari);
    }// end of method

    /**
     * Importa la croce
     *
     * @param companyOld      company usata in webambulanze
     * @param siglaCompanyNew nome della company da usare in wam
     * @return la nuova company
     */
    private WamCompany importSingolaCroce(CroceAmb companyOld, String siglaCompanyNew) {
        WamCompany companyNew;

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
        companyNew.save();

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
     * @param companyOld company usata in webambulanze
     * @param companyNew company usata in wam
     */
    private List<WrapFunzione> importFunzioni(CroceAmb companyOld, WamCompany companyNew) {
        List<WrapFunzione> listaWrapFunzioni = new ArrayList<>();
        Funzione funzioneNew;
        List<FunzioneAmb> listaFunzioniOld = FunzioneAmb.findAll(companyOld, MANAGER);

        if (listaFunzioniOld != null && listaFunzioniOld.size() > 0) {
            for (FunzioneAmb funzioneOld : listaFunzioniOld) {
                funzioneNew = creaSingolaFunzione(companyNew, funzioneOld);
                listaWrapFunzioni.add(new WrapFunzione(funzioneOld, funzioneNew));
            }// end of for cycle
        }// end of if cycle

        return listaWrapFunzioni;
    }// end of method

    /**
     * Crea la singola funzione
     *
     * @param companyNew  company usata in wam
     * @param funzioneOld della companyOld
     * @return la nuova funzione
     */
    private Funzione creaSingolaFunzione(WamCompany companyNew, FunzioneAmb funzioneOld) {
        String code = funzioneOld.getSigla();
        String sigla = funzioneOld.getSigla_visibile();
        int ordine = funzioneOld.getOrdine();
        String descrizione = funzioneOld.getDescrizione();
        FontAwesome glyph = selezionaIcona(descrizione);

        return Funzione.crea(companyNew, code, sigla, descrizione, ordine, glyph);
    }// end of method

    /**
     * Elabora la vecchia descrizione per selezionare una icona adeguata
     *
     * @param descrizione usata in webambulanze
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
     * Importa i servizi della croce
     *
     * @param companyOld    company usata in webambulanze
     * @param companyNew    company usata in wam
     * @param listaFunzioni nuove appena create
     */
    private List<WrapServizio> importServizi(CroceAmb companyOld, WamCompany companyNew, List<Funzione> listaFunzioni) {
        List<WrapServizio> listaWrapServizi = new ArrayList<>();
        List<ServizioAmb> lista = ServizioAmb.findAll(companyOld, MANAGER);
        Servizio servizioNew;

        if (lista != null && lista.size() > 0) {
            for (ServizioAmb servizioOld : lista) {
                servizioNew = creaSingoloServizio(companyNew, servizioOld, listaFunzioni);
                listaWrapServizi.add(new WrapServizio(servizioOld, servizioNew));
            }// end of for cycle
        }// end of if cycle

        return listaWrapServizi;
    }// end of method

    /**
     * Crea il singola servizio
     *
     * @param companyNew       company usata in wam
     * @param servizioOld      della companyOld
     * @param listaFunzioniAll nuove appena create
     * @return il nuovo servizio
     */
    @SuppressWarnings("all")
    private Servizio creaSingoloServizio(WamCompany companyNew, ServizioAmb servizioOld, List<Funzione> listaFunzioniAll) {
        int ordine = servizioOld.getOrdine();
        String sigla = servizioOld.getSigla();
        String descrizione = servizioOld.getDescrizione();
        int numObbligatorie = servizioOld.getFunzioni_obbligatorie();
        List<ServizioFunzione> listaServizioFunzioni = recuperaFunzioni(companyNew, servizioOld, numObbligatorie, listaFunzioniAll);
        int colore = 0;
        int oraInizio = servizioOld.getOra_inizio();
        int minutiInizio = servizioOld.getMinuti_inizio();
        int oraFine = servizioOld.getOra_fine();
        int minutiFine = servizioOld.getMinuti_fine();

        return Servizio.crea(companyNew, sigla, true, descrizione, ordine, colore, true, oraInizio, minutiInizio, oraFine, minutiFine, null, listaServizioFunzioni);
    }// end of method

    /**
     * Crea la lista delle funzioni per il servizio
     *
     * @param servizioOld      della companyOld
     * @param listaFunzioniAll nuove appena create
     * @return lista di nuove funzioni
     */
    @SuppressWarnings("all")
    private List<ServizioFunzione> recuperaFunzioni(WamCompany companyNew, ServizioAmb servizioOld, int numObbligatorie, List<Funzione> listaFunzioniAll) {
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

        for (long key : chiavi) {
            pos++;
            funzOld = null;
            if (key > 0) {
                funzOld = FunzioneAmb.find(key, MANAGER);
            }// end of if cycle
            if (funzOld != null) {
                code = funzOld.getSigla();
                for (Funzione funz : listaFunzioniAll) {
                    obbligatoria = false;
                    if (code.equals(funz.getCode())) {
                        if (pos <= numObbligatorie) {
                            obbligatoria = true;
                        }// end of if cycle
                        servFunz = new ServizioFunzione(companyNew, null, funz, obbligatoria, pos);
                        listaServizioFunzioni.add(servFunz);
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of for cycle

        return listaServizioFunzioni;
    }// end of method

    /**
     * Importa i volontari della croce
     *
     * @param companyOld           company usata in webambulanze
     * @param companyNew           company usata in wam
     * @param listaWrapFunzioniAll vecchie e nuove appena create
     */
    private List<WrapVolontario> importaVolontari(CroceAmb companyOld, WamCompany companyNew, List<WrapFunzione> listaWrapFunzioniAll) {
        List<WrapVolontario> listaWrapVolontari = new ArrayList<>();
        List<VolontarioAmb> listaVolontari = VolontarioAmb.findAll(companyOld, MANAGER);
        Volontario volontarioNew = null;
        List<UtenteAmb> listaUtenti = UtenteAmb.getList(MANAGER);

        if (listaVolontari != null && listaVolontari.size() > 0) {
            for (VolontarioAmb volontarioOld : listaVolontari) {
                volontarioNew = creaSingoloVolontario(companyNew, listaUtenti, volontarioOld, listaWrapFunzioniAll);
                listaWrapVolontari.add(new WrapVolontario(volontarioOld, volontarioNew));
            }// end of for cycle
        }// end of if cycle

        return listaWrapVolontari;
    }// end of method

    /**
     * Crea il singolo volontario
     *
     * @param companyNew       company usata in wam
     * @param volontarioOld    della companyOld
     * @param listaFunzioniAll nuove appena create
     * @return il nuovo volontario
     */
    @SuppressWarnings("all")
    private Volontario creaSingoloVolontario(WamCompany companyNew, List<UtenteAmb> listaUtenti, VolontarioAmb volontarioOld, List<WrapFunzione> listaWrapFunzioniAll) {
        Volontario volontario = null;

        String nome = volontarioOld.getNome();
        String cognome = volontarioOld.getCognome();
        String cellulare = volontarioOld.getTelefono_cellulare();
        String telefono = volontarioOld.getTelefono_fisso();
        String email = volontarioOld.getEmail();
        Date dataNascita = volontarioOld.getData_nascita();
        String note = volontarioOld.getNote();
        boolean dipendente = volontarioOld.isDipendente();
        boolean attivo = volontarioOld.isAttivo();
        int oreAnno = volontarioOld.getOre_anno();
        int turniAnno = volontarioOld.getTurni_anno();
        int oreExtra = volontarioOld.getOre_extra();
        boolean admin = recuperaAdmin(listaUtenti, volontarioOld);
        String password = recuperaPassword(listaUtenti, volontarioOld);
        List<Funzione> funzioni = recuperaFunzioni(listaWrapFunzioniAll, volontarioOld);

        volontario = Volontario.crea(companyNew, null, nome, cognome, cellulare, email, "", false, funzioni);
        if (volontario != null) {
            volontario.setTelefono(telefono);
            volontario.setDataNascita(dataNascita);
            volontario.setNote(note);
            volontario.setDipendente(dipendente);
            volontario.setAttivo(attivo);
            volontario.setOreAnno(oreAnno);
            volontario.setTurniAnno(turniAnno);
            volontario.setOreExtra(oreExtra);
            volontario.setAdmin(admin);
            volontario.setPassword(password);
            if (password.equals("")) {
                volontario.setAttivo(false);
            }// end of if cycle

            volontario = (Volontario) volontario.save();
        }// end of if cycle

        return volontario;
    }// end of method

    private List<Funzione> recuperaFunzioni(List<WrapFunzione> listaWrapFunzioniAll, VolontarioAmb volontarioOld) {
        List<Funzione> funzioniNew = new ArrayList<>();
        List<MiliteFunzioneAmb> listaIncroci;
        long keyMiliteID = volontarioOld.getId();
        long keyFunzioneOld;
        Funzione funzNew;

        listaIncroci = MiliteFunzioneAmb.getListByMilite(keyMiliteID, MANAGER);
        if (listaIncroci != null && listaIncroci.size() > 0) {
            for (MiliteFunzioneAmb incrocio : listaIncroci) {
                keyFunzioneOld = incrocio.getFunzione_id();
                funzNew = recuperaFunzioneNew(listaWrapFunzioniAll, keyFunzioneOld);
                funzioniNew.add(funzNew);
            }// end of for cycle
        }// end of if cycle

        return funzioniNew;
    }// end of method

    private Funzione recuperaFunzioneNew(List<WrapFunzione> listaWrapFunzioniAll, long keyFunzioneOld) {
        Funzione funzioneNew = null;
        FunzioneAmb funzioneOld;

        funzioneOld = FunzioneAmb.find(keyFunzioneOld, MANAGER);
        if (funzioneOld != null) {
            for (WrapFunzione wrap : listaWrapFunzioniAll) {
                if (wrap.getFunzioneOld().equals(funzioneOld)) {
                    funzioneNew = wrap.getFunzioneNew();
                    break;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return funzioneNew;
    }// end of method

    /**
     * @todo Non riesco in nessun modo a leggere la tavola UtenteRuoloAmb, perché manca/ci sono 2 primary key
     */
    private boolean recuperaAdmin(List<UtenteAmb> listaUtenti, VolontarioAmb volontarioOld) {
        boolean admin = false;
        UtenteAmb utente = UtenteAmb.getEntityByVolontario(listaUtenti, volontarioOld);

        if (utente != null) {

            EntityManager manager = getManager();
            LazyEntityContainer<UtenteRuoloAmb> container = new LazyEntityContainer(manager, UtenteRuoloAmb.class, 1000, null, true, true, true);
            container.addContainerProperty("ruolo_id", Long.class, 0L, true, true);
            container.addContainerProperty("utente_id", Long.class, 0L, true, true);

            // iterazione
            Collection itemIds = container.getItemIds();
            UtenteRuoloAmb ura;
            for (Object id : itemIds) {
                ura = container.getEntity(id);
                int a = 87;
            }

            // filtro
            Container.Filter filter = new Compare.Equal("ruolo_id", 4);
            container.addContainerFilter(filter);
            itemIds = container.getItemIds();
            int size = container.size();
            for (Object id : itemIds) {
                ura = container.getEntity(id);
                int a = 87;
            }

            manager.close();


//            admin = UtenteRuoloAmb.isAdmin(utente, MANAGER);
        }// end of if cycle

        //-@todo Metto pertanto una patch
        //-crf
        if (volontarioOld.getCroce().getSigla().equals("CRF")) {
            if (volontarioOld.getCognome().equals("Biazzi")) {
                admin = true;
            }// end of if cycle
            if (volontarioOld.getCognome().equals("Bove")) {
                admin = true;
            }// end of if cycle
            if (volontarioOld.getCognome().equals("Nefori")) {
                admin = true;
            }// end of if cycle
            if (volontarioOld.getCognome().equals("Aniello")) {
                admin = true;
            }// end of if cycle
        }// end of if cycle

        //-@todo Metto pertanto una patch
        //-crf
        if (volontarioOld.getCroce().getSigla().equals("CRPT")) {
            if (volontarioOld.getCognome().equals("Michelini") && volontarioOld.getNome().equals("Mauro")) {
                admin = true;
            }// end of if cycle
            if (volontarioOld.getCognome().equals("Giulivi")) {
                admin = true;
            }// end of if cycle
            if (volontarioOld.getCognome().equals("Federico")) {
                admin = true;
            }// end of if cycle
        }// end of if cycle

        //-@todo Metto pertanto una patch
        //-pap
        if (volontarioOld.getCroce().getSigla().equals("PAP")) {
            if (volontarioOld.getCognome().equals("Piana")) {
                admin = true;
            }// end of if cycle
            if (volontarioOld.getCognome().equals("Rampa")) {
                admin = true;
            }// end of if cycle
            if (volontarioOld.getCognome().equals("I.P. Manova")) {
                admin = true;
            }// end of if cycle
            if (volontarioOld.getCognome().equals("Caselli")) {
                admin = true;
            }// end of if cycle
            if (volontarioOld.getCognome().equals("Fusini")) {
                admin = true;
            }// end of if cycle
        }// end of if cycle

        return admin;
    }// end of method

    private String recuperaPassword(List<UtenteAmb> listaUtenti, VolontarioAmb volontarioOld) {
        String password = "";
        UtenteAmb utente = UtenteAmb.getEntityByVolontario(listaUtenti, volontarioOld);

        if (utente != null) {
            password = utente.getPass();
        }// end of if cycle

        return password;
    }// end of method

    private List<Funzione> getSoloFunzioniNew(List<WrapFunzione> listaWrapFunzioni) {
        List<Funzione> listaFunzioniNew = new ArrayList<>();

        if (listaWrapFunzioni != null && listaWrapFunzioni.size() > 0) {
            for (WrapFunzione wrap : listaWrapFunzioni) {
                listaFunzioniNew.add(wrap.getFunzioneNew());
            }// end of for cycle
        }// end of if cycle

        return listaFunzioniNew;
    }// end of method

    private List<Turno> importaTurni(CroceAmb companyOld, WamCompany companyNew, List<WrapServizio> listaWrapServizi, List<WrapVolontario> listaWrapVolontari) {
        List<TurnoAmb> listaTurniOld = TurnoAmb.findAll(companyOld, MANAGER);
        List<Turno> listaTurniNew = new ArrayList<>();
        Turno turnoNew;

        if (listaTurniOld != null && listaTurniOld.size() > 0) {
            for (TurnoAmb turnoOld : listaTurniOld) {
                turnoNew = creaSingoloTurno(companyNew, turnoOld, listaWrapServizi, listaWrapVolontari);
                listaTurniNew.add(turnoNew);
            }// end of for cycle
        }// end of if cycle

        return listaTurniNew;
    }// end of method

    private Turno creaSingoloTurno(WamCompany companyNew, TurnoAmb turnoOld, List<WrapServizio> listaWrapServizi, List<WrapVolontario> listaWrapVolontari) {
        Turno turnoNew = null;
        Servizio servizio = recuperaServizio(turnoOld, listaWrapServizi);
        Date inizio = turnoOld.getInizio();
        Date fine = turnoOld.getFine();
        List<Iscrizione> iscrizioni = null;
        String titoloExtra = turnoOld.getTitolo_extra();
        String localitaExtra = turnoOld.getLocalità_extra();
        String note = turnoOld.getNote();

//        turnoNew = Turno.crea(companyNew, servizio, inizio, fine, MANAGER);
//        turnoNew.setIscrizioni(recuperaIscrizioni(turnoOld, turnoNew, listaWrapVolontari));
//        turnoNew.setTitoloExtra(titoloExtra);
//        turnoNew.setLocalitaExtra(localitaExtra);
//        turnoNew.setNote(note);
//        turnoNew.save(MANAGER);

        return turnoNew;
    }// end of method

    private Servizio recuperaServizio(TurnoAmb turnoOld, List<WrapServizio> listaWrapServizi) {
        Servizio servizioNew = null;
        ServizioAmb servizioOld = null;

        if (turnoOld != null) {
            servizioOld = turnoOld.getTipo_turno();
            servizioNew = recuperaServizioNew(listaWrapServizi, servizioOld);
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

    private List<Iscrizione> recuperaIscrizioni(TurnoAmb turnoOld, Turno turnoNew, List<WrapVolontario> listaWrapVolontari) {
        List<Iscrizione> iscrizioni = new ArrayList<>();
        Iscrizione iscrizione;
        VolontarioAmb volontarioOld;
        Volontario volontarioNew;
        ServizioFunzione serFunz = null;

        iscrizione = recuperaIscrizione1(turnoOld, turnoNew, listaWrapVolontari);
        if (iscrizione != null) {
            iscrizioni.add(iscrizione);
        }// end of if cycle

        volontarioOld = turnoOld.getMilite_funzione1();
//        if (volontarioOld != null) {
//            isc = new Iscrizione();
//            isc.setTurno(turnoNew);
//            volontarioNew = null;
//            isc.setVolontario(volontarioNew);
//            isc.setServizioFunzione(serFunz);
//        }// end of if cycle


        return iscrizioni;
    }// end of method


    private Iscrizione recuperaIscrizione1(TurnoAmb turnoOld, Turno turnoNew, List<WrapVolontario> listaWrapVolontari) {
        Iscrizione iscrizione = null;
        VolontarioAmb volontarioOld;
        Volontario volontarioNew;
        ServizioFunzione serFunz = null;

        volontarioOld = turnoOld.getMilite_funzione1();
        if (volontarioOld != null) {
            volontarioNew = recuperaVolontarioNew(listaWrapVolontari, volontarioOld);
            iscrizione = new Iscrizione();
            iscrizione.setTurno(turnoNew);
            iscrizione.setVolontario(volontarioNew);
            iscrizione.setServizioFunzione(serFunz);
            iscrizione.save(MANAGER);
        }// end of if cycle


        return iscrizione;
    }// end of method

    private Volontario recuperaVolontarioNew(List<WrapVolontario> listaWrapVolontari, VolontarioAmb volontarioOld) {
        Volontario volontarioNew = null;

        if (volontarioNew != null) {
            for (WrapVolontario wrap : listaWrapVolontari) {
                if (wrap.getVolontarioOld().equals(volontarioOld)) {
                    volontarioNew = wrap.getVolontarioNew();
                    break;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return volontarioNew;
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
