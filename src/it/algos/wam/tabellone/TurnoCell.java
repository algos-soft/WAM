package it.algos.wam.tabellone;

import com.vaadin.ui.Component;

/**
 * Created by alex on 05/03/16.
 */
public interface TurnoCell extends Component {

    /**
     * @return x la colonna del tabellone in cui è posizionato questo componente
     */
    int getX();

    /**
     * @param x la colonna del tabellone in cui è posizionato questo componente
     */
    void setX(int x);

    /**
     * @return la riga del tabellone in cui è posizionato questo componente
     */
    int getY();

    /**
     * @return la colonna del tabellone in cui è posizionato questo componente
     */
    void setY(int y);

}
