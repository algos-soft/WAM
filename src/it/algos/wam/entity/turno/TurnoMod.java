package it.algos.wam.entity.turno;


import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.wamcompany.WamMod;
import it.algos.webbase.web.module.ModulePop;

/**
 * Gestione (minimale) del modulo specifico
 */
@SuppressWarnings("serial")
public class TurnoMod extends WamMod {

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Turni (solo admin)";

    /**
     * Costruttore senza parametri
     * <p/>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public TurnoMod() {
        super(Turno.class, MENU_ADDRESS, FontAwesome.BARS);
    }// end of constructor

}// end of class

