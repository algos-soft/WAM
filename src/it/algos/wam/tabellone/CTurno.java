package it.algos.wam.tabellone;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.milite.Milite;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.wrap.Iscrizione;
import it.algos.wam.wrap.WrapTurno;

/**
 * Componente grafico che rappresenta un Turno
 * Ospita diverse Iscrizioni
 * Created by alex on 20/02/16.
 */
public class CTurno extends GridLayout {


    public CTurno(int columns, int rows) {
        super(columns, rows);
        setSpacing(false);
        setWidth("6em");
        setHeight("100%");
        addStyleName("cturno");
    }

//    /**
//     * Costruttore vuoto
//     */
//    public CTurno() {
//        super();
//    }// end of constructor




}// end of class
