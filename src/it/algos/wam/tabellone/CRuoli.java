package it.algos.wam.tabellone;


import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.wrap.WrapServizio;

import java.util.List;

/**
 * Componente grafico che rappresenta un elenco di ruoli
 * Created by alex on 21/02/16.
 */
public class CRuoli extends VerticalLayout {


    /**
     * Costruttore completo
     *
     * @param listaFunzioni in forma di sigla
     */
    public CRuoli(List<String> listaFunzioni) {
        addStyleName("greenBg");
        //addStyleName("ctabellone");

        setSpacing(false);

        //setHeight("100%");

        setWidth("3em");

        if (listaFunzioni == null) {
            return;
        }// end of if cycle

        for (String sigla : listaFunzioni) {
            Label label = new Label(sigla);
            label.addStyleName("cruolo");
            addComponent(label);
        }// end of for cycle

    }// end of constructor

    /**
     * Costruttore completo
     *
     * @param wrapServizio con le funzioni
     */
    public CRuoli(WrapServizio wrapServizio) {
        addStyleName("greenBg");
        //addStyleName("ctabellone");

        setSpacing(false);

        //setHeight("100%");

        setWidth("3em");

        if (wrapServizio == null) {
            return;
        }// end of if cycle

        for (WrapServizio.Wrap wrap : wrapServizio.getWrap()) {
            Label label = new Label(wrap.funzione.getSigla());
            if (wrap.obbligatoria) {
                label.addStyleName("cruolor");
            } else {
                label.addStyleName("cruolo");
            }// end of if/else cycle

            addComponent(label);
        }// end of for cycle

    }// end of constructor

    /**
     * @deprecated
     */
    public CRuoli(String... ruoli) {

        addStyleName("greenBg");
        //addStyleName("ctabellone");

        setSpacing(false);

        //setHeight("100%");

        setWidth("3em");

        for (String s : ruoli) {
            Label label = new Label(s);
            label.addStyleName("cruolo");
            addComponent(label);
        }

    }
}// end of class
