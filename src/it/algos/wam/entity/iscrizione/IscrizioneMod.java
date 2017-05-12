package it.algos.wam.entity.iscrizione;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import it.algos.wam.entity.companyentity.*;
import it.algos.wam.entity.serviziofunzione.ServizioFunzioneSearch;
import it.algos.wam.entity.serviziofunzione.ServizioFunzioneTable;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione_;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.turno.Turno_;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.metamodel.Attribute;

/**
 * Created by gac on 17/02/17.
 * Gestione (minimale) del modulo specifico
 */
public class IscrizioneMod extends WamModSenzaDoppioClick {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Iscrizioni";

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
    public IscrizioneMod() {
        super(Iscrizione.class, MENU_ADDRESS, ICON);
    }// end of constructor

    /**
     * Crea una Table già filtrata sulla company corrente
     * The concrete subclass must override for a specific Table.
     *
     * @return the Table
     */
    @Override
    public ATable createTable() {
        return new IscrizioneTable(this);
    }// end of method

    /**
     * Create the Table Portal
     * <p>
     * in caso di developer, portale specifico col menu di selezione della company
     * in caso di admin e utente normale, portale standard
     *
     * @return the TablePortal
     */
    @Override
    public TablePortal createTablePortal() {
        WamTablePortal portal = new WamTablePortalSoloRicerca(this);

        TableToolbar toolBar = portal.getToolbar();
        if (toolBar != null) {
            toolBar.setEditButtonVisible(true);
        }// end of if cycle

        return portal;
    }// end of method


}// end of class
