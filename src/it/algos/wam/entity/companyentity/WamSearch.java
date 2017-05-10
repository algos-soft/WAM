package it.algos.wam.entity.companyentity;

import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;

import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;

/**
 * Created by gac on 09/05/17.
 * .
 */
public class WamSearch extends SearchManager {

    /**
     * Constructor
     */
    public WamSearch() {
        this(null);
    }// end of basic constructor

    /**
     * Constructor
     *
     * @param module the reference module
     */
    public WamSearch(ModulePop module) {
        super(module);
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

}// end of class
