package it.algos.wam.entity.servizio;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Notification;
import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Entity che descrive un Servizio (tipo di turno)
 * Estende la Entity astratta WamCompany che contiene la property wamcompany
 * <p>
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
public class Servizio extends WamCompanyEntity {

    //------------------------------------------------------------------------------------------------------------------------
    // Property
    //------------------------------------------------------------------------------------------------------------------------
    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    //--sigla di codifica visibile (obbligatoria, non unica)
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    @Column(length = 20)
    @Index
    private String sigla = "";

    //--sigla di codifica interna specifica per company (obbligatoria, unica)
    //--calcolata -> codeCompanyUnico = company.companyCode + funzione.sigla (20+20=40);
    @NotEmpty
    @NotNull
    @Column(length = 40, unique = true)
    @Index
    private String codeCompanyUnico;

    //--descrizione per il tabellone (obbligatoria)
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    private String descrizione = "";

    //--ordine di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
    @NotNull
    @Index
    private int ordine = 0;

    // colore del servizio (facoltativo)
    private int colore = new Color(128, 128, 128).getRGB();

    //--orario predefinito (avis, centralino ed extra non ce l'hanno)
    private boolean orario = true;

    //--ora prevista di inizio turno (obbligatoria, se orario è true)
    private int oraInizio;

    //--minuti previsti di inizio turno (facoltativo, standard è zero)
    //--nella GUI la scelta viene bloccata ai quarti d'ora
    private int minutiInizio = 0;

    //--ora prevista di fine turno (obbligatoria, se orario è true)
    private int oraFine;

    //--minuti previsti di fine turno (facoltativo, standard è zero)
    //--nella GUI la scelta viene bloccata ai quarti d'ora
    private int minutiFine = 0;


