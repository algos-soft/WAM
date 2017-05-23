package it.algos.wam.entity.companyentity;

import com.vaadin.server.Resource;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.table.TablePortal;

/**
 * Created by gac on 14/05/17.
 * .
 */
public class WamModEditDeveloper extends WamMod {
    /**
     * Costruttore
     *
     * @param entity    di riferimento del modulo
     * @param menuLabel etichetta visibile nella menu bar
     * @param menuIcon  icona del menu
     */
    public WamModEditDeveloper(Class entity, String menuLabel, Resource menuIcon) {
        super(entity, menuLabel, menuIcon);
    }// end of constructor


    /**
     * Sovrascrive per DISABILITARE il doppio click nella lista
     */
    @Override
    public void edit() {
        if (LibSession.isDeveloper()) {
            super.edit();
        }// end of if cycle
    }// end of method


    @Override
    public TablePortal createTablePortal() {
        return new WamTablePortalEditDeveloper(this);
    }// end of method

}// end of class
