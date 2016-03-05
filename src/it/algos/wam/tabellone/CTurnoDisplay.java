package it.algos.wam.tabellone;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.GridLayout;

/**
 * Componente grafico che rappresenta un Turno visualizzato
 * all'interno del Tabellone.
 * Ospita diverse Iscrizioni.
 * Created by alex on 20/02/16.
 */
public class CTurnoDisplay extends GridLayout implements TabelloneCell {

    private CTabellone tabellone;
    private int x;
    private int y;

    /**
     * Costruttore
     *
     * @param tabellone il tabellone di riferimento
     * @param rows      il numero di righe nella griglia
     */
    public CTurnoDisplay(CTabellone tabellone, int rows) {
        super(1, rows);
        this.tabellone = tabellone;
        setSpacing(false);
        setWidth("6em");
        setHeight("100%");
        addStyleName("cturno");

        addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                tabellone.cellClicked(x, y);
            }
        });

    }

    /**
     * @return x la colonna del tabellone in cui è posizionato questo componente
     */
    public int getX() {
        return x;
    }

    /**
     * @param x la colonna del tabellone in cui è posizionato questo componente
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return la riga del tabellone in cui è posizionato questo componente
     */
    public int getY() {
        return y;
    }

    /**
     * @return la colonna del tabellone in cui è posizionato questo componente
     */
    public void setY(int y) {
        this.y = y;
    }

}// end of class
