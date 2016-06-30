package it.algos.wam.entity.volontario;

import com.vaadin.data.Container;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

/**
 * Created by gac on 25 mag 2016.
 * .
 */
public class VolontarioTable extends ETable {

    // larghezza delle colonne funzioni
    private static int LAR = 90;


    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public VolontarioTable(ModulePop module) {
        super(module);
    }// end of constructor


    /**
     * Create additional columns
     * (add generated columns, nested properties...)
     * <p>
     * Override in the subclass
     */
    @Override
    protected void createAdditionalColumns() {
        ArrayList<Funzione> listaFunzioni = Funzione.findAll();
        for (Funzione funz : listaFunzioni) {
            addGeneratedColumn(funz.getSiglaInterna(), new FunzioniColumnGenerator(funz));
        }// end of for cycle
    }// end of method


    /**
     * Returns an array of the visible columns ids. Ids might be of type String
     * or Attribute.<br>
     * This implementations returns all the columns (no order).
     *
     * @return the list
     */
    @Override
    protected Object[] getDisplayColumns() {
        ArrayList lista = new ArrayList<>();
        ArrayList<Funzione> listaFunzioni = Funzione.findAll();

        if (LibSession.isDeveloper()) {
            lista.add(WamCompanyEntity_.company);
        }// end of if cycle

        lista.add(Volontario_.nome);
        lista.add(Volontario_.cognome);

        if (LibSession.isDeveloper()||LibSession.isAdmin()) {
            lista.add(Volontario_.admin);
        }// end of if cycle

        for (Funzione funz : listaFunzioni) {
            lista.add(funz.getSiglaInterna());
        }// end of for cycle

        return lista.toArray();
    }// end of method


    /**
     * Initializes the table.
     * Must be called from the costructor in each subclass
     * Chiamato dal costruttore di ModuleTable
     */
    @Override
    protected void init() {
        super.init();

        setColumnReorderingAllowed(true);

        fixSort();
        fixColumn();
    }// end of method


    private void fixSort() {
        Container cont = getContainerDataSource();
        if (cont instanceof Sortable) {
            Sortable sortable = (Sortable) cont;
            sortable.sort(new Object[]{Volontario_.cognome.getName()}, new boolean[]{true});
        }// end of if cycle
    }// end of method


    private void fixColumn() {
        ArrayList<Funzione> listaFunzioni = Funzione.findAll();

        setColumnHeader(Volontario_.nome, "Nome");
        setColumnHeader(Volontario_.cognome, "Cognome");

        for (Funzione funz : listaFunzioni) {
            setColumnWidth(funz.getSiglaInterna(), LAR);
        }// end of for cycle
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
            Volontario vol = null;
            VolontarioFunzione volFunz = null;
            Label label = null;

            if (itemId instanceof Long) {
                vol = Volontario.find((Long) itemId);
            }// fine del blocco if

            if (vol != null && funz != null) {
                volFunz = VolontarioFunzione.findByVolFun(vol, funz);
            }// fine del blocco if

            if (volFunz != null) {
                label = new Label(FontAwesome.CHECK.getHtml(), ContentMode.HTML);
                label.addStyleName("verde");
            } else {
//                label = new Label(FontAwesome.REMOVE.getHtml(), ContentMode.HTML);
//                label.addStyleName("rosso");

            }// fine del blocco if-else

            return label;
        }// end of method
    }// end of inner class

}// end of class
