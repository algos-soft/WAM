package it.algos.wam.login;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import it.algos.webbase.web.login.LoginButton;

import java.util.List;

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
        }// end of if cycl

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

        if (pos == 0) {
            altroMenu.setWidth("100%");
            setExpandRatio(altroMenu, 1);
        }// fine del blocco if

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

    /**
     * The item has been selected.
     * de-selects all the menubars
     * highlights the selected item
     *
     * @param selectedItem da evidenziare
     */
    public void menuSelected(MenuBar.MenuItem selectedItem) {

        for (Component comp : components) {
            if (comp instanceof MenuBar && !(comp instanceof WamLoginButton)) {
                menuSelected((MenuBar) comp, selectedItem);
            }// end of if cycle
        }// end of for cycle

        // highlights the selected item
        // the style name will be prepended automatically with "v-menubar-menuitem-"
        selectedItem.setStyleName("highlight");
    }// end of method


    /**
     * De-selects all the items in the menubar
     *
     * @param menuBar      menu selezionato
     * @param selectedItem da evidenziare
     */
    private void menuSelected(MenuBar menuBar, MenuBar.MenuItem selectedItem) {

        if (menuBar != null) {
            List<MenuBar.MenuItem> items = menuBar.getItems();
            for (MenuBar.MenuItem item : items) {
                deselectItem(item);
            } // fine del ciclo for
        }// fine del blocco if
    }// end of method

    /**
     * Recursively de-selects one item and all its children
     */
    private void deselectItem(MenuBar.MenuItem item) {
        item.setStyleName(null);
        List<MenuBar.MenuItem> items = item.getChildren();
        if (items != null) {
            for (MenuBar.MenuItem child : items) {
                deselectItem(child);
            } // fine del ciclo for
        }// fine del blocco if
    }// end of method

}// end of class
