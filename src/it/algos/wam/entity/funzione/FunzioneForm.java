package it.algos.wam.entity.funzione;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.companyentity.*;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.ui.NavComponent;
import it.algos.wam.ui.WamUI;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.web.component.AHorizontalLayout;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.navigator.AlgosNavigator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by alex on 18-04-2016.
 * Scheda personalizzata per la entity Funzione
 */
public class FunzioneForm extends WanForm implements FunzListener {

    private static String LAR_SHEET = "800px";

    //--Campi del form. Potrebbero essere variabili locali, ma così li 'vedo' meglio
    //--alcuni sono nella superclasse
    private Button bIcona;
    private TextField fCode;

    /**
     * The form used to edit an item.
     * <p>
     * Invoca la superclasse passando i parametri:
     *
     * @param item   singola istanza della classe (obbligatorio in modifica e nullo per newRecord)
     * @param module di riferimento (obbligatorio)
     */
    public FunzioneForm(Item item, ModulePop module) {
        super(item, module);
    }// end of constructor


    /**
     * Crea prima tutti i fields (ed altri componenti)
     * Alcuni hanno delle particolarità aggiuntive
     * Vengono regolati i valori dal DB verso la UI
     */
    @Override
    protected void creaFields() {
        super.creaFields();

        this.creaBottoneIcona();
        this.creaCode();
    }// end of method

    /**
     * Create the detail component (the upper part containing the fields).
     * <p>
     * Usa il FormLayout che ha le label a sinistra dei campi (standard)
     * Se si vogliono le label sopra i campi, sovrascivere questo metodo e usare un VerticalLayout
     * <p>
     * Costruisco 3 diversi layout, disposti in verticale:
     * 1- layoutDeveloper con la selezione (eventuale) della company ed il codeCompanyUnico
     * 2- layoutForm standard per i campi normali
     * 3- layoutAggiuntivo per un placehorder
     *
     * @return the detail component containing the fields
     */
    @Override
    protected Component createComponent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        //--crea prima tutti i fields
        //--alcuni hanno delle particolarità aggiuntive
        creaFields();

        if (Pref.getBool(WAMApp.DISPLAY_LISTE_COLLEGATE, null, false)) {
            layout.addComponent(createTabSheet());
        } else {
            layout.addComponent(creaTabForm());
        }// end of if/else cycle

