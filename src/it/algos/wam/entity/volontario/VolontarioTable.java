package it.algos.wam.entity.volontario;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.data.Container;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import org.vaadin.addons.lazyquerycontainer.LazyEntityContainer;

import java.util.ArrayList;

/**
 * Created by gac on 25 mag 2016.
 * .
 */
public class VolontarioTable extends ETable {

    private ArrayList<String> colonne = new ArrayList();

    public VolontarioTable(ModulePop module) {
        super(module);
        Container cont = getContainerDataSource();
        if (cont instanceof Container.Sortable) {
            Container.Sortable sortable = (Container.Sortable) cont;
            sortable.sort(new Object[]{Volontario_.cognome.getName()}, new boolean[]{true});
        }// end of if cycle
    }// end of constructor


    protected Object[] getDisplayColumns() {
        ArrayList<Funzione> listaFunzioni= Funzione.findAll();

        if (LibSession.isDeveloper()) {
            return new Object[]{
                    WamCompanyEntity_.company,
                    Volontario_.nome,
                    Volontario_.cognome
            };
        } else {
            return new Object[]{
                    Volontario_.nome,
                    Volontario_.cognome
            };
        }// end of if/else cycle
    }// end of method

    /**
     * Refreshes the underlying container from the database
     */
    @Override
    public void refresh() {
        int a=87;
    }// end of method

}// end of class
