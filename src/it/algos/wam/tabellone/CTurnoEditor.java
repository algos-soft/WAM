package it.algos.wam.tabellone;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.milite.Milite;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.wrap.Iscrizione;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.lib.DateConvertUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;

/**
 * Componente grafico per presentare e modificare un turno.
 * Created by alex on 05/03/16.
 */
public class CTurnoEditor extends VerticalLayout implements View {

    private Turno turno;
    private ArrayList<DismissListener> dismissListeners=new ArrayList();
    private HashMap<String, RelatedComboField> mappaCombo=new HashMap<>();

    public CTurnoEditor(Turno turno) {
        this.turno=turno;

        // layout interno
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthUndefined();
        layout.addComponent(creaCompTitolo());
        layout.addComponent(creaCompIscrizioni());
        layout.addComponent(creaPanComandi());
        layout.addStyleName("yellowBg");

        // layout esterno (questo)
        addComponent(layout);
        setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
        setWidth("100%");
        setHeight("100%");
        addStyleName("greenBg");

        // seleziona i valori nei combo in base alle iscrizioni esistenti
        for(Iscrizione i : turno.getIscrizioni()){
            Funzione f = i.getFunzione();
            RelatedComboField combo = mappaCombo.get(f.getSigla());
            Milite m = i.getMilite();
            //combo.setValue(m);
        }

    }



    /**
     * Crea il componente che visualizza il titolo del turno
     * (descrizione, data, ora ecc..)
     * @return il componente titolo
     */
    private Component creaCompTitolo(){
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Servizio serv = turno.getServizio();
        LocalDate dataInizio = DateConvertUtils.asLocalDate(turno.getInizio());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d LLLL YYYY");
        String sData = dataInizio.format(formatter);
        String sOra = turno.getServizio().getOrario();
        String dataOra =sData+", ore "+sOra;

        layout.addComponent(new Label(dataOra));
        layout.addComponent(new Label("<strong>"+serv.getDescrizione()+"</strong>", ContentMode.HTML));

        return layout;
    }

    /**
     * Crea il componente che ospita la griglia delle iscrizioni
     * (un elemento per ogni funzione)
     * @return il componente iscrizioni
     */
    private Component creaCompIscrizioni(){
        int numFunzioni=turno.getServizio().getNumFunzioni();
        ArrayList<Funzione> funzioni = turno.getServizio().getFunzioni();
        GridLayout gridlayout = new GridLayout(2,numFunzioni);
        gridlayout.setSpacing(true);

        for(int i=0; i<numFunzioni; i++){

            String fn = funzioni.get(i).getSigla();
            Label lblFunzione = new Label(fn);
            gridlayout.addComponent(lblFunzione, 0, i);
            gridlayout.setComponentAlignment(lblFunzione, Alignment.MIDDLE_LEFT);

            RelatedComboField combo = new RelatedComboField(Milite.class);
            gridlayout.addComponent(combo, 1, i);

            mappaCombo.put(fn, combo);

        }

        return gridlayout;
    }


    /**
     * Crea e ritorna il pannello comandi.
     * @return il pannello comandi
     */
    private Component creaPanComandi() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        Button bAnnulla = new Button("Annulla");
        bAnnulla.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                fireDismissListeners(new DismissEvent(bAnnulla, false));
            }
        });

        Button bRegistra = new Button("Registra");
        bRegistra.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                turno.save();
                fireDismissListeners(new DismissEvent(bRegistra, false));
            }
        });

        layout.addComponent(bAnnulla);
        layout.addComponent(bRegistra);


        return layout;
    }


    private void fireDismissListeners(DismissEvent e){
        for(DismissListener l : dismissListeners){
            l.editorDismissed(e);
        }
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public void addDismissListener(DismissListener l){
        dismissListeners.add(l);
    }

    /**
     * Listener per editor dismissed
     */
    public interface DismissListener {
        public void editorDismissed(DismissEvent e);
    }

    /**
     * Evento editor dismissed
     */
    public class DismissEvent extends EventObject{
        private boolean saved;

        public DismissEvent(Object source, boolean saved) {
            super(source);
            this.saved = saved;
        }

        public boolean isSaved() {
            return saved;
        }
    }
}
