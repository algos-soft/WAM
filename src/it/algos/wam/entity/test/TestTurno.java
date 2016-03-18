package it.algos.wam.entity.test;

import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.lib.LibWam;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.lib.DateConvertUtils;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity per fare dei test sulle relazioni.
 */
@Entity
public class TestTurno extends WamCompanyEntity {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // servizio di riferimento
    @ManyToOne
    private Servizio servizio;

    // iscrizioni dei volontari a questo turno
    @OneToMany(mappedBy = "turno", cascade=CascadeType.ALL)
    @CascadeOnDelete
    private List<TestIscrizione> iscrizioni = new ArrayList();


    //--chiave indicizzata per query più veloci e 'mirate' (obbligatoria)
    //--annoX1000 + giorno nell'anno
    @NotNull
    @Index
    private long chiave;

    //--giorno, ora e minuto di inizio turno
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date inizio;

    //--giorno, ora e minuto di fine turno
    //--i servizi senza orario (fisso) vengono creati solo con la data di inizio; la data di fine viene aggiunata dopo
    @Temporal(TemporalType.TIMESTAMP)
    private Date fine;



    //--motivazione del turno extra
    private String titoloExtra;
    //--nome evidenziato della località per turni extra
    private String localitàExtra;
    //--descrizione dei viaggi extra
    private String note;

    //--turno previsto (vuoto) oppure assegnato (militi inseriti)
    private boolean assegnato = false;

    /**
     * Costruttore vuoto
     */
    public TestTurno() {
        this(null, null, null, false);
    }// end of constructor

    /**
     * Costruttore completo
     *
     * @param servizio  tipologia di servizio (obbligatoria)
     * @param inizio    giorno, ora e minuto di inizio turno
     * @param fine      giorno, ora e minuto di fine turno
     * @param assegnato turno previsto (vuoto) oppure assegnato (militi inseriti)
     */
    public TestTurno(Servizio servizio, Date inizio, Date fine, boolean assegnato) {
        super();
        setServizio(servizio);
        setInizio(inizio);
        setFine(fine);
        setAssegnato(assegnato);
    }// end of constructor


    public void setIscrizioni(List<TestIscrizione> iscrizioni) {
        this.iscrizioni = iscrizioni;
    }




