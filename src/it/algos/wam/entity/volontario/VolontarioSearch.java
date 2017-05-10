package it.algos.wam.entity.volontario;

import it.algos.wam.entity.companyentity.WamSearch;
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

/**
 * Created by gac on 09/05/17.
 * .
 */
public class VolontarioSearch extends WamSearch {

    /**
     * Constructor
     *
     * @param module the reference module
     */
    public VolontarioSearch(ModulePop module) {
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
                Volontario_.nome,
                Volontario_.cognome,
                Volontario_.cellulare,
                Volontario_.email,
                Volontario_.admin,
                Volontario_.dipendente,
                Volontario_.infermiere,
                Volontario_.attivo,
                Volontario_.esenteFrequenza,
                Volontario_.invioMail
        );//end of bracket
    }// end of method


}// end of class
