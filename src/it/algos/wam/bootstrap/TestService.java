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

    private Funzione funz = null;
    private Servizio serv = null;

    private long key1;
    private long key2;

    private int numRecTotali;
    private int numRec;

    /**
     * Costruttore
     */
    public TestService() {
        System.out.println("Run test: ");

        testCompany();
        testFunzioneNew();
        testFunzioneCrea();
        testServizioNew();
        testVolontario();
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
        numRecTotali = Funzione.countByAllCompanies();
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

        //--nessuna company corrente e nessuna company passata come pareametro
        //--non registra
        if (companyCorrente == null) {
            funz = new Funzione("code", "sigla", "descrizione");
            funz = (Funzione) funz.save();
            assertNull(funz);
            CompanySessionLib.setCompany(companyDemo);
            companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        }// end of if cycle

        //--company corrente presa in automatico
        //--registra
        funz = new Funzione("code", "sigla", "descrizione");
        funz = (Funzione) funz.save();
        assertNotNull(funz);
        key1 = funz.getId();
        assertEquals(funz.getCompany(), companyDemo);
        assertEquals(funz.getCompany(), companyCorrente);

        //--company passata come parametro
        //--registra
        funz = new Funzione(companyTest, "code", "sigla", "descrizione");
        funz = (Funzione) funz.save();
        assertNotNull(funz);
        key2 = funz.getId();
        assertEquals(funz.getCompany(), companyTest);
        assertNotSame(funz.getCompany(), companyCorrente);

        //--valore già esistente (controlla codeCompanyUnico)
        //--non registra
        funz = new Funzione(companyDemo, "code", "sigla", "descrizione");
        funz = (Funzione) funz.save();
        assertNull(funz);

        //--cancella le 2 (due) entity create per prova
        Funzione.find(key1).delete();
        Funzione.find(key2).delete();

        //--controlla che ci siano le stesse entities che c'erano all'inizio
        numRec = Funzione.countByAllCompanies();
        assertEquals(numRec, numRecTotali);

        CompanySessionLib.setCompany(null);
    }// end of method


    /**
     * Funzione
     * Controllla i metodi Crea
     * Con e senza company selezionata
     */
    private void testFunzioneCrea() {
        numRecTotali = Funzione.countByAllCompanies();
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

        //--nessuna company corrente e nessuna company passata come pareametro
        //--non registra
        if (companyCorrente == null) {
            funz = Funzione.crea("code", "sigla", "descrizione");
            assertNull(funz);
            CompanySessionLib.setCompany(companyDemo);
            companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        }// end of if cycle

        //--company corrente presa in automatico
        //--registra
        funz = Funzione.crea("code", "sigla", "descrizione");
        assertNotNull(funz);
        key1 = funz.getId();
        assertEquals(funz.getCompany(), companyDemo);
        assertEquals(funz.getCompany(), companyCorrente);

        //--company passata come parametro
        //--registra
        funz = Funzione.crea(companyTest, "code", "sigla", "descrizione");
        assertNotNull(funz);
        key2 = funz.getId();
        assertEquals(funz.getCompany(), companyTest);
        assertNotSame(funz.getCompany(), companyCorrente);

        //--valore già esistente (controlla codeCompanyUnico)
        //--non registra
        funz = Funzione.crea(companyDemo, "code", "sigla", "descrizione");
        assertNull(funz);

        //--cancella le 2 (due) entity create per prova
        Funzione.find(key1).delete();
        Funzione.find(key2).delete();

        //--controlla che ci siano le stesse entities che c'erano all'inizio
        numRec = Funzione.countByAllCompanies();
        assertEquals(numRec, numRecTotali);

        CompanySessionLib.setCompany(null);
    }// end of method


    /**
     * Servizio
     * Controllla i metodi New
     * Con e senza company selezionata
     */
    private void testServizioNew() {
        numRecTotali = Servizio.countByAllCompanies();
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

        //--nessuna company corrente e nessuna company passata come pareametro
        //--non registra
        if (companyCorrente == null) {
            serv = new Servizio("sigla", "descrizione");
            serv = (Servizio) serv.save();
            assertNull(serv);
            CompanySessionLib.setCompany(companyDemo);
            companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        }// end of if cycle

        //--company corrente presa in automatico
        //--registra
        serv = new Servizio("sigla", "descrizione");
        serv = (Servizio) serv.save();
        assertNotNull(serv);
        key1 = serv.getId();
        assertEquals(serv.getCompany(), companyDemo);
        assertEquals(serv.getCompany(), companyCorrente);

        //--company passata come parametro
        //--registra
        serv = new Servizio(companyTest, "sigla", "descrizione");
        serv = (Servizio) serv.save();
        assertNotNull(serv);
        key2 = serv.getId();
        assertEquals(serv.getCompany(), companyTest);
        assertNotSame(serv.getCompany(), companyCorrente);

        //--valore già esistente (controlla codeCompanyUnico)
        //--non registra
        serv = new Servizio(companyDemo, "sigla", "descrizione");
        serv = (Servizio) serv.save();
        assertNull(serv);

        //--cancella le 2 (due) entity create per prova
        Servizio.find(key1).delete();
        Servizio.find(key2).delete();

        //--controlla che ci siano le stesse entities che c'erano all'inizio
        numRec = Servizio.countByAllCompanies();
        assertEquals(numRec, numRecTotali);

        CompanySessionLib.setCompany(null);
    }// end of method

    /**
     * Servizio
     * Controllla i metodi Crea
     * Con e senza company selezionata
     */
    private void testServizioCrea() {
        numRecTotali = Servizio.countByAllCompanies();
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

        //--nessuna company corrente e nessuna company passata come pareametro
        //--non registra
        if (companyCorrente == null) {
            serv = Servizio.crea("sigla", "descrizione");
            assertNull(serv);
            CompanySessionLib.setCompany(companyDemo);
            companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        }// end of if cycle

        //--company corrente presa in automatico
        //--registra
        serv = Servizio.crea("sigla", "descrizione");
        assertNotNull(serv);
        key1 = serv.getId();
        assertEquals(serv.getCompany(), companyDemo);
        assertEquals(serv.getCompany(), companyCorrente);

        //--company passata come parametro
        //--registra
        serv = Servizio.crea(companyTest, "sigla", "descrizione");
        assertNotNull(serv);
        key2 = serv.getId();
        assertEquals(serv.getCompany(), companyTest);
        assertNotSame(serv.getCompany(), companyCorrente);

        //--valore già esistente (controlla codeCompanyUnico)
        //--non registra
        serv = Servizio.crea(companyDemo, "sigla", "descrizione");
        assertNull(serv);

        //--cancella le 2 (due) entity create per prova
        Servizio.find(key1).delete();
        Servizio.find(key2).delete();

        //--controlla che ci siano le stesse entities che c'erano all'inizio
        numRec = Servizio.countByAllCompanies();
        assertEquals(numRec, numRecTotali);

        CompanySessionLib.setCompany(null);
    }// end of method

    /**
     * Volontario
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
