package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import it.algos.wam.entity.funzione.FunzioneMod;
import it.algos.wam.entity.milite.MiliteMod;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.entity.wamcompany.WamCompanyMod;
import it.algos.wam.lib.WamRuoli;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.screen.ErrorScreen;
import it.algos.webbase.web.ui.AlgosUI;

/**
 * Created by Gac on 08 mar 2016.
 */
@Theme("valo")
public class WamUI extends AlgosUI {

    private Navigator nav;
    private MenuBar menuBar = null;

    /**
     * Initializes this UI. This method is intended to be overridden by subclasses to build the view and configure
     * non-component functionality. Performing the initialization in a constructor is not suggested as the state of the
     * UI is not properly set up when the constructor is invoked.
     * <p>
     * The {@link VaadinRequest} can be used to get information about the request that caused this UI to be created.
     * </p>
     * Se viene sovrascritto dalla sottoclasse, deve (DEVE) richiamare anche il metodo della superclasse
     * di norma dopo (DOPO) aver effettuato alcune regolazioni <br>
     * Nella sottoclasse specifica viene eventualmente regolato il nome del modulo di partenza <br>
     *
     * @param request the Vaadin request that caused this UI to be created
     */
    @Override
    protected void init(VaadinRequest request) {
//        footerLayout.addComponent(new Label("Wam versione 0.1 del 1 mar 2016"));
        WamCompany company = null;
        Component comp;

        //--legge la croce
        company = leggeCroce(request);

        if (company != null) {
            //--controlla la property della croce, per sapere se far partire subito il tabellone
            if (company.isVaiSubitoTabellone()) {  // mostra subito il tabellone senza login
                comp = new ErrorScreen("qui ci va il tabellone");
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
     * @param request the Vaadin request that caused this UI to be created
     * @return la croce selezionata (company)
     */
    private WamCompany leggeCroce(VaadinRequest request) {
        WamCompany company = null;
        String path;
        String siglaCroce = "";
        String[] parti = null;

        path = request.getPathInfo();
        if (path != null && !path.equals("")) {
            parti = path.split("/");
        }// end of if cycle

        if (parti != null && parti.length > 1) {
            siglaCroce = parti[1];
        }// fine del blocco if

        //legge la croce
        company = WamCompany.findByCode(siglaCroce);
        if (company == null) {
            company = WamCompany.findByCode("demo");
        }// end of if cycle
        CompanySessionLib.setCompany(company);

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
        return utente;
    }// end of method

    private Component gatErrorComp() {
        return new ErrorScreen("Errore");
    }

//    private void configProgrammatore(BaseComponent baseComp, boolean vaiSubitoTabellone) {
////        ModulePop modMilite = new MiliteMod();
//        nav = baseComp.getNav();
//        this.add(new WamCompanyMod());
//        this.add(new MiliteMod());
//        this.add(new FunzioneMod());
//
//        if (vaiSubitoTabellone) {
//            nav.navigateTo("tabellone");
//        } else {
//            nav.navigateTo(MiliteMod.MENU_ADDRESS);
//        }// fine del blocco if-else
//
//        baseComp.putHeader(menuBar);
//    }// end of method


    /**
     * Crea il componente per il programmatore
     *
     * @return il componente creato
     */
    private Component creaCompProgrammatore() {
        BaseComponent comp = new BaseComponent(this);

//        ModulePop modMilite = new MiliteMod();
        nav = comp.getNav();

        menuBar = new MenuBar();
        this.add(new WamCompanyMod());
        this.add(new MiliteMod());
        this.add(new FunzioneMod());
        comp.putHeader(menuBar);

        nav.navigateTo(MiliteMod.MENU_ADDRESS);

        return comp;

    }

    private void configCustode(BaseComponent baseComp) {
    }

    private void configAdmin(BaseComponent baseComp) {
    }

    private void configUtente(BaseComponent baseComp) {
    }

    private void configGuest(BaseComponent baseComp) {
    }

    private void add(ModulePop mod) {
        nav.addView(mod.getMenuLabel(), mod);

        menuBar.addItem(mod.getMenuLabel(), new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                nav.navigateTo(mod.getMenuLabel());
            }
        });
    }

//    private void creaComponenteBase(boolean usaTabellone){
//        VerticalLayout layout=new VerticalLayout();
//        layout.addComponent();
//
//    }
}
