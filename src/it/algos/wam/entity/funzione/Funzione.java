package it.algos.wam.entity.funzione;

import it.algos.wam.entity.company.Company;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * Classe di tipo JavaBean
 * <p>
 * 1) la classe deve avere un costruttore senza argomenti
 * <p>
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
public class Funzione extends BaseEntity {


    //--sigla di riferimento interna
    @NotEmpty
    @Column(length = 10)
    @Index
    private String sigla;


    //--descrizione per il tabellone
    @NotEmpty
    private String descrizione;


    //--ordine di presentazione nelle liste
    @Index
    private int ordine;


    //--croce di riferimento
    @NotNull
    @ManyToOne
    private Company company;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public Funzione() {
        this("");
    }// end of constructor

    /**
     * Costruttore
     *
     * @param sigla prevista
     */
    public Funzione(String sigla) {
        this(null, sigla, "", 0);
    }// end of general constructor


    /**
     * Costruttore completo
     *
     * @param company
     * @param sigla
     * @param descrizione
     * @param ordine
     */
    @SuppressWarnings("all")
    public Funzione(Company company, String sigla, String descrizione, int ordine) {
        super();
        this.setCompany(company);
        this.setSigla(sigla);
        this.setDescrizione(descrizione);
        this.setOrdine(ordine);
    }// end of constructor

    /**
     * Recupera una istanza di Funzione usando la query standard della Primary Key
     *
     * @param id valore della Primary Key
     * @return istanza di Funzione, null se non trovata
     */
    public static Funzione find(long id) {
        Funzione instance = null;
        BaseEntity entity = AQuery.queryById(Funzione.class, id);

        if (entity != null) {
            if (entity instanceof Funzione) {
                instance = (Funzione) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Funzione usando la query di una property specifica
     *
     * @param sigla valore della property sigla
     * @return istanza di Funzione, null se non trovata
     * @deprecated
     */
    public static Funzione findBySigla(String sigla) {
        Funzione instance = null;
        BaseEntity entity = AQuery.queryOne(Funzione.class, Funzione_.sigla, sigla);

        if (entity != null) {
            if (entity instanceof Funzione) {
                instance = (Funzione) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method


    /**
     * Recupera una istanza di Funzione usando la query di una property specifica
     * Relativa ad una company
     *
     * @param company di appartenenza
     * @param sigla   valore della property sigla
     * @return istanza di Funzione, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Funzione find(Company company, String sigla) {
        Funzione instance = null;

        //@todo migliorabile
        ArrayList<Funzione> funzioniPerSigla = (ArrayList<Funzione>) AQuery.queryLista(Funzione.class, Funzione_.sigla, sigla);

        if (funzioniPerSigla != null && funzioniPerSigla.size() > 0) {
            for (Funzione funzione : funzioniPerSigla) {
                if (funzione.getCompany().getId().equals(company.getId())) {
                    instance = funzione;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return null;
    }// end of method


    /**
     * Recupera il valore del numero totale di records della della Entity
     *
     * @return numero totale di records della tavola
     */
    public static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(Funzione.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     *
     * @return lista di tutte le istanze di Funzione
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Funzione> findAll() {
        return (ArrayList<Funzione>) AQuery.getLista(Funzione.class);
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Relativa ad una company
     *
     * @return lista di tutte le istanze di Funzione
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Funzione> findAll(Company company) {
        //@todo da sviluppare
        return null;
    }// end of method


    /**
     * Creazione iniziale di una funzione
     * Lo crea SOLO se non esiste già
     *
     * @param company
     * @param sigla
     */
    @SuppressWarnings("all")
    public static Funzione crea(Company company, String sigla) {
        return crea(company, sigla, "", 0);
    }// end of static method


    /**
     * Creazione iniziale di una funzione
     * Lo crea SOLO se non esiste già
     *
     * @param company
     * @param sigla
     * @param descrizione
     * @param ordine
     */
    @SuppressWarnings("all")
    public static Funzione crea(Company company, String sigla, String descrizione, int ordine) {
        Funzione funzione = Funzione.find(company, sigla);

        if (funzione == null) {
            funzione = new Funzione(company, sigla, descrizione, ordine);
            funzione.save();
        }// end of if cycle

        return funzione;
    }// end of static method

    @Override
    public String toString() {
        return sigla;
    }// end of method

    public String getSigla() {
        return sigla;
    }// end of getter method

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }//end of setter method

    public Company getCompany() {
        return company;
    }// end of getter method

    public void setCompany(Company company) {
        this.company = company;
    }//end of setter method

    public int getOrdine() {
        return ordine;
    }// end of getter method

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }//end of setter method

    public String getDescrizione() {
        return descrizione;
    }// end of getter method

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }//end of setter method

    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatoria invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza di Funzione con gli stessi valori dei parametri di questa istanza
     */
    @Override
    @SuppressWarnings("all")
    public Funzione clone() throws CloneNotSupportedException {
        try {
            return (Funzione) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of domain class
