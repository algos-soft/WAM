package it.algos.wam.tabellone;


import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

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
        Label label = new Label(nome, ContentMode.HTML);
        label.setHeight(GridTabellone.H_ISCRIZIONI);
        label.addStyleName("cfunzione");
        addComponent(label);
        return label;
    }


}// end of class
