package it.algos.wam.entity.funzione;

import it.algos.wam.entity.companyentity.WamTablePortal;
import it.algos.wam.entity.companyentity.WamTablePortalSposta;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;

import javax.persistence.EntityManager;

/**
 * Created by alex on 08/04/16.
 * .
 */
public class FunzioneTablePortal extends WamTablePortalSposta {

    public FunzioneTablePortal(ModulePop modulo) {
        super(modulo);
    }// end of constructor


    /**
     * Spostamento in su del singolo record.
     * Sovrascritto
     */
    @Override
    protected void spostaSu() {
        Funzione funz = (Funzione) table.getSelectedEntity();
        Funzione adiacente = WamQuery.queryFunzioneAdiacente(table.getEntityManager(), funz, true);

        if (funz != null && adiacente != null) {
            swap(funz, adiacente);
        }// end of if cycle
    }// end of method

    /**
     * Spostamento in giu del singolo record.
     * Sovrascritto
     */
    @Override
    protected void spostaGiu() {
        Funzione funz = (Funzione) table.getSelectedEntity();
        Funzione adiacente = WamQuery.queryFunzioneAdiacente(table.getEntityManager(), funz, false);

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

        EntityManager manager = table.getEntityManager();
        manager.getTransaction().begin();
        try {
            manager.merge(f1);
            manager.merge(f2);
            manager.getTransaction().commit();
            table.refresh();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }

    }// end of method


}// end of class
