package it.algos.wam.daemons;

import it.algos.wam.WAMApp;
import it.algos.wam.bootstrap.BootService;
import it.algos.webbase.domain.log.Log;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.web.lib.LibTime;

/**
 * Created by gac on 10 nov 2016.
 * .
 */
public class MigrationTask implements Runnable {

    public MigrationTask() {
    }// end of constructor

    @Override
    public void run() {
        if (Pref.getBool(WAMApp.USA_MIGRATION, true)) {
            new MigrationTask();
        }// end of if cycle
    }// end of method

}// end of runnable class
