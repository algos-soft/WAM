package it.algos.wam.entity.volontariofunzione;


import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;

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

    /**
     * Sovrascrive per DISABILITARE il doppio click nella lista
     */
    @Override
    public void edit() {
    }// end of method

    /**
     * Crea una Table già filtrata sulla company corrente
     * The concrete subclass must override for a specific Table.
     *
     * @return the Table
     */
    @Override
    public ATable createTable() {
        return new VolontarioFunzioneTable(this);
    }// end of method

}// end of class

