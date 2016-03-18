package it.algos.wam.tabellone;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.dialog.ConfirmDialog;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

/**
 * Componente per presentare e modificare un Servizio nel Tabellone.
 * Created by alex on 18-03-2016.
 */
public class CServizioEditor extends CTabelloneEditor {

    private Servizio servizio;

    public CServizioEditor(Servizio servizio, EntityManager entityManager) {
        super(entityManager);
        this.servizio = servizio;

        setSpacing(true);

        addComponent(creaCompTitolo());
        addComponent(creaCompDetail());
        addComponent(creaPanComandi());


    }


    /**
     * Crea il componente che visualizza il titolo
     *
     * @return il componente titolo
     */
    private Component creaCompTitolo() {
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label(servizio.getOrario()));
        layout.addComponent(new Label("<strong>" + servizio.getDescrizione() + "</strong>", ContentMode.HTML));

        return layout;
    }

    /**
     * Crea il componente che visualizza il dettaglio
     *
     * @return il componente dettagli
     */
    private Component creaCompDetail() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        TextField fDescrizione = new TextField("Descrizione");
        fDescrizione.setWidth("20em");
        layout.addComponent(fDescrizione);

        List<Funzione> lista=(List<Funzione>)CompanyQuery.getList(Funzione.class);
        Collections.sort(lista);

        VerticalLayout lFunc = new VerticalLayout();
        lFunc.setCaption("Funzioni previste");

        for(Funzione f : lista){
            lFunc.addComponent(new EditorFunzione(f));
        }

        layout.addComponent(lFunc);

        return layout;
    }


    /**
     * Crea e ritorna il pannello comandi.
     *
     * @return il pannello comandi
     */
    private Component creaPanComandi() {
        HorizontalLayout layout = new HorizontalLayout();
//        layout.setMargin(true);
        layout.setSpacing(true);

        Button bElimina = new Button("Elimina servizio");
        bElimina.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ConfirmDialog dialog = new ConfirmDialog("Elimina servizio", "Sei sicuro?", new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog, boolean confirmed) {
                        if (confirmed) {
                            servizio.delete(entityManager);
                            fireDismissListeners(new DismissEvent(bElimina, false, true));
                        }
                    }
                });
                dialog.show();
            }
        });

        Button bAnnulla = new Button("Annulla");
        bAnnulla.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                fireDismissListeners(new DismissEvent(bAnnulla, false, false));
            }
        });

        Button bRegistra = new Button("Registra");
        bRegistra.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        bRegistra.addStyleName(ValoTheme.BUTTON_PRIMARY);    // "primary" not "default"
        bRegistra.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                servizio.save(entityManager);
                fireDismissListeners(new DismissEvent(bRegistra, true, false));
            }
        });

        // controllo se il servizio è persisted
        if (servizio.getId() != null) {
            layout.addComponent(bElimina);
        }
        layout.addComponent(bAnnulla);
        layout.addComponent(bRegistra);

        return layout;
    }


    /**
     * Editor di una singola funzione
     */
    private class EditorFunzione extends HorizontalLayout {

        private Funzione funzione;
        private CheckBox checkSel;
        private CheckBox checkObbl;


        public EditorFunzione(Funzione funzione) {

            this.funzione=funzione;

            checkSel=new CheckBox(funzione.getDescrizione());
            checkSel.setWidth("10em");
            checkSel.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    checkObbl.setEnabled(checkSel.getValue());
                    if (!checkSel.getValue()) {
                        checkObbl.setValue(false);
                    }
                }
            });

            checkObbl=new CheckBox("obbligatoria");
            checkSel.setWidth("10em");

            checkSel.setValue(!checkSel.getValue());    // inverte il valore per forzare un value change
            checkSel.setValue(false);   // all'inizio spegne il check

            addComponent(checkSel);
            addComponent(checkObbl);
        }

        public Funzione getFunzione() {
            return funzione;
        }

        public boolean isSelezionata() {
            return checkSel.getValue();
        }

        public boolean isObbligatoria() {
            return checkObbl.getValue();
        }

    }


}
