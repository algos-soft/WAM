package it.algos.wam.bootstrap;

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

    private static ArrayList<Funzione> FUNZ_DEMO;
    private static ArrayList<Funzione> FUNZ_TEST;
    private static ArrayList<Servizio> SER_DEMO;

    /**
     * Creazione iniziale di una croce demo
     * Visibile a tutti
     * <p>
     * La crea SOLO se non esiste già
     */
    public static void creaCompanyDemo() {
        WamCompany company;
        ArrayList<Funzione> listaFunzioni = new ArrayList<>();

        company = creaCroceDemo();
        listaFunzioni = creaFunzioni(company);
        creaServizi(company);
//        creaVolontari(company, listaFunzioni);
//        creaTurniDemo();
    }// end of static method

    /**
     * Creazione iniziale di una croce test
     * Visibile solo a noi (developer)
     * Serve come prova per visualizzare solo i Militi, le Funzioni ed i Turni di una demo rispetto all'altra
     * <p>
     * La crea SOLO se non esiste già
     */
    public static void creaCompanyTest() {
        WamCompany company;
        ArrayList<Funzione> listaFunzioni = new ArrayList<>();

        company = creaCroceTest();
        listaFunzioni = creaFunzioni(company);
        creaServizi(company);
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

//    /**
//     * Creazione iniziale di alcune funzioni per la croce demo
//     * Li crea SOLO se non esistono già
//     */
//    private static void creaFunzioniDemo() {
//        WamCompany company = WamCompany.findByCode(WamCompany.DEMO_COMPANY_CODE);
//        Funzione funzione;
//        int k = 0;
//
//        if (company != null) {
//            FUNZ_DEMO = new ArrayList<>();
//            funzione = Funzione.crea(company, "aut", "Autista", ++k, "Autista patentato 118");
//            FUNZ_DEMO.add(funzione);
//            funzione = Funzione.crea(company, "soc", "Soccorritore", ++k, "Soccorritore 118");
//            FUNZ_DEMO.add(funzione);
//            funzione = Funzione.crea(company, "bar", "Barelliere", ++k, "Barelliere");
//            FUNZ_DEMO.add(funzione);
//            funzione = Funzione.crea(company, "pro", "Bar prova", ++k, "Soccorritore in prova");
//            FUNZ_DEMO.add(funzione);
//        }// end of if cycle
//    }// end of static method

    /**
     * Creazione iniziale di alcuni militi per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static void creaMilitiDemo2() {
        WamCompany company = WamCompany.findByCode(WamCompany.DEMO_COMPANY_CODE);
        Funzione funz1 = FUNZ_DEMO.get(0);
        Funzione funz2 = FUNZ_DEMO.get(1);
        Funzione funz3 = FUNZ_DEMO.get(2);
        Volontario vol;

//        if (company != null) {
//            vol = Volontario.crea(company, "Piero", "Bernocchi", null, "335-471824");
//            vol.add(funz1);
//            vol.save();
//            vol = Volontario.crea(company, "Maria", "Cavazzini");
//            vol.add(funz1);
//            vol.save();
//            vol = Volontario.crea(company, "Francesco", "Mantovani", LibTime.adesso(), "338-679115");
//            vol.add(funz1);
//            vol.add(funz2);
//            vol.save();
//            vol = Volontario.crea(company, "Giulia", "Politi");
//            vol = Volontario.crea(company, "Maria", "Rovescala", LibDate.getPrimoGennaio(1987), "340-453728", true, true);
//            vol.add(funz1);
//            vol.add(funz2);
//            vol.add(funz3);
//            vol.save();
//            vol = Volontario.crea(company, "Aldo", "Vaccari");
//        }// end of if cycle
    }// end of static method

    /**
     * Creazione iniziale di alcuni servizi per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static void creaServiziDemo2() {
        WamCompany company = WamCompany.findByCode(WamCompany.DEMO_COMPANY_CODE);
        Servizio servizio;
        Funzione funz1 = FUNZ_DEMO.get(0);
        Funzione funz2 = FUNZ_DEMO.get(1);
        Funzione funz3 = FUNZ_DEMO.get(2);
        Funzione funz4 = FUNZ_DEMO.get(3);
        SER_DEMO = new ArrayList<>();

        int k = 0;
        servizio = Servizio.crea(company, ++k, "msa-mat", "Automedica mattino", 8, 14, true, true, false, 2);
        SER_DEMO.add(servizio);
        servizio.add(funz1, true);
        servizio.add(funz2);
        servizio.add(funz2);
        servizio.add(funz3);
        servizio.save();
        servizio = Servizio.crea(company, ++k, "msa-pom", "Automedica pomeriggio", 14, 20, true, true, false, 2);
        SER_DEMO.add(servizio);
        servizio.add(funz1);
        servizio.add(funz2);
        servizio.add(funz2);
        servizio.save();
        servizio = Servizio.crea(company, ++k, "msa-not", "Automedica notte", 20, 8, true, true, false, 2);
        SER_DEMO.add(servizio);
        servizio.add(funz1);
        servizio.add(funz2);
        servizio.add(funz2);
        servizio.save();
        servizio = Servizio.crea(company, ++k, "dia", "Dialisi mattino", 7, 13, true, true, false, 2);
        SER_DEMO.add(servizio);
        servizio.add(funz1);
        servizio.add(funz2);
        servizio.add(funz3);
        servizio.save();
        servizio = Servizio.crea(company, ++k, "ord-mat", "Ordinario mattino", 7, 12, true, true, false, 3);
        SER_DEMO.add(servizio);
        servizio.add(funz3);
        servizio.save();
        servizio = Servizio.crea(company, ++k, "ord-pom", "Ordinario pomeriggio", 12, 18, true, true, false, 3);
        SER_DEMO.add(servizio);
        SER_DEMO.add(servizio);
        servizio.add(funz1);
        servizio.add(funz2);
        servizio.add(funz3);
        servizio.save();
        servizio = Servizio.crea(company, ++k, "ord-ser", "Ordinario sera", 18, 24, true, true, false, 2);
        SER_DEMO.add(servizio);
        servizio.add(funz1, true);
        servizio.add(funz2, true);
        servizio.add(funz3);
        servizio.save();
        servizio = Servizio.crea(company, ++k, "ext", "Extra", 0, 0, true, false, true, 2);
        SER_DEMO.add(servizio);
        servizio.add(funz1);
        servizio.add(funz2);
        servizio.add(funz3);
        servizio.save();
        servizio = Servizio.crea(company, ++k, "avis", "Servizio AVIS", 0, 0, true, false, false, 1);
        servizio.add(funz1);
        servizio.add(funz2);
        servizio.add(funz3);
        servizio.save();
    }// end of static method

    /**
     * Creazione iniziale di alcuni turni per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static void creaTurniDemo() {
        WamCompany company = WamCompany.findByCode(WamCompany.DEMO_COMPANY_CODE);

        Servizio servizio = null;
        Turno turno;

        servizio = Servizio.find(company, "msa-pom");
        turno = Turno.crea(company, servizio, LibDate.creaData(14, 8, 2016));

        servizio = Servizio.find(company, "ord-mat");
        turno = Turno.crea(company, servizio, LibDate.creaData(22, 11, 2015));

        servizio = Servizio.find(company, "avis");
        turno = Turno.crea(company, servizio, LibDate.creaData(5, 2, 2016));

        servizio = Servizio.find(company, "ext");
        turno = Turno.crea(company, servizio, LibDate.creaData(17, 4, 2016));
        turno.setTitoloExtra("Trasferimento");
        turno.setLocalitàExtra("Padova");
        turno.setNote("Sedia-Basta un milite");
        turno.save();

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
     * Creazione iniziale di alcuni militi per la croce test
     * Li crea SOLO se non esistono già
     */
    private static void creaMilitiTest() {
        WamCompany company = WamCompany.findByCode(WAMApp.TEST_COMPANY_CODE);
//        Volontario.crea(company, "Carlo", "Bagno");
//        Volontario.crea(company, "Renzo", "Cerrato", LibDate.creaData(14, 7, 1995), "340-564738", false);
//        Volontario.crea(company, "Lucia", "Donadoni", LibDate.creaData(11, 3, 1999), "335-5124396", true, false);
//        Volontario.crea(company, "Ambra", "Angeletti");
//        Volontario.crea(company, "Flacio", "Brambilla", LibDate.creaData(27, 10, 1991), "340-6786432");
//        Volontario.crea(company, "Ruggero", "Testa");
    }// end of static method

    /**
     * Creazione iniziale di alcune funzioni per la croce test
     * Li crea SOLO se non esistono già
     */
    private static void creaFunzioniTest() {
        WamCompany company = WamCompany.findByCode(WAMApp.TEST_COMPANY_CODE);
        Funzione funzione;
        int k = 0;
        FUNZ_TEST = new ArrayList<>();
        funzione = Funzione.crea(company, "aut", "Autista", ++k, "Autista emergenze");
        FUNZ_TEST.add(funzione);
        funzione = Funzione.crea(company, "aut2", "Autista", ++k, "Autista dimissioni");
        FUNZ_TEST.add(funzione);
        funzione = Funzione.crea(company, "sec", "Secondo", ++k, "Soccorritore");
        FUNZ_TEST.add(funzione);
        funzione = Funzione.crea(company, "ter", "Aiuto", ++k, "Barelliere in prova");
        FUNZ_TEST.add(funzione);
    }// end of static method

    /**
     * Creazione iniziale di alcuni servizi per la croce test
     * Li crea SOLO se non esistono già
     */
    private static void creaServiziTest() {
        WamCompany company = WamCompany.findByCode(WAMApp.TEST_COMPANY_CODE);

        int k = 0;
        Servizio.crea(company, ++k, "amb-mat", "Ambulanza mattino", 8, 12, true, true, false, 3);
        Servizio.crea(company, ++k, "amb-pom", "Ambulanza pomeriggio", 12, 18, true, true, false, 3);
        Servizio.crea(company, ++k, "amb-ser", "Ambulanza sera", 18, 24, true, true, false, 2);
        Servizio.crea(company, ++k, "dim", "Dimissioni", 0, 0, true, false, false, 2);
        Servizio.crea(company, ++k, "ext", "Extra", 0, 0, true, false, true, 2);
    }// end of static method

    /**
     * Creazione iniziale di alcuni turni per la croce test
     * Li crea SOLO se non esistono già
     */
    private static void creaTurniTest() {
        WamCompany company = WamCompany.findByCode(WAMApp.TEST_COMPANY_CODE);
        Servizio servizio = null;
        Date data = null;
        ArrayList<Iscrizione> lista;
        Iscrizione iscrizione;
        Iscrizione iscrizione2;
        Iscrizione iscrizione3;
        Funzione funzione = Funzione.find(company, "aut");
        Volontario milite = Volontario.find(company, "Ruggero", "Testa");
        Volontario milite2 = Volontario.find(company, "Lucia", "Donadoni");
        Volontario milite3 = Volontario.find(company, "Renzo", "Cerrato");

        if (company != null) {
            servizio = Servizio.find(company, "amb-mat");
            data = LibDate.creaData(3, 3, 2016);
            lista = new ArrayList<>();
            Turno.crea(company, servizio, data, data, true);

            servizio = Servizio.find(company, "dim");
            data = LibDate.creaData(4, 3, 2016);
//            iscrizione = new Iscrizione(funzione, milite);
//            lista = new ArrayList<>();
//            lista.add(iscrizione);
            Turno.crea(company, servizio, data, data, true);

            servizio = Servizio.find(company, "amb-pom");
            data = LibDate.creaData(8, 3, 2016);
//            iscrizione2 = new Iscrizione(funzione, milite2);
//            iscrizione3 = new Iscrizione(funzione, milite3);
//            lista = new ArrayList<>();
//            lista.add(iscrizione2);
//            lista.add(iscrizione3);
            Turno.crea(company, servizio, data, data, true);
        }// end of if cycle
    }// end of static method

    /**
     * Creazione iniziale di alcune funzioni per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce selezionata
     *
     * @return lista delle funzioni create
     */
    private static ArrayList<Funzione> creaFunzioni(WamCompany company) {
        ArrayList<Funzione> listaFunz = new ArrayList<>();
        int k = 0;

        if (company != null) {
            addFunz(listaFunz, company, "aut", "Autista118", ++k, "Autista patentato 118");
            addFunz(listaFunz, company, "aut2", "Autista", ++k, "Autista");
            addFunz(listaFunz, company, "soc", "Soccorritore", ++k, "Soccorritore 118");
            addFunz(listaFunz, company, "sec", "Secondo", ++k, "Soccorritore in prova");
            addFunz(listaFunz, company, "ter", "Terzo", ++k, "Autista patentato 118");
            addFunz(listaFunz, company, "bar", "Barelliere", ++k, "Barelliere");
        }// end of if cycle

        return listaFunz;
    }// end of static method

    /**
     * Creazione iniziale di alcuni servizi per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce selezionata
     *
     * @return lista dei servizi creati
     */
    private static void creaServizi(WamCompany company) {
        int k = 0;

        if (company != null) {
            Servizio.crea(company, ++k, "med-mat", "Automedica mattino", 8, 12, true, true, false, 3);
            Servizio.crea(company, ++k, "med-pom", "Automedica pomeriggio", 12, 18, true, true, false, 3);
            Servizio.crea(company, ++k, "med-sera", "Automedica sera", 18, 22, true, true, false, 2);
            Servizio.crea(company, ++k, "amb-mat", "Ambulanza mattino", 8, 12, true, true, false, 3);
            Servizio.crea(company, ++k, "amb-pom", "Ambulanza pomeriggio", 12, 20, true, true, false, 3);
            Servizio.crea(company, ++k, "amb-notte", "Ambulanza notte", 20, 8, true, true, false, 2);
            Servizio.crea(company, ++k, "dim", "Dimissioni", 0, 0, true, false, false, 2);
            Servizio.crea(company, ++k, "ext", "Extra", 0, 0, true, false, true, 2);
        }// end of if cycle

    }// end of static method

    /**
     * Creazione iniziale di alcuni volontari per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce selezionata
     */

    private static void creaVolontari(WamCompany company) {
        creaVolontari(company, null);
    }// end of static method

    /**
     * Creazione iniziale di alcuni volontari per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company       croce selezionata
     * @param listaFunzioni della company
     */
    private static void creaVolontari(WamCompany company, ArrayList<Funzione> listaFunzioni) {

        if (company != null) {
            Volontario.crea(company, "Mario", "Brambilla");
            Volontario.crea(company, "Giovanna", "Durante", listaFunzioni);

//            vol = Volontario.crea(company, "Piero", "Bernocchi",listaFunzioni);
//            vol.add(funz1);
//            vol.save();
//            vol = Volontario.crea(company, "Maria", "Cavazzini");
//            vol.add(funz1);
//            vol.save();
//            vol = Volontario.crea(company, "Francesco", "Mantovani", LibTime.adesso(), "338-679115");
//            vol.add(funz1);
//            vol.add(funz2);
//            vol.save();
//            vol = Volontario.crea(company, "Giulia", "Politi");
//            vol = Volontario.crea(company, "Maria", "Rovescala", LibDate.getPrimoGennaio(1987), "340-453728", true, true);
//            vol.add(funz1);
//            vol.add(funz2);
//            vol.add(funz3);
//            vol.save();
//            vol = Volontario.crea(company, "Aldo", "Vaccari");
        }// end of if cycle
    }// end of static method

    /**
     * Creazione iniziale di alcune funzioni per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param listaFunzioni create
     * @param company       croce di appartenenza
     * @param sigla         sigla di riferimento interna (obbligatoria)
     * @param descrizione   per il tabellone (obbligatoria)
     * @param ordine        di presentazione nelle liste
     * @param note          di spiegazione (facoltative)
     */
    private static void addFunz(ArrayList<Funzione> listaFunzioni, WamCompany company, String sigla, String descrizione, int ordine, String note) {
        Funzione funzione;

        if (listaFunzioni != null && company != null) {
            funzione = Funzione.crea(company, sigla, descrizione, ordine, note);
            listaFunzioni.add(funzione);
        }// end of if cycle

    }// end of static method

}// end of abstract static class
