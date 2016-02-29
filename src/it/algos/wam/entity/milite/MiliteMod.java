package it.algos.wam.entity.milite;


import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.wamcompany.WamCompany_;
import it.algos.wam.entity.wamcompany.WamMod;

import javax.persistence.metamodel.Attribute;

/**
 * Modulo del Milite
 */
@SuppressWarnings("serial")
public class MiliteMod extends WamMod {

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Volontari";

    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public MiliteMod() {
        super(Milite.class, MENU_ADDRESS, FontAwesome.GEAR);
    }// end of constructor

    /**
     * Crea i campi visibili nella lista (table)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella lista <br>
     */
    protected Attribute<?, ?>[] creaFieldsList() {
        return new Attribute[]{
                WamCompany_.company,
                Milite_.nome,
                Milite_.cognome,
                Milite_.dataNascita,
                Milite_.cellulare,
                Milite_.dipendente,
                Milite_.attivo};
    }// end of method


}// end of class

