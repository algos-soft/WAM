package it.algos.wam.bootstrap;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.colorpicker.Color;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.lib.LibDate;

import java.util.ArrayList;
import java.util.Date;

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
        creaCompany(company);
    }// end of static method

    /**
     * Creazione iniziale di una croce test
     * Visibile solo a noi (developer)
     * Serve come prova per visualizzare solo i Militi, le Funzioni ed i Turni di una demo rispetto all'altra
     * <p>
     * La crea SOLO se non esiste già
     */
    public static void creaCompanyTest() {
        WamCompany company = creaCroceTest();
        creaCompany(company);
    }// end of static method

    /**
     * Creazione iniziale di una croce
     * Visibile solo a noi (developer)
     * Serve come prova per visualizzare solo i Militi, le Funzioni ed i Turni di una demo rispetto all'altra
     * <p>
     * La crea SOLO se non esiste già
     */
    private static void creaCompany(WamCompany company) {
        ArrayList<Funzione> listaFunzioni;
        ArrayList<Volontario> listaVolontari;
        ArrayList<Servizio> listaServizi;
        ArrayList<Turno> listaTurni;

        listaFunzioni = creaFunzioni(company);
        listaVolontari = creaVolontari(company, listaFunzioni);
        listaServizi = creaServizi(company, listaFunzioni);
        listaTurni = creaTurniVuoti(company, listaServizi);
        riempieTurni(company, listaVolontari, listaServizi, listaTurni);
    }// end of static method

    /**
     * Creazione iniziale dei dati generali per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static WamCompany creaCroceDemo() {
        WamCompany company = WamCompany.findByCode(WAMApp.DEMO_COMPANY_CODE);

        if (company == null) {
            company = new WamCompany(WAMApp.DEMO_COMPANY_CODE, "Demo");
            company.setAddress1("Via Turati, 12");
            company.setAddress1("20199 Garbagnate Milanese");
            company.setContact("Mario Bianchi");
            company.setEmail("info@crocedemo.it");
            company.setVaiSubitoTabellone(true);
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
            company = new WamCompany();
            company.setCompanyCode(WAMApp.TEST_COMPANY_CODE);
            company.setName("Test");
            company.setAddress1("Via Roma, 17");
            company.setAddress1("20020 Gossolengo");
            company.setContact("Francesca Raggi");
            company.setEmail("info@crocetest.it");
            company.save();
        }// end of if cycle
        return company;
    }// end of method


    /**
     * Creazione iniziale di alcune funzioni per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce selezionata
     * @return lista delle funzioni create
     */
    private static ArrayList<Funzione> creaFunzioni(WamCompany company) {
        ArrayList<Funzione> listaFunz = new ArrayList<>();
        int k = 0;

        if (company != null) {
            addFunz(listaFunz, company, ++k, "aut", "Autista118", "Autista patentato 118", FontAwesome.AMBULANCE);
            addFunz(listaFunz, company, ++k, "aut2", "Autista", "Autista", FontAwesome.WHEELCHAIR);
            addFunz(listaFunz, company, ++k, "soc", "Soccorritore", "Soccorritore 118", FontAwesome.HEART);
            addFunz(listaFunz, company, ++k, "sec", "Secondo", "Soccorritore in prova", FontAwesome.STETHOSCOPE);
            addFunz(listaFunz, company, ++k, "ter", "Terzo", "Autista patentato 118", FontAwesome.USER);
            addFunz(listaFunz, company, ++k, "bar", "Barelliere", "Barelliere", FontAwesome.USER_MD);
        }// end of if cycle

        return listaFunz;
    }// end of static method

    /**
     * Creazione iniziale di alcuni servizi per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce selezionata
     */
    private static ArrayList<Servizio> creaServizi(WamCompany company, ArrayList<Funzione> listaFunz) {
        ArrayList<Servizio> listaServ = new ArrayList<>();
        int k = 0;
        int azzurro = new Color(146, 189, 255).getRGB();
        int verdino = new Color(146, 255, 189).getRGB();
        int rosa = new Color(255, 146, 211).getRGB();

        if (company != null) {
            addServ(listaServ, company, ++k, "med-mat", "Automedica mattino", 8, 12, true, true, false, 3, azzurro, listaFunz.get(0), listaFunz.get(2), listaFunz.get(5));
            addServ(listaServ, company, ++k, "med-pom", "Automedica pomeriggio", 12, 18, true, true, false, 3, azzurro, listaFunz.get(0), listaFunz.get(2), listaFunz.get(5));
            addServ(listaServ, company, ++k, "med-sera", "Automedica sera", 18, 22, true, true, false, 2, azzurro, listaFunz.get(0), listaFunz.get(2), listaFunz.get(5));
            addServ(listaServ, company, ++k, "amb-mat", "Ambulanza mattino", 8, 12, true, true, false, 3, verdino, listaFunz.get(1), listaFunz.get(3));
            addServ(listaServ, company, ++k, "amb-pom", "Ambulanza pomeriggio", 12, 20, true, true, false, 3, verdino, listaFunz.get(1), listaFunz.get(3));
            addServ(listaServ, company, ++k, "amb-notte", "Ambulanza notte", 20, 8, true, true, false, 2, verdino, listaFunz.get(1), listaFunz.get(3));
            addServ(listaServ, company, ++k, "dim", "Dimissioni ordinarie", 0, 0, true, false, false, 2, rosa, listaFunz.get(1), listaFunz.get(4));
            addServ(listaServ, company, ++k, "ext", "Extra", 0, 0, true, false, true, 2, rosa, listaFunz.get(1), listaFunz.get(4));
            addServ(listaServ, company, ++k, "avis", "Avis", 0, 0, true, false, true, 1, rosa, listaFunz.get(1));
        }// end of if cycle

        return listaServ;
    }// end of static method


    /**
     * Creazione iniziale di alcuni volontari per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company   croce selezionata
     * @param listaFunz ioni della company
     */
    private static ArrayList<Volontario> creaVolontari(WamCompany company, ArrayList<Funzione> listaFunz) {
        ArrayList<Volontario> listaVolontari = new ArrayList<Volontario>();

        if (company != null) {
            listaVolontari.add(Volontario.crea(company, "Mario", "Brambilla"));
            listaVolontari.add(Volontario.crea(company, "Giovanna", "Durante", listaFunz));
            listaVolontari.add(Volontario.crea(company, "Diego", "Bertini", listaFunz.get(3)));
            listaVolontari.add(Volontario.crea(company, "Roberto", "Marchetti", listaFunz.get(2), listaFunz.get(3)));
            listaVolontari.add(Volontario.crea(company, "Edoardo", "Politi"));
            listaVolontari.add(Volontario.crea(company, "Sabina", "Roncelli"));
        }// end of if cycle

        return listaVolontari;
    }// end of static method

    /**
     * Creazione iniziale di alcune funzioni per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param listaFunzioni create
     * @param company       croce selezionata
     * @param ordine        di presentazione nelle liste
     * @param sigla         sigla di riferimento interna (obbligatoria)
     * @param descrizione   per il tabellone (obbligatoria)
     * @param note          di spiegazione (facoltative)
     */
    private static void addFunz(ArrayList<Funzione> listaFunzioni, WamCompany company, int ordine, String sigla, String descrizione, String note, FontAwesome glyph) {
        Funzione funzione;

        if (listaFunzioni != null && company != null) {
            funzione = Funzione.crea(company, sigla, descrizione, ordine, note, glyph);
            listaFunzioni.add(funzione);
        }// end of if cycle

    }// end of static method

    /**
     * Creazione iniziale di alcuni servizi per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param listaServizi creati
     * @param company      selezionata
     * @param ordine       di presentazione nel tabellone
     * @param sigla        sigla di riferimento interna (obbligatoria)
     * @param descrizione  per il tabellone (obbligatoria)
     * @param oraInizio    del servizio (facoltativo)
     * @param oraFine      del servizio (facoltativo)
     * @param visibile     nel tabellone
     * @param orario       servizio ad orario prefissato e fisso ogni giorno
     * @param multiplo     servizio suscettibile di essere effettuato diverse volte nella giornata
     * @param persone      minime indispensabile allo svolgimento del servizio
     */
    private static void addServ(ArrayList<Servizio> listaServizi, WamCompany company, int ordine, String sigla, String descrizione, int oraInizio, int oraFine, boolean visibile, boolean orario, boolean multiplo, int persone, int colore, Funzione... funzioni) {
        Servizio servizio;

        if (listaServizi != null && company != null) {
            servizio = Servizio.crea(company, ordine, sigla, descrizione, oraInizio, oraFine, visibile, colore, funzioni);
            listaServizi.add(servizio);
        }// end of if cycle

    }// end of static method

    /**
     * Creazione iniziale di alcuni turni vuoti per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce selezionata
     */
    private static ArrayList<Turno> creaTurniVuoti(WamCompany company, ArrayList<Servizio> listaServizi) {
        ArrayList<Turno> listaTurni = new ArrayList<Turno>();
        Date oggi = LibDate.today();

        for (int k = 0; k < 30; k++) {
            for (Servizio serv : listaServizi) {
                listaTurni.add(Turno.crea(company, serv, LibDate.add(oggi, k)));
            }// end of for cycle
        }// end of for cycle

        return listaTurni;
    }// end of static method

    /**
     * Riempimento iniziale di alcuni turni vuoti per la croce selezionata
     *
     * @param company croce selezionata
     */
    private static void riempieTurni(WamCompany company, ArrayList<Volontario> listaVolontari, ArrayList<Servizio> listaServizi, ArrayList<Turno> listaTurni) {
        Date oggi = LibDate.today();
        Servizio servizio;
        Iscrizione isc;
        Turno turno;
        ArrayList<Iscrizione> iscrizioni;

//        if (listaServizi != null && listaServizi.size() > 1) {
//            servizioMattina = listaServizi.get(1);
//        }// end of if cycle

//        for (int k = 0; k < 30; k++) {
//            Turno.crea(company, serv, LibDate.add(oggi, k));
//        }// end of for cycle

        for (int k = 0; k < 30; k++) {
            for (Servizio serv : listaServizi) {
                iscrizioni = new ArrayList<Iscrizione>();
                turno = Turno.find(serv, LibDate.add(oggi, k));
                isc = new Iscrizione(turno, listaVolontari.get(3), null);
                iscrizioni.add(isc);
                if (turno != null) {
                    turno.setIscrizioni(iscrizioni);
                    turno.save();
                }// end of if cycle
            }// end of for cycle
        }// end of for cycle

    }// end of static method


}// end of abstract static class
