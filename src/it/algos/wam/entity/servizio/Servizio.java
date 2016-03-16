package it.algos.wam.entity.servizio;

import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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

    private static final long serialVersionUID = 1L;

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

    //--durata del turno (in ore)
    private int durata = 0;

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

    //--ultimo turno di un eventuale raggruppamento a video (nel tabellone)
    //boolean ultimo = false

    //--Primo turno di un eventuale raggruppamento a video (nel tabellone)
    private boolean primo = false;

    //--turno a cavallo della mezzanotte - termina il giorno successivo
    private boolean fineGiornoSuccessivo = false;

    //--visibilità nel tabellone
    private boolean visibile = true;

    //--orario predefinito (avis, centralino ed extra non ce l'hanno)
    private boolean orario = true;

    //--possibilità di occorrenze multiple (extra)
    private boolean multiplo = false;

    //--numero di volntari/militi/funzioni obbligatorie
    private int persone = 0;

    //--elenco delle funzioni previste per questo tipo di turno
    //--massimo hardcoded di 4
//    private WrapServizio wrapServizio = null;

    @OneToMany(mappedBy = "servizio")
    @CascadeOnDelete
    private List<ServizioFunzione> servizioFunzioni = new ArrayList();

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
        this(0, sigla, descrizione, 0, 0, 0);
    }// end of constructor


    /**
     * Costruttore completo
     *
     * @param ordine      di presentazione nel tabellone
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param oraInizio   del servizio (facoltativo)
     * @param oraFine     del servizio (facoltativo)
     * @param persone     minime indispensabile allo svolgimento del servizio
     */
    public Servizio(int ordine, String sigla, String descrizione, int oraInizio, int oraFine, int persone) {
        super();
        setOrdine(ordine);
        setSigla(sigla);
        setDescrizione(descrizione);
        setOraInizio(oraInizio);
        setOraFine(oraFine);
        setPersone(persone);
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
     * @param sigla valore della property Sigla
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
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @return istanza di Servizio
     */
    public static Servizio crea(WamCompany company, String sigla, String descrizione) {
        Servizio servizio = Servizio.find(company, sigla);

        if (servizio == null) {
            servizio = new Servizio(sigla, descrizione);
            servizio.save();
        }// end of if cycle

        return servizio;
    }// end of static method


    /**
     * Creazione iniziale di un servizio
     * Lo crea SOLO se non esiste già
     *
     * @param ordine      di presentazione nel tabellone
     * @param sigla       sigla di riferimento interna (obbligatoria)
     * @param descrizione per il tabellone (obbligatoria)
     * @param oraInizio   del servizio (facoltativo)
     * @param oraFine     del servizio (facoltativo)
     * @param visibile    nel tabellone
     * @param orario      servizio ad orario prefissato e fisso ogni giorno
     * @param multiplo    servizio suscettibile di essere effettuato diverse volte nella giornata
     * @param persone     minime indispensabile allo svolgimento del servizio
     * @return istanza di Servizio
     */
    public static Servizio crea(WamCompany company, int ordine, String sigla, String descrizione, int oraInizio, int oraFine, boolean visibile, boolean orario, boolean multiplo, int persone) {
        Servizio servizio = Servizio.find(company, sigla);

        if (servizio == null) {
            servizio = new Servizio(ordine, sigla, descrizione, oraInizio, oraFine, persone);
            servizio.setCompany(company);
            servizio.setDurata(Math.abs(oraFine - oraInizio));
            servizio.setVisibile(visibile);
            servizio.setOrario(orario);
            servizio.setMultiplo(multiplo);
            servizio.save();
        }// end of if cycle

        return servizio;
    }// end of static method


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
    public ArrayList<Funzione> getFunzioniObbligatorie() {
        ArrayList<Funzione> lista = new ArrayList<>();

        for (ServizioFunzione serFun : servizioFunzioni) {
            if (serFun.isObbligatoria()) {
                lista.add(serFun.getFunzione());
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


//    public ArrayList<String> getSigleFunzioni() {
//        ArrayList<String> lista = null;
//
//        if (wrapServizio != null) {
//            lista = wrapServizio.getSigleFunzioni();
//        }// end of if cycle
//
//        return lista;
//    }// end of method

    @Override
    public String toString() {
        return sigla;
    }// end of method

    /**
     * @return intervallo fine - inizio
     */
    public String getOrario() {
        String orario = "";

        if (oraInizio > 0 && oraFine > 0) {
            orario = oraInizio + "-" + oraFine;
        }// end of if cycle

        return orario;
    }// end of getter method

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

    public int getDurata() {
        return durata;
    }// end of getter method

    public void setDurata(int durata) {
        this.durata = durata;
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

    public boolean isPrimo() {
        return primo;
    }// end of getter method

    public void setPrimo(boolean primo) {
        this.primo = primo;
    }//end of setter method

    public boolean isFineGiornoSuccessivo() {
        return fineGiornoSuccessivo;
    }// end of getter method

    public void setFineGiornoSuccessivo(boolean fineGiornoSuccessivo) {
        this.fineGiornoSuccessivo = fineGiornoSuccessivo;
    }//end of setter method

    public boolean isVisibile() {
        return visibile;
    }// end of getter method

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }//end of setter method

    public boolean isOrario() {
        return orario;
    }// end of getter method

    public void setOrario(boolean orario) {
        this.orario = orario;
    }//end of setter method

    public boolean isMultiplo() {
        return multiplo;
    }// end of getter method

    public void setMultiplo(boolean multiplo) {
        this.multiplo = multiplo;
    }//end of setter method

    public int getPersone() {
        return persone;
    }// end of getter method

    public void setPersone(int funzioniObbligatorie) {
        this.persone = funzioniObbligatorie;
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


    /**
     * @return true se questo servizio prevede orari variabili, quindi nel tabellone
     * non appare l'orario in corrispondenza del servizio, ma appare un titolo sopra ogni turno
     */
    public boolean isOrarioVariabile() {
        return isMultiplo();    //?? alex
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
