package it.algos.wam.lib;

import java.util.ArrayList;

/**
 * Created by Gac on 08 mar 2016.
 * Ruoli specifici di questa applicazione
 */
public enum WamRuoli {

    developer("ROLE_developer"),
    custode("ROLE_custode"),
    admin("ROLE_admin"),
    user("ROLE_user"),
    guest("ROLE_guest");

    private String nome;

    WamRuoli(String nome) {
        this.nome = nome;
    }// end of basic constructor

    public static ArrayList<String> getAllNames() {
        ArrayList<String> lista = new ArrayList<String>();

        for (WamRuoli tipo : WamRuoli.values()) {
            lista.add(tipo.getNome());
        }// fine del ciclo for

        return lista;
    }// fine del metodo statico

    public static WamRuoli get(String nomeRuolo) {
        WamRuoli wam = null;

        for (WamRuoli wamTmp : values()) {
            if (wamTmp.getNome().equals(nomeRuolo)) {
                wam = wamTmp;
                break;
            }// fine del blocco if
        } // fine del ciclo for-each

        return wam;
    }// fine del metodo statico

    public String getNome() {
        return nome;
    }
}// end of enumeration
