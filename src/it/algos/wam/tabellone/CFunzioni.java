package it.algos.wam.tabellone;


import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.wrap.WrapServizio;

import java.util.List;

/**
 * Componente grafico che rappresenta un elenco di funzioni
 * Created by alex on 21/02/16.
 */
public class CFunzioni extends VerticalLayout {

    /**
     * Costruttore completo
     */
    public CFunzioni() {
        addStyleName("greenBg");
        setSpacing(false);
        setWidth("3em");
    }

//    /**
//     * Costruttore completo
//     *
//     * @param listaFunzioni in forma di sigla
//     */
//    public CFunzioni(List<String> listaFunzioni) {
//        addStyleName("greenBg");
//        //addStyleName("ctabellone");
//
//        setSpacing(false);
//
//        //setHeight("100%");
//
//        setWidth("3em");
//
//        if (listaFunzioni == null) {
//            return;
//        }// end of if cycle
//
//        for (String sigla : listaFunzioni) {
//            Label label = new Label(sigla);
//            label.addStyleName("cruolo");
//            addComponent(label);
//        }// end of for cycle
//
//    }// end of constructor
//
//    /**
//     * Costruttore completo
//     *
//     * @param wrapServizio con le funzioni
//     */
//    public CFunzioni(WrapServizio wrapServizio) {
//        addStyleName("greenBg");
//        //addStyleName("ctabellone");
//
//        setSpacing(false);
//
//        //setHeight("100%");
//
//        setWidth("3em");
//
//        if (wrapServizio == null) {
//            return;
//        }// end of if cycle
//
//        for (WrapServizio.Wrap wrap : wrapServizio.getWrap()) {
//            Label label = new Label(wrap.funzione.getSigla());
//            if (wrap.obbligatoria) {
//                label.addStyleName("cruolor");
//            } else {
//                label.addStyleName("cruolo");
//            }// end of if/else cycle
//
//            addComponent(label);
//        }// end of for cycle
//
//    }// end of constructor
//
//    /**
//     * @deprecated
//     */
//    public CFunzioni(String... funzioni) {
//
//        addStyleName("greenBg");
//        //addStyleName("ctabellone");
//
//        setSpacing(false);
//
//        //setHeight("100%");
//
//        setWidth("3em");
//
//        for (String s : ruoli) {
//            Label label = new Label(s);
//            label.addStyleName("cruolo");
//            addComponent(label);
//        }
//
//    }
//

    /**
     * Aggiunge un componente grafico rappresentante una funzione
     *
     * @param nome nome della funzione
     * @return il componente grafico aggiuntp
     */
    public Component addFunzione(String nome) {
        Label label = new Label(nome);
        label.addStyleName("cruolo");
        addComponent(label);
        return label;
    }


}// end of class
