package it.algos.wam.tabellone;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Componente grafico che rappresenta l'assenza di turno
 */
public class CNoTurno extends VerticalLayout {


    public CNoTurno() {
        super();
        setSpacing(false);
        setWidth("6em");
        setHeight("100%");
        addStyleName("cnoturno");
        addComponent(new Label("non previsto"));
    }


}// end of class
