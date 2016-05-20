package it.algos.wam.entity.funzione;

import it.algos.wam.entity.companyentity.WamTablePortalSposta;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.web.module.ModulePop;

/**
 * Created by alex on 08/04/16.
 * .
 */
public class FunzioneTablePortal extends WamTablePortalSposta {


    public FunzioneTablePortal(ModulePop modulo) {
        super(modulo);
    }// end of constructor


    /**
     * Spostamento effettivo, in su o in giu del singolo record.
     * Sovrascritto
     *
     * @param sopra true per spostare in alto, false per spostare in basso
     */
    protected void spostaRecord(boolean sopra) {
        Funzione funz = (Funzione) table.getSelectedEntity();
        Funzione adiacente = WamQuery.queryFunzioneAdiacente(table.getEntityManager(), funz, sopra);

        if (funz != null && adiacente != null) {
            swap(funz, adiacente);
        }// end of if cycle
    }// end of method


    /**
     * Scambia il numero d'ordine di due record
     */
    private void swap(Funzione f1, Funzione f2) {
        int o1 = f1.getOrdine();
        int o2 = f2.getOrdine();

        f1.setOrdine(o2);
        f2.setOrdine(o1);

        super.swapCommit(f1, f2);
    }// end of method


    /**
     * Shows in the table only the needed wamcompany
     * Creates a filter corresponding to the needed wamcompany in the table
     * I filtri sono comprensivi del livello sottostante (GreaterOrEqual)
     */
    protected void setFiltro(WamCompany company) {
        super.setFiltro(company);
        getTable().setColumnCollapsed(Funzione_.ordine.getName(), useAllCompany);
        getTable().refresh();
    }// end of method


}// end of class
