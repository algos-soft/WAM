package it.algos.wam.entity.servizio;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.colorpicker.Color;
import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione_;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.turno.Turno_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.AFType;
import it.algos.webbase.web.field.AIField;
import it.algos.webbase.web.lib.DateConvertUtils;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.query.SortProperty;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

/**
 * Entity che descrive un Servizio (tipo di turno)
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
public class Servizio extends WamCompanyEntity implements Comparable<Servizio> {

    //------------------------------------------------------------------------------------------------------------------------
    // Property
    //------------------------------------------------------------------------------------------------------------------------
    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    //--company di riferimento (facoltativa nella superclasse)
    //--company di riferimento (obbligatoria in questa classe)
    //--private BaseCompany company;

    //--sigla di codifica visibile (obbligatoria, non unica in generale ma unica all'interno della company)
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    @Column(length = 20)
    @Index
    @AIField(type = AFType.text, required = true, width = "8em", caption = "Sigla", prompt = "sigla visibile", help = "Sigla visibile nel tabellone.", error = "Manca la sigla di codifica")
    private String sigla = "";

    //--sigla di codifica interna (obbligatoria, unica in generale indipendentemente dalla company)
    //--calcolata -> codeCompanyUnico = company.companyCode + funzione.sigla (20+20=40);
    @NotEmpty
    @NotNull
    @Column(length = 40, unique = true)
    @Index
    @AIField(type = AFType.text, required = true, enabled = false, width = "18em", caption = "CodiceUnico", help = "Codifica interna. Valore unico. Calcola automaticamente")
    private String codeCompanyUnico;

    //--descrizione per il tabellone (obbligatoria, non unica)
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    @AIField(type = AFType.text, required = true, width = "26em", caption = "Descrizione", prompt = "descrizione completa", help = "Descrizione completa del servizio.", error = "Manca la descrizione")
    private String descrizione = "";

    //--ordine di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
    @NotNull
    @Index
    @AIField(type = AFType.integer, enabled = false, width = "3em", caption = "Ordine", help = "Ordine di apparizione nei PopUp. Modificabile nella lista con i bottoni Sposta su e giu")
    private int ordine = 0;

    // colore del servizio (facoltativo)
    private int colore = new Color(128, 128, 128).getRGB();

    //--orario predefinito (avis, centralino ed extra non ce l'hanno)
    @AIField(type = AFType.checkbox, width = "12em", caption = "Orario predefinito", help = "Servizio ad orario fisso")
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

    // visibile nel tabellone (di default true)
    @AIField(type = AFType.checkbox, width = "12em", caption = "Visibile nel tabellone", help = "Permette di modificare i servizi visibili nel tabellone")
    private boolean visibile = true;

    //--tavola di incrocio
    // CascadeType.ALL: quando chiamo persist sul padre, persiste automaticamente tutti i nuovi figli aggiunti
    // alla lista e non ancora registrati (e così per tutte le operazioni dell'EntityManager)
    // orphanRemoval = true: quando registro il padre, cancella tutti i figli eventualmente rimasti orfani.
    // CascadeOnDelete: instaura l'integrità referenziale a livello di database (foreign key on delete cascade)
    @OneToMany(mappedBy = "servizio", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<Turno> turni = new ArrayList<>();


    //--tavola di incrocio
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
     * Da non usare per la creazione diretta di una nuova istanza (si perdono i controlli)
     */
    public Servizio() {
        this("", "");
    }// end of JavaBean constructor


    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * L'ordine di presentazione nel tabellone viene inserito in automatico prima del persist
     *
     * @param sigla       di riferimento interna (obbligatoria, unica all'interno della company)
     * @param descrizione per il tabellone (obbligatoria)
     */
    public Servizio(String sigla, String descrizione) {
        this((WamCompany) null, sigla, descrizione);
    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Filtrato sulla company passata come parametro.
     * Se il valore della company è nullo, utilizza la company corrente, regolata nella superclasse
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * L'ordine di presentazione nel tabellone viene inserito in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       di riferimento interna (obbligatoria, unica all'interno della company)
     * @param descrizione per il tabellone (obbligatoria)
     */
    public Servizio(WamCompany company, String sigla, String descrizione) {
        this(company, sigla, descrizione, 0, 0);
    }// end of constructor


    /**
     * Costruttore ridotto
     * Filtrato sulla company passata come parametro.
     * Se il valore della company è nullo, utilizza la company corrente, regolata nella superclasse
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       di riferimento interna (obbligatoria, unica all'interno della company)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param colore      del gruppo (facoltativo)
     */
    public Servizio(WamCompany company, String sigla, String descrizione, int ordine, int colore) {
        this(company, sigla, descrizione, ordine, colore, false, 0, 0);
    }// end of constructor


    /**
     * Costruttore completo
     * Filtrato sulla company passata come parametro.
     * Se il valore della company è nullo, utilizza la company corrente, regolata nella superclasse
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       di riferimento interna (obbligatoria, unica all'interno della company)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param colore      del gruppo (facoltativo)
     * @param orario      servizio ad orario prefissato e fisso ogni giorno (facoltativo)
     * @param oraInizio   del servizio (facoltativo, obbligatorio se orario è true)
     * @param oraFine     del servizio (facoltativo, obbligatorio se orario è true)
     */
    public Servizio(WamCompany company, String sigla, String descrizione, int ordine, int colore, boolean orario, int oraInizio, int oraFine) {
        super();
        if (company != null) {
            this.setCompany(company);
        }// end of if cycle
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
        return AQuery.count(Servizio.class, manager);
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
     * Filtrato sulla company corrente.
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
        return CompanyQuery.count(Servizio.class, company, manager);
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
        return CompanyQuery.count(Servizio.class, attr, value, company, manager);
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
    public static Servizio find(long id, EntityManager manager) {
        return (Servizio) AQuery.find(Servizio.class, id, manager);
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
    public static Servizio getEntityByCodeCompanyUnico(String codeCompanyUnico) {
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
    public static Servizio getEntityByCodeCompanyUnico(String codeCompanyUnico, EntityManager manager) {
        return (Servizio) AQuery.getEntity(Servizio.class, Servizio_.codeCompanyUnico, codeCompanyUnico, manager);
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
     * @param sigla di riferimento interna (obbligatoria, unica all'interno della company)
     * @return istanza della Entity, null se non trovata
     */
    public static Servizio getEntityBySigla(String sigla) {
        return getEntityByCompanyAndSigla((WamCompany) null, sigla, null);
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param sigla   di riferimento interna (obbligatoria, unica all'interno della company)
     * @return istanza della Entity, null se non trovata
     */
    public static Servizio getEntityByCompanyAndSigla(WamCompany company, String sigla) {
        return getEntityByCompanyAndSigla(company, sigla, null);
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param sigla   di riferimento interna (obbligatoria, unica all'interno della company)
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Servizio getEntityByCompanyAndSigla(WamCompany company, String sigla, EntityManager manager) {
        return (Servizio) CompanyQuery.getEntity(Servizio.class, Servizio_.sigla, sigla, company, manager);
    }// end of static method

    /**
     * Controlla che esista una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param sigla   di riferimento interna (obbligatoria, unica all'interno della company)
     * @param manager the EntityManager to use
     * @return vero se esiste Entity, false se non trovata
     */
    public static boolean isEntityByCompanyAndSigla(WamCompany company, String sigla, EntityManager manager) {
        return getEntityByCompanyAndSigla(company, sigla, manager) != null;
    }// end of static method

    public static boolean isEntityBySigla(String sigla) {
        return getEntityBySigla(sigla) != null;
    }// end of static method

    public static boolean isNotEntityBySigla(String sigla) {
        return !isEntityBySigla(sigla);
    }// end of static method

    /**
     * Controlla che non esista una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param sigla   di riferimento interna (obbligatoria, unica all'interno della company)
     * @param manager the EntityManager to use
     * @return vero se esiste Entity, false se non trovata
     */
    public static boolean isNotEntityByCompanyAndSigla(WamCompany company, String sigla, EntityManager manager) {
        return !isEntityByCompanyAndSigla(company, sigla, manager);
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
    public static List<Servizio> getListByAllCompanies() {
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
    public static List<Servizio> getListByAllCompanies(EntityManager manager) {
        return (List<Servizio>) AQuery.getList(Servizio.class, manager);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     *
     * @return lista di tutte le entities
     */
    public static List<Servizio> getListByCurrentCompany() {
        return getListByCurrentCompany((EntityManager) null);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente
     *
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    public static List<Servizio> getListByCurrentCompany(EntityManager manager) {
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
    public static List<Servizio> getListByCompany(WamCompany company) {
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
    public static List<Servizio> getListByCompany(WamCompany company, EntityManager manager) {
        if (company != null) {
            return (List<Servizio>) AQuery.getList(Servizio.class, CompanyEntity_.company, company, manager);
        } else {
            return new ArrayList<>();
        }// end of if/else cycle
    }// end of static method

    public static ArrayList<Servizio> getListVisibiliConOrario(EntityManager manager) {
        return getListVisibiliConOrario(WamCompany.getCurrent(), manager);
    }// end of static method

    public static ArrayList<Servizio> getListVisibiliConOrario(WamCompany company, EntityManager manager) {
        return getListVisibili(company, manager, true);
    }// end of static method

    public static ArrayList<Servizio> getListVisibiliSenzaOrario(EntityManager manager) {
        return getListVisibiliSenzaOrario(WamCompany.getCurrent(), manager);
    }// end of static method

    public static ArrayList<Servizio> getListVisibiliSenzaOrario(WamCompany company, EntityManager manager) {
        return getListVisibili(company, manager, false);
    }// end of static method

    /**
     * Recupera una lista (array) di tutti i servizi visibili con e senza orario prestabilito
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
    public static ArrayList<Servizio> getListVisibili(WamCompany company, EntityManager manager, boolean orario) {
        SortProperty sort = new SortProperty(Servizio_.ordine);
        Container.Filter filtroVisibile = new Compare.Equal(Servizio_.visibile.getName(), true);
        Container.Filter filtroOrario = new Compare.Equal(Servizio_.orario.getName(), orario);

        return (ArrayList<Servizio>) CompanyQuery.getList(Servizio.class, sort, company, manager, filtroVisibile, filtroOrario);
    }// end of static method

    /**
     * Recupera una lista (array) di tutti i servizi visibili
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
    public static ArrayList<Servizio> getListVisibili(WamCompany company, EntityManager manager) {
        SortProperty sort = new SortProperty(Servizio_.ordine);
        Container.Filter filtroVisibile = new Compare.Equal(Servizio_.visibile.getName(), true);

        return (ArrayList<Servizio>) CompanyQuery.getList(Servizio.class, sort, company, manager, filtroVisibile);
    }// end of static method


    /**
     * Se il tabellone contiene giorni passati, recupera anche i servizi che hanno dei turni associati
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Servizio> getListPassati(EntityManager manager, LocalDate inizioPeriodo, LocalDate finePeriodo) {
        ArrayList<Servizio> lista;
        WamCompany company = WamCompany.getCurrent();

        Date dataInizioPeriodo = DateConvertUtils.asUtilDate(inizioPeriodo);
        Date dataFinePeriodo = DateConvertUtils.asUtilDate(finePeriodo);

        Container.Filter filtroCompany = new Compare.Equal(CompanyEntity_.company.getName(), company);
        Container.Filter filtroInizio = new Compare.GreaterOrEqual(Turno_.inizio.getName(), dataInizioPeriodo);
        Container.Filter filtroFine = new Compare.LessOrEqual(Turno_.inizio.getName(), dataFinePeriodo);

        lista = (ArrayList<Servizio>) AQuery.getListProperty(Turno.class, Turno_.servizio, manager, filtroCompany, filtroInizio, filtroFine);
        lista = LibArray.valoriUnici(lista);

        return lista;
    }// end of static method

    /**
     * La lista dei servizi deve essere composta:
     * Sempre, da tutti i servizi abilitati
     * Se il tabellone contiene giorni passati, aggiunge anche i servizi che hanno dei turni associati
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Servizio> getListVisibiliConOrarioAndPassati(EntityManager manager, LocalDate inizioPeriodo, LocalDate finePeriodo) {
        ArrayList<Servizio> lista;
        ArrayList<Servizio> listaServiziPrevisti;
        ArrayList<Servizio> listaServiziPassati;

        listaServiziPrevisti = Servizio.getListVisibiliConOrario(manager);
        listaServiziPassati = Servizio.getListPassati(manager, inizioPeriodo, finePeriodo);
        lista = LibArray.somma(listaServiziPrevisti, listaServiziPassati);
        lista = LibArray.valoriUnici(lista);
        lista = LibArray.sort(lista);

        return lista;
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i servizi relativi ad una funzione
     * Filtrato sulla company corrente
     *
     * @param funzione da utilizzare per filtrare i servizi
     * @return lista delle entities selezionate
     */
    public static List<Servizio> getListByFunzione(Funzione funzione) {
        List<Servizio> lista = new ArrayList<>();
        List<ServizioFunzione> servizioFunzioni = null;

        if (funzione != null) {
            servizioFunzioni = (List<ServizioFunzione>) CompanyQuery.getList(ServizioFunzione.class, ServizioFunzione_.funzione, funzione);
        }// end of if cycle

        if (servizioFunzioni != null) {
            for (ServizioFunzione servFunz : servizioFunzioni) {
                lista.add(servFunz.getServizio());
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Get properties (list)
    //------------------------------------------------------------------------------------------------------------------------

    public static List<String> getListStrForCodeCompanyUnico() {
        return getListStrForCodeCompanyUnico((EntityManager) null);
    }// end of static method

    /**
     * Search for the values of a given property of the given Entity class
     * Ordinate sul valore della property indicata
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param manager the EntityManager to use
     */
    public static List<String> getListStrForCodeCompanyUnico(EntityManager manager) {
        return CompanyQuery.getListStr(Servizio.class, Servizio_.codeCompanyUnico, null, manager);
    }// end of static method


    public static List<String> getListStrForSiglaByCompany() {
        return getListStrForSiglaByCompany(WamCompany.getCurrent());
    }// end of static method

    public static List<String> getListStrForSiglaByCompany(WamCompany company) {
        return getListStrForSiglaByCompany(company, (EntityManager) null);
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
    public static List<String> getListStrForSiglaByCompany(WamCompany company, EntityManager manager) {
        return CompanyQuery.getListStr(Servizio.class, Servizio_.sigla, company, manager);
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // New and save
    //------------------------------------------------------------------------------------------------------------------------


    public static Servizio crea(String sigla, String descrizione) {
        return crea(WamCompany.getCurrent(), sigla, descrizione);
    }// end of static method

    public static Servizio crea(WamCompany company, String sigla, String descrizione) {
        return crea(company, sigla, descrizione, (EntityManager) null);
    }// end of static method

    public static Servizio crea(WamCompany company, String sigla, String descrizione, EntityManager manager) {
        return crea(company, sigla, descrizione, 0, 0, false, 0, 0, manager);
    }// end of static method

    public static Servizio crea(WamCompany company, String sigla, String descrizione, int ordine, int colore, boolean orario, int oraInizio, int oraFine) {
        return crea(company, sigla, descrizione, ordine, colore, orario, oraInizio, oraFine, (EntityManager) null);
    }// end of static method

    public static Servizio crea(WamCompany company, String sigla, String descrizione, int ordine, int colore, boolean orario, int oraInizio, int oraFine, EntityManager manager) {
        return crea(company, sigla, descrizione, ordine, colore, orario, oraInizio, 0, oraFine, 0, manager, (ArrayList<Funzione>) null);
    }// end of static method


    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       di riferimento interna (obbligatoria, unica all'interno della company)
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
        if (funzioni != null) {
            return crea(company, sigla, descrizione, ordine, colore, orario, oraInizio, 0, oraFine, 0, manager, new ArrayList<>(Arrays.asList(funzioni)));
        } else {
            return crea(company, sigla, descrizione, ordine, colore, orario, oraInizio, 0, oraFine, 0, manager, (ArrayList<Funzione>) null);
        }// end of if/else cycle
    }// end of static method

    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company      di appartenenza (property della superclasse)
     * @param sigla        di riferimento interna (obbligatoria, unica all'interno della company)
     * @param descrizione  per il tabellone (obbligatoria)
     * @param ordine       di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param colore       del gruppo (facoltativo)
     * @param orario       servizio ad orario prefissato e fisso ogni giorno (facoltativo)
     * @param oraInizio    del servizio (facoltativo, obbligatorio se orario è true)
     * @param minutiInizio del servizio (facoltativo, obbligatorio se orario è true)
     * @param oraFine      del servizio (facoltativo, obbligatorio se orario è true)
     * @param minutiFine   del servizio (facoltativo, obbligatorio se orario è true)
     * @param manager      the EntityManager to use
     * @param funzioni     lista delle funzioni (facoltativa)
     * @return istanza della Entity
     */
    public static Servizio crea(
            WamCompany company,
            String sigla,
            String descrizione,
            int ordine,
            int colore,
            boolean orario,
            int oraInizio,
            int minutiInizio,
            int oraFine,
            int minutiFine,
            EntityManager manager,
            ArrayList<Funzione> funzioni) {
        List<ServizioFunzione> servizioFunzioni = new ArrayList<>();

        if (funzioni != null) {
            for (int k = 0; k < funzioni.size(); k++) {
                servizioFunzioni.add(new ServizioFunzione(company, null, funzioni.get(k)));
            }// end of for cycle
        }// fine del blocco if


        return crea(company, sigla, true, descrizione, ordine, colore, orario, oraInizio, minutiInizio, oraFine, minutiFine, manager, servizioFunzioni);
    }// end of static method

    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company          di appartenenza (property della superclasse)
     * @param sigla            di riferimento interna (obbligatoria, unica all'interno della company)
     * @param descrizione      per il tabellone (obbligatoria)
     * @param colore           del gruppo (facoltativo)
     * @param orario           servizio ad orario prefissato e fisso ogni giorno (facoltativo)
     * @param oraInizio        del servizio (facoltativo, obbligatorio se orario è true)
     * @param oraFine          del servizio (facoltativo, obbligatorio se orario è true)
     * @param manager          the EntityManager to use
     * @param servizioFunzioni lista delle funzioni (facoltativa)
     * @return istanza della Entity
     */
    public static Servizio crea(
            WamCompany company,
            String sigla,
            String descrizione,
            int colore,
            boolean orario,
            int oraInizio,
            int oraFine,
            EntityManager manager,
            List<ServizioFunzione> servizioFunzioni) {
        return crea(company, sigla, true, descrizione, 0, colore, orario, oraInizio, 0, oraFine, 0, manager, servizioFunzioni);
    }// end of static method

    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company          di appartenenza (property della superclasse)
     * @param sigla            di riferimento interna (obbligatoria, unica all'interno della company)
     * @param descrizione      per il tabellone (obbligatoria)
     * @param visibile         nel tabellone (di default true)
     * @param colore           del gruppo (facoltativo)
     * @param orario           servizio ad orario prefissato e fisso ogni giorno (facoltativo)
     * @param oraInizio        del servizio (facoltativo, obbligatorio se orario è true)
     * @param oraFine          del servizio (facoltativo, obbligatorio se orario è true)
     * @param manager          the EntityManager to use
     * @param servizioFunzioni lista delle funzioni (facoltativa)
     * @return istanza della Entity
     */
    public static Servizio crea(
            WamCompany company,
            String sigla,
            boolean visibile,
            String descrizione,
            int colore,
            boolean orario,
            int oraInizio,
            int oraFine,
            EntityManager manager,
            List<ServizioFunzione> servizioFunzioni) {
        return crea(company, sigla, visibile, descrizione, 0, colore, orario, oraInizio, 0, oraFine, 0, manager, servizioFunzioni);
    }// end of static method


    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company          di appartenenza (property della superclasse)
     * @param sigla            di riferimento interna (obbligatoria, unica all'interno della company)
     * @param visibile         nel tabellone (di default true)
     * @param descrizione      per il tabellone (obbligatoria)
     * @param ordine           di presentazione nel tabellone (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param colore           del gruppo (facoltativo)
     * @param orario           servizio ad orario prefissato e fisso ogni giorno (facoltativo)
     * @param oraInizio        del servizio (facoltativo, obbligatorio se orario è true)
     * @param minutiInizio     del servizio (facoltativo, obbligatorio se orario è true)
     * @param oraFine          del servizio (facoltativo, obbligatorio se orario è true)
     * @param minutiFine       del servizio (facoltativo, obbligatorio se orario è true)
     * @param manager          the EntityManager to use
     * @param servizioFunzioni lista delle funzioni (facoltativa)
     * @return istanza della Entity
     */
    public static Servizio crea(
            WamCompany company,
            String sigla,
            boolean visibile,
            String descrizione,
            int ordine,
            int colore,
            boolean orario,
            int oraInizio,
            int minutiInizio,
            int oraFine,
            int minutiFine,
            EntityManager manager,
            List<ServizioFunzione> servizioFunzioni) {
        Servizio servizio = null;
        ServizioFunzione servFunz;

        if (isNotEntityByCompanyAndSigla(company, sigla, manager)) {
            try { // prova ad eseguire il codice
                servizio = new Servizio(company, sigla, descrizione, ordine, colore, orario, oraInizio, oraFine);
                servizio.setMinutiInizio(minutiInizio);
                servizio.setMinutiFine(minutiFine);
                servizio.setVisibile(visibile);

                if (servizioFunzioni != null) {
                    for (int k = 0; k < servizioFunzioni.size(); k++) {
                        servFunz = servizioFunzioni.get(k);
                        servFunz.setServizio(servizio);
                        servizio.servizioFunzioni.add(servFunz);
                    }// end of for cycle
                }// fine del blocco if

                servizio = servizio.save(company, manager);
            } catch (Exception unErrore) { // intercetta l'errore
                servizio = null;
            }// fine del blocco try-catch
        }// end of if cycle

        return servizio;
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Delete
    //------------------------------------------------------------------------------------------------------------------------

    public static int deleteAll() {
        return deleteAll(CompanySessionLib.getCompany(), (EntityManager) null);
    }// end of static method

    public static int deleteAll(BaseCompany company) {
        return deleteAll(company, (EntityManager) null);
    }// end of static method

    public static int deleteAll(EntityManager manager) {
        return deleteAll(CompanySessionLib.getCompany(), manager);
    }// end of static method

    /**
     * Delete all the records for the Entity class
     * Bulk delete records with CriteriaDelete
     *
     * @param manager the EntityManager to use
     */
    public static int deleteAll(BaseCompany company, EntityManager manager) {
        return CompanyQuery.delete(Servizio.class, company, manager);
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
        return CompanyQuery.maxInt(Servizio.class, Servizio_.ordine, company, manager);
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

    public boolean isVisibile() {
        return visibile;
    }// end of getter method

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
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
        return this.save((EntityManager) null);
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
     * @param company azinda da filtrare
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
            valido = this.checkChiave(company, manager);
        }// end of if cycle
        if (valido) {
            this.checkOrdine(company, manager);
        }// end of if cycle
        if (valido) {
            this.checkOrario();
        }// end of if cy

        if (valido) {
            return (Servizio) super.save(manager);
        } else {
            return null;
        }// end of if/else cycle

    }// end of method

    /**
     * Saves this entity to the database.
     * <p>
     * Usa l'EntityManager di default
     *
     * @return the merged Entity (new entity, unmanaged, has the id), casted as Funzione
     */
    public Servizio saveSafe() {
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
    public Servizio saveSafe(EntityManager manager) {
        return (Servizio) this.save(manager);
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
        boolean valido = true;

        codeCompanyUnico = LibText.creaChiave(getCompany(), sigla);
        if (codeCompanyUnico.equals("") || isEsistente(codeCompanyUnico, manager)) {
            valido = false;
        }// end of if cycle

        return valido;
    } // end of method

    public boolean isEsistente(String codeCompanyUnico, EntityManager manager) {
        return this.getId() == null && Servizio.isEntityByCodeCompanyUnico(codeCompanyUnico, manager);
    } // end of method

    /**
     * Appena prima di persistere sul DB
     * Elimino l'annotazione ed uso una chiamata dal metodo save(),
     * perché altrimenti non riuscirei a passare il parametro manager
     *
     * @param company da filtrare
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     *                //@PrePersist
     */
    private void checkOrdine(WamCompany company, EntityManager manager) {
        if (getOrdine() == 0) {
            setOrdine(maxOrdine(company, manager) + 1);
        }// end of if cycle
    }// end of method

    /**
     * Appena prima di persistere sul DB
     * Se l'orario non è predefinito, annulla i valori di ore e minuti
     */
    private void checkOrario() {
        if (!isOrario()) {
            setOraInizio(0);
            setOraFine(0);
            setMinutiInizio(0);
            setMinutiFine(0);
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
    }// end of method


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
            if (currFun.getCode().equals(f.getCode())) {
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
        for (ServizioFunzione sf : getServizioFunzioniOrdine()) {
            if (sf.getFunzione().equals(f)) {
                sfOut = sf;
                break;
            }

        }
        return sfOut;

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
    public ArrayList<ServizioFunzione> getServizioFunzioniOrdine() {
        ArrayList<ServizioFunzione> listaOrdinata = new ArrayList<>();
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

    /**
     * List NON garantisce l'ordinamento
     */
    public ArrayList<ServizioFunzione> getServizioFunzioniOrd() {
        ArrayList<ServizioFunzione> listaOrdinata = new ArrayList<>();
        LinkedHashMap<Integer, ServizioFunzione> mappa = new LinkedHashMap<>();

        for (ServizioFunzione serFunz : servizioFunzioni) {
            mappa.put(serFunz.getOrdine(), serFunz);
        }// end of for cycle

        mappa = LibArray.ordinaMappa(mappa);
        for (Integer key : mappa.keySet()) {
            listaOrdinata.add(mappa.get(key));
        }// end of for cycle

        return listaOrdinata;
    }// end of getter method

    public List<ServizioFunzione> getServizioFunzioni() {
        return servizioFunzioni;
    }// end of getter method

    public void setServizioFunzioni(List<ServizioFunzione> servizioFunzioni) {
        this.servizioFunzioni = servizioFunzioni;
    }//end of setter method

    public void add(Funzione funzione) {
        add(funzione, false);
    }// end of method


    /**
     * Aggiunge un ServizioFunzione a questo servizio.
     * Regola automaticamente il link al Servizio.
     */
    public void add(ServizioFunzione sf) {
        sf.setServizio(this);
        servizioFunzioni.add(sf);
    }


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

    /**
     * Compara per ordine del servizio
     *
     * @param other servizio
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    @Override
    @SuppressWarnings("all")
    public int compareTo(Servizio other) {
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
