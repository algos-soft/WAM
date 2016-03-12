package it.algos.wam.tabellone;

import com.vaadin.ui.Label;

import java.time.LocalDate;

/**
 * Componente che rappresenta una singola Iscrizione
 * Created by alex on 21/02/16.
 */
public class CIscrizione extends Label {

    public CIscrizione(String nome) {
        super(nome);

        setHeight("100%");

        addStyleName("ciscrizione");
    }

}
