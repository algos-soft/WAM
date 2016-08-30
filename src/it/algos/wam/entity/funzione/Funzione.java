package it.algos.wam.entity.funzione;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyEntity_;
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

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;


    @NotEmpty
    @Column(length = 20)
    @Index
    private String sigla = "";


//    //--descrizione visibile nel tabellone (obbligatoria)
//    @NotEmpty
//    @Column(length = 100)
//    @Index
//    private String siglaVisibile = "";


    //--ordine di presentazione nelle liste
    @Index
    private int ordine = 0;


    //--codepoint dell'icona di FontAwesome (facoltative)
    private int iconCodepoint;


    @Column(columnDefinition = "text")
    private String descrizione = "";


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
     * @param company croce di appartenenza (property della superclasse)
     * @param sigla   sigla di riferimento interna (obbligatoria)
     */
    public Funzione(BaseCompany company, String sigla) {
        this(company, sigla, 0, null, "");
    }// end of constructor


    /**
     * Costruttore completo
     *
     * @param company     croce di appartenenza
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param ordine      di presentazione nelle liste
     * @param glyph       icona di FontAwesome (facoltative)
     * @param descrizione note (facoltative)
     */
    public Funzione(BaseCompany company, String sigla, int ordine, FontAwesome glyph, String descrizione) {
        super();
        this.setCompany(company);
        this.setSigla(sigla);
//        this.setSiglaVisibile(siglaVisibile);
        this.setOrdine(ordine);
        this.setIcon(glyph);
        this.setDescrizione(descrizione);
    }// end of constructor

    /**
     * Recupera il totale dei records della Entity
     * Filtrato sulla azienda corrente.
     *
     * @return numero totale di records nella Entity
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
     */
    public static int count(WamCompany company) {
        int totRec = 0;
        long totTmp = CompanyQuery.getCount(Funzione.class, company);

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
     */
    public static int countAll() {
        return count((WamCompany) null);
    }// end of method


    /**
     * Recupera una istanza di Funzione usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
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
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company corrente.
     *
     * @return lista di tutte le istanze di Funzione
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Funzione> findAll() {
        return (ArrayList<Funzione>) CompanyQuery.getList(Funzione.class);
    }// end of method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Filtrato sulla company passata come parametro.
     *
     * @param company croce di appartenenza
     * @return lista di tutte le istanze di Funzione
     */
    @SuppressWarnings("unchecked")
    public static List<Funzione> findAll(WamCompany company) {
        List<Funzione> lista;

//        Container.Filter filter = new Compare.Equal(CompanyEntity_.company.getName(), company);
        lista = (List<Funzione>) AQuery.findAll(Funzione.class, CompanyEntity_.company, company);

        return lista;
    }// end of method


    /**
     * Recupera una lista (array) di tutti i records della Entity
     * Senza filtri.
     *
     * @return lista di tutte le istanze di Funzione
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Funzione> findAllAll() {
        return (ArrayList<Funzione>) AQuery.findAll(Funzione.class, null);
    }// end of method


    /**
     * Recupera una istanza di Funzione usando la query di una property specifica
     * Filtrato sulla azienda corrente.
     *
     * @param siglaInterna sigla di riferimento interna (obbligatoria)
     * @return istanza di Funzione, null se non trovata
     */
    public static Funzione findBySigla(String siglaInterna) {
        Funzione instance = null;
        BaseEntity bean = CompanyQuery.queryOne(Funzione.class, Funzione_.sigla, siglaInterna);

        if (bean != null && bean instanceof Funzione) {
            instance = (Funzione) bean;
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Funzione usando la query di una property specifica
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company      croce di appartenenza
     * @param siglaInterna sigla di riferimento interna (obbligatoria)
     * @return istanza di Funzione, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Funzione findBySigla(WamCompany company, String siglaInterna) {
        Funzione instance = null;
        BaseEntity bean;

        EntityManager manager = EM.createEntityManager();
        bean = CompanyQuery.queryOne(Funzione.class, Funzione_.sigla, siglaInterna, manager, company);
        manager.close();

        if (bean != null && bean instanceof Funzione) {
            instance = (Funzione) bean;
        }// end of if cycle

        return instance;
    }// end of method


    /**
     * Creazione iniziale di una funzione
     * La crea SOLO se non esiste già
     *
     * @param company      croce di appartenenza
     * @param siglaInterna sigla di riferimento interna (obbligatoria)
     * @return istanza di Funzione
     */
    public static Funzione crea(WamCompany company, String siglaInterna) {
        return crea(company, null, siglaInterna, 0, "");
    }// end of static method

    /**
     * Creazione iniziale di una funzione
     * La crea SOLO se non esiste già
     *
     * @param company      croce di appartenenza
     * @param manager      the EntityManager to use
     * @param siglaInterna sigla di riferimento interna (obbligatoria)
     * @return istanza di Funzione
     */
    public static Funzione crea(WamCompany company, EntityManager manager, String siglaInterna) {
        return crea(company, manager, siglaInterna, 0, "");
    }// end of static method

    /**
     * Creazione iniziale di una funzione
     * La crea SOLO se non esiste già
     *
     * @param company      croce di appartenenza
     * @param manager      the EntityManager to use
     * @param siglaInterna sigla di riferimento interna (obbligatoria)
     * @param ordine       di presentazione nelle liste
     * @param note         di spiegazione (facoltative)
     * @return istanza di Funzione
     */
    public static Funzione crea(WamCompany company, EntityManager manager, String siglaInterna, int ordine, String note) {
        return crea(company, manager, siglaInterna, ordine, note, null);
    }// end of static method

    /**
     * Creazione iniziale di una funzione
     * La crea SOLO se non esiste già
     *
     * @param company      croce di appartenenza
     * @param manager      the EntityManager to use
     * @param siglaInterna sigla di riferimento interna (obbligatoria)
     * @param ordine       di presentazione nelle liste
     * @param note         di spiegazione (facoltative)
     * @param glyph        dell'icona (facoltativo)
     * @return istanza di Funzione
     */
    public static Funzione crea(
            WamCompany company,
            EntityManager manager,
            String siglaInterna,
            int ordine,
            String note,
            FontAwesome glyph) {
        Funzione funzione = Funzione.findBySigla(company, siglaInterna);

        if (funzione == null) {
            funzione = new Funzione();
            funzione.setCompany(company);
            funzione.setSigla(siglaInterna);
            funzione.setOrdine(ordine);
            funzione.setDescrizione(note);
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

//    @Override
//    public String toString() {
//        return siglaVisibile;
//    }// end of method

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

    public int getOrdine() {
        return ordine;
    }// end of getter method

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }//end of setter method

//    public String getSiglaVisibile() {
//        return siglaVisibile;
//    }// end of getter method

//    public void setSiglaVisibile(String descrizione) {
//        this.siglaVisibile = descrizione;
//    }//end of setter method

    public String getDescrizione() {
        return descrizione;
    }// end of getter method

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }//end of setter method


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
        }
        if (glyph != null) {
            iconHtml = glyph.getHtml();
        }// end of if cycle

        return iconHtml;
    }// end of inner method

}// end of domain class
