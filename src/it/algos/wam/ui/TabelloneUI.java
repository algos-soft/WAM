package it.algos.wam.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.lib.LibWam;
import it.algos.wam.tabellone.*;
import it.algos.webbase.web.lib.DateConvertUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 05/03/16.
 */
public class TabelloneUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        // set theme
        String themeName;
        if (Page.getCurrent().getWebBrowser().isTouchDevice()) {
            themeName = "wam-mob";
        } else {
            themeName = "wam";
        }
        setTheme(themeName);

        setContent(new Tabellone(null));

    }

}
