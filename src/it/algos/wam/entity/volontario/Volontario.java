package it.algos.wam.entity.volontario;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Grid;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.iscrizione.Iscrizione_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione_;
import it.algos.wam.entity.statistiche.StatisticheMod;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.lib.LibWam;
import it.algos.wam.settings.CompanyPrefs;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.log.Log;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.field.AFType;
import it.algos.webbase.web.field.AIField;
import it.algos.webbase.web.lib.LibCrypto;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.login.UserIF;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.query.SortProperty;
import it.algos.webbase.web.search.SearchManager;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Entity che descrive un Volontario
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
@DefaultSort({"cognome,true", "nome,true"})
public class Volontario extends WamCompanyEntity implements UserIF {

    //------------------------------------------------------------------------------------------------------------------------
    // Properties
    //------------------------------------------------------------------------------------------------------------------------
    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    //--company di riferimento (facoltativa nella superclasse)
    //--company di riferimento (obbligatoria in questa classe)
    //--private BaseCompany company;
    /**
     * Nel form un field di tipo EmailField (facoltativo)
     */
    @AIField(type = AFType.email, width = "16em", caption = "Mail", prompt = "nome.cognome@mail.it", help = "Inserire un indirizzo valido, oppure lasciare vuoto")
    public String email = "";
    /**
     * Nome del volontario (obbligatorio, non unico)
     */
    @NotEmpty
    @Column(length = 30)
    @Index
    @AIField(type = AFType.text, required = true, width = "12em", caption = "Nome", prompt = "Giovanni", help = "Inserire un nome. Obbligatorio", search = SearchManager.SearchType.STARTS_WITH)
    private String nome = "";
    /**
     * Cognome del volontario (obbligatorio, non unico)
     */
    @NotEmpty
    @Column(length = 30)
    @Index
    @AIField(type = AFType.text, required = true, width = "16em", caption = "Cognome", prompt = "Rossi", help = "Inserire un cognome. Obbligatorio", search = SearchManager.SearchType.STARTS_WITH)
    private String cognome = "";
    /**
     * Sigla di codifica interna (obbligatoria, unica in generale indipendentemente dalla company)
     * Calcolata -> codeCompanyUnico = company.companyCode + volontario.cognome + volontario.nome (20+30+30=80);
     * Va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
     */
    @NotEmpty
    @NotNull
    @Column(length = 80, unique = true)
    @Index
    @AIField(type = AFType.text, required = true, enabled = false, width = "18em", caption = "CodiceUnico", help = "Codifica interna. Valore unico. Calcola automaticamente")
    private String codeCompanyUnico = "";
    /**
     * Dati personali facoltativi
     */
    @AIField(type = AFType.text, width = "12em", caption = "Cellulare", prompt = "000 111111", help = "Inserire un numero di cellulare, oppure lasciare vuoto")
    private String cellulare = "";

    private String telefono = "";

    @AIField(type = AFType.date, caption = "Data di nascita", help = "Data di nascita (facoltativa)")
    private Date dataNascita = null;

    @AIField(type = AFType.password, required = true, caption = "Password", prompt = "...", help = "Password iniziale, successivamente modificabile solo dal volontario.")
    private String password = "";

    @AIField(type = AFType.checkbox, caption = "Invio Mail Turni")
    private boolean invioMail = false;

    private String note = "";

    /**
     * Dati dell'associazione
     */
    @AIField(type = AFType.checkbox, caption = "Admin")
    private boolean admin = false;
    @AIField(type = AFType.checkbox, caption = "Dipendente")
    private boolean dipendente = false;
    @AIField(type = AFType.checkbox, caption = "Infermiere")
    private boolean infermiere = false;

    //--attivo è un flag MANUALE per un volontario da eliminare (non opera più), ma senza cancellare il record
    @AIField(type = AFType.checkbox, caption = "Attivo")
    private boolean attivo = true;

