package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import it.algos.wam.entity.company.CompanyModulo;
import it.algos.wam.entity.milite.MiliteModulo;
import it.algos.wam.funzione.FunzioneMod;
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
        this.addModulo(new CompanyModulo());
        this.addModulo(new MiliteModulo());
        this.addModulo(new FunzioneMod());
    }// end of method

}// end of class
