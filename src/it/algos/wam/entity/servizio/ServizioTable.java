package it.algos.wam.entity.servizio;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ColorPicker;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorChangeListener;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;

import java.util.List;

/**
 * Created by alex on 7-04-2016.
 * .
 */
public class ServizioTable extends ETable {

    // id della colonna generata "durata"
    protected static final String COL_DURATA = "Durata";

    // id della colonna generata "funzioni"
    protected static final String COL_FUNZIONI = "Funzioni";

    // id della colonna generata "colore"
    protected static final String COL_COLORE = "Colore";

    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public ServizioTable(ModulePop module) {
        super(module);
        Container cont = getContainerDataSource();
        if (cont instanceof Sortable) {
            Sortable sortable = (Sortable) cont;
            sortable.sort(new Object[]{Servizio_.ordine.getName()}, new boolean[]{true});
        }
    }// end of constructor

    /**
     * Create additional columns
     * (add generated columns, nested properties...)
     * <p>
     * Override in the subclass
     */
    @Override
    protected void createAdditionalColumns() {
        addGeneratedColumn(COL_DURATA, new DurataColumnGenerator());
        addGeneratedColumn(COL_FUNZIONI, new FunzioniColumnGenerator());
        addGeneratedColumn(COL_COLORE, new ColoreColumnGenerator());
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
        if (LibSession.isDeveloper()) {
            return new Object[]{
                    WamCompanyEntity_.company,
                    Servizio_.ordine,
                    Servizio_.sigla,
                    Servizio_.descrizione,
                    COL_DURATA,
                    COL_FUNZIONI,
                    COL_COLORE
            };// end of array
        } else {
            return new Object[]{
                    Servizio_.sigla,
                    Servizio_.descrizione,
                    COL_DURATA,
                    COL_FUNZIONI,
                    COL_COLORE
            };// end of array
        }// end of if/else cycle
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
        if (LibSession.isDeveloper()) {
            Container cont = getContainerDataSource();
            if (cont instanceof Sortable) {
                Sortable sortable = (Sortable) cont;
                sortable.sort(new Object[]{Servizio_.ordine.getName()}, new boolean[]{true});
            }// end of if cycle
        } else {
            setSortEnabled(false);
        }// end of if/else cycle
    }// end of method


    private void fixColumn() {
        setColumnHeader(Servizio_.ordine, "#");
        setColumnHeader(Servizio_.sigla, "Sigla");
        setColumnHeader(Servizio_.descrizione, "Descrizione");

        setColumnAlignment(COL_DURATA, Align.LEFT);

        setColumnExpandRatio(COL_FUNZIONI, 2);
        setColumnExpandRatio(Servizio_.sigla, 1);
        setColumnExpandRatio(Servizio_.descrizione, 2);

        setColumnWidth(COL_DURATA, 140);
        setColumnWidth(COL_COLORE, 95);
    }// end of method

    /**
     * Colonna generata: durata.
     */
    private class DurataColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella della durata.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {

            Property prop;
            Item item = source.getItem(itemId);

            prop = item.getItemProperty(Servizio_.orario.getName());
            boolean orario = (Boolean) prop.getValue();

            String s;
            if (orario) {
                BeanItem bi = LibBean.fromItem(item);
                Servizio serv = (Servizio) bi.getBean();
                s = serv.getStrOrario();
            } else {
                s = "variabile";
            }

            return new Label(s);
        }// end of method
    }// end of inner class


    /**
     * Colonna generata: funzioni.
     */
    private class FunzioniColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella delle funzioni.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Item item = source.getItem(itemId);
            BeanItem bi = LibBean.fromItem(item);
            Servizio serv = (Servizio) bi.getBean();
            String s = "";
            List<ServizioFunzione> lista = serv.getServizioFunzioni();
//            Collections.sort(lista);
            for (ServizioFunzione sf : lista) {
                if (s.length() > 0) {
                    s += ", ";
                }
                if (sf.isObbligatoria()) {
                    s += "<strong>" + "<span style=\"color:red;\">";
                } else {
                    s += "<span style=\"color:blue;\">";
                }// end of if/else cycle

                s += sf.getFunzione().getSiglaInterna();
                if (sf.isObbligatoria()) {
                    s += "</span>" + "</strong>";
                } else {
                    s += "</span>";
                }// end of if/else cycle
            }
            return new Label(s, ContentMode.HTML);
        }// end of method
    }// end of inner class


    /**
     * Colonna generata: colore.
     */
    private class ColoreColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella del colore.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Property prop;
            Item item = source.getItem(itemId);
            prop = item.getItemProperty(Servizio_.colore.getName());
            int codColore = (Integer) prop.getValue();
            ColorPicker picker = new ServizioColorPicker();
            picker.setColor(new Color(codColore));
            picker.setCaption("&#8203;"); //zero-width space
            picker.addColorChangeListener(new ColorChangeListener() {
                @Override
                public void colorChanged(ColorChangeEvent colorChangeEvent) {
                    BeanItem bi = LibBean.fromItem(item);
                    Servizio serv = (Servizio) bi.getBean();
                    int colorcode = colorChangeEvent.getColor().getRGB();
                    serv.setColore(colorcode);
                    serv.save();
                }
            });
            return picker;
        }// end of method
    }// end of inner class

}// end of class
