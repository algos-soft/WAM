package it.algos.wam.entity.funzione;


import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Table;
import it.algos.wam.entity.companyentity.WamModSposta;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.metamodel.Attribute;

/**
 * Gestione (minimale) del modulo specifico
 */
public class FunzioneMod extends WamModSposta  {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Funzioni";

    // icona (eventuale) del modulo
    public static Resource ICON = FontAwesome.CHECK_SQUARE_O;


    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public FunzioneMod() {
        super(Funzione.class, MENU_ADDRESS, ICON);
    }// end of constructor


    @Override
    public ModuleForm createForm(Item item) {
        return new FunzioneForm(item, this);
    }// end of method


    @Override
    public ATable createTable() {
        return new FunzioneTable(this);
    }// end of method


    @Override
    public TablePortal createTablePortal() {
        TablePortal portaleFunzione = new FunzioneTablePortal(this);
        portaleFunzione.delCmd(TableToolbar.CMD_SEARCH);

        return portaleFunzione;
    }// end of method


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
//                Funzione_.descrizione);
//    }// end of method



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


}// end of class

