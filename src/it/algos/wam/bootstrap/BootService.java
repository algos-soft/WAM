package it.algos.wam.bootstrap;

import it.algos.wam.WAMApp;
import it.algos.wam.entity.company.Company;

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
    }// end of static method

}// end of abstract static class
