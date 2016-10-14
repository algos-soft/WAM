package it.algos.wam.entity.volontario;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.web.component.AHorizontalLayout;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.*;
import it.algos.webbase.web.field.DateField;
import it.algos.webbase.web.field.PasswordField;
import it.algos.webbase.web.field.TextArea;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by webbase templates.
 * Scheda personalizzata per la entity Volontario
 */
public class VolontarioForm extends ModuleForm {

    //--Campi del form. Potrebbero essere variabili locali, ma così li 'vedo' meglio
    @SuppressWarnings("all")
    private RelatedComboField fCompanyCombo;
    @SuppressWarnings("all")
    private TextField fCompanyText;
    @SuppressWarnings("all")
    private Field fCodeCompanyUnico;
    @SuppressWarnings("all")
    private Field fNome;
    @SuppressWarnings("all")
    private Field fCognome;
    @SuppressWarnings("all")
    private Field fCellulare;
    @SuppressWarnings("all")
    private Field fEmail;
    @SuppressWarnings("all")
    private Field fPassword;
    private  TextField fPasswordTextField = null;
    @SuppressWarnings("all")
    private Field fAdmin;
    @SuppressWarnings("all")
    private Field fDipendente;
    @SuppressWarnings("all")
    private Field fAttivo;
    @SuppressWarnings("all")
    private Field fScadenzaBLSD;
    @SuppressWarnings("all")
    private Field fScadenzaPNT;
    @SuppressWarnings("all")
    private Field fScadenzaBPHT;
    private CheckBoxField mostraBrevetti;
    private CheckBoxField mostraFunzioni;
    private AHorizontalLayout placeholderBrevetti;
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

