package it.algos.wam.tabellone;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.servizio.Servizio;

/**
 * Componente che rapresenta la cella con il titolo del servizio nel tabellone.
 * Created by alex on 20/02/16.
 */
public class CServizio extends VerticalLayout {


    /**
     * Costruttore completo
     *
     * @param servizio tipologia di servizio (obbligatoria)
     */
    public CServizio(Servizio servizio) {
        super();

        if (servizio == null) {
            return;
        }// end of if cycle

        setWidth("6em");
        setSpacing(false);
        setHeight("100%");
        addStyleName("cservizio");

        Label labelOra = new Label(servizio.getOrario());
        labelOra.addStyleName("cservizio-ora");
        labelOra.addStyleName("blueBg");
        addComponent(labelOra);

        Label labelNome = new Label(servizio.getDescrizione());
        labelNome.addStyleName("cservizio-nome");
        labelNome.addStyleName("greenBg");

        labelNome.setHeight("100%");
        addComponent(labelNome);

        setExpandRatio(labelNome, 1);
    }// end of constructor


    /**
     * @deprecated
     */
    public CServizio(String nome) {
        super();

        setWidth("6em");
        setSpacing(false);
        setHeight("100%");
        addStyleName("cservizio");


        Label labelOra = new Label("14-20");
        labelOra.addStyleName("cservizio-ora");
        labelOra.addStyleName("blueBg");
        addComponent(labelOra);

        Label labelNome = new Label(nome);
        labelNome.addStyleName("cservizio-nome");
        labelNome.addStyleName("greenBg");

        labelNome.setHeight("100%");
        addComponent(labelNome);

        setExpandRatio(labelNome, 1);

    }// end of constructor

}// end of class
