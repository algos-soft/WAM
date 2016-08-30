package it.algos.wam.migration;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by gac on 01 ago 2016.
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 */
public class FunzioneAmb_ {
    public static volatile SingularAttribute<FunzioneAmb, Croce> croce;
    public static volatile SingularAttribute<FunzioneAmb, String> sigla;
    public static volatile SingularAttribute<FunzioneAmb, String> descrizione;
    public static volatile SingularAttribute<FunzioneAmb, Integer> ordine;
    public static volatile SingularAttribute<FunzioneAmb, String> sigla_visibile;
    public static volatile SingularAttribute<FunzioneAmb, String> funzioni_dipendenti;
}// end of entity class
