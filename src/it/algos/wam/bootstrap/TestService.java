package it.algos.wam.bootstrap;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gac on 19 mag 2016.
 * <p>
 * Run test di alcune funzionalità del database
 * Soprattutto l'uso in automatico della company corrente
 * Nei test normali non ho la Sessione
 */
public class TestService {

    private WamCompany companyDemo = WamCompany.findByCode("demo");
    private WamCompany companyTest = WamCompany.findByCode("test");
    private WamCompany companyCorrente;

    /**
     * Costruttore
     */
    public TestService() {
        System.out.println("Run test: ");

        testCompany();
        testFunzioneNew();
        testFunzioneCrea();
        testVolontario();
        testServizio();
    }// end of constructor


    /**
     * Company
     */
    private void testCompany() {
        int numCompany = WamCompany.count();
        List<WamCompany> listaCompany = WamCompany.findAll();
        print("Numero di croci (count)", numCompany);
        print("Numero di croci (lista)", listaCompany.size());
        for (int k = 0; k < listaCompany.size(); k++) {
            print(k + 1 + ") croce", listaCompany.get(k).getCompanyCode());
        }// end of for cycle

        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        if (companyCorrente != null) {
            print("Company selezionata", companyCorrente.getCompanyCode());
        } else {
            System.out.println("Nessuna company selezionata");
        }// end of if/else cycle

        System.out.println("");
    }// end of method


    /**
     * Funzione
     * Controllla i metodi New
     * Con e senza company selezionata
     */
    private void testFunzioneNew() {
        int numFunzioniTotali = Funzione.countByAllCompanies();
        int numFunzioni;
        long key1;
        long key2;
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        Funzione funz = null;

        //--nessuna company corrente e nessuna company passata come pareametro
        //--non registra
        if (companyCorrente == null) {
            funz = new Funzione("alfa", "beta", "gamma");
            funz = (Funzione) funz.save();
            assertNull(funz);
            CompanySessionLib.setCompany(companyDemo);
            companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        }// end of if cycle

        //--company corrente presa in automatico
        //--registra
        funz = new Funzione("alfa", "beta", "gamma");
        funz = (Funzione) funz.save();
        assertNotNull(funz);
        key1 = funz.getId();
        assertEquals(funz.getCompany(), companyDemo);
        assertEquals(funz.getCompany(), companyCorrente);

        //--company passata come parametro
        //--registra
        funz = new Funzione(companyTest, "alfa", "beta", "gamma");
        funz = (Funzione) funz.save();
        assertNotNull(funz);
        key2 = funz.getId();
        assertEquals(funz.getCompany(), companyTest);
        assertNotSame(funz.getCompany(), companyCorrente);

        //--valore già esistente (controlla codeCompanyUnico)
        //--non registra
        funz = new Funzione(companyDemo, "alfa", "beta", "gamma");
        funz = (Funzione) funz.save();
        assertNull(funz);

        //--cancella le 2 (due) funzioni create per prova
        Funzione.find(key1).delete();
        Funzione.find(key2).delete();

        //--controlla che ci siano le stesse funzioni che c'erano all'inizio
        numFunzioni = Funzione.countByAllCompanies();
        assertEquals(numFunzioni, numFunzioniTotali);

        CompanySessionLib.setCompany(null);
    }// end of method


    /**
     * Funzione
     * Controllla i metodi Crea
     * Con e senza company selezionata
     */
    private void testFunzioneCrea() {
        int numFunzioniTotali = Funzione.countByAllCompanies();
        int numFunzioni;
        long key1;
        long key2;
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        Funzione funz = null;

        //--nessuna company corrente e nessuna company passata come pareametro
        //--non registra
        if (companyCorrente == null) {
            funz = Funzione.crea("alfa", "beta", "gamma");
            assertNull(funz);
            CompanySessionLib.setCompany(companyDemo);
            companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        }// end of if cycle

        //--company corrente presa in automatico
        //--registra
        funz = Funzione.crea("alfa", "beta", "gamma");
        assertNotNull(funz);
        key1 = funz.getId();
        assertEquals(funz.getCompany(), companyDemo);
        assertEquals(funz.getCompany(), companyCorrente);

        //--company passata come parametro
        //--registra
        funz = Funzione.crea(companyTest, "alfa", "beta", "gamma");
        assertNotNull(funz);
        key2 = funz.getId();
        assertEquals(funz.getCompany(), companyTest);
        assertNotSame(funz.getCompany(), companyCorrente);

        //--valore già esistente (controlla codeCompanyUnico)
        //--non registra
        funz = Funzione.crea(companyDemo, "alfa", "beta", "gamma");
        assertNull(funz);

        //--cancella le 2 (due) funzioni create per prova
        Funzione.find(key1).delete();
        Funzione.find(key2).delete();

        //--controlla che ci siano le stesse funzioni che c'erano all'inizio
        numFunzioni = Funzione.countByAllCompanies();
        assertEquals(numFunzioni, numFunzioniTotali);

        CompanySessionLib.setCompany(null);
    }// end of method


