package it.algos.wam.tabellone;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;

import java.util.Collections;
import java.util.List;

/**
 * Componente grafico che rappresenta la cella con il titolo del servizio
 * e l'elenco delle funzioni previste.
 * Created by alex on 20/02/16.
 */
public class CServizioDisplay extends HorizontalLayout implements TabelloneCell {

    private GridTabellone tabellone;
    private Servizio servizio;
    private int x;
    private int y;


    /**
     * Costruttore completo
     *
     * @param tabellone   la grigia del tabellone
     * @param servizio    il servizio
     */
    public CServizioDisplay(GridTabellone tabellone, Servizio servizio) {
        super();

        this.tabellone = tabellone;
        this.servizio = servizio;

        setSpacing(true);
        addStyleName("cservizio");

        addComponent(new CompServizio());
        addComponent(new CompFunzioni());

    }



    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }


    /**
     * Componente con la descrizione del servizio
     */
    private class CompServizio extends VerticalLayout {
        public CompServizio() {
            setWidth("7em");
            setSpacing(false);
            //setHeight("100%");

            String orario=servizio.getOrario();
            String style = null;
            if (orario.equals("")) {
                orario = "&nbsp;";
            } else {
                style = "cservizio-ora";
            }// evita label con testo vuoto, danno problemi

            Label labelOra = new Label(orario, ContentMode.HTML);
            if (style != null) {
                labelOra.addStyleName(style);
            }

            //labelOra.addStyleName("blueBg");
            addComponent(labelOra);


            String descrizione=servizio.getDescrizione();
            if (descrizione.equals("")) {
                descrizione = "&nbsp;";
            }// evita label con testo vuoto, danno problemi
            Label labelNome = new Label(descrizione, ContentMode.HTML);
            labelNome.addStyleName("cservizio-nome");
            //labelNome.addStyleName("greenBg");

            labelNome.setHeight("100%");
            addComponent(labelNome);

            setExpandRatio(labelNome, 1);


            // listener quando viene cliccato il servizio
            addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    tabellone.cellClicked(CellType.SERVIZIO, x, y, servizio);
                }
            });
        }
    }

    /**
     * Componente con l'elenco ordinato delle funzioni
     */
    private class CompFunzioni extends VerticalLayout{

        /**
         * Costruttore completo
         */
        public CompFunzioni() {
            //addStyleName("greenBg");
            setSpacing(false);
            setWidth("3em");


            Funzione funz;
            boolean obbligatoria;


            // se orario variabile, prima riga vuota per allinearsi con i turni che in questo caso avranno un titolo
            if (servizio.isOrarioVariabile()) {
                Label label = new Label("&nbsp;", ContentMode.HTML);
                addComponent(label);
            }

            List<ServizioFunzione> lista = servizio.getServizioFunzioni();
            Collections.sort(lista);
            for (ServizioFunzione serFun : lista) {
                funz = serFun.getFunzione();
                obbligatoria = serFun.isObbligatoria();

                Component comp = addFunzione(funz.getSigla());
                if (obbligatoria) {
                    comp.addStyleName("cfunzioneobblig");
                }
            }

        }

        /**
         * Aggiunge un componente grafico rappresentante una funzione
         *
         * @param nome nome della funzione
         * @return il componente grafico aggiuntp
         */
        private Component addFunzione(String nome) {
            Label label = new Label(nome, ContentMode.HTML);
            label.setHeight(GridTabellone.H_ISCRIZIONI);
            label.addStyleName("cfunzione");
            addComponent(label);
            return label;
        }

    }

}
