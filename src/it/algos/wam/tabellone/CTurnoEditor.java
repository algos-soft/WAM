package it.algos.wam.tabellone;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.milite.Milite;
import it.algos.wam.entity.turno.Turno;
import it.algos.webbase.web.field.RelatedComboField;

/**
 * Componente grafico per presentare e modificare un turno.
 * Created by alex on 05/03/16.
 */
public class CTurnoEditor extends VerticalLayout implements View {

    Turno turno;

    public CTurnoEditor(Turno turno) {
        this.turno=turno;
        addComponent(creaCompTitolo());
        addComponent(creaCompIscrizioni());
    }

    /**
     * Crea il componente che visualizza il titolo del turno
     * (descrizione, data, ora ecc..)
     * @return il componente titolo
     */
    private Component creaCompTitolo(){
        Component comp = new Label(turno.getServizio().getDescrizione());
        return comp;
    }

    /**
     * Crea il componente che ospita la griglia delle iscrizioni
     * (un elemento per ogni funzione)
     * @return il componente iscrizioni
     */
    private Component creaCompIscrizioni(){
        int numFunzioni=turno.getServizio().getNumFunzioni();
        GridLayout comp = new GridLayout(1,numFunzioni);
        for(int i=0; i<numFunzioni; i++){
            RelatedComboField combo = new RelatedComboField(Milite.class, "Milite");
            comp.addComponent(combo, 0, i);
        }
        return comp;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
