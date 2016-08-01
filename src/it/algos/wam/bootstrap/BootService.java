package it.algos.wam.bootstrap;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.colorpicker.Color;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.settings.CompanyPrefs;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.domain.pref.PrefType;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibDate;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by gac on 25 feb 2016.
 * Classe statica astratta.
 */
public abstract class BootService {

    /**
     * Creazione iniziale di una croce demo
     * Visibile a tutti
     * <p>
     * La crea SOLO se non esiste già
     */
    public static void creaCompanyDemo() {
        WamCompany company = creaCroceDemo();
        initCompany(company, true, true);
    }// end of static method

    /**
     * Creazione iniziale di una croce test
     * Visibile a tutti
     * <p>
     * La crea SOLO se non esiste già
     */
    public static void creaCompanyTest() {

        WamCompany company = creaCroceTest();
        initCompany(company, true, true);
    }// end of static method

    /**
     * Inizializza una croce appena creata, con alcuni dati di esempio
     * Visibile solo a noi (developer)
     * Crea alcune funzioni standard
     * Crea una lista di volontari di esempio
     * Crea alcuni servizi di esempio
     *
     * @param company croce di appartenenza
     */
    public static void initCompany(WamCompany company) {
        initCompany(company, true, false);
    }// end of static method

    /**
     * Inizializza una croce appena creata, con alcuni dati di esempio
     * Visibile solo a noi (developer)
     * Crea alcune funzioni standard
     * Crea una lista di volontari di esempio
     * Crea alcuni servizi di esempio
     * Crea alcuni turni vuoti (opzionale)
     * Crea le iscrizioni per i turni creati (opzionale)
     *
     * @param company   croce di appartenenza
     * @param creaTurni flag per la creazione di turni vuoti
     * @param company   flag per la creazione delle iscrizioni per i turni
     */
    public static void initCompany(WamCompany company, boolean creaTurni, boolean creaIscrizioni) {
        ArrayList<Funzione> listaFunzioni;
        ArrayList<Servizio> listaServizi;
        ArrayList<Volontario> listaVolontari;
        ArrayList<Turno> listaTurni = null;
        EntityManager manager = EM.createEntityManager();
        manager.getTransaction().begin();

        creaPreferenze(company);
        listaFunzioni = creaFunzioni(company, manager);
        listaServizi = creaServizi(company, manager, listaFunzioni);
        listaVolontari = creaVolontari(company, manager, listaFunzioni);

        if (creaTurni) {
            listaTurni = creaTurniVuoti(company, manager, listaServizi);
        }// end of if cycle

        if (creaIscrizioni) {
            riempieTurni(company, manager, listaTurni, listaVolontari);
        }// end of if cycle

        manager.getTransaction().commit();
        manager.close();
    }// end of static method


    /**
     * Creazione iniziale delle preferenze per ogni croce
     * Le crea SOLO se non esistono già
     */
    private static void creaPreferenze(WamCompany company) {
        Pref.crea(CompanyPrefs.tabellonePubblico.toString(), PrefType.bool, company, "tabellone è liberamente accessibile in visione senza login", true);
        Pref.crea(CompanyPrefs.senderAddress.toString(), PrefType.string, company, "indirizzo email del mittente", company.getCompanyCode() + "@algos.it");
        Pref.crea(CompanyPrefs.sendMailToBackup.toString(), PrefType.bool, company, "invia ogni mail anche a una casella di backup", false);
        Pref.crea(CompanyPrefs.backupMailbox.toString(), PrefType.string, company, "casella di backup delle email", "");
        Pref.crea(CompanyPrefs.inviaNotificaInizioTurno.toString(), PrefType.bool, company, "invia le notifiche di inizio turno", true);
        Pref.crea(CompanyPrefs.quanteOrePrimaNotificaInizioTurno.toString(), PrefType.integer, company, "quante ore prima invia le notifiche di inizio turno", 24);
    }// end of static method


