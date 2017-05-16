package it.algos.wam.entity.companyentity;

import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.toolbar.TableToolbar;

/**
 * Created by gac on 14/05/17.
 * .
 */
public class WamTablePortalEditDeveloper extends WamTablePortal{

    /**
     * Costruttore base
     */
    public WamTablePortalEditDeveloper(ModulePop modulo) {
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
        toolbar = super.createToolbar();

        if (toolbar != null) {
            if (developer) {
                toolbar.setCreateButtonVisible(true);
                toolbar.setEditButtonVisible(true);
                toolbar.setDeleteButtonVisible(true);
            } else {
                toolbar.setCreateButtonVisible(false);
                toolbar.setEditButtonVisible(false);
                toolbar.setDeleteButtonVisible(false);
            }// end of if/else cycle
            toolbar.setSearchButtonVisible(true);
            toolbar.setSelectButtonVisible(true);
        }// end of if cycle

        return toolbar;
    }// end of method

}// end of class
