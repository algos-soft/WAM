package it.algos.wam.migration;

import javax.persistence.metamodel.SingularAttribute;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by gac on 11 ott 2016.
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 */
public class TurnoAmb_ {
    public static volatile SingularAttribute<TurnoAmb, CroceAmb> croce;
    public static volatile SingularAttribute<TurnoAmb, ServizioAmb> tipo_turno;
    public static volatile SingularAttribute<TurnoAmb, String> sigla;
    public static volatile SingularAttribute<TurnoAmb, Date> giorno;
    public static volatile SingularAttribute<TurnoAmb, Date> inizio;
    public static volatile SingularAttribute<TurnoAmb, Date> fine;

    public static volatile SingularAttribute<TurnoAmb, FunzioneAmb> funzione1;
    public static volatile SingularAttribute<TurnoAmb, FunzioneAmb> funzione2;
    public static volatile SingularAttribute<TurnoAmb, FunzioneAmb> funzione3;
    public static volatile SingularAttribute<TurnoAmb, FunzioneAmb> funzione4;

    public static volatile SingularAttribute<TurnoAmb, VolontarioAmb> milite_funzione1;
    public static volatile SingularAttribute<TurnoAmb, VolontarioAmb> milite_funzione2;
    public static volatile SingularAttribute<TurnoAmb, VolontarioAmb> milite_funzione3;
    public static volatile SingularAttribute<TurnoAmb, VolontarioAmb> milite_funzione4;

    public static volatile SingularAttribute<TurnoAmb, Timestamp> modifica_funzione1;
    public static volatile SingularAttribute<TurnoAmb, Timestamp> modifica_funzione2;
    public static volatile SingularAttribute<TurnoAmb, Timestamp> modifica_funzione3;
    public static volatile SingularAttribute<TurnoAmb, Timestamp> modifica_funzione4;

    public static volatile SingularAttribute<TurnoAmb, Integer> ore_milite1;
    public static volatile SingularAttribute<TurnoAmb, Integer> ore_milite2;
    public static volatile SingularAttribute<TurnoAmb, Integer> ore_milite3;
    public static volatile SingularAttribute<TurnoAmb, Integer> ore_milite4;

    public static volatile SingularAttribute<TurnoAmb, Boolean> problemi_funzione1;
    public static volatile SingularAttribute<TurnoAmb, Boolean> problemi_funzione2;
    public static volatile SingularAttribute<TurnoAmb, Boolean> problemi_funzione3;
    public static volatile SingularAttribute<TurnoAmb, Boolean> problemi_funzione4;

    public static volatile SingularAttribute<TurnoAmb, String> titolo_extra;
    public static volatile SingularAttribute<TurnoAmb, String> località_extra;
    public static volatile SingularAttribute<TurnoAmb, String> note;
    public static volatile SingularAttribute<TurnoAmb, Boolean> assegnato;
}// end of entity class
