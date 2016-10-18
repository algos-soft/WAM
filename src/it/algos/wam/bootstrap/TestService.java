package it.algos.wam.bootstrap;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;

import java.util.Date;
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
    private Volontario vol = null;
    private Turno turno = null;

    private long key1;
    private long key2;
    private long key3;

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
        testServizioCrea();
        testVolontarioNew();
        testVolontarioCrea();
        testTurnoNew();
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
        }// end of if cycle
        CompanySessionLib.setCompany(companyDemo);
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

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
        }// end of if cycle
        CompanySessionLib.setCompany(companyDemo);
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

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
        }// end of if cycle
        CompanySessionLib.setCompany(companyDemo);
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

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
        }// end of if cycle
        CompanySessionLib.setCompany(companyDemo);
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

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
     * Controllla i metodi New
     * Con e senza company selezionata
     */
    private void testVolontarioNew() {
        numRecTotali = Volontario.countByAllCompanies();
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

        //--nessuna company corrente e nessuna company passata come pareametro
        //--non registra
        if (companyCorrente == null) {
            vol = new Volontario("nome", "cognome");
            vol = (Volontario) vol.save();
            assertNull(vol);
        }// end of if cycle
        CompanySessionLib.setCompany(companyDemo);
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

        //--company corrente presa in automatico
        //--registra
        vol = new Volontario("nome", "cognome");
        vol = (Volontario) vol.save();
        assertNotNull(vol);
        key1 = vol.getId();
        assertEquals(vol.getCompany(), companyDemo);
        assertEquals(vol.getCompany(), companyCorrente);

        //--company passata come parametro
        //--registra
        vol = new Volontario(companyTest, "nome", "cognome");
        vol = (Volontario) vol.save();
        assertNotNull(vol);
        key2 = vol.getId();
        assertEquals(vol.getCompany(), companyTest);
        assertNotSame(vol.getCompany(), companyCorrente);

        //--valore già esistente (controlla codeCompanyUnico)
        //--non registra
        vol = new Volontario(companyDemo, "nome", "cognome");
        vol = (Volontario) vol.save();
        assertNull(vol);

        //--cancella le 2 (due) entity create per prova
        Volontario.find(key1).delete();
        Volontario.find(key2).delete();

        //--controlla che ci siano le stesse entities che c'erano all'inizio
        numRec = Volontario.countByAllCompanies();
        assertEquals(numRec, numRecTotali);

        CompanySessionLib.setCompany(null);
    }// end of method

    /**
     * Volontario
     * Controllla i metodi Crea
     * Con e senza company selezionata
     */
    private void testVolontarioCrea() {
        numRecTotali = Volontario.countByAllCompanies();
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

        //--nessuna company corrente e nessuna company passata come pareametro
        //--non registra
        if (companyCorrente == null) {
            vol = Volontario.crea("nome", "cognome");
            assertNull(vol);
        }// end of if cycle
        CompanySessionLib.setCompany(companyDemo);
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

        //--company corrente presa in automatico
        //--registra
        vol = Volontario.crea("nome", "cognome");
        assertNotNull(vol);
        key1 = vol.getId();
        assertEquals(vol.getCompany(), companyDemo);
        assertEquals(vol.getCompany(), companyCorrente);

        //--company passata come parametro
        //--registra
        vol = Volontario.crea(companyTest, "nome", "cognome");
        assertNotNull(vol);
        key2 = vol.getId();
        assertEquals(vol.getCompany(), companyTest);
        assertNotSame(vol.getCompany(), companyCorrente);

        //--valore già esistente (controlla codeCompanyUnico)
        //--non registra
        vol = Volontario.crea(companyDemo, "nome", "cognome");
        assertNull(vol);

        //--cancella le 2 (due) entity create per prova
        Volontario.find(key1).delete();
        Volontario.find(key2).delete();

        //--controlla che ci siano le stesse entities che c'erano all'inizio
        numRec = Volontario.countByAllCompanies();
        assertEquals(numRec, numRecTotali);

        CompanySessionLib.setCompany(null);
    }// end of method


    /**
     * Volontario
     * Controllla i metodi New
     * Con e senza company selezionata
     */
    private void testTurnoNew() {
        numRecTotali = Turno.countByAllCompanies();
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

        //--crea un servizio di appoggio
        serv = new Servizio(companyDemo, "sigla", "descrizione");
        serv = (Servizio) serv.save();
        assertNotNull(serv);
        key3 = serv.getId();

        //--nessuna company corrente e nessuna company passata come pareametro
        //--non registra
        if (companyCorrente == null) {
            turno = new Turno(serv, new Date());
            turno = (Turno) turno.save();
            assertNull(vol);
        }// end of if cycle
        CompanySessionLib.setCompany(companyDemo);
        companyCorrente = (WamCompany) CompanySessionLib.getCompany();

        //--company corrente presa in automatico
        //--registra
        turno = new Turno(serv, new Date());
        turno = (Turno) turno.save();
        assertNotNull(turno);
        key1 = turno.getId();
        assertEquals(turno.getCompany(), companyDemo);
        assertEquals(turno.getCompany(), companyCorrente);

//        //--company passata come parametro
//        //--registra
//        turno = new Turno(companyTest, serv, new Date());
//        turno = (Turno) turno.save();
//        assertNotNull(turno);
//        key2 = turno.getId();
//        assertEquals(turno.getCompany(), companyTest);
//        assertNotSame(turno.getCompany(), companyCorrente);

//        //--valore già esistente (controlla servizio (solo quelli ad orario) e data di inizio )
//        //--non registra
//        turno = new Turno(companyTest, serv, new Date());
//        turno = (Turno) turno.save();
////        assertNull(turno);

        //--cancella le 2 (due) entity create per prova
        Turno.find(key1).delete();
        Turno.find(key2).delete();
        Servizio.find(key3).delete();

        //--controlla che ci siano le stesse entities che c'erano all'inizio
        numRec = Turno.countByAllCompanies();
        assertEquals(numRec, numRecTotali);

        CompanySessionLib.setCompany(null);
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
