package it.algos.wam.entity.servizio;

import com.vaadin.ui.ComboBox;

/**
 * Created by gac on 06 nov 2016.
 * Combo per i minuti
 */
public class ComboMinuti extends ComboBox {


    public ComboMinuti() {
        setWidth("5em");
        setTextInputAllowed(false);
        setNewItemsAllowed(false);

        inizia();
    }// end of constructor

    /**
     * Inizializzazione
     */
    public void inizia() {
        addItem(intToString(0));
        addItem(intToString(15));
        addItem(intToString(30));
        addItem(intToString(45));
    }// end of method

    /**
     * Trasforma un intero in stringa
     *
     * @param i l'intero
     * @return la stringa
     */
    private String intToString(int i) {
        String s = "" + i;
        if (s.length() == 1) {
            s = "0" + s;
        }// end of if cycle

        return s;
    }// end of method

    /**
     * Returns the minute
     *
     * @return the minute, -1 if not selected
     */
    public int getMinute() {
        int num = -1;
        Object value = getValue();

        if (value != null) {
            num = Integer.parseInt(value.toString());
        }// end of if cycle

        return num;
    }// end of method

    /**
     * Assegna i minuti
     *
     * @param m i minuti
     */
    public void setMinute(Integer m) {
        setValue(intToString(m));
    }// end of method

    @Override
    public boolean isValid() {
        return (getMinute() != -1);
    }// end of method


}// end of class
