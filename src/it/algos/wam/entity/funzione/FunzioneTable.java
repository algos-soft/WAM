package it.algos.wam.entity.funzione;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;

/**
 * Created by alex on 08/04/16.
 * .
 */
public class FunzioneTable extends ETable {


    // id della colonna generata "Icona"
    protected static final String COL_ICON = "Icona";


    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public FunzioneTable(ModulePop module) {
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
        addGeneratedColumn(COL_ICON, new IconColumnGenerator());
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
                    Funzione_.ordine,
                    COL_ICON,
                    Funzione_.sigla,
                    Funzione_.descrizione,
            };// end of array
        } else {
            return new Object[]{
                    COL_ICON,
                    Funzione_.sigla,
                    Funzione_.descrizione,
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
        Container cont = getContainerDataSource();
        if (cont instanceof Sortable) {
            Sortable sortable = (Sortable) cont;
            sortable.sort(new Object[]{Funzione_.ordine.getName()}, new boolean[]{true});
        }// end of if cycle
    }// end of method


    private void fixColumn() {
        setColumnHeader(Funzione_.ordine, "##"); // visibile solo per il developer
        setColumnHeader(Funzione_.sigla, "Sigla");

        setColumnAlignment(COL_ICON, Align.CENTER);

        setColumnExpandRatio(Funzione_.sigla, 1);
        setColumnExpandRatio(Funzione_.descrizione, 3);

        setColumnWidth(Funzione_.ordine, 50);
        setColumnWidth(COL_ICON, 80);
        setColumnWidth(Funzione_.sigla, 110);
    }// end of method


    /**
     * Colonna generata: icona.
     */
    private class IconColumnGenerator implements ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {

            final Item item = source.getItem(itemId);
            Button bIcon = new Button();
            bIcon.setHtmlContentAllowed(true);
            bIcon.setWidth("3em");
            bIcon.setCaption("...");
            bIcon.addStyleName("verde");

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
                            }

                        }
                    });
                    dialog.show();

                }
            });

            Property prop = item.getItemProperty(Funzione_.iconCodepoint.getName());
            if (prop != null) {
                FontAwesome glyph = null;
                int codepoint = (int) prop.getValue();
                try {
                    glyph = FontAwesome.fromCodepoint(codepoint);
                    bIcon.setCaption(glyph.getHtml());
                } catch (Exception e) {
                }
            }
            return bIcon;
        }// end of method
    }// end of inner class

}// end of class
