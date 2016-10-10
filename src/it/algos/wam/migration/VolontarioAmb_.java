package it.algos.wam.migration;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

/**
 * Created by gac on 08 ott 2016.
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 */
public class VolontarioAmb_ {
    public static volatile SingularAttribute<VolontarioAmb, CroceAmb> croce;
    public static volatile SingularAttribute<VolontarioAmb, String> nome;
    public static volatile SingularAttribute<VolontarioAmb, String> cognome;
    public static volatile SingularAttribute<VolontarioAmb, String> telefonoCellulare;
    public static volatile SingularAttribute<VolontarioAmb, String> telefonoFisso;
    public static volatile SingularAttribute<VolontarioAmb, String> email;
    public static volatile SingularAttribute<VolontarioAmb, Date> dataNascita;
    public static volatile SingularAttribute<VolontarioAmb, String> note;
    public static volatile SingularAttribute<VolontarioAmb, Boolean> admin;
    public static volatile SingularAttribute<VolontarioAmb, Boolean> dipendente;
    public static volatile SingularAttribute<VolontarioAmb, Boolean> attivo;
    public static volatile SingularAttribute<VolontarioAmb, Integer> oreAnno;
    public static volatile SingularAttribute<VolontarioAmb, Integer> turniAnno;
    public static volatile SingularAttribute<VolontarioAmb, Integer> oreExtra;
}// end of entity class
