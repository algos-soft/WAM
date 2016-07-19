package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.server.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import it.algos.wam.bootstrap.TestService;
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
import it.algos.wam.tabellone.Tabellone;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.log.LogMod;
import it.algos.webbase.domain.pref.PrefMod;
import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
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

    // si registra chi è interessato alle modifiche delle company (aggiunta, cancellazione, modifica di quella corrente)
    private ArrayList<CompanyListener> companyListeners = new ArrayList<>();


    /**
     * @param request the Vaadin request that caused this UI to be created
     */
    @Override
    protected void init(VaadinRequest request) {

        Component comp = null;

        // Questa applicazione necessita di una logica di login specifica.
        // Inietto subito l'oggetto Login specifico nella sessione
        WamLogin login = new WamLogin();
        Login.setLogin(login);

        login.addLoginListener(new LoginListener() {
            @Override
            public void onUserLogin(LoginEvent e) {
                Component comp = new Tabellone(getCurrentAddress());
                setContent(comp);
            }
        });

        login.addLogoutListener(new LogoutListener() {
            @Override
            public void onUserLogout(LogoutEvent e) {
                Component comp;
                BaseCompany company = CompanySessionLib.getCompany();
                if (company != null) {  // logout from company
                    WamCompany wamComp = (WamCompany) company;
                    if (wamComp.isVaiSubitoTabellone()) {
                        comp = new Tabellone(getCurrentAddress());
                    } else {
                        comp = new WamSplashComponent();
                    }
                } else {
                    comp = new WamSplashComponent();
                }

                setContent(comp);

            }
        });


        // controlla l'accesso come programmatore
        boolean prog = leggeBackdoor(request);

        if (!prog) {

            String companyName = getCompanyNameFromUrl();
            if (companyName != null) {
                WamCompany company = WamCompany.findByCode(companyName);
                if (company != null) {

                    // registra la Company nella sessione
                    CompanySessionLib.setCompany(company);

                    // auto login from cookies (solo dopo che abbiamo la Company in sessione!)
                    boolean logged = Login.getLogin().loginFromCookies();
                    logged = true; //@todo il form per desso non funziona

                    if (company.isVaiSubitoTabellone()) {
                        if (checkFirstTime()) {
                            comp = new Tabellone(getCurrentAddress());
                        } else {
                            if (logged) {
                                comp = creaComponente();
                            } else {
                                comp = new WamSplashComponent();
                            }// end of if/else cycle
                        }// end of if/else cycle
                    } else {
                        if (logged) {
                            comp = creaComponente();
                        } else {
                            comp = new WamSplashComponent();
                        }// end of if/else cycle
                    }// end of if/else cycle
                } else {
                    comp = new ErrorScreen("Company " + companyName + " non trovata");
                }// end of if/else cycle

            } else {
                comp = new ErrorScreen("Company non specificata");
            }// end of if/else cycle

        } else {
            fixCompanySession();
            comp = creaComponente();
        }// end of if/else cycle

        this.setContent(comp);

    }// end of method

