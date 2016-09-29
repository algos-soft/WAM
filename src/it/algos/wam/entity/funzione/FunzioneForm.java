package it.algos.wam.entity.funzione;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;

/**
 * Created by alex on 18-04-2016.
 * Scheda personalizzata per la entity Funzione
 */
public class FunzioneForm extends ModuleForm {

    //--Campi del form. Potrebbero essere variabili locali, ma così li 'vedo' meglio
    @SuppressWarnings("all")
    private RelatedComboField fCompanyCombo;
    @SuppressWarnings("all")
    private TextField fCompanyText;
    @SuppressWarnings("all")
    private TextField fCodeCompanyUnico;
    private Button bIcona;
    @SuppressWarnings("all")
    private TextField fCode;
    @SuppressWarnings("all")
    private TextField fSigla;
    @SuppressWarnings("all")
    private TextField fDescrizione;
    @SuppressWarnings("all")
    private IntegerField fOrdine;


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
     * Crea il componente che visualizza il dettaglio
     * Retrieve the fields from the binder and place them in the UI.
     *
     * @return il componente dettagli
     */
    @Override
    protected Component creaCompDetail(FormLayout layout) {

        // selezione della company (solo per developer)
        if (LibSession.isDeveloper()) {
            layout.addComponent(this.creaCompany());
            layout.addComponent(this.creaCodeCompany());
        }// end of if cycle

        layout.addComponent(this.creaBottoneIcona());
        layout.addComponent(this.creaCode());
        layout.addComponent(this.creaSigla());
        layout.addComponent(this.creaDescrizione());

        if (!isNewRecord() && LibSession.isAdmin()) {
            layout.addComponent(this.creaOrdine());
        }// end of if cycle

        return layout;
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

            if (LibSession.isDeveloper() && fCompanyCombo.getValue() == null) {
                fCompanyCombo.focus();
            }// end of if cycle

            return fCompanyCombo;
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

            return fCompanyText;
        }// end of if/else cycle
    }// end of method

    /**
     * Crea il campo codeCompanyUnico, obbligatorio e unico
     * Viene inserito in automatico e NON dal form
     *
     * @return il componente creato
     */
    private TextField creaCodeCompany() {
        fCodeCompanyUnico = (TextField) getField(Funzione_.codeCompanyUnico);

        fCodeCompanyUnico.setWidth("16em");
        fCodeCompanyUnico.setEnabled(false);
        fCodeCompanyUnico.setRequired(true);

        return fCodeCompanyUnico;
    }// end of method


    /**
     * Crea il bottone per la selezione dell'icona
     *
     * @return il componente creato
     */
    private Button creaBottoneIcona() {
        bIcona = new Button("Icona");
        bIcona.setHtmlContentAllowed(true);
        bIcona.addStyleName("verde");
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

//        this.bIcona = bIcona;
        syncButton();

        return bIcona;
    }// end of method

    /**
     * Crea il campo sigla interna, obbligatorio
     *
     * @return il componente creato
     */
    private TextField creaCode() {
        fCode = (TextField) getField(Funzione_.code);

        fCode.setWidth("8em");
        fCode.setRequired(true);
        fCode.setRequiredError("Manca la sigla interna");

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
                syncCode();
            }// end of inner method
        });// end of anonymous inner class

        return fCode;
    }// end of method

    /**
     * Crea il campo sigla visibile, obbligatorio
     *
     * @return il componente creato
     */
    private TextField creaSigla() {
        fSigla = (TextField) getField(Funzione_.sigla);

        fSigla.setWidth("8em");
        fSigla.setRequired(true);
        fSigla.setRequiredError("Manca la sigla visibile");

        return fSigla;
    }// end of method


    /**
     * Crea il campo descrizione, obbligatorio
     *
     * @return il componente creato
     */
    private TextField creaDescrizione() {
        fDescrizione = (TextField) getField(Funzione_.descrizione);

        fDescrizione.setWidth("24em");
        fDescrizione.setRequired(true);
        fDescrizione.setRequiredError("Manca la descrizione");

        return fDescrizione;
    }// end of method

    /**
     * Crea il campo ordine, obbligatorio con inserimento automatico
     * Viene inserito in automatico e NON dal form
     *
     * @return il componente creato
     */
    private IntegerField creaOrdine() {
        fOrdine = (IntegerField) getField(Funzione_.ordine);

        fOrdine.setEnabled(false);
        if (!isNewRecord()) {
            fOrdine.setRequired(true);
        }// end of if cycle

        return fOrdine;
    }// end of method

    /**
     * Recupera il glifo dal field
     */
    @SuppressWarnings("all")
    private FontAwesome getGlyph() {
        FontAwesome fa = null;
        Field field = getBinder().getField(Funzione_.iconCodepoint.getName());
        int codepoint = 0;

        if (field != null) {
            codepoint = (int) field.getValue();
        }// fine del blocco if

        try { // prova ad eseguire il codice
            fa = FontAwesome.fromCodepoint(codepoint);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return fa;
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
    private void syncCode() {
        String codeCompany = getCodeCompany();
        String code = fCode.getValue();
        String codeCompanyUnico = codeCompany.toLowerCase() + code.toLowerCase();

        fCodeCompanyUnico.setValue(codeCompanyUnico);
        if (isNewRecord()) {
            fSigla.setValue(LibText.primaMaiuscola(code));
            fSigla.selectAll();
        }// end of if cycle

    }// end of method

    /**
     * Restituisce il codice della company selezionata
     */
    private String getCodeCompany() {
        if (isNewRecord()) {
            WamCompany company = WamCompany.find((long) fCompanyCombo.getValue());
            return company.getCompanyCode();
        } else {
            return fCompanyText.getValue();
        }// end of if/else cycle
    }// end of method

}// end of class
