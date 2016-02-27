package it.algos.wam.tabellone;


import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Componente grafico che rappresenta un elenco di ruoli
 * Created by alex on 21/02/16.
 */
public class CRuoli extends VerticalLayout {

    public CRuoli(String... ruoli) {

        setSpacing(false);

        //setHeight("100%");

        setWidth("4em");

        for(String s : ruoli){
            Label label = new Label(s);
            label.setStyleName("cruolo");
            //label.addStyleName("yellowBg");
            addComponent(label);
        }

    }
}