//    /**
//     * @param request the Vaadin request that caused this UI to be created
//     */
//    //@Override
//    protected void initOld(VaadinRequest request) {
//
//
////        // set theme
////        String themeName;
////        if (Page.getCurrent().getWebBrowser().isTouchDevice()) {
////            themeName = "wam-mob";
////        } else {
////            themeName = "wam";
////        }
////        setTheme(themeName);
//
//        // Questa applicazione necessita di una logica di login specifica
//        // Inietto subito l'oggetto Login nella sessione
//        Login.setLogin(new WamLogin());
//
//        // controlla l'accesso come programmatore
//        leggeBackdoor(request);
//
//        // legge la croce
//        WamCompany company = fixCompanySession();
//
//
//        Component comp;
//
//        if (company != null) {
//
//            // registra la Company nella sessione
//            CompanySessionLib.setCompany(company);
//
//            // auto login from cookies (solo dopo che abbiamo la Company in sessione!)
//            boolean logged = Login.getLogin().loginFromCookies();
//
//
//            //--controlla la property della croce, per sapere se far partire subito il tabellone
//            if (company.isVaiSubitoTabellone()) {  // mostra subito il tabellone senza login
//                if (checkFirstTime()) {
//                    comp = new Tabellone(getCurrentAddress());
//                } else {
//                    comp = creaCompPerRuolo();
//                }
//            } else {
//                //--crea il componente da visualizzare in funzione del ruolo.
//                comp = creaCompPerRuolo();
//                if (comp == null) {
//                    comp = new ErrorScreen("Login fallito");
//                }// end of if cycle
//            }// end of if/else cycle
//
//        } else {
//            if (LibSession.isDeveloper()) {
//                comp = creaCompProgrammatore();
//            } else {
//                // company non specificata nell'url
//                // company non trovata nel db
//                comp = new ErrorScreen("Nome azienda non specificato nell'url o non esistente");
//            }// end of if/else cycle
//        }// end of if/else cycle
//
//
//        this.setContent(comp);
//
//
//        // log di partenza con uscita in Output
//        TestService.runTest();
//    }// end of method


    /**
     * Controlla se è la prima volta che passa di qui, nell'ambito della sessione
     * Registra la condizione nella sessione corrente
     */
    private boolean checkFirstTime() {
        boolean primaVolta = false;

        if (LibSession.isFirstTime()) {
            primaVolta = true;
        }// end of if/else cycle
        LibSession.setFirstTime(false);

        return primaVolta;
    }// end of method

    /**
     * Elabora l'URL della Request ed estrae (se esiste) il parametro programmatore
     * Se non trova nulla, di default parte come utente normale
     * È sempre possibile effettuare il login
     * Registra la condizione (di Prog) nella Sessione corrente
     *
     * @param request the Vaadin request that caused this UI to be created
     */
    private boolean leggeBackdoor(VaadinRequest request) {
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


//    /**
//     * Crea il componente da visualizzare in funzione del ruolo.
//     *
//     * @return il componente
//     */
//    private Component creaCompPerRuolo() {
//        Component comp = null;
//
//        // chiedo il login - se riuscito registro l'utente nella sessione
//        Utente user = login();
//
//        // recupero il ruolo dall'utente
//        Ruolo ruolo = new Ruolo();  // provvisorio
//        ruolo.setNome("prog");
//        //ruolo.setNome(TipoRuolo.developer.get);
//
//        String nomeRuolo = ruolo.getNome();
//        WamRuoli eRuolo = WamRuoli.get(nomeRuolo);
//
//        // provvisorio
//        if (LibSession.isDeveloper()) {
//            eRuolo = WamRuoli.developer;
//        } else {
//            eRuolo = WamRuoli.user;
//        }// end of if/else cycle
//
//        switch (eRuolo) {
//            case developer:
//                comp = creaCompProgrammatore();
//                break;
//            case custode:
//
//                break;
//            case admin:
//
//                break;
//            case user:
//                comp = creaCompUtente();
//                break;
//            default: // caso non definito
//                break;
//        }
//
//        return comp;
//    }// end of method

    /**
     * Presento il dialogo di login
     *
     * @return l'utente loggato, null se fallito
     */
    private Utente login() {
        Utente utente = new Utente();
//        Ruolo ruolo = new Ruolo();
//        ruolo.setNome(WamRuoli.developer.getNome());
//        utente.set
        return utente;
    }// end of method

//    /**
//     * Crea il componente per il programmatore
//     *
//     * @return il componente creato
//     */
//    private Component creaCompProgrammatore() {
//        NavComponent nc = (NavComponent) creaCompUtente();
////        MenuBar.MenuItem itemCroce;
////        MenuBar.MenuItem itemIncroci;
////
////        /* creo un componente standard di navigazione */
////        NavComponent nc = new NavComponent(this);
////        MenuBar mb = nc.getMenuBar();
////        menubar = mb;
////
////        // aggiungo le view - la menubar viene riempita automaticamente
////        nc.addMod(new UtenteModulo("User"));
////        nc.addMod(new VersMod());
////        nc.addMod(new LogMod());
////        nc.addMod(new PrefMod());
////
////        itemCroce = nc.addMod(new WamCompanyMod());
////
////        addMod(nc, new FunzioneMod());
////        addMod(nc, new ServizioMod());
////        addMod(nc, new VolontarioMod());
////
////        itemIncroci = nc.getMenuBar().addItem("Incroci", null, null);
////        addSottoMenuIncroci(mb, itemIncroci);
////
////        addMod(nc, new TurnoMod());
////
//////        nc.setFooter(new Label("Footer text"));
////
////        // aggiungo un MenuItem con il tabellone.
////        // volendo posso anche aggiungerlo nella posizione desiderata
////        mb.addItemBefore("Tabellone", FontAwesome.CALENDAR_O, new MenuBar.Command() {
////            @Override
////            public void menuSelected(MenuBar.MenuItem selectedItem) {
////                Tabellone tab = new Tabellone(getCurrentAddress());
////                setContent(tab);
////            }
////        }, itemCroce);
////
////        // da chiamare dopo che ho aggiunto tutti i MenuItems,
////        // configura il Navigator in base alla MenuBar
////        nc.setup();
////
////        // modulo iniziale (per programmatori)
////        nc.navigateTo(WamCompanyMod.class);
//
//        MenuBarWithLogin menu = ((MenuBarWithLogin) nc.getComponent(0));
//
//        // controlla se è un admin
//        if (true) {
//            MenuBar menuBarAdmin = new MenuBar();
//            menuBarAdmin.addStyleName("salmone");
//            MenuBar.MenuItem menuItem;
//            menuItem = menuBarAdmin.addItem("Admin", FontAwesome.USER_MD, null);
//            createMenuItem(menuItem, LogMod.class, "Logo", true, FontAwesome.TASKS);
//            createMenuItem(menuItem, PrefMod.class, "Pref", true, FontAwesome.TASKS);
//            menu.addMenu(menuBarAdmin);
//        }// end of if cycle
//
//
//        // controlla se è un developer
//        if (true) {
//            MenuBar menuBarDev = new MenuBar();
//            menuBarDev.addStyleName("rosso");
//            MenuBar.MenuItem menuItem2;
//            menuItem2 = menuBarDev.addItem("Prog", FontAwesome.LIGHTBULB_O, null);
//            createMenuItem(menuItem2, UtenteModulo.class, "User", true, FontAwesome.TASKS);
//            createMenuItem(menuItem2, VersMod.class, "Vers", true, FontAwesome.TASKS);
//            menu.addMenu(menuBarDev);
//        }// end of if cycle
//
//
//        return nc;
//    }// end of method

//    /**
//     * Crea il componente per il programmatore
//     *
//     * @return il componente creato
//     */
//    private Component creaComponenteNoGood() {
//        // creo un componente standard di navigazione
//        NavComponent navComp = new NavComponent(this);
//        MenuBar menuBar = navComp.getMenuBar();
//
//        // aggiungo le view - la menubar viene riempita automaticamente
//        navComp.addView(FunzioneMod.class, FunzioneMod.MENU_ADDRESS, FontAwesome.CHECK_SQUARE);
//        navComp.addView(ServizioMod.class, ServizioMod.MENU_ADDRESS, FontAwesome.TASKS);
//        navComp.addView(VolontarioMod.class, VolontarioMod.MENU_ADDRESS, FontAwesome.USER);
////        addMod(menuBar, new FunzioneMod());
////        addMod(menuBar, new ServizioMod());
////        addMod(menuBar, new VolontarioMod());
//
//        // aggiungo un MenuItem con il tabellone.
//        // volendo posso anche aggiungerlo nella posizione desiderata
//        menuBar.addItem("Tabellone", FontAwesome.CALENDAR_O, new MenuBar.Command() {
//            @Override
//            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                Tabellone tab = new Tabellone(getCurrentAddress());
//                setContent(tab);
//            }// end of inner method
//        });// end of anonymous inner class
//
//        MenuBarWithLogin menu = (MenuBarWithLogin) navComp.getComponent(0);
//        // controlla se è un admin
//        if (LibSession.isDeveloper()) {
//            MenuBar menuBarAdmin = new MenuBar();
//            menuBarAdmin.addStyleName("salmone");
//            MenuBar.MenuItem menuItem;
//            menuItem = menuBarAdmin.addItem("Admin", FontAwesome.USER_MD, null);
//            addMod(menuItem, new LogMod());
//            addMod(menuItem, new PrefMod());
//            menu.addMenu(menuBarAdmin);
//            navComp.setup(menuBarAdmin);
//        }// end of if cycle
//
//        // controlla se è un developer
//        if (LibSession.isDeveloper()) {
//            MenuBar menuBarDev = new MenuBar();
//            menuBarDev.addStyleName("rosso");
//            MenuBar.MenuItem menuItem2;
//            menuItem2 = menuBarDev.addItem("Prog", FontAwesome.LIGHTBULB_O, null);
//            addMod(menuItem2, new UtenteModulo("User"));
//            addMod(menuItem2, new VersMod());
//            addMod(menuItem2, new WamCompanyMod());
//            menu.addMenu(menuBarDev);
//            navComp.setup(menuBarDev);
//        }// end of if cycle
//
//        // da chiamare dopo che ho aggiunto tutti i MenuItems,
//        // configura il Navigator in base alla MenuBar
//        navComp.setup();
//
//        return navComp;
//    }// end of method


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
        navComp.addView(this, menuBarUtente, FunzioneMod.class, true, FunzioneMod.MENU_ADDRESS, FontAwesome.CHECK_SQUARE);
        navComp.addView(this, menuBarUtente, ServizioMod.class, true, ServizioMod.MENU_ADDRESS, FontAwesome.TASKS);
        navComp.addView(this, menuBarUtente, VolontarioMod.class, true, VolontarioMod.MENU_ADDRESS, FontAwesome.USER);
//        addMod(menuBarUtente, new FunzioneMod());
//        addMod(menuBarUtente, new ServizioMod());
//        addMod(menuBarUtente, new VolontarioMod());

        // aggiungo un MenuItem con il tabellone.
        // volendo posso anche aggiungerlo nella posizione desiderata
        menuBarUtente.addItem("Tabellone", FontAwesome.CALENDAR_O, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Tabellone tab = new Tabellone(getCurrentAddress());
                setContent(tab);
            }// end of inner method
        });// end of anonymous inner class

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

        WamMenuCommand cmd = new WamMenuCommand(menu, modulo, this);
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
     * @deprecated
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
//        MenuBar.MenuItem itemVolontari;
//
//        /* creo un componente standard di navigazione */
//        NavComponent nc = new NavComponent(this);
//
//        // aggiungo le view - la menubar viene riempita automaticamente
//        itemVolontari = nc.addMod(new VolontarioMod());
//        nc.addMod(new FunzioneMod());
//        nc.addMod(new ServizioMod());
////        nc.setFooter(new Label("Footer text"));
//
//        // aggiungo un MenuItem con il tabellone.
//        // volendo posso anche aggiungerlo nella posizione desiderata
//        MenuBar mb = nc.getMenuBar();
//        mb.addItemBefore("Tabellone", FontAwesome.CALENDAR_O, new MenuBar.Command() {
//            @Override
//            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                Tabellone tab = new Tabellone(getCurrentAddress());
//                setContent(tab);
//            }
//        }, itemVolontari);
//
//        // da chiamare dopo che ho aggiunto tutti i MenuItems,
//        // configura il Navigator in base alla MenuBar
//        nc.setup();
//
//        return nc;
//    }// end of method

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
//                Tabellone tab = new Tabellone(getCurrentAddress());
//                setContent(tab);
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


    /**
     * Crea il componente per l'admin
     *
     * @return il componente creato
     */
    private Component creaCompAdmin() {
        ErrorScreen comp = new ErrorScreen("Componente Admin - work in progress");
        return comp;
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

    private void configCustode(NavComponent baseComp) {
    }

    private void configAdmin(NavComponent baseComp) {
    }

    private void configUtente(NavComponent baseComp) {
    }

    private void configGuest(NavComponent baseComp) {
    }

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

    // @todo - serve?
    public void removeCompanyChangeListeners() {
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

}// end of class
