package it.algos.wam.entity.companyentity;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Field;
import it.algos.webbase.multiazienda.ESearchManager;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by gac on 20/05/17.
 * .
 */
public class WamSearch extends ESearchManager {


    /**
     * Constructor
     *
     * @param module the reference module
     */
    public WamSearch(ModulePop module) {
        super(module);
    }// end of constructor


    /**
     * @param attr the StaticMetamodel attribute
     */
    protected Object getFieldValue(Attribute attr) {
        Object value = null;
        Field field = getField(attr);

        if (field != null) {
            value = field.getValue();
        }// end of if cycle

        return value;
    }// end of method

    /**
     * @param name the name of StaticMetamodel attribute
     */
    protected Field getField(String name) {

        for (Map.Entry<Attribute, Field> entry : getFieldMap().entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                return entry.getValue();
            }// end of if cycle
        }// end of for cycle

        return null;
    }// end of method

    /**
     * Create a filter for String searches.
     *
     * @param attr the StaticMetamodel attribute
     */
    protected Container.Filter createInizioFilter(Attribute attr) {
        return new Compare.GreaterOrEqual(attr.getName(), getFieldValue(attr));
    }// end of for cycle

    /**
     * Create a filter for String searches.
     *
     * @param attr the StaticMetamodel attribute
     */
    protected Container.Filter createFineFilter(Attribute attr, Date value) {
        return new Compare.LessOrEqual(attr.getName(), LibDate.add(value, 1));
    }// end of for cycle

}// end of class
