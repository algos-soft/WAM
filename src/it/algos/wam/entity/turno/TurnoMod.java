package it.algos.wam.entity.turno;


import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.companyentity.WamModEditDeveloper;
import it.algos.wam.entity.companyentity.WamModSenzaDoppioClick;
import it.algos.wam.entity.serviziofunzione.ServizioFunzioneSearch;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzioneTable;
import it.algos.webbase.domain.log.LogTablePortal;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;

/**
 * Gestione (minimale) del modulo specifico
 */
public class TurnoMod extends WamModEditDeveloper {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Turni";

    // icona (eventuale) del modulo
    public static Resource ICON = FontAwesome.TASKS;

    /**
     * Costruttore senza parametri
     * <p/>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public TurnoMod() {
        super(Turno.class, MENU_ADDRESS, ICON);
    }// end of constructor


    /**
     * Create the Search Manager
     *
     * @return the SearchManager
     */
    @Override
    public SearchManager createSearchManager() {
        return new TurnoSearch(this);
    }// end of method


    /**
     * Crea una Table già filtrata sulla company corrente
     * The concrete subclass must override for a specific Table.
     *
     * @return the Table
     */
    @Override
    public ATable createTable() {
        return new TurnoTable(this);
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
                Turno_.servizio,
                Turno_.inizio,
                Turno_.fine,
                Turno_.titoloExtra,
                Turno_.localitaExtra,
                Turno_.note
        };//end of brace
    }// end of method


}// end of class

