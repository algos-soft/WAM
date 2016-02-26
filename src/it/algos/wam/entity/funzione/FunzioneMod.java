package it.algos.wam.entity.funzione;


import com.vaadin.server.FontAwesome;
import it.algos.webbase.web.module.ModulePop;

/**
 * Gestione (minimale) del modulo specifico
 */
@SuppressWarnings("serial")
public class FunzioneMod extends ModulePop {

    /**
     * Costruttore senza parametri
     * <p/>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public FunzioneMod() {
        super(Funzione.class, FontAwesome.GEAR);
    }// end of constructor

}// end of class