    /**
     * Funzione
     */
    private void testVolontario() {
        int numVolontariTotali = Volontario.countByAllCompanies();
        ArrayList<Volontario> listaVolontariTotali = Volontario.findAllAll();
        print("Numero di volontari totali (count)", numVolontariTotali);
        print("Numero di volontari totali (lista)", listaVolontariTotali.size());

        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        int numVolontariCorrenti = Volontario.countByCompany(companyCorrente);
        ArrayList<Volontario> listaVolontariCorrenti = Volontario.findAll(companyCorrente);
        print("Numero di volontari con company selezionata (count)", numVolontariCorrenti);
        print("Numero di volontari con company selezionata (lista)", listaVolontariCorrenti.size());

        int numVolontariCorrenti2 = Volontario.countByAllCompanies();
        ArrayList<Volontario> listaVolontariCorrenti2 = Volontario.findAll();
        print("Numero di volontari con company corrente (count)-2", numVolontariCorrenti2);
        print("Numero di volontari con company corrente (lista)-2", listaVolontariCorrenti2.size());

        CompanySessionLib.setCompany(null);
        int numVolontariCorrenti3 = Volontario.countByAllCompanies();
        ArrayList<Volontario> listaVolontariCorrenti3 = Volontario.findAll();
        print("Numero di volontari con company nulla (count)", numVolontariCorrenti3);
        print("Numero di volontari con company nulla (lista)", listaVolontariCorrenti3.size());
        CompanySessionLib.setCompany(companyCorrente);

        riTestCompany();
    }// end of method

    /**
     * Funzione
     */
    private void testServizio() {
        int numServiziTotali = Servizio.countByAllCompanies();
        List<Servizio> listaServiziTotali = Servizio.getListByAllCompanies();
        print("Numero di servizi totali (count)", numServiziTotali);
        print("Numero di servizi totali (lista)", listaServiziTotali.size());

        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        int numServiziCorrenti = Servizio.countByCompany(companyCorrente);
        List<Servizio> listaServiziCorrenti = Servizio.getListBySingleCompany(companyCorrente);
        print("Numero di servizi con company selezionata (count)", numServiziCorrenti);
        print("Numero di servizi con company selezionata (lista)", listaServiziCorrenti.size());

        int numServiziCorrenti2 = Servizio.countByAllCompanies();
        List<Servizio> listaServiziCorrenti2 = Servizio.getListByCurrentCompany();
        print("Numero di servizi con company corrente (count)-2", numServiziCorrenti2);
        print("Numero di servizi con company corrente (lista)-2", listaServiziCorrenti2.size());

        CompanySessionLib.setCompany(null);
        int numServiziCorrenti3 = Servizio.countByAllCompanies();
        List<Servizio> listaServiziCorrenti3 = Servizio.getListByCurrentCompany();
        print("Numero di servizi con company nulla (count)-2", numServiziCorrenti3);
        print("Numero di servizi con company nulla (lista)-2", listaServiziCorrenti3.size());
        CompanySessionLib.setCompany(companyCorrente);

        riTestCompany();
    }// end of method

    /**
     * Company
     */
    private void riTestCompany() {
        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        if (companyCorrente != null) {
            print("ri-selezionata", companyCorrente.getCompanyCode());
        } else {
            System.out.println("Nessuna company selezionata");
        }// end of if/else cycle

        System.out.println("");
    }// end of method

    /**
     * Visualizza i risultati
     */
    private void print(String sigla, Object valore) {
        System.out.println(sigla + " = " + valore);
    }// end of method


}// end of class