    @OneToMany(mappedBy = "servizio", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<Turno> turni = new ArrayList<>();


    // CascadeType.ALL: quando chiamo persist sul padre, persiste automaticamente tutti i nuovi figli aggiunti
    // alla lista e non ancora registrati (e così per tutte le operazioni dell'EntityManager)
    // orphanRemoval = true: quando registro il padre, cancella tutti i figli eventualmente rimasti orfani.
    // CascadeOnDelete: instaura l'integrità referenziale a livello di database (foreign key on delete cascade)
    @OneToMany(mappedBy = "servizio", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<ServizioFunzione> servizioFunzioni = new ArrayList<>();


    //------------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Costruttore senza argomenti
     * Obbligatorio per le specifiche JavaBean
     */
    public Servizio() {
    }// end of constructor


    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * L'ordine di presentazione nel tabellone viene inserito in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     */
    public Servizio(WamCompany company, String sigla, String descrizione) {
        this(company, sigla, descrizione, 0, 0);
    }// end of constructor


    /**
     * Costruttore ridotto
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param colore      del gruppo (facoltativo)
     */
    public Servizio(WamCompany company, String sigla, String descrizione, int ordine, int colore) {
        this(company, sigla, descrizione, ordine, colore, false, 0, 0);
    }// end of constructor


    /**
     * Costruttore completo
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param colore      del gruppo (facoltativo)
     * @param orario      servizio ad orario prefissato e fisso ogni giorno (facoltativo)
     * @param oraInizio   del servizio (facoltativo, obbligatorio se orario è true)
     * @param oraFine     del servizio (facoltativo, obbligatorio se orario è true)
     */
    public Servizio(WamCompany company, String sigla, String descrizione, int ordine, int colore, boolean orario, int oraInizio, int oraFine) {
        super();
        this.setCompany(company);
        this.setSigla(sigla);
        this.setDescrizione(descrizione);
        this.setOrdine(ordine);
        this.setColore(colore);
        this.setOrario(orario);
        this.setOraInizio(oraInizio);
        this.setOraFine(oraFine);
    }// end of constructor


    //------------------------------------------------------------------------------------------------------------------------
    // Count records
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera il numero totale dei records della Entity
     * Senza filtri.
     *
     * @return il numero totale di record nella Entity
     */
    public static int countByAllCompanies() {
        return countByAllCompanies(null);
    }// end of static method

    /**
     * Recupera il numero totale dei records della Entity
     * Senza filtri.
     * Use a specific manager (must be close by caller method)
     *
     * @param manager the EntityManager to use
     * @return il numero totale di record nella Entity
     */
    public static int countByAllCompanies(EntityManager manager) {
        long totRec = AQuery.getCount(Servizio.class, manager);
        return check(totRec);
    }// end of static method


    /**
     * Recupera il numero totale dei records della Entity
     * Filtrato sulla azienda corrente.
     *
     * @return il numero totale di record nella Entity
     */
    public static int countByCurrentCompany() {
        return countByCurrentCompany(null);
    }// end of static method

    /**
     * Recupera il numero totale dei records della Entity
     * Filtrato sulla azienda corrente.
     * Use a specific manager (must be close by caller method)
     *
     * @param manager the EntityManager to use
     * @return il numero totale di record nella Entity
     */
    public static int countByCurrentCompany(EntityManager manager) {
        return countBySingleCompany(WamCompany.getCurrent(), manager);
    }// end of static method

    /**
     * Recupera il numero totale dei records della Entity
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @return il numero totale di record nella Entity
     */
    public static int countBySingleCompany(WamCompany company) {
        return countBySingleCompany(company, null);
    }// end of static method

    /**
     * Recupera il numero totale dei records della Entity
     * Filtrato sulla azienda passata come parametro.
     * Use a specific manager (must be close by caller method)
     *
     * @param company di appartenenza (property della superclasse)
     * @param manager the EntityManager to use
     * @return il numero totale di record nella Entity
     */
    public static int countBySingleCompany(WamCompany company, EntityManager manager) {
        long totRec = CompanyQuery.getCount(Servizio.class, company, manager);
        return check(totRec);
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
    public static Servizio find(long id) {
        return find(id, null);
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
     *
     * @param id      valore (unico) della Primary Key
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Servizio find(long id, EntityManager manager) {
        BaseEntity entity = AQuery.find(Servizio.class, id, manager);
        return check(entity);
    }// end of static method

    /**
     * Controlla se l'istanza della Entity esiste ed è della classe corretta
     *
     * @param entity (BaseEntity) restituita dalla query generica
     * @return istanza della Entity specifica, null se non trovata
     */
    private static Servizio check(BaseEntity entity) {
        Servizio instance = null;

        if (entity != null && entity instanceof Servizio) {
            instance = (Servizio) entity;
        }// end of if cycle

        return instance;
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // Find entity by SingularAttribute
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla azienda corrente.
     *
     * @param sigla di riferimento interna (obbligatoria)
     * @return istanza della Entity, null se non trovata
     */
    public static Servizio findBySigla(String sigla) {
        return findBySigla(sigla, null);
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla azienda corrente.
     *
     * @param sigla   di riferimento interna (obbligatoria)
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Servizio findBySigla(String sigla, EntityManager manager) {
        BaseEntity entity = CompanyQuery.queryOne(Funzione.class, Servizio_.sigla, sigla, manager);
        return check(entity);
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param sigla   di riferimento interna (obbligatoria)
     * @return istanza della Entity, null se non trovata
     */
    public static Servizio findByCompanyAndBySigla(WamCompany company, String sigla) {
        return findByCompanyAndBySigla(company, sigla, null);
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param sigla   di riferimento interna (obbligatoria)
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Servizio findByCompanyAndBySigla(WamCompany company, String sigla, EntityManager manager) {
        BaseEntity entity = CompanyQuery.queryOne(Servizio.class, Servizio_.sigla, sigla, manager, company);
        return check(entity);
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // Find entities (list)
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Senza filtri.
     *
     * @return lista di tutte le entities
     */
    public static List<Servizio> findByAllCompanies() {
        return findByAllCompanies(null);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Senza filtri.
     *
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    @SuppressWarnings("unchecked")
    public static List<Servizio> findByAllCompanies(EntityManager manager) {
        return (List<Servizio>) AQuery.findAll(Servizio.class, manager);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente.
     *
     * @return lista di tutte le entities
     */
    public static List<Servizio> findByCurrentCompany() {
        return findByCurrentCompany(null);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente.
     *
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    public static List<Servizio> findByCurrentCompany(EntityManager manager) {
        return findBySingleCompany(WamCompany.getCurrent(), manager);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company passata come parametro.
     * Se si arriva qui con una company null, vuol dire che non esiste la company corrente
     *
     * @param company di appartenenza (property della superclasse)
     * @return lista di tutte le entities
     */
    public static List<Servizio> findBySingleCompany(WamCompany company) {
        return findBySingleCompany(company, null);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company passata come parametro.
     * Se si arriva qui con una company null, vuol dire che non esiste la company corrente
     *
     * @param company di appartenenza (property della superclasse)
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    @SuppressWarnings("unchecked")
    public static List<Servizio> findBySingleCompany(WamCompany company, EntityManager manager) {
        if (company != null) {
            return (List<Servizio>) AQuery.findAll(Servizio.class, CompanyEntity_.company, company, manager);
        } else {
            return new ArrayList<>();
        }// end of if/else cycle
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // New and save
    //------------------------------------------------------------------------------------------------------------------------


    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @return istanza della Entity
     */
    public static Servizio crea(WamCompany company, String sigla, String descrizione) {
        return crea(company, sigla, descrizione, (EntityManager) null);
    }// end of static method


    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param manager     the EntityManager to use
     * @return istanza della Entity
     */
    public static Servizio crea(WamCompany company, String sigla, String descrizione, EntityManager manager) {
        return crea(company, sigla, descrizione, 0, 0, false, 0, 0, manager);
    }// end of static method


    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param colore      del gruppo (facoltativo)
     * @param orario      servizio ad orario prefissato e fisso ogni giorno (facoltativo)
     * @param oraInizio   del servizio (facoltativo, obbligatorio se orario è true)
     * @param oraFine     del servizio (facoltativo, obbligatorio se orario è true)
     * @return istanza della Entity
     */
    public static Servizio crea(WamCompany company, String sigla, String descrizione, int ordine, int colore, boolean orario, int oraInizio, int oraFine) {
        return crea(company, sigla, descrizione, ordine, colore, orario, oraInizio, oraFine, (EntityManager) null);
    }// end of static method


    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param colore      del gruppo (facoltativo)
     * @param orario      servizio ad orario prefissato e fisso ogni giorno (facoltativo)
     * @param oraInizio   del servizio (facoltativo, obbligatorio se orario è true)
     * @param oraFine     del servizio (facoltativo, obbligatorio se orario è true)
     * @param manager     the EntityManager to use
     * @return istanza della Entity
     */
    public static Servizio crea(WamCompany company, String sigla, String descrizione, int ordine, int colore, boolean orario, int oraInizio, int oraFine, EntityManager manager) {
        return crea(company, sigla, descrizione, ordine, colore, orario, oraInizio, oraFine, manager, (List<Funzione>) null);
    }// end of static method


    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param colore      del gruppo (facoltativo)
     * @param orario      servizio ad orario prefissato e fisso ogni giorno (facoltativo)
     * @param oraInizio   del servizio (facoltativo, obbligatorio se orario è true)
     * @param oraFine     del servizio (facoltativo, obbligatorio se orario è true)
     * @param manager     the EntityManager to use
     * @param funzioni    lista delle funzioni (facoltativa)
     * @return istanza della Entity
     */
    public static Servizio crea(WamCompany company, String sigla, String descrizione, int ordine, int colore, boolean orario, int oraInizio, int oraFine, EntityManager manager, List<Funzione> funzioni) {
        if (funzioni != null) {
            return crea(company, sigla, descrizione, ordine, colore, orario, oraInizio, oraFine, manager, funzioni.toArray(new Funzione[funzioni.size()]));
        } else {
            return crea(company, sigla, descrizione, ordine, colore, orario, oraInizio, oraFine, manager, (Funzione) null);
        }// end of if/else cycle
    }// end of static method

    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param colore      del gruppo (facoltativo)
     * @param orario      servizio ad orario prefissato e fisso ogni giorno (facoltativo)
     * @param oraInizio   del servizio (facoltativo, obbligatorio se orario è true)
     * @param oraFine     del servizio (facoltativo, obbligatorio se orario è true)
     * @param manager     the EntityManager to use
     * @param funzioni    lista delle funzioni (facoltativa)
     * @return istanza della Entity
     */
    public static Servizio crea(WamCompany company, String sigla, String descrizione, int ordine, int colore, boolean orario, int oraInizio, int oraFine, EntityManager manager, Funzione... funzioni) {
        Servizio servizio = Servizio.findByCompanyAndBySigla(company, sigla, manager);

        if (servizio == null) {
            servizio = new Servizio(company, sigla, descrizione, ordine, colore, orario, oraInizio, oraFine);

            if (funzioni != null) {
                for (int k = 0; k < funzioni.length; k++) {
//                    servizio.servizioFunzioni.add(new ServizioFunzione(company, servizio, funzioni[k], k < obbligatori));
                }// end of for cycle
            }// fine del blocco if

            servizio.save(company, manager);
        }// end of if cycle

        return servizio;
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // Delete
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Delete all the records for the domain class
     * Bulk delete records with CriteriaDelete
     */
    public static void deleteAll() {
        EntityManager manager = EM.createEntityManager();
        deleteAll(manager);
        manager.close();
    }// end of static method

    /**
     * Delete all the records for the domain class
     * Bulk delete records with CriteriaDelete
     *
     * @param manager the EntityManager to use
     */
    public static void deleteAll(EntityManager manager) {
        AQuery.deleteAll(Servizio.class, manager);
    }// end of static method


    /**
     * Recupera una istanza di Servizio usando la query di tutte e sole le property obbligatorie
     *
     * @param company selezionata
     * @param sigla   valore della property Sigla
     * @return istanza di Servizio, null se non trovata
     * @deprecated
     */
    @SuppressWarnings("unchecked")
    public static Servizio find(WamCompany company, String sigla) {
        Servizio instance = null;

        List<Servizio> serviziPerSigla = (List<Servizio>) AQuery.queryList(Servizio.class, Servizio_.sigla, sigla);
        if (serviziPerSigla != null && serviziPerSigla.size() > 0) {
            for (Servizio servizio : serviziPerSigla) {
                if (servizio.getCompany() != null && servizio.getCompany().equals(company)) {
                    instance = servizio;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return instance;
    }// end of method


    //------------------------------------------------------------------------------------------------------------------------
    // Getter and setter
    //------------------------------------------------------------------------------------------------------------------------
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }// end of getter method

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

    public String getCodeCompanyUnico() {
        return codeCompanyUnico;
    }// end of getter method

    public void setCodeCompanyUnico(String codeCompanyUnico) {
        this.codeCompanyUnico = codeCompanyUnico;
    }//end of setter method

    public String getDescrizione() {
        return descrizione;
    }// end of getter method

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }//end of setter method

    public int getOrdine() {
        return ordine;
    }// end of getter method

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }//end of setter method

    public int getColore() {
        return colore;
    }// end of getter method

    public void setColore(int colore) {
        this.colore = colore;
    }//end of setter method

    public boolean isOrario() {
        return orario;
    }// end of getter method

    public void setOrario(boolean orario) {
        this.orario = orario;
    }//end of setter method

    public int getOraInizio() {
        return oraInizio;
    }// end of getter method

    public void setOraInizio(int oraInizio) {
        this.oraInizio = oraInizio;
    }//end of setter method

    public int getMinutiInizio() {
        return minutiInizio;
    }// end of getter method

    public void setMinutiInizio(int minutiInizio) {
        this.minutiInizio = minutiInizio;
    }//end of setter method

    public int getOraFine() {
        return oraFine;
    }// end of getter method

    public void setOraFine(int oraFine) {
        this.oraFine = oraFine;
    }//end of setter method

    public int getMinutiFine() {
        return minutiFine;
    }// end of getter method

    public void setMinutiFine(int minutiFine) {
        this.minutiFine = minutiFine;
    }//end of setter method

    public List<Turno> getTurni() {
        return turni;
    }// end of getter method

    public void setTurni(List<Turno> turni) {
        this.turni = turni;
    }//end of setter method


    //------------------------------------------------------------------------------------------------------------------------
    // Save
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Saves this entity to the database using a local EntityManager
     * <p>
     *
     * @return the merged Entity (new entity, unmanaged, has the id)
     */
    @Override
    public BaseEntity save() {
        return this.save(null);
    }// end of method

    /**
     * Saves this entity to the database using a local EntityManager
     * <p>
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
     * @param company azienda da filtrare
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return the merged Entity (new entity, unmanaged, has the id)
     */
    public Servizio save(WamCompany company, EntityManager manager) {
        boolean valido;

        valido = super.checkCompany();
        if (valido) {
            valido = this.checkSigla();
        }// end of if cycle
        if (valido) {
            valido = this.checkDescrizione();
        }// end of if cycle
        if (valido) {
            valido = this.checkChiave(company);
        }// end of if cycle
        if (valido) {
            this.checkOrdine(company, manager);
        }// end of if cycle

        if (valido) {
            return (Servizio) super.save(manager);
        } else {
            return null;
        }// end of if/else cycle

    }// end of method


    //------------------------------------------------------------------------------------------------------------------------
    // Utilities
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Implementa come business logic, la obbligatorietà della sigla
     * <p>
     *
     * @return true se esiste, false se non esiste
     */
    private boolean checkSigla() {
        String caption = "La funzione non può essere accettata, perché manca la sigla che è obbligatoria";

        if (getSigla() != null && !getSigla().equals("")) {
            return true;
        } else {
            Notification.show(caption, Notification.Type.WARNING_MESSAGE);
            return false;
        }// end of if/else cycle
    } // end of method

    /**
     * Implementa come business logic, la obbligatorietà della descrizione
     * <p>
     *
     * @return true se esiste, false se non esiste
     */
    private boolean checkDescrizione() {
        String caption = "La funzione non può essere accettata, perché manca la descrizione che è obbligatoria";

        if (getDescrizione() != null && !getDescrizione().equals("")) {
            return true;
        } else {
            Notification.show(caption, Notification.Type.WARNING_MESSAGE);
            return false;
        }// end of if/else cycle
    } // end of method

    /**
     * Controlla l'esistenza della chiave univoca, PRIMA di salvare il valore nel DB
     * La crea se non esiste già
     *
     * @param company azienda da filtrare
     */
    private boolean checkChiave(WamCompany company) {
        boolean valido = false;

        if (getSigla() == null || getSigla().equals("")) {
            codeCompanyUnico = null;
        } else {
            if (company != null) {
                codeCompanyUnico = company.getCompanyCode();
            }// end of if cycle
            codeCompanyUnico += getSigla();
            valido = true;
        }// end of if/else cycle

        return valido;
    } // end of method

    /**
     * Appena prima di persistere sul DB
     * Elimino l'annotazione ed uso una chiamata dal metodo save(),
     * perché altrimenti non riuscirei a passare il parametro manager
     *
     * @param company azienda da filtrare
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     *                //@PrePersist
     */
    private void checkOrdine(WamCompany company, EntityManager manager) {
        if (getOrdine() == 0) {
            int max = WamQuery.maxOrdineServizio(company, manager);
            setOrdine(max + 1);
        }// end of if cycle
    }// end of method

    /**
     * Ritorna l'elenco delle funzioni previste per questo servizio
     *
     * @return le funzioni
     */
    public ArrayList<Funzione> getFunzioni() {
        ArrayList<Funzione> lista = new ArrayList<>();

        for (ServizioFunzione serFun : servizioFunzioni) {
            lista.add(serFun.getFunzione());
        }// end of for cycle

        return lista;
    }


//    @PrePersist
//    protected void prePersist() {
//        if (getOrdine() == 0) {
//            int max = WamQuery.queryMaxOrdineServizio(null);
//            setOrdine(max + 1);
//        }
//    }

    /**
     * Ritorna l'elenco delle funzioni obbligatorie previste per questo servizio
     *
     * @return le funzioni obbligatorie
     */
    public ArrayList<ServizioFunzione> getFunzioniObbligatorie() {
        ArrayList<ServizioFunzione> lista = new ArrayList<>();

        for (ServizioFunzione serFun : servizioFunzioni) {
            if (serFun.isObbligatoria()) {
                lista.add(serFun);
            }// end of if cycle
        }// end of for cycle

        return lista;
    }

    /**
     * Ritorna il numero di funzioni previste per questo servizio
     */
    public int getNumFunzioni() {
        return getFunzioni().size();
    }

    /**
     * Restituisce la posizione di una data funzione tra le funzioni previste per il turno.
     *
     * @param f la funzione
     * @return la posizione, -1 se non trovata
     */
    public int getPosFunzione(Funzione f) {
        int pos = -1;
        ArrayList<Funzione> funzioni = getFunzioni();
        for (int i = 0; i < funzioni.size(); i++) {
            Funzione currFun = funzioni.get(i);
            if (currFun.getSigla().equals(f.getSigla())) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    /**
     * Recupera il ServizioFunzione relativo a una data funzione
     *
     * @param f la funzione
     * @return il ServizioFunzione con la funzione, null se non trovato
     */
    public ServizioFunzione getServizioFunzione(Funzione f) {
        ServizioFunzione sfOut = null;
        for (ServizioFunzione sf : getServizioFunzioni()) {
            if (sf.getFunzione().equals(f)) {
                sfOut = sf;
                break;
            }

        }
        return sfOut;

    }

    /**
     * Aggiunge un ServizioFunzione a questo servizio.
     * Regola automaticamente il link al Servizio.
     */
    public void add(ServizioFunzione sf) {
        sf.setServizio(this);
        getServizioFunzioni().add(sf);
    }

    /**
     * Ritorna il tempo totale del servizio in minuti
     */
    public int getMinutiTotali() {
        int minutiTot;
        int minutiStart = getOraInizio() * 60 + getMinutiInizio();
        int minutiEnd = getOraFine() * 60 + getMinutiFine();
        int diff = minutiEnd - minutiStart;
        if (diff >= 0) {
            minutiTot = diff;
        } else {
            minutiTot = 1440 + diff;    // giorni diversi
        }
        return minutiTot;
    }

    /**
     * List NON garantisce l'ordinamento
     */
    public ArrayList<ServizioFunzione> getServizioFunzioni() {
        ArrayList<ServizioFunzione> listaOrdinata = new ArrayList<ServizioFunzione>();
        LinkedHashMap<Integer, ServizioFunzione> mappa = new LinkedHashMap<>();

        for (ServizioFunzione serFunz : servizioFunzioni) {
            mappa.put(serFunz.getFunzione().getOrdine(), serFunz);
        }// end of for cycle

        mappa = LibArray.ordinaMappa(mappa);
        for (Integer key : mappa.keySet()) {
            listaOrdinata.add(mappa.get(key));
        }// end of for cycle

        return listaOrdinata;
    }// end of getter method

    public void setServizioFunzioni(List<ServizioFunzione> servizioFunzioni) {
        this.servizioFunzioni = servizioFunzioni;
    }//end of setter method

    public void add(Funzione funzione) {
        add(funzione, false);
    }// end of method

    public void add(Funzione funzione, boolean obbligatoria) {
        ServizioFunzione serFun = null;

        if (getCompany() == null) {
            Exception e = new Exception("Impossibile aggiungere funzioni al servizio se manca la company");
            e.printStackTrace();
            return;
        }// end of if cycle

        if (servizioFunzioni != null) {
            serFun = new ServizioFunzione(this, funzione);
            serFun.setCompany(getCompany());
            serFun.setObbligatoria(obbligatoria);
            servizioFunzioni.add(serFun);
        }// end of if cycle
    }// end of method


    /**
     * Ritorna una stringa che rappresenta l'orario dalle... alle...
     */
    public String getStrOrario() {
        return strHM(oraInizio) + ":" + strHM(minutiInizio) + " - " + strHM(oraFine) + ":" + strHM(minutiFine);
    }

    /**
     * @return il numero di ore o minuti formattato su 2 caratteri fissi
     */
    private String strHM(int num) {
        String s = "" + num;
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Clone
    //------------------------------------------------------------------------------------------------------------------------
    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatoria invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza di Servizio con gli stessi valori dei parametri di questa istanza
     */
    @Override
    @SuppressWarnings("all")
    public Servizio clone() throws CloneNotSupportedException {
        try {
            return (Servizio) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of Entity class
