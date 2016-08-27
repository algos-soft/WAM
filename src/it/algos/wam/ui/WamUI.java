package it.algos.wam.ui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.*;
import it.algos.wam.entity.companyentity.CompanyListener;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.funzione.FunzioneMod;
import it.algos.wam.entity.servizio.ServizioMod;
import it.algos.wam.entity.serviziofunzione.ServizioFunzioneMod;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.volontario.VolontarioMod;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzioneMod;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.entity.wamcompany.WamCompanyMod;
import it.algos.wam.lib.WamRuoli;
import it.algos.wam.login.WamLogin;
import it.algos.wam.menu.WamMenuCommand;
import it.algos.wam.settings.CompanyPrefs;
import it.algos.wam.settings.ConfigScreen;
import it.algos.wam.settings.MgrConfigScreen;
import it.algos.wam.tabellone.Tabellone;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.log.LogMod;
import it.algos.webbase.domain.pref.PrefMod;
import it.algos.webbase.domain.utente.UtenteModulo;
import it.algos.webbase.domain.vers.VersMod;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.*;
import it.algos.webbase.web.menu.AMenuBar;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.navigator.MenuCommand;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * UI principale e unica del programma.
 * Questa classe DEVE estendere AlgosUI per evitare di duplicare tutti i metodi (utili) già esistenti nella superclasse gac/7-8-16
 */
@Theme("wam")
@Push(value = PushMode.AUTOMATIC, transport = Transport.WEBSOCKET_XHR)   // se non uso questo tipo di transport i cookies non funzionano
public class WamUI extends UI {

    //--backdoor password
    public static final String BACKDOOR = "gac";
    public static final String KEY_TABELLONE = "tabellone";
    public static final String KEY_MAIN_COMP = "maincomp";
    public static final String KEY_TABVISIBLE = "tabvisible";
    public static final String KEY_GOHOME = "gohome";
    // componente standard di navigazione
    NavComponent navComp;
    private MenuBar menubar;
    // si registra chi è interessato alle modifiche delle company (aggiunta, cancIscrizione, modifica di quella corrente)
    private ArrayList<CompanyListener> companyListeners = new ArrayList<>();

