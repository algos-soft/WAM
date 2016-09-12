package it.algos.wam.entity.volontario;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibCrypto;
import it.algos.webbase.web.login.UserIF;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity che descrive un Volontario
 * Estende la Entity astratta WamCompany che contiene la property wamcompany
 * <p>
 * Classe di tipo JavaBean
 * <p>
 * 1) la classe deve avere un costruttore senza argomenti
 * <p>
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 * <p>
 */
@Entity
public class Volontario extends WamCompanyEntity implements UserIF {

    //------------------------------------------------------------------------------------------------------------------------
    // Property
    //------------------------------------------------------------------------------------------------------------------------
    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    //--nome del volontario (obbligatorio, non unico)
    @NotEmpty
    @Column(length = 30)
    @Index
    private String nome = "";

    //--cognome del volontario (obbligatorio, non unico)
    @NotEmpty
    @Column(length = 30)
    @Index
    private String cognome = "";

    //--sigla di codifica interna specifica per company (obbligatoria, unica)
    //--calcolata -> codeCompanyUnico = company.companyCode + volontario.cognome + volontario.nome (20+30+30=80);
    @NotEmpty
    @NotNull
    @Column(length = 80, unique = true)
    @Index
    private String codeCompanyUnico;

    //--dati personali facoltativi
    private String cellulare = "";
    private String telefono = "";
    private String email = "";
    private String note = "";
    private Date dataNascita = null;

    //--dati dell'associazione
    private boolean dipendente = false;
    private boolean attivo = true;

    private String password;

    // se è admin
    private boolean admin;

    //--scadenza certificati
    //--data di scadenza del certificato BSD
    //--se non valorizzata, il milite non ha acquisito il certificato
//    private Date scadenzaBLSD = null;
//    //--data di scadenza del certificato Trauma
//    //--se non valorizzata, il milite non ha acquisito il certificato
//    private Date scadenzaTrauma = null;
//    //--data di scadenza del certificato Non Trauma
//    //--se non valorizzata, il milite non ha acquisito il certificato
//    private Date scadenzaNonTrauma = null;

