package it.algos.wam.daemons;

import it.algos.wam.WAMApp;
import it.algos.wam.migration.Migration;
import it.algos.webbase.domain.pref.Pref;

/**
 * Created by gac on 10 nov 2016.
 * Migrazione dei dati dal vecchio programma webAmbulanze, limitatamente ai turni dell'anno 2017
 */
public class Migration2017Task implements Runnable {

    public Migration2017Task() {
    }// end of constructor

    @Override
    public void run() {
        if (Pref.getBool(WAMApp.ATTIVA_MIGRATION, true)) {
            new Migration(2017);
        }// end of if cycle
    }// end of run method

}// end of runnable class
