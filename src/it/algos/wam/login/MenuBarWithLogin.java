package it.algos.wam.login;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import it.algos.webbase.web.login.LoginButton;

/**
 * Componente che ospita una Menubar e il bottone Login
 * Created by alex on 24/05/16.
 */
public class MenuBarWithLogin extends HorizontalLayout {

    private LoginButton loginButton;

    public MenuBarWithLogin(MenuBar menubar) {

        setWidth("100%");
        setSpacing(true);

        addComponent(menubar);

        loginButton=new WamLoginButton();
        addComponent(loginButton);

        menubar.setWidth("100%");
        setExpandRatio(menubar, 1);

    }// end of constructor


    /**
     * Aggiunge un menu dopo quello esistente e prima del login
     */
    public void addMenu(Component altroMenu) {
        addComponent(altroMenu, this.getComponentCount() - 1);
    }

    public LoginButton getLoginButton() {
        return loginButton;
    }

}// end of class
