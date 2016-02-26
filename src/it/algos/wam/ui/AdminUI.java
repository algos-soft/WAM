package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import it.algos.wam.entity.company.CompanyMod;
import it.algos.wam.entity.milite.MiliteMod;
import it.algos.webbase.web.ui.AlgosUI;
import it.algos.wam.entity.funzione.*;

/**
 * La UI dell'admin
 * L'admin amministra tutte le preferenze dell'azienda
 * L'admin accede a tutte le aziende
 * L'admin siamo noi
 */
@Theme("valo")
public class AdminUI extends AlgosUI {

    /*
     * Aggiunge i moduli specifici dell'applicazione
     */
    @Override
    protected void addModuli() {
        this.addModulo(new CompanyMod());
        this.addModulo(new MiliteMod());
        this.addModulo(new FunzioneMod());
    }// end of method

}// end of class
