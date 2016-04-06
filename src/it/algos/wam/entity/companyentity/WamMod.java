package it.algos.wam.entity.companyentity;


import com.vaadin.server.Resource;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;

/**
 * Modulo astratto per implementare la creazione della WamTablePortal
 * Si interpone come layer tra le classi Mod che usano la property Company e la superclasse standard ModulePop
 */
@SuppressWarnings("serial")
public abstract class WamMod extends CompanyModule {

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
     * Costruttore
     *
     * @param entity    di riferimento del modulo
     * @param menuLabel etichetta visibile nella menu bar
     * @param menuIcon  icona del menu
     */
    public WamMod(Class entity, String menuLabel, Resource menuIcon) {
        super(entity, menuLabel, menuIcon);
    }// end of constructor


    /**
     * Aggiunge il campo company
     * <p>
     * in caso di developer, aggiunge (a sinistra) la colonna della company
     * aggiunge tutte le altre property, definite nella sottoclasse
     * Chiamato dalla sottoclasse
     */
    protected Attribute<?, ?>[] addCompanyField(Attribute... elenco) {
        ArrayList<Attribute> lista = new ArrayList<>();

        if (LibSession.isDeveloper()) {
            lista.add(WamCompanyEntity_.company);
        }// end of if cycle

        for (Attribute attr : elenco) {
            lista.add(attr);
        }// end of for cycle

        return lista.toArray(new Attribute[lista.size()]);
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

        if (LibSession.isDeveloper()) {
            return new WamTablePortal(this);
        } else {
            return super.createTablePortal();
        }// end of if/else cycle

    }// end of method

    @Override
    public void search() {
        super.search();
    }

}// end of class
