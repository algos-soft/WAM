package it.algos.wam.entity.volontario;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.companyentity.WanForm;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.component.AHorizontalLayout;
import it.algos.webbase.web.field.*;
import it.algos.webbase.web.field.DateField;
import it.algos.webbase.web.field.PasswordField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by webbase templates.
 * Scheda personalizzata per la entity Volontario
 */
public class VolontarioForm extends WanForm {

    //--Campi del form. Potrebbero essere variabili locali, ma così li 'vedo' meglio
    //--alcuni sono nella superclasse
    private TextField fNome;
    private TextField fCognome;
    private TextField fCellulare;
    private EmailField fEmail;
    private PasswordField fPassword;
    private TextField fPasswordTextField;

    private CheckBoxField fAdmin;
    private CheckBoxField fDipendente;
    private CheckBoxField fAttivo;
    private CheckBoxField fInvioMail;

    private CheckBoxField mostraBrevetti;
    private DateField fScadenzaBLSD;
    private DateField fScadenzaPNT;
    private DateField fScadenzaBPHT;
    private AHorizontalLayout placeholderBrevetti;

    private CheckBoxField mostraFunzioni;
    private VerticalLayout placeholderFunzioni;


    /**
     * The form used to edit an item.
     * <p>
     * Invoca la superclasse passando i parametri:
     *
     * @param item   singola istanza della classe (obbligatorio in modifica e nullo per newRecord)
     * @param module di riferimento (obbligatorio)
     */
    public VolontarioForm(Item item, ModulePop module) {
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

        this.creaNome();
        this.creaCognome();

        fCellulare = (TextField) getField(Volontario_.cellulare);
        fEmail = (EmailField) getField(Volontario_.email);
        fPassword = (PasswordField) getField(Volontario_.password);
        fAdmin = (CheckBoxField) getField(Volontario_.admin);
        fDipendente = (CheckBoxField) getField(Volontario_.dipendente);
        fAttivo = (CheckBoxField) getField(Volontario_.attivo);
        fInvioMail = (CheckBoxField) getField(Volontario_.invioMail);

        fScadenzaBLSD = (DateField) getField(Volontario_.scadenzaBLSD);
        fScadenzaPNT = (DateField) getField(Volontario_.scadenzaNonTrauma);
        fScadenzaBPHT = (DateField) getField(Volontario_.scadenzaTrauma);
        this.creaChekBrevetti();
        this.creaPlacehorderBrevetti();

        this.creaChekFunzioni();
    }// end of method

    /**
     * Fields creati in maniera assolutamente automatica
     * Parte centrale del Form
     *
     * @return the component
     */
    @Override
    protected Component creaCompStandard(AbstractOrderedLayout layout) {

        layout.addComponent(new AHorizontalLayout(fNome, fCognome));

        if (isNewRecord()) {
            layout.addComponent(new AHorizontalLayout(fCellulare, fEmail, fPassword));
        } else {
            if (LibSession.isDeveloper() || WAMApp.ADMIN_VEDE_PASSWORD) {
                layout.addComponent(new AHorizontalLayout(fCellulare, fEmail, regolaPasswordField()));
            } else {
                layout.addComponent(new AHorizontalLayout(fCellulare, fEmail));
            }// end of if/else cycle
        }// end of if/else cycle

        layout.addComponent(new AHorizontalLayout(fAdmin, fDipendente, fAttivo,fInvioMail));

        layout.addComponent(new Label("&nbsp;", ContentMode.HTML)); // aggiunge un po di spazio
        layout.addComponent(mostraBrevetti);
        layout.addComponent(creaPlaceholderBrevetti());

        layout.addComponent(new Label("&nbsp;", ContentMode.HTML)); // aggiunge un po di spazio
        layout.addComponent(mostraFunzioni);
        layout.addComponent(placeholderFunz);

        return layout;
    }// end of method


