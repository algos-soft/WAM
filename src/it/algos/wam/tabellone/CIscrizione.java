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

    public CIscrizione(String nome, FontAwesome icon) {
        super();

        // impostazione del layout
        setHeight(GridTabellone.H_ISCRIZIONI);
        addStyleName("ciscrizione");

        // label per l'icona della funzione
        Label iconLabel = new Label();
        iconLabel.setContentMode(ContentMode.HTML);
        iconLabel.setWidth("1em");
        if (icon != null) {
            iconLabel.setValue(icon.getHtml());
        }

        // label per il nome del volontario
        Label nameLabel = new Label();
        nameLabel.setContentMode(ContentMode.HTML);
        nameLabel.setValue(nome);

        // composizione grafica
        addComponent(iconLabel);
        addComponent(nameLabel);

    }

}
