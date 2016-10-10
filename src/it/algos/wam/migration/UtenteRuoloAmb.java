package it.algos.wam.migration;


import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.SortProperty;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 09 ott 2016.
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
@Table(name = "Utente_ruolo")
@Access(AccessType.PROPERTY)
@ReadOnly
public class UtenteRuoloAmb extends BaseEntity {


    private long ruolo_id = 0;
    private long utente_id = 0;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public UtenteRuoloAmb() {
    }// end of constructor

    /**
     * Recupera una istanza della Entity usando la query per una property specifica
     *
     * @return istanza della Entity, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static boolean isAdmin(UtenteAmb utente, EntityManager manager) {
        boolean admin = false;
        long keyUtente = 0;
        Query query;
        int num = 0;
        String queryText = "select (ruolo_id) from utente_ruolo";

        if (manager == null) {
            return false;
        }// end of if cycle

        if (utente != null) {
            keyUtente = utente.getId();
        }// end of if cycle

        if (keyUtente > 0) {
//            queryText += keyUtente;
//            query = manager.createQuery(queryText);
//         Object alfa=   query.getResultList();
            int a = 87;


//            Object beta = AQuery.getList(UtenteRuoloAmb.class, UtenteRuoloAmb_.utente_id, keyUtente, manager);

            SortProperty sorts = new SortProperty("ruolo_id");
//            Object alfa = AQuery.getList(UtenteRuoloAmb.class, null, null, sorts, manager);
            Object alfa = getList(UtenteRuoloAmb.class, manager);
            int ac = 87;
        }// end of if cycle

        return admin;
    }// end of method


    public static List<? extends BaseEntity> getList(Class<? extends Object> clazz, EntityManager manager) {
        ArrayList<BaseEntity> entities = new ArrayList<>();
        JPAContainer<BaseEntity> container;
        EntityItem<BaseEntity> item;

        // se non specificato l'EntityManager, esce
        if (manager == null) {
            return null;
        }// end of if cycle

        // create a read-only JPA container for a given domain class (eventually sorted) and filters (eventually)
        container = getContainerRead(clazz, manager);

        // costruisce la lista, spazzolando il container
        for (Object id : container.getItemIds()) {
            item = container.getItem(id);
            entities.add(item.getEntity());
        }// end of for cycle

        return entities;
    }// end of static method


    public static JPAContainer<BaseEntity> getContainerRead(Class<? extends Object> clazz, EntityManager manager) {
        JPAContainer<BaseEntity> container = (JPAContainer<BaseEntity>) JPAContainerFactory.makeNonCachedReadOnly(clazz, manager);

        return container;
    }// end of static method

    public long getRuolo_id() {
        return ruolo_id;
    }// end of getter method

    public void setRuolo_id(long ruolo_id) {
        this.ruolo_id = ruolo_id;
    }//end of setter method

    public long getUtente_id() {
        return utente_id;
    }// end of getter method

    public void setUtente_id(long utente_id) {
        this.utente_id = utente_id;
    }//end of setter method

}// end of entity class
