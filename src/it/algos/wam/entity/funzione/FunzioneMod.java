package it.algos.wam.entity.funzione;


import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.wamcompany.WamCompany_;
import it.algos.wam.entity.wamcompany.WamMod;

import javax.persistence.metamodel.Attribute;

/**
 * Gestione (minimale) del modulo specifico
 */
@SuppressWarnings("serial")
public class FunzioneMod extends WamMod {

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Funzioni";

    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public FunzioneMod() {
        super(Funzione.class, MENU_ADDRESS, FontAwesome.GEAR);
    }// end of constructor

    /**
     * Crea i campi visibili nella lista (table)
     * <p/>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella lista <br>
     */
    protected Attribute<?, ?>[] creaFieldsList() {
        return new Attribute[]{WamCompany_.company, Funzione_.ordine, Funzione_.sigla, Funzione_.descrizione, Funzione_.note};
    }// end of method

}// end of class

