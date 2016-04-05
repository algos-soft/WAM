package it.algos.wam.entity.turno;


import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.volontario.Volontario_;

import javax.persistence.metamodel.Attribute;

/**
 * Gestione (minimale) del modulo specifico
 */
@SuppressWarnings("serial")
public class TurnoMod extends WamMod {

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Turni";

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


    /**
     * Crea i campi visibili nella lista (table)
     * <p/>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella lista <br>
     */
    protected Attribute<?, ?>[] creaFieldsList() {
        return new Attribute[]{
                WamCompanyEntity_.company,
                Turno_.servizio,
                Turno_.inizio,
                Turno_.fine,
                Turno_.titoloExtra,
                Turno_.localitaExtra,
                Turno_.note,
                Turno_.assegnato};
    }// end of method


    /**
     * Crea i campi visibili nella scheda (form)
     * <p/>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    protected Attribute<?, ?>[] creaFieldsForm() {
        return new Attribute[]{
                WamCompanyEntity_.company,
                Turno_.iscrizioni,
                Turno_.servizio,
                Turno_.inizio,
                Turno_.fine,
                Turno_.titoloExtra,
                Turno_.localitaExtra,
                Turno_.note,
                Turno_.assegnato};
    }// end of method



}// end of class

