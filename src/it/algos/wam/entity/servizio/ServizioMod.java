package it.algos.wam.entity.servizio;


import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.webbase.web.lib.LibSession;

import javax.persistence.metamodel.Attribute;

/**
 * Modulo del servizio (fisso o variabile) previsto o effettuato
 */
@SuppressWarnings("serial")
public class ServizioMod extends WamMod {

    // indirizzo interno del modulo - etichetta del menu
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
        super(Servizio.class, MENU_ADDRESS, FontAwesome.TASKS);
    }// end of constructor

    /**
     * Crea i campi visibili nella lista (table)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella lista <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsList() {

        return super.creaFieldsListWam(
                Servizio_.ordine,
                Servizio_.sigla,
                Servizio_.descrizione,
                Servizio_.durata,
                Servizio_.oraInizio,
                Servizio_.oraFine,
                Servizio_.visibile,
                Servizio_.orario,
                Servizio_.multiplo,
                Servizio_.persone);

    }// end of method



}// end of class

