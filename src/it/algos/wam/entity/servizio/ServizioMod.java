package it.algos.wam.entity.servizio;


import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.wamcompany.WamCompany_;
import it.algos.wam.entity.wamcompany.WamMod;

import javax.persistence.metamodel.Attribute;

/**
 * Modulo del servizio (fisso o variabile) previsto o effettuato
 */
@SuppressWarnings("serial")
public class ServizioMod extends WamMod {

    // indirizzo interno del modulo (serve nei menu)
    public static String MENU_ADDRESS = "Servizi";

    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public ServizioMod() {
        super(Servizio.class, MENU_ADDRESS, FontAwesome.GEAR);
    }// end of constructor

    /**
     * Crea i campi visibili
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * NON garantisce l'ordine con cui vengono presentati i campi nella scheda <br>
     * Può mostrare anche il campo ID, oppure no <br>
     * Se si vuole differenziare tra Table, Form e Search, <br>
     * sovrascrivere creaFieldsList, creaFieldsForm e creaFieldsSearch <br>
     */
    protected Attribute<?, ?>[] creaFieldsList() {
        return new Attribute[]{
                WamCompany_.company,
                Servizio_.ordine,
                Servizio_.sigla,
                Servizio_.descrizione,
                Servizio_.durata,
                Servizio_.oraInizio,
                Servizio_.oraFine,
                Servizio_.visibile,
                Servizio_.orario,
                Servizio_.multiplo,
                Servizio_.persone};
    }// end of method

}// end of class

