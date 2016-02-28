package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import it.algos.wam.entity.company.CompanyMod;
import it.algos.wam.entity.funzione.FunzioneMod;
import it.algos.wam.entity.milite.MiliteMod;
import it.algos.wam.entity.servizio.*;
import it.algos.webbase.web.ui.AlgosUI;

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
        this.addModulo(new ServizioMod());
    }// end of method

}// end of class
