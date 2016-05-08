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
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.webbase.domain.company.BaseCompany_;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.module.ModulePop;

import java.util.Collections;
import java.util.List;

/**
 * Created by alex on 7-04-2016.
 * .
 */
public class ServizioTable extends ETable {

    // id della colonna generata "durata"
    protected static final String COL_DURATA = "durata";

    // id della colonna generata "funzioni"
    protected static final String COL_FUNZIONI = "funzioni";

    // id della colonna generata "colore"
    protected static final String COL_COLORE = "colore";



    public ServizioTable(ModulePop module) {
        super(module);
        Container cont = getContainerDataSource();
        if(cont instanceof Sortable){
            Sortable sortable = (Sortable)cont;
            sortable.sort(new Object[]{Servizio_.ordine.getName()}, new boolean[]{true});
        }
    }

    protected Object[] getDisplayColumns() {
        return new Object[]{
                Servizio_.ordine,
                Servizio_.sigla,
                Servizio_.descrizione,
                COL_DURATA,
                COL_FUNZIONI,
                COL_COLORE
        };


    }// end of method


    @Override
    protected void init() {
        super.init();

        setColumnReorderingAllowed(true);
        setSortEnabled(false);

        setColumnExpandRatio(Servizio_.sigla, 1);
        setColumnExpandRatio(Servizio_.descrizione, 2);
        setColumnExpandRatio(COL_DURATA, 1);
        setColumnExpandRatio(COL_FUNZIONI, 2);
        setColumnExpandRatio(COL_COLORE, 1);

        setColumnAlignment(COL_DURATA, Align.LEFT);
    }




    @Override
    protected void createAdditionalColumns() {
        addGeneratedColumn(COL_DURATA, new DurataColumnGenerator());
        addGeneratedColumn(COL_FUNZIONI, new FunzioniColumnGenerator());
        addGeneratedColumn(COL_COLORE, new ColoreColumnGenerator());
    }


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
            boolean orario = (Boolean)prop.getValue();

            String s;
            if(orario){
                BeanItem bi = LibBean.fromItem(item);
                Servizio serv = (Servizio)bi.getBean();
                s=serv.getStrOrario();
            }else{
                s="variabile";
            }

            return new Label(s);
        }
    }

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
            Servizio serv = (Servizio)bi.getBean();
            String s="";
            List<ServizioFunzione> lista= serv.getServizioFunzioni();
            Collections.sort(lista);
            for(ServizioFunzione sf : lista){
                if(s.length()>0){s+=", ";}
                if(sf.isObbligatoria()){s+="<strong>";}
                s+=sf.getFunzione().getSigla();
                if(sf.isObbligatoria()){s+="</strong>";}
            }
            return new Label(s, ContentMode.HTML);
        }
    }

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
            int codColore = (Integer)prop.getValue();
            ColorPicker picker = new ServizioColorPicker();
            picker.setColor(new Color(codColore));
            picker.setCaption("&#8203;"); //zero-width space
            picker.addColorChangeListener(new ColorChangeListener() {
                @Override
                public void colorChanged(ColorChangeEvent colorChangeEvent) {
                    BeanItem bi = LibBean.fromItem(item);
                    Servizio serv = (Servizio)bi.getBean();
                    int colorcode=colorChangeEvent.getColor().getRGB();
                    serv.setColore(colorcode);
                    serv.save();
                }
            });
            return picker;
        }
    }

}
