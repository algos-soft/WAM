package it.algos.wam.entity.funzione;

import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity per una funzione
 * Estende la Entity astratta WamCompany che contiene la property wamcompany
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
public class Funzione extends WamCompanyEntity {

    private static final long serialVersionUID = 1L;

    //--sigla di riferimento interna (obbligatoria)
    @NotEmpty
    @Column(length = 20)
    @Index
    private String sigla;


    //--descrizione per il tabellone (obbligatoria)
    @NotEmpty
    private String descrizione;


    //--ordine di presentazione nelle liste
    @Index
    private int ordine;


    //--note di spiegazione (facoltative)
    private String note;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public Funzione() {
        this(null, "", "");
    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     *
     * @param company     croce di appartenenza
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     */
    public Funzione(WamCompany company, String sigla, String descrizione) {
        this(company, sigla, descrizione, 0, "");
    }// end of constructor


    /**
     * Costruttore completo
     *
     * @param company     croce di appartenenza
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nelle liste
     * @param note        di spiegazione (facoltative)
     */
    public Funzione(WamCompany company, String sigla, String descrizione, int ordine, String note) {
        super();
        this.setCompany(company);
        this.setSigla(sigla);
        this.setDescrizione(descrizione);
        this.setOrdine(ordine);
        this.setNote(note);
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
     * @param sigla sigla di riferimento interna (obbligatoria)
     * @return istanza di Funzione, null se non trovata
     * @deprecated perché manca la wamcompany e potrebbero esserci records multipli con la stessa sigla
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
     * Relativa ad una wamcompany
     *
     * @param company croce di appartenenza
     * @param sigla   sigla di riferimento interna (obbligatoria)
     * @return istanza di Funzione, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Funzione find(WamCompany company, String sigla) {
        Funzione instance = null;

        //@todo migliorabile
//        ArrayList<Funzione> funzioniPerSigla = (ArrayList<Funzione>) AQuery.queryList(Funzione.class, Funzione_.sigla, sigla);
        List<Funzione> funzioniPerSigla = (List<Funzione>) AQuery.queryList(Funzione.class, Funzione_.sigla, sigla);

        if (funzioniPerSigla != null && funzioniPerSigla.size() > 0) {
            for (Funzione funzione : funzioniPerSigla) {
                if (funzione.getCompany().getId().equals(company.getId())) {
                    instance = funzione;
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
     * Relativa ad una wamcompany
     *
     * @return lista di tutte le istanze di Funzione
     */
    public static ArrayList<Funzione> findAll(WamCompany company) {
        //@todo da sviluppare
        return null;
    }// end of method


    /**
     * Creazione iniziale di una funzione
     * La crea SOLO se non esiste già
     *
     * @param company croce di appartenenza
     * @param sigla   sigla di riferimento interna (obbligatoria)
     * @return istanza di Funzione
     */
    public static Funzione crea(WamCompany company, String sigla) {
        return crea(company, sigla, "", 0, "");
    }// end of static method


    /**
     * Creazione iniziale di una funzione
     * La crea SOLO se non esiste già
     *
     * @param company     croce di appartenenza
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nelle liste
     * @param note        di spiegazione (facoltative)
     * @return istanza di Funzione
     */
    public static Funzione crea(WamCompany company, String sigla, String descrizione, int ordine, String note) {
        Funzione funzione = Funzione.find(company, sigla);

        if (funzione == null) {
            funzione = new Funzione(company, sigla, descrizione, ordine, note);
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

    public String getNote() {
        return note;
    }// end of getter method

    public void setNote(String note) {
        this.note = note;
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Funzione funzione = (Funzione) o;
//
//        return sigla.equals(funzione.sigla);
//
//    }


}// end of domain class