        return layout;
    }// end of method



    protected Component createTabSheet() {
        TabSheet tabsheet = new TabSheet();
        tabsheet.setWidth(LAR_SHEET);
        tabsheet.addTab(creaTabForm(), "Scheda");
        tabsheet.addTab(creaTabServ(), "Servizi");
        tabsheet.addTab(creaTabVol(), "Volontari");

        return tabsheet;
    }// end of method


    protected Component creaTabForm() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, false, false));
        layout.setSpacing(true);

        if (LibSession.isDeveloper()) {
            layout.addComponent(creaCompDeveloper());
            layout.addComponent(new Label("&nbsp;", ContentMode.HTML));
        }// end of if cycle

        AHorizontalLayout riga = new AHorizontalLayout(fCode, bIcona, fSigla, fOrdine);
        riga.setComponentAlignment(bIcona, Alignment.BOTTOM_CENTER);
        layout.addComponent(new AHorizontalLayout(riga));
        layout.addComponent(new AHorizontalLayout(fDescrizione));
        layout.addComponent(new Label("&nbsp;", ContentMode.HTML));

        layout.addComponent(placeholderFunz);

        return layout;
    }// end of method

    private Component creaTabServ() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, false, false));
        List<Servizio> servizi;
        Label label = new Label("Servizi che usano questa funzione");
        servizi = Servizio.getListByFunzione(getFunzione());

        // Create a grid
        Grid grid = new Grid();
        grid.setWidth(LAR_SHEET);

        // Define some columns
        grid.addColumn("sigla", String.class);
        grid.addColumn("tab", Boolean.class);
        grid.addColumn("desc", String.class);
        grid.addColumn("time", String.class);

        // Add some data rows
        for (Servizio serv : servizi) {
            grid.addRow(serv.getSigla(), serv.isVisibile(), serv.getDescrizione(), serv.getStrOrario());
        }// end of for cycle

        layout.addComponent(label);
        layout.addComponent(grid);

        return layout;
    }// end of method

    private Component creaTabVol() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, false, false));
        List<Volontario> volontari;
        Label label = new Label("Volontari abilitati per questa funzione e scadenza certificati");
        DateRenderer blsdRenderer = new DateRenderer("%1$te %1$tb %1$ty", Locale.ITALIAN);
        DateRenderer pntRenderer = new DateRenderer("%1$te %1$tb %1$ty", Locale.ITALIAN);
        DateRenderer bphtpRenderer = new DateRenderer("%1$te %1$tb %1$ty", Locale.ITALIAN);
        volontari = Volontario.getListByFunzione(getFunzione());

        // Create a grid
        Grid grid = new Grid();
        grid.setWidth(LAR_SHEET);

        // Define some columns
        grid.addColumn("nome", String.class);
        grid.addColumn("cognome", String.class);
        grid.addColumn("cell", String.class);
        grid.addColumn("BLSD", Date.class);
        grid.addColumn("PNT", Date.class);
        grid.addColumn("BPHTP", Date.class);

        // Add some data rows
        for (Volontario vol : volontari) {
            grid.addRow(vol.getNome(), vol.getCognome(), vol.getCellulare(), vol.getScadenzaBLSD(), vol.getScadenzaNonTrauma(), vol.getScadenzaTrauma());
        }// end of for cycle

        Grid.Column blsdColumn = grid.getColumn("BLSD");
        blsdColumn.setRenderer(blsdRenderer);
        Grid.Column pntColumn = grid.getColumn("PNT");
        pntColumn.setRenderer(pntRenderer);
        Grid.Column bphtpColumn = grid.getColumn("BPHTP");
        bphtpColumn.setRenderer(bphtpRenderer);

        layout.addComponent(label);
        layout.addComponent(grid);

        return layout;
    }// end of method


    /**
     * Crea il bottone per la selezione dell'icona
     */
    private void creaBottoneIcona() {
        bIcona = new Button("Icona");
        bIcona.setHtmlContentAllowed(true);
        bIcona.addStyleName("verde");
        bIcona.setDescription("Icona grafica rappresentativa della funzione");

        bIcona.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                SelectIconDialog dialog = new SelectIconDialog();
                dialog.addCloseListener(new SelectIconDialog.CloseListener() {
                    @Override
                    public void dialogClosed(SelectIconDialog.DialogEvent event) {
                        int exitcode = event.getExitcode();
                        switch (exitcode) {
                            case 0:   // close, no action
                                break;
                            case 1:   // icon selected
                                int codepoint = event.getCodepoint();
                                setGlyph(FontAwesome.fromCodepoint(codepoint));
                                syncButton();
                                break;
                            case 2:   // rewove icon
                                setGlyph(null);
                                syncButton();
                                break;
                        } // fine del blocco switch
                    }// end of inner method
                });// end of anonymous inner class
                dialog.show();
            }// end of inner method
        });// end of anonymous inner class

        syncButton();
    }// end of method

    /**
     * Crea il campo sigla interna, obbligatorio
     */
    private void creaCode() {
        fCode = (TextField) getField(Funzione_.code);

        if (isNewRecord()) {
            if (LibSession.isDeveloper()) {
                if (fCompanyCombo.getValue() != null) {
                    fCode.focus();
                }// end of if cycle
            } else {
                fCode.focus();
            }// end of if/else cycle
        } else {
            fCode.focus();
            fCode.selectAll();
        }// end of if/else cycle

        fCode.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncCodeCompanyUnico();
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    /**
     * Recupera il glifo dal field
     */
    @SuppressWarnings("all")
    private FontAwesome getGlyph() {
        FontAwesome font = null;
        Field field = getBinder().getField(Funzione_.iconCodepoint.getName());
        int codepoint = 0;

        if (field != null) {
            codepoint = (int) field.getValue();
        }// fine del blocco if

        try { // prova ad eseguire il codice
            font = FontAwesome.fromCodepoint(codepoint);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return font;
    }// end of method

    /**
     * Registra un glifo nel field
     */
    @SuppressWarnings("unchecked")
    private void setGlyph(FontAwesome glyph) {
        Field field = getBinder().getField(Funzione_.iconCodepoint.getName());

        if (field != null) {
            if (glyph != null) {
                field.setValue(glyph.getCodepoint());
            } else {
                field.setValue(0);
            }// end of if/else cycle
        }// fine del blocco if

    }// end of method

    /**
     * Sincronizza l'icona del bottone con il codepoint contenuto nel field
     */
    private void syncButton() {
        bIcona.setCaption("Scegli una icona...");
        FontAwesome glyph = getGlyph();
        if (glyph != null) {
            bIcona.setCaption(glyph.getHtml());
        }// end of if cycle
    }// end of method

    /**
     * Sincronizza il codeCompanyUnico e suggerisce la sigla
     */
    protected void syncCodeCompanyUnico() {
        String codeCompanyUnico = LibText.creaChiave(getCompany(), fCode.getValue());

        fCodeCompanyUnico.setValue(codeCompanyUnico);
        if (isNewRecord()) {
            fSigla.setValue(LibText.primaMaiuscola(fCode.getValue()));
            fSigla.selectAll();
        }// end of if cycle

    }// end of method


    /**
     * Crea un bottone per creare nuove funzioni
     *
     * @return il componente creato
     */
    @Override
    protected Button creaBottoneNuova() {
        FunzioneForm form = this;
        bNuova = new Button("Aggiungi funzione", FontAwesome.PLUS_CIRCLE);
        bNuova.setDescription("Funzioni che vengono automaticamente abilitate per il volontario, oltre a questa");

        bNuova.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditorFunz editor = new EditorFunz(form);
                placeholderFunz.addComponent(editor);
                fEditors.add(editor);
            }// end of inner method
        });// end of anonymous inner class

        return bNuova;
    }// end of method

    /**
     * Crea il placeholder aggiuntivo
     *
     * @return il componente creato
     */
    @Override
    protected AbstractLayout creaPlacehorder() {
        placeholderFunz = new VerticalLayout();
        placeholderFunz.setCaption("Funzioni dipendenti da questa");
        ((VerticalLayout) placeholderFunz).setSpacing(true);
        placeholderFunz.addComponent(bNuova);

        if (isNewRecord()) {
            placeholderFunz.setVisible(this.getCompany() != null);
        }// end of if cycle

        // aggiunge gli editor per le funzioni esistenti
        if (!isNewRecord()) {
            List<Funzione> listaFunzioniDipenenti = getFunzione().getFunzioniDipendenti();
            for (Funzione funz : listaFunzioniDipenenti) {
                EditorFunz editor = new EditorFunz(this, funz);
                placeholderFunz.addComponent(editor);
                fEditors.add(editor);
            }// end of for cycle
        }// end of if cycle

        return placeholderFunz;
    }// end of method


    /**
     * Restituisce la funzione di questo Form
     */
    public Funzione getFunzione() {
        return (Funzione) super.getEntity();
    }// end of method


    /**
     * Controlla se questa funzione è registrabile
     *
     * @return stringa vuota se registrabile, motivo se non registrabile
     */
    @Override
    protected String checkRegistrabile() {
        Funzione funzione = getFunzione();
        ArrayList<String> lista = new ArrayList<>();

        //--codeCompanyUnico deve essere unico (per i nuovi record)
        if (isNewRecord()) {
            String codeCompanyUnico = fCodeCompanyUnico.getValue();
            if (Funzione.isEntityByCodeCompanyUnico(codeCompanyUnico)) {
                return "Esiste già una funzione con questo code";
            }// end of if cycle
        }// end of if cycle

        //--le funzioni dipendenti non possono contenere la funzione principale
        if (!isNewRecord()) {
            for (Funzione funz : funzione.getFunzioniDipendenti()) {
                if (funz != null) {
                    if (funzione.getCode().equals(funz.getCode())) {
                        return "Una funzione dipendente è uguale a se stessa";
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        //--le funzioni dipendenti non possono essere nulle
        if (!isNewRecord()) {
            for (Funzione funz : funzione.getFunzioniDipendenti()) {
                if (funz == null) {
                    return "Una funzione dipendente è nulla";
                }// end of for cycle
            }// end of if cycle

            //--due (o più) funzioni dipendenti sono uguali
            for (Funzione funz : funzione.getFunzioniDipendenti()) {
                if (funz != null) {
                    if (!lista.contains(funz.getCodeCompanyUnico())) {
                        lista.add(funz.getCodeCompanyUnico());
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
            if (lista.size() < funzione.getFunzioniDipendenti().size()) {
                return "Due funzioni dipendenti sono uguali";
            }// end of if cycle
        }// end of if cycle

        return "";
    }// end of method

    @Override
    protected boolean save() {
        regolaEditorFunz();
        return super.save();
    }// end of method


    private void regolaEditorFunz() {
        Funzione funzioneMadre = getFunzione();
        Funzione funzFiglia;
        List<Funzione> funzioniDipendenti = new ArrayList<>();

        for (EditorFunz editor : fEditors) {
            funzFiglia = editor.getFunzione();
            funzioniDipendenti.add(funzFiglia);
        }// end of for cycle

        funzioneMadre.setFunzioniDipendenti(funzioniDipendenti);
    }// end of method


    @Override
    public void doDelete(EditorWam editor) {
        placeholderFunz.removeComponent(editor);
        fEditors.remove((EditorFunz) editor);
    }// end of method

}// end of class
