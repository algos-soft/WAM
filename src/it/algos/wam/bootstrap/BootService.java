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
        int k = 0;

        if (company != null) {
            Funzione.crea(company, "aut", "Autista", ++k, "Autista patentato 118");
            Funzione.crea(company, "soc", "Soccorritore", ++k, "Soccorritore 118");
            Funzione.crea(company, "bar", "Barelliere", ++k, "Soccorritore in prova");
        }// end of if cycle
    }// end of static method


    /**
     * Creazione iniziale di alcuni servizi per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static void creaServiziDemo() {
        Company company = Company.findByCode(WAMApp.DEMO_COMPANY_CODE);
        int k = 0;

        if (company != null) {
            Servizio.crea(company, ++k, "msa-mat", "Automedica mattino", 8, 14, true, true, false, 2);
            Servizio.crea(company, ++k, "msa-pom", "Automedica pomeriggio", 14, 20, true, true, false, 2);
            Servizio.crea(company, ++k, "msa-not", "Automedica notte", 20, 8, true, true, false, 2);
            Servizio.crea(company, ++k, "dia", "Dialisi mattino", 7, 13, true, true, false, 2);
            Servizio.crea(company, ++k, "ord-mat", "Ordinario mattino", 7, 12, true, true, false, 3);
            Servizio.crea(company, ++k, "ord-pom", "Ordinario pomeriggio", 12, 18, true, true, false, 3);
            Servizio.crea(company, ++k, "ord-ser", "Ordinario sera", 18, 24, true, true, false, 2);
            Servizio.crea(company, ++k, "ext", "Extra", 0, 0, true, false, true, 2);
            Servizio.crea(company, ++k, "avis", "Servizio AVIS", 0, 0, true, false, false, 1);
        }// end of if cycle
    }// end of static method

    /**
     * Creazione iniziale di alcuni turni per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static void creaTurniDemo() {
        Company company = Company.findByCode(WAMApp.DEMO_COMPANY_CODE);

        if (company != null) {

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
        int k = 0;

        if (company != null) {
            Funzione.crea(company, "aut", "Autista", ++k, "Autista emergenze");
            Funzione.crea(company, "aut2", "Autista", ++k, "Autista dimissioni");
            Funzione.crea(company, "sec", "Secondo", ++k, "Soccorritore");
            Funzione.crea(company, "ter", "Aiuto", ++k, "Barelliere in prova");
        }// end of if cycle
    }// end of static method


    /**
     * Creazione iniziale di alcuni servizi per la croce test
     * Li crea SOLO se non esistono già
     */
    private static void creaServiziTest() {
        Company company = Company.findByCode(WAMApp.TEST_COMPANY_CODE);
        int k = 0;

        if (company != null) {
            Servizio.crea(company, ++k, "amb-mat", "Ambulanza mattino", 8, 12, true, true, false, 3);
            Servizio.crea(company, ++k, "amb-pom", "Ambulanza pomeriggio", 12, 18, true, true, false, 3);
            Servizio.crea(company, ++k, "amb-ser", "Ambulanza sera", 18, 24, true, true, false, 2);
            Servizio.crea(company, ++k, "dim", "Dimissioni", 0, 0, true, false, false, 2);
            Servizio.crea(company, ++k, "ext", "Extra", 0, 0, true, false, true, 2);
        }// end of if cycle
    }// end of static method

}// end of abstract static class