    /**
     * Creazione iniziale dei dati generali per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static WamCompany creaCroceDemo() {
        WamCompany company = WamCompany.findByCode(WAMApp.DEMO_COMPANY_CODE);

        if (company == null) {
            company = new WamCompany(WAMApp.DEMO_COMPANY_CODE, "Demo", "info@crocedemo.it");
            company.setAddress1("Via Turati, 12");
            company.setAddress1("20199 Garbagnate Milanese");
            company.setContact("Mario Bianchi");
            company.save();
        }// end of if cycle
        return company;
    }// end of static method

    /**
     * Creazione iniziale dei dati generali per la croce test
     * Li crea SOLO se non esistono già
     */
    private static WamCompany creaCroceTest() {
        WamCompany company = WamCompany.findByCode(WAMApp.TEST_COMPANY_CODE);

        if (company == null) {
            company = new WamCompany(WAMApp.TEST_COMPANY_CODE, "Test", "info@crocetest.it");
            company.setAddress1("Piazza Napoli, 51");
            company.setAddress1("20100 Milano");
            company.setContact("Giovanni Rossi");
            company.save();
        }// end of if cycle
        return company;
    }// end of static method

    /**
     * Creazione iniziale di alcune funzioni standard per la croce selezionata
     * Le crea SOLO se non esistono già
     *
     * @param company croce di appartenenza
     * @param manager the EntityManager to use
     * @return lista delle funzioni create
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Funzione> creaFunzioni(WamCompany company, EntityManager manager) {
        ArrayList<Funzione> listaFunzioni = new ArrayList<>();
        ArrayList lista = new ArrayList<>();

        if (company == null) {
            return null;
        }// end of if cycle

        lista.add(Arrays.asList("aut-msa", "Aut-msa", "Autista automedica abilitato 118", FontAwesome.AMBULANCE));
        lista.add(Arrays.asList("aut-amb", "Aut-amb", "Autista ambulanza abilitato 118", FontAwesome.AMBULANCE));
        lista.add(Arrays.asList("aut-ord", "Aut-ord", "Autista ordinario", FontAwesome.AMBULANCE));

        lista.add(Arrays.asList("soc-dae", "DAE", "Soccorritore abilitato DAE", FontAwesome.HEART));
        lista.add(Arrays.asList("soc-uno", "1° Soc", "Primo soccorritore", FontAwesome.STETHOSCOPE));
        lista.add(Arrays.asList("soc-due", "2° Soc", "Secondo soccorritore", FontAwesome.STETHOSCOPE));

        lista.add(Arrays.asList("bar", "Bar", "Barelliere", FontAwesome.USER));
        lista.add(Arrays.asList("bar-aff", "Bar-aff", "Barelliere in affiancamento", FontAwesome.USER));

        lista.add(Arrays.asList("avis", "Avis", "Operatore trasporto AVIS", FontAwesome.MEDKIT));
        lista.add(Arrays.asList("cent", "Cen", "Centralinista", FontAwesome.PHONE));

        for (int k = 0; k < lista.size(); k++) {
            listaFunzioni.add(creaFunzBase(company, manager, k + 1, (List) lista.get(k)));
        }// end of for cycle

        return listaFunzioni;
    }// end of static method

    /**
     * Creazione della singola funzione per la croce selezionata
     * La crea SOLO se non esiste già
     *
     * @param company  croce di appartenenza
     * @param manager  the EntityManager to use
     * @param ordine   di presentazione nelle liste
     * @param listaTmp di alcune property
     * @return istanza di Funzione
     */
    private static Funzione creaFunzBase(WamCompany company, EntityManager manager, int ordine, List listaTmp) {
        String sigla = (String) listaTmp.get(0);
        String descrizione = (String) listaTmp.get(1);
        String note = (String) listaTmp.get(2);
        FontAwesome glyph = (FontAwesome) listaTmp.get(3);

        return Funzione.crea(company, manager, sigla, descrizione, ordine, note, glyph);
    }// end of static method

