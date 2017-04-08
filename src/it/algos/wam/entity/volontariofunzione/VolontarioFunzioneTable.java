package it.algos.wam.entity.volontariofunzione;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamTable;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzioneTable;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione_;
import it.algos.webbase.web.module.ModulePop;

/**
 * Created by gac on 27/03/17.
 * Sovrascrive la classe standard per aggiungere una colonna generata 'al volo'
 */
public class VolontarioFunzioneTable extends WamTable {

    // id della colonna generata "icona"
    protected static final String COL_ICONA_FUNZIONE = "icona";

    //--titolo della table
    private static String CAPTION = "Volontari abilitati per ogni funzione";

    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public VolontarioFunzioneTable(ModulePop module) {
        super(module,CAPTION);
    }// end of constructor


    /**
     * Create additional columns
     * (add generated columns, nested properties...)
     * <p>
     * Override in the subclass
     */
    @Override
    protected void createAdditionalColumns() {
        addGeneratedColumn(COL_ICONA_FUNZIONE, new VolontarioFunzioneTable.IconaColumnGenerator());
    }// end of method

    /**
     * Returns an array of the visible columns ids. Ids might be of type String or Attribute.<br>
     * This implementations returns all the columns (no order).
     *
     * @return the list
     */
    @Override
    protected Object[] getDisplayColumns() {
        return new Object[]{
                WamCompanyEntity_.company,
                COL_ICONA_FUNZIONE,
                VolontarioFunzione_.funzione,
                VolontarioFunzione_.volontario
        };// end of array
    }// end of method


    @Override
    protected void fixColumn() {
        setColumnAlignment(COL_ICONA_FUNZIONE, Align.CENTER);

        setColumnExpandRatio(VolontarioFunzione_.funzione, 2);
        setColumnExpandRatio(VolontarioFunzione_.volontario, 2);

        setColumnWidth(COL_ICONA_FUNZIONE, 80);
    }// end of method


    /**
     * Colonna generata: icona.
     */
    private class IconaColumnGenerator implements ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {

            final Item item = source.getItem(itemId);
            com.vaadin.ui.Button bIcon = new com.vaadin.ui.Button();
            bIcon.setHtmlContentAllowed(true);
            bIcon.setWidth("3em");
            bIcon.setCaption("...");
            bIcon.addStyleName("blue");

            Property prop = item.getItemProperty(VolontarioFunzione_.funzione.getName());
            if (prop != null) {
                Funzione funz = (Funzione) prop.getValue();
                FontAwesome glyph = null;
                int codepoint =  funz.getIconCodepoint();
                try {
                    glyph = FontAwesome.fromCodepoint(codepoint);
                    bIcon.setCaption(glyph.getHtml());
                } catch (Exception e) {
                }// fine del blocco try-catch
            }// end of if cycle

            return bIcon;
        }// end of inner method
    }// end of inner class

}// end of class
