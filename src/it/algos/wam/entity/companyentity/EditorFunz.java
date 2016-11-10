package it.algos.wam.entity.companyentity;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Not;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.lib.LibWam;
import it.algos.webbase.multiazienda.ERelatedComboField;

/**
 * Created by gac on 19 ott 2016.
 * Editor di una singola funzione dipendente della funzione
 * Layout orizzontale con:
 * - icona
 * - popup delle funzioni della company
 * - bottone di cancellazione
 */
public class EditorFunz extends EditorWam {

//    private Funzione funzione;


    public EditorFunz(FunzioneListener formChiamante) {
        this(formChiamante, (Funzione) null);
    }// end of constructor

    public EditorFunz(FunzioneListener formChiamante, Funzione funzione) {
        super(formChiamante,funzione);
        this.init();
    }// end of constructor

    protected void init() {
        //--crea prima tutti i componenti
        super.init();

        //--assembla i vari elementi grafici
        addComponent(bIcona);
        addComponent(comboFunzioni);
        addComponent(bElimina);
    }// end of method

    /**
     * Crea il bottone per la selezione dell'icona
     */
    protected void creaBottoneIcona() {
        super.creaBottoneIcona();
        setIconButton(funzione);
    }// end of method

    /**
     * Crea il combo di selezione della funzione
     * Elimina la funzione madre nel comboBox delle funzioni dipendenti
     */
    protected void creaComboBox() {
        comboFunzioni = new ERelatedComboField(Funzione.class, formChiamante.getCompany());

        comboFunzioni.sort(Funzione_.sigla);
        comboFunzioni.setWidth("25em");
        comboFunzioni.setDescription("Funzione dipendente che viene abilitata automaticamente per il volontario");
        comboFunzioni.setNullSelectionAllowed(false);

        // escludi la funzione corrente
        Container.Filter filter = new Compare.Equal(Funzione_.id.getName(), formChiamante.getFunzione().getId());
        comboFunzioni.getFilterableContainer().addContainerFilter(new Not(filter));

        if (funzione != null) {
            comboFunzioni.setValue(funzione.getId());
        }// end of if cycle

        comboFunzioni.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
                setIconButton(LibWam.getFunzione(event));
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

    /**
     * Assegna un'icona al bottone
     */
    private void setIconButton(Funzione funz) {
        if (funz != null) {
            bIcona.setCaption(funz.getIconHtml());
        } else {
            bIcona.setCaption("");
        }// end of if/else cycle
    }// end of method

    /**
     * Eliminazione effettiva di questo componente
     * La relativa Funzione, deve essere regolata in FunzioneForm
     * La relativa ServizioFunzione, deve essere regolata in ServizioForm
     */
    protected void doDelete() {
        formChiamante.doDeleteFunz(this);
    }// end of method


}// end of class
