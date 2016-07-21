package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import it.algos.wam.entity.companyentity.CompanyListener;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.funzione.FunzioneMod;
import it.algos.wam.entity.servizio.ServizioMod;
import it.algos.wam.entity.serviziofunzione.ServizioFunzioneMod;
import it.algos.wam.entity.volontario.VolontarioMod;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzioneMod;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.entity.wamcompany.WamCompanyMod;
import it.algos.wam.lib.WamRuoli;
import it.algos.wam.login.MenuBarWithLogin;
import it.algos.wam.login.WamLogin;
import it.algos.wam.menu.WamMenuCommand;
import it.algos.wam.settings.ConfigScreen;
import it.algos.wam.tabellone.Tabellone;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.log.LogMod;
import it.algos.webbase.domain.pref.PrefMod;
import it.algos.webbase.domain.utente.UtenteModulo;
import it.algos.webbase.domain.vers.VersMod;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.*;
import it.algos.webbase.web.menu.AMenuBar;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.navigator.MenuCommand;
import it.algos.webbase.web.screen.ErrorScreen;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gac on 08 mar 2016.
 * .
 */
@Theme("wam")
public class WamUI extends UI {

    private MenuBar menubar;
    public static final String KEY_TABVISIBLE="tabvisible";

    // si registra chi è interessato alle modifiche delle company (aggiunta, cancellazione, modifica di quella corrente)
    private ArrayList<CompanyListener> companyListeners = new ArrayList<>();

    /**
     * @param request the Vaadin request that caused this UI to be created
     */
    @Override
    protected void init(VaadinRequest request) {


        // controlla l'accesso come programmatore come parametro nell'url
        // attiva il flag developer nella sessione
        checkDeveloper(request);
        if (LibSession.isDeveloper()) {
            developerInit();
            return;
        }

        // da qui in poi non è programmatore

        // recupera la company dall'url
        String companyName = getCompanyNameFromUrl();

        // la company deve esistere nell'url
        if (companyName == null) {
            Component comp = new WamErrComponent("Company non specificata");
            UI.getCurrent().setContent(comp);
            return;
        }

        // la company deve esistere nel db
        // appena la company è disponibile la inietto nella sessione
        WamCompany company = WamCompany.findByCode(companyName);
        if (company != null) {
            CompanySessionLib.setCompany(company);
        }else{
            Component comp = new WamErrComponent("Company " + companyName + " non trovata nel database");
            UI.getCurrent().setContent(comp);
            return;
        }

        // Se non c'è ancora l'oggetto Login, lo crea ora e lo inietta nella sessione.
        // L'oggetto Login è unico nella sessione.
        // Questa applicazione necessita di una logica di login specifica (WamLogin)
        // (inizialmente il Login non ha user e quindi non è in stato logged).
        if(!Login.isLogin()){
            Login.setLogin(new WamLogin(company));

            // listener dei tentativi di login
            Login.getLogin().setLoginListener(new LoginListener() {
                @Override
                public void onUserLogin(LoginEvent e) {
                    if (e.isSuccess()) {
                        if(!LibSession.isAttribute(WamUI.KEY_TABVISIBLE)){
                            UI.getCurrent().setContent(getTabellone());
                        }
                    } else {
                        Notification notif = new Notification("Username o password errati", "", Notification.Type.ERROR_MESSAGE);
                        notif.show(Page.getCurrent());
                        return;
                    }
                }
            });

            // listener dei logout
            Login.getLogin().addLogoutListener(new LogoutListener() {
                @Override
                public void onUserLogout(LogoutEvent e) {
                    Page.getCurrent().reload();
                }
            });

        }

        // Se è loggato, la company dell'url
        // deve essere uguale alla company loggata
        if (Login.getLogin().isLogged()) {
            BaseCompany currCompany = CompanySessionLib.getCompany();
            if (currCompany != null) {
                if (!company.equals(currCompany)) {
                    Component comp = new WamErrComponent("Company non valida (diversa da quella corrente)");
                    UI.getCurrent().setContent(comp);
                    return;
                }
            }
        }

        // Se la company prevede tabellone pubblico, mostra sempre il tabellone
        // Questo viene fatto solo la prima volta quindi a questo punto l'utente non è ancora loggato
        if(company.isVaiSubitoTabellone()){
            if(!LibSession.isAttribute("TABVISIBLE")) {
                if(!Login.getLogin().isLogged()){
                    UI.getCurrent().setContent(getTabellone());
                    return;
                }
            }
        }


        // Se non già loggato, presento la pagina di login
        if (!Login.getLogin().isLogged()) {
            Login.getLogin().readCookies();
            UI.getCurrent().setContent(new WamLoginComponent(Login.getLogin()));
            return;
        }

        // se arrivo qui sono già loggato
        // presento la home
        Component comp = creaComponente();
        UI.getCurrent().setContent(comp);

    }



