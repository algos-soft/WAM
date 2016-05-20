package it.algos.wam.bootstrap;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.EM;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

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
        ArrayList<WamCompany> listaCompany = WamCompany.findAll();
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

    }// end of method


    /**
     * Funzione
     */
    private static void testFunzione() {
        int numFunzioniTotali = Funzione.countAll();
        ArrayList<Funzione> listaFunzioniTotali = Funzione.findAllAll();
        print("Numero di funzioni totali (count)", numFunzioniTotali);
        print("Numero di funzioni totali (lista)", listaFunzioniTotali.size());

        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        int numFunzioniCorrenti = Funzione.count(companyCorrente);
        ArrayList<Funzione> listaFunzioniCorrenti = Funzione.findAll(companyCorrente);
        print("Numero di funzioni company corrente (count)", numFunzioniCorrenti);
        print("Numero di funzioni company corrente (lista)", listaFunzioniCorrenti.size());

        int numFunzioniCorrenti2 = Funzione.count();
        ArrayList<Funzione> listaFunzioniCorrenti2 = Funzione.findAll();
        print("Numero di funzioni company corrente (count)-2", numFunzioniCorrenti2);
        print("Numero di funzioni company corrente (lista)-2", listaFunzioniCorrenti2.size());

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
        print("Numero di volontari company corrente (count)", numVolontariCorrenti);
        print("Numero di volontari company corrente (lista)", listaVolontariCorrenti.size());

        int numVolontariCorrenti2 = Volontario.count();
        ArrayList<Volontario> listaVolontariCorrenti2 = Volontario.findAll();
        print("Numero di volontari company corrente (count)-2", numVolontariCorrenti2);
        print("Numero di volontari company corrente (lista)-2", listaVolontariCorrenti2.size());

    }// end of method

    /**
     * Funzione
     */
    private static void testServizio() {
        int numServiziTotali = Servizio.countAll();
        ArrayList<Servizio> listaServiziTotali = Servizio.findAllAll();
        print("Numero di servizi totali (count)", numServiziTotali);
        print("Numero di servizi totali (lista)", listaServiziTotali.size());

        WamCompany companyCorrente = (WamCompany) CompanySessionLib.getCompany();
        int numServiziCorrenti = Servizio.count(companyCorrente);
        ArrayList<Servizio> listaServiziCorrenti = Servizio.findAll(companyCorrente);
        print("Numero di servizi company corrente (count)", numServiziCorrenti);
        print("Numero di servizi company corrente (lista)", listaServiziCorrenti.size());

        int numServiziCorrenti2 = Servizio.count();
        ArrayList<Servizio> listaServiziCorrenti2 = Servizio.findAll();
        print("Numero di servizi company corrente (count)-2", numServiziCorrenti2);
        print("Numero di servizi company corrente (lista)-2", listaServiziCorrenti2.size());

    }// end of method

    /**
     * Visualizza i risultati
     */
    private static void print(String sigla, Object valore) {
        System.out.println(sigla + " = " + valore);
    }// end of method


}// end of class
