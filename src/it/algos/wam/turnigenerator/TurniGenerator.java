package it.algos.wam.turnigenerator;

import java.util.ArrayList;

/**
 * Motore di generazione/eliminazione di turni
 */
public class TurniGenerator {

    private GeneratorData data;
    private ArrayList<TurnoDoneListener> turnoDoneListeners = new ArrayList<>();
    private ArrayList<EngineDoneListener> engineDoneListeners = new ArrayList<>();
    private boolean abort;

    private static final int MAX=50;

    /**
     * @param data i dati di impostazione del motore
     */
    public TurniGenerator(GeneratorData data) {
        this.data = data;
    }

    /**
     * Avvia il motore
     */
    public void start(){



        for(int i=0; i<MAX; i++){

            try {
                Thread.sleep(100);
                fireTurnoDoneListeners();
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

    private void fireTurnoDoneListeners(){
        for(TurnoDoneListener l : turnoDoneListeners){
            l.turnoDone();
        }
    }


    public void addTurnoDoneListener(TurnoDoneListener l){
        turnoDoneListeners.add(l);
    }


    public interface TurnoDoneListener{
        void turnoDone();
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
