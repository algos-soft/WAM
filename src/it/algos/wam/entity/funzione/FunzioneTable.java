package it.algos.wam.entity.funzione;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamTable;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;

import java.util.List;

/**
 * Created by alex on 08/04/16.
 * .
 */
public class FunzioneTable extends WamTable {


    //--id della colonna generata "Icona"
    private static final String COL_ICON = "Icona";


    //--id della colonna generata "Altre funzioni"
    private static final String COL_FUNZIONI = "Funzioni dipendenti";

    //--titolo della table
    private static String CAPTION = "Funzioni previste. Ogni volontario può essere abilitato per una o più funzioni";

    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public FunzioneTable(ModulePop module) {
        super(module, CAPTION);
    }// end of constructor


    /**
     * Create additional columns
     * (add generated columns, nested properties...)
     * <p>
     * Override in the subclass
     */
    @Override
    protected void createAdditionalColumns() {
        addGeneratedColumn(COL_ICON, new IconColumnGenerator());
        addGeneratedColumn(COL_FUNZIONI, new FunzColumnGenerator());
        addGeneratedColumn("testdabuttare", new TestColumnGenerator());
        addGeneratedColumn("testdabuttare2", new TestColumnGenerator2());
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
                    Funzione_.codeCompanyUnico,
                    Funzione_.ordine,
                    COL_ICON,
                    Funzione_.code,
                    Funzione_.sigla,
                    Funzione_.descrizione,
                    COL_FUNZIONI,
            };// end of array
        } else {
            return new Object[]{
                    Funzione_.ordine,
                    COL_ICON,
                    Funzione_.sigla,
                    Funzione_.descrizione,
                    COL_FUNZIONI,
                    "testdabuttare",
                    "testdabuttare2",
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
        this.setColumnCollapsed(Funzione_.ordine.getName(), !CompanySessionLib.isCompanySet());
    }// end of method


    @Override
    protected void fixSort() {
        Container cont = getContainerDataSource();
        if (cont instanceof Sortable) {
            Sortable sortable = (Sortable) cont;
            sortable.sort(new Object[]{Funzione_.ordine.getName()}, new boolean[]{true});
        }// end of if cycle
    }// end of method


    @Override
    protected void fixColumn() {
        setColumnHeader(Funzione_.ordine, "##"); // visibile solo per il developer
        setColumnHeader(Funzione_.code, "Cod.");
        setColumnHeader(Funzione_.sigla, "Sigla");
        setColumnHeader(Funzione_.descrizione, "Descrizione");

        setColumnAlignment(COL_ICON, Align.CENTER);

        setColumnExpandRatio(Funzione_.code, 1);
        setColumnExpandRatio(Funzione_.sigla, 1);
        setColumnExpandRatio(Funzione_.descrizione, 2);
        setColumnExpandRatio(COL_FUNZIONI, 3);

        setColumnWidth(Funzione_.ordine, 45);
        setColumnWidth(COL_ICON, 80);
        setColumnWidth(Funzione_.sigla, 120);
    }// end of method


    /**
     * Colonna generata: icona.
     */
    private class IconColumnGenerator implements ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {
            Button bIcon = new Button();
            final Item item = source.getItem(itemId);
            bIcon.setHtmlContentAllowed(true);
            bIcon.setWidth("3em");
            bIcon.setCaption("...");
            bIcon.addStyleName("blue");

            Property prop = item.getItemProperty(Funzione_.iconCodepoint.getName());
            if (prop != null) {
                FontAwesome glyph = null;
                int codepoint = (int) prop.getValue();
                try {
                    glyph = FontAwesome.fromCodepoint(codepoint);
                    bIcon.setCaption(glyph.getHtml());
                } catch (Exception e) {
                    int a = 78;
                }// fine del blocco try-catch
            }// end of if cycle

            if (Pref.getBool(WAMApp.CLICK_BOTTONI_IN_LISTA, false)) {
                if (LibSession.isAdmin()) {
                    bIcon.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            SelectIconDialog dialog = new SelectIconDialog();
                            dialog.addCloseListener(new SelectIconDialog.CloseListener() {
                                @Override
                                public void dialogClosed(SelectIconDialog.DialogEvent event) {
                                    int exitcode = event.getExitcode();
                                    BeanItem bi = LibBean.fromItem(item);
                                    Funzione funz = (Funzione) bi.getBean();

                                    switch (exitcode) {
                                        case 0:   // close, no action
                                            break;
                                        case 1:   // icon selected
                                            int codepoint = event.getCodepoint();
                                            funz.setIconCodepoint(codepoint);
                                            funz.save();
                                            refresh();
                                            break;
                                        case 2:   // rebove icon
                                            funz.setIconCodepoint(0);
                                            funz.save();
                                            refresh();
                                            break;
                                    } // fine del blocco switch

                                }// end of inner method
                            });// end of anonymous inner class
                            dialog.show();

                        }// end of inner method
                    });// end of anonymous inner class
                }// end of if cycle
            }// end of if cycle

            return bIcon;
        }// end of inner method
    }// end of inner class

    /**
     * Colonna generata: funzioni.
     */
    private class FunzColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella delle funzioni.
         * Usando una Label come componente, la selezione della
         * riga funziona anche cliccando sulla colonna custom.
         */
        public Component generateCell(Table table, Object itemId, Object columnId) {
            String str = "";
            Item item = table.getItem(itemId);
            BeanItem bean = LibBean.fromItem(item);
            Funzione funzione = (Funzione) bean.getBean();

            List<Funzione> lista = funzione.getFunzioniDipendenti();
            for (int k = 0; k < lista.size(); k++) {
                Funzione funz = lista.get(k);
                int codePoint = funz.getIconCodepoint();
                if (codePoint > 0) {
                    FontAwesome glyph = FontAwesome.fromCodepoint(codePoint);
                    str += glyph.getHtml() + " ";
                }// fine del blocco if

                String sigla = funz.getCode();
                str += "<strong>";
                str += "<font color=\"blue\">" + sigla + "</font>";
                str += "</strong>";

                if (k < lista.size() - 1) {
                    str += ", ";
                }

            }

            return new Label(str, ContentMode.HTML);
        }// end of inner method
    }// end of inner class


    private class TestColumnGenerator implements ColumnGenerator {
        /**
         * Genera la cella delle funzioni.
         * Usando una Label come componente, la selezione della
         * riga funziona anche cliccando sulla colonna custom.
         */
        public Component generateCell(Table table, Object itemId, Object columnId) {
            Funzione funzione= (Funzione)getBean(table,itemId);
            return new Label(funzione.getSigla(), ContentMode.HTML);
        }// end of inner method
    }// end of inner class

    private class TestColumnGenerator2 implements ColumnGenerator {
        /**
         * Genera la cella delle funzioni.
         * Usando una Label come componente, la selezione della
         * riga funziona anche cliccando sulla colonna custom.
         */
        public Component generateCell(Table table, Object itemId, Object columnId) {
            String labelTxt = (String) getPropValue(table, itemId, Funzione_.sigla.getName());
            return new Label(labelTxt, ContentMode.HTML);
        }// end of inner method
    }// end of inner class

    protected Object getBean(Table table, Object itemId) {
        Object obj = null;
        BeanItem bean = null;
        Item item = table.getItem(itemId);

        bean = LibBean.fromItem(item);
        obj = bean.getBean();

        return obj;
    }// end of inner method

    protected Object getPropValue(Table table, Object itemId, String propName) {
        Item item = table.getItem(itemId);
        Property prop = item.getItemProperty(propName);
        return prop.getValue();
    }// end of inner method

}// end of class
