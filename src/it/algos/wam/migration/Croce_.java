package it.algos.wam.migration;

import it.algos.wam.entity.wamcompany.Organizzazione;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by gac on 01 ago 2016.
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 */
public class Croce_ {
    public static volatile SingularAttribute<Croce, String> sigla;
    public static volatile SingularAttribute<Croce, String> descrizione;
    public static volatile SingularAttribute<Croce, String> presidente;
    public static volatile SingularAttribute<Croce, String> indirizzo;
    public static volatile SingularAttribute<Croce, Organizzazione> organizzazione;
}// end of entity class
