package it.algos.wam.entity.servizio;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alex on 5-04-2016.
 * .
 */
public class ServizioForm extends ModuleForm {

    private VerticalLayout placeholderFunc;
    private ArrayList<EditorSF> sfEditors;

    private HorizontalLayout placeholderOrario;
    private OreMinuti oraInizio;
    private OreMinuti oraFine;
    private Field fOrarioPredefinito;
    private ColorPicker picker;


    /**
     * The form used to edit an item.
     * <p>
     * Invoca la superclasse passando i parametri:
     *
     * @param item   singola istanza della classe (obbligatorio in modifica e nullo per newRecord)
     * @param module di riferimento (obbligatorio)
     */
    public ServizioForm(Item item, ModulePop module) {
        super(item, module);
    }// end of constructor

    @Override
    protected void init() {
        super.init();
        servizioToUi();
    }


    @Override
    public void createFields() {
        super.createFields();
    }

    /**
     * Create the detail component (the upper part containing the fields).
     * <p>
     * Usa il FormLayout che ha le label a sinsitra dei campi (standard)
     * Se si vogliono le label sopra i campi, sovrascivere questo metodo e usare un VerticalLayout
     *
     * @return the detail component containing the fields
     */
    @Override
    protected Component createComponent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        sfEditors = new ArrayList<>();

