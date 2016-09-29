package it.algos.wam.migration;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import java.util.List;


/**
 * Created by gac on 27 ago 2016.
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
@Table(name = "Funzione")
@Access(AccessType.PROPERTY)
@ReadOnly
public class FunzioneAmb extends MigrationEntity {

    @ManyToOne
    private Croce croce;

    private String sigla;
    private String descrizione;
    private int ordine;
    private String sigla_visibile;
    private String funzioni_dipendenti;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public FunzioneAmb() {
    }// end of constructor



    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
     *
     * @param id      valore (unico) della Primary Key
     * @return istanza della Entity, null se non trovata
     */
    public static FunzioneAmb find(long id) {
        return (FunzioneAmb) AQuery.find(FunzioneAmb.class, id, getManager());
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query per una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza
     * @param sigla   valore della property code
     * @return istanza della Entity, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static FunzioneAmb findByCompanyAndSigla(Croce company, String sigla) {
        FunzioneAmb instance = null;
        BaseEntity entity = null;
        EntityManager manager = getManager();
        List<FunzioneAmb> entities;

        Container.Filter filterA = new Compare.Equal(FunzioneAmb_.croce.getName(), company);
        Container.Filter filterB = new Compare.Equal(FunzioneAmb_.sigla.getName(), sigla);
        entities = (List<FunzioneAmb>) AQuery.getList(FunzioneAmb.class, null, manager, filterA, filterB);
        manager.close();

        if (entities != null && entities.size() == 1) {
            entity = entities.get(0);
        }// end of if cycle

        if (entity != null) {
            instance = (FunzioneAmb) entity;
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
    public static List<FunzioneAmb> findAll() {
        List<FunzioneAmb> lista;
        EntityManager manager = getManager();

        lista = (List<FunzioneAmb>) AQuery.findAll(FunzioneAmb.class, manager);
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
    public static List<FunzioneAmb> findAll(Croce company) {
        List<FunzioneAmb> lista;
        EntityManager manager = getManager();

        lista = (List<FunzioneAmb>) AQuery.findAll(FunzioneAmb.class, FunzioneAmb_.croce, company, manager);
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

    public String getSigla_visibile() {
        return sigla_visibile;
    }// end of getter method

    public void setSigla_visibile(String sigla_visibile) {
        this.sigla_visibile = sigla_visibile;
    }//end of setter method

    public String getFunzioni_dipendenti() {
        return funzioni_dipendenti;
    }// end of getter method

    public void setFunzioni_dipendenti(String funzioni_dipendenti) {
        this.funzioni_dipendenti = funzioni_dipendenti;
    }//end of setter method

}// end of entity class