    //--esenteFrequenza è un flag MANUALE per un volontario che non ha l'obbligo di rispettare la frequenza minima
    @AIField(type = AFType.checkbox, caption = "Esente frequenza minima")
    private boolean esenteFrequenza = false;

    //--scadenza patente di guida (per autisti)
    //--data di scadenza della patente (normale o CRI)
    //--se non valorizzata, il milite non ha acquisito la patente
    private Date scadenzaPatente = null;
    //--scadenza certificati
    //--data di scadenza del certificato BSD
    //--se non valorizzata, il milite non ha acquisito il certificato
    @AIField(type = AFType.date, caption = "BLSD", help = "Basic LifeSupport & Defibrillation")
    private Date scadenzaBLSD = null;
    //    //--data di scadenza del certificato Non Trauma
    //--se non valorizzata, il milite non ha acquisito il certificato
    @AIField(type = AFType.date, caption = "PNT", help = "Patologie Non Traumatiche")
    private Date scadenzaNonTrauma = null;
    //--data di scadenza del certificato Trauma
    //--se non valorizzata, il milite non ha acquisito il certificato
    @AIField(type = AFType.date, caption = "BPHTP", help = "Basic Pre Hospital & Presidi")
    private Date scadenzaTrauma = null;

    //--tavola di incrocio
    // CascadeType.ALL: quando chiamo persist sul padre, persiste automaticamente tutti i nuovi figli aggiunti
    // alla lista e non ancora registrati (e così per tutte le operazioni dell'EntityManager)
    // orphanRemoval = true: quando registro il padre, cancella tutti i figli eventualmente rimasti orfani.
    // CascadeOnDelete: instaura l'integrità referenziale a livello di database (foreign key on delete cascade)
    @OneToMany(mappedBy = "volontario", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<VolontarioFunzione> volontarioFunzioni = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Costruttore senza argomenti
     * Obbligatorio per le specifiche JavaBean
     * Da non usare MAI per la creazione diretta di una nuova istanza (si perdono i controlli)
     */
    public Volontario() {
        this("", "");
    }// end of JavaBean constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * L'ordine di presentazione nel tabellone viene inserito in automatico prima del persist
     *
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     */
    public Volontario(String nome, String cognome) {
        this((WamCompany) null, nome, cognome);
    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Filtrato sulla company passata come parametro.
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * L'ordine di presentazione nel tabellone viene inserito in automatico prima del persist
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     */
    public Volontario(WamCompany company, String nome, String cognome) {
        this(company, nome, cognome, (Date) null, "", false);
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
        if (company != null) {
            this.setCompany(company);
        }// end of if cycle
        this.setNome(nome);
        this.setCognome(cognome);
        this.setDataNascita(dataNascita);
        this.setCellulare(cellulare);
        this.setDipendente(dipendente);
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
        return AQuery.count(Volontario.class, manager);
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
        return CompanyQuery.count(Volontario.class, company, manager);
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
        return CompanyQuery.count(Volontario.class, attr, value, company, manager);
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
    public static Volontario find(long id) {
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
    public static Volontario find(long id, EntityManager manager) {
        return (Volontario) AQuery.find(Volontario.class, id, manager);
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
    public static Volontario getEntityByCodeCompanyUnico(String codeCompanyUnico) {
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
    public static Volontario getEntityByCodeCompanyUnico(String codeCompanyUnico, EntityManager manager) {
        return (Volontario) AQuery.getEntity(Volontario.class, Volontario_.codeCompanyUnico, codeCompanyUnico, manager);
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
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @return istanza della Entity, null se non trovata
     */
    public static Volontario getEntityByNomeAndCognome(String nome, String cognome) {
        return getEntityByCompanyAndNomeAndCognome((WamCompany) null, nome, cognome);
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @return istanza della Entity, null se non trovata
     */
    public static Volontario getEntityByCompanyAndNomeAndCognome(WamCompany company, String nome, String cognome) {
        return getEntityByCompanyAndNomeAndCognome(WamCompany.getCurrent(), nome, cognome, (EntityManager) null);
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Volontario getEntityByCompanyAndNomeAndCognome(WamCompany company, String nome, String cognome, EntityManager manager) {
        Container.Filter filterCompany = new Compare.Equal(Volontario_.company.getName(), company);
        Container.Filter filterNome = new Compare.Equal(Volontario_.nome.getName(), nome);
        Container.Filter filterCognome = new Compare.Equal(Volontario_.cognome.getName(), cognome);
        return (Volontario) AQuery.getEntity(Volontario.class, manager, filterCompany, filterNome, filterCognome);
    }// end of static method

    public static boolean isEntityByNomeAndCognome(String nome, String cognome) {
        return getEntityByNomeAndCognome(nome, cognome) != null;
    }// end of static method

    public static boolean isNotEntityByNomeAndCognome(String nome, String cognome) {
        return !isEntityByNomeAndCognome(nome, cognome);
    }// end of static method

    /**
     * Controlla che esista una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @param manager the EntityManager to use
     * @return vero se esiste Entity, false se non trovata
     */
    public static boolean isEntityByCompanyAndNomeAndCognome(WamCompany company, String nome, String cognome, EntityManager manager) {
        return getEntityByCompanyAndNomeAndCognome(company, nome, cognome, manager) != null;
    }// end of static method

    /**
     * Controlla che non esista una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @param manager the EntityManager to use
     * @return vero se esiste Entity, false se non trovata
     */
    public static boolean isNotEntityByCompanyAndNomeAndCognome(WamCompany company, String nome, String cognome, EntityManager manager) {
        return !isEntityByCompanyAndNomeAndCognome(company, nome, cognome, manager);
    }// end of static method


    /**
     * Recupera un volontario della company corrente per nick
     *
     * @param nick il nick (come ritornato da getNickname())
     * @return il volontario
     */
    @SuppressWarnings("unchecked")
    public static Volontario queryByNick(String nick) {
        Volontario found = null;

        for (Volontario vol : getListByCurrentCompany()) {
            if (vol.getNickname().equals(nick)) {
                found = vol;
                break;
            }
        }

        return found;
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
    public static List<Volontario> getListByAllCompanies() {
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
    public static List<Volontario> getListByAllCompanies(EntityManager manager) {
        return (List<Volontario>) AQuery.getList(Volontario.class, manager);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     *
     * @return lista di tutte le entities
     */
    public static List<Volontario> getListByCurrentCompany() {
        return getListByCurrentCompany((EntityManager) null);
    }// end of static method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     *
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    public static List<Volontario> getListByCurrentCompany(EntityManager manager) {
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
    public static List<Volontario> getListByCompany(WamCompany company) {
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
    public static List<Volontario> getListByCompany(WamCompany company, EntityManager manager) {
        if (company != null) {
            return (List<Volontario>) CompanyQuery.getList(Volontario.class, CompanyEntity_.company, company, manager);
        } else {
            return new ArrayList<>();
        }// end of if/else cycle
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i volontari attivi relativi ad una funzione
     * Filtrato sulla company corrente
     *
     * @param funzione da utilizzare per filtrare i volontari
     * @return lista delle entities selezionate
     */
    @SuppressWarnings("unchecked")
    public static List<Volontario> getListByFunzione(Funzione funzione) {
        List<Volontario> lista = new ArrayList<>();
        List<VolontarioFunzione> volontarioFunzioni = null;
        Volontario vol;

        if (funzione != null) {
            volontarioFunzioni = (List<VolontarioFunzione>) CompanyQuery.getList(VolontarioFunzione.class, VolontarioFunzione_.funzione, funzione);
        }// end of if cycle

        if (volontarioFunzioni != null) {
            for (VolontarioFunzione volFunz : volontarioFunzioni) {
                vol = volFunz.getVolontario();
                if (vol.isAttivo()) {
                    lista.add(volFunz.getVolontario());
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return orderList(lista);
    }// end of static method

    /**
     * Recupera una lista (array) di tutti i volontari attivi
     * Filtrato sulla company corrente
     *
     * @return lista delle entities selezionate
     */
    @SuppressWarnings("unchecked")
    public static List<Volontario> getListIsAttivo() {
        List<Volontario> lista = (List<Volontario>) CompanyQuery.getList(Volontario.class, Volontario_.attivo, true);
        return orderList(lista);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i volontari abilitati al login
     * Filtrato sulla company corrente
     *
     * @return lista delle entities selezionate
     */
    @SuppressWarnings("unchecked")
    public static List<Volontario> getListIsAbilitato() {
        List<Volontario> listaAbilitati = null;
        List<Volontario> listaAttivi = getListIsAttivo();

        if (listaAttivi != null && listaAttivi.size() > 0) {
            listaAbilitati = new ArrayList<>();
            for (Volontario vol : listaAttivi) {
                if (vol.isLoginAbilitato()) {
                    listaAbilitati.add(vol);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return listaAbilitati;
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Get properties (list)
    //------------------------------------------------------------------------------------------------------------------------
    public static List<String> getListStrForCodeCompanyUnico() {
        return getListStrForCodeCompanyUnico((EntityManager) null);
    }// end of static method

    public static List<String> getListStrForCodeCompanyUnico(EntityManager manager) {
        return CompanyQuery.getListStr(Volontario.class, Volontario_.codeCompanyUnico, null, manager);
    }// end of static method

    public static List<String> getListStrForNickname() {
        return getListStrForNicknameByCompany((WamCompany) null, (EntityManager) null);
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
    public static List<String> getListStrForNicknameByCompany(WamCompany company, EntityManager manager) {
        List<String> lista = new ArrayList<>();

        for (Volontario vol : getListByCompany(company, manager)) {
            lista.add(vol.toString());
        }// end of for cycle

        return lista;
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // New and save
    //------------------------------------------------------------------------------------------------------------------------


    public static Volontario crea(String nome, String cognome) {
        return crea(WamCompany.getCurrent(), nome, cognome);
    }// end of static method

    public static Volontario crea(WamCompany company, String nome, String cognome) {
        return crea(company, nome, cognome, (EntityManager) null);
    }// end of static method

    public static Volontario crea(WamCompany company, String nome, String cognome, EntityManager manager) {
        return crea(company, manager, nome, cognome, "", "", (List<Funzione>) null);
    }// end of static method

    public static Volontario crea(
            WamCompany company,
            EntityManager manager,
            String nome,
            String cognome,
            String cellulare,
            String email,
            List<Funzione> listaFunz) {
        return crea(company, manager, nome, cognome, (Date) null, cellulare, "", email, "", "", false, false, true, null, null, null, false, listaFunz);
    }// end of static method

    public static Volontario crea(
            WamCompany company,
            EntityManager manager,
            String nome,
            String cognome,
            String cellulare,
            String email,
            String password,
            boolean admin,
            Date sBLSD,
            Date sPNT,
            Date sBPHTP,
            List<Funzione> listaFunz) {
        return crea(company, manager, nome, cognome, (Date) null, cellulare, "", email, password, "", admin, false, true, sBLSD, sPNT, sBPHTP, false, listaFunz);
    }// end of static method

    /**
     * Creazione iniziale di un volontario
     * Lo crea SOLO se non esiste già
     *
     * @param company       croce di appartenenza
     * @param manager       the EntityManager to use
     * @param nome          del volontario/milite (obbligatorio)
     * @param cognome       del volontario/milite (obbligatorio)
     * @param cellulare     del volontario/milite (facoltativo)
     * @param email         del volontario/milite
     * @param password      del volontario/milite (facoltativa)
     * @param admin         flag per il ruolo (facoltativa)
     * @param scadenzaBLSD  del brevetto (facoltativa)
     * @param scadenzaPNT   del brevetto (facoltativa)
     * @param scadenzaBPHTP del brevetto (facoltativa)
     * @param funzioni      lista delle funzioni (facoltativa)
     * @return istanza di Volontario
     */
    public static Volontario crea(
            WamCompany company,
            EntityManager manager,
            String nome,
            String cognome,
            Date dataNascita,
            String cellulare,
            String telefono,
            String email,
            String password,
            String note,
            boolean admin,
            boolean dipendente,
            boolean attivo,
            Date scadenzaBLSD,
            Date scadenzaPNT,
            Date scadenzaBPHTP,
            boolean esenteFrequenza,
            List<Funzione> funzioni) {
        Volontario volontario = null;

        if (isNotEntityByCompanyAndNomeAndCognome(company, nome, cognome, manager)) {
            try { // prova ad eseguire il codice
                volontario = new Volontario(company, nome, cognome);
                volontario.setDataNascita(dataNascita);
                volontario.setCellulare(cellulare);
                volontario.setTelefono(telefono);
                volontario.setEmail(email);
                volontario.setPassword(password);
                volontario.setNote(note);
                volontario.setAdmin(admin);
                volontario.setDipendente(dipendente);
                volontario.setAttivo(attivo);
                volontario.setScadenzaBLSD(scadenzaBLSD);
                volontario.setScadenzaNonTrauma(scadenzaPNT);
                volontario.setScadenzaTrauma(scadenzaBPHTP);
                volontario.setEsenteFrequenza(esenteFrequenza);

                if (funzioni != null) {
                    for (Funzione funz : funzioni) {
                        volontario.volontarioFunzioni.add(new VolontarioFunzione(company, volontario, funz));
                    } // fine del ciclo for-each
                }// fine del blocco if

                volontario = (Volontario) volontario.save(manager);
            } catch (Exception unErrore) { // intercetta l'errore
                volontario = null;
            }// fine del blocco try-catch
        }// end of if cycle

        return volontario;
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
        return CompanyQuery.delete(Volontario.class, company, manager);
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
        if (CompanyPrefs.usaPrimaCognome.getBool()) {
            return getCognome() + " " + getNome().substring(0, 1) + ".";
        } else {
            return getNome() + " " + getCognome().substring(0, 1) + ".";
        }// end of if/else cycle
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

    public Date getScadenzaPatente() {
        return scadenzaPatente;
    }// end of getter method

    public void setScadenzaPatente(Date scadenzaPatente) {
        this.scadenzaPatente = scadenzaPatente;
    }//end of setter method

    public Date getScadenzaBLSD() {
        return scadenzaBLSD;
    }// end of getter method

    public void setScadenzaBLSD(Date scadenzaBLSD) {
        this.scadenzaBLSD = scadenzaBLSD;
    }//end of setter method

    public Date getScadenzaTrauma() {
        return scadenzaTrauma;
    }// end of getter method

    public void setScadenzaTrauma(Date scadenzaTrauma) {
        this.scadenzaTrauma = scadenzaTrauma;
    }//end of setter method

    public Date getScadenzaNonTrauma() {
        return scadenzaNonTrauma;
    }// end of getter method

    public void setScadenzaNonTrauma(Date scadenzaNonTrauma) {
        this.scadenzaNonTrauma = scadenzaNonTrauma;
    }//end of setter method

    public boolean getAdmin() {
        return admin;
    }// end of getter method

//    public int getOreAnno() {
//        return oreAnno;
//    }// end of getter method
//
//    public void setOreAnno(int oreAnno) {
//        this.oreAnno = oreAnno;
//    }//end of setter method
//
//    public int getTurniAnno() {
//        return turniAnno;
//    }// end of getter method
//
//    public void setTurniAnno(int turniAnno) {
//        this.turniAnno = turniAnno;
//    }//end of setter method
//
//    public int getOreExtra() {
//        return oreExtra;
//    }// end of getter method
//
//    public void setOreExtra(int oreExtra) {
//        this.oreExtra = oreExtra;
//    }//end of setter method

    public boolean isInvioMail() {
        return invioMail;
    }

    public void setInvioMail(boolean invioMail) {
        this.invioMail = invioMail;
    }

    public boolean isEsenteFrequenza() {
        return esenteFrequenza;
    }

    public void setEsenteFrequenza(boolean esenteFrequenza) {
        this.esenteFrequenza = esenteFrequenza;
    }

    public boolean isInfermiere() {
        return infermiere;
    }

    public void setInfermiere(boolean infermiere) {
        this.infermiere = infermiere;
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Save
    //------------------------------------------------------------------------------------------------------------------------
    @PrePersist
    public void checkNomeCognome() {
        nome = LibText.primaMaiuscola(nome);
        cognome = LibText.primaMaiuscola(cognome);
    }// end of method

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
    public Volontario save(WamCompany company, EntityManager manager) {
        boolean valido;

        valido = super.checkCompany();
        if (valido) {
            valido = this.checkNome();
        }// end of if cycle
        if (valido) {
            valido = this.checkCognome();
        }// end of if cycle
        if (valido) {
            valido = this.checkChiave(company, manager);
        }// end of if cycle

        if (valido) {
            return (Volontario) super.save(manager);
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
    public Volontario saveSafe() {
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
    public Volontario saveSafe(EntityManager manager) {
        return (Volontario) this.save(manager);
    }// end of method

    //------------------------------------------------------------------------------------------------------------------------
    // Utilities
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Ordina una lista (array) di volontari per cognome
     *
     * @return lista delle entities selezionate
     */
    @SuppressWarnings("unchecked")
    public static List<Volontario> orderList(List<Volontario> listaDisordinata) {
        List<Volontario> listaOrdinata = new ArrayList<>();
        TreeMap<String, Volontario> mappa = new TreeMap();
        Set set;

        for (Volontario vol : listaDisordinata) {
            mappa.put(vol.getCognome() + vol.getNome(), vol);
        }// end of for cycle

        for (String key : mappa.keySet()) {
            listaOrdinata.add(mappa.get(key));
        }// end of for cycle

        return listaOrdinata;
    }// end of static method

    /**
     * Implementa come business logic, la obbligatorietà del nome
     * <p>
     *
     * @return true se esiste, false se non esiste
     */
    private boolean checkNome() {
        String caption = "La funzione non può essere accettata, perché manca il nome che è obbligatorio";

        if (getNome() != null && !getNome().equals("")) {
            return true;
        } else {
//            Notification.show(caption, Notification.Type.WARNING_MESSAGE);
            return false;
        }// end of if/else cycle
    } // end of method


    /**
     * Implementa come business logic, la obbligatorietà del cognome
     * <p>
     *
     * @return true se esiste, false se non esiste
     */
    private boolean checkCognome() {
        String caption = "La funzione non può essere accettata, perché manca il cognome che è obbligatorio";

        if (getCognome() != null && !getCognome().equals("")) {
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

        codeCompanyUnico = LibText.creaChiave(getCompany(), cognome, nome);
        if (codeCompanyUnico.equals("") || isEsistente(codeCompanyUnico, manager)) {
            valido = false;
        }// end of if cycle

        return valido;
    } // end of method

    public boolean isEsistente(String codeCompanyUnico, EntityManager manager) {
        return this.getId() == null && Volontario.isEntityByCodeCompanyUnico(codeCompanyUnico, manager);
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

//        if (password != null) {
//            pass = LibCrypto.decrypt(password);
//        }

        if (WAMApp.PASSWORD_CRIPTATE) {
            pass = LibCrypto.decrypt(password);
        } else {
            pass = password;
        }// end of if/else cycle

        return pass;
    }// end of method

    public void setPassword(String password) {
        String pass = null;

//        if (password != null) {
//            pass = LibCrypto.encrypt(password);
//        }

        if (WAMApp.PASSWORD_CRIPTATE) {
            pass = LibCrypto.encrypt(password);
        } else {
            pass = password;
        }// end of if/else cycle

        this.password = pass;
    }// end of method

    public String getEncryptedPassword() {
//        return password;
        return LibCrypto.encrypt(password);
    }// end of method

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
    }//end of setter method

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

    /**
     * Controlla che la frequenza dei turni eseguita sia valida per i parametri della company
     * <p>
     * Se la company NON controlla la frequenza (), questa è sempre valida e i turni minimi richiesti sono pari a zero
     * Recupera dalla company il valore di frequenza minima necessario
     * Calcola i turni richiesti da inizio anno
     * //     * Recupera le iscrizioni del volontario
     * Calcola i turni effettivamente eseguiti da inizio anno
     * Controlla che i turni effettivamente eseguiti siano uguali o superiori a quelli richiesti
     *
     * @return true se i turni eseguiti sono uguali o superiori a quelli richiesti
     */
    public boolean isFrequenzaValida() {
        boolean valida = false;
        int turniMinimiMensili;
        int turniTeoriciDaInizioAnno;
        int turniEffettivi;

        //--Se la company NON controlla la frequenza (), questa è sempre valida e i turni minimi richiesti sono pari a zero
        if (!Pref.getBool(CompanyPrefs.controllaFrequenza.getCode(), true)) {
            return true;
        }// end of if cycle

        //--Recupera dalla company il valore di frequenza minima necessario
        turniMinimiMensili = Pref.getInt(CompanyPrefs.turniMinimiMensili.getCode(), 2);

        //--Calcola i turni richiesti da inizio anno
        turniTeoriciDaInizioAnno = turniDaInizioAnno(turniMinimiMensili);

        //--Calcola i turni effettivamente eseguiti da inizio anno
        turniEffettivi = turniEffettivi();

        //--Controlla che i turni effettivamente eseguiti siano uguali o superiori a quelli richiesti
        valida = (turniEffettivi >= turniTeoriciDaInizioAnno);

        return valida;
    }// end of method


    /**
     * Calcola i turni richiesti da inizio anno
     */
    private int turniDaInizioAnno(int turniMinimiMensili) {
        int turniDaInizioAnno;
        int giorniDaInizioAnno = LibDate.getDayYear();
        int giorniTurno = 30 / turniMinimiMensili;

        turniDaInizioAnno = giorniDaInizioAnno / giorniTurno;

        return turniDaInizioAnno;
    }// end of method

    /**
     * Calcola i turni effettivamente eseguiti da inizio anno
     */
    private int turniEffettivi() {
        int turniEffettivi = 0;
        int chiaveIni = LibWam.creaChiavePrimaGennaio();
        int chiaveEnd = LibWam.creaChiaveOdierna();
        int chiave;
        Turno turno;
        List<Iscrizione> listaIscrizioni = (List<Iscrizione>) CompanyQuery.getList(Iscrizione.class, Iscrizione_.volontario, this);
        //       Container.Filter filtroVolontario = new Compare.Equal(Iscrizione_.volontario.getName(), this);
        //       Container.Filter filtroOggi = new Compare.Equal(Iscrizione_.turno.getBindableJavaType().getName(), new Date());

        for (Iscrizione isc : listaIscrizioni) {
            turno = isc.getTurno();
            if (turno != null) {
                chiave = (int) turno.getChiave();
                if (chiave >= chiaveIni && chiave <= chiaveEnd) {
                    turniEffettivi++;
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

        return turniEffettivi;
    }// end of method

    /**
     * Controlla che il login sia abilitato
     * <p>
     * Il volontario deve essere attivo
     * Il volontario deve avere regolato a true il flag esenteFrequenza oppure
     * la company non controlla la frequenza oppure
     * deve avere una frequenza valida
     *
     * @return true se il login soddisfa le condizioni ed è abilitato
     */
    public boolean isLoginAbilitato() {
        boolean abilitato = false;

        //--Il volontario deve essere attivo
        if (!this.isAttivo()) {
            return false;
        }// end of if cycle

        if (this.isEsenteFrequenza()) {
            return true;
        }// end of if cycle

        if (!Pref.getBool(CompanyPrefs.controllaFrequenza.getCode(), true)) {
            return true;
        }// end of if cycle

        if (this.isFrequenzaValida()) {
            return true;
        }// end of if cycle

        return abilitato;
    }// end of method


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
