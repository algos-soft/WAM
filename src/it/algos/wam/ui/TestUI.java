package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import it.algos.wam.tabellone.CTabellone;

/**
 * Created by alex on 20/02/16.
 */
@Theme("valo")
public class TestUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        Component comp = new CTabellone();
        setContent(comp);
    }

}
