package it.algos.wam.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import it.algos.wam.ui.TabelloneUI;
import it.algos.webbase.web.servlet.AlgosServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(value = "/wam-test/*", asyncSupported = true, displayName = "WAM-Test")
@VaadinServletConfiguration(productionMode = false, ui = TabelloneUI.class)
public class TestServlet extends AlgosServlet {

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        super.sessionInit(event);

        // Do session start stuff here


    }

}// end of servlet class
