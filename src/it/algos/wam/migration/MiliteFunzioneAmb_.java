package it.algos.wam.migration;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by gac on 10 ott 2016.
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 */
public class MiliteFunzioneAmb_ {
    public static volatile SingularAttribute<MiliteFunzioneAmb, Long> funzione_id;
    public static volatile SingularAttribute<MiliteFunzioneAmb, Long> milite_id;
}// end of entity class
