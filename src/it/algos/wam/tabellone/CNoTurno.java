package it.algos.wam.tabellone;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Componente grafico che rappresenta l'assenza di turno
 */
public class CNoTurno extends VerticalLayout implements TabelloneCell {

    private CTabellone tabellone;
    private InfoNewTurnoWrap wrapper;
    private int x;
    private int y;


    public CNoTurno(CTabellone tabellone, InfoNewTurnoWrap wrapper) {
        super();
        this.tabellone=tabellone;
        this.wrapper=wrapper;
        setSpacing(false);
        setWidth("6em");
        setHeight("100%");
        addStyleName("cnoturno");
        addComponent(new Label("non previsto"));

        addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                tabellone.cellClicked(CellType.NO_TURNO, x, y, wrapper);
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
