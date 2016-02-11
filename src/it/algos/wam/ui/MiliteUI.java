package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import it.algos.wam.entity.company.CompanyModulo;
import it.algos.webbase.web.ui.AlgosUI;

/**
 * La UI del Milite.
 * Accede al tabellone e gestisce le proprie iscrizioni
 */
@Theme("valo")
public class MiliteUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        setContent(new Label("UI del Milite"));
    }


}
