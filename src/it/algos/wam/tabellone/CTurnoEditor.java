package it.algos.wam.tabellone;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.component.HHMMComponent;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.lib.DateConvertUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente per presentare e modificare un turno nel Tabellone.
 * Ha due modalità di funzionamento: iscrizione singola o multi-iscrizione (vedi isMultiIscrizione())
 * In modalità iscrizione singola, un volontario può iscrivere solo se stesso
 * e l'iscrizione viene fatta tramite bottoni.
 * In modalità multi-iscrizione, un volontario può iscrivere anche gli altri e
 * l'iscrizione viene fatta selezionando i nomi dai dei popup.
 * <p>
 * Created by alex on 05/03/16.
 */
public class CTurnoEditor extends CTabelloneEditor {

    private Turno turno;
    private IscrizioneGroupEditor iscrizioneGroupEditor;
    private TextField fieldNote;

    public CTurnoEditor(Turno turno, EntityManager entityManager) {
        super(entityManager);
        this.turno = turno;

        addComponent(creaCompTitolo());

        Component comp = creaCompDettaglio();
        if (comp != null) {
            addComponent(comp);
        }

        iscrizioneGroupEditor = new IscrizioneGroupEditor();
        addComponent(iscrizioneGroupEditor);

        Component panComandi = creaPanComandi();
        addComponent(panComandi);
        setComponentAlignment(panComandi, Alignment.BOTTOM_CENTER);

    }


    /**
     * Crea il componente che visualizza il titolo del turno
     * (descrizione, data, ora ecc..)
     *
     * @return il componente titolo
     */
    private Component creaCompTitolo() {
        VerticalLayout layout = new VerticalLayout();

        Servizio serv = turno.getServizio();
        LocalDate dataInizio = DateConvertUtils.asLocalDate(turno.getInizio());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d LLLL YYYY");
        String dataOra = dataInizio.format(formatter);
        if (serv.isOrario()) {
            dataOra += ", ore " + serv.getStrOrario();
        }

        layout.addComponent(new Label(dataOra));
        layout.addComponent(new Label("<strong>" + serv.getDescrizione() + "</strong>", ContentMode.HTML));

        return layout;
    }


    /**
     * Crea il componente che visualizza i dettagli del turno
     * (nome ecc..)
     *
     * @return il componente di dettaglio
     */
    private Component creaCompDettaglio() {
        Servizio serv = turno.getServizio();
        if (!serv.isOrario()) {
            fieldNote = new TextField("note");
            fieldNote.setWidth("100%");
            fieldNote.setValue(turno.getNote());
        }
        return fieldNote;
    }


