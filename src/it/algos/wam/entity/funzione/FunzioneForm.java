package it.algos.wam.entity.funzione;

import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import it.algos.webbase.web.field.TextArea;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

/**
 * Created by alex on 18-04-2016.
 * .
 */
public class FunzioneForm extends ModuleForm {

    private Button iconButton;

    public FunzioneForm(Item item, ModulePop module) {
        super(item, module);
    }

    @Override
    protected void init() {
        super.init();
    }


//    /**
//     * Create the detail component (the upper part containing the fields).
//     * <p>
//     * Usa il FormLayout che ha le label a sinsitra dei campi (standard)
//     * Se si vogliono le label sopra i campi, sovrascivere questo metodo e usare un VerticalLayout
//     *
//     * @return the detail component containing the fields
//     */
//    protected Component createComponent() {
//
////        VerticalLayout vl = new VerticalLayout();
////        vl.setSizeUndefined();
////        vl.setMargin(true);
////        vl.addStyleName("yellowBg");
////        vl.setHeight("500px");
//
//
//        //vl.addComponent(fl);
//        Component fl = creaCompDetail3();
//        //fl.setHeight("100px");
//        fl.setWidthUndefined();
////        vl.addComponent(fl);
//
////        Label label = new Label("ciao");
////        vl.addComponent(label);
//
//        //vl.addComponent(creaCompDetail3());
//
////        VerticalLayout v2 = new VerticalLayout();
////        Field fsigla = getField(Funzione_.sigla);
////        Field fdesc = getField(Funzione_.siglaVisibile);
////        Field fnote = getField(Funzione_.note);
////
////        ///v2.addComponent(fsigla);
////        //v2.addComponent(fdesc);
////        v2.addComponent(fnote);
//
//       // vl.addComponent(v2);
//
////        vl.addComponent(creaCompDetail3());
//
//
//        return fl;
//
//
//    }// end of method


    /**
     * Crea il componente che visualizza il dettaglio
     * Retrieve the fields from the binder and place them in the UI.
     *
     * @return il componente dettagli
     */
    private Component creaCompDetail2() {

        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setWidthUndefined();

        TextField f1 = new TextField();
        f1.setCaption("campo1");

        TextField f2 = new TextField();
        f2.setCaption("campo2");

        layout.addComponent(f1);
        layout.addComponent(f2);


        return layout;
    }


    /**
     * Crea il componente che visualizza il dettaglio
     * Retrieve the fields from the binder and place them in the UI.
     *
     * @return il componente dettagli
     */
    private Component creaCompDetail3() {

        FormLayout layout = new FormLayout();

        Field fsigla = getField(Funzione_.sigla);
        fsigla.focus();
        Field fnote = getField(Funzione_.descrizione);

        Button b = new Button();
        b.setHtmlContentAllowed(true);
        b.addStyleName("bfunzione");


        layout.addComponent(b);
        layout.addComponent(fsigla);
        layout.addComponent(fnote);

        return layout;
    }

//    /**
//     * Crea il componente che visualizza il dettaglio
//     * Retrieve the fields from the binder and place them in the UI.
//     *
//     * @return il componente dettagli
//     */
//    protected Component creaCompDetail(FormLayout layout) {
//        Collection<Field<?>> fields = getBinder().getFields();
//
//        boolean focused=false;
//        for (Field<?> field : fields) {
//
//            layout.addComponent(field);
//
//            // focus to first field
//            if(!focused){
//                field.focus();
//                focused=true;
//            }
//
//        }// end of for cycle
//
//        return layout;
//    }// end of method



    /**
     * Crea il componente che visualizza il dettaglio
     * Retrieve the fields from the binder and place them in the UI.
     *
     * @return il componente dettagli
     */
    protected Component creaCompDetailTest(FormLayout layout) {
        Field fsigla = getField(Funzione_.sigla);
//        fsigla.focus();

        Field fnote = getField(Funzione_.descrizione);

//        iconButton = new Button();
//        iconButton.setHtmlContentAllowed(true);
//        iconButton.addStyleName("bfunzione");

//        layout.addComponent(iconButton);
        layout.addComponent(fsigla);
        layout.addComponent(fnote);

        layout.addComponent(getField(Funzione_.iconCodepoint));
        layout.addComponent(getField(Funzione_.ordine));

        return layout;
    }



    /**
     * Crea il componente che visualizza il dettaglio
     * Retrieve the fields from the binder and place them in the UI.
     *
     * @return il componente dettagli
     */
    protected Component creaCompDetail(FormLayout layout) {
        Field fsigla = getField(Funzione_.sigla);
        fsigla.focus();

        Field fnote = getField(Funzione_.descrizione);

        iconButton = new Button();
        iconButton.setHtmlContentAllowed(true);
        iconButton.addStyleName("verde");
        iconButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                SelectIconDialog dialog = new SelectIconDialog();
                dialog.addCloseListener(new SelectIconDialog.CloseListener() {
                    @Override
                    public void dialogClosed(SelectIconDialog.DialogEvent event) {
                        int exitcode = event.getExitcode();
                        switch (exitcode) {
                            case 0:   // close, no action
                                break;
                            case 1:   // icon selected
                                int codepoint = event.getCodepoint();
                                setGlyph(FontAwesome.fromCodepoint(codepoint));
                                syncButton();
                                break;
                            case 2:   // rewove icon
                                setGlyph(null);
                                syncButton();
                                break;
                        }

                    }
                });
                dialog.show();
            }
        });
        syncButton();

        layout.addComponent(iconButton);
        layout.addComponent(fsigla);
        layout.addComponent(fnote);


//        // test remove menu item
//        Button button=new Button("Remove menu");
//        button.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                UI ui = getUI();
//                if (ui instanceof WamUI) {
//                    WamUI wamUI = (WamUI)ui;
//                    wamUI.removeMenuItem(FunzioneMod.MENU_ADDRESS);
//                }
//            }
//        });
//        layout.addComponent(button);

        return layout;
    }



    @Override
    protected Field createField(Attribute attr) {
        Field field;
        if (attr.equals(Funzione_.descrizione)) {
            TextArea area = new TextArea();
            area.setColumns(20);
            area.setRows(2);
            area.setCaption(LibText.primaMaiuscola(Funzione_.descrizione.getName()));
            field = area;
        } else {
            field = super.createField(attr);
        }
        return field;
    }

    /**
     * Recupera il glifo dal field
     */
    private FontAwesome getGlyph() {
        Field field = getBinder().getField(Funzione_.iconCodepoint.getName());
        int codepoint = (int) field.getValue();
        FontAwesome fa = null;
        try {
            fa = FontAwesome.fromCodepoint(codepoint);
        } catch (Exception e) {
        }
        return fa;
    }

    /**
     * Registra un glifo nel field
     */
    private void setGlyph(FontAwesome glyph) {
        Field field = getBinder().getField(Funzione_.iconCodepoint.getName());
        if (glyph != null) {
            field.setValue(glyph.getCodepoint());
        } else {
            field.setValue(0);
        }
    }

    /**
     * Sincronizza l'icona del bottone con il codepoint contenuto nel field
     */
    private void syncButton() {
        iconButton.setCaption("Scegli una icona...");
        FontAwesome glyph = getGlyph();
        if (glyph != null) {
            iconButton.setCaption(glyph.getHtml());
        }
    }


}
