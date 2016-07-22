package it.algos.wam.turnigenerator;

import java.util.Date;

/**
 * Wrapper dei dati di configurazione per il motore di generazione turni.
 */
public class GeneratorData {
    private Date d1;
    private Date d2;
    private int action;

    public static final int ACTION_CREATE = 1;
    public static final int ACTION_DELETE = 2;

    public GeneratorData(Date d1, Date d2, int action) {
        this.d1 = d1;
        this.d2 = d2;
        this.action = action;
    }


    /**
     * Controlla se i dati sono validi e congrui.
     *
     * @return stringa vuota se ok, motivo se non validi
     */
    public String checkValid() {
        return "";
    }

    public Date getD1() {
        return d1;
    }

    public Date getD2() {
        return d2;
    }

    public int getAction() {
        return action;
    }
}