    /**
     * Crea e ritorna il pannello comandi.
     *
     * @return il pannello comandi
     */
    private Component creaPanComandi() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        Button bElimina = new Button("Elimina turno");
        bElimina.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ConfirmDialog dialog = new ConfirmDialog("Elimina turno", "Sei sicuro?", new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog, boolean confirmed) {
                        if (confirmed) {
                            deleteTurno();
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
                if (saveTurnoMulti()) {
                    fireDismissListeners(new DismissEvent(bRegistra, true, false));
                }
            }
        });

        // controllo se il turno è persisted

        if (turno.getId() != null) {
            layout.addComponent(bElimina);
        }

        layout.addComponent(bAnnulla);

        /**
         * Il bottone Registra
         * Usato solo in modalità multi-iscrizione
         */
        if (isMultiIscrizione()) {
            layout.addComponent(bRegistra);
        }


        return layout;
    }


    /**
     * Registra il turno corrente.
     * Visualizza una notifica se non riuscito
     *
     * @return true se riuscito
     */
    private boolean saveTurnoMulti() {
        boolean success = false;
        entityManager.getTransaction().begin();

        try {

            // Registra le note
            turno.setNote(getNote());

            //cancella le iscrizioni correnti dal db
            for (Iscrizione i : turno.getIscrizioni()) {
                entityManager.remove(i);
            }

            // pulisce la lista iscrizioni del turno
            turno.getIscrizioni().clear();

            // recupera le nuove iscrizioni e le aggiunge
            ArrayList<Iscrizione> iscrizioni = iscrizioneGroupEditor.getIscrizioni();
            for (Iscrizione i : iscrizioni) {
                turno.add(i);
            }

            // registra il turno
            entityManager.persist(turno);
            entityManager.getTransaction().commit();
            success = true;

        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }

        return success;
    }


    /**
     * Elimina il turno corrente.
     * Visualizza una notifica se non riuscito
     *
     * @return true se riuscito
     */
    private boolean deleteTurno() {
        turno.delete(entityManager);
        return true;
    }


    /**
     * Ritorna il testo correntemente presente nel campo note
     */
    private String getNote() {
        String note = "";
        if (fieldNote != null) {
            note = fieldNote.getValue();
        }
        return note;
    }


    /**
     * Editor delle iscrizioni
     */
    private class IscrizioneGroupEditor extends VerticalLayout {

        private ArrayList<IscrizioneEditor> iEditors = new ArrayList<>();

        public IscrizioneGroupEditor() {

            setSpacing(true);

            // crea gli editor di iscrizione e li aggiunge
            for (ServizioFunzione sf : turno.getServizio().getServizioFunzioni()) {

                Iscrizione i = turno.getIscrizione(sf);

                // se l'iscrizione non esiste la crea ora
                if (i == null) {
                    i = new Iscrizione(turno, null, sf);
                }

                IscrizioneEditor ie = new IscrizioneEditor(i, this);
                iEditors.add(ie);
                addComponent(ie);

            }
        }

        /**
         * Controlla se un dato volontario è iscritto
         *
         * @param v       il volontario da controllare
         * @param exclude eventuale editor da escludere
         */
        public boolean isIscritto(Volontario v, IscrizioneEditor exclude) {
            boolean iscritto = false;
            for (IscrizioneEditor ie : iEditors) {

                boolean skip = false;
                if (exclude != null) {
                    if (ie.equals(exclude)) {
                        skip = true;
                    }
                }

                if (!skip) {
                    Volontario iev = ie.getVolontario();
                    if (iev != null) {
                        if (iev.equals(v)) {
                            iscritto = true;
                            break;
                        }
                    }
                }

            }
            return iscritto;
        }

        /**
         * Ritorna l'elenco delle iscrizioni correntemente presenti
         * (tutte quelle che hanno un volontario)
         *
         * @return l'elenco delle iscrizioni
         */
        public ArrayList<Iscrizione> getIscrizioni() {
            ArrayList<Iscrizione> iscrizioni = new ArrayList();
            for (IscrizioneEditor ie : iEditors) {
                Iscrizione i = ie.getIscrizione();
                if (i != null) {
                    iscrizioni.add(i);
                }
            }
            return iscrizioni;
        }

    }


    /**
     * Editor di una singola Iscrizione
     */
    private class IscrizioneEditor extends CustomComponent {

        private IscrizioneGroupEditor parent;
        private SelettoreUtenti selUtenti;
        private TextField fNote;
        private HHMMComponent cTime;
        private Volontario currentSelectedVolontario;
        private Iscrizione iscrizione;

        /**
         * @param iscrizione l'iscrizione da editare
         * @param parent     l'editor parente per il controllo che il
         *                   volontario non sia già iscritto in altra posizione
         */
        public IscrizioneEditor(Iscrizione iscrizione, IscrizioneGroupEditor parent) {
            this.iscrizione = iscrizione;
            this.parent = parent;
            setSpacing(true);

            // crea il componente note
            fNote = new TextField("note");
            fNote.setValue(iscrizione.getNota());
            fNote.setWidth("10em");

            // crea il componente ore
            cTime = new HHMMComponent("tempo hh:mm");
            cTime.setHoursMinutes(iscrizione.getMinutiEffettivi());

            // crea il componente editor di tipo diverso a seconda
            // della modalità operativa multi-iscrizione
            Component comp;
            if (isMultiIscrizione()) {
                comp = creaCompPopup();
            } else {
                if (isAdmin()) {
                    comp = creaCompPopup();
                } else {
                    comp = creaCompBottoni();
                }
            }

            setCompositionRoot(comp);

        }

        /**
         * Crea un componente popup che permette di selezionare e iscrivere un volontario
         */
        private Component creaCompPopup() {

            selUtenti = new SelettoreUtenti();
            selUtenti.setWidth("10em");
            if (iscrizione.getVolontario() != null) {
                currentSelectedVolontario = iscrizione.getVolontario();
                selUtenti.setValue(currentSelectedVolontario);
            }
            selUtenti.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {

                    // controllo che non sia già iscritto
                    Volontario sel = selUtenti.getVolontario();
                    if (sel != null) {
                        boolean giaIscritto = IscrizioneEditor.this.parent.isIscritto(sel, IscrizioneEditor.this);
                        if (giaIscritto) {
                            Notification.show(sel + " è già iscritto a questo turno.", Notification.Type.ERROR_MESSAGE);
                            if (currentSelectedVolontario == null) {
                                selUtenti.select(null);
                            } else {
                                selUtenti.select(currentSelectedVolontario);
                            }
                        } else { //nuova iscrizione
                            int minutiTurno = turno.getMinutiTotali();
                            iscrizione.setMinutiEffettivi(minutiTurno);
                            cTime.setHoursMinutes(minutiTurno);
                            fNote.setValue("");
                        }
                    }

                    syncFields();

                    currentSelectedVolontario = selUtenti.getVolontario();
                }
            });

            VerticalLayout volLayout = new VerticalLayout();
            Label volLabel = new Label(creaTestoComponente(), ContentMode.HTML);
            volLayout.addComponent(volLabel);
            volLayout.addComponent(selUtenti);

            syncFields();

            HorizontalLayout layout = new HorizontalLayout();
            layout.setSpacing(true);
            layout.addComponent(volLayout);
            layout.addComponent(fNote);
            layout.addComponent(cTime);

            return layout;
        }

        /**
         * Crea un componente con bottoni che mostra il volontario iscritto o
         * iscrive/disiscrive il volontario correntemente loggato.
         * Solo gli utenti normali hanno questo tipo di componente, mentre gli admin
         * hanno sempre il componente popup - qui non serve mai controllare isAdmin()
         */
        private Component creaCompBottoni() {

            // bottone iscrizione
            Button bMain = new Button();
            bMain.setWidth("100%");
            bMain.setHtmlContentAllowed(true);
            Volontario volIscritto = iscrizione.getVolontario();
            Funzione funz = iscrizione.getServizioFunzione().getFunzione();
            String caption;
            if (volIscritto != null) {  // già iscritto
                caption = volIscritto.toString();
                FontAwesome glyph = funz.getIcon();
                if (glyph != null) {
                    caption = glyph.getHtml() + " " + caption;
                }
            } else {                    // non iscritto
                caption = "";
                FontAwesome glyph = funz.getIcon();
                if (glyph != null) {
                    caption = glyph.getHtml() + " " + caption;
                }
                caption += " Iscriviti come <strong>" + funz.getDescrizione() + "</strong>";
                if (!getLoggedUser().haFunzione(funz)) {
                    bMain.addStyleName("lightGrayBg");
                }
            }
            bMain.setCaption(caption);
            // click listener solo se non c'è nessuno iscritto
            bMain.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    boolean cont = true;

                    // controllo che non ci sia già un iscritto
                    if (cont) {
                        if (volIscritto != null) {
                            cont = false;
                        }
                    }

                    // controllo che l'utente corrente abbia la funzione richiesta
                    if (cont) {
                        if (!getLoggedUser().haFunzione(funz)) {
                            Notification notif = new Notification("Nel tuo profilo non c'è la funzione " + funz.getDescrizione() + "<br>" + "Rivolgiti all'amministratore", Notification.Type.WARNING_MESSAGE);
                            notif.setHtmlContentAllowed(true);
                            notif.show(Page.getCurrent());
                            cont = false;
                        }
                    }

                    // controllo che non sia già iscritto in qualche altra posizione di questo turno
                    if (cont) {
                        boolean giaIscritto = IscrizioneEditor.this.parent.isIscritto(getLoggedUser(), IscrizioneEditor.this);
                        if (giaIscritto) {
                            Notification.show("Sei già iscritto a questo turno.", Notification.Type.ERROR_MESSAGE);
                            cont = false;
                        }
                    }

                    // se tutto ok procedo alla iscrizione
                    if (cont) {
                        entityManager.getTransaction().begin();
                        turno.getIscrizioni().add(iscrizione);
                        iscrizione.setVolontario(getLoggedUser());
                        iscrizione.setNota(fNote.getValue());
                        iscrizione.setMinutiEffettivi(cTime.getTotalMinutes());
                        entityManager.merge(turno);
                        entityManager.getTransaction().commit();
                        fireDismissListeners(new DismissEvent(bMain, true, false));
                    }

                }
            });


            // bottone remove
            Button bRemove = new Button();
            bRemove.setIcon(FontAwesome.REMOVE);
            bRemove.addStyleName("icon-red");
            bRemove.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    entityManager.getTransaction().begin();
                    turno.getIscrizioni().remove(iscrizione);
                    entityManager.merge(turno);
                    entityManager.getTransaction().commit();
                    fireDismissListeners(new DismissEvent(bRemove, true, false));
                }
            });

            // bottone registra
            Button bSave = new Button();
            bSave.setIcon(FontAwesome.CHECK);
            bSave.addStyleName("icon-green");
            bSave.setVisible(volIscritto != null);
            bSave.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    entityManager.getTransaction().begin();
                    iscrizione.setNota(fNote.getValue());
                    iscrizione.setMinutiEffettivi(cTime.getTotalMinutes());
                    entityManager.merge(iscrizione);
                    entityManager.getTransaction().commit();
                    fireDismissListeners(new DismissEvent(bRemove, true, false));
                }
            });

            // disponibilità bottone remove:
            // visibile solo se c'è un iscritto e se quello sono io.
            boolean visible=false;
            if (volIscritto != null) {
                if (volIscritto.equals(getLoggedUser())) {
                    visible = true;
                }
            }
            bRemove.setVisible(visible);


            // disponibilità bottone save:
            // disponibile solo se c'è un iscritto e se quello sono io
            // se il bottone non c'è ci metto una label vuota per mantenere gli allineamenti
            Component rightComp = new Label("&nbsp;", ContentMode.HTML);
            if (volIscritto != null) {
                if (volIscritto.equals(getLoggedUser())) {
                    rightComp = bSave;
                }
            }
            rightComp.setWidth("3em");

            // abilitazione note e tempo:
            // questi campi sono sempre visibili.
            // se c'è già un iscritto diverso da me, oppure se non sono abilitato per questa funzione, sono disabilitati
            boolean enabled=true;
            if (volIscritto != null) {
                if (!volIscritto.equals(getLoggedUser())) {
                    enabled=false;
                }
            }
            if(enabled){
                if(!getLoggedUser().haFunzione(funz)){
                    enabled=false;
                }
            }
            fNote.setEnabled(enabled);
            cTime.setEnabled(enabled);



            // layout finale
            HorizontalLayout layout = new HorizontalLayout();
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.addComponent(bRemove);
            layout.addComponent(bMain);
            layout.addComponent(fNote);
            layout.addComponent(cTime);
            layout.addComponent(rightComp);
            layout.setExpandRatio(bMain, 1);

            layout.setComponentAlignment(bRemove, Alignment.BOTTOM_CENTER);
            layout.setComponentAlignment(bMain, Alignment.BOTTOM_CENTER);
            layout.setComponentAlignment(fNote, Alignment.BOTTOM_LEFT);
            layout.setComponentAlignment(cTime, Alignment.BOTTOM_LEFT);
            layout.setComponentAlignment(rightComp, Alignment.BOTTOM_CENTER);

            return layout;
        }

        /**
         * Crea la stringa html da visualizzare nel componente
         * di selezione (usato come etichetta del popup o testo del bottone)
         */
        private String creaTestoComponente() {
            Funzione funz = iscrizione.getServizioFunzione().getFunzione();
            String lbltext = funz.getDescrizione();
            FontAwesome glyph = funz.getIcon();
            if (glyph != null) {
                lbltext = glyph.getHtml() + " " + lbltext;
            }
            return lbltext;
        }


        private void syncFields() {
            boolean enable = (selUtenti.getVolontario() != null);
            fNote.setVisible(enable);
            cTime.setVisible(enable);
        }


        /**
         * Recupera il volontario correntemente selezionato nell'editor
         *
         * @return il volontario
         */
        public Volontario getVolontario() {
            Volontario v;
            if (isMultiIscrizione()) {
                v = selUtenti.getVolontario();
            } else {
                v = iscrizione.getVolontario();
            }
            return v;
        }

        /**
         * Recupera l'iscrizione aggiornata
         *
         * @return l'iscrizione aggiornata, null se nell'iscrizione
         * non è specificato il volontario (significa che nessuno è iscritto)
         */
        public Iscrizione getIscrizione() {
            if (getVolontario() != null) {
                iscrizione.setVolontario(getVolontario());
                iscrizione.setNota(fNote.getValue());
                iscrizione.setMinutiEffettivi(cTime.getTotalMinutes());
                return iscrizione;
            } else {
                return null;
            }
        }


        /**
         * Combo filtrato sugli utenti che sono abilitati alla funzione corrente
         */
        class SelettoreUtenti extends ComboBox {

            public SelettoreUtenti() {

                // tutti i volontari che hanno la funzione corrente
                Funzione funz = iscrizione.getServizioFunzione().getFunzione();
                List volontari = CompanyQuery.getList(Volontario.class);
                for (Object obj : volontari) {
                    Volontario v = (Volontario) obj;
                    if (v.haFunzione(funz)) {
                        addItem(v);
                    }
                }
            }

            public Volontario getVolontario() {
                Volontario v = null;
                Object obj = getValue();
                if (obj != null && obj instanceof Volontario) {
                    v = (Volontario) obj;
                }
                return v;
            }

        }// end class SelettoreUtenti


    } // end class IscrizioneEditor

    /**
     * @return true se è attiva la modalità multi-iscrizione
     * (un volontario può iscrivere anche gli altri)
     */
    private boolean isMultiIscrizione() {
        return isAdmin();
    }

    /**
     * Ritorna l'utente correntemente loggato
     *
     * @return l'utente loggato
     */
    private Volontario getLoggedUser() {
        Volontario volontario = Volontario.find(1);
        return volontario;
    }

    /**
     * Verifica se il volontario è un admin
     *
     * @return true se è un admin
     */
    private boolean isAdmin() {
        boolean admin = false;
        Volontario vol = getLoggedUser();
        if (vol != null) {
            admin = vol.isAdmin();
        }
        return admin;
    }


}
