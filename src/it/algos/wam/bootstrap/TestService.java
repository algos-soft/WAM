package it.algos.wam.bootstrap;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 19 mag 2016.
 * <p>
 * Run test di alcune funzionalità del database
 */

public abstract class TestService {

    public static void runTest() {
        System.out.println("Run test: ");

        testCompany();
        testFunzione();
        testVolontario();
        testServizio();
    }// end of method


    /**
     * Company
     */
    private static void testCompany() {
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
     */
    private static void testFunzione() {
        int numFunzioniTotali = Funzione.countByAllCompanies();
        List<Funzione> listaFunzioniTotali = Funzione.getListByAllCompanies();
        print("Numero di funzioni totali (count)", numFunzioniTotali);
        print("Numero di funzioni totali (lista)", listaFunzioniTotali.size());

        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        int numFunzioniCorrenti = Funzione.countByCurrentCompany();
        List<Funzione> listaFunzioniCorrenti = Funzione.getListByCurrentCompany();
        print("Numero di funzioni con company selezionata (count)", numFunzioniCorrenti);
        print("Numero di funzioni con company selezionata (lista)", listaFunzioniCorrenti.size());

        int numFunzioniCorrenti2 = Funzione.countByCurrentCompany();
        List<Funzione> listaFunzioniCorrenti2 = Funzione.getListByCurrentCompany();
        print("Numero di funzioni con company corrente (count)-2", numFunzioniCorrenti2);
        print("Numero di funzioni con company corrente (lista)-2", listaFunzioniCorrenti2.size());

        CompanySessionLib.setCompany(null);
        int numFunzioniCorrenti3 = Funzione.countByCurrentCompany();
        List<Funzione> listaFunzioniCorrenti3 = Funzione.getListByCurrentCompany();
        print("Numero di funzioni con company nulla (count)", numFunzioniCorrenti3);
        print("Numero di funzioni con company nulla (lista)", listaFunzioniCorrenti3.size());
        CompanySessionLib.setCompany(companyCorrente);

        Funzione funz1= Funzione.getEntityBySigla("Cen");
        Funzione funz2= Funzione.getEntityBySigla("Bar");

        BaseEntity funz4= AQuery.findOne(Funzione.class, Funzione_.sigla, "Bar");
        BaseEntity funz3= AQuery.findOne(Funzione.class, Funzione_.sigla, "Cen");

        BaseEntity funz5= AQuery.findFirst(Funzione.class, Funzione_.sigla, "Bar");
        BaseEntity funz6= AQuery.findFirst(Funzione.class, Funzione_.sigla, "Cen");

        riTestCompany();
    }// end of method


    /**
     * Funzione
     */
    private static void testVolontario() {
        int numVolontariTotali = Volontario.countAll();
        ArrayList<Volontario> listaVolontariTotali = Volontario.findAllAll();
        print("Numero di volontari totali (count)", numVolontariTotali);
        print("Numero di volontari totali (lista)", listaVolontariTotali.size());

        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        int numVolontariCorrenti = Volontario.count(companyCorrente);
        ArrayList<Volontario> listaVolontariCorrenti = Volontario.findAll(companyCorrente);
        print("Numero di volontari con company selezionata (count)", numVolontariCorrenti);
        print("Numero di volontari con company selezionata (lista)", listaVolontariCorrenti.size());

        int numVolontariCorrenti2 = Volontario.count();
        ArrayList<Volontario> listaVolontariCorrenti2 = Volontario.findAll();
        print("Numero di volontari con company corrente (count)-2", numVolontariCorrenti2);
        print("Numero di volontari con company corrente (lista)-2", listaVolontariCorrenti2.size());

        CompanySessionLib.setCompany(null);
        int numVolontariCorrenti3 = Volontario.count();
        ArrayList<Volontario> listaVolontariCorrenti3 = Volontario.findAll();
        print("Numero di volontari con company nulla (count)", numVolontariCorrenti3);
        print("Numero di volontari con company nulla (lista)", listaVolontariCorrenti3.size());
        CompanySessionLib.setCompany(companyCorrente);

        riTestCompany();
    }// end of method

    /**
     * Funzione
     */
    private static void testServizio() {
        int numServiziTotali = Servizio.countByAllCompanies();
        List<Servizio> listaServiziTotali = Servizio.getListByAllCompanies();
        print("Numero di servizi totali (count)", numServiziTotali);
        print("Numero di servizi totali (lista)", listaServiziTotali.size());

        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        int numServiziCorrenti = Servizio.countBySingleCompany(companyCorrente);
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
    private static void riTestCompany() {
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
    private static void print(String sigla, Object valore) {
        System.out.println(sigla + " = " + valore);
    }// end of method


}// end of class
