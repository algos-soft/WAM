package it.algos.wam.entity.iscrizione;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import it.algos.wam.entity.companyentity.*;
import it.algos.wam.entity.serviziofunzione.ServizioFunzioneSearch;
import it.algos.wam.entity.serviziofunzione.ServizioFunzioneTable;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione_;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.turno.TurnoSearch;
import it.algos.wam.entity.turno.Turno_;
import it.algos.wam.entity.volontario.VolontarioTablePortal;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.metamodel.Attribute;

/**
 * Created by gac on 17/02/17.
 * Gestione (minimale) del modulo specifico
 */
public class IscrizioneMod extends WamModEditDeveloper {

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
     * Create the Search Manager
     *
     * @return the SearchManager
     */
    @Override
    public SearchManager createSearchManager() {
        return new IscrizioneSearch(this);
    }// end of method


//    @Override
//    public Attribute[] getFieldsSearch() {
//        return new Attribute[]{
//               Iscrizione_.turno,
//                Iscrizione_.volontario,
//                Iscrizione_.servizioFunzione,
//                Iscrizione_.esisteProblema,
//                Iscrizione_.notificaInviata
//        };//end of brace
//    }// end of method

}// end of class
