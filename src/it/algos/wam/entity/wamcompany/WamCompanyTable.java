package it.algos.wam.entity.wamcompany;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.company.BaseCompany_;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ModuleTable;
import org.vaadin.addons.lazyquerycontainer.LazyEntityContainer;

/**
 * Created by gac on 14/05/17.
 * Tavola normale per il developer
 * Tavola con un solo record per l'admin. Unico bottone 'Modifica'
 */
public class WamCompanyTable extends ModuleTable {

    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public WamCompanyTable(ModulePop module) {
        super(module);
    }// end of constructor

    /**
     * Initializes the table.
     * Must be called from the costructor in each subclass
     * Chiamato dal costruttore di ModuleTable
     */
    @Override
    protected void init() {
        super.init();
        fixColumn();
    }// end of method

    /**
     * Creates the container
     * <p>
     * The id property is added here by default.
     *
     * @return the container
     */
    @Override
    public Container createContainer() {
        String companyCode = CompanySessionLib.getCompanyCode();

        if (LibSession.isDeveloper()) {
            return super.createContainer();
        } else {
            return super.createContainer(new Compare.Equal(BaseCompany_.companyCode.getName(), companyCode));
        }// end of if/else cycle
    }// end of method

    /**
     * Returns an array of the visible columns ids. Ids might be of type String
     * or Attribute.<br>
     * This implementations returns all the columns (no order).
     *
     * @return the list
     */
    @Override
    protected Object[] getDisplayColumns() {
        if (LibSession.isDeveloper()) {
            return new Object[]{
                    BaseCompany_.companyCode,
                    WamCompany_.organizzazione,
                    BaseCompany_.name,
                    WamCompany_.presidente,
                    BaseCompany_.contact,
                    BaseCompany_.address1,
            };// end of array
        } else {
            return new Object[]{
                    WamCompany_.organizzazione,
                    BaseCompany_.name,
                    WamCompany_.presidente,
                    BaseCompany_.contact,
                    BaseCompany_.address1,
            };// end of array
        }// end of if/else cycle
    }// end of method

    private void fixColumn() {
        setColumnHeader(WamCompany_.organizzazione, "Organizzazione");
        setColumnHeader(BaseCompany_.name, "Nome");
        setColumnHeader(WamCompany_.presidente, "Presidente");
        setColumnHeader(BaseCompany_.contact, "Contatto");
        setColumnHeader(BaseCompany_.address1, "Indirizzo");
    }// end of method

}// end of class
