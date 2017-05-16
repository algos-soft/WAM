package it.algos.wam.entity.volontario;

import it.algos.wam.entity.companyentity.WamTablePortal;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
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
        boolean developer = LibSession.isDeveloper();
        boolean admin = LibSession.isAdmin();
        toolbar = super.createToolbar();

        if (toolbar != null) {
            if (developer) {
                toolbar.setCreateButtonVisible(true);
                toolbar.setEditButtonVisible(true);
                toolbar.setDeleteButtonVisible(true);
            } else {
                if (admin) {
                    toolbar.setCreateButtonVisible(true);
                    toolbar.setEditButtonVisible(true);
                    toolbar.setDeleteButtonVisible(false);
                } else {
                    toolbar.setCreateButtonVisible(false);
                    toolbar.setEditButtonVisible(false);
                    toolbar.setDeleteButtonVisible(false);
                }// end of if/else cycle
            }// end of if/else cycle
            toolbar.setSearchButtonVisible(true);
            toolbar.setSelectButtonVisible(true);
        }// end of if cycle

        return toolbar;
    }// end of method


}// end of class
