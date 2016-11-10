package it.algos.wam.entity.companyentity;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.webbase.multiazienda.ERelatedComboField;

/**
 * Created by gac on 10 nov 2016.
 * Editor di una singola funzione
 * Layout orizzontale con:
 * - icona
 * - popup delle funzioni della company
 * - checkbox di obbligatorietà (in una sottoclasse, non nell'altra)
 * - bottone di cancellazione
 */
public abstract class EditorWam extends HorizontalLayout {

    protected Button bIcona;
    protected ERelatedComboField comboFunzioni;
    protected CheckBox checkObbligatorio;
    protected Button bElimina;
    protected WamListener formChiamante;


    public EditorWam(FunzListener formChiamante) {
        this.formChiamante = formChiamante;
    }// end of constructor

    public EditorWam(ServFunzListener formChiamante) {
        this.formChiamante = formChiamante;
    }// end of constructor


    protected void init() {
        setSpacing(true);

        //--crea prima tutti i componenti
        creaComponenti();

        //--assembla i vari elementi grafici
        addComponent(bIcona);
        addComponent(comboFunzioni);
        if (checkObbligatorio != null) {
            addComponent(checkObbligatorio);
        }// end of if cycle
        addComponent(bElimina);

    }// end of method

    protected void creaComponenti() {
        this.creaBottoneIcona();
        this.creaComboBox();
        this.creaBottoneElimina();
    }// end of method

    /**
     * Crea il bottone per la selezione dell'icona
     */
    protected void creaBottoneIcona() {
        bIcona = new Button();
        bIcona.setHtmlContentAllowed(true);
        bIcona.addStyleName("blue");
        bIcona.setWidth("3em");
        bIcona.setDescription("Icona grafica rappresentativa della funzione");
    }// end of method

    /**
     * Crea il combo di selezione della funzione
     * Elimina la funzione madre nel comboBox delle funzioni dipendenti
     */
    protected void creaComboBox() {
    }// end of method


    /**
     * Crea il bottone per eliminare la funzione
     */
    protected void creaBottoneElimina() {
        bElimina = new Button("", FontAwesome.TRASH_O);
        bElimina.setDescription("Elimina la funzione dipendente");
    }// end of method


    /**
     * Eliminazione effettiva di questo componente
     * La relativa Funzione, deve essere regolata in FunzioneForm
     * La relativa ServizioFunzione, deve essere regolata in ServizioForm
     */
    protected void doDelete() {
    }// end of method


    /**
     * Ritorna la funzione correntemente selezionata nel popup
     *
     * @return la funzione selezionata
     */
    public Funzione getFunzione() {
        Object obj;
        Funzione funz = null;

        obj = comboFunzioni.getSelectedBean();
        if (obj != null && obj instanceof Funzione) {
            funz = (Funzione) obj;
        }// end of if cycle

        return funz;
    }// end of method

//    public boolean usaFieldObbligatorio() {
//        return false;
//    }// end of method

}// end of class
