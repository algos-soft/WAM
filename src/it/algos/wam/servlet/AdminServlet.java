package it.algos.wam.servlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.ui.AdminUI;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.servlet.AlgosServlet;

import javax.servlet.annotation.WebServlet;

/**
 * Servlet dell'admin
 * L'admin amministra tutte le preferenze dell'azienda
 * L'admin accede a tutte le aziende
 * L'admin siamo noi
 */
@Theme("valo")
@WebServlet(value = "/wam-admin/*", asyncSupported = true, displayName = "WAM-Admin")
@VaadinServletConfiguration(productionMode = false, ui = AdminUI.class)
public class AdminServlet extends AlgosServlet {

    /**
     * Invoked when a new Vaadin service session is initialized for that service.
     * <p>
     * Because of the way different service instances share the same session, the listener is not necessarily notified immediately
     * when the session is created but only when the first request for that session is handled by a specific service.
     * Deve (DEVE) richiamare anche il metodo della superclasse (questo)
     * prima (PRIMA) di eseguire le regolazioni specifiche <br>
     *
     * @param event the initialization event
     * @throws ServiceException a problem occurs when processing the event
     */
    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        super.sessionInit(event);

        // Do session start stuff here

        // provvisorio!! fino a quando non gestiamo il login
        CompanySessionLib.setCompany(WamCompany.getDemo());
    }// end of method

}// end of servlet class