    /**
     * Recupera una istanza di TestTurno usando la query standard della Primary Key
     *
     * @param id valore della Primary Key
     * @return istanza di TestTurno, null se non trovata
     */
    public static TestTurno find(long id) {
        TestTurno instance = null;
        BaseEntity entity = AQuery.queryById(TestTurno.class, id);

        if (entity != null) {
            if (entity instanceof TestTurno) {
                instance = (TestTurno) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una lista di Turni usando la chiave specifica
     *
     * @param chiave indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @return lista di Turni, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<TestTurno> findAllChiave(int chiave) {

        BaseCompany company = CompanySessionLib.getCompany();

        ArrayList<TestTurno> lista = null;
        List<TestTurno> listaPerChiave = (List<TestTurno>) CompanyQuery.queryList(TestTurno.class, Turno_.chiave, chiave);

        if (listaPerChiave != null && listaPerChiave.size() > 0) {
            lista = new ArrayList();
            for (TestTurno turno : listaPerChiave) {
                if (turno.getCompany().getId().equals(company.getId())) {
                    lista.add(turno);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method

    /**
     * Recupera una lista di Turni usando la query di tutte e sole le property obbligatorie
     * Se il servizio è multiplo, ce ne possono essere diversi al giorno (per wamcompany)
     * Se il servizio NON è multiplo, conviene usare la chiamata find (stessi parametri)
     *
     * @param servizio tipologia di servizio (obbligatoria)
     * @param chiave   indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @return istanza di Servizio, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<TestTurno> findAll(Servizio servizio, int chiave) {
        ArrayList<TestTurno> lista = null;

        if (servizio == null || chiave == 0) {
            return null;
        }// end of if cycle

        lista = findAllChiave(chiave);

        return lista;
    }// end of method

    /**
     * Recupera una lista di Turni usando la query di tutte e sole le property obbligatorie
     * Se il servizio è multiplo, ce ne possono essere diversi al giorno (per wamcompany)
     * Se il servizio NON è multiplo, conviene usare la chiamata find (stessi parametri)
     *
     * @param company  croce di appartenenza
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     * @return istanza di Servizio, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<TestTurno> findAll(WamCompany company, Servizio servizio, Date inizio) {
        return findAll(servizio, LibWam.creaChiave(inizio));
    }// end of method

    /**
     * Recupera una istanza di TestTurno usando la chiave specifica
     *
     * @param company croce di appartenenza
     * @param chiave  indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @return istanza di TestTurno, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static TestTurno findChiave(WamCompany company, int chiave) {
        TestTurno instance = null;
        ArrayList<TestTurno> lista = findAllChiave(chiave);

        if (lista != null && lista.size() == 1) {
            instance = lista.get(0);
        }// end of if cycle

        return instance;
    }// end of static method

    /**
     * Recupera una istanza di TestTurno usando la query di tutte e sole le property obbligatorie
     * Se il servizio NON è multiplo, ce ne deve essere SOLO UNO al giorno (per wamcompany)
     * Se il servizio è multiplo, usare la chiamata findAll (stessi parametri)
     *
     * @param servizio tipologia di servizio (obbligatoria)
     * @param chiave   indicizzata per query più veloci e 'mirate' (obbligatoria)
     * @return istanza di Servizio, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static TestTurno find(Servizio servizio, int chiave) {
        TestTurno instance = null;
        ArrayList<TestTurno> listaTurni = null;
        Servizio servizioTmp = null;

        if (servizio == null || chiave == 0) {
            return null;
        }// end of if cycle

        if (servizio.isMultiplo()) {
            return null;//@todo per ora
        } else {
            listaTurni = findAll(servizio, chiave);
            if (listaTurni != null && listaTurni.size() > 0) {
                for (TestTurno turno : listaTurni) {
                    servizioTmp = turno.getServizio();
                    if (servizioTmp != null && servizioTmp.getId().equals(servizio.getId())) {
                        instance = turno;
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of if/else cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di TestTurno usando la query di tutte e sole le property obbligatorie
     * Se il servizio NON è multiplo, ce ne deve essere SOLO UNO al giorno (per wamcompany)
     * Se il servizio è multiplo, usare la chiamata findAll (stessi parametri)
     *
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     * @return istanza di Servizio, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static TestTurno find(Servizio servizio, Date inizio) {
        return find(servizio, LibWam.creaChiave(inizio));
    }// end of method

    /**
     * Recupera il valore del numero totale di records della della Entity
     *
     * @return numero totale di records della tavola
     */
    public static int count() {
        int totRec = 0;
        long totTmp = CompanyQuery.getCount(TestTurno.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     *
     * @return lista di tutte le istanze di TestTurno
     */
    @SuppressWarnings("unchecked")
    public static List<TestTurno> findAll() {
        return (ArrayList<TestTurno>) CompanyQuery.getList(TestTurno.class);
    }// end of method

    /**
     * Creazione iniziale di un turno
     * Lo crea SOLO se non esiste
     *
     * @param servizio tipologia di servizio (obbligatoria)
     * @param inizio   giorno, ora e minuto di inizio turno
     * @return istanza di turno
     */
    public static TestTurno crea(WamCompany company, Servizio servizio, Date inizio) {
        return crea(company, servizio, inizio, null, false);
    }// end of static method

    /**
     * Creazione iniziale di un turno
     * Lo crea SOLO se non esiste
     *
     * @param servizio  tipologia di servizio (obbligatoria)
     * @param inizio    giorno, ora e minuto di inizio turno
     * @param fine      giorno, ora e minuto di fine turno
     * @param assegnato turno previsto (vuoto) oppure assegnato (militi inseriti)
     * @return istanza di turno
     */
    public static TestTurno crea(WamCompany company, Servizio servizio, Date inizio, Date fine, boolean assegnato) {
        TestTurno turno = null;

        if (servizio == null || inizio == null) {
            return null;
        }// end of if cycle

        if (!servizio.isMultiplo()) {
            turno = TestTurno.find(servizio, inizio);
        }// end of if cycle

        if (turno == null) {
            if (servizio != null && servizio.isOrario()) {
                fine = inizio;
            }// end of if cycle

            turno = new TestTurno(servizio, inizio, fine, assegnato);
            turno.setCompany(company);
            turno.save();
        }// end of if cycle

        return turno;
    }// end of static method

    public void add(TestIscrizione iscrizione) {
//        TurnoIscrizione tunIsc = null;

//        if (getCompany() == null) {
//            Exception e = new Exception("Impossibile aggiungere iscrizioni al turno se manca la company");
//            e.printStackTrace();
//            return;
//        }// end of if cycle

        iscrizioni.add(iscrizione);

//        tunIsc = new TurnoIscrizione(this, iscrizione);
//        tunIsc.setCompany(getCompany());


    }// end of method

    /**
     * Costruisce (od aggiorna) la chiave di ricerca indicizzata
     */
    @PrePersist
    public void fixChiave() {
        chiave = LibWam.creaChiave(inizio);
    }// end of method

    @Override
    public String toString() {
        return getServizio() + "/" + getInizio();
    }// end of method

    public long getChiave() {
        return chiave;
    }// end of getter method

    public void setChiave(long chiave) {
        this.chiave = chiave;
    }//end of setter method

    public Servizio getServizio() {
        return servizio;
    }// end of getter method

    public void setServizio(Servizio servizio) {
        this.servizio = servizio;
    }//end of setter method

    public Date getInizio() {
        return inizio;
    }// end of getter method

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }//end of setter method

    /**
     * Ritorna la data iniziale come LocalDate
     */
    public LocalDate getData1() {
        LocalDate d = null;
        if (getInizio() != null) {
            d = DateConvertUtils.asLocalDate(getInizio());
        }
        return d;
    }// end of getter method

    public Date getFine() {
        return fine;
    }// end of getter method

    public void setFine(Date fine) {
        this.fine = fine;
    }//end of setter method


//    public List<TurnoIscrizione> getTurnoIscrizioni() {
//        return turnoIscrizioni;
//    }// end of getter method

//    public void setTurnoIscrizioni(List<TurnoIscrizione> turnoIscrizioni) {
//        this.turnoIscrizioni = turnoIscrizioni;
//    }//end of setter method


    public String getTitoloExtra() {
        return titoloExtra;
    }// end of getter method

    public void setTitoloExtra(String titoloExtra) {
        this.titoloExtra = titoloExtra;
    }//end of setter method

    public String getLocalitàExtra() {
        return localitàExtra;
    }// end of getter method

    public void setLocalitàExtra(String localitàExtra) {
        this.localitàExtra = localitàExtra;
    }//end of setter method

    public String getNote() {
        return note;
    }// end of getter method

    public void setNote(String note) {
        this.note = note;
    }//end of setter method

    public boolean isAssegnato() {
        return assegnato;
    }// end of getter method

    public void setAssegnato(boolean assegnato) {
        this.assegnato = assegnato;
    }//end of setter method


    /**
     * Controlla se questo turno ha le iscrizioni coperte per tutte le funzioni
     * obbligatorie dichiarate dal relativo Servizio.
     *
     * @return true se ha le iscrizioni.
     */
    public boolean isValido() {
        boolean valido = true;
        List<ServizioFunzione> funzioni = getServizio().getFunzioniObbligatorie();
        for (ServizioFunzione sf : funzioni) {
            if (getIscrizione(sf) == null) {
                valido = false;
                break;
            }
        }
        return valido;
    }

    /**
     * Controlla se questo turno ha le iscrizioni coperte per tutte le funzioni
     * (obbligatorie e non) dichiarate dal relativo Servizio
     *
     * @return true se ha le iscrizioni.
     */
    public boolean isCompleto() {
        boolean completo = true;
        List<ServizioFunzione> lista = getServizio().getServizioFunzioni();
        for (ServizioFunzione sf : lista) {
            if (getIscrizione(sf) == null) {
                completo = false;
                break;
            }
        }
        return completo;
    }


    public List<TestIscrizione> getIscrizioni() {
        return iscrizioni;
    }


    public void setIscrizioni(ArrayList<TestIscrizione> iscrizioni) {
        this.iscrizioni=iscrizioni;
    }



    /**
     * Recupera la eventuale iscrizione a una data funzione.
     *
     * @param sf il ServizioFunzione
     * @return l'iscrizione
     */
    public TestIscrizione getIscrizione(ServizioFunzione sf) {
        TestIscrizione iscrizione = null;
        for (TestIscrizione i : getIscrizioni()) {
            ServizioFunzione s = i.getServizioFunzione();
            if(s!=null){
                if (s.equals(sf)) {
                    iscrizione = i;
                    break;
                }
            }
        }
        return iscrizione;
    }



    @Override
    @SuppressWarnings("all")
    public TestTurno clone() throws CloneNotSupportedException {
        try {
            return (TestTurno) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of class
