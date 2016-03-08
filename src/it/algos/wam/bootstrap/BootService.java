package it.algos.wam.bootstrap;

import it.algos.wam.WAMApp;
import it.algos.wam.entity.company.Company;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.milite.Milite;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.wrap.Iscrizione;
import it.algos.wam.wrap.WrapServizio;
import it.algos.wam.wrap.WrapTurno;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.lib.LibTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gac on 25 feb 2016.
 * Classe statica astratta
 */
public abstract class BootService {

    private static ArrayList<Funzione> FUNZ_DEMO;
    private static ArrayList<Funzione> FUNZ_TEST;

    /**
     * Creazione iniziale di una croce demo
     * Visibile a tutti
     * <p>
     * La crea SOLO se non esiste già
     */
    public static void creaCompanyDemo() {
        creaCroceDemo();
        creaMilitiDemo();
        creaFunzioniDemo();
        creaServiziDemo();
        creaTurniDemo();
    }// end of static method

    /**
     * Creazione iniziale di una croce test
     * Visibile solo a noi (admin)
     * Serve come prova per visualizzare solo i Militi, le Funzioni ed i Turni di una demo rispetto all'altra
     * <p>
     * La crea SOLO se non esiste già
     */
    public static void creaCompanyTest() {
        creaCroceTest();
        creaMilitiTest();
        creaFunzioniTest();
        creaServiziTest();
        creaTurniTest();
    }// end of static method


