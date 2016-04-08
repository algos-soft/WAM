package it.algos.wam.entity.funzione;


import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TextArea;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.servizio.ServizioTable;
import it.algos.wam.entity.servizio.ServizioTablePortal;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;

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
        super(Funzione.class, MENU_ADDRESS, FontAwesome.CHECK_SQUARE_O);
    }// end of constructor

//    /**
//     * Crea i campi visibili nella lista (table)
//     * <p>
//     * Come default spazzola tutti i campi della Entity <br>
//     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
//     * Serve anche per l'ordine con cui vengono presentati i campi nella lista <br>
//     */
//    @Override
//    protected Attribute<?, ?>[] creaFieldsList() {
//        return super.addCompanyField(
//                Funzione_.ordine,
//                Funzione_.sigla,
//                Funzione_.descrizione,
//                Funzione_.note);
//    }// end of method

    /**
     * Crea i campi visibili nella scheda (form)
     * <p/>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsForm() {
        return super.addCompanyField(
                Funzione_.sigla,
                Funzione_.descrizione,
                Funzione_.note);
    }// end of method

//    /**
//     * Crea i campi visibili nella scheda (search)
//     * <p/>
//     * Come default spazzola tutti i campi della Entity <br>
//     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
//     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
//     */
//    @Override
//    protected Attribute<?, ?>[] creaFieldsSearch() {
//        return super.addCompanyField(
//                Funzione_.sigla,
//                Funzione_.descrizione);
//    }// end of method

    @Override
    public TablePortal createTablePortal() {
        return new FunzioneTablePortal(this);
    }

    @Override
    public ATable createTable() {
        return new FunzioneTable(this);
    }



}// end of class

