package it.algos.wam.entity.turno;

import it.algos.wam.entity.company.Company;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.lib.LibWam;
import it.algos.wam.wrapturno.WrapTurno;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    //--chiave indicizzata per query più veloci e 'mirate' (obbligatoria)
    //--annoX1000 + giorno nell'anno
    @NotNull
    @Index
    private long chiave;

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

    //--iscrizioni dei militi/volontari per questo turno
    //--all'inizio puà essere nullo (per un turno previsto ma ancora senza iscrizioni)
    private WrapTurno wrapTurno = null;


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
        this(company, servizio, inizio, null, null, false);
    }// end of constructor

    /**
     * Costruttore completo
     *
     * @param company   croce di appartenenza
     * @param servizio  tipologia di servizio (obbligatoria)
     * @param inizio    giorno, ora e minuto di inizio turno
     * @param fine      giorno, ora e minuto di fine turno
     * @param wrapTurno iscrizioni dei militi/volontari per questo turno
     * @param assegnato turno previsto (vuoto) oppure assegnato (militi inseriti)
     */
    public Turno(Company company, Servizio servizio, Date inizio, Date fine, WrapTurno wrapTurno, boolean assegnato) {
        super();
        super.setCompany(company);
        setServizio(servizio);
        setInizio(inizio);
        setFine(fine);
        setWrapTurno(wrapTurno);
        setAssegnato(assegnato);
    }// end of constructor

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

    /**
     * Recupera una lista di Turni usando la chiave specifica
     *
     * @param company croce di appartenenza
     * @param chiave  indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @return lista di Turni, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Turno> findAllChiave(Company company, long chiave) {
        ArrayList<Turno> lista = null;
        List<Turno> listaPerChiave = (List<Turno>) AQuery.queryList(Turno.class, Turno_.chiave, chiave);

        if (listaPerChiave != null && listaPerChiave.size() > 0) {
            lista = new ArrayList();
            for (Turno turno : listaPerChiave) {
                if (turno.getCompany().getId().equals(company.getId())) {
                    lista.add(turno);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method

    /**
     * Recupera una istanza di Turno usando la chiave specifica
     *
     * @param company croce di appartenenza
     * @param chiave  indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @return istanza di Turno, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Turno findChiave(Company company, long chiave) {
        Turno istanza = null;
        ArrayList<Turno> lista = findAllChiave(company, chiave);

        if (lista != null && lista.size() == 1) {
            istanza = lista.get(0);
        }// end of if cycle

        return istanza;
    }// end of static method


    /**
     * Recupera una lista di Turni usando la query di tutte e sole le property obbligatorie
     * Se il servizio è multiplo, ce ne possono essere diversi al giorno (per company)
     * Se il servizio NON è multiplo, conviene usare la chiamata find (stessi parametri)
     *
     * @param company  croce di appartenenza
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     * @return istanza di Servizio, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Turno> findAll(Company company, Servizio servizio, Date inizio) {
        ArrayList<Turno> lista = null;
        long chiave;

        if (company == null || servizio == null || inizio == null) {
            return null;
        }// end of if cycle

        chiave = LibWam.creaChiave(inizio);
        lista = findAllChiave(company, chiave);

        return lista;
    }// end of method


    /**
     * Recupera una istanza di Turno usando la query di tutte e sole le property obbligatorie
     * Se il servizio NON è multiplo, ce ne deve essere SOLO UNO al giorno (per company)
     * Se il servizio è multiplo, usare la chiamata findAll (stessi parametri)
     *
     * @param company  croce di appartenenza
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     * @return istanza di Servizio, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Turno find(Company company, Servizio servizio, Date inizio) {
        Turno instance = null;
        long chiave;

        if (company == null || servizio == null || inizio == null) {
            return null;
        }// end of if cycle

        if (servizio.isMultiplo()) {
            return null;
        }// end of if cycle

        chiave = LibWam.creaChiave(inizio);
        instance = findChiave(company, chiave);

        return instance;
    }// end of method

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


    /**
     * Creazione iniziale di un turno
     * Lo crea SOLO se non esiste
     *
     * @param company  croce di appartenenza
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     * @return istanza di turno
     */
    public static Turno crea(Company company, Servizio servizio, Date inizio) {
        return crea(company, servizio, inizio, null, null, false);
    }// end of static method


    /**
     * Creazione iniziale di un turno
     * Lo crea SOLO se non esiste
     *
     * @param company   croce di appartenenza
     * @param servizio  tipologia di servizio (obbligatoria)
     * @param inizio    giorno, ora e minuto di inizio turno
     * @param fine      giorno, ora e minuto di fine turno
     * @param wrapTurno iscrizioni dei militi/volontari per questo turno
     * @param assegnato turno previsto (vuoto) oppure assegnato (militi inseriti)
     * @return istanza di turno
     */
    public static Turno crea(Company company, Servizio servizio, Date inizio, Date fine, WrapTurno wrapTurno, boolean assegnato) {
        Turno turno = null;

        if (company == null || servizio == null || inizio == null) {
            return null;
        }// end of if cycle

        if (!servizio.isMultiplo()) {
            turno = Turno.find(company, servizio, inizio);
        }// end of if cycle

        if (turno == null) {
            if (servizio!=null&&servizio.isOrario()) {
                fine=inizio;
            }// end of if cycle

            turno = new Turno(company, servizio, inizio, fine, wrapTurno, assegnato);
            turno.save();
        }// end of if cycle

        return turno;
    }// end of static method


    /**
     * Costruisce (od aggiorna) la chiave di ricerca indicizzata
     */
    @PrePersist
    public void fixChiave() {
        chiave = LibWam.creaChiave(inizio);
    }// end of method

    @Override
    public String toString() {
        return getServizio() + "/" + getInizio();
    }// end of method

    public long getChiave() {
        return chiave;
    }// end of getter method

    public void setChiave(long chiave) {
        this.chiave = chiave;
    }//end of setter method

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

    public WrapTurno getWrapTurno() {
        return wrapTurno;
    }// end of getter method

    public void setWrapTurno(WrapTurno wrapTurno) {
        this.wrapTurno = wrapTurno;
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
