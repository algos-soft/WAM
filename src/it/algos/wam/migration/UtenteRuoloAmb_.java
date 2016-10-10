package it.algos.wam.migration;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

/**
 * Created by gac on 09 ott 2016.
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 */
public class UtenteRuoloAmb_ {
    public static volatile SingularAttribute<UtenteRuoloAmb, Long> ruolo_id;
    public static volatile SingularAttribute<UtenteRuoloAmb, Long> utente_id;
}// end of entity class
