package it.algos.wam.entity.turno;

import com.vaadin.data.Item;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

//
//import com.vaadin.data.Item;
//import com.vaadin.data.Property;
//import com.vaadin.server.FontAwesome;
//import com.vaadin.ui.*;
//import it.algos.wam.entity.funzione.*;
//import it.algos.wam.entity.wamcompany.WamCompany;
//import it.algos.webbase.multiazienda.CompanyEntity_;
//import it.algos.webbase.web.component.AHorizontalLayout;
//import it.algos.webbase.web.entity.BaseEntity;
//import it.algos.webbase.web.field.*;
//import it.algos.webbase.web.field.DateField;
//import it.algos.webbase.web.field.TextArea;
//import it.algos.webbase.web.field.TextField;
//import it.algos.webbase.web.form.AFormLayout;
//import it.algos.webbase.web.form.ModuleForm;
//import it.algos.webbase.web.lib.LibSession;
//import it.algos.webbase.web.lib.LibText;
//import it.algos.webbase.web.module.ModulePop;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by gac on 29 ott 2016.
// * Scheda personalizzata per la entity Turno (solo per developer)
// */
public class TurnoForm extends ModuleForm  {
//
//    //--Campi del form. Potrebbero essere variabili locali, ma così li 'vedo' meglio
//    private RelatedComboField fCompanyCombo;
//    private TextField fCompanyText;
//    private TextField fChiave;
//    private RelatedComboField fServizio;
//    private DateField fInizio;
//    private DateField fFine;
//    private TextField fTitolo;
//    private TextField fLocalita;
//    private TextArea fNote;
//
////    private ArrayList<EditorIscr> fEditors;
//
//
    /**
     * The form used to edit an item.
     * <p>
     * Invoca la superclasse passando i parametri:
     *
     * @param item   singola istanza della classe (obbligatorio in modifica e nullo per newRecord)
     * @param module di riferimento (obbligatorio)
     */
    public TurnoForm(Item item, ModulePop module) {
        super(item, module);
    }// end of constructor
//
//
//    /**
//     * Create the detail component (the upper part containing the fields).
//     * <p>
//     * Usa il FormLayout che ha le label a sinistra dei campi (standard)
//     * Se si vogliono le label sopra i campi, sovrascivere questo metodo e usare un VerticalLayout
//     * <p>
//     * Costruisco 3 diversi layout, disposti in verticale:
//     * 1- layoutDeveloper con la selezione (eventuale) della company ed il codeCompanyUnico
//     * 2- layoutForm standard per i campi normali
//     * 3- layoutFunzioni per il placehorder delle funzioni dipendenti
//     *
//     * @return the detail component containing the fields
//     */
//    @Override
//    protected Component createComponent() {
//        VerticalLayout layout = new VerticalLayout();
//        layout.setMargin(true);
//        layout.setSpacing(true);
//
//        //--crea prima tutti i fields
//        //--alcuni hanno delle particolarità aggiuntive
//        creaComponenti();
//
//        //--assembla i vari elementi grafici
//        layout.addComponent(creaCompDeveloper());
//        layout.addComponent(creaCompStandard());
//        layout.addComponent(creaCompFunzioni());
//
//        return layout;
//    }// end of method
//
//    /**
//     * Crea prima tutti i fields
//     * Alcuni hanno delle particolarità aggiuntive
//     */
//    private void creaComponenti() {
////        fEditors = new ArrayList<>();
////
////        this.creaCompany();
////        this.creaChiave();
////        this.creaSigla();
////        this.creaDescrizione();
////        this.creaOrdine();
////        this.creaBottoneNuova();
////        this.creaPlacehorderFunzioni();
//    }// end of method
//
//    /**
//     * Selezione della company (solo per developer)
//     * Prima parte in alto del Form
//     *
//     * @return the component
//     */
//    private Component creaCompDeveloper() {
//        VerticalLayout layout = new VerticalLayout();
//
//        if (isNewRecord()) {
//            layout.addComponent(new AHorizontalLayout(fCompanyCombo, fCodeCompanyUnico));
//        } else {
//            layout.addComponent(new AHorizontalLayout(fCompanyText, fCodeCompanyUnico));
//        }// end of if/else cycle
//
//        return layout;
//    }// end of method
//
//    /**
//     * Fields creati in maniera assolutamente automatica
//     * Parte centrale del Form
//     *
//     * @return the component
//     */
//    private Component creaCompStandard() {
//        FormLayout layout = new AFormLayout();
//
//        layout.addComponent(bIcona);
//        layout.addComponent(fCode);
//        layout.addComponent(fSigla);
//        layout.addComponent(fDescrizione);
//
//        if (!isNewRecord() && LibSession.isAdmin()) {
//            layout.addComponent(fOrdine);
//        }// end of if cycle
//
//        return layout;
//    }// end of method
//
//    /**
//     * Parte bassa del Form
//     *
//     * @return the component
//     */
//    private Component creaCompFunzioni() {
//        VerticalLayout layout = new VerticalLayout();
//
//        layout.addComponent(placeholderFunz);
//
//        return layout;
//    }// end of method
//
//    /**
//     * Selezione della company (solo per developer)
//     * Crea il campo company, obbligatorio
//     * Nel nuovo record è un ComboBox di selezione
//     * Nella modifica è un TextField
//     */
//    private void creaCompany() {
//        // popup di selezione (solo per nuovo record)
//        if (isNewRecord()) {
//            fCompanyCombo = (RelatedComboField) getField(CompanyEntity_.company);
//            fCompanyCombo.setWidth("8em");
//            fCompanyCombo.setRequired(true);
//            fCompanyCombo.setRequiredError("Manca la company");
//
//            fCompanyCombo.addValueChangeListener(new Property.ValueChangeListener() {
//                @Override
//                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
//                    syncPlaceholderFunz();
//                }// end of inner method
//            });// end of anonymous inner class
//
//            if (LibSession.isDeveloper() && fCompanyCombo.getValue() == null) {
//                fCompanyCombo.focus();
//            }// end of if cycle
//        } else { // label fissa (solo per modifica record) NON si può cambiare (farebbe casino)
//            fCompanyText = null;
//            BaseEntity entity = getEntity();
//            WamCompany company = null;
//            Funzione funz = null;
//            if (entity != null && entity instanceof Funzione) {
//                funz = (Funzione) entity;
//                company = (WamCompany) funz.getCompany();
//                fCompanyText = new TextField("Company", company.getCompanyCode());
//                fCompanyText.setWidth("8em");
//                fCompanyText.setEnabled(false);
//                fCompanyText.setRequired(true);
//            }// end of if cycle
//        }// end of if/else cycle
//    }// end of method
//
//    /**
//     * Crea il campo codeCompanyUnico, obbligatorio e unico
//     * Viene inserito in automatico e NON dal form
//     *
//     * @return il componente creato
//     */
//    private TextField creaCodeCompany() {
//        fCodeCompanyUnico = (TextField) getField(Funzione_.codeCompanyUnico);
//        return fCodeCompanyUnico;
//    }// end of method
//
//
//
//    /**
//     * Crea il campo sigla interna, obbligatorio
//     *
//     * @return il componente creato
//     */
//    private TextField creaCode() {
//        fCode = (TextField) getField(Funzione_.code);
//
//        if (isNewRecord()) {
//            if (LibSession.isDeveloper()) {
//                if (fCompanyCombo.getValue() != null) {
//                    fCode.focus();
//                }// end of if cycle
//            } else {
//                fCode.focus();
//            }// end of if/else cycle
//        } else {
//            fCode.focus();
//            fCode.selectAll();
//        }// end of if/else cycle
//
//        fCode.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
//                syncCode();
//            }// end of inner method
//        });// end of anonymous inner class
//
//        return fCode;
//    }// end of method
//
//    /**
//     * Crea il campo sigla visibile, obbligatorio
//     *
//     * @return il componente creato
//     */
//    private TextField creaSigla() {
//        fSigla = (TextField) getField(Funzione_.sigla);
//        return fSigla;
//    }// end of method
//
//
//    /**
//     * Crea il campo descrizione, obbligatorio
//     *
//     * @return il componente creato
//     */
//    private TextField creaDescrizione() {
//        fDescrizione = (TextField) getField(Funzione_.descrizione);
//        return fDescrizione;
//    }// end of method
//
//
//    /**
//     * Recupera il glifo dal field
//     */
//    @SuppressWarnings("all")
//    private FontAwesome getGlyph() {
//        FontAwesome fa = null;
//        Field field = getBinder().getField(Funzione_.iconCodepoint.getName());
//        int codepoint = 0;
//
//        if (field != null) {
//            codepoint = (int) field.getValue();
//        }// fine del blocco if
//
//        try { // prova ad eseguire il codice
//            fa = FontAwesome.fromCodepoint(codepoint);
//        } catch (Exception unErrore) { // intercetta l'errore
//        }// fine del blocco try-catch
//
//        return fa;
//    }// end of method
//
//    /**
//     * Registra un glifo nel field
//     */
//    @SuppressWarnings("unchecked")
//    private void setGlyph(FontAwesome glyph) {
//        Field field = getBinder().getField(Funzione_.iconCodepoint.getName());
//
//        if (field != null) {
//            if (glyph != null) {
//                field.setValue(glyph.getCodepoint());
//            } else {
//                field.setValue(0);
//            }// end of if/else cycle
//        }// fine del blocco if
//
//    }// end of method
//
//    /**
//     * Sincronizza l'icona del bottone con il codepoint contenuto nel field
//     */
//    private void syncButton() {
//        bIcona.setCaption("Scegli una icona...");
//        FontAwesome glyph = getGlyph();
//        if (glyph != null) {
//            bIcona.setCaption(glyph.getHtml());
//        }// end of if cycle
//    }// end of method
//
//    /**
//     * Sincronizza il codeCompanyUnico e suggerisce la sigla
//     */
//    private void syncCode() {
//        String codeCompanyUnico = LibText.creaChiave(getCompany(), fCode.getValue());
//
//        fCodeCompanyUnico.setValue(codeCompanyUnico);
//        if (isNewRecord()) {
//            fSigla.setValue(LibText.primaMaiuscola(fCode.getValue()));
//            fSigla.selectAll();
//        }// end of if cycle
//
//    }// end of method
//
//
//    /**
//     * Restituisce la company di questo Form
//     */
//    public WamCompany getCompany() {
//        WamCompany company = null;
//
//        if (isNewRecord()) {
//            Object obj = fCompanyCombo.getValue();
//            if (obj != null && obj instanceof Long) {
//                long companyID = (long) obj;
//                company = WamCompany.find(companyID);
//            }// end of if cycle
//        } else {
//            company = WamCompany.findByCode(fCompanyText.getValue());
//        }// end of if/else cycle
//
//        return company;
//    }// end of method
//
//    /**
//     * Restituisce la funzione di questo Form
//     */
//    private Funzione getFunzione() {
//        return (Funzione) super.getEntity();
//    }// end of method
//
//    private void syncPlaceholderFunz() {
//        if (LibSession.isDeveloper()) {
//            placeholderFunz.setVisible(fCompanyCombo.getValue() != null);
//            bNuova.setVisible(fCompanyCombo.getValue() != null);
//        } else {
//            bNuova.setVisible(true);
//        }// end of if/else cycle
//    }// end of method
//
//    /**
//     * Crea il placeholder per le funzioni dipendenti
//     *
//     * @return il componente creato
//     */
//    private VerticalLayout creaPlacehorderFunzioni() {
//        placeholderFunz = new VerticalLayout();
//        placeholderFunz.setCaption("Funzioni dipendenti da questa");
//        placeholderFunz.setSpacing(true);
//        placeholderFunz.addComponent(bNuova);
//
//        if (isNewRecord()) {
//            placeholderFunz.setVisible(this.getCompany() != null);
//        }// end of if cycle
//
//        // aggiunge gli editor per le funzioni esistenti
//        if (!isNewRecord()) {
//            List<Funzione> listaFunzioniDipenenti = getFunzione().getFunzioniDipendenti();
//            for (Funzione funz : listaFunzioniDipenenti) {
//                EditorFunz editor = new EditorFunz(this, funz, false);
//                placeholderFunz.addComponent(editor);
//                fEditors.add(editor);
//            }// end of for cycle
//        }// end of if cycle
//
//        return placeholderFunz;
//    }// end of method
//
//    /**
//     * Crea un bottone per creare nuove funzioni
//     *
//     * @return il componente creato
//     */
//    private Button creaBottoneNuova() {
//        bNuova = new Button("Aggiungi funzione", FontAwesome.PLUS_CIRCLE);
//        FunzioneForm form = this;
//
//        bNuova.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                EditorFunz editor = new EditorFunz(form, null, false);
//                placeholderFunz.addComponent(editor);
//                fEditors.add(editor);
//            }// end of inner method
//        });// end of anonymous inner class
//
//        return bNuova;
//    }// end of method
//
//    @Override
//    public void doDelete(EditorFunz editor) {
//        placeholderFunz.removeComponent(editor);
//        fEditors.remove(editor);
//    }// end of method
//
//    private void regolaEditorFunz() {
//        Funzione funzioneMadre = getFunzione();
//        Funzione funzFiglia;
//        List<Funzione> funzioniDipendenti = new ArrayList<>();
//
//        for (EditorFunz editor : fEditors) {
//            funzFiglia = editor.getFunzione();
//            funzioniDipendenti.add(funzFiglia);
//        }// end of for cycle
//
//        funzioneMadre.setFunzioniDipendenti(funzioniDipendenti);
//
//    }// end of method
//
//    /**
//     * Sincronizza le funzioni dipendenti esistenti: modifica quelle esistenti,
//     * cancella quelle inesistenti e crea quelle nuovi.
//     */
//    private void syncFunzioni() {
//        // modifica quelli esistenti e aggiunge i nuovi
////        for (EditorFunz editor : fEditors) {
////            Funzione funz = editor.getFunzione();
////            // aggiorna l'entity dall'editor
////            sf.setFunzione(editor.getFunzione());
////            sf.setObbligatoria(editor.isObbligatoria());
////
////            // se nuovo, lo aggiunge al servizio
////            if (sf.getId() == null) {
////                getServizio().add(sf);
////            }
////        }
//
////        // Solo se non sono nuovi:
////        // cancella quelli inesistenti nell'editor (sono stati cancellati).
////        // Dato che elimina elementi della stessa lista che viene iterata, esegue
////        // l'iterazione partendo dal fondo
////        List<ServizioFunzione> lista = getServizio().getServizioFunzioniOrd();
////        int dim = lista.size();
////        for (int i = dim - 1; i >= 0; i--) {
////            ServizioFunzione sf = lista.get(i);
////            if (sf.getId() != null) {   // non considera quelli senza id, sono i nuovi!
////                boolean found = false;
////
////                for (EditorSF editor : sfEditors) {
////                    if (editor.getServizioFunzione() != null) {
////                        if (editor.getServizioFunzione().equals(sf)) {
////                            found = true;
////                            break;
////                        }// end of if cycle
////                    }// end of if cycle
////                }// end of for cycle
////
////                if (!found) {
////                    lista.remove(i);
////                }// end of if cycle
////            }// end of if cycle
////            getServizio().setServizioFunzioni(lista);
////        }// end of for cycle
//
//    }// end of method
//
//
//    @Override
//    protected boolean save() {
//        regolaEditorFunz();
////        uiToServizio();
//        return super.save();
//    }// end of method
//
//    @Override
//    public void postCommit() {
//        //        regolaEditorFunz();
////        regolaEditorFunz();
//        super.postCommit();
//    }// end of method
//
}// end of class
