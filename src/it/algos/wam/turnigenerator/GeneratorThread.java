package it.algos.wam.turnigenerator;

import com.vaadin.ui.ProgressBar;

/**
 * Thread di esecuzione del generatore di turni.
 * Esegue l'operazione in un thread separato e si occupa di
 * visualizzare il progresso nella UI
 */
public class GeneratorThread extends Thread {

    private TurniGenerator generator;
    private ProgressBar progressBar;

    /**
     * Costruttore.
     *
     * @param generator   il motore di generazione
     * @param progressBar la progressBar da aggiornare sulla UI
     */
    public GeneratorThread(TurniGenerator generator, ProgressBar progressBar) {
        this.generator = generator;
        this.progressBar = progressBar;
    }

    @Override
    public void run() {

        // resett la progress bar
        progressBar.getUI().access(new Runnable() {
            @Override
            public void run() {
                progressBar.setCaption("");
                progressBar.setValue(0f);
                progressBar.removeStyleName("invisible");
            }
        });

        // aggiunge un listener per essere informato ogni volta che un turno è fatto
        // e aggiornare la progressbar
        generator.addTurnoDoneListener(new TurniGenerator.TurnoDoneListener() {

            int done = 0;

            @Override
            public void turnoDone() {
                done++;

                progressBar.getUI().access(new Runnable() {
                    @Override
                    public void run() {
                        float progress = (float) done / (generator.getQuantiTurni());
                        progressBar.setCaption(done+"/"+generator.getQuantiTurni());
                        progressBar.setValue(progress);
                    }
                });


            }
        });

        // avvia il motore di generazione
        generator.start();

    }


}
