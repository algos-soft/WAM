package it.algos.wam.tabellone;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.dialog.ConfirmDialog;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Componente per presentare e modificare un Servizio nel Tabellone.
 * Created by alex on 18-03-2016.
 */
public class CServizioEditor extends CTabelloneEditor {

    private Servizio servizio;
    private VerticalLayout lFunc;

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

        lFunc = new VerticalLayout();
        lFunc.setCaption("Funzioni previste");
        lFunc.setSpacing(true);

        // aggiunge gli editor per le funzioni esistenti
        for(ServizioFunzione sf : servizio.getServizioFunzioni()){
            lFunc.addComponent(new EditorSF(sf));
        }
        layout.addComponent(lFunc);

        // aggiunge un bottone per creare nuove funzioni
        Button bNuova=new Button("Aggiungi funzione", FontAwesome.PLUS_CIRCLE);
        bNuova.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                lFunc.addComponent(new EditorSF(null));
            }
        });
        layout.addComponent(bNuova);


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
     * Editor di una singola funzione del servizio
     */
    private class EditorSF extends HorizontalLayout {

        private ServizioFunzione serFun;
        private CheckBox checkSel;
        private ERelatedComboField comboFunzioni;
        private CheckBox checkObbl;


        public EditorSF(ServizioFunzione serFun) {

            this.serFun=serFun;

            setSpacing(true);

            comboFunzioni = new ERelatedComboField(Funzione.class);
            comboFunzioni.setWidth("12em");
            if(serFun!=null){
                Funzione f = serFun.getFunzione();
                if(f!=null){
                    comboFunzioni.setValue(f.getId());
                }
            }


            checkObbl=new CheckBox("obbligatoria");
            // imposta il checkbox obbligatorio
            if(serFun!=null){
                checkObbl.setValue(this.serFun.isObbligatoria());
            }

            Button bElimina=new Button("Elimina", FontAwesome.TRASH_O);

            addComponent(comboFunzioni);
            addComponent(checkObbl);
            addComponent(bElimina);
            setComponentAlignment(comboFunzioni, Alignment.MIDDLE_LEFT);
            setComponentAlignment(checkObbl, Alignment.MIDDLE_LEFT);
            setComponentAlignment(bElimina, Alignment.MIDDLE_LEFT);



        }

        public boolean isObbligatoria() {
            return checkObbl.getValue();
        }

    }


}
