package it.algos.wam.tabellone;

import com.vaadin.ui.Label;

import java.time.LocalDate;

/**
 * Created by alex on 21/02/16.
 */
public class CGiorno extends Label {
    public CGiorno(LocalDate date) {
        super();
        setValue(date.toString());
        addStyleName("redBg");
    }
}
