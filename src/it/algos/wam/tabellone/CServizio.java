package it.algos.wam.tabellone;

import com.vaadin.ui.Label;

/**
 * Componente che rapresenta la cella con il titolo del servizio nel tabellone.
 * Created by alex on 20/02/16.
 */
public class CServizio extends Label {
    public CServizio(String nome) {
        super(nome);
        addStyleName("greenBg");
    }
}
