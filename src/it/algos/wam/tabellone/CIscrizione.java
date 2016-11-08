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
     * @param rightIconTooltip il tooltip da visualizzare su hover dell'icona di destra
     */
    public CIscrizione(String nome, FontAwesome leftIcon, FontAwesome rightIcon, String rightIconTooltip) {
        super();

        setWidth("100%");

        // impostazione del layout
        setHeight(GridTabellone.H_ISCRIZIONI);
        addStyleName("ciscrizione");

        // label per l'icona della funzione
        Label leftIconLabel = new Label();
        leftIconLabel.setContentMode(ContentMode.HTML);
        leftIconLabel.setWidth("1em");
        if (leftIcon != null) {
            leftIconLabel.setValue(leftIcon.getHtml());
        }

        // label per il nome del volontario
        Label nameLabel = new Label();
        nameLabel.setContentMode(ContentMode.HTML);
        String text=nome;
        nameLabel.setValue(text);

        // label per l'icona a destra
        Label rightIconLabel = new Label();
        if (rightIcon != null) {
            rightIconLabel.setContentMode(ContentMode.HTML);
            rightIconLabel.setWidth("1em");
            rightIconLabel.setValue(rightIcon.getHtml());
            rightIconLabel.setDescription(rightIconTooltip);
            rightIconLabel.addStyleName("red");
        }


        // composizione grafica
        addComponent(leftIconLabel);
        addComponent(nameLabel);
        addComponent(rightIconLabel);

        setExpandRatio(nameLabel, 1.0f);

    }

}
