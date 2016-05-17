package it.algos.wam.entity.volontario;

import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;
import java.util.List;

/**
 * Created by webbase templates.
 */
public class VolontarioForm extends ModuleForm {

    private static String LAR_CAMPO = "300px";

    /**
     * The form used to edit an item.
     * <p>
     * Invoca la superclasse passando i parametri:
     *
     * @param item   (facoltativo) singola istanza della classe
     * @param module (obbligatorio) di riferimento
     */
    public VolontarioForm(Item item, ModulePop module) {
        super(item, module);
    }// end of constructor


//    @Override
//    protected void init() {
//        super.init();
//        readFunzioni();
//    }

    /**
     * Populate the map to bind item properties to fields.
     * <p>
     * Crea e aggiunge i campi.<br>
     * Implementazione di default nella superclasse.<br>
     * I campi vengono recuperati dal Modello.<br>
     * I campi vengono creati del tipo grafico previsto nella Entity.<br>
     * Se si vuole aggiungere un campo (solo nel form e non nel Modello),<br>
     * usare il metodo sovrascritto nella sottoclasse
     * invocando prima (o dopo) il metodo della superclasse.
     * Se si vuole un layout completamente diverso sovrascrivere
     * senza invocare il metodo della superclasse
     */
    @Override
    public void createFields() {
        TextField field;
        CheckBoxField fieldCheck;
        TextArea fieldArea;
//        super.createFields();

        Attribute[] attributes = {
                Volontario_.nome,
                Volontario_.cognome,
                Volontario_.dipendente,
                Volontario_.attivo,
                Volontario_.note,
                Volontario_.admin
        };

        field = new TextField(Volontario_.nome.getName());
        field.setColumns(25);
        addField(attributes[0], field);
        field = new TextField(Volontario_.cognome.getName());
        field.setColumns(25);
        addField(attributes[1], field);
        fieldCheck = new CheckBoxField(Volontario_.dipendente.getName());
        addField(attributes[2], fieldCheck);
        fieldCheck = new CheckBoxField(Volontario_.attivo.getName());
        addField(attributes[3], fieldCheck);
        fieldArea = new TextArea(Volontario_.note.getName());
        fieldArea.setColumns(25);
        fieldArea.setRows(4);
        addField(attributes[4], fieldArea);

        fieldCheck = new CheckBoxField(Volontario_.admin.getName());
        addField(Volontario_.admin, fieldCheck);

    }// end of method

    @Override
    protected Component createComponent() {
        VerticalLayout layout = new VerticalLayout();

        layout.addComponent(creaCompDetail());
        layout.setMargin(true);

        return layout;
    }// end of method

    private Component creaCompDetail() {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutBoxFunz;
        layout.setSpacing(true);
        Field field;
        Volontario volontario;
        List<VolontarioFunzione> listaFunzioni = null;
        String funzText;
        Label label;

        field = getField(Volontario_.nome.getName());
        field.setWidth(LAR_CAMPO);
        layout.addComponent(field);

        field = getField(Volontario_.cognome.getName());
        field.setWidth(LAR_CAMPO);
        layout.addComponent(field);

        HorizontalLayout layoutBox = new HorizontalLayout();
        layoutBox.setSpacing(true);
        field = getField(Volontario_.dipendente.getName());
        layoutBox.addComponent(field);
        field = getField(Volontario_.attivo.getName());
        layoutBox.addComponent(field);
        layout.addComponent(layoutBox);

        field = getField(Volontario_.admin.getName());
        layout.addComponent(field);


//        layout.addComponent(new Label("Funzioni abilitate"));
//
//        //--funzioni del volontario
//        volontario = (Volontario) getEntity();
//        if (volontario != null) {
//            listaFunzioni = volontario.getVolontarioFunzioni();
//        }// end of if cycle

//        if (listaFunzioni != null) {
//            for (VolontarioFunzione funz : listaFunzioni) {
//                funzText=funz.getFunzione().getDescrizione();
//                label= new Label(funzText);
//                label.setWidth(LAR_CAMPO);
//                layoutBoxFunz = new HorizontalLayout();
//                layoutBoxFunz.setSpacing(true);
//                layoutBoxFunz.addComponent(label);
//                layoutBoxFunz.addComponent(new CheckBoxField());
//                layout.addComponent(layoutBoxFunz);
//            }// end of for cycle
//        }// end of if cycle

        layout.addComponent(creaCompFunzioni());

        return layout;
    }// end of method


    /**
     * Crea il componente per la selezione delle funzioni abilitate.
     *
     * @return il componente funzioni
     */
    private Component creaCompFunzioni() {
        GridLayout grid = new GridLayout();
        grid.setSpacing(true);
        grid.setCaption("Funzioni abilitate");
        grid.setColumns(3);
        List<Funzione> funzioni = Funzione.findAll();
        for (Funzione f : funzioni) {
            grid.addComponent(new CheckBoxFunzione(f));
        }
        return grid;
    }

    private Volontario getVolontario() {
        return (Volontario) getEntity();
    }

    @Override
    protected boolean save() {
        return super.save();
    }

    /**
     * Componente per il checkbox funzione, fatto di CheckBox e label HTML con icona
     */
    private class CheckBoxFunzione extends HorizontalLayout {
        public CheckBoxFunzione(Funzione f) {
            setSpacing(true);

            // label con icona e nome funzione
            Label label = new Label();
            label.setContentMode(ContentMode.HTML);
            FontAwesome icon = f.getIcon();
            if (icon != null) {
                label.setValue(icon.getHtml());
                label.setWidth("0.7em");
            }

            // checkBox con nome funzione
            CheckBox box = new CheckBox(f.getSiglaVisibile());
            box.addValueChangeListener(valueChangeEvent -> {
                if (box.getValue()) {
                    if (!getVolontario().haFunzione(f)) {
                        getVolontario().addFunzione(f);
                    }
                } else {
                    if (getVolontario().haFunzione(f)) {
                        getVolontario().removeFunzione(f);
                    }
                }
            });
            box.setValue(getVolontario().haFunzione(f));

            // dispongo i componenti
            addComponent(label);
            addComponent(box);

        }

    }

}// end of class
