package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import it.algos.wam.entity.company.CompanyModulo;
import it.algos.webbase.web.ui.AlgosUI;

/**
 * La UI del manager.
 * Il manager amministra le aziende, crea e cancella aziende, opera sui contratti,
 * accede alle preferenze globali ecc...
 * Il manager siamo noi.
 */
@Theme("valo")
public class ManagerUI extends AlgosUI {

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);
    }


    /**
     * Aggiunge i moduli specifici dell'applicazione
     */
    @Override
    protected void addModuli() {
        addView(CompanyModulo.class, "Aziende", null);
    }

}

