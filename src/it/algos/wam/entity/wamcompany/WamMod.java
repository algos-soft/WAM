package it.algos.wam.entity.wamcompany;


import com.vaadin.server.Resource;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;

/**
 * Modulo astratto per implementare la creazione della WamTablePortal
 * Si interpone come layer tra le classi Mod che usano la property Company e la superclasse standard ModulePop
 */
@SuppressWarnings("serial")
public abstract class WamMod extends ModulePop {

    /**
     * Costruttore
     *
     * @param entity   di riferimento del modulo
     * @param menuIcon icona del menu
     */
    public WamMod(Class entity, Resource menuIcon) {
        super(entity, menuIcon);
    }// end of constructor


    /**
     * Create the Table Portal
     *
     * @return the TablePortal
     */
    @Override
    public TablePortal createTablePortal() {
        return new WamTablePortal(this);
    }// end of method

}// end of class
