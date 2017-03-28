package it.algos.wam.entity.serviziofunzione;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.colorpicker.*;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorChangeListener;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamTable;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.servizio.ServizioColorPicker;
import it.algos.wam.entity.servizio.ServizioMod;
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;
import java.awt.*;
import java.awt.Button;

/**
 * Created by gac on 27/03/17.
 * Sovrascrive la classe standard per aggiungere due colonne generate 'al volo'
 */
public class ServizioFunzioneTable extends WamTable {

    // id della colonna generata "colore"
    private static final String COL_COLORE_SERVIZIO = "gruppo";

    // id della colonna generata "icona"
    private static final String COL_ICONA_FUNZIONE = "icona";

    //--titolo della table
    private static String CAPTION = "Funzioni per ogni servizio";

    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public ServizioFunzioneTable(ModulePop module) {
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
        addGeneratedColumn(COL_COLORE_SERVIZIO, new ColoreColumnGenerator());
        addGeneratedColumn(COL_ICONA_FUNZIONE, new IconaColumnGenerator());
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
                COL_COLORE_SERVIZIO,
                ServizioFunzione_.servizio,
                ServizioFunzione_.ordine,
                COL_ICONA_FUNZIONE,
                ServizioFunzione_.funzione,
                ServizioFunzione_.obbligatoria
        };// end of array
    }// end of method


    @Override
    protected void fixColumn() {
        setColumnHeader(ServizioFunzione_.ordine, "##");

        setColumnAlignment(COL_COLORE_SERVIZIO, Align.CENTER);
        setColumnAlignment(ServizioFunzione_.ordine, Align.CENTER);
        setColumnAlignment(COL_ICONA_FUNZIONE, Align.CENTER);

        setColumnExpandRatio(ServizioFunzione_.servizio, 2);
        setColumnExpandRatio(ServizioFunzione_.funzione, 2);

        setColumnWidth(COL_COLORE_SERVIZIO, 80);
        setColumnWidth(COL_ICONA_FUNZIONE, 80);
        setColumnWidth(ServizioFunzione_.obbligatoria, 120);
    }// end of method

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
            prop = item.getItemProperty(ServizioFunzione_.servizio.getName());
            Servizio serv = (Servizio) prop.getValue();
            int codColore = serv.getColore();
            ColorPicker picker = new ServizioColorPicker();
            picker.setWidth("45");
            picker.setColor(new Color(codColore));
            picker.setCaption("&#8203;"); //zero-width space
            picker.setReadOnly(true);

            return picker;
        }// end of method
    }// end of class


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

            Property prop = item.getItemProperty(ServizioFunzione_.funzione.getName());
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