    /**
     * Init per il developer
     */
    private void developerInit() {
        fixCompanySession();
        Component comp = creaComponente();
        UI.getCurrent().setContent(comp);
    }



    /**
     * Elabora l'URL della Request ed estrae (se esiste) il parametro programmatore
     * Se non trova nulla, di default parte come utente normale
     * È sempre possibile effettuare il login
     * Registra la condizione (di Prog) nella Sessione corrente
     *
     * @param request the Vaadin request that caused this UI to be created
     */
    private boolean checkDeveloper(VaadinRequest request) {
        boolean isProg = false;

        String prog = request.getParameter(WamRuoli.developer.getNome());

        if (prog != null && !prog.isEmpty()) {
            if (prog.equals("gac") || prog.equals("alex")) {
                LibSession.setDeveloper(true);
                isProg = true;
            }// end of if cycle
        }// end of if cycle

        return isProg;

    }// end of method

    /**
     * Solo per il developer
     * Elabora l'URL della Request ed estrae (se esiste) il nome della croce selezionata
     * Se non trova nulla, di default parte con la croce 'demo'
     * Se il nome della croce è sbagliato o non esiste nella lista delle croci esistenti (?), mostra un messaggio di errore
     * Registra la croce (come Company) nella Sessione corrente
     *
     * @return la company selezionata
     */
    private WamCompany fixCompanySession() {
        WamCompany company = null;

        // recupero il codice della company dall'url
        URI uri = Page.getCurrent().getLocation();
        String path = uri.getPath();
        String[] parti = path.split("/");
        String siglaComp = null;
        for (int i = 0; i < parti.length; i++) {
            if (i > 1) {
                siglaComp = parti[i];
                break;
            }
        }

        // recupero la company dal db
        if (siglaComp != null) {
            company = WamCompany.findByCode(siglaComp);
            if (company == null) {
                company = WamCompany.findByCode(WamCompany.DEMO_COMPANY_CODE);
            }// end of if cycle
        }

        // registra la company nella sessione
        CompanySessionLib.setCompany(company);

        return company;

    }// end of method


    /**
     * Estrae dall'url il nome della company
     *
     * @return il nome della company
     */
    private String getCompanyNameFromUrl() {
        WamCompany company = null;

        // recupero il codice della company dall'url
        URI uri = Page.getCurrent().getLocation();
        String path = uri.getPath();
        String[] parti = path.split("/");
        String siglaComp = null;
        for (int i = 0; i < parti.length; i++) {
            if (i > 1) {
                siglaComp = parti[i];
                break;
            }
        }

        return siglaComp;

    }// end of method


