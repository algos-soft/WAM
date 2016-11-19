package it.algos.wam.entity.servizio;

import it.algos.wam.entity.companyentity.WamTablePortalSposta;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.web.module.ModulePop;

/**
 * Created by alex on 7-04-2016.
 * .
 */
public class ServizioTablePortal extends WamTablePortalSposta {


    public ServizioTablePortal(ModulePop modulo) {
        super(modulo);
    }// end of constructor

    /**
     * Spostamento effettivo, in su o in giu del singolo record.
     * Sovrascritto
     *
     * @param sopra true per spostare in alto, false per spostare in basso
     */
    public void spostaRecord(boolean sopra) {
        Servizio serv = (Servizio) table.getSelectedEntity();
        Servizio adiacente = WamQuery.queryServizioAdiacente(table.getEntityManager(), serv, sopra);

        if (serv != null && adiacente != null) {
            swap(serv, adiacente);
        }// end of if cycle
    }// end of method

    /**
     * Scambia il numero d'ordine di due servizi
     */
    private void swap(Servizio s1, Servizio s2) {
        int o1 = s1.getOrdine();
        int o2 = s2.getOrdine();

        s1.setOrdine(o2);
        s2.setOrdine(o1);

        super.swapCommit(s1, s2);
    }// end of method


    /**
     * Shows in the table only the needed wamcompany
     * Creates a filter corresponding to the needed wamcompany in the table
     * I filtri sono comprensivi del livello sottostante (GreaterOrEqual)
     */
    protected void setFiltro(WamCompany company) {
        super.setFiltro(company);
        table.setColumnCollapsed(Funzione_.ordine.getName(), useAllCompany);
        getTable().refresh();
    }// end of method

}// end of class
