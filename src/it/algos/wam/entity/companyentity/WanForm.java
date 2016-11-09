package it.algos.wam.entity.companyentity;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.web.component.AHorizontalLayout;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

/**
 * Created by Gac on 02 nov 2016.
 * Scheda personalizzata per le entity Funzione, Servizio, Volonario
 */
public abstract class WanForm extends ModuleForm implements FunzioneListener {

    protected ArrayList<EditorFunz> fEditors;
    protected ArrayList<EditorServ> sfEditors;

    //--Campi del form. Potrebbero essere variabili locali, ma così li 'vedo' meglio
    protected RelatedComboField fCompanyCombo;
    protected TextField fCompanyText;
    protected TextField fCodeCompanyUnico;
    protected TextField fSigla;
    protected TextField fDescrizione;
    protected IntegerField fOrdine;

    protected Button bNuova;
    protected VerticalLayout placeholderFunz;

    /**
     * The form used to edit an item.
     * <p>
     * Invoca la superclasse passando i parametri:
     *
     * @param item   singola istanza della classe (obbligatorio in modifica e nullo per newRecord)
     * @param module di riferimento (obbligatorio)
     */
    public WanForm(Item item, ModulePop module) {
        super(item, module);
    }// end of constructor

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
        VerticalLayout layoutAll = new VerticalLayout();
        layoutAll.setMargin(true);
        layoutAll.setSpacing(true);
        AbstractOrderedLayout layoutStandard;

        if (Pref.getBool(WAMApp.USA_FORM_LAYOUT, null, false)) {
            layoutStandard = new FormLayout();
        } else {
            layoutStandard = new VerticalLayout();
            layoutStandard.setSpacing(true);
            layoutStandard.setMargin(new MarginInfo(true, false, true, false));
        }// end of if/else cycle

        //--crea prima tutti i fields
        //--alcuni hanno delle particolarità aggiuntive
        creaFields();

        //--assembla i vari elementi grafici
        if (LibSession.isDeveloper()) {
            layoutAll.addComponent(creaCompDeveloper());
        }// end of if cycle
        layoutAll.addComponent(creaCompStandard(layoutStandard));
        layoutAll.addComponent(creaCompPlaceholder());

