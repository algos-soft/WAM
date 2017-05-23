package it.algos.wam.entity.servizio;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ColorPicker;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorChangeListener;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamTable;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibTable;
import it.algos.webbase.web.module.ModulePop;

import java.util.List;

/**
 * Created by alex on 7-04-2016.
 * .
 */
public class ServizioTable extends WamTable {

    // id della colonna generata "orario"
    protected static final String COL_ORARIO = Servizio_.orario.getName();

    // id della colonna generata "funzioni"
    protected static final String COL_FUNZIONI = "Funzioni previste";

    // id della colonna generata "colore"
    protected static final String COL_COLORE = Servizio_.colore.getName();

    //--titolo della table
    private static String CAPTION = "Servizi previsti. Ogni servizio ha una o più funzioni, di cui almeno una è normalmente obbligatoria (in rosso)";

    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public ServizioTable(ModulePop module) {
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
        addGeneratedColumn(COL_ORARIO, new OrarioColumnGenerator());
        addGeneratedColumn(COL_FUNZIONI, new FunzioniColumnGenerator());
        addGeneratedColumn(COL_COLORE, new ColoreColumnGenerator());
    }// end of method

    /**
     * Returns an array of the visible columns ids. Ids might be of type String or Attribute.<br>
     * This implementations returns all the columns (no order).
     *
     * @return the list
     */
    @Override
    protected Object[] getDisplayColumns() {
        if (LibSession.isDeveloper()) {
            return new Object[]{
                    WamCompanyEntity_.company,
                    Servizio_.codeCompanyUnico,
                    Servizio_.ordine,
                    Servizio_.sigla,
                    Servizio_.abilitato,
                    Servizio_.descrizione,
                    COL_ORARIO,
                    COL_FUNZIONI,
                    COL_COLORE
            };// end of array
        } else {
            return new Object[]{
                    Servizio_.ordine,
                    Servizio_.sigla,
                    Servizio_.abilitato,
                    Servizio_.descrizione,
                    COL_ORARIO,
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
        this.setColumnCollapsed(Servizio_.ordine.getName(), !CompanySessionLib.isCompanySet());
    }// end of method


    @Override
    protected void fixSort() {
        Container cont = getContainerDataSource();
        if (cont instanceof Sortable) {
            Sortable sortable = (Sortable) cont;
            sortable.sort(new Object[]{Servizio_.ordine.getName()}, new boolean[]{true});
        }// end of if cycle
    }// end of method


    @Override
    protected void fixColumn() {
        setColumnHeader(Servizio_.ordine, "##"); // visibile solo per il developer
        setColumnHeader(Servizio_.sigla, "Sigla");
        setColumnHeader(Servizio_.abilitato, "Tab");
        setColumnHeader(Servizio_.descrizione, "Descrizione");
        setColumnHeader(COL_COLORE, ServizioMod.LABEL_COLOR);

        setColumnAlignment(Servizio_.abilitato, Align.CENTER);
        setColumnAlignment(COL_ORARIO, Align.LEFT);
        setColumnAlignment(COL_COLORE, Align.CENTER);

        setColumnExpandRatio(COL_FUNZIONI, 2);
        setColumnExpandRatio(Servizio_.sigla, 1);
        setColumnExpandRatio(Servizio_.descrizione, 2);

        setColumnWidth(Servizio_.abilitato, 50);
        setColumnWidth(Servizio_.ordine, 45);
        setColumnWidth(COL_ORARIO, 140);
        setColumnWidth(COL_COLORE, 100);
    }// end of method

    /**
     * Colonna generata: orario.
     */
    private class OrarioColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella della durata.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            boolean isOrario =  LibTable.getBool(source, itemId, columnId);
            String labelTxt;

            if (isOrario) {
                Servizio serv = (Servizio) LibBean.getEntity(source, itemId);
                labelTxt = serv.getStrOrario();
            } else {
                labelTxt = "variabile";
            }// end of if/else cycle

            return new Label(labelTxt);
        }// end of inner method
    }// end of inner class


    /**
     * Colonna generata: funzioni.
     */
    private class FunzioniColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella delle funzioni.
         * Usando una Label come componente, la selezione della
         * riga funziona anche cliccando sulla colonna custom.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Servizio serv = (Servizio) LibBean.getEntity(source, itemId);

            List<ServizioFunzione> lista = serv.getServizioFunzioniOrd();
            String str = "";
            for (int k = 0; k < lista.size(); k++) {
                ServizioFunzione sf = serv.getServizioFunzioniOrd().get(k);
                int codePoint = sf.getFunzione().getIconCodepoint();
                if (codePoint > 0) {
                    FontAwesome glyph = FontAwesome.fromCodepoint(codePoint);
                    str += glyph.getHtml() + " ";
                }// fine del blocco if

                String sigla = sf.getFunzione().getCode();
                str += "<strong>";
                if (sf.isObbligatoria()) {
                    str += "<font color=\"red\">" + sigla + "</font>";
                } else {
                    str += "<font color=\"green\">" + sigla + "</font>";
                }
                str += "</strong>";

                if (k < lista.size() - 1) {
                    str += ", ";
                }

            }

            return new Label(str, ContentMode.HTML);
        }// end of inner method

    }// end of inner class


    /**
     * Colonna generata: colore.
     */
    private class ColoreColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella del colore.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            int   codColore =  LibTable.getInt(source, itemId, columnId);
            final Servizio serv = (Servizio) LibBean.getEntity(source, itemId);

            ColorPicker picker = new ServizioColorPicker();
            picker.setWidth("45");
            picker.setColor(new Color(codColore));
            picker.setCaption("&#8203;"); //zero-width space
            picker.setReadOnly(true);

            if (Pref.getBool(WAMApp.CLICK_BOTTONI_IN_LISTA, false)) {
                picker.addColorChangeListener(new ColorChangeListener() {
                    @Override
                    public void colorChanged(ColorChangeEvent colorChangeEvent) {
                        int colorcode = colorChangeEvent.getColor().getRGB();
                        serv.setColore(colorcode);
                        serv.save();
                    }// end of inner method
                });// end of anonymous inner class
                picker.setReadOnly(false);
            }// end of if cycle

            return picker;
        }// end of inner method
    }// end of inner class

}// end of class
