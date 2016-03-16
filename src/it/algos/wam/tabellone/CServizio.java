package it.algos.wam.tabellone;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.servizio.Servizio;

/**
 * Componente grafico che rappresenta la cella con il titolo del servizio nel tabellone.
 * Created by alex on 20/02/16.
 */
public class CServizio extends VerticalLayout {





    /**
     * Costruttore completo
     *
     * @param orario orario del servizio (stringa)
     * @param descrizione descrizione del servizio
     */
    public CServizio(String orario, String descrizione) {
        super();

        setWidth("7em");
        setSpacing(false);
        //setHeight("100%");
        addStyleName("cservizio");

        String style=null;
        if(orario.equals("")) {
            orario = "&nbsp;";
        }else {
            style="cservizio-ora";
        }// evita label con testo vuoto, danno problemi

        Label labelOra = new Label(orario, ContentMode.HTML);
        if(style!=null){
            labelOra.addStyleName(style);
        }

        //labelOra.addStyleName("blueBg");
        addComponent(labelOra);

        if(descrizione.equals("")){  descrizione="&nbsp;";}// evita label con testo vuoto, danno problemi
        Label labelNome = new Label(descrizione,ContentMode.HTML);
        labelNome.addStyleName("cservizio-nome");
        //labelNome.addStyleName("greenBg");

        labelNome.setHeight("100%");
        addComponent(labelNome);

        setExpandRatio(labelNome, 1);

    }

}