    /**
     * Crea il campo nome, obbligatorio
     */
    protected void creaNome() {
        fNome = (TextField) getField(Volontario_.nome);
        fNome.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncCodeCompanyUnico();
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

    /**
     * Crea il campo cognome, obbligatorio
     */
    protected void creaCognome() {
        fCognome = (TextField) getField(Volontario_.cognome);
        fCognome.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncCodeCompanyUnico();
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

    /**
     * Sincronizza il codeCompanyUnico e suggerisce la sigla
     */
    protected void syncCodeCompanyUnico() {
        String codeCompanyUnico = "";

        if (LibText.isValida(fCognome.getValue()) && LibText.isValida(fNome.getValue())) {
            codeCompanyUnico = LibText.creaChiave(getCompany(), fCognome.getValue(), fNome.getValue());
        }// end of if cycle

        fCodeCompanyUnico.setValue(codeCompanyUnico);
    }// end of method

    /**
     * Crea il chekbox brevetti
     */
    private void creaChekBrevetti() {
        mostraBrevetti = new CheckBoxField("Controllo scadenze brevetti");

        mostraBrevetti.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                placeholderBrevetti.setVisible(mostraBrevetti.getValue());
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

    /**
     * Crea il placeholder per i brevetti
     */
    private void creaPlacehorderBrevetti() {
        placeholderBrevetti = new AHorizontalLayout(fScadenzaBLSD, fScadenzaPNT, fScadenzaBPHT);
        placeholderBrevetti.setVisible(mostraBrevetti.getValue());
    }// end of method

    /**
     * Crea il chekbox funzioni
     */
    private void creaChekFunzioni() {
        mostraFunzioni = new CheckBoxField("Controllo funzioni abilitate");

        mostraFunzioni.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                placeholderFunz.setVisible(mostraFunzioni.getValue());
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

    /**
     * Crea il placeholder per le funzioni previste
     *
     * @return il componente creato
     */
    @Override
    protected AbstractLayout creaPlacehorder() {
        placeholderFunz = new GridLayout();
        ((GridLayout) placeholderFunz).setMargin(false);
        ((GridLayout) placeholderFunz).setSpacing(true);
        ((GridLayout) placeholderFunz).setColumns(5);

        List<Funzione> funzioni = Funzione.getListByCompany(getCompany());
        for (Funzione funz : funzioni) {
            placeholderFunz.addComponent(new CheckBoxFunzione(funz));
        }// end of for cycle

        placeholderFunz = creaCompanyFunzioni((GridLayout) placeholderFunz);
        return placeholderFunz;
    }// end of method

    /**
     * Crea il placeholder per le funzioni previste
     *
     * @return il componente creato
     */
    private AbstractLayout creaCompanyFunzioni(GridLayout grid) {
        grid.removeAllComponents();

        List<Funzione> funzioni = Funzione.getListByCompany(getCompany());
        for (Funzione funz : funzioni) {
            grid.addComponent(new CheckBoxFunzione(funz));
        }// end of for cycle

        return grid;
    }// end of method


    protected void syncPlaceholder() {
        if (LibSession.isDeveloper()) {
            placeholderFunz.setVisible(fCompanyCombo.getValue() != null);
            placeholderFunz = creaCompanyFunzioni((GridLayout) placeholderFunz);
        }// end of if cycle
    }// end of method


    /**
     * Crea un field di tipo Text invece che Password
     * Permette di vedere la password solo alla creazione di una nuova scheda
     * Permette di vedere sempre la password al programmatore
     *
     * @return il componente creato
     */
    @SuppressWarnings("all")
    private TextField regolaPasswordField() {
        Field vaadinField = null;
        java.lang.reflect.Field javaField = null;
        Annotation annotation = null;
        String fieldType = "";
        AIField fieldAnnotation = null;
        Property<String> value;

        try { // prova ad eseguire il codice
            javaField = Volontario.class.getDeclaredField("password");
        } catch (Exception unErrore) { // intercetta l'errore
            return null;
        }// fine del blocco try-catch

        if (javaField != null) {
            annotation = javaField.getAnnotation(AIField.class);
        } else {
            return null;
        }// end of if/else cycle

        if (annotation != null && annotation instanceof AIField) {
            fieldAnnotation = (AIField) annotation;
        } else {
            return null;
        }// end of if/else cycle

        fPasswordTextField = new TextField();
//        fPasswordTextField.setInputPrompt(fieldAnnotation.prompt());
        fPasswordTextField.setDescription(fieldAnnotation.help());
        fPasswordTextField.setEnabled(fieldAnnotation.enabled());
        fPasswordTextField.setRequired(fieldAnnotation.required());
        fPasswordTextField.setCaption(fieldAnnotation.caption());
        fPasswordTextField.setWidth(fieldAnnotation.width());

        //--carica i valori dal DB alla UI
        value = getItem().getItemProperty(Volontario_.password.getName());
        fPasswordTextField.setValue(value.getValue());

        fPasswordTextField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
//                fPassword.setValue(valueChangeEvent.getProperty().getValue());
            }// end of inner method
        });// end of anonymous inner class

        return fPasswordTextField;
    }// end of method


    /**
     * Crea il placeholder per i brevetti, facoltativi
     *
     * @return il componente creato
     */
    private HorizontalLayout creaPlaceholderBrevetti() {
        placeholderBrevetti = new AHorizontalLayout(fScadenzaBLSD, fScadenzaPNT, fScadenzaBPHT);
        placeholderBrevetti.setVisible(mostraBrevetti.getValue());
        return placeholderBrevetti;
    }// end of method


    /**
     * Sincronizza il codeCompanyUnico
     */
    @SuppressWarnings("all")
    private void syncCode() {
        String codeCompany = LibText.primaMaiuscola(getCodeCompany());
        String nome = LibText.primaMaiuscola((String) fNome.getValue());
        String cognome = LibText.primaMaiuscola((String) fCognome.getValue());

        fCodeCompanyUnico.setValue(codeCompany + cognome + nome);
        fNome.setValue(LibText.primaMaiuscola((String) fNome.getValue()));
        fCognome.setValue(LibText.primaMaiuscola((String) fCognome.getValue()));
    }// end of method


    /**
     * Restituisce il codice della company selezionata
     */
    private String getCodeCompany() {
        String code = "";
        WamCompany company = getCompany();

        if (company != null) {
            code = company.getCompanyCode();
        }// end of if cycle

        return code;
    }// end of method

    /**
     * Restituisce la company selezionata
     */
    public WamCompany getCompany() {
        WamCompany company = null;

        if (isNewRecord()) {
            if (fCompanyCombo != null && fCompanyCombo.getValue() != null && fCompanyCombo.getValue() instanceof Long) {
                company = WamCompany.find((long) fCompanyCombo.getValue());
            }// end of if cycle
        } else {
            company = WamCompany.findByCode(fCompanyText.getValue());
        }// end of if/else cycle
        return company;
    }// end of method

    private boolean isCompanyValida() {
        return getCompany() != null;
    }// end of method


    private Volontario getVolontario() {
        return (Volontario) getEntity();
    }


    /**
     * Componente per il checkbox funzione, fatto di CheckBox e label HTML con icona
     */
    private class CheckBoxFunzione extends HorizontalLayout {
        public CheckBoxFunzione(Funzione f) {
            setSpacing(true);

            // label con icona e nome funzione
            Label label = new Label();
            label.setContentMode(ContentMode.HTML);
            FontAwesome icon = f.getIcon();
            if (icon != null) {
                label.setValue(icon.getHtml());
                label.setWidth("0.7em");
            }

            // checkBox con nome funzione
            CheckBox box = new CheckBox(f.getCode());
            box.addValueChangeListener(valueChangeEvent -> {
                if (box.getValue()) {
                    if (!getVolontario().haFunzione(f)) {
                        getVolontario().addFunzione(f);
                    }
                } else {
                    if (getVolontario().haFunzione(f)) {
                        getVolontario().removeFunzione(f);
                    }
                }
            });
            box.setValue(getVolontario().haFunzione(f));

            // dispongo i componenti
            addComponent(label);
            addComponent(box);

        }

    }

    /**
     * Controlla se questo volontario è registrabile
     *
     * @return stringa vuota se registrabile, motivo se non registrabile
     */
    @Override
    protected String checkRegistrabile() {

        //--codeCompanyUnico deve essere unico (per i nuovi record)
        if (isNewRecord()) {
            String codeCompanyUnico = fCodeCompanyUnico.getValue();
            if (Volontario.isEntityByCodeCompanyUnico(codeCompanyUnico)) {
                return "Esiste già un volontario con questo nome e cognome";
            }// end of if cycle
        }// end of if cycle

        return "";
    }// end of method

}// end of class
