package it.algos.wam.login;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import it.algos.webbase.web.login.LoginButton;

/**
 * Componente che ospita una (o più) Menubar e il bottone Login
 * Created by alex on 24/05/16.
 */
public class MenuBarWithLogin extends HorizontalLayout {

    private LoginButton loginButton;

    /**
     * Costruttore.
     */
    public MenuBarWithLogin() {
        this(null);
    }// end of constructor


    /**
     * Costruttore.
     *
     * @param menubar primo menu
     */
    public MenuBarWithLogin(MenuBar menubar) {
        setWidth("100%");
        setSpacing(true);

        // primo menu a sinistra
        if (menubar != null) {
            this.addMenu(menubar);
        }// end of if cycle

        loginButton = new WamLoginButton();
        addComponent(loginButton);
    }// end of constructor


    /**
     * Aggiunge un menu (anche bottone) dopo quello esistente e prima del login
     */
    public void addMenu(Component altroMenu) {
        int pos = this.getComponentCount() - 1;
        if (pos < 0) {
            pos = 0;
        }// end of if cycle

        addComponent(altroMenu, pos);
        altroMenu.setWidth("100%");
        setExpandRatio(altroMenu, 1);
    }// end of method


    public MenuBar getFirstMenu() {
        MenuBar menu = null;
        Component comp = null;

        if (this.getComponentCount() > 0) {
            comp = getComponent(0);
        }// end of if cycle

        if (comp != null && comp instanceof MenuBar) {
            menu = (MenuBar) comp;
        }// end of if cycle

        return menu;
    }// end of method


    public LoginButton getLoginButton() {
        return loginButton;
    }// end of method


}// end of class
