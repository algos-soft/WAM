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
 * Entity per una funzione
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
public class ServizioAmb extends MigrationEntity {

    @ManyToOne
    private Croce croce;

    private String sigla;
    private String descrizione;
    private int ordine;
    private int ora_inizio;
    private int minuti_inizio;
    private int ora_fine;
    private int minuti_fine;
    private boolean orario;
    private boolean primo;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public ServizioAmb() {
    }// end of constructor


    /**
     * Recupera una istanza della Entity usando la query per una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza
     * @param sigla   valore della property code
     * @return istanza della Entity, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static ServizioAmb findByCompanyAndSigla(Croce company, String sigla) {
        ServizioAmb instance = null;
        BaseEntity entity = null;
        EntityManager manager = getManager();
        List<ServizioAmb> entities;

        Container.Filter filterA = new Compare.Equal(ServizioAmb_.croce.getName(), company);
        Container.Filter filterB = new Compare.Equal(ServizioAmb_.sigla.getName(), sigla);
        entities = (List<ServizioAmb>) AQuery.findAll(ServizioAmb.class, null, manager, filterA, filterB);
        manager.close();

        if (entities != null && entities.size() == 1) {
            entity = entities.get(0);
        }// end of if cycle

        if (entity != null) {
            instance = (ServizioAmb) entity;
        }// end of if cycle

        return instance;
    }// end of method


    /**
     * Recupera una lista di tutti i records della Entity
     * Nessun filtro sulla company
     *
     * @return lista di tutte le istanze della Entity
     */
    @SuppressWarnings("unchecked")
    public static List<ServizioAmb> findAll() {
        List<ServizioAmb> lista;
        EntityManager manager = getManager();

        lista = (List<ServizioAmb>) AQuery.findAll(ServizioAmb.class, manager);
        manager.close();

        return lista;
    }// end of method


    /**
     * Recupera una lista di tutti i records della Entity
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza
     * @return lista delle istanze filtrate della Entity
     */
    @SuppressWarnings("unchecked")
    public static List<ServizioAmb> findAll(Croce company) {
        List<ServizioAmb> lista;
        EntityManager manager = getManager();

        lista = (List<ServizioAmb>) AQuery.findAll(ServizioAmb.class, ServizioAmb_.croce, company, manager);
        manager.close();

        return lista;
    }// end of method


    public Croce getCroce() {
        return croce;
    }// end of getter method

    public void setCroce(Croce croce) {
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

}// end of entity class
