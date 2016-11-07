package it.algos.wam.entity.funzione;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import it.algos.wam.entity.companyentity.EditorFunz;
import it.algos.wam.entity.companyentity.WanForm;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 18-04-2016.
 * Scheda personalizzata per la entity Funzione
 */
public class FunzioneForm extends WanForm {

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
     * Fields creati in maniera assolutamente automatica
     * Parte centrale del Form
     *
     * @return the component
     */
    @Override
    protected Component creaCompStandard(FormLayout layout) {

        layout.addComponent(bIcona);
        layout.addComponent(fCode);

        return super.creaCompStandard(layout);
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
                EditorFunz editor = new EditorFunz(form, null, false);
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
            List<Funzione> listaFunzioniDipenenti = getFunzione().getFunzioniDipendenti();
            for (Funzione funz : listaFunzioniDipenenti) {
                EditorFunz editor = new EditorFunz(this, funz, false);
                placeholderFunz.addComponent(editor);
                fEditors.add(editor);
            }// end of for cycle
        }// end of if cycle

        return placeholderFunz;
    }// end of method


    /**
     * Restituisce la funzione di questo Form
     */
    private Funzione getFunzione() {
        return (Funzione) super.getEntity();
    }// end of method


    @Override
    public void doDeleteFunz(EditorFunz editor) {
        placeholderFunz.removeComponent(editor);
        fEditors.remove(editor);
    }// end of method


    /**
     * Controlla se questa funzione è registrabile
     *
     * @return stringa vuota se registrabile, motivo se non registrabile
     */
    @Override
    protected String checkRegistrabile() {
        String err = "";
        Funzione funzione = getFunzione();
        ArrayList<String> lista = new ArrayList<>();

        //--codeCompanyUnico deve essere unico (per i nuovi record)
        if (isNewRecord()) {
            String codeCompanyUnico = fCodeCompanyUnico.getValue();
            if (Funzione.isEntityByCodeCompanyUnico(codeCompanyUnico)) {
                err += "Esiste già una funzione con questo code";
            }// end of if cycle
        }// end of if cycle

        //--le funzioni dipendenti non possono contenere la funzione principale
        if (!isNewRecord()) {
            for (Funzione funz : funzione.getFunzioniDipendenti()) {
                if (funzione.getCode().equals(funz.getCode())) {
                    err += "Una funzione dipendente è uguale a se stessa";
                    break;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        //--due (o più) funzioni dipendenti sono uguali
        for (Funzione funz : funzione.getFunzioniDipendenti()) {
            if (!lista.contains(funz.getCodeCompanyUnico())) {
                lista.add(funz.getCodeCompanyUnico());
            }// end of if cycle
        }// end of for cycle
        if (lista.size() < funzione.getFunzioniDipendenti().size()) {
            err += "Due funzioni dipendenti sono uguali";
        }// end of if cycle

        return err;
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

}// end of class
