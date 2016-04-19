package it.algos.wam.entity.funzione;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.wam.entity.servizio.ServizioColorPicker;
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.ImageField;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 18-04-2016.
 */
public class FunzioneForm extends ModuleForm {

    public FunzioneForm(Item item, ModulePop module) {
        super(item, module);
    }

    @Override
    protected void init() {
        super.init();
//        servizioToUi();
    }

    @Override
    protected Component createComponent() {

        VerticalLayout layout = new VerticalLayout();

        layout.addComponent(creaCompDetail());

        layout.setMargin(true);

        return layout;
    }


    /**
     * Crea il componente che visualizza il dettaglio
     *
     * @return il componente dettagli
     */
    private Component creaCompDetail() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        Field fsigla = getField(Funzione_.sigla);
//        fsigla.setWidth("8em");
        Field fdesc = getField(Funzione_.descrizione);
//        fdesc.setWidth("16em");
        Button iconButton = new Button("Icona");
        iconButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                selectIcon();
            }
        });


        layout.addComponent(fsigla);
        layout.addComponent(fdesc);
        layout.addComponent(iconButton);
//        layout.addComponent(createIconSelector());
        layout.addComponent(new IconTable());



        return layout;
    }


    private void selectIcon(){

        ConfirmDialog dialog = new SelectIconDialog(new ConfirmDialog.Listener() {
            @Override
            public void onClose(ConfirmDialog dialog, boolean confirmed) {

            }
        });

        dialog.show();

//        Window subWindow = new Window("Scegli una icona");
//        VerticalLayout subContent = new VerticalLayout();
//        subContent.setMargin(true);
//        subWindow.setHeight("20em");
//        subWindow.setContent(subContent);
//
//        // Put some components in it
//        subContent.addComponent(new IconTable());
//
//        // Center it in the browser window
//        subWindow.center();
//
//        // Open it in the UI
//        UI.getCurrent().addWindow(subWindow);

    }


    private class SelectIconDialog extends ConfirmDialog{

        public SelectIconDialog(Listener closeListener) {
            super(closeListener);
            setTitle("Scegli una icona");
            addComponent(new IconGrid(this));
        }


    }


    @Override
    protected Attribute<?, ?>[] getAttributesList() {
        Attribute[] attrs = new Attribute[]{
                Funzione_.sigla,
                Funzione_.descrizione,
                Funzione_.note,
                Funzione_.iconCodepoint
        };
        return attrs;
    }

    protected Field createField(Attribute attr) {
        Field field;
        if (attr.equals(Funzione_.iconCodepoint)) {

            int[] codepoints = {FontAwesome.AMBULANCE.getCodepoint(),
                    FontAwesome.WHEELCHAIR.getCodepoint(),
                    FontAwesome.CAB.getCodepoint(),
                    FontAwesome.MOTORCYCLE.getCodepoint(),
                    FontAwesome.MEDKIT.getCodepoint(),
                    FontAwesome.HEART.getCodepoint(),
                    FontAwesome.STETHOSCOPE.getCodepoint(),
                    FontAwesome.HOTEL.getCodepoint(),
                    FontAwesome.USER.getCodepoint(),
                    FontAwesome.USER_MD.getCodepoint(),
                    FontAwesome.MALE.getCodepoint(),
                    FontAwesome.FEMALE.getCodepoint(),
            };

            List<FontAwesome> glyphs = new ArrayList();
            for(int codepoint : codepoints){
                FontAwesome fa = FontAwesome.fromCodepoint(codepoint);
                glyphs.add(fa);
            }

//            field=new ArrayComboField("Icona");

        } else {
            field=super.createField(attr);
        }
        return super.createField(attr);
    }


    class IconGrid extends GridLayout{
        private SelectIconDialog dialog;

        public IconGrid(SelectIconDialog dialog) {
            super();
            this.dialog=dialog;
            setColumns(6);
            setSpacing(true);
            populate();
        }

        private void populate(){
            int[] codepoints = {FontAwesome.AMBULANCE.getCodepoint(),
                    FontAwesome.WHEELCHAIR.getCodepoint(),
                    FontAwesome.CAB.getCodepoint(),
                    FontAwesome.MOTORCYCLE.getCodepoint(),
                    FontAwesome.MEDKIT.getCodepoint(),
                    FontAwesome.HEART.getCodepoint(),
                    FontAwesome.STETHOSCOPE.getCodepoint(),
                    FontAwesome.HOTEL.getCodepoint(),
                    FontAwesome.USER.getCodepoint(),
                    FontAwesome.USER_MD.getCodepoint(),
                    FontAwesome.MALE.getCodepoint(),
                    FontAwesome.FEMALE.getCodepoint(),
            };

            for(int codepoint : codepoints){
                FontAwesome glyph = FontAwesome.fromCodepoint(codepoint);
                Label lbl = new Label();
                lbl.setContentMode(ContentMode.HTML);
                lbl.setValue(glyph.getHtml());
                lbl.addStyleName("funzioneicon");
                lbl.addStyleName("pippo");
                HorizontalLayout layout = new HorizontalLayout();
                layout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                    @Override
                    public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                        iconClicked(codepoint);
                    }
                });
                layout.addStyleName("pippo");
                layout.addComponent(lbl);
                addComponent(layout);
            }

        }

        private void iconClicked(int codepoint){
            dialog.close();
            int a = 87;
            int b=a;
        }


    }




    class IconTable extends Table{

        private static final String ICON_COLUMN="Icona";

        public IconTable() {

            setCaption("Icona");
            setSelectable(true);
            setMultiSelect(false);
            setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
//            setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);

            addContainerProperty(ICON_COLUMN, Integer.class, null);

            int[] codepoints = {FontAwesome.AMBULANCE.getCodepoint(),
                    FontAwesome.WHEELCHAIR.getCodepoint(),
                    FontAwesome.CAB.getCodepoint(),
                    FontAwesome.MOTORCYCLE.getCodepoint(),
                    FontAwesome.MEDKIT.getCodepoint(),
                    FontAwesome.HEART.getCodepoint(),
                    FontAwesome.STETHOSCOPE.getCodepoint(),
                    FontAwesome.HOTEL.getCodepoint(),
                    FontAwesome.USER.getCodepoint(),
                    FontAwesome.USER_MD.getCodepoint(),
                    FontAwesome.MALE.getCodepoint(),
                    FontAwesome.FEMALE.getCodepoint(),
            };

            for(int codepoint : codepoints){
                Object newItemId = addItem();
                Item row = getItem(newItemId);
                row.getItemProperty(ICON_COLUMN).setValue(codepoint);
            }

            addGeneratedColumn(ICON_COLUMN, new IconColumnGenerator());
            setPageLength(size());

        }


        /**
         * Colonna generata: icona.
         */
        private class IconColumnGenerator implements Table.ColumnGenerator {

            public Component generateCell(Table source, Object itemId, Object columnId) {
                final Item item = source.getItem(itemId);
                Property prop = item.getItemProperty(ICON_COLUMN);
                int codepoint = (int)prop.getValue();
                FontAwesome glyph = FontAwesome.fromCodepoint(codepoint);

                Label lbl = new Label();
                lbl.setContentMode(ContentMode.HTML);
                lbl.setValue(glyph.getHtml());
                lbl.addStyleName("redicon");
                return lbl;
            }
        }



    }




}
