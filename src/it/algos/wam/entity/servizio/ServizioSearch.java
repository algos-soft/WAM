package it.algos.wam.entity.servizio;

import it.algos.wam.entity.companyentity.WamSearch;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

/**
 * Created by gac on 09/05/17.
 * .
 */
public class ServizioSearch extends WamSearch {

    /**
     * Constructor
     *
     * @param module the reference module
     */
    public ServizioSearch(ModulePop module) {
        super(module);
    }// end of constructor

    /**
     * Attributes used in this search
     * Di default prende dal modulo
     * Può essere sovrascritto se c'è un Search specifico
     *
     * @return a list containing all the attributes used in this search
     */
    @Override
    protected Attribute<?, ?>[] getAttributesList() {
        return addCompanyField(
                Servizio_.sigla,
                Servizio_.descrizione,
                Servizio_.abilitato,
                Servizio_.orario
        );//end of bracket
    }// end of method


}// end of class
