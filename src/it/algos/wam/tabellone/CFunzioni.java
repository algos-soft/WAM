package it.algos.wam.tabellone;


import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.wrap.WrapServizio;

import java.util.List;

/**
 * Componente grafico che rappresenta un elenco di funzioni
 * Created by alex on 21/02/16.
 */
public class CFunzioni extends VerticalLayout {

    /**
     * Costruttore completo
     */
    public CFunzioni() {
        //addStyleName("greenBg");
        setSpacing(false);
        setWidth("3em");
    }

    /**
     * Aggiunge un componente grafico rappresentante una funzione
     *
     * @param nome nome della funzione
     * @return il componente grafico aggiuntp
     */
    public Component addFunzione(String nome) {
        Label label = new Label(nome);
        label.addStyleName("cfunzione");
        addComponent(label);
        return label;
    }


}// end of class
