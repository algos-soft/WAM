package it.algos.wam.entity.servizio;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;

/**
 * Created by gac on 06 nov 2016.
 * Combo per l'ora
 */
public class ComboOra extends ComboBox {


    public ComboOra() {
        setWidth("5em");
        setTextInputAllowed(false);
        setNewItemsAllowed(false);

        inizia();
    }// end of constructor


    /**
     * Inizializzazione
     */
    public void inizia() {
        for (int i = 0; i < 24; i++) {
            addItem(intToString(i));
        }// end of for cycle
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
     * Returns the hour
     *
     * @return the hour, -1 if not selected
     */
    public int getHour() {
        int num = -1;
        Object value = getValue();

        if (value != null) {
            num = Integer.parseInt(value.toString());
        }// end of if cycle

        return num;
    }// end of method

    /**
     * Assegna l'ora
     *
     * @param h l'ora
     */
    public void setHour(Integer h) {
        setValue(intToString(h));
    }// end of method

    @Override
    public boolean isValid() {
        return (getHour() != -1);
    }// end of method

}// end of class
