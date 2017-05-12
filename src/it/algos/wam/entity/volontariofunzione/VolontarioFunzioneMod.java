package it.algos.wam.entity.volontariofunzione;


import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.companyentity.WamModSenzaDoppioClick;
import it.algos.wam.entity.companyentity.WamTablePortal;
import it.algos.wam.entity.companyentity.WamTablePortalSoloRicerca;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;

/**
 * Gestione (minimale) del modulo specifico
 */
public class VolontarioFunzioneMod extends WamModSenzaDoppioClick {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    private static String MENU_ADDRESS = "VolontarioFunzione";

    // icona (eventuale) del modulo
    private static Resource ICON = FontAwesome.TASKS;


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
     * Crea una Table già filtrata sulla company corrente
     * The concrete subclass must override for a specific Table.
     *
     * @return the Table
     */
    @Override
    public ATable createTable() {
        return new VolontarioFunzioneTable(this);
    }// end of method

//    @Override
//    public TablePortal createTablePortal() {
//        WamTablePortal tablePortal= new WamTablePortal(this);
//
//        tablePortal.getToolbar().setCreateButtonVisible(false);
//        tablePortal.getToolbar().setEditButtonVisible(false);
//        tablePortal.getToolbar().setDeleteButtonVisible(false);
//        tablePortal.getToolbar().setSearchButtonVisible(true);
//        tablePortal.getToolbar().setSelectButtonVisible(true);
//
//        return tablePortal;
//    }// end of method

}// end of class

