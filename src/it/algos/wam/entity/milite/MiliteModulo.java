package it.algos.wam.entity.milite;


import com.vaadin.server.FontAwesome;
import it.algos.webbase.web.module.ModulePop;

/**
 * Modulo del Milite
 */
@SuppressWarnings("serial")
public class MiliteModulo extends ModulePop {

    /**
     * Costruttore senza parametri
     * <p/>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public MiliteModulo() {
        super(Milite.class, FontAwesome.GEAR);
    }// end of constructor

}// end of class

