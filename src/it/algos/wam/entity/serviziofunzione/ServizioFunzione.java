package it.algos.wam.entity.serviziofunzione;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
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

    //--ordine di presentazione nel servizio (obbligatorio, con controllo automatico prima del persist se è zero)@todo da fare
    @Index
    private int ordine = 0;

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
     * Costruttore completo
     */
    public ServizioFunzione(WamCompany company, Servizio servizio, Funzione funzione) {
        this(company, servizio, funzione, false);
    }// end of general constructor

    /**
     * Costruttore completo
     *
     * @param company      croce di appartenenza (obbligatorio)
     * @param servizio     di riferimento (obbligatorio)
     * @param funzione     di riferimento (obbligatorio)
     * @param obbligatoria la funzione (facoltativo)
     */
    public ServizioFunzione(WamCompany company, Servizio servizio, Funzione funzione, boolean obbligatoria) {
        super();
        this.setCompany(company);
        this.setServizio(servizio);
        this.setFunzione(funzione);
        this.setObbligatoria(obbligatoria);
    }// end of general constructor

    /**
     * Costruttore completo
     *
     * @param company      croce di appartenenza (obbligatorio)
     * @param servizio     di riferimento (obbligatorio)
     * @param funzione     di riferimento (obbligatorio)
     * @param obbligatoria la funzione (facoltativo)
     * @param ordine
     */
    public ServizioFunzione(WamCompany company, Servizio servizio, Funzione funzione, boolean obbligatoria, int ordine) {
        super();
        this.setCompany(company);
        this.setServizio(servizio);
        this.setFunzione(funzione);
        this.setObbligatoria(obbligatoria);
        this.setOrdine(ordine);
    }// end of general constructor

    /**
     * Recupera una istanza di ServizioFunzione usando la query standard della Primary Key
     *
     * @param id valore della Primary Key
     * @return istanza di ServizioFunzione, null se non trovata
     */
    public static ServizioFunzione find(long id) {
        ServizioFunzione instance = null;
        BaseEntity entity = AQuery.find(ServizioFunzione.class, id);

        if (entity != null) {
            if (entity instanceof ServizioFunzione) {
                instance = (ServizioFunzione) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di ServizioFunzione usando la query di una property specifica
     * Filtrato sulla azienda corrente.
     *
     * @param servizio di riferimento (obbligatorio)
     * @param funzione di riferimento (obbligatorio)
     * @return istanza di ServizioFunzione, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static ServizioFunzione findByServFunz(Servizio servizio, Funzione funzione) {
        ServizioFunzione instance = null;
        BaseEntity bean = null;

//        EntityManager manager = EM.createEntityManager();
//        bean = CompanyQuery.queryOne(ServizioFunzione.class, ServizioFunzione_.sigla,"");
//        manager.close();

        if (bean != null && bean instanceof ServizioFunzione) {
            instance = (ServizioFunzione) bean;
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di ServizioFunzione usando la query di una property specifica
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company  croce di appartenenza
     * @param servizio di riferimento (obbligatorio)
     * @param funzione di riferimento (obbligatorio)
     * @return istanza di ServizioFunzione, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static ServizioFunzione findByServFunz(WamCompany company, Servizio servizio, Funzione funzione) {
        ServizioFunzione instance = null;
        BaseEntity bean = null;
        ArrayList<ServizioFunzione> lista;

        Container.Filter filter = new Compare.Equal(ServizioFunzione_.servizio.getName(), servizio);
        Container.Filter filter2 = new Compare.Equal(ServizioFunzione_.funzione.getName(), funzione);
        lista = (ArrayList<ServizioFunzione>) AQuery.getLista(ServizioFunzione.class, filter, filter2);

        if (lista.size() == 1) {
            bean = lista.get(0);
        }// end of if cycle

        if (bean != null) {
            instance = (ServizioFunzione) bean;
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
     * Creazione iniziale di un ServizioFunzione
     * Lo crea SOLO se non esiste già
     *
     * @param company      croce di appartenenza (obbligatorio)
     * @param manager      the EntityManager to use
     * @param servizio     di riferimento (obbligatorio)
     * @param funzione     di riferimento (obbligatorio)
     * @param obbligatoria la funzione (facoltativo)
     * @return istanza di ServizioFunzione
     */
    public static ServizioFunzione crea(WamCompany company, EntityManager manager, Servizio servizio, Funzione funzione, boolean obbligatoria) {
        ServizioFunzione servFunz = ServizioFunzione.findByServFunz(company, servizio, funzione);

        if (servFunz == null) {
            servFunz = new ServizioFunzione(company, servizio, funzione, obbligatoria);
            servFunz = (ServizioFunzione) servFunz.save(manager);
        }// end of if cycle

        return servFunz;
    }// end of static method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     *
     * @return lista di tutte le istanze di ServizioFunzione
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<ServizioFunzione> findAll() {
        return (ArrayList<ServizioFunzione>) AQuery.getLista(ServizioFunzione.class);
    }// end of method

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }// end of getter method

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

    public int getOrdine() {
        return ordine;
    }// end of getter method

    public void setOrdine(int ordine) {
        this.ordine = ordine;
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
        Funzione fnQuesto = getFunzione();
        Funzione fnAltro = other.getFunzione();
        return fnQuesto.compareTo(fnAltro);
    }


}// end of domain class
