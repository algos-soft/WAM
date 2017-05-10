package it.algos.wam.entity.servizio;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.companyentity.EditorServ;
import it.algos.wam.entity.companyentity.EditorWam;
import it.algos.wam.entity.companyentity.ServFunzListener;
import it.algos.wam.entity.companyentity.WanForm;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.web.component.AHorizontalLayout;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alex on 5-04-2016.
 * Scheda personalizzata per la entity Servizio
 */
public class ServizioForm extends WanForm implements ServFunzListener {


    //--Campi del form. Potrebbero essere variabili locali, ma così li 'vedo' meglio
    //--alcuni sono nella superclasse
    private ColorPicker cPicker;
    private HorizontalLayout placeholderPicker;

    private CheckBoxField fVisibileTabellone;

    private CheckBoxField fOrarioPredefinito;
    private OreMinuti hlOraInizio;
    private OreMinuti hlOraFine;
    private HorizontalLayout placeholderOrario;


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


    /**
     * Crea prima tutti i fields (ed altri componenti)
     * Alcuni hanno delle particolarità aggiuntive
     * Vengono regolati i valori dal DB verso la UI
     */
    @Override
    protected void creaFields() {
        super.creaFields();

        this.creaRigaPicker();
        this.creaChekOrario();
        this.creaPlacehorderOrario();

        fVisibileTabellone = (CheckBoxField) getField(Servizio_.abilitato);
    }// end of method


    /**
     * Fields creati in maniera assolutamente automatica
     * Parte centrale del Form
     *
     * @return the component
     */
    @Override
    protected Component creaCompStandard(AbstractOrderedLayout layout) {

        layout.addComponent(placeholderPicker);
        layout.addComponent(new AHorizontalLayout(fDescrizione));

        layout.addComponent(new Label("&nbsp;", ContentMode.HTML));
        layout.addComponent(fVisibileTabellone);
        layout.addComponent(fOrarioPredefinito);
        layout.addComponent(placeholderOrario);

        layout.addComponent(placeholderFunz);

        return layout;
    }// end of method

    /**
     * Crea il campo sigla visibile, obbligatorio
     */
    protected void creaSigla() {
        fSigla = (TextField) getField(Servizio_.sigla);
        fSigla.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncCodeCompanyUnico();
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

    /**
     * Crea la riga (HorizontalLayout) con sigla, picker e ordine
     */
    private void creaRigaPicker() {
        int colorCode = getServizio().getColore();
        Color color = new Color(colorCode);

        cPicker = new ServizioColorPicker();
        cPicker.setWidth("8em");
        cPicker.setColor(color);

        if (!isNewRecord() && Pref.getBool(WAMApp.DISPLAY_FIELD_ORDINE, null, true)) {
            placeholderPicker = new AHorizontalLayout(fSigla, cPicker, fOrdine);
        } else {
            placeholderPicker = new AHorizontalLayout(fSigla, cPicker);
        }// end of if/else cycle

        placeholderPicker.setComponentAlignment(cPicker, Alignment.BOTTOM_CENTER);
    }// end of method


    /**
     * Crea il chekbox orario
     */
    private void creaChekOrario() {
        fOrarioPredefinito = (CheckBoxField) getField(Servizio_.orario);

        fOrarioPredefinito.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                placeholderOrario.setVisible(isOrarioPredefinito());
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    /**
     * Crea il placeholder per l'orario di inizio e fine
     */
    private void creaPlacehorderOrario() {
        Servizio servizio = getServizio();
        int ore;
        int minuti;

        ore = servizio.getOraInizio();
        minuti = servizio.getMinutiInizio();
        hlOraInizio = new OreMinuti("Ora inizio", ore, minuti);

        ore = servizio.getOraFine();
        minuti = servizio.getMinutiFine();
        hlOraFine = new OreMinuti("Ora fine", ore, minuti);

        placeholderOrario = new AHorizontalLayout(hlOraInizio, hlOraFine);
        placeholderOrario.setVisible(isOrarioPredefinito());
    }// end of method


    /**
     * Sincronizza il codeCompanyUnico e suggerisce la sigla
     */
    protected void syncCodeCompanyUnico() {
        String codeCompanyUnico = LibText.creaChiave(getCompany(), fSigla.getValue());
        fCodeCompanyUnico.setValue(codeCompanyUnico);
    }// end of method

    /**
     * Crea un bottone per creare nuove funzioni
     *
     * @return il componente creato
     */
    @Override
    protected Button creaBottoneNuova() {
        ServizioForm form = this;
        bNuova = new Button("Aggiungi funzione", FontAwesome.PLUS_CIRCLE);
        bNuova.setDescription("Funzioni previste in questo servizio. Alcune obbligatorie, altre facoltative");

        bNuova.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditorServ editor = new EditorServ(form, getServizio());
                placeholderFunz.addComponent(editor);
                sfEditors.add(editor);
            }// end of inner method
        });// end of anonymous inner class

