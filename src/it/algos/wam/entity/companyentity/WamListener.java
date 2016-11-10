package it.algos.wam.entity.companyentity;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.wamcompany.WamCompany;

/**
 * Created by gac on 10 nov 2016.
 * .
 */
public interface WamListener {

    WamCompany getCompany();

    Funzione getFunzione();

}// end of interface
