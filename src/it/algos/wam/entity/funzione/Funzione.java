package it.algos.wam.entity.funzione;

import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
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
public class Funzione extends WamCompanyEntity implements Comparable<Funzione> {

    private static final long serialVersionUID = 1L;


    //--sigla di riferimento interna NON visibile nel tabellone (obbligatoria)
    //@todo cambiare nome della property in siglaInterna
    @NotEmpty
    @Column(length = 20)
    @Index
    private String sigla = "";


    //--descrizione visibile nel tabellone (obbligatoria)
    //@todo cambiare nome della property in siglaVisibile
    @NotEmpty
    @Column(length = 100)
    @Index
    private String descrizione = "";


    //--ordine di presentazione nelle liste
    @Index
    private int ordine = 0;


    //--codepoint dell'icona di FontAwesome (facoltative)
    private int iconCodepoint;


    //--note di spiegazione (facoltative)
    @Column(columnDefinition = "text")
    private String note = "";


    //--tavola di incrocio
    @OneToMany(mappedBy = "funzione", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<ServizioFunzione> servizioFunzioni = new ArrayList();


    //--tavola di incrocio
    @OneToMany(mappedBy = "funzione", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<VolontarioFunzione> volontarioFunzioni = new ArrayList();


    /**
     * Costruttore senza argomenti
     * Obbligatorio per le specifiche JavaBean
     */
    public Funzione() {
    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     *
     * @param company     croce di appartenenza (property della superclasse)
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     */
    public Funzione(BaseCompany company, String sigla, String descrizione) {
        this(company, sigla, descrizione, 0, null, "");
    }// end of constructor


    /**
     * Costruttore completo
     *
     * @param company     croce di appartenenza
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nelle liste
     * @param glyph       icona di FontAwesome (facoltative)
     * @param note        di spiegazione (facoltative)
     */
    public Funzione(BaseCompany company, String sigla, String descrizione, int ordine, FontAwesome glyph, String note) {
        super();
        this.setCompany(company);
        this.setSigla(sigla);
        this.setDescrizione(descrizione);
        this.setOrdine(ordine);
        this.setIcon(glyph);
        this.setNote(note);
    }// end of constructor

    /**
     * Recupera il totale dei records della Entity
     * Filtrato sulla azienda corrente.
     *
     * @return numero totale di records della tavola
     */
    public static int count() {
        return count(CompanySessionLib.getCompany());
    }// end of method

    /**
     * Recupera il totale dei records della Entity
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company croce di appartenenza
     * @return numero totale di records della tavola
     */
    public static int count(BaseCompany company) {
        int totRec = 0;
        long totTmp = CompanyQuery.getCount(Funzione.class, company);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method


    /**
     * Recupera una istanza di Funzione usando la query standard della Primary Key
     * Nessun filtro sulla azienda, perché la primary key è unica
     *
     * @param id valore (unico) della Primary Key
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
     * Filtrato sulla azienda corrente.
     *
     * @param sigla sigla di riferimento interna (obbligatoria)
     * @return istanza di Funzione, null se non trovata
     */
    public static Funzione findBySigla(String sigla) {
        Funzione instance = null;
        BaseEntity bean = CompanyQuery.queryOne(Funzione.class, Funzione_.sigla, sigla);

        if (bean != null && bean instanceof Funzione) {
            instance = (Funzione) bean;
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Funzione usando la query di una property specifica
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company croce di appartenenza
     * @param sigla   sigla di riferimento interna (obbligatoria)
     * @return istanza di Funzione, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Funzione findBySigla(BaseCompany company, String sigla) {
        Funzione instance = null;
        BaseEntity bean;

        EntityManager manager = EM.createEntityManager();
        bean = CompanyQuery.queryOne(Funzione.class, Funzione_.sigla, sigla, manager, company);
        manager.close();

        if (bean != null && bean instanceof Funzione) {
            instance = (Funzione) bean;
        }// end of if cycle

        return instance;
    }// end of method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla azienda corrente.
     *
     * @return lista di tutte le istanze di Funzione
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Funzione> findAll() {
        return findAll(CompanySessionLib.getCompany());
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla azienda passata come parametro.
     *
     * @return lista di tutte le istanze di Funzione
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Funzione> findAll(BaseCompany company) {
        return (ArrayList<Funzione>) CompanyQuery.getList(Funzione.class);
    }// end of method

    /**
     * Creazione iniziale di una funzione
     * La crea SOLO se non esiste già
     *
     * @param company croce di appartenenza
     * @param sigla   sigla di riferimento interna (obbligatoria)
     * @return istanza di Funzione
     */
    public static Funzione creaNew(WamCompany company, String sigla) {
        return creaNew(company, null, sigla, "", 0, "");
    }// end of static method

    /**
     * Creazione iniziale di una funzione
     * La crea SOLO se non esiste già
     *
     * @param company croce di appartenenza
     * @param manager the EntityManager to use
     * @param sigla   sigla di riferimento interna (obbligatoria)
     * @return istanza di Funzione
     */
    public static Funzione creaNew(WamCompany company, EntityManager manager, String sigla) {
        return creaNew(company, manager, sigla, "", 0, "");
    }// end of static method

    /**
     * Creazione iniziale di una funzione
     * La crea SOLO se non esiste già
     *
     * @param company     croce di appartenenza
     * @param manager     the EntityManager to use
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nelle liste
     * @param note        di spiegazione (facoltative)
     * @return istanza di Funzione
     */
    public static Funzione creaNew(WamCompany company, EntityManager manager, String sigla, String descrizione, int ordine, String note) {
        return creaNew(company, manager, sigla, descrizione, ordine, note, null);
    }// end of static method

    /**
     * Creazione iniziale di una funzione
     * La crea SOLO se non esiste già
     *
     * @param company     croce di appartenenza
     * @param manager     the EntityManager to use
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param ordine      di presentazione nelle liste
     * @param note        di spiegazione (facoltative)
     * @param glyph       dell'icona (facoltativo)
     * @return istanza di Funzione
     */
    public static Funzione creaNew(
            WamCompany company,
            EntityManager manager,
            String sigla,
            String descrizione,
            int ordine,
            String note,
            FontAwesome glyph) {
        Funzione funzione = Funzione.findBySigla(company, sigla);

        if (funzione == null) {
            funzione = new Funzione();
            funzione.setCompany(company);
            funzione.setSigla(sigla);
            funzione.setDescrizione(descrizione);
            funzione.setOrdine(ordine);
            funzione.setNote(note);
            funzione.setIcon(glyph);
            funzione.save(manager);
        }// end of if cycle

        return funzione;
    }// end of static method

    @PrePersist
    protected void prePersist() {
        if (getOrdine() == 0) {
            int max = WamQuery.queryMaxOrdineFunzione(null);
            setOrdine(max + 1);
        }
    }

    public List<ServizioFunzione> getServizioFunzioni() {
        return servizioFunzioni;
    }

    public void setServizioFunzioni(List<ServizioFunzione> servizioFunzioni) {
        this.servizioFunzioni = servizioFunzioni;
    }

    public List<VolontarioFunzione> getVolontarioFunzioni() {
        return volontarioFunzioni;
    }

    public void setVolontarioFunzioni(List<VolontarioFunzione> volontarioFunzioni) {
        this.volontarioFunzioni = volontarioFunzioni;
    }

    @Override
    public String toString() {
        return descrizione;
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

//    public byte[] getIcon() {
//        return icon;
//    }
//
//    public void setIcon(byte[] icon) {
//        this.icon = icon;
//    }


    // codepoint della icona FontAwesome
    public int getIconCodepoint() {
        return iconCodepoint;
    }

    public void setIconCodepoint(int iconCodepoint) {
        this.iconCodepoint = iconCodepoint;
    }

    /**
     * Recupera l'icona
     *
     * @return l'icona
     */
    public FontAwesome getIcon() {
        FontAwesome glyph = null;
        int codepoint = getIconCodepoint();
        try {
            glyph = FontAwesome.fromCodepoint(codepoint);
        } catch (Exception e) {

        }
        return glyph;
    }

    /**
     * Assegna una icona
     *
     * @param glyph l'icona FontAwesome
     */
    public void setIcon(FontAwesome glyph) {
        int codepoint = 0;
        if (glyph != null) {
            codepoint = glyph.getCodepoint();
        }
        setIconCodepoint(codepoint);
    }

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


    /**
     * Compara per ordine della funzione
     */
    @Override
    public int compareTo(Funzione other) {
        Integer ordQuesto = getOrdine();
        Integer ordAltro = other.getOrdine();
        return ordQuesto.compareTo(ordAltro);
    }


}// end of domain class
