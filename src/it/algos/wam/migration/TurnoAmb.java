package it.algos.wam.migration;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.lib.LibCookie;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by gac on 11 ott 2016.
 * <p>
 * Entity per un turno
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "Turno")
@Access(AccessType.PROPERTY)
@ReadOnly
public class TurnoAmb extends BaseEntity {
    @ManyToOne
    private CroceAmb croce;


    //--tipologia del turno
    @ManyToOne
    private ServizioAmb tipo_turno;
    //--giorno di svolgimento del turno (giorno iniziale se termina il mattino dopo)
    //--ore e minuti sono sempre a zero
    private Date giorno;
    //--giorno, ora e minuto di inizio turno
    private Date inizio;
    //--giorno, ora e minuto di fine turno
    private Date fine;
    //--numero variabile di funzioni previste per il tipo di turno
    //--massimo hardcoded di 4
    @ManyToOne
    private FunzioneAmb funzione1;
    @ManyToOne
    private FunzioneAmb funzione2;
    @ManyToOne
    private FunzioneAmb funzione3;
    @ManyToOne
    private FunzioneAmb funzione4;
    //--numero variabile di militi assegnati alle funzioni previste per il tipo di turno
    //--massimo hardcoded di 4
    @ManyToOne
    private VolontarioAmb milite_funzione1 = null;
    @ManyToOne
    private VolontarioAmb milite_funzione2 = null;
    @ManyToOne
    private VolontarioAmb milite_funzione3 = null;
    @ManyToOne
    private VolontarioAmb milite_funzione4 = null;
    //--ultima modifica effettuata per le funzioni previste per il tipo di turno
    //--massimo hardcoded di 4
    //--serve per bloccare le modifiche dopo un determinatpo intervallo di tempo
    private Timestamp modifica_funzione1 = null;
    private Timestamp modifica_funzione2 = null;
    private Timestamp modifica_funzione3 = null;
    private Timestamp modifica_funzione4 = null;
    //--durata del turno per ogni milite
    //--massimo hardcoded di 4
    private int ore_milite1 = 0;
    private int ore_milite2 = 0;
    private int ore_milite3 = 0;
    private int ore_milite4 = 0;
    //--eventuali problemi di presenza del milite nel turno
    //--serve per evidenziare il problema nel tabellone
    //--massimo hardcoded di 4
    private boolean problemi_funzione1 = false;
    private boolean problemi_funzione2 = false;
    private boolean problemi_funzione3 = false;
    private boolean problemi_funzione4 = false;
    //--motivazione del turno
    private String titolo_extra = "";
    //--nome evidenziato della località
    private String località_extra = "";
    //--descrizione dei viaggi extra
    private String note = "";
    //--turno previsto (vuoto) oppure assegnato (militi inseriti)
    private boolean assegnato = false;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public TurnoAmb() {
    }// end of constructor


    public static TurnoAmb find(long id, EntityManager manager) {
        if (manager != null) {
            return (TurnoAmb) AQuery.find(TurnoAmb.class, id, manager);
        }// end of if cycle
        return null;
    }// end of static method

    /**
     * Recupera una lista di tutti i records della Entity
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza
     * @return lista delle istanze filtrate della Entity
     */
    @SuppressWarnings("unchecked")
    public static List<TurnoAmb> findAll(CroceAmb company, EntityManager manager) {
        return (List<TurnoAmb>) AQuery.getList(TurnoAmb.class, TurnoAmb_.croce, company, manager);
    }// end of method

    /**
     * Recupera una lista di tutti i records della Entity
     * Filtrato sulla company passata come parametro.
     * <p>
     * Recupera tutti i turni a partire da 7 (sette) giorni prima delkla data attuale
     *
     * @param company di appartenenza
     * @return lista delle istanze filtrate della Entity
     */
    @SuppressWarnings("unchecked")
    public static List<TurnoAmb> findAllRecenti(CroceAmb company, EntityManager manager) {
        List lista;
        int delta = 10;
        Date oggi = new Date();
        Date dataIniziale = LibDate.add(oggi, -delta);
        dataIniziale= LibDate.getPrimoGennaio(2017);

        Container.Filter filtroCroce = new Compare.Equal(TurnoAmb_.croce.getName(), company);
        Container.Filter filtroData = new Compare.Greater(TurnoAmb_.giorno.getName(), dataIniziale);
        lista = AQuery.getList(TurnoAmb.class, manager, filtroCroce, filtroData);

        return lista;
    }// end of method

