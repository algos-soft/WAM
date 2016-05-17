package it.algos.wam.entity.companyentity;

import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.toolbar.TableToolbar;

/**
 * Created by gac on 07 mag 2016.
 * Sovrascrive la classe standard per aggiungere due bottone/menu di spostamento dei records
 */
public class WamTablePortalSposta extends WamTablePortal implements ATable.SelectionChangedListener{

    public WamTablePortalSposta(ModulePop modulo) {
        super(modulo);
    }// end of constructor

    public TableToolbar createToolbar() {
        super.setUsaBottoniSpostamento(true);
        return super.createToolbar();
    }// end of method

    /**
     * Cambiata la selezione delle righe.
     * Possibilità di modificare l'aspetto (e la funzionalità) dei bottoni, eventualmente disabilitandoli
     */
    @Override
    public void selectionChanged(ATable.SelectionChangeEvent e) {
        syncButtons(e.isSingleRowSelected(), e.isMultipleRowsSelected());
    }// end of method

}// end of class