    /**
     * Creazione iniziale di alcuni servizi per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce di appartenenza
     * @param manager the EntityManager to use
     * @param funz    lista delle funzioni di questa croce
     * @return lista dei servizi creati
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Servizio> creaServizi(WamCompany company, EntityManager manager, ArrayList<Funzione> funz) {
        ArrayList<Servizio> listaServizi = new ArrayList<>();
        ArrayList lista = new ArrayList<>();
        int azzurro = Color.BLUE.hashCode();
        int verdino = Color.CYAN.hashCode();
        int rosa = Color.MAGENTA.hashCode();
        int giallo = Color.YELLOW.hashCode();

        if (company == null) {
            return null;
        }// end of if cycle

        lista.add(Arrays.asList("med-mat", "Automedica mattino", 8, 13, true, azzurro, setMedica(funz), 2));
        lista.add(Arrays.asList("med-pom", "Automedica pomeriggio", 13, 18, true, azzurro, setMedica(funz), 2));
        lista.add(Arrays.asList("med-sera", "Automedica sera", 18, 22, true, azzurro, setMedicaNotte(funz), 2));
        lista.add(Arrays.asList("amb-mat", "Ambulanza mattino", 8, 14, true, verdino, setAmbulanza(funz), 2));
        lista.add(Arrays.asList("amb-pom", "Ambulanza pomeriggio", 14, 20, true, verdino, setAmbulanza(funz), 2));
        lista.add(Arrays.asList("amb-notte", "Ambulanza notte", 20, 8, true, verdino, setAmbulanzaNotte(funz), 2));
        lista.add(Arrays.asList("dim", "Dimissioni ordinarie", 0, 0, false, rosa, setOrdinaria(funz), 2));
        lista.add(Arrays.asList("ext", "Extra", 0, 0, false, rosa, setOrdinaria(funz), 2));
        lista.add(Arrays.asList("avis", "Avis", 0, 0, false, rosa, setAvis(funz), 1));
        lista.add(Arrays.asList("cent-mat", "Centralino mattino", 8, 13, true, giallo, setCentralino(funz), 1));
        lista.add(Arrays.asList("cent-pom", "Centralino pomeriggio", 13, 18, true, giallo, setCentralino(funz), 1));

        for (int k = 0; k < lista.size(); k++) {
            listaServizi.add(creaServBase(company, manager, k + 1, (List) lista.get(k)));
        }// end of for cycle

        return listaServizi;
    }// end of static method

    /**
     * Creazione iniziale di un servizio
     * La crea SOLO se non esiste già
     *
     * @param company  croce di appartenenza
     * @param manager  the EntityManager to use
     * @param ordine   di presentazione nelle liste
     * @param listaTmp di alcune property
     * @return istanza di Servizio
     */
    private static Servizio creaServBase(WamCompany company, EntityManager manager, int ordine, List listaTmp) {
        String sigla = "";
        String descrizione = "";
        int oraInizio = 0;
        int oraFine = 0;
        boolean orario = false;
        int colore = 0;
        Funzione[] funzioni = null;
        ArrayList lista = null;
        int obbligatori = 0;

        if (listaTmp.size() > 0 && listaTmp.get(0) instanceof String) {
            sigla = (String) listaTmp.get(0);
        }// end of if cycle

        if (listaTmp.size() > 1 && listaTmp.get(1) instanceof String) {
            descrizione = (String) listaTmp.get(1);
        }// end of if cycle

        if (listaTmp.size() > 2 && listaTmp.get(2) instanceof Integer) {
            oraInizio = (Integer) listaTmp.get(2);
        }// end of if cycle

        if (listaTmp.size() > 3 && listaTmp.get(3) instanceof Integer) {
            oraFine = (Integer) listaTmp.get(3);
        }// end of if cycle

        if (listaTmp.size() > 4 && listaTmp.get(4) instanceof Boolean) {
            orario = (Boolean) listaTmp.get(4);
        }// end of if cycle

        if (listaTmp.size() > 5 && listaTmp.get(5) instanceof Integer) {
            colore = (Integer) listaTmp.get(5);
        }// end of if cycle

        if (listaTmp.size() > 6 && listaTmp.get(6) instanceof ArrayList) {
            lista = (ArrayList) listaTmp.get(6);
            funzioni = (Funzione[]) lista.toArray(new Funzione[lista.size()]);
        }// end of if cycle

        if (listaTmp.size() > 7 && listaTmp.get(7) instanceof Integer) {
            obbligatori = (Integer) listaTmp.get(7);
        }// end of if cycle

        return Servizio.crea(company, manager, ordine, sigla, descrizione, oraInizio, oraFine, orario, colore, obbligatori, funzioni);
    }// end of static method

