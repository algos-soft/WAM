package it.algos.wam.entity.servizio;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Created by gac on 06 nov 2016.
 * Componente con ore e minuti
 */
public class OreMinuti extends HorizontalLayout {

    private ComboOra cOre;
    private ComboMinuti cMinuti;


    public OreMinuti(String title) {
        this(title, 0, 0);
    }// end of constructor

    public OreMinuti(String title, int ore, int minuti) {
        setCaption(title);
        setSpacing(false);

        inizia(title, ore, minuti);
    }// end of constructor


    /**
     * Inizializzazione
     */
    public void inizia(String title, int ore, int minuti) {
        Label label = new Label("&nbsp;:&nbsp;", ContentMode.HTML);
        cOre = new ComboOra();
        cMinuti = new ComboMinuti();

        this.setHour(ore);
        this.setMinute(minuti);

        addComponent(cOre);
        addComponent(label);
        addComponent(cMinuti);

        setComponentAlignment(cOre, Alignment.MIDDLE_CENTER);
        setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        setComponentAlignment(cMinuti, Alignment.MIDDLE_CENTER);
    }// end of method

    /**
     * Assegna ore e minuti
     *
     * @param h le ore
     * @param m i minuti
     * @deprecated
     */
    public void setTime(Integer h, Integer m) {
        cOre.setHour(h);
        cMinuti.setMinute(m);
    }// end of method

    public int getHour() {
        return cOre.getHour();
    }// end of method

    public void setHour(int ore) {
        cOre.setHour(ore);
    }// end of method

    public int getMinute() {
        return cMinuti.getMinute();
    }// end of method

    public void setMinute(int minuti) {
        cMinuti.setMinute(minuti);
    }// end of method

    public boolean isValid() {
        return (cOre.isValid() && cMinuti.isValid());
    }// end of method

}// end of class
