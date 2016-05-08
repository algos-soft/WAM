package it.algos.wam.entity.servizio;

import it.algos.wam.entity.companyentity.WamTablePortalSposta;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.EntityManager;

/**
 * Created by alex on 7-04-2016.
 * .
 */
public class ServizioTablePortal extends WamTablePortalSposta {

    public ServizioTablePortal(ModulePop modulo) {
        super(modulo);
    }

//    public TableToolbar createToolbar() {
//        return new ServizioTableToolbar(getTable());
//    }

    /**
     * Spostamento in su del singolo record.
     * Sovrascritto
     */
    @Override
    protected void spostaSu() {
        Servizio serv = (Servizio) table.getSelectedEntity();
        Servizio adiacente = WamQuery.queryServizioAdiacente(table.getEntityManager(), serv, true);

        if (serv != null && adiacente != null) {
            swap(serv, adiacente);
        }// end of if cycle
    }// end of method

    /**
     * Spostamento in giu del singolo record.
     * Sovrascritto
     */
    @Override
    protected void spostaGiu() {
        Servizio serv = (Servizio) table.getSelectedEntity();
        Servizio adiacente = WamQuery.queryServizioAdiacente(table.getEntityManager(), serv, false);

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

        EntityManager manager = table.getEntityManager();
        manager.getTransaction().begin();
        try {
            manager.merge(s1);
            manager.merge(s2);
            manager.getTransaction().commit();
            table.refresh();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }

    }

    /**
     * Shows in the table only the needed wamcompany
     * Creates a filter corresponding to the needed wamcompany in the table
     * I filtri sono comprensivi del livello sottostante (GreaterOrEqual)
     */
    protected void setFiltro(WamCompany company) {
        super.setFiltro(company);
        getTable().setColumnCollapsed(Servizio_.ordine.getName(), useAllCompany);
        getTable().refresh();
    }// end of method

}// end of class
