package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import it.algos.wam.entity.wamcompany.WamCompanyMod;
import it.algos.webbase.web.ui.AlgosUI;

/**
 * La UI del manager.
 * Il manager amministra alcune preferenze di una singola azienda (non tutte)
 * Il manager amministra gli utenti, crea i turni, controlla le statistiche
 * Il manager NON accede alle altre aziende
 * Il manager NON siamo noi.
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
        addView(WamCompanyMod.class, "Aziende", null);
    }

}

