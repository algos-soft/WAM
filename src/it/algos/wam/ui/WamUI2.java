package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
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
import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.screen.ErrorScreen;
import it.algos.webbase.web.ui.AlgosUI;

import java.net.URI;

/**
 * Created by Gac on 08 mar 2016.
 */
@Theme("valo")
public class WamUI2 extends AlgosUI {

    private Navigator nav;
    private MenuBar menuBar;

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


        // recupero la company dall'url
        URI uri = Page.getCurrent().getLocation();
        String path = uri.getPath();
        String[] parti = path.split("/");
        String pcompany = null;
        for (int i = 0; i < parti.length; i++) {
            if (i > 1) {
                pcompany = parti[i];
                break;
            }
        }

        Component comp;

        // recupero la Company dal db
        if (pcompany != null) {
            WamCompany company = WamCompany.findByCode(pcompany);
            if (company != null) {
                CompanySessionLib.setCompany(company);
                if (company.isVaiSubitoTabellone()) {  // mostra subito il tabellone senza login
                    comp = new ErrorScreen("qui ci va il tabellone");
                } else {
                    // chiedo il login - se riuscito registro l'utente nella sessione
                    Utente user = login();
                    if (user != null) {

                        // recupero il ruolo dall'utente
                        Ruolo ruolo = new Ruolo();  // provvisorio
                        ruolo.setNome("prog");
                        //ruolo.setNome(TipoRuolo.developer.get);

                        // crea il componente relativo al ruolo dell'utente
                        comp = creaCompPerRuolo(ruolo);

                    } else {
                        comp = new ErrorScreen("Login fallito");
                    }
                }

            } else {
                // company non trovata nel db
                comp = new ErrorScreen("Azienda " + pcompany + " non trovata");
            }

        } else {
            // company non specificata nell'url
            comp = new ErrorScreen("Nome azienda non specificato nell'url");
        }

        this.setContent(comp);

    }// end of method


    /**
     * Crea il componente da visualizzare in funzione del ruolo.
     *
     * @param ruolo il ruolo
     * @return il componente
     */
    private Component creaCompPerRuolo(Ruolo ruolo) {
        String nomeRuolo = ruolo.getNome();
        WamRuoli eRuolo = WamRuoli.get(nomeRuolo);

        eRuolo=WamRuoli.developer;  // provvisorio

        Component comp=null;

        switch (eRuolo) {
            case developer:
                comp=creaCompProgrammatore();
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

    }

    /**
     * Presento il dialogo di login
     *
     * @return l'utente loggato, null se fallito
     */
    private Utente login() {
        Utente utente = new Utente();
        return utente;
    }


    /**
     * Crea il componente per il programmatore
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

}