    /**
     * @param request the Vaadin request that caused this UI to be created
     */
    @Override
    protected void init(VaadinRequest request) {

        // controlla l'accesso come programmatore come parametro nell'url
        // attiva il flag developer nella sessione
        if (checkDeveloper(request)) {
            LibSession.setDeveloper(true);
            developerInit();
            return;
        } else {
            if (LibSession.isDeveloper()) {
                developerInit();
                return;
            }// end of if cycle

//            LibSession.setDeveloper(false);
        }// end of if/else cycle

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
        if (company == null) {
            Component comp = new WamErrComponent("Company " + companyName + " non trovata nel database");
            UI.getCurrent().setContent(comp);
            return;
        }

        // Se c'è una company nella sessione e la company dell'URL è diversa dalla company della sessione
        // eseguo un logout automatico (che elimina la company dalla sessione)
        BaseCompany sessionComp = CompanySessionLib.getCompany();
        if (sessionComp != null) {
            if (!company.equals(sessionComp)) {
                Login.getLogin().logout();
                return;
            }
        }

        // registra la company nella sessione
        CompanySessionLib.setCompany(company);

        // Se non c'è ancora l'oggetto Login, lo crea ora e lo inietta nella sessione.
        // L'oggetto Login è unico nella sessione.
        // Questa applicazione necessita di una logica di login specifica (WamLogin)
        // (inizialmente il Login non ha user e quindi non è in stato logged).
        if (!Login.isEsisteLoginInSessione()) {
            Login.setLogin(new WamLogin(company));

            // listener dei tentativi di login
            Login.getLogin().setLoginListener(new LoginListener() {
                @Override
                public void onUserLogin(LoginEvent e) {
                    if (e.isSuccess()) {

                        // al login, annulla il tabellone e il main component di sessione per farli ricreare
                        LibSession.setAttribute(KEY_TABELLONE, null);
                        LibSession.setAttribute(KEY_MAIN_COMP, null);

                        // si parte sempre dal tabellone
                        UI.getCurrent().setContent(getTabellone());

                    } else {
                        Object obj;
                        WamLogin wamLogin = null;
                        AbsLoginForm form = null;
                        String backDoorKey = "";
                        obj = e.getSource();
                        if (obj instanceof WamLogin) {
                            wamLogin = (WamLogin) obj;
                        }// end of if cycle3
                        if (wamLogin != null) {
                            form = wamLogin.getLoginForm();
                        }// end of if cycle
                        if (form != null) {
                            backDoorKey = form.getPassField().getValue();
                        }// end of if cycle

                        if (backDoorKey != null && backDoorKey.equals(BACKDOOR)) {
//                            Notification notif = new Notification("Sei entrato come programmatore", "", Notification.Type.WARNING_MESSAGE);
                            LibSession.setDeveloper(true);
                        } else {
                            Notification notif = new Notification("Username o password errati", "", Notification.Type.ERROR_MESSAGE);
                            notif.show(Page.getCurrent());
                        }// end of if/else cycle
//                            Notification notif = new Notification("Username o password errati", "", Notification.Type.ERROR_MESSAGE);
//                            notif.show(Page.getCurrent());

                    }
                }
            });

            // listener dei logout
            Login.getLogin().addLogoutListener(new LogoutListener() {
                @Override
                public void onUserLogout(LogoutEvent e) {
                    LibSession.setAttribute(KEY_TABELLONE, null); // annulla il tabellone di sessione per farlo ricreare
                    Page.getCurrent().reload();
                }
            });

        }
        boolean loggato = Login.getLogin().isLogged();

        //--controllo se l'url contiene un login valido
        String utente = request.getParameter("utente");
        String password = request.getParameter("password");
        if (utente != null && !utente.equals("")) {
            List<Volontario> militiPerCognome = (List<Volontario>) CompanyQuery.queryList(Volontario.class, Volontario_.cognome, utente);
            if (militiPerCognome != null && militiPerCognome.size() > 0) {
                Volontario volontario = militiPerCognome.get(0);
                if (volontario.getPassword().equals(password)) {
                    Login.getLogin().setUser(volontario);
                }// end of if cycle
            }// end of if cycle
        }// end of if cycle

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

        // Se la company prevede tabellone pubblico, mostra il tabellone prima del login
        // (se non viene dal goHome() del tabellone stesso)
        boolean tabPubblico = CompanyPrefs.tabellonePubblico.getBool(company);
        if (tabPubblico) {
            if (!LibSession.isAttribute(KEY_GOHOME)) {
                UI.getCurrent().setContent(getTabellone());
                return;
            }
            LibSession.setAttribute(KEY_GOHOME, null);
        }

        // Se non già loggato, presento la pagina di login
        if (!Login.getLogin().isLogged()) {
            Login.getLogin().readCookies();
            UI.getCurrent().setContent(new WamLoginComponent(Login.getLogin()));
            return;
        }

        // se arrivo qui sono già loggato
        // presento la home
        UI.getCurrent().setContent(getMainComponent());

    }

    /**
     * Init per il developer
     */
    private void developerInit() {
        fixCompanySession();
        UI.getCurrent().setContent(getMainComponent());
    }

    /**
     * Elabora l'URL della Request ed estrae (se esiste) il parametro programmatore
     *
     * @param request the Vaadin request that caused this UI to be created
     *
     * @return true se l'url è di un developer
     */
    private boolean checkDeveloper(VaadinRequest request) {
        boolean isProg = false;

        String prog = request.getParameter(WamRuoli.developer.getNome());

        if (prog != null && !prog.isEmpty()) {
            if (prog.equals("gac") || prog.equals("alex")) {
                isProg = true;
            }
        }

        return isProg;

    }

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
        navComp = new NavComponent(this);