    /**
     * Crea il componente per l'utente normale
     * Aggiunge i menu per l'admin e per il programmatore (eventuali)
     * Si possono usare sia i moduli lazy che quelli normali
     *
     * @return il componente creato
     */
    private Component creaComponente() {
        // creo un componente standard di navigazione
        NavComponent navComp = new NavComponent(this);
        MenuBar menuBarUtente = navComp.getMenuBar();
        MenuBar.MenuItem item;

        // aggiungo le view - la menubar viene riempita automaticamente

        // aggiungo un MenuItem con il tabellone.
        // volendo posso anche aggiungerlo nella posizione desiderata
        menuBarUtente.addItem("Tabellone", FontAwesome.CALENDAR_O, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                UI.getCurrent().setContent(getTabellone());
            }
        });

        navComp.addView(FunzioneMod.class, FunzioneMod.MENU_ADDRESS, FontAwesome.CHECK_SQUARE);
        navComp.addView(ServizioMod.class, ServizioMod.MENU_ADDRESS, FontAwesome.TASKS);
        navComp.addView(VolontarioMod.class, VolontarioMod.MENU_ADDRESS, FontAwesome.USER);
        if(LibSession.isAdmin()){
            navComp.addView(LogMod.class, "Log", FontAwesome.CLOCK_O);
            navComp.addView(ConfigScreen.class, "Impostazioni", FontAwesome.WRENCH);
        }







        MenuBarWithLogin menu = (MenuBarWithLogin) navComp.getComponent(0);

        // controlla se è un admin
        if (LibSession.isDeveloper()) {
            MenuBar menuBarAdmin = new MenuBar();
            menuBarAdmin.setStyleName("verde");
            addMod(menuBarAdmin, new LogMod());
            addMod(menuBarAdmin, new PrefMod());
            menu.addMenu(menuBarAdmin);
            navComp.setup(menuBarAdmin);
        }// end of if cycle

        // controlla se è un developer
        if (LibSession.isDeveloper()) {
            MenuBar menuBarDeveloper = new MenuBar();
            menuBarDeveloper.addStyleName("rosso");
            addMod(menuBarDeveloper, new UtenteModulo("User"));
            addMod(menuBarDeveloper, new VersMod());
            addMod(menuBarDeveloper, new WamCompanyMod());
            menu.addMenu(menuBarDeveloper);
            navComp.setup(menuBarDeveloper);
        }// end of if cycle

        // da chiamare dopo che ho aggiunto tutti i MenuItems,
        // configura il Navigator in base alla MenuBar
        navComp.setup();

        return navComp;
    }// end of method


    private MenuBar.MenuItem createMenuItem(MenuBar.MenuItem menu, Class<? extends View> viewClass, String label, boolean cached, Resource icon) {
        MenuBar.MenuItem menuItem;
//        MenuCommand cmd = new MenuBar.Command(menu, viewClass, cached);
        menuItem = menu.addItem(label, icon, null);
        menuItem.setStyleName(AMenuBar.MENU_DISABILITATO);
        return menuItem;
    }

    /**
     * Aggiunge un modulo alla UI
     * Il modulo implementa la gestione delle company
     *
     * @param navComp - componente standard di navigazione
     * @param modulo  da visualizzare nel placeholder alla pressione del bottone di menu
     */
    private void addMod(NavComponent navComp, WamMod modulo) {
        this.addCompanyListeners(modulo);
        navComp.addMod(modulo);
    }// end of method

    /**
     * Aggiunge un modulo alla UI
     * Il modulo implementa la gestione delle company
     *
     * @param menu
     * @param modulo da visualizzare nel placeholder alla pressione del bottone di menu
     */
    private MenuBar.MenuItem addMod(MenuBar menu, ModulePop modulo) {
        MenuBar.MenuItem menuItem = null;
        String label = modulo.getMenuLabel();
        Resource icon = modulo.getMenuIcon();

        WamMenuCommand cmd = new WamMenuCommand(null, modulo, this);
        menuItem = menu.addItem(label, icon, cmd);

        if (modulo instanceof WamMod) {
            addCompanyListeners((WamMod) modulo);
        }// end of if cycle

        return menuItem;
    }// end of method


    /**
     * Aggiunge un modulo alla UI
     * Il modulo implementa la gestione delle company
     *
     * @param menu
     * @param modulo da visualizzare nel placeholder alla pressione del bottone di menu
     */
    private MenuBar.MenuItem addMod(MenuBar.MenuItem menu, ModulePop modulo) {
        MenuBar.MenuItem menuItem = null;
        String label = modulo.getMenuLabel();
        Resource icon = modulo.getMenuIcon();

        WamMenuCommand cmd = new WamMenuCommand(null, modulo, this);
        menuItem = menu.addItem(label, icon, cmd);

        if (menuItem != null) {
            modulo.addSottoMenu(menuItem);
        }// end of if cycle

        if (modulo instanceof WamMod) {
            addCompanyListeners((WamMod) modulo);
        }// end of if cycle

        return menuItem;
    }// end of method


    /**
     * Crea il componente per l'utente
     *
     * @return il componente creato
     */
    private Component creaCompUtente() {

        // creo un componente standard di navigazione
        NavComponent nc = new NavComponent(this);

        // aggiungo le view - la menubar viene riempita automaticamente
        MenuBar.MenuItem itemFunzione = nc.addView(FunzioneMod.class, FunzioneMod.MENU_ADDRESS, FontAwesome.CHECK_SQUARE_O);
        nc.addView(ServizioMod.class, ServizioMod.MENU_ADDRESS, FontAwesome.TASKS);
        MenuBar.MenuItem itemVolontario = nc.addView(VolontarioMod.class, VolontarioMod.MENU_ADDRESS, FontAwesome.USER);
//        nc.setFooter(new Label("Footer text"));

        // aggiungo un MenuItem con il tabellone.
        // volendo posso anche aggiungerlo nella posizione desiderata
        MenuBar mb = nc.getMenuBar();
        menubar = mb;

        mb.addItemBefore("Tabellone", FontAwesome.CALENDAR_O, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Tabellone tab = getTabellone();
                UI.getCurrent().setContent(tab);
            }
        }, itemFunzione);

        // da chiamare dopo che ho aggiunto tutti i MenuItems,
        // configura il Navigator in base alla MenuBar
        nc.setup();

        return nc;

    }



    /**
     * Crea i sottomenu specifici per le tavole di incrocio (solo per developer, ovviamente)
     * <p>
     *
     * @param menuItem principale del modulo
     */
    public void addSottoMenuIncroci(MenuBar menuBar, MenuBar.MenuItem menuItem) {

        MenuCommand cmd = new MenuCommand(menuBar, new ServizioFunzioneMod());
        menuItem.addItem(ServizioFunzioneMod.MENU_ADDRESS, null, cmd);

        MenuCommand cmd2 = new MenuCommand(menuBar, new VolontarioFunzioneMod());
        menuItem.addItem(VolontarioFunzioneMod.MENU_ADDRESS, null, cmd2);

    }// end of method


    private String getCurrentAddress() {
        URI uri = Page.getCurrent().getLocation();
        String str = uri.getScheme() + ":" + uri.getSchemeSpecificPart();

        if (LibSession.isDeveloper()) {
            str = uri.getScheme() + "://" + uri.getRawAuthority() + "/wam?" + uri.getQuery();
        }// fine del blocco if

        return str;
    }

    public void addCompanyListeners(CompanyListener listener) {
        companyListeners.add(listener);
    }// end of method


    public void fireCompanyAdded(WamCompany company) {
        for (CompanyListener listener : companyListeners) {
            listener.companyAdded(company);
        }// end of for cycle
    }// end of method

    public void fireCompanyRemoved(WamCompany company) {
        for (CompanyListener listener : companyListeners) {
            listener.companyRemoved(company);
        }// end of for cycle
    }// end of method

    public void fireCompanyChanged(WamCompany company) {
        for (CompanyListener listener : companyListeners) {
            listener.companyChanged(company);
        }// end of for cycle
    }// end of method


    public void removeMenuItem(String caption) {
        if (menubar == null) {
            return;
        }// end of if cycle

        List<MenuBar.MenuItem> items = menubar.getItems();
        for (MenuBar.MenuItem item : items) {
            if (item.getText().equals(caption)) {
                menubar.removeItem(item);
                break;
            }// fine del blocco if
        }// end of for cycle
    }// end of method

    /**
     * The item has been selected.
     * Navigate to the View and select the item in the menubar
     */
    public void menuSelected(MenuBar.MenuItem selectedItem) {
        int a = 87;
    }// end of method


    /**
     * Ritorna la unica istanza per sessione del Tabellone
     */
    private Tabellone getTabellone(){
        Tabellone tab;
        Object obj = LibSession.getAttribute("TABELLONE");
        if(obj==null) {
            tab = new Tabellone(getCurrentAddress());
            LibSession.setAttribute("TABELLONE", tab);
        }else{
            tab=(Tabellone)obj;
        }
        return tab;
    }

}// end of class
