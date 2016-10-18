package it.algos.wam.entity.turno;

import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.lib.LibWam;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.lib.DateConvertUtils;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.*;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity che descrive un Turno
 * Estende la Entity astratta WamCompany che contiene la property wamcompany
 * La property wamcompany può essere nulla nella superclasse, ma NON in questa classe dove è obbligatoria
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 * <p>
 */
@Entity
public class Turno extends WamCompanyEntity {

    //------------------------------------------------------------------------------------------------------------------------
    // Properties
    //------------------------------------------------------------------------------------------------------------------------
    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    //--company di riferimento (facoltativa nella superclasse)
    //--company di riferimento (obbligatoria in questa classe)
    //--private BaseCompany company;

    // servizio di riferimento
    @NotNull
    @ManyToOne
    private Servizio servizio = null;

    // iscrizioni dei volontari a questo turno
    @OneToMany(mappedBy = "turno", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<Iscrizione> iscrizioni = new ArrayList<>();

    //--chiave indicizzata per query più veloci e 'mirate' (obbligatoria)
    //--annoX1000 + giorno nell'anno
    //--individua tutti i turni di una company per un giorno
    @NotNull
    @Index
    private long chiave;

    //--giorno, ora e minuto di inizio turno
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date inizio;

    //--giorno, ora e minuto di fine turno
    //--i servizi senza orario (fisso) vengono creati solo con la data di inizio; la data di fine viene aggiunta dopo
    @Temporal(TemporalType.TIMESTAMP)
    private Date fine;

    //--motivazione del turno extra
    private String titoloExtra = "";
    //--nome evidenziato della località per turni extra
    private String localitaExtra = "";
    //--descrizione dei viaggi extra
    private String note = "";

    //--turno previsto (vuoto) oppure assegnato (militi inseriti)
    private boolean assegnato = false;

    //------------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Costruttore senza argomenti
     * Obbligatorio per le specifiche JavaBean
     * Da non usare MAI per la creazione diretta di una nuova istanza (si perdono i controlli)
     */
    public Turno() {
        this(null,null);
    }// end of JavaBean constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * La chiave (obbligatoria) viene calcolata in automatico prima del persist
     *
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     */
    public Turno(Servizio servizio, Date inizio) {
        this((WamCompany) null, servizio, inizio);
    }// end of constructor


    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Filtrato sulla company passata come parametro.
     * La chiave (obbligatoria) viene calcolata in automatico prima del persist
     *
     * @param company  di appartenenza (property della superclasse)
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     */
    public Turno(WamCompany company, Servizio servizio, Date inizio) {
        this(company, servizio, inizio, (Date) null, (List<Iscrizione>) null);
    }// end of constructor

    /**
     * Costruttore completo
     * Filtrato sulla company passata come parametro.
     * La chiave (obbligatoria) viene calcolata in automatico prima del persist
     *
     * @param company    di appartenenza (property della superclasse)
     * @param servizio   tipologia di servizio (obbligatoria)
     * @param inizio     giorno, ora e minuto di inizio turno
     * @param fine       giorno, ora e minuto di fine turno
     * @param iscrizioni dei volontari a questo turno
     */
    public Turno(WamCompany company, Servizio servizio, Date inizio, Date fine, List<Iscrizione> iscrizioni) {
        super();
        if (company != null) {
            this.setCompany(company);
        }// end of if cycle
        this.setServizio(servizio);
        this.setInizio(inizio);
        this.setFine(fine);
        this.setIscrizioni(iscrizioni);
        this.setAssegnato(iscrizioni != null);
    }// end of constructor


    //------------------------------------------------------------------------------------------------------------------------
    // Count records
    //------------------------------------------------------------------------------------------------------------------------


    /**
     * Recupera il numero totale di records della Entity
     * Senza filtri.
     *
     * @return il numero totale di records nella Entity
     */
    public static int countByAllCompanies() {
        return countByAllCompanies((EntityManager) null);
    }// end of static method

    /**
     * Recupera il numero totale di records della Entity
     * Senza filtri.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param manager the EntityManager to use
     * @return il numero totale di records nella Entity
     */
    public static int countByAllCompanies(EntityManager manager) {
        return AQuery.count(Turno.class, manager);
    }// end of static method

    /**
     * Recupera il numero di records della Entity
     * Filtrato sulla company corrente.
     *
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCurrentCompany() {
        return countByCurrentCompany((EntityManager) null);
    }// end of static method

    /**
     * Recupera il numero di records della Entity
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param manager the EntityManager to use
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCurrentCompany(EntityManager manager) {
        return countByCompany(WamCompany.getCurrent(), manager);
    }// end of static method


    /**
     * Recupera il numero di records della Entity
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCompany(WamCompany company) {
        return countByCompany(company, (EntityManager) null);
    }// end of static method

    /**
     * Recupera il numero di records della Entity
     * Filtrato sulla company passata come parametro.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param company di appartenenza (property della superclasse)
     * @param manager the EntityManager to use
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCompany(WamCompany company, EntityManager manager) {
        return CompanyQuery.count(Turno.class, company, manager);
    }// end of static method

    /**
     * Recupera il numero di records della Entity, filtrato sul valore della property indicata
     * Filtrato sulla company passata come parametro.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param company di appartenenza (property della superclasse)
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the EntityManager to use
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCompanyAndProperty(WamCompany company, SingularAttribute attr, Object value, EntityManager manager) {
        return CompanyQuery.count(Turno.class, attr, value, company, manager);
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Find entity by primary key
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
     *
     * @param id valore (unico) della Primary Key
     * @return istanza della Entity, null se non trovata
     */
    public static Turno find(long id) {
        return find(id, (EntityManager) null);
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param id      valore (unico) della Primary Key
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Turno find(long id, EntityManager manager) {
        return (Turno) AQuery.find(Turno.class, id, manager);
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // Get single entity by SingularAttribute
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company corrente
     *
     * @param chiave   indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @param servizio tipologia di servizio (obbligatoria)
     * @return istanza della Entity, null se non trovata
     */
    public static Turno getEntityByChiaveAndServizio(long chiave, Servizio servizio) {
        return getEntityByCompanyAndChiaveAndServizio(WamCompany.getCurrent(), chiave, servizio, (EntityManager) null);
    }// end of static method

    /**
     * Controlla che esista una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company corrente
     *
     * @param chiave   indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @param servizio tipologia di servizio (obbligatoria)
     * @return vero se esiste Entity, false se non trovata
     */
    public static boolean isEntityByChiaveAndServizio(long chiave, Servizio servizio) {
        return getEntityByChiaveAndServizio(chiave, servizio) != null;
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     * I servizi orari sono uno per giorno e quindi trova la entity
     * I servizi non orari possono essere più di uno per giorno e quindi (se sono più di uno) non trova la entity
     *
     * @param company  di appartenenza (property della superclasse)
     * @param chiave   indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @param servizio tipologia di servizio (obbligatoria)
     * @param manager  the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Turno getEntityByCompanyAndChiaveAndServizio(WamCompany company, long chiave, Servizio servizio, EntityManager manager) {
        return (Turno) CompanyQuery.getEntity(Turno.class, Turno_.chiave, chiave, Turno_.servizio, servizio, company, manager);
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Get entities (list)
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Senza filtri.
     *
     * @return lista di tutte le entities
     */
    public static List<Turno> getListByAllCompanies() {
        return getListByAllCompanies((EntityManager) null);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Senza filtri.
     * (non va usata CompanyQuery, altrimenti arriverebbe solo la lista della company corrente)
     *
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    @SuppressWarnings("unchecked")
    public static List<Turno> getListByAllCompanies(EntityManager manager) {
        return (List<Turno>) AQuery.getList(Turno.class, manager);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     *
     * @return lista di tutte le entities
     */
    public static List<Turno> getListByCurrentCompany() {
        return getListByCurrentCompany((EntityManager) null);
    }// end of static method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     *
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    public static List<Turno> getListByCurrentCompany(EntityManager manager) {
        return getListByCompany(WamCompany.getCurrent(), manager);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company passata come parametro.
     * Se si arriva qui con una company null, vuol dire che non esiste la company corrente
     *
     * @param company di appartenenza (property della superclasse)
     * @return lista di tutte le entities
     */
    public static List<Turno> getListByCompany(WamCompany company) {
        return getListByCompany(company, (EntityManager) null);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company passata come parametro.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param company di appartenenza (property della superclasse)
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    @SuppressWarnings("unchecked")
    public static List<Turno> getListByCompany(WamCompany company, EntityManager manager) {
        if (company != null) {
            return (List<Turno>) CompanyQuery.getList(Turno.class, CompanyEntity_.company, company, manager);
        } else {
            return new ArrayList<>();
        }// end of if/else cycle
    }// end of static method


    /**
     * Recupera una lista (array) di entities usando la query di una property specifica
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param inizio data di svolgimento (inizio) del servizio (obbligatoria)
     * @return lista delle entities filtrate
     */
    @SuppressWarnings("unchecked")
    public static List<Turno> getListByDate(Date inizio) {
        return getListByCompanyAndDate((WamCompany) CompanySessionLib.getCompany(), inizio, (EntityManager) null);
    }// end of static method

    /**
     * Recupera una lista (array) di entities usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param company di appartenenza (property della superclasse)
     * @param inizio  data di svolgimento (inizio) del servizio (obbligatoria)
     * @param manager the EntityManager to use
     * @return lista delle entities filtrate
     */
    @SuppressWarnings("unchecked")
    public static List<Turno> getListByCompanyAndDate(WamCompany company, Date inizio, EntityManager manager) {
        return (List<Turno>) CompanyQuery.getList(Turno.class, Turno_.inizio, company, manager);
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // New and save
    //------------------------------------------------------------------------------------------------------------------------

    public static Turno crea(Servizio servizio, Date inizio) {
        return crea((WamCompany) CompanySessionLib.getCompany(), servizio, inizio, (Date) null);
    }// end of static method

    public static Turno crea(WamCompany company, Servizio servizio, Date inizio) {
        return crea(company, servizio, inizio, (Date) null);
    }// end of static method

    public static Turno crea(WamCompany company, Servizio servizio, Date inizio, Date fine) {
        return crea(company, servizio, inizio, fine, (EntityManager) null);
    }// end of static method

    public static Turno crea(WamCompany company, Servizio servizio, Date inizio, Date fine, EntityManager manager) {
        return crea(company, servizio, inizio, fine, false, manager);
    }// end of static method

    /**
     * Creazione iniziale di un turno
     * Lo crea SOLO se non esiste (nel casi di servizi ad orario)
     * Lo crea anche se esiste già (nel caso di servizi ripetitivi nello stesso giorno)
     *
     * @param company   croce di appartenenza
     * @param manager   the EntityManager to use
     * @param servizio  tipologia di servizio (obbligatoria)
     * @param inizio    giorno, ora e minuto di inizio turno
     * @param fine      giorno, ora e minuto di fine turno
     * @param assegnato turno previsto (vuoto) oppure assegnato (militi inseriti)
     * @return istanza di turno
     */
    public static Turno crea(WamCompany company, Servizio servizio, Date inizio, Date fine, boolean assegnato, EntityManager manager) {
        Turno turno = null;
        int chiave = LibWam.creaChiave(inizio);

        if (servizio == null || inizio == null) {
            return null;
        }// end of if cycle

        if (servizio.isOrario()) {
//            turno = Turno.find(servizio, inizio);
//            turno = Turno.find(servizio, chiave);
            turno = getEntityByChiaveAndServizio(chiave, servizio);
//            if (isEntityByChiaveAndServizio(chiave,servizio)) {
//                codice
//            }// end of if cycle

        }// end of if cycle

        if (turno == null) {
            if (fine == null && servizio.isOrario()) {
                fine = inizio;
            }// end of if cycle

            turno = new Turno(company, servizio, inizio);
            turno.setFine(fine);
            turno.setAssegnato(assegnato);
            turno = turno.save(company, manager);
        }// end of if cycle

        return turno;
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // Delete
    //------------------------------------------------------------------------------------------------------------------------
    public static int deleteAll() {
        return deleteAll((WamCompany) CompanySessionLib.getCompany(), (EntityManager) null);
    }// end of static method

    public static int deleteAll(WamCompany company) {
        return deleteAll(company, (EntityManager) null);
    }// end of static method

    public static int deleteAll(EntityManager manager) {
        return deleteAll((WamCompany) CompanySessionLib.getCompany(), manager);
    }// end of static method

    /**
     * Delete all the records for the Entity class
     * Bulk delete records with CriteriaDelete
     *
     * @param manager the EntityManager to use
     */
    public static int deleteAll(WamCompany company, EntityManager manager) {
        return CompanyQuery.delete(Turno.class, company, manager);
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // utilities
    //------------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------------
    // Getter and setter
    //------------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------------
    // Save
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una lista di Turni usando la chiave specifica
     *
     * @param chiave indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @return lista di Turni, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Turno> findAllChiave(int chiave) {

        BaseCompany company = CompanySessionLib.getCompany();

        ArrayList<Turno> lista = null;
        List<Turno> listaPerChiave = (List<Turno>) CompanyQuery.getList(Turno.class, Turno_.chiave, chiave);

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
     * Recupera una lista di Turni usando la query di tutte e sole le property obbligatorie
     * Se il servizio è multiplo, ce ne possono essere diversi al giorno (per wamcompany)
     * Se il servizio NON è multiplo, conviene usare la chiamata find (stessi parametri)
     *
     * @param servizio tipologia di servizio (obbligatoria)
     * @param chiave   indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @return istanza di Servizio, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Turno> findAll(Servizio servizio, int chiave) {
        ArrayList<Turno> lista = null;

        if (servizio == null || chiave == 0) {
            return null;
        }// end of if cycle

        lista = findAllChiave(chiave);

        return lista;
    }// end of method

    /**
     * Recupera una lista di Turni usando la query di tutte e sole le property obbligatorie
     * Se il servizio è multiplo, ce ne possono essere diversi al giorno (per wamcompany)
     * Se il servizio NON è multiplo, conviene usare la chiamata find (stessi parametri)
     *
     * @param company  croce di appartenenza
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     * @return istanza di Servizio, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Turno> findAll(WamCompany company, Servizio servizio, Date inizio) {
        return findAll(servizio, LibWam.creaChiave(inizio));
    }// end of method

    /**
     * Recupera una istanza di Turno usando la chiave specifica
     *
     * @param company croce di appartenenza
     * @param chiave  indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @return istanza di Turno, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Turno findChiave(WamCompany company, int chiave) {
        Turno instance = null;
        ArrayList<Turno> lista = findAllChiave(chiave);

        if (lista != null && lista.size() == 1) {
            instance = lista.get(0);
        }// end of if cycle

        return instance;
    }// end of static method

    /**
     * Recupera una istanza di Turno usando la query di tutte e sole le property obbligatorie
     * Se il servizio NON è multiplo, ce ne deve essere SOLO UNO al giorno (per wamcompany)
     * Se il servizio è multiplo, usare la chiamata findAll (stessi parametri)
     *
     * @param servizio tipologia di servizio (obbligatoria)
     * @param chiave   indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @return istanza di Servizio, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Turno find(Servizio servizio, int chiave) {
        Turno instance = null;
        ArrayList<Turno> listaTurni = null;
        Servizio servizioTmp = null;

        if (servizio == null || chiave == 0) {
            return null;
        }// end of if cycle

        if (!servizio.isOrario()) {
            return null;//@todo per ora
        } else {
            listaTurni = findAll(servizio, chiave);
            if (listaTurni != null && listaTurni.size() > 0) {
                for (Turno turno : listaTurni) {
                    servizioTmp = turno.getServizio();
                    if (servizioTmp != null && servizioTmp.getId().equals(servizio.getId())) {
                        instance = turno;
                    }
                }
            }
        }

        return instance;
    }// end of method

    //------------------------------------------------------------------------------------------------------------------------
    // Utilities
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una istanza di Turno usando la query di tutte e sole le property obbligatorie
     * Se il servizio NON è multiplo, ce ne deve essere SOLO UNO al giorno (per wamcompany)
     * Se il servizio è multiplo, usare la chiamata findAll (stessi parametri)
     *
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     * @return istanza di Servizio, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Turno find(Servizio servizio, Date inizio) {
        return find(servizio, LibWam.creaChiave(inizio));
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     *
     * @return lista di tutte le istanze di Turno
     */
    @SuppressWarnings("unchecked")
    public static List<Turno> findAll() {
        return (ArrayList<Turno>) CompanyQuery.getList(Turno.class);
    }// end of method

    //------------------------------------------------------------------------------------------------------------------------
    // Save
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Saves this entity to the database using a local EntityManager
     *
     * @return the merged Entity (new entity, unmanaged, has the id)
     */
    @Override
    public BaseEntity save() {
        return this.save((EntityManager) null);
    }// end of method


    /**
     * Saves this entity to the database using a local EntityManager
     *
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return the merged Entity (new entity, unmanaged, has the id)
     */
    @Override
    public BaseEntity save(EntityManager manager) {
        return this.save(getWamCompany(), manager);
    }// end of method

    /**
     * Saves this entity to the database.
     * <p>
     * If the provided EntityManager has an active transaction, the operation is performed inside the transaction.<br>
     * Otherwise, a new transaction is used to save this single entity.
     *
     * @param company da filtrare
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return the merged Entity (new entity, unmanaged, has the id)
     */
    public Turno save(WamCompany company, EntityManager manager) {
        boolean valido;

        valido = super.checkCompany();
        if (valido) {
            valido = this.checkServizio();
        }// end of if cycle
        if (valido) {
            valido = this.checkInizio();
        }// end of if cycle
        if (valido) {
            valido = this.checkChiave();
        }// end of if cycle

        if (valido) {
            return (Turno) super.save(manager);
        } else {
            return null;
        }// end of if/else cycle

    }// end of method

    /**
     * Saves this entity to the database.
     * Usa l'EntityManager di default
     *
     * @return the merged Entity (new entity, unmanaged, has the id), casted as Funzione
     */
    public Turno saveSafe() {
        return saveSafe((EntityManager) null);
    }// end of method

    /**
     * Saves this entity to the database.
     * <p>
     * If the provided EntityManager has an active transaction, the operation is performed inside the transaction.<br>
     * Otherwise, a new transaction is used to save this single entity.
     *
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return the merged Entity (new entity, unmanaged, has the id), casted as Funzione
     */
    public Turno saveSafe(EntityManager manager) {
        return (Turno) this.save(manager);
    }// end of method

    /**
     * Implementa come business logic, la obbligatorietà del servizio
     * <p>
     *
     * @return true se esiste, false se non esiste
     */
    private boolean checkServizio() {
        String caption = "La funzione non può essere accettata, perché manca la sigla che è obbligatoria";

        if (getServizio() != null && getServizio() != null) {
            return true;
        } else {
//            Notification.show(caption, Notification.Type.WARNING_MESSAGE);
            return false;
        }// end of if/else cycle
    } // end of method

    /**
     * Implementa come business logic, la obbligatorietà della data di inizio turno
     * <p>
     *
     * @return true se esiste, false se non esiste
     */
    private boolean checkInizio() {
        String caption = "La funzione non può essere accettata, perché manca la descrizione che è obbligatoria";

        if (getInizio() != null && getInizio() != null) {
            return true;
        } else {
//            Notification.show(caption, Notification.Type.WARNING_MESSAGE);
            return false;
        }// end of if/else cycle
    } // end of method

//    /**
//     * Recupera il valore del numero totale di records della della Entity
//     *
//     * @return numero totale di records della tavola
//     */
//    public static int count() {
//        int totRec = 0;
//        long totTmp = CompanyQuery.count(Turno.class);
//
//        if (totTmp > 0) {
//            totRec = (int) totTmp;
//        }// fine del blocco if
//
//        return totRec;
//    }// end of method

    /**
     * Costruisce (od aggiorna) la chiave di ricerca indicizzata
     * Se il servizio è orario, deve essere l'unico nella giornata (per la stessa company, ovviamente)
     * Se il servizio non è orario, nessun controllo
     */
    public boolean checkChiave() {
        boolean valido = true;
        chiave = LibWam.creaChiave(inizio);

        if (servizio.isOrario()) {
//            turno = Turno.find(servizio, inizio);
//            turno = Turno.find(servizio, chiave);

        }// end of if cycle

        return valido;
    }// end of method

    public void add(Iscrizione iscrizione) {
//        TurnoIscrizione tunIsc = null;

//        if (getCompany() == null) {
//            Exception e = new Exception("Impossibile aggiungere iscrizioni al turno se manca la company");
//            e.printStackTrace();
//            return;
//        }// end of if cycle

        iscrizioni.add(iscrizione);

//        tunIsc = new TurnoIscrizione(this, iscrizione);
//        tunIsc.setCompany(getCompany());

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

    /**
     * Ritorna la data iniziale come LocalDate
     */
    public LocalDate getData1() {
        LocalDate d = null;
        if (getInizio() != null) {
            d = DateConvertUtils.asLocalDate(getInizio());
        }
        return d;
    }


    /**
     * Ritorna il tempo di inizio del turno
     * (data del turno + ora del servizio)
     */
    public LocalDateTime getStartTime() {
        Timestamp timestamp = Timestamp.valueOf(getData1().atStartOfDay());
        LocalDateTime ldt = timestamp.toLocalDateTime();
        int hours = getServizio().getOraInizio();
        ldt = ldt.plusHours(hours);
        int mins = getServizio().getMinutiInizio();
        ldt = ldt.plusMinutes(mins);
        return ldt;
    }


    public Date getFine() {
        return fine;
    }// end of getter method

    public void setFine(Date fine) {
        this.fine = fine;
    }//end of setter method

    /**
     * Ritorna la durata totale del turno in minuti
     */
    public int getMinutiTotali() {
        int minuti = 0;
        Servizio serv = getServizio();
        if (serv != null) {
            minuti = serv.getMinutiTotali();
        }
        return minuti;
    }

    public String getTitoloExtra() {
        return titoloExtra;
    }// end of getter method

    public void setTitoloExtra(String titoloExtra) {
        this.titoloExtra = titoloExtra;
    }//end of setter method

    public String getLocalitaExtra() {
        return localitaExtra;
    }// end of getter method

    public void setLocalitaExtra(String localitaExtra) {
        this.localitaExtra = localitaExtra;
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
     * Controlla se questo turno ha le iscrizioni coperte per tutte le funzioni
     * obbligatorie dichiarate dal relativo Servizio.
     *
     * @return true se ha le iscrizioni.
     */
    public boolean isValido() {
        boolean valido = true;
        List<ServizioFunzione> funzioni = getServizio().getFunzioniObbligatorie();
        for (ServizioFunzione sf : funzioni) {
            if (getIscrizione(sf) == null) {
                valido = false;
                break;
            }
        }
        return valido;
    }

    /**
     * Controlla se questo turno ha le iscrizioni coperte per tutte le funzioni
     * (obbligatorie e non) dichiarate dal relativo Servizio
     *
     * @return true se ha le iscrizioni.
     */
    public boolean isCompleto() {
        boolean completo = true;
        List<ServizioFunzione> lista = getServizio().getServizioFunzioniOrdine();
        for (ServizioFunzione sf : lista) {
            if (getIscrizione(sf) == null) {
                completo = false;
                break;
            }
        }
        return completo;
    }

    public List<Iscrizione> getIscrizioni() {
        return iscrizioni;
    }

    public void setIscrizioni(List<Iscrizione> iscrizioni) {
        this.iscrizioni = iscrizioni;
    }

    /**
     * Recupera la eventuale iscrizione a una data funzione.
     *
     * @param sf il ServizioFunzione
     * @return l'iscrizione
     */
    public Iscrizione getIscrizione(ServizioFunzione sf) {
        Iscrizione iscrizione = null;
        if (getIscrizioni()!=null) {
            for (Iscrizione i : getIscrizioni()) {
                if (i != null) {
                    ServizioFunzione s = i.getServizioFunzione();
                    if (s != null) {
                        if (s.equals(sf)) {
                            iscrizione = i;
                            break;
                        }
                    }
                }
            }
        }// end of if cycle

        return iscrizione;
    }

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
