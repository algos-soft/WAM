package it.algos.wam.migration;

import it.algos.webbase.web.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by gac on 30 ago 2016.
 * Superclasse per costruire il manager specifico di questo database
 */
public class MigrationEntity extends BaseEntity {


    private final static String PERSISTENCE_UNIT_NAME = "Webambulanzelocal";


    /**
     * Creazione di un manager specifico
     * DEVE essere chiuso (must be close by caller method)
     *
     * @return manager specifico
     */
    protected static EntityManager getManager() {
        EntityManager manager = null;
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        if (factory != null) {
            manager = factory.createEntityManager();
        }// end of if cycle

        return manager;
    }// end of method

}// end of entity class