        return layoutAll;
    }// end of method


    /**
     * Crea prima tutti i fields (ed altri componenti)
     * Alcuni hanno delle particolarità aggiuntive
     * Vengono regolati i valori dal DB verso la UI
     */
    protected void creaFields() {
        fEditors = new ArrayList<>();
        sfEditors = new ArrayList<>();

        this.creaCompany();
        this.creaCodeCompany();
        this.creaSigla();
        this.creaDescrizione();
        this.creaOrdine();
        this.creaBottoneNuova();
        this.creaPlacehorder();
    }// end of method

    /**
     * Selezione della company (solo per developer)
     * Prima parte in alto del Form
     *
     * @return the component
     */
    protected Component creaCompDeveloper() {
        VerticalLayout layout = new VerticalLayout();

        if (isNewRecord()) {
            layout.addComponent(new AHorizontalLayout(fCompanyCombo, fCodeCompanyUnico));
        } else {
            layout.addComponent(new AHorizontalLayout(fCompanyText, fCodeCompanyUnico));
        }// end of if/else cycle

        return layout;
    }// end of method

    /**
     * Fields creati in maniera assolutamente automatica
     * Parte centrale del Form
     *
     * @return the component
     */
    protected Component creaCompStandard(AbstractOrderedLayout layout) {

        layout.addComponent(fSigla);
        layout.addComponent(fDescrizione);

        if (!isNewRecord() && LibSession.isAdmin() && Pref.getBool(WAMApp.DISPLAY_FIELD_ORDINE, null, true)) {
            layout.addComponent(fOrdine);
        }// end of if cycle

        return layout;
    }// end of method

    /**
     * Parte bassa del Form
     *
     * @return the component
     */
    protected Component creaCompPlaceholder() {
        VerticalLayout layout = new VerticalLayout();

        layout.addComponent(placeholderFunz);

        return layout;
    }// end of method


    /**
     * Selezione della company (solo per developer)
     * Crea il campo company, obbligatorio
     * Nel nuovo record è un ComboBox di selezione
     * Nella modifica è un TextField
     */
    protected void creaCompany() {
        // popup di selezione (solo per nuovo record)
        if (isNewRecord()) {
            fCompanyCombo = (RelatedComboField) getField(CompanyEntity_.company);
            fCompanyCombo.setWidth("8em");
            fCompanyCombo.setRequired(true);
            fCompanyCombo.setRequiredError("Manca la company");

            fCompanyCombo.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    syncPlaceholder();
                }// end of inner method
            });// end of anonymous inner class

            if (LibSession.isDeveloper() && fCompanyCombo.getValue() == null) {
                fCompanyCombo.focus();
            }// end of if cycle
        } else { // label fissa (solo per modifica record) NON si può cambiare (farebbe casino)
            fCompanyText = null;
            BaseEntity entity = getEntity();
            WamCompany company = null;
            WamCompanyEntity wamEntity = null;
            if (entity != null && entity instanceof WamCompanyEntity) {
                wamEntity = (WamCompanyEntity) entity;
                company = (WamCompany) wamEntity.getCompany();
                fCompanyText = new TextField("Company", company.getCompanyCode());
                fCompanyText.setWidth("8em");
                fCompanyText.setEnabled(false);
                fCompanyText.setRequired(true);
            }// end of if cycle
        }// end of if/else cycle
    }// end of method


    /**
     * Crea il campo codeCompanyUnico, obbligatorio e unico
     * Viene inserito in automatico e NON dal form
     */
    protected void creaCodeCompany() {
        fCodeCompanyUnico = (TextField) getField(Funzione_.codeCompanyUnico);
    }// end of method

    /**
     * Crea il campo sigla visibile, obbligatorio
     */
    protected void creaSigla() {
        fSigla = (TextField) getField(Funzione_.sigla);
    }// end of method

    /**
     * Crea il campo descrizione, obbligatorio
     */
    protected void creaDescrizione() {
        fDescrizione = (TextField) getField(Funzione_.descrizione);
    }// end of method

    /**
     * Crea il campo ordine, obbligatorio con inserimento automatico
     * Viene inserito in automatico e NON dal form
     */
    protected void creaOrdine() {
        fOrdine = (IntegerField) getField(Funzione_.ordine);

        if (LibSession.isDeveloper()) {
            fOrdine.setEnabled(true);
        }// end of if cycle

        if (!isNewRecord()) {
            fOrdine.setRequired(true);
        }// end of if cycle
    }// end of method


    /**
     * Crea un bottone per creare nuove funzioni
     *
     * @return il componente creato
     */
    protected Button creaBottoneNuova() {
        return bNuova;
    }// end of method

    /**
     * Crea il placeholder aggiuntivo
     *
     * @return il componente creato
     */
    protected VerticalLayout creaPlacehorder() {
        return placeholderFunz;
    }// end of method


    protected void syncPlaceholder() {
        if (LibSession.isDeveloper()) {
            placeholderFunz.setVisible(fCompanyCombo.getValue() != null);
            bNuova.setVisible(fCompanyCombo.getValue() != null);
        } else {
            bNuova.setVisible(true);
        }// end of if/else cycle
    }// end of method

    /**
     * Restituisce la company di questo Form
     */
    public WamCompany getCompany() {
        WamCompany company = null;

        if (isNewRecord()) {
            Object obj = fCompanyCombo.getValue();
            if (obj != null && obj instanceof Long) {
                long companyID = (long) obj;
                company = WamCompany.find(companyID);
            }// end of if cycle
        } else {
            company = WamCompany.findByCode(fCompanyText.getValue());
        }// end of if/else cycle

        return company;
    }// end of method

    @Override
    protected ArrayList<String> isValid() {
        ArrayList<String> errs = super.isValid();

        // controlla se l'istanza di questo form è registrabile
        String err = checkRegistrabile();
        if (!err.isEmpty()) {
            errs.add(err);
        }// end of if cycle

        return errs;
    }// end of method

    /**
     * Controlla se l'istanza di questo form è registrabile
     *
     * @return stringa vuota se registrabile, motivo se non registrabile
     */
    protected String checkRegistrabile() {
        return "";
    }// end of method

    @Override
    public void doDeleteFunz(EditorFunz editor) {
    }// end of method

    @Override
    public void doDeleteServ(EditorServ editor) {
    }// end of method

}// end of class
