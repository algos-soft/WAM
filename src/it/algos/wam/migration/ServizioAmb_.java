package it.algos.wam.migration;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by gac on 01 ago 2016.
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 */
public class ServizioAmb_ {
    public static volatile SingularAttribute<ServizioAmb, Croce> croce;
    public static volatile SingularAttribute<ServizioAmb, String> sigla;
    public static volatile SingularAttribute<ServizioAmb, String> descrizione;
    public static volatile SingularAttribute<ServizioAmb, Integer> ordine;
    public static volatile SingularAttribute<ServizioAmb, Integer> ora_inizio;
    public static volatile SingularAttribute<ServizioAmb, Integer> minuti_inizio;
    public static volatile SingularAttribute<ServizioAmb, Integer> ora_fine;
    public static volatile SingularAttribute<ServizioAmb, Integer> minuti_fine;
    public static volatile SingularAttribute<ServizioAmb, Boolean> orario;
    public static volatile SingularAttribute<ServizioAmb, Boolean> primo;
}// end of entity class
