package it.algos.wam.entity.funzione;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Notification;
import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
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
 * Entity per una funzione
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

    //--descrizione (obbligatoria)
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    private String descrizione = "";

    //--ordine di presentazione nelle liste (obbligatorio, con controllo automatico prima del persist se è zero)
    @NotNull
    @Index
    private int ordine = 0;

    //--codepoint dell'icona di FontAwesome (facoltative)
    private int iconCodepoint;

    //--tavola di incrocio
    @OneToMany(mappedBy = "funzione", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<ServizioFunzione> servizioFunzioni = new ArrayList<>();

    //--tavola di incrocio
    @OneToMany(mappedBy = "funzione", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<VolontarioFunzione> volontarioFunzioni = new ArrayList<>();


    //------------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Costruttore senza argomenti
     * Obbligatorio per le specifiche JavaBean
     */
    public Funzione() {
    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * Se manca l'ordine di presentazione o è uguale a zero, viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       di riferimento interna (obbligatoria)
     * @param descrizione (obbligatoria)
     */
    public Funzione(WamCompany company, String sigla, String descrizione) {
        this(company, sigla, descrizione, 0, null);
    }// end of constructor

    /**
     * Costruttore completo
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       di riferimento interna (obbligatoria)
     * @param descrizione (obbligatoria)
     * @param ordine      di presentazione nelle liste (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param glyph       icona di FontAwesome (facoltative)
     */
    public Funzione(WamCompany company, String sigla, String descrizione, int ordine, FontAwesome glyph) {
        super();
        this.setCompany(company);
        this.setSigla(sigla);
        this.setDescrizione(descrizione);
        this.setOrdine(ordine);
        this.setIcon(glyph);
    }// end of constructor


    //------------------------------------------------------------------------------------------------------------------------
    // Count records
    // CountBy...
    // Con e senza Company
    // Con e senza EntityManager
    // Con Company, EntityManager And Property
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera il numero totale di records della Entity
     * Senza filtri.
     *
     * @return il numero totale di records nella Entity
     */
    public static int countByAllCompanies() {
        return countByAllCompanies(null);
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
     * Filtrato sulla azienda corrente.
     *
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCurrentCompany() {
        return countByCurrentCompany(null);
    }// end of static method

    /**
     * Recupera il numero di records della Entity
     * Filtrato sulla azienda corrente.
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
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCompany(WamCompany company) {
        return countByCompany(company, null);
    }// end of static method

    /**
     * Recupera il numero di records della Entity
     * Filtrato sulla azienda passata come parametro.
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
     * Filtrato sulla azienda passata come parametro.
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
        return CompanyQuery.count(Funzione.class, company, manager);
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
    public static Funzione find(long id, EntityManager manager) {
        BaseEntity entity = AQuery.find(Funzione.class, id, manager);
        return check(entity);
    }// end of static method

    /**
     * Controlla se l'istanza della Entity esiste ed è della classe corretta
     *
     * @param entity (BaseEntity) restituita dalla query generica
     * @return istanza della Entity specifica, null se non trovata
     */
    private static Funzione check(BaseEntity entity) {
        Funzione instance = null;

        if (entity != null && entity instanceof Funzione) {
            instance = (Funzione) entity;
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
    public static Funzione findBySigla(String sigla) {
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
    public static Funzione findBySigla(String sigla, EntityManager manager) {
        return (Funzione)CompanyQuery.getEntity(Funzione.class, Funzione_.sigla, sigla, manager);
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param sigla   di riferimento interna (obbligatoria)
     * @return istanza della Entity, null se non trovata
     */
    public static Funzione findByCompanyAndBySigla(WamCompany company, String sigla) {
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
    public static Funzione findByCompanyAndBySigla(WamCompany company, String sigla, EntityManager manager) {
        return (Funzione) CompanyQuery.getEntity(Funzione.class, Funzione_.sigla, sigla, company, manager);
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
    public static List<Funzione> findByAllCompanies() {
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
    public static List<Funzione> findByAllCompanies(EntityManager manager) {
        return (List<Funzione>) AQuery.findAll(Funzione.class, manager);
    }// end of static method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente.
     *
     * @return lista di tutte le entities
     */
    public static List<Funzione> findByCurrentCompany() {
        return findByCurrentCompany(null);
    }// end of static method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente.
     *
     * @param manager the EntityManager to use
     * @return lista di tutte le entities
     */
    public static List<Funzione> findByCurrentCompany(EntityManager manager) {
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
    public static List<Funzione> findBySingleCompany(WamCompany company) {
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
    public static List<Funzione> findBySingleCompany(WamCompany company, EntityManager manager) {
        if (company != null) {
            return (List<Funzione>) AQuery.findAll(Funzione.class, CompanyEntity_.company, company, manager);
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
     * @param sigla       di riferimento interna (obbligatoria)
     * @param descrizione (obbligatoria)
     * @return istanza della Entity
     */
    public static Funzione crea(WamCompany company, String sigla, String descrizione) {
        return crea(company, sigla, descrizione, (EntityManager) null);
    }// end of static method


    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       di riferimento interna (obbligatoria)
     * @param descrizione (obbligatoria)
     * @param manager     the EntityManager to use
     * @return istanza della Entity
     */
    public static Funzione crea(WamCompany company, String sigla, String descrizione, EntityManager manager) {
        return crea(company, sigla, descrizione, 0, (FontAwesome) null, manager);
    }// end of static method

    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       di riferimento interna (obbligatoria)
     * @param descrizione (obbligatoria)
     * @param ordine      di presentazione nelle liste (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param glyph       dell'icona (facoltativo)
     * @return istanza della Entity
     */
    public static Funzione crea(WamCompany company, String sigla, String descrizione, int ordine, FontAwesome glyph) {
        return crea(company, sigla, descrizione, ordine, glyph, (EntityManager) null);
    }// end of static method


    /**
     * Creazione iniziale di una istanza della Entity
     * La crea SOLO se non esiste già
     *
     * @param company     di appartenenza (property della superclasse)
     * @param sigla       di riferimento interna (obbligatoria)
     * @param descrizione (obbligatoria)
     * @param ordine      di presentazione nelle liste (obbligatorio, con controllo automatico prima del persist se è zero)
     * @param glyph       dell'icona (facoltativo)
     * @param manager     the EntityManager to use
     * @return istanza della Entity
     */
    public static Funzione crea(
            WamCompany company,
            String sigla,
            String descrizione,
            int ordine,
            FontAwesome glyph,
            EntityManager manager) {
        Funzione funzione = Funzione.findByCompanyAndBySigla(company, sigla, manager);

        if (funzione == null) {
            funzione = new Funzione(company, sigla, descrizione, ordine, glyph);
            funzione.save(company, manager);
        }// end of if cycle

        return funzione;
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // Delete
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Delete all the records for the Entity class
     * Bulk delete records with CriteriaDelete
     */
    public static void deleteAll() {
        EntityManager manager = EM.createEntityManager();
        deleteAll(manager);
        manager.close();
    }// end of static method

    /**
     * Delete all the records for the Entity class
     * Bulk delete records with CriteriaDelete
     *
     * @param manager the EntityManager to use
     */
    public static void deleteAll(EntityManager manager) {
        AQuery.deleteAll(Funzione.class, manager);
    }// end of static method


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
    public Funzione save(WamCompany company, EntityManager manager) {
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
            return (Funzione) super.save(manager);
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
            int max = WamQuery.maxOrdineFunzione(company, manager);
            setOrdine(max + 1);
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
     * Compara per ordine della funzione
     *
     * @param other funzione
     */
    @Override
    public int compareTo(Funzione other) {
        Integer ordQuesto = getOrdine();
        Integer ordAltro = other.getOrdine();
        return ordQuesto.compareTo(ordAltro);
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