    @OneToMany(mappedBy = "volontario", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<VolontarioFunzione> volontarioFunzioni = new ArrayList<>();


    //------------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Costruttore senza argomenti
     * Obbligatorio per le specifiche JavaBean
     */
    public Volontario() {
    }// end of constructor


    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     */
    public Volontario(WamCompany company, String nome, String cognome) {
        this(company, nome, cognome, null, "");
    }// end of constructor

    /**
     * Costruttore ridotto
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param nome        del volontario/milite (obbligatorio)
     * @param cognome     del volontario/milite (obbligatorio)
     * @param dataNascita del volontario/milite (facoltativo)
     * @param cellulare   del volontario/milite (facoltativo)
     */
    public Volontario(WamCompany company, String nome, String cognome, Date dataNascita, String cellulare) {
        this(company, nome, cognome, dataNascita, cellulare, false);
    }// end of constructor

    /**
     * Costruttore completo
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param nome        del volontario/milite (obbligatorio)
     * @param cognome     del volontario/milite (obbligatorio)
     * @param dataNascita del volontario/milite (facoltativo)
     * @param cellulare   del volontario/milite (facoltativo)
     * @param dipendente  dell'associazione NON volontario
     */
    public Volontario(WamCompany company, String nome, String cognome, Date dataNascita, String cellulare, boolean dipendente) {
        super();
        this.setCompany(company);
        setNome(nome);
        setCognome(cognome);
        setDataNascita(dataNascita);
        setCellulare(cellulare);
        setDipendente(dipendente);
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
        long totRec = AQuery.getCount(Volontario.class, manager);
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
        long totRec = CompanyQuery.getCount(Volontario.class, company, manager);
        return check(totRec);
    }// end of static method

    /**
     * Recupera il totale dei records della Entity
     * Filtrato sulla azienda corrente.
     *
     * @return numero totale di records nella Entity
     * @deprecated
     */
    public static int count() {
        return count((WamCompany) CompanySessionLib.getCompany());
    }// end of method

    /**
     * Recupera il totale dei records della Entity
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company croce di appartenenza
     * @return numero totale di records della tavola
     * @deprecated
     */
    public static int count(WamCompany company) {
        int totRec = 0;
        long totTmp = CompanyQuery.getCount(Volontario.class, company);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera il totale dei records della Entity
     * Senza filtri.
     *
     * @return numero totale di records nella Entity
     * @deprecated
     */
    public static int countAll() {
        return count((WamCompany) null);
    }// end of method

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
    public static Volontario find(long id) {
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
    public static Volontario find(long id, EntityManager manager) {
        BaseEntity entity = AQuery.find(Volontario.class, id, manager);
        return check(entity);
    }// end of static method

    /**
     * Controlla se l'istanza della Entity esiste ed è della classe corretta
     *
     * @param entity (BaseEntity) restituita dalla query generica
     * @return istanza della Entity specifica, null se non trovata
     */
    private static Volontario check(BaseEntity entity) {
        Volontario instance = null;

        if (entity != null && entity instanceof Volontario) {
            instance = (Volontario) entity;
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
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @return istanza della Entity, null se non trovata
     */
    public static Volontario findByNomeAndCognome(String nome, String cognome) {
        return findByNomeAndCognome(nome, cognome, null);
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla azienda corrente.
     *
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Volontario findByNomeAndCognome(String nome, String cognome, EntityManager manager) {
        return findByCompanyAndNomeAndCognome(WamCompany.getCurrent(), nome, cognome, manager);
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @return istanza della Entity, null se non trovata
     */
    public static Volontario findByCompanyAndNomeAndCognome(WamCompany company, String nome, String cognome) {
        return findByCompanyAndNomeAndCognome(company, nome, cognome, null);
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Volontario findByCompanyAndNomeAndCognome(WamCompany company, String nome, String cognome, EntityManager manager) {

        Container.Filter filterCompany = new Compare.Equal(Volontario_.company, company);
        Container.Filter filterNome = new Compare.Equal(Volontario_.nome, nome);
        Container.Filter filterCognome = new Compare.Equal(Volontario_.cognome, cognome);

        BaseEntity entity = AQuery.getEntity(Volontario.class, filterCompany, filterNome, filterCognome);
        return check(entity);
    }// end of static method

    /**
     * Recupera un volontario della company corrente per nick
     *
     * @param nick il nick (come ritornato da getNickname())
     * @return il volontario
     */
    public static Volontario queryByNick(String nick) {
        Volontario found = null;
        List<Volontario> list = (List<Volontario>) CompanyQuery.queryList(Volontario.class);
        for (Volontario v : list) {
            if (v.getNickname().equals(nick)) {
                found = v;
                break;

            }
        }
        return found;
    }


    //------------------------------------------------------------------------------------------------------------------------
    // Find entities (list)
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Senza filtri.
     *
     * @return lista di tutte le entities
     */
    public static List<Volontario> findByAllCompanies() {
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
    public static List<Volontario> findByAllCompanies(EntityManager manager) {
        return (List<Volontario>) AQuery.findAll(Funzione.class, manager);
    }// end of static method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente.
     *
     * @return lista di tutte le entities
     */
    public static List<Volontario> findByCurrentCompany() {
        return findByCurrentCompany(null);
    }// end of static method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente.
     *
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    public static List<Volontario> findByCurrentCompany(EntityManager manager) {
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
    public static List<Volontario> findBySingleCompany(WamCompany company) {
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
    public static List<Volontario> findBySingleCompany(WamCompany company, EntityManager manager) {
        if (company != null) {
            return (List<Volontario>) AQuery.findAll(Volontario.class, CompanyEntity_.company, company, manager);
        } else {
            return new ArrayList<>();
        }// end of if/else cycle
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla azienda corrente.
     *
     * @return lista di tutte le istanze di Funzione
     * @deprecated
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Volontario> findAll() {
        return (ArrayList<Volontario>) CompanyQuery.getList(Volontario.class);
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company croce di appartenenza
     * @return lista di tutte le istanze di Funzione
     * @deprecated
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Volontario> findAll(WamCompany company) {
        ArrayList<Volontario> lista = null;
        ArrayList<Volontario> listaTmp;

//        Container.Filter filter = new Compare.Equal(CompanyEntity_.company, company);
        listaTmp = (ArrayList<Volontario>) AQuery.getLista(Volontario.class);

        if (company == null) {
            return listaTmp;
        }// end of if cycle

        if (listaTmp != null && listaTmp.size() > 0) {
            lista = new ArrayList<>();
            for (Volontario vol : listaTmp) {
                if (vol.getCompany().getId() == company.getId()) {
                    lista.add(vol);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Senza filtri.
     *
     * @return lista di tutte le istanze di Funzione
     * @deprecated
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Volontario> findAllAll() {
        return (ArrayList<Volontario>) AQuery.getLista(Volontario.class);
    }// end of method

    /**
     * Recupera una istanza di Volontario usando la query di tutte e sole le property obbligatorie
     *
     * @param company valore della property Company
     * @param nome    valore della property Nome
     * @param cognome valore della property Cognome
     * @return istanza di Volontario, null se non trovata
     * @deprecated
     */
    @SuppressWarnings("unchecked")
    public static Volontario find(WamCompany company, String nome, String cognome) {
        Volontario instance = null;

        //@todo questo non funziona - si blocca
//        Container.Filter f1 = new Compare.Equal(Company_.companyCode.getName(), wamcompany.getCompanyCode());
//        Container.Filter f2 = new Compare.Equal(Milite_.nome.getName(), nome);
//        Container.Filter f3 = new Compare.Equal(Milite_.cognome.getName(), cognome);
//        Container.Filter filter = new And(f1, f2, f3);
//        ArrayList<BaseEntity> militi = AQuery.getList(Milite.class, filter);
//
//        if (militi != null && militi.size() > 0) {
//            instance = (Milite) militi.get(0);
//        }// end of if cycle

//        ArrayList<Volontario> militiPerCognome = (ArrayList<Milite>) AQuery.queryList(Volontario.class, Milite_.cognome, cognome);
        //@todo da migliorare
        List<Volontario> militiPerCognome = (List<Volontario>) AQuery.queryList(Volontario.class, Volontario_.cognome, cognome);
        if (militiPerCognome != null && militiPerCognome.size() > 0) {
            for (Volontario milite : militiPerCognome) {
                if (milite.getNome().equals(nome) && milite.getCompany().getId().equals(company.getId())) {
                    instance = milite;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return instance;
    }// end of method

    //------------------------------------------------------------------------------------------------------------------------
    // New and save
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Creazione iniziale di una istanza della Entity
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * La crea SOLO se non esiste già
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @return istanza della Entity
     */
    public static Volontario crea(WamCompany company, String nome, String cognome) {
        return crea(company, nome, cognome, (EntityManager) null);
    }// end of static method


    /**
     * Creazione iniziale di una istanza della Entity
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * La crea SOLO se non esiste già
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @param manager the EntityManager to use
     * @return istanza della Entity
     */
    public static Volontario crea(WamCompany company, String nome, String cognome, EntityManager manager) {
        return crea(company, nome, cognome, (Date) null, "", false, manager);
    }// end of static method

    /**
     * Creazione iniziale di una istanza della Entity
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param nome        del volontario/milite (obbligatorio)
     * @param cognome     del volontario/milite (obbligatorio)
     * @param dataNascita del volontario/milite (facoltativo)
     * @param cellulare   del volontario/milite (facoltativo)
     * @param dipendente  dell'associazione NON volontario
     * @return istanza della Entity
     */
    public static Volontario crea(WamCompany company, String nome, String cognome, Date dataNascita, String cellulare, boolean dipendente) {
        return crea(company, nome, cognome, dataNascita, cellulare, dipendente, (EntityManager) null);
    }// end of static method

    /**
     * Creazione iniziale di una istanza della Entity
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param nome        del volontario/milite (obbligatorio)
     * @param cognome     del volontario/milite (obbligatorio)
     * @param dataNascita del volontario/milite (facoltativo)
     * @param cellulare   del volontario/milite (facoltativo)
     * @param dipendente  dell'associazione NON volontario
     * @param manager     the EntityManager to use
     * @return istanza della Entity
     */
    public static Volontario crea(WamCompany company, String nome, String cognome, Date dataNascita, String cellulare, boolean dipendente, EntityManager manager) {
        Volontario volontario = Volontario.findByCompanyAndNomeAndCognome(company, nome, cognome, manager);

        if (volontario == null) {
            volontario = new Volontario(company, nome, cognome, dataNascita, cellulare, dipendente);
            volontario.save(company, manager);
        }// end of if cycle

        return volontario;
    }// end of static method


    /**
     * Creazione iniziale di un volontario
     * Lo crea SOLO se non esiste già
     *
     * @param company croce di appartenenza
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @return istanza di Volontario
     */
    public static Volontario crea(WamCompany company, String nome, String cognome, String email) {
        return crea(company, null, nome, cognome, email, new ArrayList<Funzione>());
    }// end of static method

    /**
     * Creazione iniziale di un volontario
     * Lo crea SOLO se non esiste già
     *
     * @param company croce di appartenenza
     * @param manager the EntityManager to use
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @return istanza di Volontario
     */
    public static Volontario crea(WamCompany company, EntityManager manager, String nome, String cognome, String email) {
        return crea(company, manager, nome, cognome, email, new ArrayList<Funzione>());
    }// end of static method

    /**
     * Creazione iniziale di un volontario
     * Lo crea SOLO se non esiste già
     *
     * @param company   croce di appartenenza
     * @param manager   the EntityManager to use
     * @param nome      del volontario/milite (obbligatorio)
     * @param cognome   del volontario/milite (obbligatorio)
     * @param listaFunz lista delle funzioni (facoltativa)
     * @return istanza di Volontario
     */
    public static Volontario crea(WamCompany company, EntityManager manager, String nome, String cognome, String email, ArrayList<Funzione> listaFunz) {
        return crea(company, manager, nome, cognome, email, listaFunz.toArray(new Funzione[listaFunz.size()]));
    }// end of static method

    /**
     * Creazione iniziale di un volontario
     * Lo crea SOLO se non esiste già
     *
     * @param company  croce di appartenenza
     * @param manager  the EntityManager to use
     * @param nome     del volontario/milite (obbligatorio)
     * @param cognome  del volontario/milite (obbligatorio)
     * @param funzioni lista delle funzioni (facoltativa)
     * @return istanza di Volontario
     */
    public static Volontario crea(WamCompany company, EntityManager manager, String nome, String cognome, String email, Funzione... funzioni) {
        return crea(company, manager, nome, cognome, email, "", false, funzioni);
    }// end of static method

    /**
     * Creazione iniziale di un volontario
     * Lo crea SOLO se non esiste già
     *
     * @param company  croce di appartenenza
     * @param manager  the EntityManager to use
     * @param nome     del volontario/milite (obbligatorio)
     * @param cognome  del volontario/milite (obbligatorio)
     * @param email    del volontario/milite
     * @param password del volontario/milite (facoltativa)
     * @param admin    flag per il ruolo (facoltativa)
     * @param funzioni lista delle funzioni (facoltativa)
     * @return istanza di Volontario
     */
    public static Volontario crea(WamCompany company, EntityManager manager, String nome, String cognome, String email, String password, boolean admin, Funzione... funzioni) {
        Volontario vol = Volontario.find(company, nome, cognome);

        if (vol == null) {
            vol = new Volontario(company, nome, cognome);
            vol.setEmail(email);
            vol.setPassword(password);
            vol.setAdmin(admin);
            vol.setDipendente(false);
            vol.setAttivo(true);

            if (funzioni != null) {
                for (Funzione funz : funzioni) {
                    vol.volontarioFunzioni.add(new VolontarioFunzione(company, vol, funz));
                } // fine del ciclo for-each
            }// fine del blocco if

            vol = (Volontario) vol.save(manager);
        }// end of if cycle

        return vol;
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
        AQuery.deleteAll(Volontario.class, manager);
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // Getter and setter
    //------------------------------------------------------------------------------------------------------------------------
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }// end of getter method

    /**
     * Abbreviazione visibile nel tabellone e nei popup
     */
    @Override
    public String toString() {
        return getCognome() + " " + getNome().substring(0, 1) + ".";
    }// end of method

    public String getNome() {
        return nome;
    }// end of getter method

    public void setNome(String nome) {
        this.nome = nome;
    }//end of setter method

    public String getCognome() {
        return cognome;
    }// end of getter method

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }//end of setter method

    public String getCodeCompanyUnico() {
        return codeCompanyUnico;
    }// end of getter method

    public void setCodeCompanyUnico(String codeCompanyUnico) {
        this.codeCompanyUnico = codeCompanyUnico;
    }//end of setter method

    public String getCellulare() {
        return cellulare;
    }// end of getter method

    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }//end of setter method

    public String getTelefono() {
        return telefono;
    }// end of getter method

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }//end of setter method

    public String getEmail() {
        return email;
    }// end of getter method

    public void setEmail(String email) {
        this.email = email;
    }//end of setter method

    public String getNote() {
        return note;
    }// end of getter method

    public void setNote(String note) {
        this.note = note;
    }//end of setter method

    public Date getDataNascita() {
        return dataNascita;
    }// end of getter method

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }//end of setter method

    public boolean isDipendente() {
        return dipendente;
    }// end of getter method

    public void setDipendente(boolean dipendente) {
        this.dipendente = dipendente;
    }//end of setter method

    public boolean isAttivo() {
        return attivo;
    }// end of getter method

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
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
    public Volontario save(WamCompany company, EntityManager manager) {
        boolean valido;

        valido = super.checkCompany();
//        if (valido) {
//            valido = this.checkSigla();
//        }// end of if cycle
//        if (valido) {
//            valido = this.checkDescrizione();
//        }// end of if cycle
        if (valido) {
            valido = this.checkChiave(company);
        }// end of if cycle
//        if (valido) {
//            this.checkOrdine(company, manager);
//        }// end of if cycle

        if (valido) {
            return (Volontario) super.save(manager);
        } else {
            return null;
        }// end of if/else cycle

    }// end of method

    //------------------------------------------------------------------------------------------------------------------------
    // Utilities
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Controlla l'esistenza della chiave univoca, PRIMA di salvare il valore nel DB
     * La crea se non esiste già
     *
     * @param company azienda da filtrare
     */
    private boolean checkChiave(WamCompany company) {
        boolean valido = false;

        if (getNome() == null || getNome().equals("") || getCognome() == null || getCognome().equals("")) {
            codeCompanyUnico = null;
        } else {
            if (company != null) {
                codeCompanyUnico = company.getCompanyCode();
            }// end of if cycle
            codeCompanyUnico += getCognome() + getNome();
            valido = true;
        }// end of if/else cycle

        return valido;
    } // end of method


    public String getNomeCognome() {
        return getNome() + " " + getCognome();
    }// end of getter method

    @Override
    public String getNickname() {
        return getNome() + getCognome();
    }

    public String getPassword() {
        String pass = null;
        if (password != null) {
            pass = LibCrypto.decrypt(password);
        }
        return pass;
    }

    public void setPassword(String password) {
        String pass = null;
        if (password != null) {
            pass = LibCrypto.encrypt(password);
        }
        this.password = pass;
    }

    public String getEncryptedPassword() {
        return password;
    }

    @Override
    /**
     * @inheritDoc
     */
    public boolean validatePassword(String password) {
        boolean valid = false;
        String clearPass = getPassword();
        if (clearPass != null) {
            if (clearPass.equals(password)) {
                valid = true;
            }
        }
        return valid;
    }

    public void addFunzione(Funzione funzione) {
        VolontarioFunzione volFun = null;

        if (getCompany() == null) {
            Exception e = new Exception("Impossibile aggiungere funzioni al volontario se manca la croce");
            e.printStackTrace();
            return;
        }// end of if cycle

        if (volontarioFunzioni != null) {
            volFun = new VolontarioFunzione(this, funzione);
            volFun.setCompany(getCompany());
            volontarioFunzioni.add(volFun);
        }// end of if cycle
    }// end of method

    /**
     * Rimuove una funzione dal volontario
     */
    public void removeFunzione(Funzione funzione) {
        for (VolontarioFunzione vf : volontarioFunzioni) {
            if (vf.getFunzione().equals(funzione)) {
                volontarioFunzioni.remove(vf);
                break;
            }
        }
    }

    /**
     * Verifica se il volontario ha una data funzione
     *
     * @param funz la funzione da verificare
     * @return true se ha la funzione
     */
    public boolean haFunzione(Funzione funz) {
        boolean found = false;
        List<VolontarioFunzione> vfunzioni = getVolontarioFunzioni();
        for (VolontarioFunzione vf : vfunzioni) {
            if (vf.getFunzione().equals(funz)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Verifica se il volontario è un admin
     *
     * @return true se è un admin
     */
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Volontario that = (Volontario) o;

        if (nome != null ? !nome.equals(that.nome) : that.nome != null) return false;
        if (cognome != null ? !cognome.equals(that.cognome) : that.cognome != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        return !(dataNascita != null ? !dataNascita.equals(that.dataNascita) : that.dataNascita != null);

    }

    @Override
    public int hashCode() {
        int result = nome != null ? nome.hashCode() : 0;
        result = 31 * result + (cognome != null ? cognome.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (dataNascita != null ? dataNascita.hashCode() : 0);
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Clone
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatoria invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza di Milite con gli stessi valori dei parametri di questa istanza
     */
    @Override
    @SuppressWarnings("all")
    public Volontario clone() throws CloneNotSupportedException {
        try {
            return (Volontario) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of Entity class
