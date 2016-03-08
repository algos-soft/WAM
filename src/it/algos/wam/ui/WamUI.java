package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import it.algos.wam.entity.funzione.FunzioneMod;
import it.algos.wam.entity.milite.MiliteMod;
import it.algos.wam.entity.servizio.ServizioMod;
import it.algos.wam.entity.turno.TurnoMod;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.entity.wamcompany.WamCompanyMod;
import it.algos.wam.lib.WamRuoli;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.ruolo.Ruolo;
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
        boolean vaiSubitoTabellone = false;
        Ruolo ruolo = new Ruolo();
        String nomeRuolo;
        menuBar = new MenuBar();
        Component baseComp = null;

        //--legge la croce
        //--vedere se serve il login per vedere il tabellone (dipende dalla croce)
        vaiSubitoTabellone = leggeCroce(request);

        //@todo programmatore (da leggere)
        nomeRuolo = ruolo.getNome();
        WamRuoli eRuolo = WamRuoli.get(nomeRuolo);
        eRuolo = WamRuoli.developer;
        if (eRuolo != null) {
            switch (eRuolo) {
                case developer:
                    baseComp = new BaseComponent(this);
                    configProgrammatore((BaseComponent) baseComp, vaiSubitoTabellone);
                    break;
                case custode:

                    break;
                case admin:

                    break;
                case user:

                    break;
//            case guest:
////                configUtente(baseComp, true);
//                break;
                default: // caso non definito
                    if (vaiSubitoTabellone) {
                        baseComp = new BaseComponent(this);
                        configGuest((BaseComponent) baseComp);
                    } else {
                        baseComp = gatErrorComp();
                    }// fine del blocco if-else
                    break;
            } // fine del blocco switch
        } else {
            baseComp = gatErrorComp();
        }// fine del blocco if-else

        this.setContent(baseComp);

    }// end of method

    /**
     * Elabora l'URL della Request ed estrae (se esiste) il nome della croce selezionata
     * Se non trova nulla, di default parte con la croce 'demo'
     * Se il nome della croce è sbagliato o non esiste nella lista delle croci esistenti (?), mostra un messaggio di errore
     * Registra la croce (come Company) nella Sessione corrente
     * Controlla la property della croce, per sapere se far partire subito il tabellone
     *
     * @param request the Vaadin request that caused this UI to be created
     * @return la partenza immediata del tabellone senza login
     */
    private boolean leggeCroce(VaadinRequest request) {
        boolean vaiSubitoTabellone = false;
        BaseCompany company = null;
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

        if (company != null) {
            vaiSubitoTabellone = ((WamCompany) company).isVaiSubitoTabellone();
        }// end of if cycle

        return vaiSubitoTabellone;
    }// end of method


    private Component gatErrorComp() {
        return new ErrorScreen("Errore");
    }

    private void configProgrammatore(BaseComponent baseComp, boolean vaiSubitoTabellone) {
//        ModulePop modMilite = new MiliteMod();
        nav = baseComp.getNav();
        this.add(new WamCompanyMod());
        this.add(new MiliteMod());
        this.add(new FunzioneMod());

        if (vaiSubitoTabellone) {
            nav.navigateTo("tabellone");
        } else {
            nav.navigateTo(MiliteMod.MENU_ADDRESS);
        }// fine del blocco if-else

        baseComp.putHeader(menuBar);

    }

    private void configCustode(BaseComponent baseComp) {
        this.add(new WamCompanyMod());
        this.addModulo(new MiliteMod());
        this.addModulo(new FunzioneMod());
        this.addModulo(new ServizioMod());
        this.addModulo(new TurnoMod());
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
