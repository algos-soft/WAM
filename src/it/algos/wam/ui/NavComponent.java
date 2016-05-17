package it.algos.wam.ui;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import it.algos.webbase.web.menu.AMenuBar;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.navigator.AlgosNavigator;
import it.algos.webbase.web.navigator.MenuCommand;
import it.algos.webbase.web.navigator.NavPlaceholder;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Componente con menubar sopra, parte centrale controllata da un Navigator che
 * può ospitare diverse View, e footer in basso.
 * <p>
 * Uso:
 * 1) creare questo oggetto,
 * 2) aggiungere le View con i metodi addView(),
 * 3) invocare setup() per configurare il componente.
 */
public class NavComponent extends VerticalLayout {

    private AlgosNavigator nav;
    private MenuBar menuBar = new MenuBar();
    private NavPlaceholder body = new NavPlaceholder(null);
    private VerticalLayout footer = new VerticalLayout();
    private LinkedHashMap<String, MenuBar.MenuItem> mappaItem = new LinkedHashMap<String, MenuBar.MenuItem>();

    /**
     * Costruttore.
     *
     * @param ui UI che il Navigator deve controllare
     */
    public NavComponent(UI ui) {
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        menuBar.setWidth("100%");

        this.nav = new AlgosNavigator(ui, body);
        this.addComponent(menuBar);
        this.addComponent(body);
        this.addComponent(footer);

        setExpandRatio(body, 1);

    }

    /**
     * Da invocare per configurare il Navigator dopo aver aggiunto i componenti
     */
    public void setup() {

        // configura il navigator in base alla MenuBar
        nav.configureFromMenubar(menuBar);

        // manda il navigatore alla prima view
        Collection<MenuBar.MenuItem> coll = mappaItem.values();
        MenuBar.MenuItem[] items = coll.toArray(new MenuBar.MenuItem[0]);
        if (items.length > 0) {
            MenuBar.MenuItem item = items[0];
            MenuCommand mcmd = (MenuCommand) item.getCommand();
            String addr = mcmd.getNavigatorAddress();
            nav.navigateTo(addr);
        }
    }

    /**
     * Inserisce un componente nel footer
     */
    public void setFooter(Component footer) {
        this.footer.removeAllComponents();
        this.footer.addComponent(footer);
    }

    /**
     * Aggiunge un modulo al Navigator
     * <p>
     * the view is cached and will be instantiated only once
     *
     * @param viewClass the class to instantiate (must implement View)
     */
    public void addView(Class<? extends View> viewClass) {
        String label = viewClass.getSimpleName();
        addView(viewClass, true, label, null);
    }

    /**
     * Adds a cached View to the UI
     * <p>
     * Will create a lazy (class-based) view provider
     * The view will be instantiated by the view provider from the provided class
     * The view will be instantiated only once, then re-used
     *
     * @param viewClass the view class to instantiate
     * @param label     the text for the menu item
     * @param icon      the icon for the menu item
     * @return menuItem appena creato
     */
    public MenuBar.MenuItem addView(Class<? extends View> viewClass, String label, Resource icon) {
        return addView(viewClass, true, label, icon);
    }

    /**
     * Adds a View to the UI
     * <p>
     * Will create a lazy (class-based) view provider
     * The view will be instantiated by the view provider from the provided class
     * The viewCached parameter controls if the view will be instantiated only once
     * or each time is requested by the Navigator.
     *
     * @param viewClass  the view class to instantiate
     * @param viewCached true to instantiated only once, false to instantiate each time
     * @param label      the text for the menu item
     * @param icon       the icon for the menu item
     * @return menuItem appena creato
     */
    public MenuBar.MenuItem addView(Class<? extends View> viewClass, boolean viewCached, String label, Resource icon) {

        MenuBar.MenuItem menuItem = createMenuItem(viewClass, label, viewCached, icon);

        if (menuItem != null) {
            String keyModulo = viewClass.getSimpleName();
            mappaItem.put(keyModulo, menuItem);
        }

        return menuItem;

    }

    /**
     * Create the MenuBar Item for this view
     * <p>
     *
     * @param viewClass da visualizzare nell'area controllata dal navigatore
     *                  alla pressione del bottone di menu
     * @param icon      icona per il menu
     * @return menuItem appena creato
     */
    private MenuBar.MenuItem createMenuItem(Class<? extends View> viewClass, String label, boolean cached, Resource icon) {
        MenuBar.MenuItem menuItem;
        MenuCommand cmd = new MenuCommand(menuBar, viewClass, cached);
        menuItem = menuBar.addItem(label, icon, cmd);
        menuItem.setStyleName(AMenuBar.MENU_DISABILITATO);
        return menuItem;
    }

    /**
     * Aggiunge un modulo alla UI
     * <p>
     * Il modulo può essere aggiunto come istanza già creata
     * Tipicamente un ModulePop
     *
     * @param modulo da visualizzare nel placeholder alla pressione del bottone di menu
     * @return menuItem appena creato
     */
    public MenuBar.MenuItem addMod(ModulePop modulo) {
        MenuBar.MenuItem menuItem = null;
        String menuLabel;
        Resource menuIcon = null;

        if (modulo != null) {
            menuLabel = modulo.getMenuLabel();
            menuIcon = modulo.getMenuIcon();
            menuItem = addVista(modulo, menuLabel, menuIcon);

            if (menuItem != null) {
                modulo.addSottoMenu(menuItem);
            }// end of if cycle
        }// end of if cycle

        return menuItem;
    }// end of method

    /**
     * Aggiunge una view alla UI
     * <p>
     * La view può essere aggiunto come istanza già creata
     * Qualunque oggetto grafico che implementi l'interfaccia View
     *
     * @param vista     da visualizzare nel placeholder alla pressione del bottone di menu
     * @param menuLabel etichetta visibile nella menu bar
     * @param menuIcon  icona del menu
     */
    public MenuBar.MenuItem addVista(View vista, String menuLabel, Resource menuIcon) {
        String keyModulo = "";
        MenuBar.MenuItem menuItem = createMenuItem(vista, menuLabel, menuIcon);

        if (menuItem != null) {
            if (vista instanceof ModulePop) {
                keyModulo = ((ModulePop) vista).getMenuLabel();
            } else {
                keyModulo = vista.getClass().getSimpleName();
            }// end of if/else cycle
            mappaItem.put(keyModulo, menuItem);
        }// end of if cycle

        return menuItem;
    }// end of method

    /**
     * Create the MenuBar Item for this view
     * <p>
     *
     * @param vista    da visualizzare nel placeholder alla pressione del bottone di menu
     * @param menuIcon del menu
     * @return menuItem appena creato
     */
    private MenuBar.MenuItem createMenuItem(View vista, String menuAddress, Resource menuIcon) {
        MenuBar.MenuItem menuItem;
        MenuCommand cmd = new MenuCommand(menuBar, vista);
        menuItem = menuBar.addItem(menuAddress, menuIcon, cmd);
        menuItem.setStyleName(AMenuBar.MENU_DISABILITATO);

        return menuItem;
    }// end of method

    /**
     * Ritorna il Navigator
     *
     * @return il Navigator
     */
    public AlgosNavigator getNavigator() {
        return nav;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Naviga al modulo indicato
     */
    public void navigateTo(Class clazz) {
        AlgosNavigator navigator = getNavigator();
        String navigatorAddress = "";

        if (clazz == null) {
            return;
        }// end of if cycle

        if (navigator != null) {
            navigatorAddress = clazz.getName();
            navigator.navigateTo(navigatorAddress);
        }// end of if cycle

    }// end of method

}
