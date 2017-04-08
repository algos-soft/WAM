package it.algos.wam.entity.companyentity;

import com.vaadin.server.Resource;
import it.algos.webbase.web.table.TablePortal;

/**
 * Created by gac on 07/04/17.
 * Estende la superclasse per eliminare il doppio click dalla lista (Table)
 */
public class WamModSenzaDoppioClick extends WamMod {

    /**
     * Costruttore
     *
     * @param entity    di riferimento del modulo
     * @param menuLabel etichetta visibile nella menu bar
     * @param menuIcon  icona del menu
     */
    public WamModSenzaDoppioClick(Class entity, String menuLabel, Resource menuIcon) {
        super(entity, menuLabel, menuIcon);
    }// end of constructor


    /**
     * Sovrascrive per DISABILITARE il doppio click nella lista
     */
    @Override
    public void edit() {
    }// end of method


    @Override
    public TablePortal createTablePortal() {
        return new WamTablePortalSoloRicerca(this);
    }// end of method

}// end of class