        return creaCompDetail(layout);
    }// end of method


    /**
     * Crea il componente che visualizza il dettaglio
     * Retrieve the fields from the binder and place them in the UI.
     *
     * @param layout per visualizzare i componenti
     * @return il componente dettagli
     */
    private Component creaCompDetail(VerticalLayout layout) {
        TextField fSigla = (TextField) getField(Funzione_.sigla);
        fSigla.setWidth("8em");
        fSigla.focus();
        TextField fCodeCompanyUnico = (TextField) getField(Funzione_.codeCompanyUnico);
        fCodeCompanyUnico.setWidth("14em");
        fCodeCompanyUnico.setEnabled(false);
        TextField fDescrizione = (TextField) getField(Funzione_.descrizione);
        fDescrizione.setWidth("22.5em");
        IntegerField fOrdine = (IntegerField) getField(Funzione_.ordine);
        fOrdine.setEnabled(false);

        picker = new ServizioColorPicker();
        picker.setWidth("8em");

        // selezione della company (solo per developer)
        if (LibSession.isDeveloper()) {
            // popup di selezione (solo per nuovo record)
            if (isNewRecord()) {
                RelatedComboField fCompany = (RelatedComboField) getField(CompanyEntity_.company);
                fCompany.setWidth("8em");
                layout.addComponent(fCompany);
            } else { // label fissa (solo per modifica record) NON si può cambiare (farebbe casino)
                BaseEntity entity = getEntity();
                WamCompany company = null;
                Servizio serv = null;
                if (entity != null && entity instanceof Servizio) {
                    serv = (Servizio) entity;
                    company = (WamCompany) serv.getCompany();
                    TextField fCompany = new TextField("Company", company.getCompanyCode());
                    fCompany.setWidth("8em");
                    fCompany.setEnabled(false);
                    HorizontalLayout h = new HorizontalLayout(fCompany, fCodeCompanyUnico);
                    h.setSpacing(true);
                    layout.addComponent(h);
                }// end of if cycle
            }// end of if/else cycle
        }// end of if cycle

        HorizontalLayout hl = new HorizontalLayout(fSigla, picker);
        if (!isNewRecord()) {
            hl.addComponent(fOrdine);
        }// end of if cycle
        hl.setComponentAlignment(picker, Alignment.BOTTOM_CENTER);

        hl.setSpacing(true);
        layout.addComponent(hl);
        layout.addComponent(fDescrizione);

        fOrarioPredefinito = getField(Servizio_.orario);
        fOrarioPredefinito.setCaption("Orario predefinito");
        layout.addComponent(fOrarioPredefinito);
        fOrarioPredefinito.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                placeholderOrario.setVisible(isOrarioPredefinito());
            }
        });

        // aggiunge il placeholder per l'orario di inizio e fine
        oraInizio = new OreMinuti("Ora inizio");
        oraFine = new OreMinuti("Ora fine");
        placeholderOrario = new HorizontalLayout(oraInizio, oraFine);
        placeholderOrario.setSpacing(true);
        layout.addComponent(placeholderOrario);
        placeholderOrario.setVisible(isOrarioPredefinito());

        // aggiunge un po di spazio
        layout.addComponent(new Label("&nbsp;", ContentMode.HTML));

        // aggiunge il placeholder per le funzioni previste
        placeholderFunc = new VerticalLayout();
        placeholderFunc.setCaption("Funzioni previste");
        placeholderFunc.setSpacing(true);
        layout.addComponent(placeholderFunc);

        // aggiunge un bottone per creare nuove funzioni
        Button bNuova = new Button("Aggiungi funzione", FontAwesome.PLUS_CIRCLE);
        bNuova.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditorSF editor = new EditorSF(null);
                placeholderFunc.addComponent(editor);
                sfEditors.add(editor);
            }
        });
        layout.addComponent(bNuova);

        return layout;
    }// end of method


    private Servizio getServizio() {
        return (Servizio) getEntity();
    }


    @Override
    protected boolean save() {
        boolean saved = super.save();
        return saved;
    }


    @Override
    public void postCommit() {
        uiToServizio();
        super.postCommit();
    }

    @Override
    protected ArrayList<String> isValid() {
        ArrayList<String> errs = super.isValid();

        // controlla se questo servizio è registrabile
        String err = checkServizioRegistrabile();
        if (!err.isEmpty()) {
            errs.add(err);
        }

        return errs;
    }

    /**
     * Controlla se questo servizio è registrabile
     *
     * @return stringa vuota se registrabile, motivo se non registrabile
     */
    private String checkServizioRegistrabile() {
        String err = "";

        // se orario predefinito, deve avere ora inizio e ora fine valide
        if (isOrarioPredefinito()) {
            if (!oraInizio.isValid()) {
                if (!err.isEmpty()) {
                    err += "\n";
                }
                err += "Orario di inizio servizio non valido";
            }
            if (!oraFine.isValid()) {
                if (!err.isEmpty()) {
                    err += "\n";
                }
                err += "Orario di fine servizio non valido";
            }
        }

        // deve avere delle funzioni previste
        if (sfEditors.size() == 0) {
            if (!err.isEmpty()) {
                err += "\n";
            }
            err += "Non ci sono funzioni previste";
        }

        // le funzioni previste devono essere tutte specificate
        for (EditorSF editor : sfEditors) {
            if (editor.getFunzione() == null) {
                if (!err.isEmpty()) {
                    err += "\n";
                }
                err += "Ci sono funzioni non specificate";
                break;
            }
        }

        return err;
    }


    /**
     * Carica i valori dal servizio alla UI
     */
    private void servizioToUi() {
        Property<Integer> pi;
        int h, m;

        pi = getItem().getItemProperty(Servizio_.oraInizio.getName());
        h = pi.getValue();
        pi = getItem().getItemProperty(Servizio_.minutiInizio.getName());
        m = pi.getValue();
        oraInizio.setTime(h, m);

        pi = getItem().getItemProperty(Servizio_.oraFine.getName());
        h = pi.getValue();
        pi = getItem().getItemProperty(Servizio_.minutiFine.getName());
        m = pi.getValue();
        oraFine.setTime(h, m);

        pi = getItem().getItemProperty(Servizio_.colore.getName());
        int colorcode = pi.getValue();
        Color color = new Color(colorcode);
        picker.setColor(color);

        // aggiunge gli editor per le funzioni esistenti
        List<ServizioFunzione> listaSF = getServizio().getServizioFunzioni();
        Collections.sort(listaSF);
        for (ServizioFunzione sf : listaSF) {
            EditorSF editor = new EditorSF(sf);
            placeholderFunc.addComponent(editor);
            sfEditors.add(editor);
        }


    }

    /**
     * Sincronizza il servizio con quanto contenuto attualmente nella UI.
     */
    private void uiToServizio() {

        int h, m;
        h = oraInizio.getHour();
        getServizio().setOraInizio(h);
        m = oraInizio.getMinute();
        getServizio().setMinutiInizio(m);

        h = oraFine.getHour();
        getServizio().setOraFine(h);
        m = oraFine.getMinute();
        getServizio().setMinutiFine(m);

        int col = picker.getColor().getRGB();
        getServizio().setColore(col);

        // sincronizza le funzioni del servizio con quelle dell'editor di funzioni
        syncFunzioni();

    }


    /**
     * Sincronizza i ServizioFunzione esistenti: modifica quelli esistenti,
     * cancella quelli inesistenti e crea quelli nuovi.
     */
    private void syncFunzioni() {
        // modifica quelli esistenti e aggiunge i nuovi
        for (EditorSF editor : sfEditors) {
            ServizioFunzione sf = editor.getServizioFunzione();
            if (sf == null) {    // se è nuovo lo crea ora
                sf = new ServizioFunzione(getServizio(), null);
                //sf.setServizio(getServizio());
            }
            // aggiorna l'entity dall'editor
            sf.setFunzione(editor.getFunzione());
            sf.setObbligatoria(editor.isObbligatoria());

            // se nuovo, lo aggiunge al servizio
            if (sf.getId() == null) {
                getServizio().add(sf);
            }
        }

        // Solo se non sono nuovi:
        // cancella quelli inesistenti nell'editor (sono stati cancellati).
        // Dato che elimina elementi della stessa lista che viene iterata, esegue
        // l'iterazione partendo dal fondo
        for (int i = getServizio().getServizioFunzioni().size() - 1; i >= 0; i--) {
            ServizioFunzione sf = getServizio().getServizioFunzioni().get(i);
            if (sf.getId() != null) {   // non considera quelli senza id, sono i nuovi!
                boolean found = false;
                for (EditorSF editor : sfEditors) {
                    if (editor.getServizioFunzione() != null) {
                        if (editor.getServizioFunzione().equals(sf)) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    getServizio().getServizioFunzioni().remove(i);
                }
            }

        }

    }


    /**
     * Ritorna il valore del check Orario Predefinito
     */
    private boolean isOrarioPredefinito() {
        return Lib.getBool(fOrarioPredefinito.getValue());
    }


    /**
     * Editor di una singola funzione del servizio
     */
    private class EditorSF extends HorizontalLayout {

        private ServizioFunzione serFun;
        private CheckBox checkSel;
        private ERelatedComboField comboFunzioni;
        private CheckBox checkObbl;
        private Button iconButton;

        public EditorSF(ServizioFunzione serFun) {

            this.serFun = serFun;

            setSpacing(true);

            //@todo aggiunta gac
            if (true) {
                iconButton = new Button();
                iconButton.setHtmlContentAllowed(true);
                iconButton.addStyleName("bfunzione");
                iconButton.setWidth("3em");
                iconButton.addStyleName("verde");

                addComponent(iconButton);
                if (serFun != null) {
                    Funzione funz = serFun.getFunzione();
                    setIconButton(funz);
                }
            }// end of if cycle


            // combo di selezione della funzione
            comboFunzioni = new ERelatedComboField(Funzione.class);
            comboFunzioni.sort(Funzione_.sigla);
            comboFunzioni.setWidth("12em");
            if (serFun != null) {
                Funzione f = serFun.getFunzione();
                if (f != null) {
                    comboFunzioni.setValue(f.getId());
                }// end of if cycle
            }// end of if cycle
            comboFunzioni.addListener(new Listener() {
                @Override
                public void componentEvent(Event event) {
                    Object obj = event.getSource();
                    Object value;
                    RelatedComboField combo;
                    Funzione funz;
                    if (obj instanceof RelatedComboField) {
                        combo = (RelatedComboField) obj;
                        value = combo.getValue();
                        if (value instanceof Long) {
                            funz = Funzione.find((Long) value);
                            setIconButton(funz);
                        }// end of if cycle
                    }// end of if cycle
                }// end of inner method
            });// end of anonymous inner class

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
                        List<Iscrizione> iscrizioni = WamQuery.queryIscrizioniServizioFunzione(getEntityManager(), serFun);

                        if (iscrizioni.size() == 0) {

                            String messaggio = "Vuoi eliminare la funzione " + serFun.getFunzione().getSigla() + "?";
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
            Servizio s = getServizio();
            placeholderFunc.removeComponent(this);
            sfEditors.remove(this);
            int a = 87;
        }

        /**
         * Ritorna la funzione correntemente selezionata nel popup
         *
         * @return la funzione selezionata
         */
        public Funzione getFunzione() {
            Funzione f = null;
            Object obj = comboFunzioni.getSelectedBean();
            if (obj != null && obj instanceof Funzione) {
                f = (Funzione) obj;
            }
            return f;
        }

        public ServizioFunzione getServizioFunzione() {
            return serFun;
        }

        /**
         * Ritorna il ServizioFunzione aggiornato in base all'editing corrente.
         * Se il ServizioFunzione è presente lo aggiorna, altrimenti lo crea ora.
         *
         * @return il ServizioFunzione aggiornato
         */
        public ServizioFunzione getServizioFunzioneAggiornato() {
            ServizioFunzione sf = serFun;
            if (sf == null) {
                sf = new ServizioFunzione(getServizio(), null);
            }
            sf.setFunzione(getFunzione());
            sf.setObbligatoria(isObbligatoria());
            return serFun;
        }


        /**
         * Assegna un'icona al bottone
         */
        private void setIconButton(Funzione funz) {
            if (funz != null) {
                iconButton.setCaption(funz.getIconHtml());
            }// end of if cycle
        }// end of inner method

    }// end of inner class


    /**
     * Combo per l'ora
     */
    private class ComboOra extends ComboBox {
        public ComboOra() {
            setWidth("5em");
            setTextInputAllowed(false);
            setNewItemsAllowed(false);
            for (int i = 0; i < 24; i++) {
                addItem(intToString(i));
            }
        }

        /**
         * Trasforma un intero in stringa
         *
         * @param i l'intero
         * @return la stringa
         */
        private String intToString(int i) {
            String s = "" + i;
            if (s.length() == 1) {
                s = "0" + s;
            }
            return s;
        }

        /**
         * Returns the hour
         *
         * @return the hour, -1 if not selected
         */
        public int getHour() {
            int num = -1;
            Object value = getValue();
            if (value != null) {
                num = Integer.parseInt(value.toString());
            }
            return num;
        }

        /**
         * Assegna l'ora
         *
         * @param h l'ora
         */
        public void setHour(Integer h) {
            setValue(intToString(h));
        }

        @Override
        public boolean isValid() {
            return (getHour() != -1);
        }
    }

    /**
     * Combo per i minuti
     */
    private class ComboMinuti extends ComboBox {
        public ComboMinuti() {
            setWidth("5em");
            setTextInputAllowed(false);
            setNewItemsAllowed(false);
            addItem(intToString(0));
            addItem(intToString(15));
            addItem(intToString(30));
            addItem(intToString(45));
        }

        /**
         * Trasforma un intero in stringa
         *
         * @param i l'intero
         * @return la stringa
         */
        private String intToString(int i) {
            String s = "" + i;
            if (s.length() == 1) {
                s = "0" + s;
            }
            return s;
        }

        /**
         * Returns the minute
         *
         * @return the minute, -1 if not selected
         */
        public int getMinute() {
            int num = -1;
            Object value = getValue();
            if (value != null) {
                num = Integer.parseInt(value.toString());
            }
            return num;
        }

        /**
         * Assegna i minuti
         *
         * @param m i minuti
         */
        public void setMinute(Integer m) {
            setValue(intToString(m));
        }

        @Override
        public boolean isValid() {
            return (getMinute() != -1);
        }


    }

    /**
     * Componente con ore e minuti
     */
    private class OreMinuti extends HorizontalLayout {

        private ComboOra ch;
        private ComboMinuti cm;

        public OreMinuti(String title) {
            setCaption(title);
            setSpacing(false);

            ch = new ComboOra();
            Label lbl = new Label("&nbsp;:&nbsp;", ContentMode.HTML);
            cm = new ComboMinuti();
            addComponent(ch);
            addComponent(lbl);
            addComponent(cm);
            setComponentAlignment(ch, Alignment.MIDDLE_CENTER);
            setComponentAlignment(lbl, Alignment.MIDDLE_CENTER);
            setComponentAlignment(cm, Alignment.MIDDLE_CENTER);
        }

        /**
         * Assegna ore e minuti
         *
         * @param h le ore
         * @param m i minuti
         */
        public void setTime(Integer h, Integer m) {
            ch.setHour(h);
            cm.setMinute(m);
        }

        public int getHour() {
            return ch.getHour();
        }

        public int getMinute() {
            return cm.getMinute();
        }

        public boolean isValid() {
            return (ch.isValid() && cm.isValid());
        }

    }


}