    /**
     * Creazione iniziale dei dati generali per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static void creaCroceDemo() {
        Company company = Company.findByCode(WAMApp.DEMO_COMPANY_CODE);

        if (company == null) {
            company = new Company();
            company.setCompanyCode(WAMApp.DEMO_COMPANY_CODE);
            company.setName("Demo");
            company.setAddress1("Via Turati, 12");
            company.setAddress1("20199 Garbagnate Milanese");
            company.setContact("Mario Bianchi");
            company.setEmail("info@crocedemo.it");
            company.save();
        }// end of if cycle
    }// end of static method

    /**
     * Creazione iniziale di alcuni militi per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static void creaMilitiDemo() {
        Company company = Company.findByCode(WAMApp.DEMO_COMPANY_CODE);

        if (company != null) {
            Milite.crea(company, "Piero", "Bernocchi", null, "335-471824");
            Milite.crea(company, "Maria", "Cavazzini");
            Milite.crea(company, "Francesco", "Mantovani", LibTime.adesso(), "338-679115");
            Milite.crea(company, "Giulia", "Politi");
            Milite.crea(company, "Maria", "Rovescala", LibDate.getPrimoGennaio(1987), "340-453728", true, true);
            Milite.crea(company, "Aldo", "Vaccari");
        }// end of if cycle
    }// end of static method

    /**
     * Creazione iniziale di alcune funzioni per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static void creaFunzioniDemo() {
        Company company = Company.findByCode(WAMApp.DEMO_COMPANY_CODE);
        Funzione funzione;
        int k = 0;

        if (company != null) {
            FUNZ_DEMO = new ArrayList<>();
            funzione = Funzione.crea(company, "aut", "Autista", ++k, "Autista patentato 118");
            FUNZ_DEMO.add(funzione);
            funzione = Funzione.crea(company, "soc", "Soccorritore", ++k, "Soccorritore 118");
            FUNZ_DEMO.add(funzione);
            funzione = Funzione.crea(company, "bar", "Barelliere", ++k, "Soccorritore in prova");
            FUNZ_DEMO.add(funzione);
        }// end of if cycle
    }// end of static method


    /**
     * Creazione iniziale di alcuni servizi per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static void creaServiziDemo() {
        Company company = Company.findByCode(WAMApp.DEMO_COMPANY_CODE);
        WrapServizio wrap;
        int k = 0;

        if (company != null) {
            wrap = new WrapServizio(FUNZ_DEMO.get(0), FUNZ_DEMO.get(1), FUNZ_DEMO.get(2));
            wrap.setObbligatoria1(true);
            wrap.setObbligatoria2(true);
            Servizio.crea(company, ++k, "msa-mat", "Automedica mattino", 8, 14, true, true, false, 2, wrap);
            Servizio.crea(company, ++k, "msa-pom", "Automedica pomeriggio", 14, 20, true, true, false, 2, wrap);
            Servizio.crea(company, ++k, "msa-not", "Automedica notte", 20, 8, true, true, false, 2, wrap);
            Servizio.crea(company, ++k, "dia", "Dialisi mattino", 7, 13, true, true, false, 2, wrap);
            Servizio.crea(company, ++k, "ord-mat", "Ordinario mattino", 7, 12, true, true, false, 3, wrap);
            Servizio.crea(company, ++k, "ord-pom", "Ordinario pomeriggio", 12, 18, true, true, false, 3, wrap);
            Servizio.crea(company, ++k, "ord-ser", "Ordinario sera", 18, 24, true, true, false, 2, wrap);
            Servizio.crea(company, ++k, "ext", "Extra", 0, 0, true, false, true, 2, new WrapServizio(FUNZ_DEMO.get(0), FUNZ_DEMO.get(1)));
            Servizio.crea(company, ++k, "avis", "Servizio AVIS", 0, 0, true, false, false, 1, new WrapServizio(FUNZ_DEMO.get(0)));
        }// end of if cycle
    }// end of static method

    /**
     * Creazione iniziale di alcuni turni per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static void creaTurniDemo() {
        Company company = Company.findByCode(WAMApp.DEMO_COMPANY_CODE);
        Servizio servizio = null;
        Turno turno;

        if (company != null) {
            servizio = Servizio.find(company, "msa-pom");
            Turno.crea(company, servizio, LibDate.creaData(14, 8, 2016));

            servizio = Servizio.find(company, "ord-mat");
            Turno.crea(company, servizio, LibDate.creaData(22, 11, 2015));

            servizio = Servizio.find(company, "avis");
            Turno.crea(company, servizio, LibDate.creaData(5, 2, 2016));

            servizio = Servizio.find(company, "ext");
            turno = Turno.crea(company, servizio, LibDate.creaData(17, 4, 2016));
            turno.setTitoloExtra("Trasferimento");
            turno.setLocalitàExtra("Padova");
            turno.setNote("Sedia-Basta un milite");
            turno.save();
        }// end of if cycle
    }// end of static method


    /**
     * Creazione iniziale dei dati generali per la croce test
     * Li crea SOLO se non esistono già
     */
    private static void creaCroceTest() {
        Company company = Company.findByCode(WAMApp.TEST_COMPANY_CODE);

        if (company == null) {
            company = new Company();
            company.setCompanyCode(WAMApp.TEST_COMPANY_CODE);
            company.setName("Test");
            company.setAddress1("Via Roma, 17");
            company.setAddress1("20020 Gossolengo");
            company.setContact("Francesca Raggi");
            company.setEmail("info@crocetest.it");
            company.save();
        }// end of if cycle
    }// end of method

    /**
     * Creazione iniziale di alcuni militi per la croce test
     * Li crea SOLO se non esistono già
     */
    private static void creaMilitiTest() {
        Company company = Company.findByCode(WAMApp.TEST_COMPANY_CODE);

        if (company != null) {
            Milite.crea(company, "Carlo", "Bagno");
            Milite.crea(company, "Renzo", "Cerrato", LibDate.creaData(14, 7, 1995), "340-564738", false);
            Milite.crea(company, "Lucia", "Donadoni", LibDate.creaData(11, 3, 1999), "335-5124396", true, false);
            Milite.crea(company, "Ambra", "Angeletti");
            Milite.crea(company, "Flacio", "Brambilla", LibDate.creaData(27, 10, 1991), "340-6786432");
            Milite.crea(company, "Ruggero", "Testa");
        }// end of if cycle
    }// end of static method


