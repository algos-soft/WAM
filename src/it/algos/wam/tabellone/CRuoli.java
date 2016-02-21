package it.algos.wam.tabellone;


import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Componente che rappresenta un elenco di ruoli
 * Created by alex on 21/02/16.
 */
public class CRuoli extends VerticalLayout {

    public CRuoli(String... ruoli) {

        for(String s : ruoli){
            Label label = new Label(s);
            label.addStyleName("cruolo");
            addComponent(label);
        }

    }
}
