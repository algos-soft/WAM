package it.algos.wam.login;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import it.algos.webbase.web.login.LoginButton;
import it.algos.webbase.web.login.Login;

/**
 * Componente che ospita una Menubar e il bottone Login
 * Created by alex on 24/05/16.
 */
public class MenuBarWithLogin extends HorizontalLayout {

    public MenuBarWithLogin(MenuBar menubar) {

        setWidth("100%");
        setSpacing(true);

        addComponent(menubar);
        addComponent(new LoginButton());

        menubar.setWidth("100%");
        setExpandRatio(menubar, 1);

    }

}
