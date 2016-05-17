package it.algos.wam.entity.funzione;

import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import it.algos.webbase.web.field.TextArea;
import it.algos.webbase.web.form.ModuleForm;
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


    /**
     * Crea il componente che visualizza il dettaglio
     * Retrieve the fields from the binder and place them in the UI.
     *
     * @return il componente dettagli
     */
    protected Component creaCompDetail(FormLayout layout) {
        Field fsigla = getField(Funzione_.siglaInterna);
        Field fdesc = getField(Funzione_.siglaVisibile);
        Field fnote = getField(Funzione_.note);

        iconButton = new Button();
        iconButton.setHtmlContentAllowed(true);
        iconButton.addStyleName("bfunzione");
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
        layout.addComponent(fdesc);
        layout.addComponent(fnote);

        return layout;
    }


    @Override
    protected Field createField(Attribute attr) {
        Field field;
        if (attr.equals(Funzione_.note)) {
            TextArea area = new TextArea();
            area.setColumns(20);
            area.setRows(2);
            area.setCaption("Note");
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
