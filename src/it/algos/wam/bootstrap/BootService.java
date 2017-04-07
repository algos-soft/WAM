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
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibDate;

import javax.persistence.EntityManager;
import java.util.ArrayList;
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
        creaFunzioniDipendenti(company, manager);
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
     * Creazione iniziale delle preferenze per una company
     * Spazzola la Enumeration CompanyPrefs e crea tutte le preferenze previste, col valore di default
     * Regolate sulla company passata come parametro.
     * Se la preferenza esiste, la ricrea (non dovrebbero esserci, visto che si crea una nuova company)
     * Inserimento del valore di default della preferenza (sovrascrivibile successivamente)
     */
    private static void creaPreferenze(WamCompany company) {
        for (CompanyPrefs pref : CompanyPrefs.values()) {
            pref.put(company);
        }// end of for cycle

        //--modifica la mail (vuota) per costruirla col nome della company
        CompanyPrefs.senderAddress.put(company, company.getCompanyCode() + "@algos.it");
    }// end of static method

    /**
     * Creazione iniziale dei dati generali per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static WamCompany creaCroceDemo() {
        WamCompany company = WamCompany.findByCode(WAMApp.DEMO_COMPANY_CODE);

        if (company != null) {
            company.delete();
        }// fine del blocco if

        company = new WamCompany(WAMApp.DEMO_COMPANY_CODE, "Company dimostrativa", "info@crocedemo.it");
        company.setAddress1("Via Turati, 12");
        company.setAddress1("20199 Garbagnate Milanese");
        company.setContact("Mario Bianchi");
        company.save();

        //--flag vari
        CompanyPrefs.usaGestioneCertificati.put(company, true);
        CompanyPrefs.usaStatisticheSuddivise.put(company, true);

        return company;
    }// end of static method

    /**
     * Creazione iniziale dei dati generali per la croce test
     * Li crea SOLO se non esistono già
     */
    private static WamCompany creaCroceTest() {
        WamCompany company = WamCompany.findByCode(WAMApp.TEST_COMPANY_CODE);

        if (company != null) {
            company.delete();
        }// fine del blocco if

        company = new WamCompany(WAMApp.TEST_COMPANY_CODE, "Company di test", "info@crocetest.it");
        company.setAddress1("Piazza Napoli, 51");
        company.setAddress1("20100 Milano");
        company.setContact("Giovanni Rossi");
        company.save();

        //--flag vari
        CompanyPrefs.usaGestioneCertificati.put(company, true);
        CompanyPrefs.usaStatisticheSuddivise.put(company, true);

        return company;
    }// end of static method

    /**
     * Creazione iniziale di alcune funzioni standard per la croce selezionata
     * Le crea SOLO se non esistono già
     *
     * @param c croce di appartenenza
     * @param m the EntityManager to use
     * @return lista delle funzioni create
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Funzione> creaFunzioni(WamCompany c, EntityManager m) {
        ArrayList<Funzione> l = new ArrayList<>();

        if (c == null) {
            return null;
        }// end of if cycle

        switch (c.getCompanyCode()) {
            case WAMApp.DEMO_COMPANY_CODE:
                l.add(funz(c, m, "autmsa", "Aut-msa", "Autista automedica abilitato 118", FontAwesome.AMBULANCE));
                l.add(funz(c, m, "1msa", "1° Soc", "Primo soccorritore automedica", FontAwesome.HEART));
                l.add(funz(c, m, "2msa", "2° Soc", "Secondo soccorritore automedica", FontAwesome.STETHOSCOPE));

                l.add(funz(c, m, "autamb", "Aut-amb", "Autista ambulanza abilitato 118", FontAwesome.AMBULANCE));
                l.add(funz(c, m, "1amb", "1° Soc", "Primo soccorritore ambulanza", FontAwesome.HEART));
                l.add(funz(c, m, "2amb", "2° Soc", "Secondo soccorritore ambulanza", FontAwesome.STETHOSCOPE));

                l.add(funz(c, m, "autord", "Aut-ord", "Autista ordinario", FontAwesome.AMBULANCE));
                l.add(funz(c, m, "dae", "DAE", "Soccorritore abilitato DAE", FontAwesome.HEART));
                l.add(funz(c, m, "bar", "Bar", "Barelliere", FontAwesome.STETHOSCOPE));
                l.add(funz(c, m, "bar2", "Bar-aff", "Barelliere in affiancamento", FontAwesome.USER));

                l.add(funz(c, m, "avis", "Avis", "Operatore trasporto AVIS", FontAwesome.MEDKIT));
                l.add(funz(c, m, "cen", "Cen", "Centralinista", FontAwesome.PHONE));
                break;
            case WAMApp.TEST_COMPANY_CODE:
                l.add(funz(c, m, "autmsa", "Aut-msa", "Autista abilitato 118", FontAwesome.AMBULANCE));
                l.add(funz(c, m, "dae", "DAE", "Soccorritore abilitato DAE", FontAwesome.HEART));
                l.add(funz(c, m, "bar", "Bar", "Barelliere", FontAwesome.STETHOSCOPE));
                l.add(funz(c, m, "bar2", "Bar-aff", "Barelliere in affiancamento", FontAwesome.USER));

                l.add(funz(c, m, "autord", "Aut-ord", "Autista ordinario", FontAwesome.AMBULANCE));
                l.add(funz(c, m, "1msa", "1° Soc", "Soccorritore abilitato", FontAwesome.HEART));
                l.add(funz(c, m, "bar", "Bar", "Barelliere", FontAwesome.STETHOSCOPE));

                l.add(funz(c, m, "avis", "Avis", "Operatore trasporto AVIS", FontAwesome.MEDKIT));
                l.add(funz(c, m, "cen", "Cen", "Centralinista", FontAwesome.PHONE));
                break;
            default: // caso non definito
                l.add(funz(c, m, "autmsa", "Aut-msa", "Autista 118", FontAwesome.AMBULANCE));
                l.add(funz(c, m, "dae", "DAE", "Soccorritore abilitato DAE", FontAwesome.HEART));
                l.add(funz(c, m, "2msa", "2° Soc", "Secondo soccorritore 118", FontAwesome.STETHOSCOPE));

                l.add(funz(c, m, "autamb", "Aut-amb", "Autista ambulanza", FontAwesome.AMBULANCE));
                l.add(funz(c, m, "1amb", "1° Soc", "Primo soccorritore", FontAwesome.HEART));
                l.add(funz(c, m, "2amb", "2° Soc", "Secondo soccorritore", FontAwesome.STETHOSCOPE));
                l.add(funz(c, m, "bar", "Bar", "Barelliere", FontAwesome.USER));

                l.add(funz(c, m, "avis", "Avis", "Operatore trasporto AVIS", FontAwesome.MEDKIT));
                l.add(funz(c, m, "cen", "Cen", "Centralinista", FontAwesome.PHONE));
                break;
        } // fine del blocco switch

        return l;
    }// end of static method

    private static Funzione funz(
            WamCompany company,
            EntityManager manager,
            String code,
            String sigla,
            String descrizione,
            FontAwesome glyph) {

        return Funzione.crea(company, code, sigla, descrizione, glyph, manager);
    }// end of static method


    private static void creaFunzioniDipendenti(WamCompany company, EntityManager manager) {
        String companyCode = company.getCompanyCode();

        switch (companyCode) {
            case WAMApp.DEMO_COMPANY_CODE:
                creaFunzioniDipendenti(company, manager, "autmsa", "autamb", "autord");
                creaFunzioniDipendenti(company, manager, "autamb", "autord");
                creaFunzioniDipendenti(company, manager, "1msa", "2msa", "1amb", "2amb", "bar", "bar2");
                creaFunzioniDipendenti(company, manager, "1amb", "2amb", "bar", "bar2");
                creaFunzioniDipendenti(company, manager, "dae", "bar", "bar2");
                creaFunzioniDipendenti(company, manager, "bar", "bar2");
                break;
            case WAMApp.TEST_COMPANY_CODE:
                break;
            default: // caso non definito
                creaFunzioniDipendenti(company, manager, "autmsa", "autamb");
                creaFunzioniDipendenti(company, manager, "dae", "2msa", "1amb", "2amb", "bar");
                creaFunzioniDipendenti(company, manager, "2msa", "2amb", "bar");
                creaFunzioniDipendenti(company, manager, "1amb", "2amb", "bar");
                creaFunzioniDipendenti(company, manager, "2amb", "bar");
                break;
        } // fine del blocco switch

    }// end of static method

    private static void creaFunzioniDipendenti(WamCompany company, EntityManager manager, String codeBase, String... codeDipendenti) {
        ArrayList<Funzione> funzioniDipendenti = new ArrayList<>();
        Funzione funzMadre;
        Funzione funzDip;

        funzMadre = Funzione.getEntityByCompanyAndCode(company, codeBase, manager);
        for (String code : codeDipendenti) {
            funzDip = Funzione.getEntityByCompanyAndCode(company, code, manager);
            funzioniDipendenti.add(funzDip);
        }// end of for cycle
        funzMadre.setFunzioniDipendenti(funzioniDipendenti);
        funzMadre.save(manager);

    }// end of static method

    /**
     * Creazione iniziale di alcuni servizi per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param c    croce di appartenenza
     * @param m    the EntityManager to use
     * @param funz lista delle funzioni di questa croce
     * @return lista dei servizi creati
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Servizio> creaServizi(WamCompany c, EntityManager m, ArrayList<Funzione> funz) {
        ArrayList<Servizio> l = new ArrayList<>();
        int azzurro = Color.BLUE.hashCode();
        int verdino = Color.CYAN.hashCode();
        int rosa = Color.MAGENTA.hashCode();
        int giallo = Color.YELLOW.hashCode();

        if (c == null) {
            return null;
        }// end of if cycle

        switch (c.getCompanyCode()) {
            case WAMApp.DEMO_COMPANY_CODE:
                l.add(servo(c, m, funz, "med-mat", "Automedica mattino", 8, 13, azzurro, 2, 0, 1, 2, 8));
                l.add(servo(c, m, funz, "med-pom", "Automedica pomeriggio", 13, 18, azzurro, 2, 0, 1, 2, 8));
                l.add(servo(c, m, funz, "med-sera", "Automedica sera", 18, 22, azzurro, 2, 0, 1, 2));

                l.add(servo(c, m, funz, "amb-mat", "Ambulanza mattino", 8, 14, verdino, 2, 3, 4, 5, 8));
                l.add(servo(c, m, funz, "amb-pom", "Ambulanza pomeriggio", 14, 20, verdino, 2, 3, 4, 5, 8));
                l.add(servo(c, m, funz, "amb-notte", "Ambulanza notte", 20, 8, verdino, 2, 3, 4, 5));

                l.add(servnot(c, m, funz, "dim", "Dimissioni ordinarie", rosa, 2, 6, 7, 8, 9));
                l.add(servno(c, m, funz, "ext", "Extra", rosa, 2, 6, 7, 8, 9));
                l.add(servno(c, m, funz, "avis", "Avis", rosa, 1, 10, 9));

                l.add(servo(c, m, funz, "cent-mat", "Centralino mattino", 8, 13, giallo, 0, 11));
                l.add(servnt(c, m, funz, "cent-pom", "Centralino pomeriggio", 13, 18, giallo, 0, 11));
                break;
            case WAMApp.TEST_COMPANY_CODE:
                l.add(servo(c, m, funz, "amb-mat", "Ambulanza mattino", 8, 14, verdino, 2, 0, 1, 2, 3));
                l.add(servo(c, m, funz, "amb-pom", "Ambulanza pomeriggio", 14, 20, verdino, 2, 0, 1, 2, 3));
                l.add(servo(c, m, funz, "amb-notte", "Ambulanza notte", 20, 8, verdino, 2, 0, 1, 2, 3));

                l.add(servno(c, m, funz, "ext", "Extra", rosa, 2, 4, 5, 6));
                l.add(servno(c, m, funz, "avis", "Avis", rosa, 1, 7, 3));

                l.add(servo(c, m, funz, "cent-mat", "Centralino mattino", 8, 13, giallo, 0, 8));
                break;
            default: // caso non definito
                l.add(servo(c, m, funz, "med-mat", "Automedica mattino", 8, 13, azzurro, 2, 0, 1, 2, 3));
                l.add(servo(c, m, funz, "med-pom", "Automedica pomeriggio", 13, 18, azzurro, 2, 0, 1, 2, 3));
                l.add(servo(c, m, funz, "med-sera", "Automedica sera", 18, 22, azzurro, 2, 0, 1));

                l.add(servo(c, m, funz, "amb-mat", "Ambulanza mattino", 8, 14, verdino, 2, 4, 5, 6, 7));
                l.add(servo(c, m, funz, "amb-pom", "Ambulanza pomeriggio", 14, 20, verdino, 2, 4, 5, 6, 7));
                l.add(servo(c, m, funz, "amb-notte", "Ambulanza notte", 20, 8, verdino, 2, 4, 5));

                l.add(servno(c, m, funz, "dim", "Dimissioni ordinarie", rosa, 2, 4, 5, 6));
                l.add(servnot(c, m, funz, "ext", "Extra", rosa, 2, 4, 5, 6));
                l.add(servno(c, m, funz, "avis", "Avis", rosa, 1, 8, 7));
                break;
        } // fine del blocco switch

        return l;
    }// end of static method

    /**
     * Servizio con orario prefissato
     */
    private static Servizio servo(
            WamCompany company,
            EntityManager manager,
            ArrayList<Funzione> funzioni,
            String sigla,
            String descrizione,
            int oraInizio,
            int oraFine,
            int colore,
            int obbligatori,
            int... numFunz) {
        return serv(company, manager, funzioni, sigla, true, descrizione, true, oraInizio, oraFine, colore, obbligatori, numFunz);
    }// end of static method

    /**
     * Servizio senza orario
     */
    private static Servizio servno(
            WamCompany company,
            EntityManager manager,
            ArrayList<Funzione> funzioni,
            String sigla,
            String descrizione,
            int colore,
            int obbligatori,
            int... numFunz) {
        return serv(company, manager, funzioni, sigla, true, descrizione, false, 0, 0, colore, obbligatori, numFunz);
    }// end of static method

    /**
     * Servizio non visibile nel tabellone
     */
    private static Servizio servnt(
            WamCompany company,
            EntityManager manager,
            ArrayList<Funzione> funzioni,
            String sigla,
            String descrizione,
            int oraInizio,
            int oraFine,
            int colore,
            int obbligatori,
            int... numFunz) {
        return serv(company, manager, funzioni, sigla, false, descrizione, true, oraInizio, oraFine, colore, obbligatori, numFunz);
    }// end of static method

    /**
     * Servizio senza orario
     * Servizio non visibile nel tabellone
     */
    private static Servizio servnot(
            WamCompany company,
            EntityManager manager,
            ArrayList<Funzione> funzioni,
            String sigla,
            String descrizione,
            int colore,
            int obbligatori,
            int... numFunz) {
        return serv(company, manager, funzioni, sigla, false, descrizione, false, 0, 0, colore, obbligatori, numFunz);
    }// end of static method

    /**
     * Servizio base
     */
    private static Servizio serv(
            WamCompany company,
            EntityManager manager,
            ArrayList<Funzione> allFunzioniCompany,
            String sigla,
            boolean visibile,
            String descrizione,
            boolean orario,
            int oraInizio,
            int oraFine,
            int colore,
            int obbligatori,
            int... numFunz) {

        int k = 0;
        Funzione funz;
        List<ServizioFunzione> listaServizioFunzioni = new ArrayList<>();

        for (int num : numFunz) {
            k++;
            funz = allFunzioniCompany.get(num);
            if (k <= obbligatori) {
                listaServizioFunzioni.add(new ServizioFunzione(company, null, funz, true, k));
            } else {
                listaServizioFunzioni.add(new ServizioFunzione(company, null, funz, false, k));
            }// end of if/else cycle
        }// end of for cycle

        return Servizio.crea(company, sigla, visibile, descrizione, colore, orario, oraInizio, oraFine, manager, listaServizioFunzioni);
    }// end of static method


    /**
     * Creazione iniziale di alcuni volontari per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param c    croce di appartenenza
     * @param m    the EntityManager to use
     * @param funz lista delle funzioni di questa croce
     * @return lista dei volontari creati
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Volontario> creaVolontari(WamCompany c, EntityManager m, ArrayList<Funzione> funz) {
        ArrayList<Volontario> l = new ArrayList<>();
        Date d1 = new Date(2016, 4, 21);
        Date d2 = new Date(2017, 2, 13);
        Date d3 = new Date(2016, 11, 4);
        Date d4 = new Date(2017, 7, 15);
        Date d5 = new Date(2018, 4, 7);
        Date d6 = new Date(2018, 8, 30);

        if (c == null) {
            return null;
        }// end of if cycle

        switch (c.getCompanyCode()) {
            case WAMApp.DEMO_COMPANY_CODE:
                l.add(volub(c, m, funz, "Stefano", "Brambilla", "337 2453817", "stefano.brambilla@wamdemo.it", "bra", d2, null, null, 0, 3));
                l.add(volu(c, m, funz, "Giovanna", "Durante", "338 7394672", "giovanna.durante@wamdemo.it", "dur", 1, 2, 4, 5, 6, 7));
                l.add(volu(c, m, funz, "Roberto", "Marchetti", "345 673351", "roberto.marchetti@wamdemo.it", "mar", 7, 8));
                l.add(vola(c, m, funz, "Edoardo", "Politi", "327 4233681", "edoardo.politi@wamdemo.it", "pol", 6));
                l.add(volub(c, m, funz, "Arturo", "Casaroli", "327 9931245", "arturo.casaroli@wamdemo.it", "cas", d3, d4, d1, 0, 1, 2, 3, 4, 5, 6));
                l.add(volu(c, m, funz, "Flavia", "Robusti", "328 131476", "flavia.robusti@wamdemo.it", "rob", 2, 5));
                l.add(volub(c, m, funz, "Marco", "Terzani", "339 397235", "marco.terzani@wamdemo.it", "ter", d1, d4, d5, 1, 2, 4, 5, 6));
                l.add(volu(c, m, funz, "Volontario", "Volontario", "", "", "volontario"));
                l.add(vola(c, m, funz, "Admin", "Admin", "", "", "admin"));

//                lista.add(Arrays.asList("Mario", "Abbati", "mario.abbati@wamdemo.it", "abb", false, funz.get(2)));
//                lista.add(Arrays.asList("Diego", "Bertini", "diego.bertini@wamdemo.it", "ber", true, funz.get(3)));
//                lista.add(Arrays.asList("Mirella", "Pace", "mirella.pace@wamdemo.it", "pac", false, funz.get(4)));
//                lista.add(Arrays.asList("Sabina", "Roncelli", "sabina.roncelli@wamdemo.it", "ron", false, funz.get(6)));
//                lista.add(Arrays.asList("Antonio", "Zambetti", "antonio.zambetti@wamdemo.it", "zam", false, funz.get(8)));
//                lista.add(Arrays.asList("Aldo", "Terzino", "aldo.terzino@wamdemo.it", "ter", false, funz.get(8), funz.get(9)));
//                lista.add(Arrays.asList("Alice", "Mantovani", "alice.mantovani@wamdemo.it", "man", false, funz.get(8), funz.get(9)));
                break;
            case WAMApp.TEST_COMPANY_CODE:
                l.add(volu(c, m, funz, "Angela", "Ferrara", "337 2453817", "angela.ferrara@wamdemo.it", "fer", 1));
                l.add(volu(c, m, funz, "Marco", "Castelli", "377 334292", "marco.castelli@wamdemo.it", "cas", 1));
                l.add(volu(c, m, funz, "Roberto", "Bianchi", "", "roberto.bianchi@wamdemo.it", "bia", 1));

//                lista.add(Arrays.asList("Roberto", "Robusti", "roberto.robusti@wamdemo.it", "rob", false, funz.get(4)));
//                lista.add(Arrays.asList("Flavia", "Bianchi", "flavia.bianchi@wamdemo.it", "bia", false, funz.get(6)));
//                lista.add(Arrays.asList("Giovanna", "Esposito", "giovanna.esposito@wamdemo.it", "esp", false, funz.get(8)));
//                lista.add(Arrays.asList("Arturo", "Della Monica", "arturo.dellamonica@wamdemo.it", "del", false, funz.get(8), funz.get(9)));
//                lista.add(Arrays.asList("Stefano", "Bramieri", "stefano.bramieri@wamdemo.it", "bra", false, funz.get(8), funz.get(9)));
//                lista.add(Arrays.asList("Lucio", "Bertuzzi", "lucio.bertuzzi@wamdemo.it", "ber", false, funz.get(2)));
//                lista.add(Arrays.asList("Renato", "Sortino", "renato.sortino@wamdemo.it", "sor", true, funz.get(3)));
//                lista.add(Arrays.asList("Maria", "Tomba", "maria.tomba@wamdemo.it", "tom", false, funz.get(4)));
//                lista.add(Arrays.asList("Teresa", "Torriglia", "teresa.torriglia@wamdemo.it", "tor", false, funz.get(6)));
//                lista.add(Arrays.asList("Carlo", "Malaguti", "carlo.malaguti@wamdemo.it", "mal", false, funz.get(8)));
//                lista.add(Arrays.asList("Luigi", "Savarese", "luigi.savarese@wamdemo.it", "sav", false, funz.get(8), funz.get(9)));
//                lista.add(Arrays.asList("Tomaso", "Zanichetti", "tomaso.zanichetti@wamdemo.it", "zan", false, funz.get(8), funz.get(9)));
                break;
            default: // caso non definito
                l.add(vola(c, m, funz, "Arturo", "Marchini", "338 185592", "arturo.marchini@wamdemo.it", "mar", 1, 2, 4, 5, 6, 7));
                l.add(volub(c, m, funz, "Luisa", "Poltronieri", "345 9931245", "luisa.poltronieri@wamdemo.it", "pol", d1, null, null, 0, 3));
                l.add(volu(c, m, funz, "Giovanni", "Speranza", "339 7394672", "giovanni.speranza@wamdemo.it", "spe", 1, 2, 4, 5, 6, 7));
                l.add(volu(c, m, funz, "Tomaso", "Savarese", "328 875124", "tomaso.savarese@wamdemo.it", "sav", 7, 8));
                l.add(volu(c, m, funz, "Teresa", "Barbieri", "377 5145764", "terese.barbieri@wamdemo.it", "bar", 6));
                l.add(volub(c, m, funz, "Stefano", "Della Monaca", "331 897432", "stefano.dellamonica@wamdemo.it", "del", d2, d3, d4, 0, 1, 2, 3, 4, 5, 6));
                l.add(volu(c, m, funz, "Lucio", "Bertuzzi", "337 131476", "lucio.bertuzzi@wamdemo.it", "ber", 2, 5));
                l.add(volu(c, m, funz, "Marta", "Lorenzini", "345 673351", "marta.lorenzini@wamdemo.it", "lor", 1, 2, 4, 5, 6));
                break;
        } // fine del blocco switch

        return l;
    }// end of static method

    /**
     * Volontario utente
     */
    private static Volontario volu(
            WamCompany company,
            EntityManager manager,
            ArrayList<Funzione> allFunzioniCompany,
            String nome,
            String cognome,
            String cellulare,
            String email,
            String password,
            int... numFunz) {

        return vol(company, manager, allFunzioniCompany, nome, cognome, cellulare, email, password, false, null, null, null, numFunz);
    }// end of static method

    /**
     * Volontario utente con brevetti
     */
    private static Volontario volub(
            WamCompany company,
            EntityManager manager,
            ArrayList<Funzione> allFunzioniCompany,
            String nome,
            String cognome,
            String cellulare,
            String email,
            String password,
            Date bBLSD,
            Date bPNT,
            Date bBPHTP,
            int... numFunz) {

        return vol(company, manager, allFunzioniCompany, nome, cognome, cellulare, email, password, false, bBLSD, bPNT, bBPHTP, numFunz);
    }// end of static method

    /**
     * Volontario admin
     */
    private static Volontario vola(
            WamCompany company,
            EntityManager manager,
            ArrayList<Funzione> allFunzioniCompany,
            String nome,
            String cognome,
            String cellulare,
            String email,
            String password,
            int... numFunz) {
        return vol(company, manager, allFunzioniCompany, nome, cognome, cellulare, email, password, true, null, null, null, numFunz);
    }// end of static method

    /**
     * Volontario base
     */
    private static Volontario vol(
            WamCompany company,
            EntityManager manager,
            ArrayList<Funzione> allFunzioniCompany,
            String nome,
            String cognome,
            String cellulare,
            String email,
            String password,
            boolean admin,
            Date sBLSD,
            Date sPNT,
            Date sBPHTP,
            int... numFunz) {

        Funzione funz;
        List<Funzione> listaFunzioni = new ArrayList<>();

        for (int num : numFunz) {
            funz = allFunzioniCompany.get(num);
            listaFunzioni.add(funz);
        }// end of for cycle

        return Volontario.crea(company, manager, nome, cognome, cellulare, email, password, admin, sBLSD, sPNT, sBPHTP, listaFunzioni);
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
        Date inizio = LibDate.add(oggi, -10);
        Turno turno;

        for (int k = 0; k < 24; k++) {
            for (Servizio servizio : servizi) {
                turno = Turno.crea(company, servizio, LibDate.add(inizio, k), null, manager);
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
            serviziFunzione = serv.getServizioFunzioniOrdine();
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
