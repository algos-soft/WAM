package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import it.algos.wam.entity.funzione.FunzioneMod;
import it.algos.wam.entity.volontario.VolontarioMod;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.entity.wamcompany.WamCompanyMod;
import it.algos.wam.lib.WamRuoli;
import it.algos.wam.tabellone.Tabellone;
import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.screen.ErrorScreen;
import it.algos.webbase.web.ui.AlgosUI;

import java.net.URI;

/**
 * Created by Gac on 08 mar 2016.
 */
public class WamUI extends UI {

    /**
     * @param request the Vaadin request that caused this UI to be created
     */
    @Override
    protected void init(VaadinRequest request) {


        // set theme
        String themeName;
        if (Page.getCurrent().getWebBrowser().isTouchDevice()) {
            themeName = "wam-mob";
        } else {
            themeName = "wam";
        }
        setTheme(themeName);

//        footerLayout.addComponent(new Label("Wam versione 0.1 del 1 mar 2016"));
        WamCompany company = null;
        Component comp;

        // legge la croce
        company = leggeCompany();

        if (company != null) {

            // registra la Company nella sessione
            CompanySessionLib.setCompany(company);

            //--controlla la property della croce, per sapere se far partire subito il tabellone
            if (company.isVaiSubitoTabellone()) {  // mostra subito il tabellone senza login
                String skip = request.getParameter("skip");// se c'è questo parametro non va al tabellone (ma non funziona, vedi goHome() in Tabellone)
                if (skip==null || skip.isEmpty()) {
                    comp = new Tabellone(getCurrentAddress());
                }else{
                    comp = creaCompPerRuolo();
                }
            } else {
                //--crea il componente da visualizzare in funzione del ruolo.
                comp = creaCompPerRuolo();
                if (comp == null) {
                    comp = new ErrorScreen("Login fallito");
                }// end of if cycle
            }// end of if/else cycle

        } else {
            // company non specificata nell'url
            // company non trovata nel db
            comp = new ErrorScreen("Nome azienda non specificato nell'url o non esistente");
        }// end of if/else cycle

        this.setContent(comp);

    }// end of method

    /**
     * Elabora l'URL della Request ed estrae (se esiste) il nome della croce selezionata
     * Se non trova nulla, di default parte con la croce 'demo'
     * Se il nome della croce è sbagliato o non esiste nella lista delle croci esistenti (?), mostra un messaggio di errore
     * Registra la croce (come Company) nella Sessione corrente
     *
     * @return la company selezionata
     */
    private WamCompany leggeCompany() {
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

        return company;

    }// end of method


    /**
     * Crea il componente da visualizzare in funzione del ruolo.
     *
     * @return il componente
     */
    private Component creaCompPerRuolo() {
        Component comp = null;

        // chiedo il login - se riuscito registro l'utente nella sessione
        Utente user = login();

        // recupero il ruolo dall'utente
        Ruolo ruolo = new Ruolo();  // provvisorio
        ruolo.setNome("prog");
        //ruolo.setNome(TipoRuolo.developer.get);

        String nomeRuolo = ruolo.getNome();
        WamRuoli eRuolo = WamRuoli.get(nomeRuolo);

        eRuolo = WamRuoli.developer;  // provvisorio

        switch (eRuolo) {
            case developer:
                comp = creaCompProgrammatore();
                break;
            case custode:

                break;
            case admin:

                break;
            case user:

                break;
            default: // caso non definito
                break;
        }

        return comp;
    }// end of method

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


    /**
     * Crea il componente per il programmatore
     *
     * @return il componente creato
     */
    private Component creaCompProgrammatore() {

        // creo un componente standard di navigazione
        NavComponent nc = new NavComponent(this);

        // aggiungo le view - la menubar viene riempita automaticamente
        MenuBar.MenuItem item = nc.addView(WamCompanyMod.class, WamCompanyMod.MENU_ADDRESS, FontAwesome.AMBULANCE);
        nc.addView(VolontarioMod.class, VolontarioMod.MENU_ADDRESS, FontAwesome.USER);
        nc.addView(FunzioneMod.class, FunzioneMod.MENU_ADDRESS, FontAwesome.TASKS);
        nc.setFooter(new Label("Footer text"));

        // aggiungo un MenuItem con il tabellone.
        // volendo posso anche aggiungerlo nella posizione desiderata
        MenuBar mb = nc.getMenuBar();
        mb.addItemBefore("Tabellone", FontAwesome.CALENDAR_O, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Tabellone tab = new Tabellone(getCurrentAddress());
                setContent(tab);
            }
        }, item);

        // da chiamare dopo che ho aggiunto tutti i MenuItems,
        // configura il Navigator in base alla MenuBar
        nc.setup();

        return nc;

    }

    private void configCustode(NavComponent baseComp) {
    }

    private void configAdmin(NavComponent baseComp) {
    }

    private void configUtente(NavComponent baseComp) {
    }

    private void configGuest(NavComponent baseComp) {
    }

    private String getCurrentAddress(){
        URI uri = Page.getCurrent().getLocation();
        String str = uri.getAuthority()+uri.getPath();
        return str;
    }

}
