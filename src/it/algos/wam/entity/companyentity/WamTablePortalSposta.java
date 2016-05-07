package it.algos.wam.entity.companyentity;

import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.toolbar.TableToolbar;

/**
 * Created by gac on 07 mag 2016.
 * Sovrascrive la classe standard per aggiungere due bottone/menu di spostamento dei records
 */
public class WamTablePortalSposta extends WamTablePortal {

    public WamTablePortalSposta(ModulePop modulo) {
        super(modulo);
    }// end of constructor

    public TableToolbar createToolbar() {
        super.setUsaBottoniSpostamento(true);
        return super.createToolbar();
    }// end of method

}// end of class
