package it.algos.wam.entity.wamcompany;

import it.algos.wam.entity.company.Company;
import it.algos.webbase.web.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Classe di tipo JavaBean
 * <p>
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 * <p>
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
            stringa = company.getCompanyCode();
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