    @SuppressWarnings("unchecked")
    public static List<TurnoAmb> findAllByDateEsatta(CroceAmb company, Date giornoIniziale, EntityManager manager) {
        Container.Filter filtroCroce = new Compare.Equal(TurnoAmb_.croce.getName(), company);
        Container.Filter filtroGiorno = new Compare.Equal(TurnoAmb_.giorno.getName(), giornoIniziale);
        return (List<TurnoAmb>) AQuery.getList(TurnoAmb.class, manager, filtroCroce, filtroGiorno);
    }// end of method

    @SuppressWarnings("unchecked")
    public static List<TurnoAmb> findAllByOre(CroceAmb company, int ore, EntityManager manager) {
        Container.Filter filtroCroce = new Compare.Equal(TurnoAmb_.croce.getName(), company);
        Container.Filter filtroOre = new Compare.Equal(TurnoAmb_.ore_milite1.getName(), ore);
        return (List<TurnoAmb>) AQuery.getList(TurnoAmb.class, manager, filtroCroce, filtroOre);
    }// end of method

    @SuppressWarnings("unchecked")
    public static List<TurnoAmb> findAllByGreaterID(CroceAmb company, long id, EntityManager manager) {
        Container.Filter filtroCroce = new Compare.Equal(TurnoAmb_.croce.getName(), company);
        Container.Filter filtroOre = new Compare.Greater(BaseEntity_.id.getName(), id);
        return (List<TurnoAmb>) AQuery.getList(TurnoAmb.class, manager, filtroCroce, filtroOre);
    }// end of method

    @SuppressWarnings("unchecked")
    public static boolean isEsisteByMilite( VolontarioAmb milite, EntityManager manager) {
        boolean esiste = false;
        List lista1= AQuery.getList(TurnoAmb.class, TurnoAmb_.milite_funzione1, milite, manager);
        List lista2= AQuery.getList(TurnoAmb.class, TurnoAmb_.milite_funzione2, milite, manager);
        List lista3= AQuery.getList(TurnoAmb.class, TurnoAmb_.milite_funzione3, milite, manager);
        List lista4= AQuery.getList(TurnoAmb.class, TurnoAmb_.milite_funzione4, milite, manager);

        if (lista1.size() > 0) {
            esiste = true;
        }// end of if cycle

        if (AQuery.getList(TurnoAmb.class, TurnoAmb_.milite_funzione2, milite, manager).size() > 0) {
            esiste = true;
        }// end of if cycle

        if (AQuery.getList(TurnoAmb.class, TurnoAmb_.milite_funzione3, milite, manager).size() > 0) {
            esiste = true;
        }// end of if cycle

        if (AQuery.getList(TurnoAmb.class, TurnoAmb_.milite_funzione4, milite, manager).size() > 0) {
            esiste = true;
        }// end of if cycle

        return esiste;
    }// end of method

    public CroceAmb getCroce() {
        return croce;
    }// end of getter method

    public void setCroce(CroceAmb croce) {
        this.croce = croce;
    }//end of setter method

    public ServizioAmb getTipo_turno() {

        return tipo_turno;
    }// end of getter method

    public void setTipo_turno(ServizioAmb tipo_turno) {
        this.tipo_turno = tipo_turno;
    }//end of setter method

    public Date getGiorno() {
        return giorno;
    }// end of getter method

    public void setGiorno(Date giorno) {
        this.giorno = giorno;
    }//end of setter method

