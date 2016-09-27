package it.algos.wam.entity.funzione;

import com.vaadin.data.Item;
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
import it.algos.webbase.web.module.ModulePop;

/**
 * Created by alex on 18-04-2016.
 * .
 */
public class FunzioneForm extends ModuleForm {

    private Button iconButton;


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
        TextField fSigla = (TextField) getField(Funzione_.sigla);
        fSigla.setWidth("8em");
        fSigla.setRequired(true);
        fSigla.setRequiredError("Manca la sigla di codifica");
        fSigla.focus();
        TextField fCodeCompanyUnico = (TextField) getField(Funzione_.codeCompanyUnico);
        fCodeCompanyUnico.setWidth("16em");
        fCodeCompanyUnico.setEnabled(false);
        TextField fDescrizione = (TextField) getField(Funzione_.descrizione);
        fDescrizione.setWidth("24em");
        fDescrizione.setRequired(true);
        fDescrizione.setRequiredError("Manca la descrizione");
        IntegerField fOrdine = (IntegerField) getField(Funzione_.ordine);
        fOrdine.setEnabled(false);

        iconButton = new Button("Icona");
        iconButton.setHtmlContentAllowed(true);
        iconButton.addStyleName("verde");
        iconButton.addClickListener(new Button.ClickListener() {
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

        // selezione della company (solo per developer)
        if (LibSession.isDeveloper()) {
            // popup di selezione (solo per nuovo record)
            if (isNewRecord()) {
                RelatedComboField fCompany = (RelatedComboField) getField(CompanyEntity_.company);
                fCompany.setWidth("8em");
                fCompany.setRequired(true);
                fCompany.setRequiredError("Manca la company");
                layout.addComponent(fCompany);
            } else { // label fissa (solo per modifica record) NON si può cambiare (farebbe casino)
                BaseEntity entity = getEntity();
                WamCompany company = null;
                Funzione funz = null;
                if (entity != null && entity instanceof Funzione) {
                    funz = (Funzione) entity;
                    company = (WamCompany) funz.getCompany();
                    TextField fCompany = new TextField("Company", company.getCompanyCode());
                    fCompany.setWidth("8em");
                    fCompany.setEnabled(false);
                    fCompany.setRequired(true);
                    layout.addComponent(fCompany);
                    layout.addComponent(fCodeCompanyUnico);
                }// end of if cycle
            }// end of if/else cycle
        }// end of if cycle

        layout.addComponent(iconButton);
        layout.addComponent(fSigla);
        layout.addComponent(fDescrizione);
        if (!isNewRecord()) {
            fOrdine.setRequired(true);
            layout.addComponent(fOrdine);
        }// end of if cycle

        return layout;
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
        iconButton.setCaption("Scegli una icona...");
        FontAwesome glyph = getGlyph();
        if (glyph != null) {
            iconButton.setCaption(glyph.getHtml());
        }// end of if cycle
    }// end of method

}// end of class
