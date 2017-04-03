package it.algos.wam.entity.funzione;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
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
import it.algos.webbase.web.lib.LibTable;
import it.algos.webbase.web.module.ModulePop;

import java.util.List;

/**
 * Created by alex on 08/04/16.
 * .
 */
public class FunzioneTable extends WamTable {


    //--id della colonna generata "Icona"
    private static final String COL_ICON = Funzione_.iconCodepoint.getName();


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
        setColumnHeader(COL_ICON, "Icona");
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
            FontAwesome glyph = null;
            int   codepoint =  LibTable.getInt(source, itemId, columnId);
            Funzione funzione = (Funzione) LibBean.getEntity(source, itemId);
            bIcon.setHtmlContentAllowed(true);
            bIcon.setWidth("3em");
            bIcon.setCaption("...");
            bIcon.addStyleName("blue");

            try {
                glyph = FontAwesome.fromCodepoint(codepoint);
                bIcon.setCaption(glyph.getHtml());
            } catch (Exception e) {
            }// fine del blocco try-catch

            if (LibSession.isAdmin() || Pref.getBool(WAMApp.CLICK_BOTTONI_IN_LISTA, false)) {
                bIcon.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        SelectIconDialog dialog = new SelectIconDialog();
                        dialog.addCloseListener(new SelectIconDialog.CloseListener() {
                            @Override
                            public void dialogClosed(SelectIconDialog.DialogEvent event) {
                                switch (event.getExitcode()) {
                                    case 0:   // close, no action
                                        break;
                                    case 1:   // icon selected
                                        funzione.setIconCodepoint(event.getCodepoint());
                                        break;
                                    case 2:   // remove icon
                                        funzione.setIconCodepoint(0);
                                        break;
                                } // fine del blocco switch
                                funzione.save();
                                refresh();
                            }// end of inner method
                        });// end of anonymous inner class
                        dialog.show();
                    }// end of inner method
                });// end of anonymous inner class
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
        public Component generateCell(Table source, Object itemId, Object columnId) {
            String labelTxt = "";
            Funzione funz;
            Funzione funzione = (Funzione) LibBean.getEntity(source, itemId);

            List<Funzione> lista = funzione.getFunzioniDipendenti();
            for (int k = 0; k < lista.size(); k++) {
                funz = lista.get(k);
                int codePoint = funz.getIconCodepoint();
                if (codePoint > 0) {
                    FontAwesome glyph = FontAwesome.fromCodepoint(codePoint);
                    labelTxt += glyph.getHtml() + " ";
                }// fine del blocco if

                String sigla = funz.getCode();
                labelTxt += "<strong>";
                labelTxt += "<font color=\"blue\">" + sigla + "</font>";
                labelTxt += "</strong>";

                if (k < lista.size() - 1) {
                    labelTxt += ", ";
                }

            }

            return new Label(labelTxt, ContentMode.HTML);
        }// end of inner method
    }// end of inner class

}// end of class