    /**
     * Creazione iniziale di alcuni volontari per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce di appartenenza
     * @param manager the EntityManager to use
     * @param funz    lista delle funzioni di questa croce
     * @return lista dei volontari creati
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Volontario> creaVolontari(WamCompany company, EntityManager manager, ArrayList<Funzione> funz) {
        ArrayList<Volontario> listaVolontari = new ArrayList<>();
        ArrayList lista = new ArrayList<>();

        if (company == null) {
            return null;
        }// end of if cycle

        if (company.getCompanyCode().equals(WAMApp.DEMO_COMPANY_CODE)) {
            lista.add(Arrays.asList("Stefano", "Brambilla", "stefano.brambilla@wamdemo.it", "bra", false, funz.get(1)));
            lista.add(Arrays.asList("Giovanna", "Durante", "giovanna.durante@wamdemo.it", "dur", false, funz));
            lista.add(Arrays.asList("Roberto", "Marchetti", "roberto.marchetti@wamdemo.it", "mar", false, funz.get(7), funz.get(9)));
            lista.add(Arrays.asList("Edoardo", "Politi", "edoardo.politi@wamdemo.it", "pol", false, funz.get(5)));
            lista.add(Arrays.asList("Lucia", "Casaroli", "lucia.casaroli@wamdemo.it", "cas", true, funz.get(0)));
            lista.add(Arrays.asList("Flavia", "Robusti", "flavia.robusti@wamdemo.it", "rob", false, funz.get(8), funz.get(9)));
            lista.add(Arrays.asList("Marco", "Terzani", "marco.terzani@wamdemo.it", "ter", false, funz.get(8), funz.get(9)));
        }// end of if cycle

        if (company.getCompanyCode().equals(WAMApp.TEST_COMPANY_CODE)) {
            lista.add(Arrays.asList("Mario", "Abbati", "mario.abbati@wamdemo.it", "abb", false, funz.get(2)));
            lista.add(Arrays.asList("Diego", "Bertini", "diego.bertini@wamdemo.it", "ber", true, funz.get(3)));
            lista.add(Arrays.asList("Mirella", "Pace", "mirella.pace@wamdemo.it", "pac", false, funz.get(4)));
            lista.add(Arrays.asList("Sabina", "Roncelli", "sabina.roncelli@wamdemo.it", "ron", false, funz.get(6)));
            lista.add(Arrays.asList("Antonio", "Zambetti", "antonio.zambetti@wamdemo.it", "zam", false, funz.get(8)));
            lista.add(Arrays.asList("Aldo", "Terzino", "aldo.terzino@wamdemo.it", "ter", false, funz.get(8), funz.get(9)));
            lista.add(Arrays.asList("Alice", "Mantovani", "alice.mantovani@wamdemo.it", "man", false, funz.get(8), funz.get(9)));
        }// end of if cycle

        for (int k = 0; k < lista.size(); k++) {
            listaVolontari.add(creaVolBase(company, manager, (List) lista.get(k)));
        }// end of for cycle

        return listaVolontari;
    }// end of static method

    /**
     * Creazione iniziale di un volontario
     * Lo crea SOLO se non esiste già
     *
     * @param company  croce di appartenenza
     * @param manager  the EntityManager to use
     * @param listaTmp di alcune property
     * @return istanza di Volontario
     */
    private static Volontario creaVolBase(WamCompany company, EntityManager manager, List listaTmp) {
        String nome = "";
        String cognome = "";
        String password = "";
        String email = "";
        boolean admin = false;
        Funzione[] funzioni = null;
        ArrayList lista = null;

        if (listaTmp.size() > 0 && listaTmp.get(0) instanceof String) {
            nome = (String) listaTmp.get(0);
        }// end of if cycle

        if (listaTmp.size() > 1 && listaTmp.get(1) instanceof String) {
            cognome = (String) listaTmp.get(1);
        }// end of if cycle

        if (listaTmp.size() > 2 && listaTmp.get(2) instanceof String) {
            email = (String) listaTmp.get(2);
        }// end of if cycle

        if (listaTmp.size() > 3 && listaTmp.get(3) instanceof String) {
            password = (String) listaTmp.get(3);
        }// end of if cycle

        if (listaTmp.size() > 4 && listaTmp.get(4) instanceof Boolean) {
            admin = (Boolean) listaTmp.get(4);
        }// end of if cycle

        if (listaTmp.size() > 5) {
            if (listaTmp.get(5) instanceof ArrayList) {
                lista = (ArrayList) listaTmp.get(5);
                funzioni = (Funzione[]) lista.toArray(new Funzione[lista.size()]);
            } else {
                lista = new ArrayList();
                for (int k = 5; k < listaTmp.size(); k++) {
                    if (listaTmp.get(k) instanceof Funzione) {
                        lista.add(listaTmp.get(k));
                    }// end of if cycle
                }// end of for cycle
                funzioni = (Funzione[]) lista.toArray(new Funzione[lista.size()]);
            }// end of if/else cycle
        }// end of if cycle

        return Volontario.crea(company, manager, nome, cognome, email, password, admin, funzioni);
    }// end of static method

