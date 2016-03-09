package it.algos.wam.lib;

import it.algos.webbase.web.lib.DateConvertUtils;
import it.algos.webbase.web.lib.LibDate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


/**
 * Created by gac on 29 feb 2016.
 * Classe statica di libreria
 */
public abstract class LibWam {


    /**
     * Costruisce una chiave della data
     * Usato per la indicizzazione dei Turni
     */
    public static int creaChiave(Date data) {
        int chiave = 0;
        int anno;
        int giorno;

        if (data != null) {
            anno = LibDate.getYear(data);
            giorno = LibDate.getDayYear(data);
            chiave = anno * 1000 + giorno;
        }// end of if cycle

        return chiave;
    }// end of method


    /**
     * Costruisce una chiave da una LocalDate
     */
    public static int creaChiave(LocalDate data) {
        Date d = DateConvertUtils.asUtilDate(data);
        return creaChiave(d);
    }




}// end of abstract static class
