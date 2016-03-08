package it.algos.wam.entity.companyentity;

import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * Superclasse astratta per tutte le Entity del progetto WAM che necessitano del campo Company
 */
@MappedSuperclass
@Entity
public abstract class WamCompanyEntity extends CompanyEntity {

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public WamCompanyEntity() {
    }// end of constructor


    public WamCompany getWamCompany() {
        return (WamCompany)getCompany();
    }// end of getter method

}// end of abstract domain class
