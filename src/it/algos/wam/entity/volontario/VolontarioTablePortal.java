package it.algos.wam.entity.volontario;

import it.algos.wam.entity.companyentity.WamTablePortal;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.module.ModulePop;

/**
 * Created by gac on 29 mag 2016.
 * .
 */
public class VolontarioTablePortal extends WamTablePortal {

    public VolontarioTablePortal(ModulePop modulo) {
        super(modulo);
    }// end of constructor

//    /**
//     * Sincronizza la company selezionata
//     * <p>
//     * Modificata la selezione della company.
//     * Regola il filtro sulla company
//     * Sincronizza lo stato dei bottoni.
//     * Regola la company della sessione
//     */
//    public void syncCompany(WamCompany companyNew) {
//        this.removeAllComponents();
//        this.table = getModule().createTable();
//        super.init();
//    }// end of method


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
