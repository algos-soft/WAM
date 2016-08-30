package it.algos.wam.lib;

import java.util.ArrayList;

/**
 * Created by Gac on 08 mar 2016.
 * .
 */
public enum WamRuoli {

    developer("developer"),
    custode("custode"),
    admin("admin"),
    user("user"),
    guest("guest");

    private String nome;

    WamRuoli(String nome) {
        this.nome = nome;
    }

    public static ArrayList<String> getAllNames() {
        ArrayList<String> lista = new ArrayList<String>();

        for (WamRuoli tipo : WamRuoli.values()) {
            lista.add(tipo.toString());
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
