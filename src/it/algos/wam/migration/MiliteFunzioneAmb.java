package it.algos.wam.migration;

import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import java.util.List;

/**
 * Created by gac on 10 ott 2016.
 * Entity per il ruolo di un utente
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "MiliteFunzione")
@Access(AccessType.PROPERTY)
@ReadOnly
public class MiliteFunzioneAmb extends BaseEntity {

    private long funzione_id = 0;
    private long milite_id = 0;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public MiliteFunzioneAmb() {
    }// end of constructor


    /**
     * Search for the values of a given property of the given Entity class
     * Usa l'EntityManager passato come parametro
     *
     * @param manager the EntityManager to use
     */
    @SuppressWarnings("all")
    public static List<MiliteFunzioneAmb> getListByMilite(long keyMiliteID, EntityManager manager) {
        return (List<MiliteFunzioneAmb>) AQuery.getList(MiliteFunzioneAmb.class, MiliteFunzioneAmb_.milite_id, keyMiliteID, manager);
    }// end of static method


    public long getFunzione_id() {
        return funzione_id;
    }// end of getter method

    public void setFunzione_id(long funzione_id) {
        this.funzione_id = funzione_id;
    }//end of setter method

    public long getMilite_id() {
        return milite_id;
    }// end of getter method

    public void setMilite_id(long milite_id) {
        this.milite_id = milite_id;
    }//end of setter method

}// end of entity class
