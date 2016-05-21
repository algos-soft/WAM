package it.algos.wam.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import it.algos.wam.ui.TestUI;
import it.algos.wam.ui.WamUI;

import javax.servlet.annotation.WebServlet;

/**
 * Created by alex on 21/05/16.
 */
@WebServlet(urlPatterns = { "/test/*"}, asyncSupported = true, displayName = "Test")
@VaadinServletConfiguration(productionMode = false, ui = TestUI.class)
public class TestServlet extends VaadinServlet {
}
