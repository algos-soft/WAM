package it.algos.wam.entity.companyentity;


import com.vaadin.server.Resource;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;

/**
 * Modulo astratto per implementare la creazione della WamTablePortalSposta
 * Si interpone come layer tra le classi Mod che usano la property Company e la superclasse standard ModulePop
 */
@SuppressWarnings("serial")
public abstract class WamModSposta extends WamMod {

    /**
     * Costruttore
     *
     * @param entity   di riferimento del modulo
     * @param menuIcon icona del menu
     */
    public WamModSposta(Class entity, Resource menuIcon) {
        super(entity, menuIcon);
    }// end of constructor

    /**
     * Costruttore
     *
     * @param entity    di riferimento del modulo
     * @param menuLabel etichetta visibile nella menu bar
     * @param menuIcon  icona del menu
     */
    public WamModSposta(Class entity, String menuLabel, Resource menuIcon) {
        super(entity, menuLabel, menuIcon);
    }// end of constructor


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
        return new WamTablePortalSposta(this);
    }// end of method


}// end of class
