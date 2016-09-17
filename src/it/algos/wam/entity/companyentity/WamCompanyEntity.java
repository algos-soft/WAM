package it.algos.wam.entity.companyentity;

import com.vaadin.ui.Notification;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.domain.company.BaseCompany;
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


    /**
     * Controlla se il valore della query è della classe corretta
     *
     * @param valore (long) restituita dalla query generica
     * @return totale dei records, zero se la query non ha funzionato
     * @deprecated
     */
    protected  static int check(long valore) {
        int totRec = 0;

        if (valore > 0) {
            totRec = (int) valore;
        }// fine del blocco if

        return totRec;
    }// end of static method

    /**
     * Recupera la company specifica
     *
     * @return la company
     */
    protected WamCompany getWamCompany() {
        WamCompany company = null;
        BaseCompany baseCompany = super.getCompany();

        if (baseCompany != null && baseCompany instanceof WamCompany) {
            company = (WamCompany) baseCompany;
        }// end of if cycle

        return company;
    }// end of method


    //------------------------------------------------------------------------------------------------------------------------
    // Utilities
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Implementa come business logic, la obbligatorietà della company
     * <p>
     *
     * @return true se esiste, false se non esiste
     */
    protected boolean checkCompany() {
        String caption = "La funzione non può essere accettata, perché manca la company che è obbligatoria";

        if (getCompany() != null) {
            return true;
        } else {
            Notification.show(caption, Notification.Type.WARNING_MESSAGE);
            return false;
        }// end of if/else cycle
    } // end of method

}// end of abstract domain class
