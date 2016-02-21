package it.algos.wam.tabellone;

import com.vaadin.ui.VerticalLayout;

/**
 * Componente che rappresenta un Turno
 * Ospita diverse Iscrizioni
 * Created by alex on 20/02/16.
 */
public class CTurno extends VerticalLayout {

    public CTurno(CIscrizione... iscrizioni) {

        for(CIscrizione i : iscrizioni){
            addComponent(i);

        }
       // addStyleName("cturno");
    }
}
