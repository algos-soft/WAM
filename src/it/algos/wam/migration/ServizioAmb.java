package it.algos.wam.migration;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import java.util.List;

/**
 * Created by gac on 30 ago 2016.
 * <p>
 * Entity per un servizio
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "Tipo_turno")
@Access(AccessType.PROPERTY)
@ReadOnly
public class ServizioAmb extends BaseEntity {

    @ManyToOne
    private CroceAmb croce;

    private String sigla;
    private String descrizione;
    private int ordine;
    private int ora_inizio;
    private int minuti_inizio;
    private int ora_fine;
    private int minuti_fine;
    private boolean orario;
    private boolean primo;
    private boolean visibile;

    private long funzione1_id;
    private long funzione2_id;
    private long funzione3_id;
    private long funzione4_id;

    private int funzioni_obbligatorie;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public ServizioAmb() {
    }// end of constructor


    /**
     * Recupera una lista di tutti i records della Entity
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza
     * @return lista delle istanze filtrate della Entity
     */
    @SuppressWarnings("unchecked")
    public static List<ServizioAmb> findAll(CroceAmb company, EntityManager manager) {
        List<ServizioAmb> lista = null;

        if (manager != null) {
            lista = (List<ServizioAmb>) AQuery.getList(ServizioAmb.class, ServizioAmb_.croce, company, manager);
        }// end of if cycle

        return lista;
    }// end of method


    public CroceAmb getCroce() {
        return croce;
    }// end of getter method

    public void setCroce(CroceAmb croce) {
        this.croce = croce;
    }//end of setter method

    public String getSigla() {
        return sigla;
    }// end of getter method

    public void setSigla(String sigla) {
        this.sigla = sigla;
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

    public int getOra_inizio() {
        return ora_inizio;
    }// end of getter method

    public void setOra_inizio(int ora_inizio) {
        this.ora_inizio = ora_inizio;
    }//end of setter method

    public int getMinuti_inizio() {
        return minuti_inizio;
    }// end of getter method

    public void setMinuti_inizio(int minuti_inizio) {
        this.minuti_inizio = minuti_inizio;
    }//end of setter method

    public int getOra_fine() {
        return ora_fine;
    }// end of getter method

    public void setOra_fine(int ora_fine) {
        this.ora_fine = ora_fine;
    }//end of setter method

    public int getMinuti_fine() {
        return minuti_fine;
    }// end of getter method

    public void setMinuti_fine(int minuti_fine) {
        this.minuti_fine = minuti_fine;
    }//end of setter method

    public boolean isOrario() {
        return orario;
    }// end of getter method

    public void setOrario(boolean orario) {
        this.orario = orario;
    }//end of setter method

    public boolean isPrimo() {
        return primo;
    }// end of getter method

    public void setPrimo(boolean primo) {
        this.primo = primo;
    }//end of setter method

    public long getFunzione1_id() {
        return funzione1_id;
    }// end of getter method

    public void setFunzione1_id(long funzione1_id) {
        this.funzione1_id = funzione1_id;
    }//end of setter method

    public long getFunzione2_id() {
        return funzione2_id;
    }// end of getter method

    public void setFunzione2_id(long funzione2_id) {
        this.funzione2_id = funzione2_id;
    }//end of setter method

    public long getFunzione3_id() {
        return funzione3_id;
    }// end of getter method

    public void setFunzione3_id(long funzione3_id) {
        this.funzione3_id = funzione3_id;
    }//end of setter method

    public long getFunzione4_id() {
        return funzione4_id;
    }// end of getter method

    public void setFunzione4_id(long funzione4_id) {
        this.funzione4_id = funzione4_id;
    }//end of setter method

    public int getFunzioni_obbligatorie() {
        return funzioni_obbligatorie;
    }// end of getter method

    public void setFunzioni_obbligatorie(int funzioni_obbligatorie) {
        this.funzioni_obbligatorie = funzioni_obbligatorie;
    }//end of setter method

    public boolean isVisibile() {
        return visibile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }
}// end of entity class
