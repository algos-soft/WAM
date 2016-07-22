package it.algos.wam.turnigenerator;

import java.util.ArrayList;

/**
 * Motore di generazione/eliminazione di turni
 */
public class TurniGenerator {

    private GeneratorData data;
    private ArrayList<TurnoDoneListener> turnoDoneListeners = new ArrayList<>();
    private ArrayList<EngineDoneListener> engineDoneListeners = new ArrayList<>();
    boolean abort;

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


        int max=10;

        for(int i=0; i<max; i++){

            try {
                Thread.sleep(500);
                fireTurnoDoneListeners();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // se abort è acceso forza uscita
            if(abort){
                i=max;
            }

        }

        fireEngineDoneListeners();

    }

    /**
     * Ritorna il numero totale di turni da creare / cancellare
     * */
    public int getQuantiTurni(){
        return 10;
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
