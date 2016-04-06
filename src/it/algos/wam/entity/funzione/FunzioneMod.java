package it.algos.wam.entity.funzione;


import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TextArea;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.volontario.Volontario_;

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

    /**
     * Crea i campi visibili nella lista (table)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella lista <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsList() {
        return super.addCompanyField(
                Funzione_.ordine,
                Funzione_.sigla,
                Funzione_.descrizione,
                Funzione_.note);
    }// end of method

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
                Funzione_.ordine,
                Funzione_.sigla,
                Funzione_.descrizione,
                Funzione_.note);
    }// end of method

    /**
     * Crea i campi visibili nella scheda (search)
     * <p/>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsSearch() {
        return super.addCompanyField(
                Funzione_.sigla,
                Funzione_.descrizione);
    }// end of method

    //    /**
//     * Populate the map to bind item properties to fields.
//     * <p>
//     * Crea e aggiunge i campi.<br>
//     * Implementazione di default nella superclasse.<br>
//     * I campi vengono recuperati dal Modello.<br>
//     * I campi vengono creti del tipo grafico previsto nella Entity.<br>
//     * Se si vuole aggiungere un campo (solo nel form e non nel Modello),<br>
//     * usare il metodo sovrascritto nella sottoclasse
//     * invocando prima (o dopo) il metodo della superclasse.
//     * Se si vuole un layout completamente diverso sovrascrivere
//     * senza invocare il metodo della superclasse
//     */
//    @Override
//    public void createFields() {
//        TextArea field;
//        super.createFields();
//        Attribute[] attributes = {Bio_.tmplBioServer, Bio_.tmplBioStandard};
//        field = new TextArea(Bio_.tmplBioServer.getName());
//        field.setColumns(NUM_COL);
//        field.setRows(NUM_ROWS);
//        addField(attributes[0], field);
//        field = new TextArea(Bio_.tmplBioStandard.getName());
//        field.setColumns(NUM_COL);
//        field.setRows(NUM_ROWS);
//        addField(attributes[1], field);
//    }// end of method

}// end of class

