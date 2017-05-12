package it.algos.wam.entity.companyentity;

import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.toolbar.TableToolbar;

/**
 * Created by gac on 07/04/17.
 * Estende la superclasse per eliminare tutti i bottoni/menu salvo quelli della ricerca e selezione
 */
public class WamTablePortalSoloRicerca extends WamTablePortal {

    /**
     * Costruttore base
     */
    public WamTablePortalSoloRicerca(ModulePop modulo) {
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
            toolbar.setCreateButtonVisible(false);
            toolbar.setEditButtonVisible(false);
            toolbar.setDeleteButtonVisible(false);
            toolbar.setSearchButtonVisible(true);
            toolbar.setSelectButtonVisible(true);
        }// end of if cycle

        return toolbar;
    }// end of method

}// end of class
