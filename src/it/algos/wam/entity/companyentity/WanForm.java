package it.algos.wam.entity.companyentity;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.funzione.EditorFunz;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.web.component.AHorizontalLayout;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gac on 02 nov 2016.
 * Scheda personalizzata per le entity Funzione,Servizio, Volonario
 */
public abstract class WanForm extends ModuleForm {

    protected ArrayList<EditorFunz> fEditors;

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
        FormLayout layoutStandard = new AFormLayout();
        layoutAll.setMargin(true);
        layoutAll.setSpacing(true);

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
    protected Component creaCompStandard(FormLayout layout) {

        layout.addComponent(fSigla);
        layout.addComponent(fDescrizione);

        if (!isNewRecord() && LibSession.isAdmin()) {
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
     * Crea prima tutti i fields
     * Alcuni hanno delle particolarità aggiuntive
     */
    protected void creaFields() {
        fEditors = new ArrayList<>();

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
            Funzione funz = null;
            if (entity != null && entity instanceof Funzione) {
                funz = (Funzione) entity;
                company = (WamCompany) funz.getCompany();
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
     *
     * @return il componente creato
     */
    protected TextField creaCodeCompany() {
        fCodeCompanyUnico = (TextField) getField(Funzione_.codeCompanyUnico);
        return fCodeCompanyUnico;
    }// end of method

    /**
     * Crea il campo sigla visibile, obbligatorio
     *
     * @return il componente creato
     */
    protected TextField creaSigla() {
        fSigla = (TextField) getField(Funzione_.sigla);
        return fSigla;
    }// end of method

    /**
     * Crea il campo descrizione, obbligatorio
     *
     * @return il componente creato
     */
    protected TextField creaDescrizione() {
        fDescrizione = (TextField) getField(Funzione_.descrizione);
        return fDescrizione;
    }// end of method

    /**
     * Crea il campo ordine, obbligatorio con inserimento automatico
     * Viene inserito in automatico e NON dal form
     *
     * @return il componente creato
     */
    protected IntegerField creaOrdine() {
        fOrdine = (IntegerField) getField(Funzione_.ordine);

        if (LibSession.isDeveloper()) {
            fOrdine.setEnabled(true);
        }// end of if cycle

        if (!isNewRecord()) {
            fOrdine.setRequired(true);
        }// end of if cycle

        return fOrdine;
    }// end of method


    /**
     * Crea un bottone per creare nuove funzioni
     *
     * @return il componente creato
     */
    private Button creaBottoneNuova() {
        WanForm form = this;
        bNuova = new Button("Aggiungi funzione", FontAwesome.PLUS_CIRCLE);
        bNuova.setDescription("Funzioni che vengono automaticamente abilitate per il volontario, oltre a questa");

        bNuova.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
//                EditorFunz editor = new EditorFunz(form, null, false);
//                placeholderFunz.addComponent(editor);
//                fEditors.add(editor);
            }// end of inner method
        });// end of anonymous inner class

        return bNuova;
    }// end of method

    /**
     * Crea il placeholder aggiuntivo
     *
     * @return il componente creato
     */
    protected VerticalLayout creaPlacehorder() {
        placeholderFunz = new VerticalLayout();
        placeholderFunz.setCaption("Funzioni dipendenti da questa");
        placeholderFunz.setSpacing(true);
        placeholderFunz.addComponent(bNuova);

        if (isNewRecord()) {
            placeholderFunz.setVisible(this.getCompany() != null);
        }// end of if cycle

        // aggiunge gli editor per le funzioni esistenti
        if (!isNewRecord()) {
//            List<Funzione> listaFunzioniDipenenti = getFunzione().getFunzioniDipendenti();
//            for (Funzione funz : listaFunzioniDipenenti) {
//                EditorFunz editor = new EditorFunz(this, funz, false);
//                placeholderFunz.addComponent(editor);
//                fEditors.add(editor);
//            }// end of for cycle
        }// end of if cycle

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

}// end of class
