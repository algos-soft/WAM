package it.algos.wam.entity.wamcompany;

import it.algos.wam.entity.company.Company;
import it.algos.webbase.web.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Superclasse astratta per tutte le Entity del progetto WAM che necessitano del campo Company
 */
@Entity
public abstract class WamCompany extends BaseEntity {

    //--croce di riferimento
    @NotNull
    @ManyToOne
    private Company company;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public WamCompany() {
    }// end of constructor


    @Override
    public String toString() {
        String stringa = "";

        if (company != null) {
            stringa = company.getName();
        }// end of if cycle

        return stringa;
    }// end of method

    public Company getCompany() {
        return company;
    }// end of getter method

    public void setCompany(Company company) {
        this.company = company;
    }//end of setter method


}// end of abstract domain class
