package it.algos.wam.bootstrap;

import it.algos.wam.WAMApp;
import it.algos.wam.entity.company.Company;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.milite.Milite;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.lib.LibTime;

/**
 * Created by gac on 25 feb 2016.
 * Classe statica
 */
public abstract class BootService {

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

        if (company != null) {
            Funzione.crea(company, "aut", "Autista", 1, "Autista patentato 118");
            Funzione.crea(company, "soc", "Soccorritore", 2, "Soccorritore 118");
            Funzione.crea(company, "bar", "Barelliere", 3, "Soccorritore in prova");
        }// end of if cycle
    }// end of static method


    /**
     * Creazione iniziale di alcuni servizi per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static void creaServiziDemo() {
        Company company = Company.findByCode(WAMApp.DEMO_COMPANY_CODE);
        Servizio servizio;

        if (company != null) {
            servizio = Servizio.crea(company, "msa-mat");
            servizio.setDescrizione("Automedica mattino");
            servizio.setDurata(6);
            servizio.setOraInizio(8);
            servizio.setOraFine(14);
            servizio.setVisibile(true);
            servizio.setOrario(true);
            servizio.setMultiplo(false);
            servizio.setFunzioniObbligatorie(2);
            servizio.save();
            servizio = Servizio.crea(company, "msa-pom");
            servizio.setDescrizione("Automedica pomeriggio");
            servizio.setDurata(6);
            servizio.setOraInizio(14);
            servizio.setOraFine(20);
            servizio.setVisibile(true);
            servizio.setOrario(true);
            servizio.setMultiplo(false);
            servizio.setFunzioniObbligatorie(2);
            servizio.save();
            servizio = Servizio.crea(company, "msa-not");
            servizio.setDescrizione("Automedica notte");
            servizio.setDurata(12);
            servizio.setOraInizio(20);
            servizio.setOraFine(8);
            servizio.setVisibile(true);
            servizio.setOrario(true);
            servizio.setMultiplo(false);
            servizio.setFunzioniObbligatorie(2);
            servizio.save();
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
            Milite.crea(company, "Renzo", "Cerrato", LibDate.creaData(14, 7, 1995), "340-564738", false, false);
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

        if (company != null) {
            Funzione.crea(company, "aut", "Autista", 1, "Autista emergenze");
            Funzione.crea(company, "aut2", "Autista", 2, "Autista dimissioni");
            Funzione.crea(company, "sec", "Secondo", 3, "Soccorritore");
            Funzione.crea(company, "ter", "Aiuto", 4, "Barelliere in prova");
        }// end of if cycle
    }// end of static method


    /**
     * Creazione iniziale di alcuni servizi per la croce test
     * Li crea SOLO se non esistono già
     */
    private static void creaServiziTest() {
        Company company = Company.findByCode(WAMApp.TEST_COMPANY_CODE);

        if (company != null) {
        }// end of if cycle
    }// end of static method

}// end of abstract static class
