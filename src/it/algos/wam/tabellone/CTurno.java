package it.algos.wam.tabellone;

import com.vaadin.ui.VerticalLayout;

/**
 * Componente grafico che rappresenta un Turno
 * Ospita diverse Iscrizioni
 * Created by alex on 20/02/16.
 */
public class CTurno extends VerticalLayout {

    public CTurno(CIscrizione... iscrizioni) {

        setSpacing(false);

        setWidth("6em");

        for(CIscrizione i : iscrizioni){
            addComponent(i);
        }

    }
}
