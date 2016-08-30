package it.algos.wam.migration;

import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity per una funzione
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@ReadOnly
public class Croce  extends MigrationEntity{

    private String sigla;
    private String descrizione;
    private String presidente;
    private String indirizzo;
    private String organizzazione;

//    @OneToMany(mappedBy = "croce", cascade = CascadeType.ALL, orphanRemoval = true)
//    @CascadeOnDelete
//    private List<FunzioneAmb> funzioni = new ArrayList();

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public Croce() {
    }// end of constructor

    /**
     * Recupera una istanza della Entity usando la query per una property specifica
     *
     * @param sigla valore della property code
     * @return istanza della Entity, null se non trovata
     */
    public static Croce findByCode(String sigla) {
        Croce instance = null;

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Webambulanzelocal");
        EntityManager manager = factory.createEntityManager();
        BaseEntity entity = AQuery.queryOne(Croce.class, Croce_.sigla, sigla, manager);
        manager.close();

        if (entity != null) {
            if (entity instanceof Croce) {
                instance = (Croce) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method


    /**
     * Recupera una lista (array) di TUTTI i records della Entity
     *
     * @return lista di tutte le istanze della Entity
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Croce> findAll() {
        ArrayList<Croce> lista;
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Webambulanzelocal");
        EntityManager manager = factory.createEntityManager();

        lista = (ArrayList<Croce>) AQuery.getLista(Croce.class, manager);
        manager.close();

        return lista;
    }// end of method


    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getPresidente() {
        return presidente;
    }

    public void setPresidente(String presidente) {
        this.presidente = presidente;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }// end of method

    public String getOrganizzazione() {
        return organizzazione;
    }// end of getter method

    public void setOrganizzazione(String organizzazione) {
        this.organizzazione = organizzazione;
    }//end of setter method

}// end of entity class
