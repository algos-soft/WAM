package it.algos.wam.tabellone;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Componente che rapresenta la cella con il titolo del servizio nel tabellone.
 * Created by alex on 20/02/16.
 */
public class CServizio extends VerticalLayout {
    public CServizio(String nome) {
        super();
        setWidth("6em");
        //setWidth("300px");

        setHeight("100%");
        //setWidth("11em");
//        setWidthUndefined();

        addStyleName("cservizio");


        Label labelOra=new Label("<strong>14-20</strong>", ContentMode.HTML);
//        labelOra.setWidth("30%");
//        labelOra.setWidth("3em");

        Label labelNome = new Label(nome, ContentMode.HTML);
//        labelNome.setWidth("70%");

//        labelNome.setWidthUndefined();

//        addComponent(labelOra);
//        setComponentAlignment(labelOra, Alignment.MIDDLE_CENTER);
        addComponent(labelNome);
        setComponentAlignment(labelNome, Alignment.MIDDLE_LEFT);

    }
}
