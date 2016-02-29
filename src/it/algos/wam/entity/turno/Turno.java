package it.algos.wam.entity.turno;

import it.algos.wam.entity.company.Company;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.milite.Milite;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Entity che descrive un Turno
 * Estende la Entity astratta WamCompany che contiene la property company
 * <p>
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
public class Turno extends WamCompany {

    //--tipologia di servizio (obbligatoria)
    @NotNull
    @ManyToOne
    private Servizio servizio;

    //--giorno, ora e minuto di inizio turno
    @NotNull
    private Date inizio;

    //--giorno, ora e minuto di fine turno
    //--i servizi senza orario (fisso) vengono creati solo con la data di inizio; la data di fine viene aggiunata dopo
    private Date fine;

    //--numero variabile di funzioni previste per il tipo di servizio
    //--massimo hardcoded di 4
    @ManyToOne
    private Funzione funzione1 = null;
    @ManyToOne
    private Funzione funzione2 = null;
    @ManyToOne
    private Funzione funzione3 = null;
    @ManyToOne
    private Funzione funzione4 = null;

    //--numero variabile di militi/volontari assegnati alle funzioni previste per il tipo di servizio
    //--massimo hardcoded di 4
    @ManyToOne
    private Milite milite1 = null;
    @ManyToOne
    private Milite milite2 = null;
    @ManyToOne
    private Milite milite3 = null;
    @ManyToOne
    private Milite milite4 = null;

    //--ultima modifica effettuata per le funzioni previste per il tipo di servizio
    //--massimo hardcoded di 4
    //--serve per bloccare le modifiche dopo un determinato intervallo di tempo
    private Timestamp lastModificaFunzione1 = null;
    private Timestamp lastModificaFunzione2 = null;
    private Timestamp lastModificaFunzione3 = null;
    private Timestamp lastModificaFunzione4 = null;

    //--durata effettiva del turno per ogni milite/volontario
    //--massimo hardcoded di 4
    private int oreMilite1 = 0;
    private int oreMilite2 = 0;
    private int oreMilite3 = 0;
    private int oreMilite4 = 0;

    //--eventuali problemi di presenza del milite nel turno
    //--serve per evidenziare il problema nel tabellone
    //--massimo hardcoded di 4
    private boolean problemiFunzione1 = false;
    private boolean problemiFunzione2 = false;
    private boolean problemiFunzione3 = false;
    private boolean problemiFunzione4 = false;

    //--eventuali note associate ad una singola funzione
    //--serve per evidenziare il problema nel tabellone
    //--massimo hardcoded di 4
    private String notaFunzione1;
    private String notaFunzione2;
    private String notaFunzione3;
    private String notaFunzione4;

    //--motivazione del turno extra
    private String titoloExtra;
    //--nome evidenziato della località per turni extra
    private String localitàExtra;
    //--descrizione dei viaggi extra
    private String note;