        // il menu utente c'è sempre
        // aggiunge una menubar con le funzioni di utente
        MenuBar menuBarUtente = new MenuBar();
        // aggiungo un MenuItem con il tabellone.
        menuBarUtente.addItem("Tabellone", FontAwesome.CALENDAR_O, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                UI.getCurrent().setContent(getTabellone());
            }
        });
//        navComp.addView(FunzioneMod.class, FunzioneMod.MENU_ADDRESS, FontAwesome.CHECK_SQUARE);
//        navComp.addView(ServizioMod.class, ServizioMod.MENU_ADDRESS, FontAwesome.TASKS);
//        navComp.addView(VolontarioMod.class, VolontarioMod.MENU_ADDRESS, FontAwesome.USER);

        this.addMod(menuBarUtente, new FunzioneMod());
        this.addMod(menuBarUtente, new ServizioMod());
        this.addMod(menuBarUtente, new VolontarioMod());
        navComp.addMenu(menuBarUtente);

        // controlla se è un admin
        // aggiunge una menubar con le funzioni di admin
        if (LibSession.isAdmin()) {
            MenuBar menuBarAdmin = new MenuBar();
            addMod(menuBarAdmin, new LogMod());
            navComp.addView(menuBarAdmin, ConfigScreen.class, "Impostazioni", FontAwesome.WRENCH);
            navComp.addMenu(menuBarAdmin,"verde");
        }// end of if cycle

        // controlla se è un developer
        // aggiunge una menubar con le funzioni di developer
        if (LibSession.isDeveloper()) {
            MenuBar menuBarDeveloper = new MenuBar();
            menuBarDeveloper.setAutoOpen(true);
            MenuBar.MenuItem menuUtilities = menuBarDeveloper.addItem("Utilities", null, null);
            addMod(menuUtilities, new UtenteModulo("User"));
            addMod(menuUtilities, new VersMod());
            addMod(menuUtilities, new PrefMod());
            navComp.addView(menuBarDeveloper, MgrConfigScreen.class, "Settings", FontAwesome.WRENCH);
            addMod(menuBarDeveloper, new WamCompanyMod());

            MenuBar.MenuItem menuTavole = menuBarDeveloper.addItem("Tavole", null, null);
            addMod(menuTavole, new ServizioFunzioneMod());
            addMod(menuTavole, new VolontarioFunzioneMod());

            navComp.addMenu(menuBarDeveloper,"rosso");
        }// end of if cycle

        // seleziona inizialmente il menuItem Funzioni
        List<MenuBar.MenuItem> items = menuBarUtente.getItems();
        for (MenuBar.MenuItem item : items) {
            if (item.getText().equals(FunzioneMod.MENU_ADDRESS)) {
                item.getCommand().menuSelected(item);
                break;
            }// end of if cycle
        }// end of for cycle

        //--footer
        navComp.setFooter(creaFooter());
        return navComp;
    }// end of method

    private MenuBar.MenuItem createMenuItem(MenuBar.MenuItem menu, Class<? extends View> viewClass, String label, boolean cached, Resource icon) {
        MenuBar.MenuItem menuItem;
//        MenuCommand cmd = new MenuBar.Command(menu, viewClass, cached);
        menuItem = menu.addItem(label, icon, null);
        menuItem.setStyleName(AMenuBar.MENU_DISABILITATO);
        return menuItem;
    }// end of method

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

        if (menuItem != null) {
            modulo.addSottoMenu(menuItem);
        }// end of if cycle

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

