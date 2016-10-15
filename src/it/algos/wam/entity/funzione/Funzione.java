package it.algos.wam.entity.funzione;

import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity che descrive una Funzione
 * Estende la Entity astratta WamCompany che contiene la property wamcompany
 * La property wamcompany può essere nulla nella superclasse, ma NON in questa classe dove è obbligatoria
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
public class Funzione extends WamCompanyEntity implements Comparable<Funzione> {

    //------------------------------------------------------------------------------------------------------------------------
    // Properties
    //------------------------------------------------------------------------------------------------------------------------
    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    //--company di riferimento (facoltativa nella superclasse)
    //--company di riferimento (obbligatoria in questa classe)
    //--private BaseCompany company;

    //--sigla di codifica interna specifica per ogni company (obbligatoria, non unica in generale ma unica all'interno della company)
    //--visibile solo per admin e developer
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    @Column(length = 20)
    @Index
    private String code = "";

    //--sigla di codifica interna (obbligatoria, unica in generale indipendentemente dalla company)
    //--calcolata -> codeCompanyUnico = company.companyCode + funzione.code (20+20=40);
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    @NotNull
    @Column(length = 40, unique = true)
    @Index
    private String codeCompanyUnico = "";

    //--sigla di codifica visibile (obbligatoria, non unica)
    //--visibile nel tabellone
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    @Column(length = 20)
    @Index
    private String sigla = "";

    //--descrizione (obbligatoria, non unica)
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    private String descrizione = "";

    //--ordine di presentazione nelle liste (obbligatorio, con controllo automatico prima del persist se è zero)
    @NotNull
    @Index
    private int ordine = 0;

    //--codepoint dell'icona di FontAwesome (facoltativa)
    private int iconCodepoint;