        return bNuova;
    }// end of method

    /**
     * Crea il placeholder per le funzioni previste
     *
     * @return il componente creato
     */
    @Override
    protected AbstractLayout creaPlacehorder() {
        placeholderFunz = new VerticalLayout();
        placeholderFunz.setCaption("Funzioni previste");
        ((VerticalLayout) placeholderFunz).setSpacing(true);
        placeholderFunz.addComponent(bNuova);

//        if (isNewRecord()) {
//            placeholderFunz.setVisible(false);
//        }// end of if cycle

        // aggiunge gli editor per le funzioni esistenti
        List<ServizioFunzione> listaSF = getServizio().getServizioFunzioniOrd();
        Collections.sort(listaSF);
        for (ServizioFunzione sf : listaSF) {
            EditorServ editor = new EditorServ(this, getServizio(), sf);
            sfEditors.add(editor);
            placeholderFunz.addComponent(editor);
        }// end of for cycle

        return placeholderFunz;
    }// end of method


    private Servizio getServizio() {
        return (Servizio) getEntity();
    }// end of method

    /**
     * Ritorna il valore del check Orario Predefinito
     */
    private boolean isOrarioPredefinito() {
        return Lib.getBool(fOrarioPredefinito.getValue());
    }// end of method


    /**
     * Controlla se questo servizio è registrabile
     *
     * @return stringa vuota se registrabile, motivo se non registrabile
     */
    @Override
    protected String checkRegistrabile() {
        String err = "";

        //--codeCompanyUnico deve essere unico (per i nuovi record)
        if (isNewRecord()) {
            String codeCompanyUnico = fCodeCompanyUnico.getValue();
            if (Servizio.isEntityByCodeCompanyUnico(codeCompanyUnico)) {
                err = "Esiste già un servizio con questa sigla";
            }// end of if cycle
        }// end of if cycle

        // se orario predefinito, deve avere ora inizio e ora fine valide
        if (isOrarioPredefinito()) {
            if (!hlOraInizio.isValid()) {
                if (!err.isEmpty()) {
                    err += "\n";
                }// end of if cycle
                err += "Orario di inizio servizio non valido";
            }// end of if cycle
            if (!hlOraFine.isValid()) {
                if (!err.isEmpty()) {
                    err += "\n";
                }
                err += "Orario di fine servizio non valido";
            }// end of if cycle
        }// end of for cycle

        //--obbligatorio avere delle funzioni previste
        if (sfEditors.size() == 0) {
            if (!err.isEmpty()) {
                err += "\n";
            }// end of if cycle
            err += "Non ci sono funzioni previste";
        }// end of if cycle

        // le funzioni previste devono essere tutte specificate
        for (EditorServ editor : sfEditors) {
            if (editor.getFunzione() == null) {
                if (!err.isEmpty()) {
                    err += "\n";
                }// end of if cycle
                err += "Ci sono funzioni non specificate";
                break;
            }// end of if cycle
        }// end of for cycle

        return err;
    }// end of method


    @Override
    protected boolean save() {
        uiToServizio();
        regolaEditorServ();
        return super.save();
    }// end of method

    /**
     * Sincronizza il servizio con quanto contenuto attualmente nella UI.
     */
    @SuppressWarnings("unchecked")
    private void uiToServizio() {
        int colorCode;
        int ore;
        int minuti;

        colorCode = cPicker.getColor().getRGB();
        getField(Servizio_.colore.getName()).setValue(colorCode);

        ore = hlOraInizio.getHour();
        minuti = hlOraInizio.getMinute();
        getField(Servizio_.oraInizio.getName()).setValue(ore);
        getField(Servizio_.minutiInizio.getName()).setValue(minuti);

        ore = hlOraFine.getHour();
        minuti = hlOraFine.getMinute();
        getField(Servizio_.oraFine.getName()).setValue(ore);
        getField(Servizio_.minutiFine.getName()).setValue(minuti);
    }// end of method


    private void regolaEditorServ() {
        Servizio servizio = getServizio();
        ServizioFunzione servFunz;
        List<ServizioFunzione> servizioFunzioni = new ArrayList<>();

        for (EditorServ editor : sfEditors) {
            servFunz = editor.getServizioFunzione();
            servizioFunzioni.add(servFunz);
        }// end of for cycle

        servizio.setServizioFunzioni(servizioFunzioni);
    }// end of method


    @Override
    public Funzione getFunzione() {
        return null;
    }// end of method

    @Override
    public void doDelete(EditorWam editor) {
        placeholderFunz.removeComponent(editor);
        sfEditors.remove((EditorServ) editor);
    }// end of method

}// end of class
