package it.algos.wam.tabellone;

import com.vaadin.ui.Label;
import it.algos.webbase.web.lib.LibDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by alex on 21/02/16.
 */
public class CGiorno extends Label {
    public CGiorno(LocalDate date) {
        super();

        setWidth(GridTabellone.W_COLONNE_TURNI);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE d MMM");
        String text = date.format(formatter);
        setValue(text);
        addStyleName("cgiorno");
    }


}
