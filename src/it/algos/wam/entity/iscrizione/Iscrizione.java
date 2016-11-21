package it.algos.wam.entity.iscrizione;

import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by gac on 28 feb 2016.
 * Entity che rappresenta una iscrizione di un volontario a un turno.
 * L'iscrizione è relativa a una certa funzione tra quelle previste nel servizio.
 */
@Entity
public class Iscrizione extends WamCompanyEntity {

    // codici modalità di controllo cancIscrizione turno
    public static final int MODE_CANC_NONE=0;   // nessun controllo
    public static final int MODE_CANC_POST=1;   // controllo minuti dopo iscrizione
    public static final int MODE_CANC_PRE=2;    // controllo ore prima di inizio turno

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // turno di riferimento
    @ManyToOne
    private Turno turno;

    // volontario di riferimento
    @OneToOne
    private Volontario volontario = null;

    // per quale funzione il volontario si iscrive
    @OneToOne
    private ServizioFunzione servizioFunzione = null;

    // timestamp di creazione.
    // (usato per bloccare la cancIscrizione dopo un determinato intervallo di tempo)
    private Timestamp tsCreazione = null;

    //--durata effettiva del turno del milite/volontario di questa iscrizione
    private int minutiEffettivi = 0;

    //--eventuali problemi di presenza del milite/volontario di questa iscrizione nel turno
    //--serve per evidenziare il problema nel tabellone
    private boolean esisteProblema = false;

    //--eventuale nota associata al milite/volontario
    //--serve per evidenziare il problema nel tabellone
    private String nota;

    // se è stata inviata la notifica di inizio turno dal sistema di notifiche automatiche
    private boolean notificaInviata;

    /**
     * Costruttore vuoto
     */
    public Iscrizione() {
        this(null, null, null);
    }// end of constructor

    /**
     * Costruttore con la funzione e il volontario
     *
     * @param turno      turno di riferimento
     * @param serFun     a quale funzione del servizio il volontario si iscrive
     * @param volontario milite/volontario assegnato alle funzione prevista per questa iscrizione (obbligatorio)
     */
    public Iscrizione(Turno turno, Volontario volontario, ServizioFunzione serFun) {
        this((WamCompany) CompanySessionLib.getCompany(), turno, volontario, serFun);
    }// end of constructor

    /**
     * Costruttore completo
     *
     * @param company    croce di appartenenza
     * @param turno      turno di riferimento
     * @param serFun     a quale funzione del servizio il volontario si iscrive
     * @param volontario milite/volontario assegnato alle funzione prevista per questa iscrizione (obbligatorio)
     */
    public Iscrizione(WamCompany company, Turno turno, Volontario volontario, ServizioFunzione serFun) {
        super();
        super.setCompany(company);
        setTurno(turno);
        setVolontario(volontario);
        setServizioFunzione(serFun);
        if (turno != null) {
            setMinutiEffettivi(turno.getMinutiTotali());    // quando viene creata ha lo stesso tempo del turno
        }
    }// end of constructor


    @PrePersist
    protected void prePersist() {
        tsCreazione=new Timestamp(System.currentTimeMillis());
    }




    /**
     * Recupera una istanza di Iscrizione usando la query di una property specifica
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company      croce di appartenenza
     * @param turno      turno di riferimento
     * @return istanza di Iscrizione, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Iscrizione findByTurno(WamCompany company, Turno turno) {
        Iscrizione instance = null;
        BaseEntity bean;

//        EntityManager manager = EM.createEntityManager();
        bean = CompanyQuery.getEntity(Iscrizione.class, Iscrizione_.turno ,turno);
//        manager.close();

        if (bean != null && bean instanceof Iscrizione) {
            instance = (Iscrizione) bean;
        }// end of if cycle

        return instance;
    }// end of method


    /**
     * Creazione iniziale di una iscrizione
     * La crea SOLO se non esiste già
     *
     * @param company    croce di appartenenza
     * @param manager    the EntityManager to use
     * @param turno      turno di riferimento
     * @param volontario milite/volontario assegnato alle funzione prevista per questa iscrizione (obbligatorio)
     * @param serFun     a quale funzione del servizio il volontario si iscrive
     *
     * @return istanza di Iscrizione
     */
    public static Iscrizione crea(WamCompany company, EntityManager manager,Turno turno, Volontario volontario, ServizioFunzione serFun) {
        Iscrizione isc = Iscrizione.findByTurno(company, turno);

        if (isc == null) {
            isc = new Iscrizione(company, turno, volontario,serFun);
            isc = (Iscrizione) isc.save(manager);
        }// end of if cycle

        return isc;
    }// end of static method



    public Volontario getVolontario() {
        return volontario;
    }// end of getter method

    public void setVolontario(Volontario milite) {
        this.volontario = milite;
    }//end of setter method

    public ServizioFunzione getServizioFunzione() {
        return servizioFunzione;
    }

    public void setServizioFunzione(ServizioFunzione servizioFunzione) {
        this.servizioFunzione = servizioFunzione;
    }

    public Timestamp getTsCreazione() {
        return tsCreazione;
    }

    public void setTsCreazione(Timestamp ts) {
        this.tsCreazione = ts;
    }

    public int getMinutiEffettivi() {
        return minutiEffettivi;
    }// end of getter method

    public void setMinutiEffettivi(int oreEffettive) {
        this.minutiEffettivi = oreEffettive;
    }//end of setter method

    public boolean isEsisteProblema() {
        return esisteProblema;
    }// end of getter method

    public void setEsisteProblema(boolean esisteProblema) {
        this.esisteProblema = esisteProblema;
    }//end of setter method

    public String getNota() {
        return nota;
    }// end of getter method

    public void setNota(String nota) {
        this.nota = nota;
    }//end of setter method

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    /**
     * Ritorna true se ha una nota
     */
    public boolean hasNota() {
        return (getNota() != null && !getNota().isEmpty());
    }

    public boolean isNotificaInviata() {
        return notificaInviata;
    }

    public void setNotificaInviata(boolean notificaInviata) {
        this.notificaInviata = notificaInviata;
    }
}// end of class
