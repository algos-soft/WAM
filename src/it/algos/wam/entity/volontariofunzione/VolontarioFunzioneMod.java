package it.algos.wam.entity.volontariofunzione;


import com.vaadin.server.FontAwesome;
import it.algos.webbase.web.module.ModulePop;

/**
 * Gestione (minimale) del modulo specifico
 */
@SuppressWarnings("serial")
public class VolontarioFunzioneMod extends ModulePop {

    // indirizzo interno del modulo (serve nei menu)
    public static String MENU_ADDRESS = "VolontarioFunzione";

    /**
     * Costruttore senza parametri
     * <p/>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public VolontarioFunzioneMod() {
        super(VolontarioFunzione.class, MENU_ADDRESS, FontAwesome.GEAR);
    }// end of constructor

}// end of class

