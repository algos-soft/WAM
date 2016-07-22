package it.algos.wam.turnigenerator;

import java.util.ArrayList;

/**
 * Motore di generazione/eliminazione di turni
 */
public class TurniGenerator {

    private GeneratorData data;
    private ArrayList<TurnoDoneListener> turnoDoneListeners = new ArrayList<>();

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
        for(int i=0; i<10; i++){

            try {
                Thread.sleep(500);
                fireTurnoDoneListeners();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Ritorna il numero totale di turni da creare / cancellare
     * */
    public int getQuantiTurni(){
        return 10;
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
}
