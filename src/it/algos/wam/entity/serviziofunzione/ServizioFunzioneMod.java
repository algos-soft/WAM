package it.algos.wam.entity.serviziofunzione;


import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;

import javax.persistence.metamodel.Attribute;

/**
 * Gestione (minimale) del modulo specifico
 */
@SuppressWarnings("serial")
public class ServizioFunzioneMod extends ModulePop {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    private static String MENU_ADDRESS = "ServizioFunzione";

    // icona (eventuale) del modulo
    private static Resource ICON = FontAwesome.USER;


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

