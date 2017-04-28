package it.algos.wam.tabellone;

import com.vaadin.ui.OptionGroup;
import it.algos.webbase.web.field.FieldAlignment;
import it.algos.webbase.web.field.FieldInterface;

import java.util.Collection;

/**
 * Created by gac on 26/04/17.
 * .
 */
public class GroupField extends OptionGroup implements FieldInterface<Object> {

    public GroupField(String caption) {
        super(caption);
    }

    public GroupField(Collection<?> options) {
        this("",options);
    }

    public GroupField(String caption, Collection<?> options) {
        super(caption, options);
        this.setNullSelectionAllowed(false); // user can not 'unselect'
        this.addStyleName("horizontal");
    }

    @Override
    public void setAlignment(FieldAlignment alignment) {

    }

    @Override
    public void initField() {

    }
}// end of class