    public Date getInizio() {
        return inizio;
    }// end of getter method

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }//end of setter method

    public Date getFine() {
        return fine;
    }// end of getter method

    public void setFine(Date fine) {
        this.fine = fine;
    }//end of setter method

    public FunzioneAmb getFunzione1() {
        return funzione1;
    }// end of getter method

    public void setFunzione1(FunzioneAmb funzione1) {
        this.funzione1 = funzione1;
    }//end of setter method

    public FunzioneAmb getFunzione2() {
        return funzione2;
    }// end of getter method

    public void setFunzione2(FunzioneAmb funzione2) {
        this.funzione2 = funzione2;
    }//end of setter method

    public FunzioneAmb getFunzione3() {
        return funzione3;
    }// end of getter method

    public void setFunzione3(FunzioneAmb funzione3) {
        this.funzione3 = funzione3;
    }//end of setter method

    public FunzioneAmb getFunzione4() {
        return funzione4;
    }// end of getter method

    public void setFunzione4(FunzioneAmb funzione4) {
        this.funzione4 = funzione4;
    }//end of setter method

    public VolontarioAmb getMilite_funzione1() {
        return milite_funzione1;
    }// end of getter method

    public void setMilite_funzione1(VolontarioAmb milite_funzione1) {
        this.milite_funzione1 = milite_funzione1;
    }//end of setter method

    public VolontarioAmb getMilite_funzione2() {
        return milite_funzione2;
    }// end of getter method

    public void setMilite_funzione2(VolontarioAmb milite_funzione2) {
        this.milite_funzione2 = milite_funzione2;
    }//end of setter method

    public VolontarioAmb getMilite_funzione3() {
        return milite_funzione3;
    }// end of getter method

    public void setMilite_funzione3(VolontarioAmb milite_funzione3) {
        this.milite_funzione3 = milite_funzione3;
    }//end of setter method

    public VolontarioAmb getMilite_funzione4() {
        return milite_funzione4;
    }// end of getter method

    public void setMilite_funzione4(VolontarioAmb milite_funzione4) {
        this.milite_funzione4 = milite_funzione4;
    }//end of setter method

    public Timestamp getModifica_funzione1() {
        return modifica_funzione1;
    }// end of getter method

    public void setModifica_funzione1(Timestamp modifica_funzione1) {
        this.modifica_funzione1 = modifica_funzione1;
    }//end of setter method

    public Timestamp getModifica_funzione2() {
        return modifica_funzione2;
    }// end of getter method

    public void setModifica_funzione2(Timestamp modifica_funzione2) {
        this.modifica_funzione2 = modifica_funzione2;
    }//end of setter method

    public Timestamp getModifica_funzione3() {
        return modifica_funzione3;
    }// end of getter method

    public void setModifica_funzione3(Timestamp modifica_funzione3) {
        this.modifica_funzione3 = modifica_funzione3;
    }//end of setter method

    public Timestamp getModifica_funzione4() {
        return modifica_funzione4;
    }// end of getter method

    public void setModifica_funzione4(Timestamp modifica_funzione4) {
        this.modifica_funzione4 = modifica_funzione4;
    }//end of setter method

    public int getOre_milite1() {
        return ore_milite1;
    }// end of getter method

    public void setOre_milite1(int ore_milite1) {
        this.ore_milite1 = ore_milite1;
    }//end of setter method

    public int getOre_milite2() {
        return ore_milite2;
    }// end of getter method

    public void setOre_milite2(int ore_milite2) {
        this.ore_milite2 = ore_milite2;
    }//end of setter method

    public int getOre_milite3() {
        return ore_milite3;
    }// end of getter method

    public void setOre_milite3(int ore_milite3) {
        this.ore_milite3 = ore_milite3;
    }//end of setter method

    public int getOre_milite4() {
        return ore_milite4;
    }// end of getter method

    public void setOre_milite4(int ore_milite4) {
        this.ore_milite4 = ore_milite4;
    }//end of setter method

    public boolean isProblemi_funzione1() {
        return problemi_funzione1;
    }// end of getter method

    public void setProblemi_funzione1(boolean problemi_funzione1) {
        this.problemi_funzione1 = problemi_funzione1;
    }//end of setter method

    public boolean isProblemi_funzione2() {
        return problemi_funzione2;
    }// end of getter method

    public void setProblemi_funzione2(boolean problemi_unzione2) {
        this.problemi_funzione2 = problemi_unzione2;
    }//end of setter method

    public boolean isProblemi_funzione3() {
        return problemi_funzione3;
    }// end of getter method

    public void setProblemi_funzione3(boolean problemi_funzione3) {
        this.problemi_funzione3 = problemi_funzione3;
    }//end of setter method

    public boolean isProblemi_funzione4() {
        return problemi_funzione4;
    }// end of getter method

    public void setProblemi_funzione4(boolean problemi_funzione4) {
        this.problemi_funzione4 = problemi_funzione4;
    }//end of setter method

    public String getTitolo_extra() {
        return titolo_extra;
    }// end of getter method

    public void setTitolo_extra(String titolo_extra) {
        this.titolo_extra = titolo_extra;
    }//end of setter method

    public String getLocalità_extra() {
        return località_extra;
    }// end of getter method

    public void setLocalità_extra(String località_extra) {
        this.località_extra = località_extra;
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

}// end of entity class
