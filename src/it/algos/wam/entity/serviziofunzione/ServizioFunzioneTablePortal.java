package it.algos.wam.entity.serviziofunzione;

import it.algos.wam.entity.companyentity.WamTablePortal;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.toolbar.TableToolbar;

/**
 * Created by gac on 05/04/17.
 * .
 */
public class ServizioFunzioneTablePortal extends WamTablePortal {

    /**
     * Costruttore base
     */
    public ServizioFunzioneTablePortal(ModulePop modulo) {
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

        toolbar.setCreate(false);
        toolbar.setCreateButtonVisible(false);
        toolbar.setDeleteButtonVisible(false);
        toolbar.setEditButtonVisible(false);
        toolbar.setSelectButtonVisible(true);

        return toolbar;
    }// end of method

}
