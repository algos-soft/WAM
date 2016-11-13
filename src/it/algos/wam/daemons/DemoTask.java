package it.algos.wam.daemons;

import it.algos.wam.WAMApp;
import it.algos.wam.bootstrap.BootService;
import it.algos.webbase.domain.log.Log;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.web.lib.LibTime;

/**
 * Created by gac on 10 nov 2016.
 * <p>
 * Task eseguito periodicamente (vedi WamScheduler).
 * Cancella e ricrea la company demo per ripulirla ed ricostruire i turni prima e dopo il periodo attuale
 * Uso:
 * E' un Runnable, implementare il codice da eseguire nel metodo run()
 */
public class DemoTask implements Runnable {

    public DemoTask() {
    }// end of constructor

    @Override
    public void run() {

        if (Pref.getBool(WAMApp.USA_REFRESH_DEMO, true)) {
            long inizio = System.currentTimeMillis();
            BootService.creaCompanyDemo();
            Log.debug("demo", "Ricostruita la company " + WAMApp.DEMO_COMPANY_CODE + " in " + LibTime.difText(inizio));
        }// end of if cycle

    }// end of method

}// end of runnable class
