package it.algos.wam.tabellone;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.multiazienda.ERelatedComboField;
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
    private VerticalLayout layoutFunc;

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

        layoutFunc = new VerticalLayout();
        layoutFunc.setCaption("Funzioni previste");
        layoutFunc.setSpacing(true);

        // aggiunge gli editor per le funzioni esistenti
        List<ServizioFunzione> listaSF = servizio.getServizioFunzioni();
        Collections.sort(listaSF);
        for (ServizioFunzione sf : listaSF) {
            layoutFunc.addComponent(new EditorSF(sf));
        }
        layout.addComponent(layoutFunc);

        // aggiunge un bottone per creare nuove funzioni
        Button bNuova = new Button("Aggiungi funzione", FontAwesome.PLUS_CIRCLE);
        bNuova.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
//                ServizioFunzione sf = new ServizioFunzione();
//                sf.setServizio(servizio);
//                servizio.getServizioFunzioni().add(sf);
                layoutFunc.addComponent(new EditorSF(null));
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

                // controlla se questo servizio è eliminabile
                String err = checkServizioRegistrabile();
                if(err.isEmpty()){
                    servizio.save(entityManager);
                    fireDismissListeners(new DismissEvent(bRegistra, true, false));
                }else{
                    Notification.show(null, err, Notification.Type.WARNING_MESSAGE);
                }

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
     * Controlla se questo servizio è registrabile
     * @return stringa vuota se registrabile, motivo se non registrabile
     */
    private String checkServizioRegistrabile() {
        String err="";

        // deve avere delle funzioni
        if(servizio.getServizioFunzioni().size()==0){
            if(!err.isEmpty()){err+="\n";}
            err+="Non ci sono funzioni";
        }

        // le funzioni devono essere tutte specificate
        for(ServizioFunzione sf : servizio.getServizioFunzioni()){
            if (sf.getFunzione()==null){
                if(!err.isEmpty()){err+="\n";}
                err+="Ci sono funzioni non specificate";
                break;
            }
        }

        return err;
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

            this.serFun = serFun;

            setSpacing(true);

            comboFunzioni = new ERelatedComboField(Funzione.class);
            comboFunzioni.setWidth("12em");
            if (serFun != null) {
                Funzione f = serFun.getFunzione();
                if (f != null) {
                    comboFunzioni.setValue(f.getId());
                }
            }


            checkObbl = new CheckBox("obbligatoria");
            // imposta il checkbox obbligatorio
            if (serFun != null) {
                checkObbl.setValue(this.serFun.isObbligatoria());
            }

            Button bElimina = new Button("Elimina", FontAwesome.TRASH_O);
            bElimina.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {

                    if (serFun != null) {
                        List<Iscrizione> iscrizioni = WamQuery.queryIscrizioniServizioFunzione(entityManager, serFun);

                        if (iscrizioni.size() == 0) {

                            String messaggio = "Vuoi eliminare la funzione " + serFun.getFunzione().getDescrizione() + "?";
                            new ConfirmDialog(null, messaggio, new ConfirmDialog.Listener() {
                                @Override
                                public void onClose(ConfirmDialog dialog, boolean confirmed) {
                                    if (confirmed) {
                                        doDelete();// elimino componente e relativo ServizioFunzione
                                    }
                                }
                            }).show();

                        } else {
                            Notification.show(null, "Questa funzione ha già delle iscrizioni, non si può cancellare", Notification.Type.WARNING_MESSAGE);
                        }

                    } else {  // ServizioFunzione null, procedo alla eliminazione del componente
                        doDelete();
                    }

                }
            });


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

        /**
         * Eliminazione effettiva di questo componente e del relativo ServizioFunzione
         */
        private void doDelete() {
            layoutFunc.removeComponent(EditorSF.this);
        }

    }


}
