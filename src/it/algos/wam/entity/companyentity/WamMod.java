package it.algos.wam.entity.companyentity;

import com.vaadin.server.Resource;
import com.vaadin.ui.Table;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;

/**
 * Modulo astratto per implementare la creazione della WamTablePortal
 * Si interpone come layer tra le classi Mod che usano la property Company e la superclasse standard ModulePop
 */
public abstract class WamMod extends CompanyModule implements CompanyListener, ModulePop.RecordSavedListener, ModulePop.RecordDeletedListener {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    /**
     * Costruttore
     *
     * @param entity   di riferimento del modulo
     * @param menuIcon icona del menu
     */
    public WamMod(Class entity, Resource menuIcon) {
        this(entity, "", menuIcon);
    }// end of constructor

    /**
     * Costruttore
     *
     * @param entity    di riferimento del modulo
     * @param menuLabel etichetta visibile nella menu bar
     * @param menuIcon  icona del menu
     */
    public WamMod(Class entity, String menuLabel, Resource menuIcon) {
        super(entity, menuLabel, menuIcon);
//        getTable().setRowHeaderMode(Table.RowHeaderMode.INDEX);
    }// end of constructor

    /**
     * Aggiunge il campo company
     * <p>
     * in caso di developer, aggiunge (a sinistra) la colonna della company
     * aggiunge tutte le altre property, definite nella sottoclasse
     * Chiamato dalla sottoclasse
     */
    protected Attribute<?, ?>[] addCompanyField(Attribute... elenco) {
        ArrayList<Attribute> lista = new ArrayList<>();

        if (LibSession.isDeveloper()) {
            lista.add(WamCompanyEntity_.company);
        }// end of if cycle

        for (Attribute attr : elenco) {
            lista.add(attr);
        }// end of for cycle

        return lista.toArray(new Attribute[lista.size()]);
    }// end of method

    /**
     * Crea una Table già filtrata sulla company corrente
     * The concrete subclass must override for a specific Table.
     * @return the Table
     */
    @Override
    public ATable createTable() {
        return new WamTable(this);
    }// end of method
    /**
     * Create the Table Portal
     * <p>
     * in caso di developer, portale specifico col menu di selezione della company
     * in caso di admin e utente normale, portale standard
     *
     * @return the TablePortal
     */
    @Override
    public TablePortal createTablePortal() {
        return new WamTablePortal(this);
    }// end of method

    @Override
    public void search() {
        super.search();
    }// end of method

    @Override
    public void companyAdded(WamCompany company) {
//        ((WamTablePortal) getTablePortal()).addCompany(company);
    }// end of method

    @Override
    public void companyRemoved(WamCompany company) {
//        ((WamTablePortal) getTablePortal()).deleteCompany(company);
    }// end of method

    @Override
    public void companyChanged(WamCompany company) {
        ((WamTablePortal) getTablePortal()).syncCompany(company);
    }// end of method

    @Override
    public void recordDeleted(RecordEvent e) {

    }// end of method

    @Override
    public void recordCreated(RecordEvent e) {

    }// end of method

    @Override
    public void recordSaved(RecordEvent e) {

    }// end of method

    /**
     * Invoked when table data changes
     */
    protected void tableDataChanged() {
        String infoStandard;
        BaseCompany croce = CompanySessionLib.getCompany();
        super.tableDataChanged();

        if (LibSession.isDeveloper() && croce != null) {
            TablePortal tablePortal = getTablePortal();
            if (tablePortal != null) {
                TableToolbar tableToolbar = tablePortal.getToolbar();
                if (tableToolbar != null) {
                    infoStandard = tableToolbar.getInfoText();
                    tableToolbar.setInfoText(infoStandard + " della croce " + croce.getName());
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

    }// end of method

}// end of class
