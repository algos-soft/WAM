package it.algos.wam.entity.volontario;

import com.vaadin.data.Container;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

/**
 * Created by gac on 25 mag 2016.
 * .
 */
public class VolontarioTable extends ETable {


    public VolontarioTable(ModulePop module) {
        super(module);
        inizia();
    }// end of constructor


    private void inizia() {
        Container cont = getContainerDataSource();
        if (cont instanceof Container.Sortable) {
            Container.Sortable sortable = (Container.Sortable) cont;
            sortable.sort(new Object[]{Volontario_.cognome.getName()}, new boolean[]{true});
        }// end of if cycle

    }// end of method


    @Override
    protected void createAdditionalColumns() {
        ArrayList<Funzione> listaFunzioni = Funzione.findAll();
        for (Funzione funz : listaFunzioni) {
            addGeneratedColumn(funz.getSiglaInterna(), new FunzioniColumnGenerator(funz));
        }// end of for cycle
    }// end of method


    protected Object[] getDisplayColumns() {
        ArrayList lista = new ArrayList<>();
        ArrayList<Funzione> listaFunzioni = Funzione.findAll();

        if (LibSession.isDeveloper()) {
            lista.add(WamCompanyEntity_.company);
        }// end of if cycle

        lista.add(Volontario_.nome);
        lista.add(Volontario_.cognome);

        for (Funzione funz : listaFunzioni) {
            lista.add(funz.getSiglaInterna());
        }// end of for cycle

        return lista.toArray();
    }// end of method


    /**
     * Colonna generata.
     */
    private class FunzioniColumnGenerator implements ColumnGenerator {

        private Funzione funz;

        /**
         * Costruttore minimo con tutte le properties obbligatorie
         */
        public FunzioniColumnGenerator(Funzione funz) {
            this.funz = funz;
        }// end of constructor

        /**
         * Genera la cella.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            String tag = funz.getSiglaInterna();
            tag = tag.substring(0, 1);
            tag = LibText.primaMaiuscola(tag);

            return new Label(tag);
        }// end of method
    }// end of inner class

}// end of class
