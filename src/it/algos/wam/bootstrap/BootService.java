package it.algos.wam.bootstrap;

import it.algos.wam.WAMApp;
import it.algos.wam.entity.company.Company;
import it.algos.wam.entity.milite.Milite;
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
        Company company = Company.findByCode(WAMApp.DEMO_COMPANY_CODE);

        if (company != null) {
            Milite.crea(company, "Piero", "Bernocchi",null,"335-471824");
            Milite.crea(company, "Maria", "Cavazzini");
            Milite.crea(company, "Francesco", "Mantovani", LibTime.adesso(),"338-679115");
            Milite.crea(company, "Giulia", "Politi");
            Milite.crea(company, "Maria", "Rovescala", LibDate.getPrimoGennaio(1987),"340-453728");
            Milite.crea(company, "Aldo", "Vaccari");
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
            Milite.crea(company, "Renzo", "Cerrato");
            Milite.crea(company, "Lucia", "Donadoni");
            Milite.crea(company, "Ambra", "Angeletti");
            Milite.crea(company, "Flacio", "Brambilla");
            Milite.crea(company, "Ruggero", "Testa");
        }// end of if cycle

    }// end of static method


//    /**
//     * Creazione iniziale di un milite
//     * Lo crea SOLO se non esiste già
//     */
//    private static Milite creaMilite(Company company, String nome, String cognome) {
//        Milite milite = Milite.findByNome(nome); //@todo errato
//
//        if (milite != null) {
//            milite = new Milite(company, nome, cognome);
//            milite.save();
//        }// end of if cycle
//
//        return milite;
//    }// end of static method

}// end of abstract static class
