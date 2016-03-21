package it.algos.wam.entity.volontariofunzione;

import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe di tipo JavaBean.
 * <p>
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
public class VolontarioFunzione extends WamCompanyEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ManyToOne
    private Volontario volontario = null;

    @NotNull
    @ManyToOne
    private Funzione funzione = null;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public VolontarioFunzione() {
        this(null, null);
    }// end of constructor

    /**
     * Costruttore completo
     *
     * @param volontario di riferimento
     * @param funzione   di riferimento
     */
    public VolontarioFunzione(Volontario volontario, Funzione funzione) {
        super();
        this.setVolontario(volontario);
        this.setFunzione(funzione);
    }// end of general constructor

    /**
     * Recupera una istanza di VolontarioFunzione usando la query standard della Primary Key
     *
     * @param id valore della Primary Key
     *
     * @return istanza di VolontarioFunzione, null se non trovata
     */
    public static VolontarioFunzione find(long id) {
        VolontarioFunzione instance = null;
        BaseEntity entity = AQuery.queryById(VolontarioFunzione.class, id);

        if (entity != null) {
            if (entity instanceof VolontarioFunzione) {
                instance = (VolontarioFunzione) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di VolontarioFunzione usando la query di una property specifica
     *
     * @param volontario di riferimento
     * @param funzione   di riferimento
     *
     * @return istanza di VolontarioFunzione, null se non trovata
     */
    @SuppressWarnings("all")
    public static VolontarioFunzione findByVolFun(Volontario volontario, Funzione funzione) {
        VolontarioFunzione instance = null;

        //@todo da migliorare
        List<VolontarioFunzione> volontari = (List<VolontarioFunzione>) AQuery.queryList(VolontarioFunzione.class, VolontarioFunzione_.volontario, volontario);
        if (volontari != null && volontari.size() > 0) {
            for (VolontarioFunzione vol : volontari) {
                if (vol.getFunzione().getId().equals(funzione.getId())) {
                    instance = vol;
                }// end of if cycle
            }// end of for cycle
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
        long totTmp = AQuery.getCount(VolontarioFunzione.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Creazione iniziale di un record della tavola d'incrocio
     * Lo crea SOLO se non esiste già
     *
     * @param volontario di riferimento
     * @param funzione   di riferimento
     *
     * @return istanza di VolontarioFunzione
     */
    public static VolontarioFunzione crea(Volontario volontario, Funzione funzione) {
        VolontarioFunzione volFun = VolontarioFunzione.findByVolFun(volontario, funzione);

        if (volFun == null) {
            volFun = new VolontarioFunzione(volontario, funzione);
            volFun.save();
        }// end of if cycle

        return volFun;
    }// end of static method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     *
     * @return lista di tutte le istanze di VolontarioFunzione
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<VolontarioFunzione> findAll() {
        return (ArrayList<VolontarioFunzione>) AQuery.getLista(VolontarioFunzione.class);
    }// end of method

    public Volontario getVolontario() {
        return volontario;
    }// end of getter method

    public void setVolontario(Volontario volontario) {
        this.volontario = volontario;
    }//end of setter method

    public Funzione getFunzione() {
        return funzione;
    }// end of getter method

    public void setFunzione(Funzione funzione) {
        this.funzione = funzione;
    }//end of setter method

    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatoria invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza di VolontarioFunzione con gli stessi valori dei parametri di questa istanza
     */
    @Override
    @SuppressWarnings("all")
    public VolontarioFunzione clone() throws CloneNotSupportedException {
        try {
            return (VolontarioFunzione) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of domain class
