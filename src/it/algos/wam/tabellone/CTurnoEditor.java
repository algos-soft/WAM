package it.algos.wam.tabellone;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.lib.DateConvertUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Componente per presentare e modificare un turno nel Tabellone.
 * Created by alex on 05/03/16.
 */
public class CTurnoEditor extends CTabelloneEditor {

    private Turno turno;
    private IscrizioniEditor iscrizioniEditor;
    private TextField fieldNote;

    public CTurnoEditor(Turno turno, EntityManager entityManager) {
        super(entityManager);
        this.turno = turno;

        addComponent(creaCompTitolo());

        Component comp=creaCompDettaglio();
        if(comp!=null){
            addComponent(comp);
        }

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

        Servizio serv = turno.getServizio();
        LocalDate dataInizio = DateConvertUtils.asLocalDate(turno.getInizio());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d LLLL YYYY");
        String sData = dataInizio.format(formatter);
        String sOra = turno.getServizio().getStrOrario();
        String dataOra = sData + ", ore " + sOra;

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
        if(!serv.isOrario()){
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
        boolean success=false;
        entityManager.getTransaction().begin();

        try {

            // Registra le note
            turno.setNote(getNote());

            //cancella le iscrizioni correnti dal db
            for(Iscrizione i : turno.getIscrizioni()){
                entityManager.remove(i);
            }

            // pulisce la lista iscrizioni del turno
            turno.getIscrizioni().clear();

            // recupera le nuove iscrizioni e le aggiunge
            ArrayList<Iscrizione>iscrizioni = iscrizioniEditor.getIscrizioni();
            for(Iscrizione i : iscrizioni){
                turno.add(i);
            }

            // registra il turno
            entityManager.persist(turno);
            entityManager.getTransaction().commit();
            success=true;

        }catch (Exception e){
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }

        return success;
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


    /**
     * Ritorna il testo correntemente presente nel campo note
     */
    private String getNote(){
        String note="";
        if(fieldNote!=null){
            note=fieldNote.getValue();
        }
        return note;
    }




    /**
     * Editor delle iscrizioni
     */
    private class IscrizioniEditor extends VerticalLayout {

        private ArrayList<IscrizioneEditor> iEditors = new ArrayList<>();

        public IscrizioniEditor() {

            setSpacing(true);

            // crea gli editor di iscrizione e li aggiunge
            for (ServizioFunzione sf : turno.getServizio().getServizioFunzioni()) {

                Iscrizione i = turno.getIscrizione(sf);

                // se l'iscrizione on esiste la crea ora
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

            String label = iscrizione.getServizioFunzione().getFunzione().getDescrizione();
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
