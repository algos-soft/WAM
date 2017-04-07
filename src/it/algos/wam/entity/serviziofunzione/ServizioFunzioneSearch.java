package it.algos.wam.entity.serviziofunzione;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;

import javax.persistence.metamodel.Attribute;
import java.lang.reflect.Method;

/**
 * Created by gac on 05/04/17.
 * .
 */
public class ServizioFunzioneSearch extends SearchManager {

    /**
     * Constructor
     *
     * @param module the reference module
     */
    public ServizioFunzioneSearch(ModulePop module) {
        super(module);
    }// end of constructor

    /**
     * Searches from the value retrieved from an Array.
     * <p>
     * It is assumed that the object returned by getValue() has a method named "getId()"
     * which returns an Integer. If not, null is returned.
     */
    protected Container.Filter createArrayFilter(ArrayComboField field, Attribute attr) {
        return super.createArrayFilter(field,attr);
    }// end of method

}// end of class