    //--turno previsto (vuoto) oppure assegnato (militi inseriti)
    private boolean assegnato = false;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public Turno() {
        this(null, null, null);
    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     *
     * @param company  croce di appartenenza
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     */
    public Turno(Company company, Servizio servizio, Date inizio) {
//        this(company, 0, sigla, descrizione, 0, 0, 0);
    }// end of constructor

    /**
     * Costruttore completo
     *
     * @param company  croce di appartenenza
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     */
    public Turno(Company company, Servizio servizio, Date inizio,Date fine) {
        super();
        super.setCompany(company);
        setServizio(servizio);
        setInizio(inizio);
        setOraInizio(oraInizio);
        setOraFine(oraFine);
        setPersone(persone);
    }// end of constructor

    /**
     * Costruttore completo
     *
     * @param sigla
     */
    public Turno(String sigla) {
        super();
    }// end of general constructor

    /**
     * Recupera una istanza di Turno usando la query standard della Primary Key
     *
     * @param id valore della Primary Key
     * @return istanza di Turno, null se non trovata
     */
    public static Turno find(long id) {
        Turno instance = null;
        BaseEntity entity = AQuery.queryById(Turno.class, id);

        if (entity != null) {
            if (entity instanceof Turno) {
                instance = (Turno) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

//    /**
//     * Recupera una istanza di Turno usando la query di una property specifica
//     *
//     * @param sigla valore della property Sigla
//     * @return istanza di Turno, null se non trovata
//     */
//    public static Turno findByv(String sigla) {
//        Turno instance = null;
//        BaseEntity entity = AQuery.queryOne(Turno.class, Turno_.sigla, sigla);
//
//        if (entity != null) {
//            if (entity instanceof Turno) {
//                instance = (Turno) entity;
//            }// end of if cycle
//        }// end of if cycle
//
//        return instance;
//    }// end of method

    /**
     * Recupera il valore del numero totale di records della della Entity
     *
     * @return numero totale di records della tavola
     */
    public static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(Turno.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     *
     * @return lista di tutte le istanze di Turno
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Turno> findAll() {
        return (ArrayList<Turno>) AQuery.getLista(Turno.class);
    }// end of method

    public Servizio getServizio() {
        return servizio;
    }// end of getter method

    public void setServizio(Servizio servizio) {
        this.servizio = servizio;
    }//end of setter method

    public Date getInizio() {
        return inizio;
    }// end of getter method

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }//end of setter method

    public Date getFine() {
        return fine;
    }// end of getter method

    public void setFine(Date fine) {
        this.fine = fine;
    }//end of setter method

    public Funzione getFunzione1() {
        return funzione1;
    }// end of getter method

    public void setFunzione1(Funzione funzione1) {
        this.funzione1 = funzione1;
    }//end of setter method

    public Funzione getFunzione2() {
        return funzione2;
    }// end of getter method

    public void setFunzione2(Funzione funzione2) {
        this.funzione2 = funzione2;
    }//end of setter method

    public Funzione getFunzione3() {
        return funzione3;
    }// end of getter method

    public void setFunzione3(Funzione funzione3) {
        this.funzione3 = funzione3;
    }//end of setter method

    public Funzione getFunzione4() {
        return funzione4;
    }// end of getter method

    public void setFunzione4(Funzione funzione4) {
        this.funzione4 = funzione4;
    }//end of setter method

    public Milite getMilite1() {
        return milite1;
    }// end of getter method

    public void setMilite1(Milite milite1) {
        this.milite1 = milite1;
    }//end of setter method

    public Milite getMilite2() {
        return milite2;
    }// end of getter method

    public void setMilite2(Milite milite2) {
        this.milite2 = milite2;
    }//end of setter method

    public Milite getMilite3() {
        return milite3;
    }// end of getter method

    public void setMilite3(Milite milite3) {
        this.milite3 = milite3;
    }//end of setter method

    public Milite getMilite4() {
        return milite4;
    }// end of getter method

    public void setMilite4(Milite milite4) {
        this.milite4 = milite4;
    }//end of setter method

    public Timestamp getLastModificaFunzione1() {
        return lastModificaFunzione1;
    }// end of getter method

    public void setLastModificaFunzione1(Timestamp modificaFunzione1) {
        this.lastModificaFunzione1 = modificaFunzione1;
    }//end of setter method

    public Timestamp getLastModificaFunzione2() {
        return lastModificaFunzione2;
    }// end of getter method

    public void setLastModificaFunzione2(Timestamp modificaFunzione2) {
        this.lastModificaFunzione2 = modificaFunzione2;
    }//end of setter method

    public Timestamp getLastModificaFunzione3() {
        return lastModificaFunzione3;
    }// end of getter method

    public void setLastModificaFunzione3(Timestamp modificaFunzione3) {
        this.lastModificaFunzione3 = modificaFunzione3;
    }//end of setter method

    public Timestamp getLastModificaFunzione4() {
        return lastModificaFunzione4;
    }// end of getter method

    public void setLastModificaFunzione4(Timestamp modificaFunzione4) {
        this.lastModificaFunzione4 = modificaFunzione4;
    }//end of setter method

    public int getOreMilite1() {
        return oreMilite1;
    }// end of getter method

    public void setOreMilite1(int oreMilite1) {
        this.oreMilite1 = oreMilite1;
    }//end of setter method

    public int getOreMilite2() {
        return oreMilite2;
    }// end of getter method

    public void setOreMilite2(int oreMilite2) {
        this.oreMilite2 = oreMilite2;
    }//end of setter method

    public int getOreMilite3() {
        return oreMilite3;
    }// end of getter method

    public void setOreMilite3(int oreMilite3) {
        this.oreMilite3 = oreMilite3;
    }//end of setter method

    public int getOreMilite4() {
        return oreMilite4;
    }// end of getter method

    public void setOreMilite4(int oreMilite4) {
        this.oreMilite4 = oreMilite4;
    }//end of setter method

    public boolean isProblemiFunzione1() {
        return problemiFunzione1;
    }// end of getter method

    public void setProblemiFunzione1(boolean problemiFunzione1) {
        this.problemiFunzione1 = problemiFunzione1;
    }//end of setter method

    public boolean isProblemiFunzione2() {
        return problemiFunzione2;
    }// end of getter method

    public void setProblemiFunzione2(boolean problemiFunzione2) {
        this.problemiFunzione2 = problemiFunzione2;
    }//end of setter method

    public boolean isProblemiFunzione3() {
        return problemiFunzione3;
    }// end of getter method

    public void setProblemiFunzione3(boolean problemiFunzione3) {
        this.problemiFunzione3 = problemiFunzione3;
    }//end of setter method

    public boolean isProblemiFunzione4() {
        return problemiFunzione4;
    }// end of getter method

    public void setProblemiFunzione4(boolean problemiFunzione4) {
        this.problemiFunzione4 = problemiFunzione4;
    }//end of setter method

    public String getNotaFunzione1() {
        return notaFunzione1;
    }// end of getter method

    public void setNotaFunzione1(String notaFunzione1) {
        this.notaFunzione1 = notaFunzione1;
    }//end of setter method

    public String getNotaFunzione2() {
        return notaFunzione2;
    }// end of getter method

    public void setNotaFunzione2(String notaFunzione2) {
        this.notaFunzione2 = notaFunzione2;
    }//end of setter method

    public String getNotaFunzione3() {
        return notaFunzione3;
    }// end of getter method

    public void setNotaFunzione3(String notaFunzione3) {
        this.notaFunzione3 = notaFunzione3;
    }//end of setter method

    public String getNotaFunzione4() {
        return notaFunzione4;
    }// end of getter method

    public void setNotaFunzione4(String notaFunzione4) {
        this.notaFunzione4 = notaFunzione4;
    }//end of setter method

    public String getTitoloExtra() {
        return titoloExtra;
    }// end of getter method

    public void setTitoloExtra(String titoloExtra) {
        this.titoloExtra = titoloExtra;
    }//end of setter method

    public String getLocalitàExtra() {
        return localitàExtra;
    }// end of getter method

    public void setLocalitàExtra(String localitàExtra) {
        this.localitàExtra = localitàExtra;
    }//end of setter method

    public String getNote() {
        return note;
    }// end of getter method

    public void setNote(String note) {
        this.note = note;
    }//end of setter method

    public boolean isAssegnato() {
        return assegnato;
    }// end of getter method

    public void setAssegnato(boolean assegnato) {
        this.assegnato = assegnato;
    }//end of setter method

    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatoria invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza di Turno con gli stessi valori dei parametri di questa istanza
     */
    @Override
    @SuppressWarnings("all")
    public Turno clone() throws CloneNotSupportedException {
        try {
            return (Turno) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of domain class
