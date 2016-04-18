package it.algos.wam.entity.funzione;

import com.vaadin.data.Item;
import com.vaadin.ui.Field;
import it.algos.webbase.web.field.ImageField;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

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
    protected Attribute<?, ?>[] getAttributesList() {
        Attribute[] attrs = new Attribute[]{
                Funzione_.sigla,
                Funzione_.descrizione,
                Funzione_.note,
                Funzione_.icon
        };
        return attrs;
    }

    protected Field createField(Attribute attr) {
        Field field;
        if (attr.equals(Funzione_.icon)) {
            field=new ImageField("Icona");
        } else {
            field=super.createField(attr);
        }
        return field;
    }

}
