package it.algos.wam.entity.serviziofunzione;

import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * Classe di tipo JavaBean.
 * <p>
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
public class ServizioFunzione extends WamCompanyEntity implements Comparable<ServizioFunzione> {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ManyToOne
    private Servizio servizio = null;

    @NotNull
    @ManyToOne
    private Funzione funzione = null;

    private boolean obbligatoria;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public ServizioFunzione() {
        this(null, null);
    }// end of constructor

    /**
     * Costruttore completo
     */
    public ServizioFunzione(Servizio servizio, Funzione funzione) {
        super();
        this.setServizio(servizio);
        this.setFunzione(funzione);
    }// end of general constructor

    /**
     * Recupera una istanza di ServizioFunzione usando la query standard della Primary Key
     *
     * @param id valore della Primary Key
     * @return istanza di ServizioFunzione, null se non trovata
     */
    public static ServizioFunzione find(long id) {
        ServizioFunzione instance = null;
        BaseEntity entity = AQuery.queryById(ServizioFunzione.class, id);

        if (entity != null) {
            if (entity instanceof ServizioFunzione) {
                instance = (ServizioFunzione) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di ServizioFunzione usando la query di una property specifica
     *
     * @param sigla valore della property Sigla
     * @return istanza di ServizioFunzione, null se non trovata
     */
    public static ServizioFunzione findByv(String sigla) {
        ServizioFunzione instance = null;
        BaseEntity entity = AQuery.queryOne(ServizioFunzione.class, ServizioFunzione_.sigla, sigla);

        if (entity != null) {
            if (entity instanceof ServizioFunzione) {
                instance = (ServizioFunzione) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera il valore del numero totale di records della della Entity
     *
     * @return numero totale di records della tavola
     */
    public static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(ServizioFunzione.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     *
     * @return lista di tutte le istanze di ServizioFunzione
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<ServizioFunzione> findAll() {
        return (ArrayList<ServizioFunzione>) AQuery.getLista(ServizioFunzione.class);
    }// end of method


    public Servizio getServizio() {
        return servizio;
    }// end of getter method

    public void setServizio(Servizio servizio) {
        this.servizio = servizio;
    }//end of setter method

    public Funzione getFunzione() {
        return funzione;
    }// end of getter method

    public void setFunzione(Funzione funzione) {
        this.funzione = funzione;
    }//end of setter method

    public boolean isObbligatoria() {
        return obbligatoria;
    }// end of getter method

    public void setObbligatoria(boolean obbligatoria) {
        this.obbligatoria = obbligatoria;
    }//end of setter method

    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatoria invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza di ServizioFunzione con gli stessi valori dei parametri di questa istanza
     */
    @Override
    @SuppressWarnings("all")
    public ServizioFunzione clone() throws CloneNotSupportedException {
        try {
            return (ServizioFunzione) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

    /**
     * Compara per sequenza della relativa funzione
     */
    @Override
    public int compareTo(ServizioFunzione other) {
        Funzione fnQuesto=getFunzione();
        Funzione fnAltro=other.getFunzione();
        return fnQuesto.compareTo(fnAltro);
    }


}// end of domain class
