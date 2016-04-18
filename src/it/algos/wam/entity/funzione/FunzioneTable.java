package it.algos.wam.entity.funzione;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.*;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorChangeListener;
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

    protected static final String COL_ICON = "icon";


    public FunzioneTable(ModulePop module) {
        super(module);
        Container cont = getContainerDataSource();
        if(cont instanceof Sortable){
            Sortable sortable = (Sortable)cont;
            sortable.sort(new Object[]{Funzione_.ordine.getName()}, new boolean[]{true});
        }

    }

    protected Object[] getDisplayColumns() {
        return new Object[]{
                Funzione_.icon,
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
            byte[] bas = (byte[]) item.getItemProperty(Funzione_.icon.getName()).getValue();
            Resource res = LibResource.getStreamResource(bas);
            Image img = new Image(null, res);
            img.setSizeUndefined();
            return img;
        }
    }




}
