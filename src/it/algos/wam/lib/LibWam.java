package it.algos.wam.lib;

import it.algos.webbase.web.lib.LibDate;

import java.util.Date;

/**
 * Created by gac on 29 feb 2016.
 * Classe statica di libreria
 */
public abstract class LibWam {

    /**
     * Costruisce una chiave della data
     * Usato per la indicizzaione dei Turni
     */
    public static long creaChiave(Date data) {
        long chiave = 0;
        int anno;
        int giorno;

        if (data != null) {
            anno = LibDate.getYear(data);
            giorno = LibDate.getDayYear(data);
            chiave = anno * 1000 + giorno;
        }// end of if cycle

        return chiave;
    }// end of method

}// end of abstract static class