    /**
     * Creazione iniziale di alcune funzioni per la croce test
     * Li crea SOLO se non esistono già
     */
    private static void creaFunzioniTest() {
        Company company = Company.findByCode(WAMApp.TEST_COMPANY_CODE);
        Funzione funzione;
        int k = 0;

        if (company != null) {
            FUNZ_TEST = new ArrayList<>();
            funzione = Funzione.crea(company, "aut", "Autista", ++k, "Autista emergenze");
            FUNZ_TEST.add(funzione);
            funzione = Funzione.crea(company, "aut2", "Autista", ++k, "Autista dimissioni");
            FUNZ_TEST.add(funzione);
            funzione = Funzione.crea(company, "sec", "Secondo", ++k, "Soccorritore");
            FUNZ_TEST.add(funzione);
            funzione = Funzione.crea(company, "ter", "Aiuto", ++k, "Barelliere in prova");
            FUNZ_TEST.add(funzione);
        }// end of if cycle
    }// end of static method


    /**
     * Creazione iniziale di alcuni servizi per la croce test
     * Li crea SOLO se non esistono già
     */
    private static void creaServiziTest() {
        Company company = Company.findByCode(WAMApp.TEST_COMPANY_CODE);
        WrapServizio wrap;
        int k = 0;

        if (company != null) {
            wrap = new WrapServizio(FUNZ_TEST.get(0), FUNZ_TEST.get(1), FUNZ_TEST.get(2), FUNZ_TEST.get(3));
            wrap.setObbligatoria1(true);
            wrap.setObbligatoria2(true);
            Servizio.crea(company, ++k, "amb-mat", "Ambulanza mattino", 8, 12, true, true, false, 3, wrap);
            wrap.setObbligatoria2(false);
            Servizio.crea(company, ++k, "amb-pom", "Ambulanza pomeriggio", 12, 18, true, true, false, 3, wrap);
            Servizio.crea(company, ++k, "amb-ser", "Ambulanza sera", 18, 24, true, true, false, 2, wrap);
            wrap.setObbligatoria2(true);
            Servizio.crea(company, ++k, "dim", "Dimissioni", 0, 0, true, false, false, 2, wrap);
            Servizio.crea(company, ++k, "ext", "Extra", 0, 0, true, false, true, 2, wrap);
        }// end of if cycle
    }// end of static method

    /**
     * Creazione iniziale di alcuni turni per la croce test
     * Li crea SOLO se non esistono già
     */
    private static void creaTurniTest() {
        Company company = Company.findByCode(WAMApp.TEST_COMPANY_CODE);
        Servizio servizio = null;
        Date data = null;
        WrapTurno wrap = null;
        Iscrizione iscrizione;
        Iscrizione iscrizione2;
        Iscrizione iscrizione3;
        Funzione funzione = Funzione.find(company, "aut");
        Milite milite = Milite.find(company, "Ruggero", "Testa");
        Milite milite2 = Milite.find(company, "Lucia", "Donadoni");
        Milite milite3 = Milite.find(company, "Renzo", "Cerrato");

        if (company != null) {
            servizio = Servizio.find(company, "amb-mat");
            data = LibDate.creaData(3, 3, 2016);
            wrap = new WrapTurno(null);
            Turno.crea(company, servizio, data, data, wrap, true);

            servizio = Servizio.find(company, "dim");
            data = LibDate.creaData(4, 3, 2016);
            iscrizione = new Iscrizione(funzione, milite);
            wrap = new WrapTurno(iscrizione);
            Turno.crea(company, servizio, data, data, wrap, true);

            servizio = Servizio.find(company, "amb-pom");
            data = LibDate.creaData(8, 3, 2016);
            iscrizione2 = new Iscrizione(funzione, milite2);
            iscrizione3 = new Iscrizione(funzione, milite3);
            wrap = new WrapTurno(iscrizione2, iscrizione3);
            Turno.crea(company, servizio, data, data, wrap, true);
        }// end of if cycle
    }// end of static method

}// end of abstract static class
