package it.algos.wam.tabellone;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.lib.DateConvertUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EventObject;

/**
 * Componente per presentare e modificare un turno nel Tabellone.
 * Created by alex on 05/03/16.
 */
public class CTurnoEditor extends VerticalLayout implements View {

    private Turno turno;
    private ArrayList<DismissListener> dismissListeners = new ArrayList();
    private IscrizioniEditor iscrizioniEditor;
    private EntityManager entityManager;

    public CTurnoEditor(Turno turno, EntityManager entityManager) {

        this.turno = turno;
        this.entityManager=entityManager;

        setSizeUndefined();

        addComponent(creaCompTitolo());
        iscrizioniEditor = new IscrizioniEditor();
        addComponent(iscrizioniEditor);
        addComponent(creaPanComandi());

    }


    /**
     * Crea il componente che visualizza il titolo del turno
     * (descrizione, data, ora ecc..)
     *
     * @return il componente titolo
     */
    private Component creaCompTitolo() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Servizio serv = turno.getServizio();
        LocalDate dataInizio = DateConvertUtils.asLocalDate(turno.getInizio());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d LLLL YYYY");
        String sData = dataInizio.format(formatter);
        String sOra = turno.getServizio().getOrario();
        String dataOra = sData + ", ore " + sOra;

        layout.addComponent(new Label(dataOra));
        layout.addComponent(new Label("<strong>" + serv.getDescrizione() + "</strong>", ContentMode.HTML));

        return layout;
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
                ConfirmDialog dialog=new ConfirmDialog("Elimina turno","Sei sicuro?",new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog, boolean confirmed) {
                        if(confirmed){
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
                if(saveTurno()){
                    fireDismissListeners(new DismissEvent(bRegistra, true, false));
                }
            }
        });

        // controllo se il turno è persisted

        if(turno.getId()!=null){
            layout.addComponent(bElimina);
        }
        layout.addComponent(bAnnulla);
        layout.addComponent(bRegistra);

        return layout;
    }



    /**
     * Registra il turno corrente.
     * Visualizza una notifica se non riuscito
     * @return true se riuscito
     */
    private boolean saveTurno() {
        ArrayList<Iscrizione>iscrizioni = iscrizioniEditor.getIscrizioni();
        turno.setIscrizioni(iscrizioni);
        turno.save(entityManager);

//        Notification.show("Turno non valido", Notification.Type.ERROR_MESSAGE);
        return true;
    }


    /**
     * Elimina il turno corrente.
     * Visualizza una notifica se non riuscito
     * @return true se riuscito
     */
    private boolean deleteTurno() {
        turno.delete(entityManager);
        return true;
    }


    private void fireDismissListeners(DismissEvent e) {
        for (DismissListener l : dismissListeners) {
            l.editorDismissed(e);
        }
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public void addDismissListener(DismissListener l) {
        dismissListeners.add(l);
    }

    public void removeAllDismissListeners(){
        dismissListeners.clear();
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
    public class DismissEvent extends EventObject {
        private boolean saved;
        private boolean deleted;

        public DismissEvent(Object source, boolean saved, boolean deleted) {
            super(source);
            this.saved = saved;
            this.deleted=deleted;
        }

        public boolean isSaved() {
            return saved;
        }

        public boolean isDeleted() {
            return deleted;
        }
    }


    /**
     * Editor delle iscrizioni
     */
    private class IscrizioniEditor extends VerticalLayout {

        private ArrayList<IscrizioneEditor> iEditors = new ArrayList<>();

        public IscrizioniEditor() {

            setSpacing(true);

            // crea gli editor di iscrizione e li aggiunge
            for (Funzione f : turno.getServizio().getFunzioni()) {

                Iscrizione i = turno.getIscrizione(f);

                // se l'iscrizione on esiste la crea ora
                if (i == null) {
                    i = new Iscrizione(f);
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
         * @return l'elenco delle iscrizioni
         */
        public ArrayList<Iscrizione> getIscrizioni() {
            ArrayList<Iscrizione> iscrizioni = new ArrayList();
            for(IscrizioneEditor ie : iEditors){
                Iscrizione i = ie.getIscrizione();
                if(i!=null){
                    iscrizioni.add(i);
                }
            }
            return iscrizioni;
        }

    }


    /**
     * Editor di una singola Iscrizione
     */
    private class IscrizioneEditor extends HorizontalLayout {

        private IscrizioniEditor parent;
        private ERelatedComboField fVolontario;
        private TextField fNote;
        private IntegerField fOre;
        private Volontario currentSelectedVolontario;
        private Iscrizione iscrizione;

        /**
         * @param iscrizione l'iscrizione da editare
         * @param parent     l'editor parente per il controllo che il volontario non sia già iscritto
         */
        public IscrizioneEditor(Iscrizione iscrizione, IscrizioniEditor parent) {
            this.iscrizione=iscrizione;
            this.parent = parent;
            setSpacing(true);

            String label = iscrizione.getFunzione().getDescrizione();
            fVolontario = new ERelatedComboField(Volontario.class, label);
            fVolontario.setWidth("10em");
            if(iscrizione.getVolontario()!=null){
                fVolontario.setValue(iscrizione.getVolontario().getId());
            }
            fVolontario.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {

                    // controllo che non sia già iscritto
                    Volontario sel = getVolontario();
                    if (sel != null) {
                        boolean giaIscritto = IscrizioneEditor.this.parent.isIscritto(sel, IscrizioneEditor.this);
                        if (giaIscritto) {
                            Notification.show(sel + " è già iscritto a questo turno", Notification.Type.ERROR_MESSAGE);
                            if (currentSelectedVolontario == null) {
                                fVolontario.select(null);
                            } else {
                                fVolontario.select(currentSelectedVolontario.getId());
                            }
                        }
                    }

                    syncFields();

                    Volontario v = getVolontario();
                    currentSelectedVolontario = v;
                }
            });
            addComponent(fVolontario);

            fNote = new TextField("Note");
            fNote.setValue(iscrizione.getNota());
            fNote.setWidth("10em");
            addComponent(fNote);

            fOre = new IntegerField("Ore");
            fOre.setWidth("2em");
            fOre.setValue(iscrizione.getOreEffettive());
            addComponent(fOre);

            syncFields();

        }

        private void syncFields() {
            boolean enable = (fVolontario.getValue() != null);
            fNote.setVisible(enable);
            fOre.setVisible(enable);
        }


        /**
         * Recupera il volontario correntemente selezionato nel combo
         *
         * @return il volontario
         */
        public Volontario getVolontario() {
            Volontario v = null;
            Object value = fVolontario.getSelectedBean();
            if (value != null && value instanceof Volontario) {
                v = (Volontario) value;
            }
            return v;
        }

        /**
         * Recupera l'iscrizione aggiornata
         * @return l'iscrizione aggiornata, null se non è specificato il volontario
         */
        public Iscrizione getIscrizione(){
            if(getVolontario()!=null){
                iscrizione.setVolontario(getVolontario());
                iscrizione.setNota(fNote.getValue());
                iscrizione.setOreEffettive(fOre.getValue());
                return iscrizione;
            }else{
                return null;
            }
        }

    }


}
