package it.algos.wam.migration;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by gac on 08 ott 2016.
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 */
public class UtenteAmb_ {
    public static volatile SingularAttribute<UtenteAmb, CroceAmb> croce;
    public static volatile SingularAttribute<UtenteAmb, VolontarioAmb> milite;
    public static volatile SingularAttribute<UtenteAmb, String> username;
    public static volatile SingularAttribute<UtenteAmb, String> nickname;
    public static volatile SingularAttribute<UtenteAmb, String> password;
    public static volatile SingularAttribute<UtenteAmb, String> pass;
    public static volatile SingularAttribute<UtenteAmb, Boolean> enabled;
    public static volatile SingularAttribute<UtenteAmb, Boolean> accountExpired;
    public static volatile SingularAttribute<UtenteAmb, Boolean> accountLocked;
    public static volatile SingularAttribute<UtenteAmb, Boolean> passwordExpired;
}// end of entity class