    //--tavola di incrocio
    // CascadeType.ALL: quando chiamo persist sul padre, persiste automaticamente tutti i nuovi figli aggiunti
    // alla lista e non ancora registrati (e così per tutte le operazioni dell'EntityManager)
    // orphanRemoval = true: quando registro il padre, cancella tutti i figli eventualmente rimasti orfani.
    // CascadeOnDelete: instaura l'integrità referenziale a livello di database (foreign key on delete cascade)
    @OneToMany(mappedBy = "funzione", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<ServizioFunzione> servizioFunzioni = new ArrayList<>();

    //--tavola di incrocio
    // CascadeType.ALL: quando chiamo persist sul padre, persiste automaticamente tutti i nuovi figli aggiunti
    // alla lista e non ancora registrati (e così per tutte le operazioni dell'EntityManager)
    // orphanRemoval = true: quando registro il padre, cancella tutti i figli eventualmente rimasti orfani.
    // CascadeOnDelete: instaura l'integrità referenziale a livello di database (foreign key on delete cascade)
    @OneToMany(mappedBy = "funzione", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<VolontarioFunzione> volontarioFunzioni = new ArrayList<>();

//    @OneToMany(mappedBy = "funzione")
//    @CascadeOnDelete
//    private List<Funzione> funzioneFunzioni = new ArrayList<>();
//
//    @ManyToOne
//    private Funzione funzione = null;

    //------------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Costruttore senza argomenti
     * Obbligatorio per le specifiche JavaBean
     * Da non usare MAI per la creazione diretta di una nuova istanza (si perdono i controlli)
     */
    public Funzione() {
    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * Se manca l'ordine di presentazione o è uguale a zero, viene calcolato in automatico prima del persist
     *
     * @param code        sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @param sigla       visibile nel tabellone (obbligatoria, non unica)
     * @param descrizione (obbligatoria)
     */
    public Funzione(String code, String sigla, String descrizione) {
        this((WamCompany) null, code, sigla, descrizione);
    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Filtrato sulla company passata come parametro.
     * Se il valore della company è nullo, utilizza la company corrente, regolata nella superclasse
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * Se manca l'ordine di presentazione o è uguale a zero, viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param code        sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @param sigla       visibile nel tabellone (obbligatoria, non unica)
     * @param descrizione (obbligatoria)
     */
    public Funzione(WamCompany company, String code, String sigla, String descrizione) {
        this(company, code, sigla, descrizione, 0, null);
    }// end of constructor

    /**
     * Costruttore completo
     * Filtrato sulla company passata come parametro.
     * Se il valore della company è nullo, utilizza la company corrente, regolata nella superclasse
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param code        sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @param sigla       visibile nel tabellone (obbligatoria, non unica)
     * @param descrizione (obbligatoria)
     * @param ordine      di presentazione nelle liste (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param glyph       icona di FontAwesome (facoltative)
     */
    public Funzione(WamCompany company, String code, String sigla, String descrizione, int ordine, FontAwesome glyph) {
        super();
        if (company != null) {
            this.setCompany(company);
        }// end of if cycle
        this.setCode(code);
        this.setSigla(sigla);
        this.setDescrizione(descrizione);
        this.setOrdine(ordine);
        this.setIcon(glyph);
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
        return AQuery.count(Funzione.class, manager);
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
        return CompanyQuery.count(Funzione.class, company, manager);
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
        return CompanyQuery.count(Funzione.class, attr, value, company, manager);
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
    public static Funzione find(long id) {
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
    public static Funzione find(long id, EntityManager manager) {
        return (Funzione) AQuery.find(Funzione.class, id, manager);
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // Get single entity by SingularAttribute
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Nessun filtro sulla company, perché la property è unica
     *
     * @param codeCompanyUnico sigla di codifica interna (obbligatoria, unica in generale indipendentemente dalla company)
     * @return istanza della Entity, null se non trovata
     */
    public static Funzione getEntityByCodeCompanyUnico(String codeCompanyUnico) {
        return getEntityByCodeCompanyUnico(codeCompanyUnico, (EntityManager) null);
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Nessun filtro sulla company, perché la property è unica
     *
     * @param codeCompanyUnico sigla di codifica interna (obbligatoria, unica in generale indipendentemente dalla company)
     * @return vero se esiste Entity, false se non trovata
     */
    public static boolean isEntityByCodeCompanyUnico(String codeCompanyUnico) {
        return getEntityByCodeCompanyUnico(codeCompanyUnico) != null;
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Nessun filtro sulla company, perché la property è unica
     *
     * @param codeCompanyUnico sigla di codifica interna (obbligatoria, unica in generale indipendentemente dalla company)
     * @param manager          the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Funzione getEntityByCodeCompanyUnico(String codeCompanyUnico, EntityManager manager) {
        return (Funzione) AQuery.getEntity(Funzione.class, Funzione_.codeCompanyUnico, codeCompanyUnico, manager);
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Nessun filtro sulla company, perché la property è unica
     *
     * @param codeCompanyUnico sigla di codifica interna (obbligatoria, unica in generale indipendentemente dalla company)
     * @return vero se esiste Entity, false se non trovata
     */
    public static boolean isEntityByCodeCompanyUnico(String codeCompanyUnico, EntityManager manager) {
        return getEntityByCodeCompanyUnico(codeCompanyUnico, manager) != null;
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     *
     * @param code sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @return istanza della Entity, null se non trovata
     */
    public static Funzione getEntityByCode(String code) {
        return getEntityByCompanyAndCode((WamCompany) null, code, (EntityManager) null);
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param code    sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @return istanza della Entity, null se non trovata
     */
    public static Funzione getEntityByCompanyAndCode(WamCompany company, String code) {
        return getEntityByCompanyAndCode(company, code, (EntityManager) null);
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param code    sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Funzione getEntityByCompanyAndCode(WamCompany company, String code, EntityManager manager) {
        return (Funzione) CompanyQuery.getEntity(Funzione.class, Funzione_.code, code, company, manager);
    }// end of static method


    public static boolean isEntityByCode(String code) {
        return getEntityByCode(code) != null;
    }// end of static method

    public static boolean isNotEntityByCode(String code) {
        return !isEntityByCode(code);
    }// end of static method

    /**
     * Controlla che esista una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param code    sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @param manager the EntityManager to use
     * @return vero se esiste Entity, false se non trovata
     */
    public static boolean isEntityByCompanyAndCode(WamCompany company, String code, EntityManager manager) {
        return getEntityByCompanyAndCode(company, code, manager) != null;
    }// end of static method


    /**
     * Controlla che non esista una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param code    sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @param manager the EntityManager to use
     * @return vero se esiste Entity, false se non trovata
     */
    public static boolean isNotEntityByCompanyAndCode(WamCompany company, String code, EntityManager manager) {
        return !isEntityByCompanyAndCode(company, code, manager);
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
    public static List<Funzione> getListByAllCompanies() {
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
    public static List<Funzione> getListByAllCompanies(EntityManager manager) {
        return (List<Funzione>) AQuery.getList(Funzione.class, manager);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     *
     * @return lista di tutte le entities
     */
    public static List<Funzione> getListByCurrentCompany() {
        return getListByCurrentCompany((EntityManager) null);
    }// end of static method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     *
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    public static List<Funzione> getListByCurrentCompany(EntityManager manager) {
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
    public static List<Funzione> getListByCompany(WamCompany company) {
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
    public static List<Funzione> getListByCompany(WamCompany company, EntityManager manager) {
        if (company != null) {
            return (List<Funzione>) CompanyQuery.getList(Funzione.class, CompanyEntity_.company, company, manager);
        } else {
            return new ArrayList<>();
        }// end of if/else cycle
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Get properties (list)
    //------------------------------------------------------------------------------------------------------------------------

    public static List<String> getListStrForCodeCompanyUnico() {
        return getListStrForCodeCompanyUnico((EntityManager) null);
    }// end of static method

    public static List<String> getListStrForCodeCompanyUnico(EntityManager manager) {
        return CompanyQuery.getListStr(Funzione.class, Funzione_.codeCompanyUnico, null, manager);
    }// end of static method


    public static List<String> getListStrForCodeByCompany() {
        return getListStrForCodeByCompany();
    }// end of static method


    public static List<String> getListStrForCodeByCompany(WamCompany company) {
        return getListStrForCodeByCompany(company, (EntityManager) null);
    }// end of static method

    /**
     * Search for the values of a given property of the given Entity class
     * Filtrato sulla company passata come parametro.
     * Ordinate sul valore della property indicata
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param company di appartenenza (property della superclasse)
     * @param manager the EntityManager to use
     */
    public static List<String> getListStrForCodeByCompany(WamCompany company, EntityManager manager) {
        return CompanyQuery.getListStr(Funzione.class, Funzione_.code, company, manager);
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // New and save
    //------------------------------------------------------------------------------------------------------------------------

    public static Funzione crea(String code, String sigla, String descrizione) {
        return crea(WamCompany.getCurrent(), code, sigla, descrizione);
    }// end of static method

    public static Funzione crea(WamCompany company, String code, String sigla, String descrizione) {
        return crea(company, code, sigla, descrizione, (EntityManager) null);
    }// end of static method

    public static Funzione crea(WamCompany company, String code, String sigla, String descrizione, EntityManager manager) {
        return crea(company, code, sigla, descrizione, 0, (FontAwesome) null, manager);
    }// end of static method

    public static Funzione crea(WamCompany company, String code, String sigla, String descrizione, int ordine, FontAwesome glyph) {
        return crea(company, code, sigla, descrizione, ordine, glyph, (EntityManager) null);
    }// end of static method

    public static Funzione crea(WamCompany company, String code, String sigla, String descrizione, FontAwesome glyph, EntityManager manager) {
        return crea(company, code, sigla, descrizione, 0, glyph, manager);
    }// end of static method


    /**
     * Creazione iniziale di una istanza della Entity
     * Filtrato sulla company passata come parametro.
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param code        sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @param sigla       visibile nel tabellone (obbligatoria, non unica)
     * @param descrizione (obbligatoria)
     * @param ordine      di presentazione nelle liste (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param glyph       dell'icona (facoltativo)
     * @param manager     the EntityManager to use
     * @return istanza della Entity
     */
    public static Funzione crea(
            WamCompany company,
            String code,
            String sigla,
            String descrizione,
            int ordine,
            FontAwesome glyph,
            EntityManager manager) {
        Funzione funzione = null;

        if (isNotEntityByCompanyAndCode(company, code, manager)) {
            try { // prova ad eseguire il codice
                funzione = new Funzione(company, code, sigla, descrizione, ordine, glyph);
                funzione = funzione.save(company, manager);
            } catch (Exception unErrore) { // intercetta l'errore
                funzione = null;
            }// fine del blocco try-catch
        }// end of if cycle

        return funzione;
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
        return CompanyQuery.delete(Funzione.class, company, manager);
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // utilities
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Numero massimo conenuto nella property
     *
     * @param company da filtrare
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return massimo valore
     */
    public static int maxOrdine(WamCompany company, EntityManager manager) {
        return CompanyQuery.maxInt(Funzione.class, Funzione_.ordine, company, manager);
    }// end of method


    //------------------------------------------------------------------------------------------------------------------------
    // Getter and setter
    //------------------------------------------------------------------------------------------------------------------------
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }// end of getter method

    @Override
    public String toString() {
        return sigla + " - " + descrizione;
    }// end of method

    public String getCode() {
        return code;
    }// end of getter method

    public void setCode(String code) {
        this.code = code;
    }//end of setter method

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

    public int getIconCodepoint() {
        return iconCodepoint;
    }// end of getter method

    public void setIconCodepoint(int iconCodepoint) {
        this.iconCodepoint = iconCodepoint;
    }//end of setter method

    public List<ServizioFunzione> getServizioFunzioni() {
        return servizioFunzioni;
    }// end of getter method

    public void setServizioFunzioni(List<ServizioFunzione> servizioFunzioni) {
        this.servizioFunzioni = servizioFunzioni;
    }//end of setter method

    public List<VolontarioFunzione> getVolontarioFunzioni() {
        return volontarioFunzioni;
    }// end of getter method

    public void setVolontarioFunzioni(List<VolontarioFunzione> volontarioFunzioni) {
        this.volontarioFunzioni = volontarioFunzioni;
    }//end of setter method


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
    public Funzione save(WamCompany company, EntityManager manager) {
        boolean valido;

        valido = super.checkCompany();
        if (valido) {
            valido = this.checkCode();
        }// end of if cycle
        if (valido) {
            valido = this.checkSigla();
        }// end of if cycle
        if (valido) {
            valido = this.checkDescrizione();
        }// end of if cycle
        if (valido) {
            valido = this.checkChiave(company, manager);
        }// end of if cycle
        if (valido) {
            this.checkOrdine(company, manager);
        }// end of if cycle

        if (valido) {
            return (Funzione) super.save(manager);
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
    public Funzione saveSafe() {
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
    public Funzione saveSafe(EntityManager manager) {
        return (Funzione) this.save(manager);
    }// end of method

    //------------------------------------------------------------------------------------------------------------------------
    // Utilities
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Implementa come business logic, la obbligatorietà del code
     * <p>
     *
     * @return true se esiste, false se non esiste
     */
    private boolean checkCode() {
        String caption = "La funzione non può essere accettata, perché manca il codice che è obbligatorio";

        if (getCode() != null && !getCode().equals("")) {
            return true;
        } else {
//            Notification.show(caption, Notification.Type.WARNING_MESSAGE);
            return false;
        }// end of if/else cycle
    } // end of method

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
//            Notification.show(caption, Notification.Type.WARNING_MESSAGE);
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
//            Notification.show(caption, Notification.Type.WARNING_MESSAGE);
            return false;
        }// end of if/else cycle
    } // end of method


    /**
     * Controlla l'esistenza della chiave univoca, PRIMA di salvare il valore nel DB
     * La crea se non esiste già
     *
     * @param company da filtrare
     */
    private boolean checkChiave(WamCompany company, EntityManager manager) {
        boolean valido = false;

        if (getCode() == null || getCode().equals("")) {
            codeCompanyUnico = null;
        } else {
            if (company != null) {
                codeCompanyUnico = company.getCompanyCode().toLowerCase();
            }// end of if cycle
            codeCompanyUnico += getCode().toLowerCase();
            valido = true;
        }// end of if/else cycle

        if (isEsistente(codeCompanyUnico, manager)) {
            valido = false;
        }// end of if cycle

        return valido;
    } // end of method

    public boolean isEsistente(String codeCompanyUnico, EntityManager manager) {
        return this.getId() == null && Funzione.isEntityByCodeCompanyUnico(codeCompanyUnico, manager);
    } // end of method

    /**
     * Appena prima di persistere sul DB
     * Elimino l'annotazione ed uso una chiamata dal metodo save(),
     * perché altrimenti non riuscirei a passare il parametro manager
     *
     * @param company da filtrare
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     */
    private void checkOrdine(WamCompany company, EntityManager manager) {
        if (getOrdine() == 0) {
            setOrdine(maxOrdine(company, manager) + 1);
        }// end of if cycle
    }// end of method

    /**
     * Recupera l'icona
     *
     * @return l'icona
     */
    public FontAwesome getIcon() {
        FontAwesome glyph = null;
        int codepoint = getIconCodepoint();

        try { // prova ad eseguire il codice
            glyph = FontAwesome.fromCodepoint(codepoint);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return glyph;
    }// end of method

    /**
     * Assegna una icona
     *
     * @param glyph l'icona FontAwesome
     */
    public void setIcon(FontAwesome glyph) {
        int codepoint = 0;
        if (glyph != null) {
            codepoint = glyph.getCodepoint();
        }// end of if cycle

        setIconCodepoint(codepoint);
    }// end of method


    /**
     * Recupera l'icona in formato htlm
     *
     * @return l'icona
     */
    public String getIconHtml() {
        String iconHtml = "";
        int codepoint = this.getIconCodepoint();
        FontAwesome glyph = null;
        try {
            glyph = FontAwesome.fromCodepoint(codepoint);
        } catch (Exception e) {
        }// fine del blocco try-catch
        if (glyph != null) {
            iconHtml = glyph.getHtml();
        }// end of if cycle

        return iconHtml;
    }// end of method

    /**
     * Compara per ordine della funzione
     *
     * @param other funzione
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    @Override
    @SuppressWarnings("all")
    public int compareTo(Funzione other) {
        Integer ordQuesto = getOrdine();
        Integer ordAltro = other.getOrdine();
        return ordQuesto.compareTo(ordAltro);
    }// end of method

    //------------------------------------------------------------------------------------------------------------------------
    // Clone
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatorio invocare questo metodo all'interno di un codice try/catch
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


}// end of Entity class
