package it.algos.wam.entity.serviziofunzione;


import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.companyentity.WamModSenzaDoppioClick;
import it.algos.wam.entity.companyentity.WamTablePortalSoloRicerca;
import it.algos.wam.entity.servizio.ServizioTablePortal;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.metamodel.Attribute;

/**
 * Gestione (minimale) del modulo specifico
 */
@SuppressWarnings("serial")
public class ServizioFunzioneMod extends WamModSenzaDoppioClick {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    private static String MENU_ADDRESS = "ServizioFunzione";

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
    public ServizioFunzioneMod() {
        super(ServizioFunzione.class, MENU_ADDRESS, ICON);
    }// end of constructor

    /**
     * Crea i campi visibili nella scheda (search)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsSearch() {
        return super.addCompanyField(
                ServizioFunzione_.servizio,
                ServizioFunzione_.funzione,
                ServizioFunzione_.obbligatoria);
    }// end of method


    /**
     * Crea una Table già filtrata sulla company corrente
     * The concrete subclass must override for a specific Table.
     *
     * @return the Table
     */
    @Override
    public ATable createTable() {
        return new ServizioFunzioneTable(this);
    }// end of method

}// end of class

