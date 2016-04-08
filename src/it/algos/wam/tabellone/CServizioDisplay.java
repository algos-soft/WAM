package it.algos.wam.tabellone;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
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
    private CompServizio compServizio;


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

        compServizio=new CompServizio();
        addComponent(compServizio);
        addComponent(new CompFunzioni());

    }

    /**
     * Mostra o nasconde il bottone che consente di creare una nuova riga
     * (significativo solo per servizi a orario variabile)
     * @param crea true per mostrare il bottone, false per nasconderlo
     */
    public void setCreaNuova(boolean crea){
        compServizio.setCreaNuova(crea);
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

        private Label labelTitolo;
        private HorizontalLayout layoutTitle;

        private LayoutEvents.LayoutClickListener listener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                tabellone.cellClicked(CellType.SERVIZIO, x, y, servizio);
                setCreaNuova(false);    // una volta che ho creato la nuova riga, questa riga perde la possibilità di crearne altre
            }
        };

        public CompServizio() {
            setWidth("7em");
            setSpacing(false);

            layoutTitle=new HorizontalLayout();// layout con dentro la label, serve per poterci attaccare il clicklistener
            layoutTitle.setWidth("100%");
            labelTitolo = new Label();
            labelTitolo.setContentMode(ContentMode.HTML);
            if(servizio.isOrario()) {
                labelTitolo.setValue(servizio.getStrOrario());
                labelTitolo.addStyleName("cservizio-ora");
            }else{
                labelTitolo.addStyleName("cservizio-variabile");
                setCreaNuova(true);
            }

            layoutTitle.addComponent(labelTitolo);
            addComponent(layoutTitle);



            String descrizione=servizio.getDescrizione();
            if (descrizione.equals("")) {
                descrizione = "&nbsp;";// evita label con testo vuoto, danno problemi
            }
            Label labelNome = new Label(descrizione, ContentMode.HTML);
            labelNome.addStyleName("cservizio-nome");

            labelNome.setHeight("100%");
            addComponent(labelNome);

            setExpandRatio(labelNome, 1);


        }


        /**
         * Mostra o nasconde il bottone che consente di creare una nuova riga
         * (significativo solo per servizi a orario variabile)
         * @param crea true per mostrare il bottone, false per nasconderlo
         */
        public void setCreaNuova(boolean crea){
            if(crea){
                labelTitolo.setValue(FontAwesome.PLUS_CIRCLE.getHtml() + " crea nuovo");
                layoutTitle.addLayoutClickListener(listener);
                layoutTitle.addStyleName("cpointer");
            }else{
                labelTitolo.setValue("&nbsp;");
                layoutTitle.removeLayoutClickListener(listener);
                layoutTitle.removeStyleName("cpointer");
            }
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
            if (!servizio.isOrario()) {
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
