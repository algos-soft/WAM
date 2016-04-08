package it.algos.wam.entity.funzione;

import com.vaadin.data.Container;
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.module.ModulePop;

/**
 * Created by alex on 08/04/16.
 */
public class FunzioneTable extends ETable {


    public FunzioneTable(ModulePop module) {
        super(module);
        Container cont = getContainerDataSource();
        if(cont instanceof Sortable){
            Sortable sortable = (Sortable)cont;
            sortable.sort(new Object[]{Funzione_.ordine.getName()}, new boolean[]{true});
        }

    }

    protected Object[] getDisplayColumns() {
        return new Object[]{
                Funzione_.sigla,
                Funzione_.descrizione,
                Funzione_.note,
        };


    }// end of method

    @Override
    protected void init() {
        super.init();

        setColumnReorderingAllowed(true);
        setSortEnabled(false);

        setColumnExpandRatio(Funzione_.sigla, 1);
        setColumnExpandRatio(Funzione_.descrizione, 2);
        setColumnExpandRatio(Funzione_.note, 2);

    }



}
