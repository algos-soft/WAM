package it.algos.wam.entity.companyentity;

import com.vaadin.data.Container;
import com.vaadin.event.Action;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;

/**
 * Created by gac on 07 ago 2016.
 * Sovrascrive la classe standard per eliminare i menu contestuali quando è loggato un utente semplice
 */
public class WamTable extends ETable {

    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public WamTable(ModulePop module) {
        super(module);
    }// end of constructor

    /**
     * Costruttore con caption
     *
     * @param module di riferimento (obbligatorio)
     */
    public WamTable(ModulePop module, String caption) {
        super(module);
        this.setCaption(caption);
    }// end of constructor

    /**
     * Initializes the table.
     * Must be called from the costructor in each subclass
     * Chiamato dal costruttore di ModuleTable
     */
    @Override
    protected void init() {
        super.init();

        setColumnReorderingAllowed(true);
        this.setColumnCollapsed(WamCompanyEntity_.company.getName(), CompanySessionLib.isCompanySet());

        fixSort();
        fixColumn();
    }// end of method

    protected void fixSort() {
    }// end of method


    protected void fixColumn() {
    }// end of method

    /**
     * Aggiunge il campo company
     * <p>
     * In caso di developer, aggiunge (a sinistra) la colonna della company
     * Aggiunge tutte le altre property, definite nella sottoclasse
     * Chiamato dalla sottoclasse
     */
    protected String[] addCompanyField(String... elenco) {
        ArrayList<Object> lista = new ArrayList<>();

        if (LibSession.isDeveloper()) {
            lista.add(WamCompanyEntity_.company);
        }// end of if cycle

        for (Object attr : elenco) {
            lista.add(attr);
        }// end of for cycle

        return lista.toArray(new String[lista.size()]);
    }// end of method

    /**
     * Return the Actions to display in contextual menu
     * <p>
     * Azioni standard della superclasse SOLO per lo sviluppatore
     * Azioni da decidere per l'admin
     * Nessun azione per gli utenti (solo visione della lista) - manca anche la footerBar
     */
    protected Action[] getActions(Object target, Object sender) {
        if (LibSession.isDeveloper()) {
            return super.getActions(target, sender);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


}// end of class
