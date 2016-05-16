package it.algos.wam.entity.volontariofunzione;


import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import it.algos.webbase.web.module.ModulePop;

/**
 * Gestione (minimale) del modulo specifico
 */
public class VolontarioFunzioneMod extends ModulePop {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "VolontarioFunzione";

    // icona (eventuale) del modulo
    public static Resource ICON = FontAwesome.USER;


    /**
     * Costruttore senza parametri
     * <p/>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public VolontarioFunzioneMod() {
        super(VolontarioFunzione.class, MENU_ADDRESS, ICON);
    }// end of constructor

}// end of class

