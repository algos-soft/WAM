package it.algos.wam.turnigenerator;

import com.vaadin.ui.ProgressBar;

import java.util.ArrayList;

/**
 * Thread di esecuzione del generatore di turni.
 * Esegue l'operazione in un thread separato.
 */
public class GeneratorThread extends Thread {

    private TurniGenerator generator;

    /**
     * Costruttore.
     *
     * @param generator   il motore di generazione
     */
    public GeneratorThread(TurniGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run() {

        // avvia il motore di generazione
        generator.start();

    }


}
