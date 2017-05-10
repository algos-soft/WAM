package it.algos.wam.entity.turno;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Field;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamSearch;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;

import javax.persistence.metamodel.Attribute;
import java.util.*;

/**
 * Created by gac on 26/04/17
 * .
 */
public class TurnoSearch extends WamSearch {

    /**
     * Constructor
     *
     * @param module the reference module
     */
    public TurnoSearch(ModulePop module) {
        super(module);
    }// end of constructor

    /**
     * Attributes used in this search
     * Di default prende dal modulo
     * Può essere sovrascritto se c'è un Search specifico
     *
     * @return a list containing all the attributes used in this search
     */
    @Override
    protected Attribute<?, ?>[] getAttributesList() {
        return new Attribute[]{
                Turno_.servizio,
                Turno_.inizio,
                Turno_.fine
        };//end of brace
    }// end of method

    /**
     * Creates and adds the filters for each search field. Invoked before performing the search.
     *
     * @return an array of filters which will be concatenated with the And clause
     */
    public ArrayList<Container.Filter> createFilters() {
        ArrayList<Container.Filter> filters = new ArrayList<>();
        Attribute attr;
        Field field;
        Container.Filter filter = null;
        Field inizioField = getField("inizio");
        Field fineField = getField("fine");
        Object inizioValue = inizioField.getValue();
        Object fineValue = fineField.getValue();

        if (inizioValue != null && fineValue != null) {
            LinkedHashMap<Attribute, Field> fieldMap = getFieldMap();
            for (Map.Entry<Attribute, Field> entry : fieldMap.entrySet()) {
                attr = entry.getKey();
                field = entry.getValue();

                if (!field.isEmpty()) {
                    if (field.equals(inizioField) || field.equals(fineField)) {
                        if (field.equals(inizioField)) {
                            filter = new Compare.GreaterOrEqual(attr.getName(), inizioValue);
                        }// end of if cycle
                        if (field.equals(fineField)) {
                            filter = new Compare.LessOrEqual(attr.getName(), fineValue);
                        }// end of if cycle
                    } else {
                        filter = createFilter(attr);
                    }// end of if/else cycle

                    if (filter != null) {
                        filters.add(filter);
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
            return filters;
        } else {
            return super.createFilters();
        }// end of if/else cycle


    }// end of method


    /**
     * crea un filtro per un Field
     * Usa il valore corrente del field
     *
     * @param attr l'attribute per recuperare il field dalla mappa
     * @return il filtro creato
     */

//    protected Container.Filter createFilter(Attribute attr) {
//        Container.Filter filter = null;
//        Class clazz;
//
//        if (attr != null) {
//            clazz = attr.getJavaType();
//
//            if (attr.isAssociation()) {
//                filter = createBeanFilter(attr);
//            }
//
//            if (clazz.equals(String.class)) {
//                if (LibField.getAnnotation(attr) != null) {
//                    filter = createStringFilter(attr, LibField.getAnnotation(attr).search());
//                } else {
//                    filter = createStringFilter(attr, SearchType.CONTAINS);
//                }// end of if/else cycle
//            }// end of if cycle
//
//            if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
//                filter = createBoolFilter(attr);
//            }
//
//            if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
//                filter = createIntegerFilter(attr);
//            }
//
//            if (clazz.equals(Date.class)) {
//                filter = createDateFilter(attr);
//            }
//
//            // altre classi sono di difficile interpretazione a livello generale
//            // e vanno gestite nelo specifico
//            if (clazz.equals(BigDecimal.class)) {
//            }
//
//
//        }
//
//        return filter;
//
//    }

}// end of class
