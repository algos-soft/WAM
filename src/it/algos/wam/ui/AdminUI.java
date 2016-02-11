package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import it.algos.wam.entity.company.CompanyModulo;
import it.algos.wam.entity.milite.MiliteModulo;
import it.algos.webbase.web.ui.AlgosUI;

/**
 * La UI dell'Admin
 * L'Admin amministra gli utenti, crea i turni, esegue le statistiche, configura l'applicazione...
 */
@Theme("valo")
public class AdminUI extends AlgosUI {

    /*
     * Aggiunge i moduli specifici dell'applicazione
     */
    @Override
    protected void addModuli() {
        addView(MiliteModulo.class, "Militi", null);
    }

}