//    /**
//     * Crea il componente per l'utente
//     *
//     * @return il componente creato
//     */
//    private Component creaCompUtente() {
//
//        // creo un componente standard di navigazione
//        NavComponent nc = new NavComponent(this);
//
//        // aggiungo le view - la menubar viene riempita automaticamente
//        MenuBar.MenuItem itemFunzione = nc.addView(FunzioneMod.class, FunzioneMod.MENU_ADDRESS, FontAwesome.CHECK_SQUARE_O);
//        nc.addView(ServizioMod.class, ServizioMod.MENU_ADDRESS, FontAwesome.TASKS);
//        MenuBar.MenuItem itemVolontario = nc.addView(VolontarioMod.class, VolontarioMod.MENU_ADDRESS, FontAwesome.USER);
////        nc.setFooter(new Label("Footer text"));
//
//        // aggiungo un MenuItem con il tabellone.
//        // volendo posso anche aggiungerlo nella posizione desiderata
//        MenuBar mb = nc.getMenuBar();
//        menubar = mb;
//
//        mb.addItemBefore("Tabellone", FontAwesome.CALENDAR_O, new MenuBar.Command() {
//            @Override
//            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                Tabellone tab = getTabellone();
//                UI.getCurrent().setContent(tab);
//            }
//        }, itemFunzione);
//
//        // da chiamare dopo che ho aggiunto tutti i MenuItems,
//        // configura il Navigator in base alla MenuBar
//        nc.setup();
//
//        return nc;
//
//    }

//    /**
//     * Crea i sottomenu specifici per le tavole di incrocio (solo per developer, ovviamente)
//     * <p>
//     *
//     * @param menuItem principale del modulo
//     */
//    public void addSottoMenuIncroci(MenuBar menuBar, MenuBar.MenuItem menuItem) {
//
//        MenuCommand cmd = new MenuCommand(menuBar, new ServizioFunzioneMod());
//        menuItem.addItem(ServizioFunzioneMod.MENU_ADDRESS, null, cmd);
//
//        MenuCommand cmd2 = new MenuCommand(menuBar, new VolontarioFunzioneMod());
//        menuItem.addItem(VolontarioFunzioneMod.MENU_ADDRESS, null, cmd2);
//
//    }// end of method

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
        navComp.setFooter(creaFooter());
    }// end of method

    @Deprecated
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
     * Ritorna un nuovo Tabellone
     */
    private Tabellone getTabellone() {
//        Tabellone tab;
//        Object obj = LibSession.getAttribute(KEY_TABELLONE);
//        if (obj == null) {
//            tab = new Tabellone(getCurrentAddress());
//            LibSession.setAttribute(KEY_TABELLONE, tab);
//        } else {
//            tab = (Tabellone) obj;
//        }
//        return tab;

        return new Tabellone(getCurrentAddress());
    }

    /**
     * Ritorna un nuovo componente main
     */
    private Component getMainComponent() {
//        Component comp;
//        Object obj = LibSession.getAttribute(KEY_MAIN_COMP);
//        if (obj == null) {
//            comp = creaComponente();
//            LibSession.setAttribute(KEY_MAIN_COMP, comp);
//        } else {
//            comp = (Component) obj;
//        }
//        return comp;
        return creaComponente();    // per ora è sempre nuova istanza
    }

    /**
     * Crea l'interfaccia utente (User Interface) iniziale dell'applicazione
     * Footer - un striscia per eventuali informazioni (Algo, copyright, ecc)
     * Le applicazioni specifiche, possono sovrascrivere questo metodo nella sottoclasse
     *
     * @return layout - normalmente un HorizontalLayout
     */
    //@todo metodo già esistente in AlgosUI - Occorre che questa classe estenda AlgosUI gac/7-8-16
    protected HorizontalLayout creaFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(false, false, false, false));
        footer.setSpacing(true);
        footer.setHeight("30px");

        footer.addComponent(new Label("Algos s.r.l."));
        footer.addComponent(new Label("-"));
        footer.addComponent(new Label("webAmbulanze"));

        if (LibSession.isDeveloper()) {
            footer.addComponent(new Label("- programmatore"));
        } else {
            if (LibSession.isAdmin()) {
                footer.addComponent(new Label("- admin"));
            }// end of if/else cycle
        }// end of if/else cycle

        if (CompanySessionLib.getCompany() != null) {
            footer.addComponent(new Label("- " + CompanySessionLib.getCompany().getCompanyCode()));
        }// end of if cycle

        if (LibSession.isDeveloper()) {
            footer.addStyleName("rosso");
        } else {
            if (LibSession.isAdmin()) {
                footer.addStyleName("verde");
            }// end of if/else cycle
        }// end of if/else cycle

        return footer;
    }// end of method

}// end of class
