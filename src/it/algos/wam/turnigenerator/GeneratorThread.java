package it.algos.wam.turnigenerator;

/**
 * Thread di esecuzione del generatore di turni.
 * Esegue l'operazione in un thread separato.
 */
public class GeneratorThread extends Thread {

    private TurniGenEngine generator;

    /**
     * Costruttore.
     *
     * @param generator   il motore di generazione
     */
    public GeneratorThread(TurniGenEngine generator) {
        this.generator = generator;
    }

    @Override
    public void run() {

        // avvia il motore di generazione
        generator.start();

    }


}
