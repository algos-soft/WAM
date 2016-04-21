package it.algos.wam.tabellone;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import java.time.LocalDate;

/**
 * Componente che rappresenta una singola Iscrizione
 * Created by alex on 21/02/16.
 */
public class CIscrizione extends HorizontalLayout {

    /**
     * Costruttore
     * @param nome il nome del volontario
     * @param leftIcon l'icona di sinistra
     * @param rightIcon l'icona di destra
     *
     * */
    public CIscrizione(String nome, FontAwesome leftIcon, FontAwesome rightIcon) {
        super();

        // impostazione del layout
        setHeight(GridTabellone.H_ISCRIZIONI);
        addStyleName("ciscrizione");

        // label per l'icona della funzione
        Label iconLabel = new Label();
        iconLabel.setContentMode(ContentMode.HTML);
        iconLabel.setWidth("1em");
        if (leftIcon != null) {
            iconLabel.setValue(leftIcon.getHtml());
        }

        // label per il nome del volontario
        Label nameLabel = new Label();
        nameLabel.setContentMode(ContentMode.HTML);
        String text=nome;
        if(rightIcon!=null){
            text+=" "+rightIcon.getHtml();
        }
        nameLabel.setValue(text);

        // composizione grafica
        addComponent(iconLabel);
        addComponent(nameLabel);

    }

}
