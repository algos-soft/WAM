package it.algos.wam.entity.turno;

import com.vaadin.data.Item;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

public class TurnoForm extends ModuleForm  {
    /**
     * The form used to edit an item.
     * <p>
     * Invoca la superclasse passando i parametri:
     *
     * @param item   singola istanza della classe (obbligatorio in modifica e nullo per newRecord)
     * @param module di riferimento (obbligatorio)
     */
    public TurnoForm(Item item, ModulePop module) {
        super(item, module);
    }// end of constructor

}// end of class
