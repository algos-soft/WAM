package it.algos.wam.entity.companyentity;

import it.algos.wam.entity.wamcompany.WamCompany;

/**
 * Created by gac on 11 mag 2016.
 * .
 */
public interface CompanyListener {

    void companyAdded(WamCompany company);
    void companyRemoved(WamCompany company);
    void companyChanged(WamCompany company);

}// end of interface
