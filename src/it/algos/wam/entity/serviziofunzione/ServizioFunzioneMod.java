package it.algos.wam.entity.serviziofunzione;


import com.vaadin.server.FontAwesome;
import it.algos.webbase.web.module.ModulePop;

/**
 * Gestione (minimale) del modulo specifico
 */
@SuppressWarnings("serial")
public class ServizioFunzioneMod extends ModulePop {

    // indirizzo interno del modulo (serve nei menu)
    public static String MENU_ADDRESS = "ServizioFunzione";

    /**
     * Costruttore senza parametri
     * <p/>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public ServizioFunzioneMod() {
        super(ServizioFunzione.class, MENU_ADDRESS, FontAwesome.GEAR);
    }// end of constructor

}// end of class

