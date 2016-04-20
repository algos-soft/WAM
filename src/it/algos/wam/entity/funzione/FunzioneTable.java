package it.algos.wam.entity.funzione;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorChangeListener;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.servizio.ServizioColorPicker;
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.lib.ByteStreamResource;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.module.ModulePop;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by alex on 08/04/16.
 */
public class FunzioneTable extends ETable {

    protected static final String COL_ICON = "icona";


    public FunzioneTable(ModulePop module) {
        super(module);
        Container cont = getContainerDataSource();
        if (cont instanceof Sortable) {
            Sortable sortable = (Sortable) cont;
            sortable.sort(new Object[]{Funzione_.ordine.getName()}, new boolean[]{true});
        }

    }

    protected Object[] getDisplayColumns() {
        return new Object[]{
                COL_ICON,
                Funzione_.sigla,
                Funzione_.descrizione,
                Funzione_.note,
        };


    }// end of method

    @Override
    protected void createAdditionalColumns() {
        addGeneratedColumn(COL_ICON, new IconColumnGenerator());
    }


    @Override
    protected void init() {
        super.init();

        setColumnReorderingAllowed(true);
        setSortEnabled(false);

        setColumnExpandRatio(Funzione_.sigla, 1);
        setColumnExpandRatio(Funzione_.descrizione, 2);
        setColumnExpandRatio(Funzione_.note, 2);

    }


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
            bIcon.addStyleName("bfunzione");

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
                FontAwesome glyph=null;
                int codepoint = (int) prop.getValue();
                try {
                    glyph=FontAwesome.fromCodepoint(codepoint);
                    bIcon.setCaption(glyph.getHtml());
                } catch (Exception e) {
                }
            }
            return bIcon;
        }
    }


}