        return creaCompDetail(layout);
    }// end of method

    /**
     * Crea il componente che visualizza il dettaglio
     * Retrieve the fields from the binder and place them in the UI.
     *
     * @param layout per visualizzare i componenti
     * @return il componente dettagli
     */
    @SuppressWarnings("all")
    private Component creaCompDetail(VerticalLayout layout) {
        creaFields();

        // selezione della company (solo per developer)
        AHorizontalLayout layoutCompany = new AHorizontalLayout(creaCompany(), fCodeCompanyUnico);
        layoutCompany.setVisible(LibSession.isDeveloper());
        layout.addComponent(layoutCompany);

        layout.addComponent(new AHorizontalLayout(fNome, fCognome));
        if (isNewRecord()) {
            layout.addComponent(new AHorizontalLayout(fCellulare, fEmail, fPassword));
        } else {
            if (LibSession.isDeveloper()|| WAMApp.ADMIN_VEDE_PASSWORD) {
                layout.addComponent(new AHorizontalLayout(fCellulare, fEmail, regolaPasswordField()));
            } else {
                layout.addComponent(new AHorizontalLayout(fCellulare, fEmail));
            }// end of if/else cycle
        }// end of if/else cycle

        // aggiunge un po di spazio
        layout.addComponent(new Label("&nbsp;", ContentMode.HTML));
        layout.addComponent(new AHorizontalLayout(fAdmin, fDipendente, fAttivo));
        layout.addComponent(mostraBrevetti);
        layout.addComponent(creaPlaceholderBrevetti());

        // aggiunge un po di spazio
        layout.addComponent(new Label("&nbsp;", ContentMode.HTML));
        layout.addComponent(mostraFunzioni);
        layout.addComponent(creaPlaceorderFunzioni());

        syncPlaceholderFunzioni();

        return layout;
    }// end of method

    protected void creaFields() {

        fCodeCompanyUnico = getField(Volontario_.codeCompanyUnico);
        fNome = getField(Volontario_.nome);
        fNome.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncCode();
            }// end of inner method
        });// end of anonymous inner class

        fCognome = getField(Volontario_.cognome);
        fCognome.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncCode();
            }// end of inner method
        });// end of anonymous inner class
        fCellulare = getField(Volontario_.cellulare);
        fEmail = getField(Volontario_.email);

        fPassword = getField(Volontario_.password);

        fAdmin = getField(Volontario_.admin);
        fDipendente = getField(Volontario_.dipendente);
        fAttivo = getField(Volontario_.attivo);

        mostraBrevetti = new CheckBoxField("Controllo scadenze brevetti", false);
        mostraBrevetti.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                placeholderBrevetti.setVisible(mostraBrevetti.getValue());
            }// end of inner method
        });// end of anonymous inner class
        fScadenzaBLSD = getField(Volontario_.scadenzaBLSD);
        fScadenzaPNT = getField(Volontario_.scadenzaNonTrauma);
        fScadenzaBPHT = getField(Volontario_.scadenzaTrauma);

        mostraFunzioni = new CheckBoxField("Controllo funzioni abilitate", isNewRecord());
        mostraFunzioni.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncPlaceholderFunzioni();
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

    /**
     * Create a single field.
     * The field type is chosen according to the Java type.
     *
     * @param attr the metamodel Attribute
     */
    @Override
    @SuppressWarnings("all")
    protected Field createField(Attribute attr) {
        Field vaadinField = null;
        java.lang.reflect.Field javaField = null;
        Annotation annotation = null;
        String fieldType = "";
        AIField fieldAnnotation = null;

        try { // prova ad eseguire il codice
            javaField = Volontario.class.getDeclaredField(attr.getName());
        } catch (Exception unErrore) { // intercetta l'errore
            return super.createField(attr);
        }// fine del blocco try-catch

        if (javaField != null) {
            annotation = javaField.getAnnotation(AIField.class);
        } else {
            return super.createField(attr);
        }// end of if/else cycle

        if (annotation != null && annotation instanceof AIField) {
            fieldAnnotation = (AIField) annotation;
        } else {
            return super.createField(attr);
        }// end of if/else cycle

        if (fieldAnnotation != null) {
            switch (fieldAnnotation.type()) {
                case text:
                    vaadinField = new TextField();
                    ((TextField) vaadinField).setInputPrompt(fieldAnnotation.prompt());
                    ((TextField) vaadinField).setDescription(fieldAnnotation.help());
                    ((TextField) vaadinField).setEnabled(fieldAnnotation.enabled());
                    break;
                case email:
                    vaadinField = new EmailField();
                    ((EmailField) vaadinField).setInputPrompt(fieldAnnotation.prompt());
                    ((EmailField) vaadinField).setDescription(fieldAnnotation.help());
                    break;
                case checkbox:
                    vaadinField = new CheckBoxField();
                    ((CheckBoxField) vaadinField).setDescription(fieldAnnotation.help());
                    break;
                case area:
                    vaadinField = new TextArea();
                    ((TextArea) vaadinField).setInputPrompt(fieldAnnotation.prompt());
                    ((TextArea) vaadinField).setDescription(fieldAnnotation.help());
                    break;
                case date:
                    vaadinField = new DateField();
                    ((DateField) vaadinField).setDescription(fieldAnnotation.help());
                    break;
                case password:
                    vaadinField = new PasswordField();
                    ((PasswordField) vaadinField).setDescription(fieldAnnotation.help());
                    ((PasswordField) vaadinField).setEnabled(fieldAnnotation.enabled());
                    break;
                default: // caso non definito
                    vaadinField = new TextField();
                    ((TextField) vaadinField).setInputPrompt(fieldAnnotation.prompt());
                    ((TextField) vaadinField).setDescription(fieldAnnotation.help());
                    ((TextField) vaadinField).setEnabled(fieldAnnotation.enabled());
                    break;
            } // fine del blocco switch
            vaadinField.setRequired(fieldAnnotation.required());
            vaadinField.setCaption(fieldAnnotation.caption());
            vaadinField.setWidth(fieldAnnotation.width());

            return vaadinField;
        } else {
            return super.createField(attr);
        }// end of if/else cycle
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
                fPassword.setValue(valueChangeEvent.getProperty().getValue());
            }// end of inner method
        });// end of anonymous inner class

        return fPasswordTextField;
    }// end of method


    /**
     * Selezione della company (solo per developer)
     * Crea il campo company, obbligatorio
     * Nel nuovo record è un ComboBox di selezione
     * Nella modifica è un TextField
     *
     * @return il componente creato
     */
    private Component creaCompany() {
        // popup di selezione (solo per nuovo record)
        if (isNewRecord()) {
            fCompanyCombo = (RelatedComboField) getField(CompanyEntity_.company);
            fCompanyCombo.setWidth("8em");
            fCompanyCombo.setRequired(true);
            fCompanyCombo.setRequiredError("Manca la company");

            fCompanyCombo.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    syncPlaceholderFunzioni();
                }// end of inner method
            });// end of anonymous inner class

            return fCompanyCombo;
        } else { // label fissa (solo per modifica record) NON si può cambiare (farebbe casino)
            fCompanyText = null;
            BaseEntity entity = getEntity();
            WamCompany company = null;
            Volontario vol = null;
            if (entity != null && entity instanceof Volontario) {
                vol = (Volontario) entity;
                company = (WamCompany) vol.getCompany();
                fCompanyText = new TextField("Company", company.getCompanyCode());
                fCompanyText.setWidth("8em");
                fCompanyText.setEnabled(false);
                fCompanyText.setRequired(true);
            }// end of if cycle

            return fCompanyText;
        }// end of if/else cycle
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
     * Crea il placeholder per le funzioni previste
     *
     * @return il componente creato
     */
    private VerticalLayout creaPlaceorderFunzioni() {
        placeholderFunzioni = new VerticalLayout();
        placeholderFunzioni.setCaption("Funzioni abilitate");
        placeholderFunzioni.setSpacing(true);

        placeholderFunzioni.setVisible(mostraFunzioni.getValue());
        return placeholderFunzioni;
    }// end of method

    private void syncPlaceholderFunzioni() {
        placeholderFunzioni.removeAllComponents();
        placeholderFunzioni.addComponent(creaCompFunzioni());

//        if (LibSession.isDeveloper()) {
        placeholderFunzioni.setVisible(isCompanyValida() && mostraFunzioni.getValue());
//            creaPlaceorderFunzioni();
//        }// end of if cycle
    }// end of method

    /**
     * Crea il componente per la selezione delle funzioni abilitate.
     *
     * @return il componente funzioni
     */
    private Component creaCompFunzioni() {
        GridLayout grid = new GridLayout();
        grid.setSpacing(true);
        grid.setColumns(5);

        List<Funzione> funzioni = Funzione.getListByCompany(getCompany());
        for (Funzione funz : funzioni) {
            grid.addComponent(new CheckBoxFunzione(funz));
        }// end of for cycle

        return grid;
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
    private WamCompany getCompany() {
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


}// end of class
