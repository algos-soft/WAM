package it.algos.wam.turnigenerator;

import java.util.ArrayList;

/**
 * Motore di generazione/eliminazione di turni
 */
public class TurniGenEngine {

    private GeneratorData data;
    private ArrayList<TurnoProgressListener> turnoProgressListeners = new ArrayList<>();
    private ArrayList<EngineDoneListener> engineDoneListeners = new ArrayList<>();
    private boolean abort;
    private long lastUpdateMillis;

    private static final int MAX=50;

    /**
     * @param data i dati di impostazione del motore
     */
    public TurniGenEngine(GeneratorData data) {
        this.data = data;
    }

    /**
     * Avvia il motore
     */
    public void start(){

        lastUpdateMillis=System.currentTimeMillis();

        int done=0;

        for(int i=0; i<MAX; i++){

            try {
                Thread.sleep(100);
                done++;

                // progress update - max 1 al secondo
                if((System.currentTimeMillis()-lastUpdateMillis)>1000){
                    lastUpdateMillis=System.currentTimeMillis();
                    fireTurnoProgressListeners(done);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // se abort è acceso forza uscita
            if(abort){
                i=MAX;
            }

        }

        fireEngineDoneListeners();

    }

    /**
     * Ritorna il numero totale di turni da creare / cancellare
     * */
    public int getQuantiTurni(){
        return MAX;
    }

    /**
     * Abortisce l'esecuzione
     */
    public void abort(){
        abort=true;
    }

    private void fireTurnoProgressListeners(int progress){
        for(TurnoProgressListener l : turnoProgressListeners){
            l.progressUpdate(progress);
        }
    }


    public void addTurnoProgressListener(TurnoProgressListener l){
        turnoProgressListeners.add(l);
    }


    public interface TurnoProgressListener {
        void progressUpdate(int progress);
    }


    private void fireEngineDoneListeners(){
        for(EngineDoneListener l : engineDoneListeners){
            l.engineDone();
        }
    }


    public void addEngineDoneListener(EngineDoneListener l){
        engineDoneListeners.add(l);
    }


    public interface EngineDoneListener{
        void engineDone();
    }

}
