package it.algos.wam.entity.servizio;

import com.vaadin.shared.ui.colorpicker.Color;
import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;


    @OneToMany(mappedBy = "servizio", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<Turno> turni = new ArrayList();

    // CascadeType.ALL: quando chiamo persist sul padre, persiste automaticamente tutti i nuovi figli aggiunti
    // alla lista e non ancora registrati (e così per tutte le operazioni dell'EntityManager)
    // orphanRemoval = true: quando registro il padre, cancella tutti i figli eventualmente rimasti orfani.
    // CascadeOnDelete: instaura l'integrità referenziale a livello di database (foreign key on delete cascade)
    @OneToMany(mappedBy = "servizio", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<ServizioFunzione> servizioFunzioni = new ArrayList();


    //--sigla di riferimento interna (obbligatoria)
    @NotEmpty
    @Column(length = 20)
    @Index
    private String sigla;


    //--descrizione per il tabellone (obbligatoria)
    @NotEmpty
    private String descrizione;

    //--ordine di presentazione nel tabellone
    @NotNull
    private int ordine = 0;

    // colore del servizio
    private int colore = new Color(128, 128, 128).getRGB();

//    //--durata del turno (in ore)
//    private int durata = 0;

    //--ora prevista (normale) di inizio turno
    private int oraInizio;

    //--minuti previsti (normali) di inizio turno
    //--nella GUI la scelta viene bloccata ai quarti d'ora
    private int minutiInizio = 0;

    //--ora prevista (normale) di fine turno
    private int oraFine;

    //--minuti previsti (normali) di fine turno
    //--nella GUI la scelta viene bloccata ai quarti d'ora
    private int minutiFine = 0;

    //--orario predefinito (avis, centralino ed extra non ce l'hanno)
    private boolean orario = true;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public Servizio() {
        this("", "");
    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     *
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     */
    public Servizio(String sigla, String descrizione) {
        this(0, sigla, descrizione, 0, 0);
    }// end of constructor


    /**
     * Costruttore completo
     *
     * @param ordine      di presentazione nel tabellone
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param oraInizio   del servizio (facoltativo)
     * @param oraFine     del servizio (facoltativo)
     */
    public Servizio(int ordine, String sigla, String descrizione, int oraInizio, int oraFine) {
        super();
        setOrdine(ordine);
        setSigla(sigla);
        setDescrizione(descrizione);
        setOraInizio(oraInizio);
        setOraFine(oraFine);
    }// end of constructor

    /**
     * Recupera una istanza di Servizio usando la query standard della Primary Key
     *
     * @param id valore della Primary Key
     * @return istanza di Servizio, null se non trovata
     */
    public static Servizio find(long id) {
        Servizio instance = null;
        BaseEntity entity = AQuery.queryById(Servizio.class, id);

        if (entity != null) {
            if (entity instanceof Servizio) {
                instance = (Servizio) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Servizio usando la query di una property specifica
     *
     * @param sigla valore della property Sigla
     * @return istanza di Servizio, null se non trovata
     * @deprecated perché manca la wamcompany e potrebbero esserci records multipli con la stessa sigla
     */
    public static Servizio findBySigla(String sigla) {
        Servizio instance = null;
        BaseEntity entity = AQuery.queryOne(Servizio.class, Servizio_.sigla, sigla);

        if (entity != null) {
            if (entity instanceof Servizio) {
                instance = (Servizio) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Servizio usando la query di tutte e sole le property obbligatorie
     *
     * @param company selezionata
     * @param sigla   valore della property Sigla
     * @return istanza di Servizio, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Servizio find(WamCompany company, String sigla) {
        Servizio instance = null;

        List<Servizio> serviziPerSigla = (List<Servizio>) AQuery.queryList(Servizio.class, Servizio_.sigla, sigla);
        if (serviziPerSigla != null && serviziPerSigla.size() > 0) {
            for (Servizio servizio : serviziPerSigla) {
                if (servizio.getCompany().equals(company)) {
                    instance = servizio;
                }
            }// end of for cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera il valore del numero totale di records della della Entity
     *
     * @return numero totale di records della tavola
     */
    public static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(Servizio.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     *
     * @return lista di tutte le istanze di Servizio
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Servizio> findAll() {
        return (ArrayList<Servizio>) AQuery.getList(Servizio.class);
    }// end of method

    /**
     * Creazione iniziale di un servizio
     * Lo crea SOLO se non esiste già
     *
     * @param company     selezionata
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @return istanza di Servizio
     */
    public static Servizio crea(WamCompany company, String sigla, String descrizione) {
        return crea(company, null, sigla, descrizione);
    }// end of static method

    /**
     * Creazione iniziale di un servizio
     * Lo crea SOLO se non esiste già
     *
     * @param company     selezionata
     * @param manager     the EntityManager to use
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @return istanza di Servizio
     */
    public static Servizio crea(WamCompany company, EntityManager manager, String sigla, String descrizione) {
        Servizio servizio = Servizio.find(company, sigla);

        if (servizio == null) {
            servizio = new Servizio(sigla, descrizione);
            servizio.save(manager);
        }// end of if cycle

        return servizio;
    }// end of static method


    /**
     * Creazione iniziale di un servizio
     * Lo crea SOLO se non esiste già
     *
     * @param company     selezionata
     * @param manager     the EntityManager to use
     * @param ordine      di presentazione nel tabellone
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param oraInizio   del servizio (facoltativo)
     * @param oraFine     del servizio (facoltativo)
     * @param orario      servizio ad orario prefissato e fisso ogni giorno
     * @param colore      del gruppo (facoltativo)
     * @return istanza di Servizio
     */
    public static Servizio crea(WamCompany company, EntityManager manager, int ordine, String sigla, String descrizione, int oraInizio, int oraFine, boolean orario, int colore, ArrayList<Funzione> listaFunz) {
        return crea(company, manager, ordine, sigla, descrizione, oraInizio, oraFine, orario, colore, listaFunz.toArray(new Funzione[listaFunz.size()]));
    }// end of static method


    /**
     * Creazione iniziale di un servizio
     * Lo crea SOLO se non esiste già
     *
     * @param company     selezionata
     * @param manager     the EntityManager to use
     * @param ordine      di presentazione nel tabellone
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param oraInizio   del servizio (facoltativo)
     * @param oraFine     del servizio (facoltativo)
     * @param orario      servizio ad orario prefissato e fisso ogni giorno
     * @param colore      del gruppo (facoltativo)
     * @param funzioni lista delle funzioni (facoltativa)
     * @return istanza di Servizio
     */
    public static Servizio crea(WamCompany company, EntityManager manager, int ordine, String sigla, String descrizione, int oraInizio, int oraFine, boolean orario, int colore, Funzione... funzioni) {
        Servizio servizio = Servizio.find(company, sigla);

        if (servizio == null) {
            servizio = new Servizio(ordine, sigla, descrizione, oraInizio, oraFine);
            servizio.setCompany(company);
            servizio.setOrario(orario);
            servizio.setColore(colore);

            if (funzioni != null) {
                for (Funzione funz : funzioni) {
                    servizio.servizioFunzioni.add(new ServizioFunzione(company, servizio, funz));
                } // fine del ciclo for-each
            }// fine del blocco if

            servizio = (Servizio) servizio.save(manager);
        }// end of if cycle

        return servizio;
    }// end of static method


    @PrePersist
    protected void prePersist() {
        if (getOrdine() == 0) {
            int max = WamQuery.queryMaxOrdineServizio(null);
            setOrdine(max + 1);
        }
    }

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
            if (currFun.getSiglaInterna().equals(f.getSiglaInterna())) {
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


    @Override
    public String toString() {
        return sigla;
    }// end of method


    /**
     * @return the nome
     */
    public String getSigla() {
        return sigla;
    }// end of getter method

    /**
     * @param sigla the sigla to set
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }// end of setter method

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
    }

    public void setColore(int colore) {
        this.colore = colore;
    }

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


    public boolean isOrario() {
        return orario;
    }// end of getter method

    public void setOrario(boolean orario) {
        this.orario = orario;
    }//end of setter method

    public List<ServizioFunzione> getServizioFunzioni() {
        return servizioFunzioni;
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


    public List<Turno> getTurni() {
        return turni;
    }

    public void setTurni(List<Turno> turni) {
        this.turni = turni;
    }

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

}// end of domain class