    /**
     * Creazione iniziale di alcuni turni vuoti per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce di appartenenza
     * @param manager the EntityManager to use
     * @param servizi lista dei servizi di questa croce
     * @return lista dei turni creati
     */
    private static ArrayList<Turno> creaTurniVuoti(WamCompany company, EntityManager manager, ArrayList<Servizio> servizi) {
        ArrayList<Turno> listaTurni = new ArrayList<>();
        Date oggi = LibDate.today();
        Turno turno;

        for (int k = 0; k < 30; k++) {
            for (Servizio servizio : servizi) {
                turno = Turno.crea(company, manager, servizio, LibDate.add(oggi, k));
                listaTurni.add(turno);
            }// end of for cycle
        }// end of for cycle

        return listaTurni;
    }// end of static method

    /**
     * Riempimento iniziale di alcuni turni vuoti per la croce selezionata
     *
     * @param company   croce di appartenenza
     * @param manager   the EntityManager to use
     * @param turni     lista dei turni di questa croce
     * @param volontari lista dei volontari di questa croce
     */
    private static void riempieTurni(
            WamCompany company,
            EntityManager manager,
            ArrayList<Turno> turni,
            ArrayList<Volontario> volontari) {
        Servizio serv;
        Iscrizione isc;
        Turno turno;
        ArrayList<Iscrizione> iscrizioni = new ArrayList<>();
        ArrayList<Volontario> volGiorno = new ArrayList<>();
        ArrayList<ServizioFunzione> serviziFunzione;
        long chiave = 0;

        for (int k = 0; k < turni.size(); k = k + 2) {
            turno = turni.get(k);
            if (chiave != turno.getChiave()) {
                volGiorno = new ArrayList<>();
                chiave = turno.getChiave();
            }// end of if cycle
            serv = turno.getServizio();
            serviziFunzione = serv.getServizioFunzioni();
            for (ServizioFunzione servFunz : serviziFunzione) {
                for (Volontario vol : volontari) {
                    if (vol.haFunzione(servFunz.getFunzione())) {
                        if (!volGiorno.contains(vol)) {
                            isc = Iscrizione.crea(company, manager, turno, vol, servFunz);
                            iscrizioni.add(isc);
                            volGiorno.add(vol);
                            break;
                        }// end of if cycle
                    }// fine del blocco if
                } // fine del ciclo for-each
            } // fine del ciclo for-each
            turno.setIscrizioni(iscrizioni);
            turno.setAssegnato(true);
            turno.save(manager);
        } // fine del ciclo for

    }// end of static method

    /**
     * Subset
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Funzione> setMedica(ArrayList<Funzione> funz) {
        ArrayList<Funzione> subset = new ArrayList();

        subset.add(funz.get(0));
        subset.add(funz.get(3));
        subset.add(funz.get(5));
        subset.add(funz.get(6));

        return subset;
    }// end of static method

    /**
     * Subset
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Funzione> setMedicaNotte(ArrayList<Funzione> funz) {
        ArrayList<Funzione> subset = new ArrayList();

        subset.add(funz.get(0));
        subset.add(funz.get(3));
        subset.add(funz.get(5));

        return subset;
    }// end of static method

    /**
     * Subset
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Funzione> setAmbulanza(ArrayList<Funzione> funz) {
        ArrayList<Funzione> subset = new ArrayList();

        subset.add(funz.get(1));
        subset.add(funz.get(3));
        subset.add(funz.get(5));
        subset.add(funz.get(6));

        return subset;
    }// end of static method

    /**
     * Subset
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Funzione> setAmbulanzaNotte(ArrayList<Funzione> funz) {
        ArrayList<Funzione> subset = new ArrayList();

        subset.add(funz.get(1));
        subset.add(funz.get(3));
        subset.add(funz.get(5));

        return subset;
    }// end of static method

    /**
     * Subset
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Funzione> setOrdinaria(ArrayList<Funzione> funz) {
        ArrayList<Funzione> subset = new ArrayList();

        subset.add(funz.get(2));
        subset.add(funz.get(4));

        return subset;
    }// end of static method

    /**
     * Subset
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Funzione> setAvis(ArrayList<Funzione> funz) {
        ArrayList<Funzione> subset = new ArrayList();

        subset.add(funz.get(8));

        return subset;
    }// end of static method

    /**
     * Subset
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Funzione> setCentralino(ArrayList<Funzione> funz) {
        ArrayList<Funzione> subset = new ArrayList();

        subset.add(funz.get(9));

        return subset;
    }// end of static method

}// end of abstract static class
