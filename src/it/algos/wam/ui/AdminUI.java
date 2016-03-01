package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import it.algos.wam.entity.company.CompanyMod;
import it.algos.wam.entity.funzione.FunzioneMod;
import it.algos.wam.entity.milite.MiliteMod;
import it.algos.wam.entity.servizio.*;
import it.algos.wam.entity.turno.TurnoMod;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.ui.AlgosUI;

/**
 * La UI dell'admin
 * L'admin amministra tutte le preferenze dell'azienda
 * L'admin accede a tutte le aziende
 * L'admin siamo noi
 */
@Theme("valo")
public class AdminUI extends AlgosUI {


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
        super.init(request);
        footerLayout.addComponent(new Label("Wam versione 0.1 del 1 mar 2016"));
    }// end of method

    /*
     * Aggiunge i moduli specifici dell'applicazione
     */
    @Override
    protected void addModuli() {
        this.addModulo(new CompanyMod());
        this.addModulo(new MiliteMod());
        this.addModulo(new FunzioneMod());
        this.addModulo(new ServizioMod());
        this.addModulo(new TurnoMod());
    }// end of method

}// end of class
