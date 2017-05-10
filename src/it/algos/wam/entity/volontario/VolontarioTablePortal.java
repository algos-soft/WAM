package it.algos.wam.entity.volontario;

import it.algos.wam.entity.companyentity.WamTablePortal;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.toolbar.TableToolbar;

/**
 * Created by gac on 29 mag 2016.
 * .
 */
public class VolontarioTablePortal extends WamTablePortal {

    public VolontarioTablePortal(ModulePop modulo) {
        super(modulo);
    }// end of constructor

    /**
     * Creates the toolbar
     * Barra standard con 5 bottoni (nuovo, modifica, elimina, cerca, selezione)
     * Sovrascrivibile, per aggiungere/modificare bottoni
     */
    @Override
    public TableToolbar createToolbar() {
        toolbar = super.createToolbar();

        if (toolbar != null) {
            toolbar.setSearchButtonVisible(true);
            toolbar.setSelectButtonVisible(true);
        }// end of if cycle

        return toolbar;
    }// end of method


    /**
     * Shows in the table only the needed wamcompany
     * Creates a filter corresponding to the needed wamcompany in the table
     * I filtri sono comprensivi del livello sottostante (GreaterOrEqual)
     */
    protected void setFiltro(WamCompany company) {
        super.setFiltro(company);
        getTable().refresh();
    }// end of method


}// end of class
